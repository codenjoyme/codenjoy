package com.codenjoy.dojo.transport.ws;

import javax.servlet.http.HttpServletRequest;

/**
 * User: serhiy.zelenin
 * Date: 4/8/13
 * Time: 9:28 PM
 */
public interface AuthenticationService {
    String authenticate(HttpServletRequest request);
}
