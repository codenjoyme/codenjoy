package com.codenjoy.dojo.transport.ws;

import javax.servlet.http.HttpServletRequest;

public class DefaultAuthenticationService implements AuthenticationService {
    @Override
    public String authenticate(HttpServletRequest request) {
        return request.getParameter("user");
    }
}
