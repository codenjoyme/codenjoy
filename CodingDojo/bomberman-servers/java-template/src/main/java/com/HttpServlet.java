package com;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;

public class HttpServlet extends javax.servlet.http.HttpServlet {

    static long totalResponseTime = 0;
    static long totalNumberOfRequests = 0;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        String boardString = req.getParameter("board");
        if (boardString != null) {
            Date start = new Date();

            String answer = new YourDirectionSolver().get(boardString).toString();
            print(boardString, answer);
            resp.getWriter().write(answer);

            Date end = new Date();
            totalNumberOfRequests++;
            totalResponseTime += (end.getTime() - start.getTime());
        } else {
            //probably you're hitting http://localhost:8888/ from browser (cause board param is not present)
            req.setAttribute("totalNumberOfRequests", totalNumberOfRequests);
            req.setAttribute("averageResponseTime", totalNumberOfRequests != 0 ? totalResponseTime / totalNumberOfRequests : "N/A");
            req.getRequestDispatcher("/index.jsp").forward(req, resp);
        }
    }

    private String split(String board) {
        int size = (int) Math.sqrt(board.length());
        StringBuffer result = new StringBuffer();
        for (int index = 0; index < size; index++) {
            result.append(board.substring(index * size, (index + 1) * size)).append("\n");
        }
        return result.toString();
    }

    private void print(String board, String answer) {
        System.out.println("--------------------------");
        System.out.println("Answer:" + answer);
        System.out.println(split(board));
    }
}
