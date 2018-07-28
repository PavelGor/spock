package com.spockatone.spock.web.servlet;

import com.spockatone.spock.entity.Lot;
import com.spockatone.spock.service.LotService;
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

public class LotsServlet extends HttpServlet {
    private TemplateEngine templateEngine = ThymeleafPageGenerator.getInstance().getTemplateEngine();
    private LotService lotService;

    public LotsServlet(LotService lotService) {
        this.lotService = lotService;
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        WebContext context = new WebContext(request, response, request.getServletContext(), request.getLocale());
        Map<String, Object> pageVariables = new HashMap<>();
        response.setContentType("text/html;charset=utf-8");

        //TODO pagination

        List<Lot> lotList = lotService.getLotsByPage(1,6); //TODO: pageNumber, itemsOnPage
        pageVariables.put("lotList", lotList);

        context.setVariables(pageVariables);

        String htmlString = templateEngine.process("lots", context);
        response.getWriter().println(htmlString);
    }
}
