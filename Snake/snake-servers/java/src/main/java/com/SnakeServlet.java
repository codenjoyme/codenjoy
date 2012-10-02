package com;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class SnakeServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String board = req.getParameter("board");
        String fixedBoard = new Board(board).fix();
        System.out.println(String.format("com.Board:\n%s", fixedBoard));
        resp.getWriter().write(answer(board));
    }

    String answer(String boardString) {
        Board board = new Board(boardString);
        Point apple = board.getApple();
        String direction = board.getSnakeDirection();
        System.out.println("com.Direction : " + direction);

        direction = new Direction(board.getHead(), apple, direction).get();
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
