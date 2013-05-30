package com.codenjoy.bomberman.servlet;

import java.io.IOException;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.codenjoy.bomberman.domain.Board;
import com.codenjoy.bomberman.gameplay.DirectionSolver;

public class BombermanServlet extends HttpServlet {
	long totalResponseTime = 0;
	long totalNumberOfRequests = 0;
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException
    {	
        String boardString = req.getParameter("board");
        if (boardString!=null) {
        	Date start = new Date();
        	Board board = new Board(boardString);
        	System.out.println(board);
        	resp.getWriter().write(answer(board));
        	Date end = new Date();
        	totalNumberOfRequests++;
        	totalResponseTime=+(end.getTime()-start.getTime());
        } else {
        	//probably you're hitting http://localhost:8888/ from browser (cause board param is not present)
        	req.setAttribute("totalNumberOfRequests", totalNumberOfRequests);
        	req.setAttribute("averageResponseTime", totalNumberOfRequests!=0 ? totalResponseTime/totalNumberOfRequests : "N/A");
        	req.getRequestDispatcher("/index.jsp").forward(req, resp);
        }
    }

    String answer(Board board) {
        String direction = new DirectionSolver(board).get();
        System.out.println("Bomberman : " + direction);
        return direction;
    }

}
