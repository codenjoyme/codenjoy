package com.codenjoy.dojo.transport.ws;

import javax.servlet.http.HttpServletRequest;

public interface AuthenticationService {
    String authenticate(HttpServletRequest request);
}
