package net.tetris.online.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * User: serhiy.zelenin
 * Date: 10/23/12
 * Time: 7:52 PM
 */
@Component("securityFilter")
public class SecurityFilter implements Filter {
    private String loginUrl;

    private String cookiePrefix;
    private String skipPattern;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest servletRequest = (HttpServletRequest) request;
        if (skipPattern!=null && servletRequest.getRequestURI()!=null && servletRequest.getRequestURI().equals(skipPattern)) {
            chain.doFilter(request, response);
            return;
        }

        String cookieValue = findAuthCookie((HttpServletRequest) request);
        if (cookieValue == null) {
            HttpServletResponse servletResponse = (HttpServletResponse) response;
            servletResponse.sendRedirect(loginUrl);
        } else {
            request.setAttribute("logged.user", getUserName(cookieValue));
            chain.doFilter(request, response);
        }
    }

    private String getUserName(String cookieValue) {
        return cookieValue.split("\\|")[0];
    }

    private String findAuthCookie(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if (cookies == null) {
            return null;
        }
        for (Cookie cookie : cookies) {
            if (cookie.getName().startsWith(cookiePrefix)) {
                return cookie.getValue();
            }
        }
        return null;
    }

    @Override
    public void destroy() {
    }

    @Value("${login.url}")
    public void setLoginUrl(String loginUrl) {
        this.loginUrl = loginUrl;
    }

    @Value("${cookie.prefix}")
    public void setCookiePrefix(String cookiePrefix) {
        this.cookiePrefix = cookiePrefix;
    }

    @Value("${skip.security.pattern}")
    public void setSkipSecurityFilter(String skipPattern) {
        this.skipPattern = skipPattern;
    }
}
