package com.spockatone.spock.web.filter;

import com.spockatone.spock.entity.User;
import com.spockatone.spock.service.security.SecurityService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;

public class SecurityFilter implements Filter {
    private static final Logger LOG = LoggerFactory.getLogger(SecurityFilter.class);
    private SecurityService securityService;

    public SecurityFilter(SecurityService securityService) {
        this.securityService = securityService;
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        HttpServletResponse httpServletResponse = (HttpServletResponse) response;

        boolean isAuth = false;
        Optional<User> optionalUser;

        String token = securityService.getToken(httpServletRequest);

        if (token != null) {
            optionalUser = securityService.getUser(token);
            if (optionalUser.isPresent()) {
                isAuth = true;
                LOG.info("Security: user {} got access rights to {}", optionalUser.get().getUserName(), httpServletRequest.getRequestURI());
            }
        }

        if (isAuth) {
            chain.doFilter(request, response);
        } else {
            httpServletResponse.sendRedirect("/login");
            LOG.info("Security: have no such user session");
        }
    }

    @Override
    public void init(FilterConfig filterConfig) {

    }

    @Override
    public void destroy() {

    }
}
