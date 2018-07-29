package com.spockatone.spock;

import com.spockatone.spock.dao.BetDao;
import com.spockatone.spock.dao.LotDao;
import com.spockatone.spock.dao.MessageDao;
import com.spockatone.spock.dao.UserDao;
import com.spockatone.spock.dao.jdbc.JdbcBetDao;
import com.spockatone.spock.dao.jdbc.JdbcLotDao;
import com.spockatone.spock.dao.jdbc.JdbcMessageDao;
import com.spockatone.spock.dao.jdbc.JdbcUserDao;
import com.spockatone.spock.service.BetService;
import com.spockatone.spock.service.LotService;
import com.spockatone.spock.service.MessageService;
import com.spockatone.spock.service.UserService;
import com.spockatone.spock.service.security.SecurityService;
import com.spockatone.spock.web.filter.SecurityFilter;
import com.spockatone.spock.web.servlet.AssetsServlet;
import com.spockatone.spock.web.servlet.CabinetServlet;
import com.spockatone.spock.web.servlet.BetServlet;
import com.spockatone.spock.web.servlet.LotServlet;
import com.spockatone.spock.web.servlet.LotsServlet;
import com.spockatone.spock.web.servlet.security.LoginServlet;
import com.spockatone.spock.web.servlet.security.LogoutServlet;
import org.apache.commons.dbcp.BasicDataSource;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.FilterHolder;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.DispatcherType;
import java.io.IOException;
import java.util.EnumSet;
import java.util.Properties;

public class Main {
    private static final Logger LOG = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) throws Exception {

        Properties properties = new Properties();
        try {
            properties.load(Main.class.getClassLoader().getResourceAsStream("application.properties"));
        } catch (IOException e) {
            LOG.info("bad file or properties in it");
            throw new RuntimeException("bad file or properties in it", e);
        }

        BasicDataSource dataSource = new BasicDataSource();
        dataSource.setUrl(properties.getProperty("db.url"));
        dataSource.setUsername(properties.getProperty("db.login"));
        dataSource.setPassword(properties.getProperty("db.password"));
        dataSource.setDriverClassName(properties.getProperty("db.className"));

        LotDao lotDao = new JdbcLotDao(dataSource);
        BetDao betDao = new JdbcBetDao(dataSource);
        UserDao userDao = new JdbcUserDao(dataSource);

        BetService betService = new BetService(betDao);
        betService.setStep(Integer.parseInt(properties.getProperty("step")));
        UserService userService = new UserService(userDao);
        SecurityService securityService = new SecurityService();
        securityService.setSessionMaxLifeTime(Integer.parseInt(properties.getProperty("sessionMaxLifeTime")));
        LotService lotService = new LotService(lotDao);
        lotService.setItemsPerPage(properties.getProperty("itemsPerPage"));

        MessageDao messageDao = new JdbcMessageDao(dataSource);
        MessageService messageService = new MessageService(messageDao);


        ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);

        context.addServlet(new ServletHolder(new LotsServlet(lotService, securityService)), "/");
        context.addServlet(new ServletHolder(new LoginServlet(userService, securityService)), "/login");
        context.addServlet(new ServletHolder(new LogoutServlet(securityService)), "/logout");
        context.addServlet(new ServletHolder(new LotServlet(lotService, betService, securityService)), "/lot/*");
        context.addServlet(new ServletHolder(new CabinetServlet(messageService, securityService)), "/cabinet");
        context.addServlet(new ServletHolder(new BetServlet(lotService, betService, securityService)), "/bet");
        context.addServlet(new ServletHolder(new AssetsServlet()), "/assets/*");

        context.addFilter(new FilterHolder(new SecurityFilter(securityService)), "/cabinet", EnumSet.of(DispatcherType.REQUEST));

        String portStr = System.getenv("PORT");
        int port = portStr == null ? 8080 : Integer.valueOf(portStr);

        Server server = new Server(port);
        server.setHandler(context);

        server.start();

        LOG.info("Main: server started");
    }
}
