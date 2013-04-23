package com.leokom;

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
        String board = req.getParameter("board");
        String answer = new DirectionSolver().get(board).name();
        print(board, answer);
        resp.getWriter().write(answer);
    }

    private void print(String board, String answer) {
        System.out.println("--------------------------");
        System.out.println("Answer:" + answer);
        System.out.println(split(board));
    }

    private String split(String board) {
        int size = (int)Math.sqrt(board.length());
        StringBuffer result = new StringBuffer();
        for (int index = 0; index < size; index++) {
            result.append(board.substring(index*size, (index + 1)*size)).append("\n");
        }
        return result.toString();
    }

    public static void main(String[] args) throws Exception {
        Server server = new Server(8887);
        ServletContextHandler context = new ServletContextHandler(server, "/");
        context.addServlet(new ServletHolder(new Runner()), "/*");
        server.setHandler(context);
        server.start();
    }
}
