package net.tetris.web.controller;

import net.tetris.online.service.SecurityFilter;
import net.tetris.services.RestScreenSender;
import net.tetris.services.UpdateRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.HttpRequestHandler;

import javax.servlet.AsyncContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collections;
import java.util.Set;

/**
 * User: serhiy.zelenin
 * Date: 10/28/12
 * Time: 6:54 PM
 */
public class ReplayScreenServlet implements HttpRequestHandler {

    @Autowired
    private RestScreenSender screenSender;

    public ReplayScreenServlet() {
    }

    @Override
    public void handleRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        AsyncContext asyncContext = request.startAsync();
        String loggedUser = (String) request.getAttribute(SecurityFilter.LOGGED_USER);
        Set<String> playersToUpdate = Collections.singleton(loggedUser);
        screenSender.scheduleUpdate(new UpdateRequest(asyncContext, false, playersToUpdate));
    }
}