package com.spockatone.spock.web.servlet;

import com.spockatone.spock.entity.Message;
import com.spockatone.spock.entity.Type;
import com.spockatone.spock.entity.User;
import com.spockatone.spock.service.MessageService;
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

public class CabinetServlet extends HttpServlet {
    private static final Logger LOG = LoggerFactory.getLogger(CabinetServlet.class);

    private TemplateEngine templateEngine = ThymeleafPageGenerator.getInstance().getTemplateEngine();
    private MessageService messageService;
    private SecurityService securityService;

    public CabinetServlet(MessageService messageService, SecurityService securityService) {
        this.messageService = messageService;
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
            int userId = user.getId();
            List<Message> messages = messageService.getMessagesByUserId(userId);
                pageVariables.put("messages", messages);
            }

        context.setVariables(pageVariables);

        String htmlString = templateEngine.process("cabinet", context);
        response.getWriter().println(htmlString);
    }
}
