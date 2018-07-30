package com.spockatone.spock.web.servlet.security;

import com.spockatone.spock.entity.User;
import com.spockatone.spock.service.UserService;
import com.spockatone.spock.service.security.SecurityService;
import com.spockatone.spock.service.security.Session;
import com.spockatone.spock.web.templater.ThymeleafPageGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.WebContext;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;

public class LoginServlet  extends HttpServlet {
    private static final Logger LOG = LoggerFactory.getLogger(LoginServlet.class);
    private UserService userService;
    private SecurityService securityService;
    private TemplateEngine templateEngine = ThymeleafPageGenerator.getInstance().getTemplateEngine();

    public LoginServlet(UserService userService, SecurityService securityService) {
        this.userService = userService;
        this.securityService = securityService;
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        WebContext context = new WebContext(request, response, request.getServletContext(),
                request.getLocale());
        response.setContentType("text/html;charset=utf-8");
        String htmlString = templateEngine.process("login", context);
        response.getWriter().println(htmlString);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("text/html;charset=utf-8");
        String login = request.getParameter("login");
        String password = request.getParameter("password");

        try {
            String token;
            User user = userService.authenticate(login, password);
            Optional<Session> optionalSession = securityService.getSession(user);

            if(optionalSession.isPresent()){
                token = optionalSession.get().getToken();
            } else {
                token = securityService.createSession(user);
            }

            Cookie cookie = new Cookie("user-token", token);
            cookie.setMaxAge(securityService.getSessionMaxLifeTime());
            response.addCookie(cookie);
            response.sendRedirect("/");
            LOG.info("User: {} logged in", user.getUserName());
        } catch (SecurityException e) {
            response.sendRedirect("/login");
        }
    }
}