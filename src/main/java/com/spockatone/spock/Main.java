package com.spockatone.spock;

import com.spockatone.spock.dao.LotDao;
import com.spockatone.spock.dao.jdbc.JdbcLotDao;
import com.spockatone.spock.service.LotService;
import com.spockatone.spock.web.servlet.AssetsServlet;
import com.spockatone.spock.web.servlet.LotsServlet;
import org.apache.commons.dbcp.BasicDataSource;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Properties;

public class Main {
    private static final Logger LOG = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) throws Exception {

        Properties properties = new Properties();
        try {
            properties.load(Main.class.getClassLoader().getResourceAsStream("application.properties"));
        } catch (IOException e) {
            LOG.info("bad file or properties in it");
            throw new RuntimeException("Za4em dalwe gut? ", e);
        }

        BasicDataSource dataSource = new BasicDataSource();
        dataSource.setUrl(properties.getProperty("db.url"));
        dataSource.setUsername(properties.getProperty("db.login"));
        dataSource.setPassword(properties.getProperty("db.password"));
        dataSource.setDriverClassName(properties.getProperty("db.className"));

        LotDao lotDao = new JdbcLotDao(dataSource);
        LotService lotService = new LotService(lotDao);

        ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);

        context.addServlet(new ServletHolder(new LotsServlet(lotService)), "/");
        context.addServlet(new ServletHolder(new AssetsServlet()), "/assets/*");

        String portStr = System.getenv("PORT");
        int port = portStr == null ? 8080 : Integer.valueOf(portStr);

        Server server = new Server(port);
        server.setHandler(context);

        server.start();

        LOG.info("Main: server started");
    }
}
