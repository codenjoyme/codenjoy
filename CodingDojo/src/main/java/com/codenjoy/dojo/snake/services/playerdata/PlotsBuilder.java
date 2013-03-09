package com.codenjoy.dojo.snake.services.playerdata;

import com.codenjoy.dojo.snake.model.Board;
import com.codenjoy.dojo.snake.model.Snake;
import com.codenjoy.dojo.snake.model.Walls;
import com.codenjoy.dojo.snake.model.artifacts.Point;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 * User: oleksandr.baglai
 * Date: 10/1/12
 * Time: 5:13 AM
 */
public class PlotsBuilder {      // TODO test me
    private Board board;

    public PlotsBuilder(Board board) {
        this.board = board;
    }

    public List<Plot> get() {
        List<Plot> result = new LinkedList<Plot>();
        result.add(getPlot(board.getApple(), PlotColor.APPLE));
        result.add(getPlot(board.getStone(), PlotColor.STONE));

        Snake snake = board.getSnake();
        Iterator<Point> iterator = snake.iterator();
        for (int index = 0; index < snake.getLength(); index++) {
            Point point = iterator.next();
            PlotColor color;
            if (index == 0) {
                color = PlotColor.TAIL;
            } else if (index == snake.getLength() - 1) {
                color = PlotColor.HEAD;
            } else {
                color = PlotColor.BODY;
            }
            result.add(getPlot(point, color));
        }

        Walls walls = board.getWalls();
        for (Point wall : walls) {
            result.add(getPlot(wall, PlotColor.WALL));
        }

        return result;
    }

    public Plot getPlot(Point point, PlotColor color) {
        return new Plot(point.getX(), point.getY(), color);
    }
}
