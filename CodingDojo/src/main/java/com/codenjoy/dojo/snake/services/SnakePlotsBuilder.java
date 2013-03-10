package com.codenjoy.dojo.snake.services;

import com.codenjoy.dojo.services.Plot;
import static com.codenjoy.dojo.snake.services.SnakePlotColor.*;
import com.codenjoy.dojo.services.playerdata.PlotsBuilder;
import com.codenjoy.dojo.snake.model.*;
import com.codenjoy.dojo.snake.model.artifacts.Point;

import java.util.LinkedList;
import java.util.List;

/**
 * User: oleksandr.baglai
 * Date: 10/1/12
 * Time: 5:13 AM
 */
public class SnakePlotsBuilder implements PlotsBuilder {      // TODO test me
    private Board board;
    private Plot[][] plots;

    public SnakePlotsBuilder(Board board) {
        this.board = board;
        plots = new Plot[board.getSize()][board.getSize()];
    }

    private void fillSpace() {
        for (int x = 0 ; x < board.getSize(); x++) {
            for (int y = 0 ; y < board.getSize(); y++) {
                plots[x][y] = new Plot(x, y, SPACE);
            }
        }
    }

    @Override
    public List<Plot> get() {
        fillSpace();

        addPlot(board.getApple(), GOOD_APPLE);
        addPlot(board.getStone(), BAD_APPLE);

        Snake snake = board.getSnake();
        for (Point point : snake) {
            addPlot(point, getColor(snake, point));
        }

        Walls walls = board.getWalls();
        for (Point wall : walls) {
            addPlot(wall, BREAK);
        }

        return asList();
    }

    private List<Plot> asList() {
        List<Plot> result = new LinkedList<Plot>();
        for (int x = 0 ; x < board.getSize(); x++) {
            for (int y = 0 ; y < board.getSize(); y++) {
                result.add(plots[x][y]);
            }
        }
        return result;
    }

    private SnakePlotColor getColor(Snake snake, Point point) {
        if (snake.itsMyHead(point)) {
            return getHeadColor(snake.getDirection());
        }

        if (snake.itsMyTail(point)) {
            return getTailColor(snake.getTailDirection());
        }

        return getBodyColor(snake.getBodyDirection(point));
    }

    private SnakePlotColor getTailColor(Direction direction) {
        switch (direction) {
            case DOWN : return TAIL_END_DOWN;
            case UP : return TAIL_END_UP;
            case LEFT : return TAIL_END_LEFT;
            case RIGHT : return TAIL_END_RIGHT;
            default : return SPACE;
        }
    }

    private SnakePlotColor getHeadColor(Direction direction) {
        switch (direction) {
            case DOWN : return HEAD_DOWN;
            case UP : return HEAD_UP;
            case LEFT : return HEAD_LEFT;
            case RIGHT : return HEAD_RIGHT;
            default : return SPACE;
        }
    }

    private SnakePlotColor getBodyColor(BodyDirection bodyDirection) {
        switch (bodyDirection) {
            case HORIZONTAL : return TAIL_HORIZONTAL;
            case VERTICAL : return TAIL_VERTICAL;
            case TURNED_LEFT_DOWN : return TAIL_LEFT_DOWN;
            case TURNED_LEFT_UP : return TAIL_LEFT_UP;
            case TURNED_RIGHT_DOWN : return TAIL_RIGHT_DOWN;
            case TURNED_RIGHT_UP : return TAIL_RIGHT_UP;
            default : return SPACE;
        }
    }

    private void addPlot(Point point, SnakePlotColor color) {
        plots[point.getX()][point.getY()] = new Plot(point.getX(), point.getY(), color);
    }
}
