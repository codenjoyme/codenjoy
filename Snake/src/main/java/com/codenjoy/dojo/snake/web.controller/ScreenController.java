package com.codenjoy.dojo.snake.web.controller;

import com.codenjoy.dojo.snake.services.ScreenSender;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.HttpRequestHandler;

import javax.servlet.AsyncContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Set;

/**
 * User: oleksandr.baglai
 * Date: 10/1/12
 * Time: 7:15 AM
 */
public class ScreenController implements HttpRequestHandler {

    @Autowired
    private ScreenSender screenSender;

    public ScreenController() {
    }

    public ScreenController(ScreenSender screenSender) {
        this.screenSender = screenSender;
    }

    @Override
    public void handleRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        AsyncContext asyncContext = request.startAsync();
        if ("true".equals(request.getParameter("allPlayersScreen"))) {
            screenSender.scheduleUpdate(new UpdateRequest(asyncContext, true, null));
        } else {
            Set<String> playersToUpdate = request.getParameterMap().keySet();
            screenSender.scheduleUpdate(new UpdateRequest(asyncContext, false, playersToUpdate));
        }
    }
}