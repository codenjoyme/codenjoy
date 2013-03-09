package com.codenjoy.dojo.web.controller;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * User: serhiy.zelenin
 * Date: 6/24/12
 * Time: 12:02 PM
 */
@WebServlet(name = "test", urlPatterns = "/test/")
public class TestServlet extends HttpServlet {

    private static String move;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if (req.getParameter("test") != null){
            move = req.getParameter("move");
        } else{
            resp.getWriter().print(move);
        }
    }
}
