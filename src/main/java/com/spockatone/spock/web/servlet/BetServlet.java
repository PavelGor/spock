package com.spockatone.spock.web.servlet;

import com.spockatone.spock.entity.Lot;
import com.spockatone.spock.entity.User;
import com.spockatone.spock.service.BetService;
import com.spockatone.spock.service.LotService;
import com.spockatone.spock.service.security.SecurityService;
import com.spockatone.spock.web.templater.ThymeleafPageGenerator;
import org.thymeleaf.TemplateEngine;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;

public class BetServlet extends HttpServlet {
    private TemplateEngine templateEngine = ThymeleafPageGenerator.getInstance().getTemplateEngine();
    private LotService lotService;
    private BetService betService;
    private SecurityService securityService;

    public BetServlet(LotService lotService, BetService betService, SecurityService securityService) {
        this.lotService = lotService;
        this.betService = betService;
        this.securityService = securityService;
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        Optional<User> optionalUser = securityService.getUser(securityService.getToken(request));
        if (optionalUser.isPresent()){
            User user = optionalUser.get();
            int lotId = Integer.parseInt(request.getParameter("id")); //TODO get from page
            Lot lot = lotService.getLotById(lotId);
            double price = betService.getPriceForBet(lot);
            betService.makeBet(user.getId(), lotId, price);
            response.sendRedirect("/cabinet");
        }

    }
}
