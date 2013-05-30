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

public class HttpClient extends HttpServlet {


    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException
    {
        String boardString = req.getParameter("board");
        String answer = new ApofigDirectionSolver().get(boardString).toString();
        print(boardString, answer);
        resp.getWriter().write(answer);
    }


    private String split(String board) {
        int size = (int)Math.sqrt(board.length());
        StringBuffer result = new StringBuffer();
        for (int index = 0; index < size; index++) {
            result.append(board.substring(index*size, (index + 1)*size)).append("\n");
        }
        return result.toString();
    }

    private void print(String board, String answer) {
        System.out.println("--------------------------");
        System.out.println("Answer:" + answer);
        System.out.println(split(board));
    }

    public static void main(String[] args) throws Exception {
        Server server = new Server(8889);
        ServletContextHandler context = new ServletContextHandler(server, "/");
        context.addServlet(new ServletHolder(new HttpClient()), "/*");
        server.setHandler(context);
        server.start();
    }
}
