package net.tetris.web.controller;

import net.tetris.services.RestScreenSender;
import net.tetris.services.ScreenSender;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.HttpRequestHandler;

import javax.servlet.AsyncContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Set;

/**
 * User: serhiy.zelenin
 * Date: 5/9/12
 * Time: 1:46 PM
 */
//@WebServlet(name = "consoleServlet", urlPatterns = "/screen", asyncSupported = true)
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
            screenSender.scheduleUpdate(new UpdateRequest(asyncContext, true, null));
        } else {
            Set<String> playersToUpdate = request.getParameterMap().keySet();
            screenSender.scheduleUpdate(new UpdateRequest(asyncContext, false, playersToUpdate));
        }
    }
}