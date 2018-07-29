package com.spockatone.spock.web.servlet;

import com.spockatone.spock.entity.Lot;
import com.spockatone.spock.entity.User;
import com.spockatone.spock.service.BetService;
import com.spockatone.spock.service.LotService;
import com.spockatone.spock.service.security.SecurityService;
import com.spockatone.spock.web.templater.ThymeleafPageGenerator;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.WebContext;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class LotServlet extends HttpServlet {
    private TemplateEngine templateEngine = ThymeleafPageGenerator.getInstance().getTemplateEngine();
    private LotService lotService;
    private BetService betService;
    private SecurityService securityService;

    public LotServlet(LotService lotService, BetService betService, SecurityService securityService) {
        this.lotService = lotService;
        this.betService = betService;
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

        int lotId = Integer.parseInt(request.getParameter("id"));

        Lot lot = lotService.getLotById(lotId);
        pageVariables.put("lot", lot);

        pageVariables.put("step", betService.getStep()*lot.getStartPrice()/100);
        pageVariables.put("winner", betService.getWinnerName(lotId));

        context.setVariables(pageVariables);

        String htmlString = templateEngine.process("lot", context);
        response.getWriter().println(htmlString);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        int lotId = Integer.parseInt(request.getParameter("id"));
        Lot lot = lotService.getLotById(lotId);

        Optional<User> optionalUser = securityService.getUser(securityService.getToken(request));
        if (optionalUser.isPresent()){
            User user = optionalUser.get();
            betService.makeBet(user.getId(), lotId, betService.getCurrentPrice(lot));
        }

        response.sendRedirect("/cabinet");
    }
}
