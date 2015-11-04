package com.codenjoy.dojo.transport.screen;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.HttpRequestHandler;

import javax.servlet.AsyncContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Set;

public class ScreenController implements HttpRequestHandler {

    @Autowired
    private RestScreenSender screenSender;

    public ScreenController() {
    }

    public ScreenController(RestScreenSender screenSender) {
        this.screenSender = screenSender;
    }


    @Override
    public void handleRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        AsyncContext asyncContext = request.startAsync();
        if ("true".equals(request.getParameter("allPlayersScreen"))) {
            screenSender.scheduleUpdate(new PlayerScreenUpdateRequest(asyncContext, true, null));
        } else {
            Set<String> playersToUpdate = request.getParameterMap().keySet();
            screenSender.scheduleUpdate(new PlayerScreenUpdateRequest(asyncContext, false, playersToUpdate));
        }
    }
}