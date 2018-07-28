package com.spockatone.spock.web.servlet;

import com.spockatone.spock.entity.Lot;
import com.spockatone.spock.service.BetService;
import com.spockatone.spock.service.LotService;
import com.spockatone.spock.web.templater.ThymeleafPageGenerator;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.WebContext;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class LotServlet extends HttpServlet {
    private TemplateEngine templateEngine = ThymeleafPageGenerator.getInstance().getTemplateEngine();
    private LotService lotService;
    private BetService betService;

    public LotServlet(LotService lotService, BetService betService) {
        this.lotService = lotService;
        this.betService = betService;
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        WebContext context = new WebContext(request, response, request.getServletContext(), request.getLocale());
        Map<String, Object> pageVariables = new HashMap<>();
        response.setContentType("text/html;charset=utf-8");

        int lotId = Integer.parseInt(request.getParameter("id"));

        Lot lot = lotService.getLotById(lotId);
        pageVariables.put("lot", lot);

        pageVariables.put("step", betService.getStep()*lot.getStartPrice()/100);

        context.setVariables(pageVariables);

        String htmlString = templateEngine.process("lot", context);
        response.getWriter().println(htmlString);
    }
}
