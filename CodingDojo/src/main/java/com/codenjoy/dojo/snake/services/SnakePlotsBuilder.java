package com.codenjoy.dojo.snake.services;

import com.codenjoy.dojo.services.Plot;
import com.codenjoy.dojo.services.playerdata.PlotsBuilder;
import com.codenjoy.dojo.snake.model.Board;
import com.codenjoy.dojo.snake.model.BodyDirection;
import com.codenjoy.dojo.snake.model.Snake;
import com.codenjoy.dojo.snake.model.Walls;
import com.codenjoy.dojo.snake.model.artifacts.Point;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import static com.codenjoy.dojo.snake.model.BodyDirection.*;

/**
 * User: oleksandr.baglai
 * Date: 10/1/12
 * Time: 5:13 AM
 */
public class SnakePlotsBuilder implements PlotsBuilder {      // TODO test me
    private Board board;

    public SnakePlotsBuilder(Board board) {
        this.board = board;
    }

    @Override
    public List<Plot> get() {
        List<Plot> result = new LinkedList<Plot>();
        result.add(getPlot(board.getApple(), SnakePlotColor.GOOD_APPLE));
        result.add(getPlot(board.getStone(), SnakePlotColor.BAD_APPLE));

        Snake snake = board.getSnake();
        Iterator<Point> iterator = snake.iterator();
        for (int index = 0; index < snake.getLength(); index++) {
            Point point = iterator.next();
            SnakePlotColor color;
            if (index == 0) {
                switch (snake.getTailDirection()) {
                    case DOWN : color = SnakePlotColor.TAIL_END_DOWN; break;
                    case UP : color = SnakePlotColor.TAIL_END_UP; break;
                    case LEFT : color = SnakePlotColor.TAIL_END_LEFT; break;
                    case RIGHT : color = SnakePlotColor.TAIL_END_RIGHT; break;
                    default : color = SnakePlotColor.SPACE; break;
                }
            } else if (index == snake.getLength() - 1) {
                switch (snake.getDirection()) {
                    case DOWN : color = SnakePlotColor.HEAD_DOWN; break;
                    case UP : color = SnakePlotColor.HEAD_UP; break;
                    case LEFT : color = SnakePlotColor.HEAD_LEFT; break;
                    case RIGHT : color = SnakePlotColor.HEAD_RIGHT; break;
                    default : color = SnakePlotColor.SPACE; break;
                }
            } else {
                switch (snake.getBodyDirection(point)) {
                    case HORIZONTAL : color = SnakePlotColor.TAIL_HORIZONTAL; break;
                    case VERTICAL : color = SnakePlotColor.TAIL_VERTICAL; break;
                    case TURNED_LEFT_DOWN : color = SnakePlotColor.TAIL_LEFT_DOWN; break;
                    case TURNED_LEFT_UP : color = SnakePlotColor.TAIL_LEFT_UP; break;
                    case TURNED_RIGHT_DOWN : color = SnakePlotColor.TAIL_RIGHT_DOWN; break;
                    case TURNED_RIGHT_UP : color = SnakePlotColor.TAIL_RIGHT_UP; break;
                    default : color = SnakePlotColor.SPACE; break;
                }
            }
            result.add(getPlot(point, color));
        }

        Walls walls = board.getWalls();
        for (Point wall : walls) {
            result.add(getPlot(wall, SnakePlotColor.BREAK));
        }

        return result;
    }

    private Plot getPlot(Point point, SnakePlotColor color) {
        return new Plot(point.getX(), point.getY(), color);
    }
}
