package com.codenjoy.dojo.transport.ws;

import javax.servlet.http.HttpServletRequest;

/**
 * User: serhiy.zelenin
 * Date: 4/8/13
 * Time: 10:49 PM
 */
public class DefaultAuthenticationService implements AuthenticationService {

    @Override
    public String authenticate(HttpServletRequest request) {
        return request.getParameter("user");
    }
}
