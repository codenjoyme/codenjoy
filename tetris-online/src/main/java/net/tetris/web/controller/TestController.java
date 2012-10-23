package net.tetris.web.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * User: serhiy.zelenin
 * Date: 10/22/12
 * Time: 10:10 PM
 */
@Controller
@RequestMapping("/cookies")
public class TestController {
    private static Logger logger = LoggerFactory.getLogger(TestController.class);

    @RequestMapping(method = RequestMethod.GET)
    public String openRegistrationForm(HttpServletResponse response, HttpServletRequest request) {
        response.addCookie(new Cookie("lalala", "bububu"));
        Cookie[] cookies = request.getCookies();
        for (Cookie cookie : cookies) {
            logger.info("{} : {}", cookie.getName(), cookie.getValue());
        }
        return "test";
    }

}
