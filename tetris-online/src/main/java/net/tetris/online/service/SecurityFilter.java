package net.tetris.online.service;

import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.net.URLCodec;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
    private static Logger logger = LoggerFactory.getLogger(SecurityFilter.class);

    public static final String LOGGED_USER = "logged.user";

    private String loginUrl;

    private String cookiePrefix;
    private String skipPattern;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest servletRequest = (HttpServletRequest) request;
        if (skipPattern != null && servletRequest.getRequestURI() != null && servletRequest.getRequestURI().equals(skipPattern)) {
            chain.doFilter(request, response);
            return;
        }

        String cookieValue = findAuthCookie((HttpServletRequest) request);
        if (cookieValue == null) {
            HttpServletResponse servletResponse = (HttpServletResponse) response;
            servletResponse.sendRedirect(loginUrl);
        } else {
            request.setAttribute(LOGGED_USER, getUserName(cookieValue));
            chain.doFilter(request, response);
        }
    }

    private String getUserName(String cookieValue) {
        URLCodec urlCodec = new URLCodec();
        String decodedCookieValue = null;
        try {
            decodedCookieValue = urlCodec.decode(cookieValue);
        } catch (DecoderException e) {
            logger.error("Unable to decode user name from cookie " + cookieValue, e);
            throw new RuntimeException("Unable to decode user name from cookie " + cookieValue, e);
        }
        return decodedCookieValue.split("\\|")[0];
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
