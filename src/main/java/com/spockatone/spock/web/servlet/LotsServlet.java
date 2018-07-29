package com.spockatone.spock.web.servlet;

import com.spockatone.spock.entity.Lot;
import com.spockatone.spock.entity.User;
import com.spockatone.spock.service.LotService;
import com.spockatone.spock.service.security.SecurityService;
import com.spockatone.spock.web.templater.ThymeleafPageGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.WebContext;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class LotsServlet extends HttpServlet {
    private static final Logger LOG = LoggerFactory.getLogger(LotsServlet.class);

    private TemplateEngine templateEngine = ThymeleafPageGenerator.getInstance().getTemplateEngine();
    private LotService lotService;
    private SecurityService securityService;

    public LotsServlet(LotService lotService, SecurityService securityService) {
        this.lotService = lotService;
        this.securityService = securityService;
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        WebContext context = new WebContext(request, response, request.getServletContext(), request.getLocale());
        Map<String, Object> pageVariables = new HashMap<>();
        response.setContentType("text/html;charset=utf-8");

        Optional<User> optionalUser = securityService.getUser(securityService.getToken(request));
        if (optionalUser.isPresent()){
            User user = optionalUser.get();
            pageVariables.put("userName", user.getUserName());
        }

        String pageParameter = request.getParameter("page");
        int page;
        try { //TODO не проще псделать проверку на нулл и не захламлять? В смысле зачем именно исключение то?
            page = Integer.parseInt(pageParameter);
        } catch (NumberFormatException num) {
            page = 1;
            LOG.error(String.format("Wrong page number,  page =  %s", pageParameter), num);
        }
        List<Lot> lotList = lotService.getLotsByPage(page);
        int pages = lotService.getLotsPagesCount();
        pageVariables.put("lotList", lotList);
        pageVariables.put("pagesCount", pages);

        context.setVariables(pageVariables);

        String htmlString = templateEngine.process("lots", context);
        response.getWriter().println(htmlString);
    }
}
