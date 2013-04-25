package com;

import com.utils.Board;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class Runner extends HttpServlet {


    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException
    {
        String boardString = req.getParameter("board");
        Board board = new Board(boardString);
        String answer = new DirectionSolver().get(board).toString();
        print(board.toString(), answer);
        resp.getWriter().write(answer);
    }

    private void print(String board, String answer) {
        System.out.println("--------------------------");
        System.out.println("Answer:" + answer);
        System.out.println(board);
    }

    public static void main(String[] args) throws Exception {
        Server server = new Server(8888);
        ServletContextHandler context = new ServletContextHandler(server, "/");
        context.addServlet(new ServletHolder(new Runner()), "/*");
        server.setHandler(context);
        server.start();
    }
}
