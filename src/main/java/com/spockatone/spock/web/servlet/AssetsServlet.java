package com.spockatone.spock.web.servlet;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

public class AssetsServlet extends HttpServlet {
    private static final int RESOURCE_START_INDEX = 1;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String resourcePath = req.getRequestURI().substring(RESOURCE_START_INDEX);
        try (InputStream resourceAsStream = getClass().getClassLoader().getResourceAsStream(resourcePath)){
            byte[] buffer = new byte[8192];
            int count;
            while ((count = resourceAsStream.read(buffer)) != -1){
                resp.getOutputStream().write(buffer,0,count);
            }
        }
    }
}