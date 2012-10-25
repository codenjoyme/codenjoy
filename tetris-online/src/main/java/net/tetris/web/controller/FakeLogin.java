package net.tetris.web.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * User: serhiy.zelenin
 * Date: 10/22/12
 * Time: 10:10 PM
 */
@Controller
public class FakeLogin {
    private static Logger logger = LoggerFactory.getLogger(FakeLogin.class);
    private boolean fakeAvailable;
    private String cookiePrefix;

    @RequestMapping(method = RequestMethod.GET, value = "/fakelogin")
    public String fakeLogin(HttpServletResponse response, HttpServletRequest request) {
        if (!fakeAvailable) {
            throw new UnsupportedOperationException();
        }
        return "fakeLogin";
    }

    @RequestMapping(method = RequestMethod.POST, value = "/fakelogin")
    public String doLogin(HttpServletResponse response, @RequestParam("userName") String userName) {
        if (!fakeAvailable) {
            throw new UnsupportedOperationException();
        }
        Cookie cookie = new Cookie(cookiePrefix, userName + "|123|erasdfaweasdf");
        response.addCookie(cookie);
        return "redirect:/view/upload.jsp";
    }

    @Value("${fake.available}")
    public void setFakeAvailable(String fakeAvailable) {
        this.fakeAvailable = Boolean.parseBoolean(fakeAvailable);
    }

    @Value("${cookie.prefix}")
    public void setCookiePrefix(String cookiePrefix) {
        this.cookiePrefix = cookiePrefix;
    }
}
