package com.codenjoy.dojo.snake.services.playerdata;

import com.codenjoy.dojo.services.Plot;
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
public class SnakePlotsBuilder {      // TODO test me
    private Board board;

    public SnakePlotsBuilder(Board board) {
        this.board = board;
    }

    public List<Plot> get() {
        List<Plot> result = new LinkedList<Plot>();
        result.add(getPlot(board.getApple(), SnakePlotColor.APPLE));
        result.add(getPlot(board.getStone(), SnakePlotColor.STONE));

        Snake snake = board.getSnake();
        Iterator<Point> iterator = snake.iterator();
        for (int index = 0; index < snake.getLength(); index++) {
            Point point = iterator.next();
            SnakePlotColor color;
            if (index == 0) {
                color = SnakePlotColor.TAIL;
            } else if (index == snake.getLength() - 1) {
                color = SnakePlotColor.HEAD;
            } else {
                color = SnakePlotColor.BODY;
            }
            result.add(getPlot(point, color));
        }

        Walls walls = board.getWalls();
        for (Point wall : walls) {
            result.add(getPlot(wall, SnakePlotColor.WALL));
        }

        return result;
    }

    public Plot getPlot(Point point, SnakePlotColor color) {
        return new Plot(point.getX(), point.getY(), color);
    }
}
