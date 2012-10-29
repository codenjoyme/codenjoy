package net.tetris.web.controller;

import net.tetris.online.service.ProgressRequest;
import net.tetris.online.service.RestDataSender;
import net.tetris.online.service.SecurityFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.HttpRequestHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * User: serhiy.zelenin
 * Date: 10/28/12
 * Time: 7:01 PM
 */
public class GameExecutionProgressServlet implements HttpRequestHandler {

    @Autowired
    private RestDataSender restDataSender;

    @Override
    public void handleRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String timestamp = request.getParameter("timestamp");
        restDataSender.scheduleGameProgressRequest(new ProgressRequest(request.startAsync(request, response),
                (String) request.getAttribute(SecurityFilter.LOGGED_USER), timestamp));
    }
}
