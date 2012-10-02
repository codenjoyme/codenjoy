package com;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

public class SnakeServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String boardString = req.getParameter("board");
        Board board = new Board(boardString);
        System.out.println(board);
        resp.getWriter().write(answer(board));
    }

    String answer(Board board) {
        String direction = new Direction(board.getHead(), board.getApple(),
                board.getSnakeDirection()).get(board.getBarriers());
        System.out.println("Rotate to : " + direction);
        return direction;
    }

    public static void main(String[] args) throws Exception {
        Server server = new Server(8888);
        ServletContextHandler context = new ServletContextHandler(server, "/");
        context.addServlet(new ServletHolder(new SnakeServlet()), "/*");
        server.setHandler(context);
        server.start();
    }
}
