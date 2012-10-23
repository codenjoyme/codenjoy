package net.tetris.online.service;

import net.tetris.online.service.SecurityFilter;
import org.junit.Before;
import org.junit.Test;
import org.springframework.mock.web.MockFilterChain;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import java.io.IOException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;

/**
 * User: serhiy.zelenin
 * Date: 10/23/12
 * Time: 8:03 PM
 */
public class SecurityFilterTest {

    public static final String LOGIN_URL = "http://login.url";
    public static final String COOKIE_PREFIX = "wordpress_logged_in";
    private SecurityFilter filter;
    private MockHttpServletRequest request;
    private MockFilterChain chain;
    private MockHttpServletResponse response;

    @Before
    public void setUp() throws Exception {
        filter = new SecurityFilter();
        request = new MockHttpServletRequest();
        chain = new MockFilterChain();
        response = new MockHttpServletResponse();
        filter.setLoginUrl(LOGIN_URL);
        filter.setCookiePrefix(COOKIE_PREFIX);
    }

    @Test
    public void shouldRedirectToLoginWhenEmptyCookies() throws IOException, ServletException {
        filter.doFilter(request, response, chain);

        assertEquals(LOGIN_URL, response.getRedirectedUrl());
    }

    @Test
    public void shouldRedirectToLoginWhenNoProperCookie() throws IOException, ServletException {
        request.setCookies(new Cookie("bla-bla", "123"));
        filter.doFilter(request, response, chain);

        assertEquals(LOGIN_URL, response.getRedirectedUrl());
    }

    @Test
    public void shouldPassWhenCookieSet() throws IOException, ServletException {
        request.setCookies(new Cookie(COOKIE_PREFIX+"bla-bla", "vasya|123|lalala"));

        filter.doFilter(request, response, chain);

        assertEquals("vasya", request.getAttribute("logged.user"));
        assertSame(request, chain.getRequest());
    }

    @Test
    public void shouldSkipLoginUrl() throws IOException, ServletException {
        filter.setSkipSecurityFilter("/fakelogin");
        request.setRequestURI("/fakelogin");

        filter.doFilter(request, response, chain);

        assertSame(request, chain.getRequest());
    }

    @Test
    public void shouldSkipProceedWithLoginWhenNullPathInfo() throws IOException, ServletException {
        filter.setSkipSecurityFilter("/fakelogin");

        filter.doFilter(request, response, chain);

        assertEquals(LOGIN_URL, response.getRedirectedUrl());
    }
}
