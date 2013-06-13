package com.codenjoy.dojo.snake.model;

import com.codenjoy.dojo.services.Direction;
import com.codenjoy.dojo.services.Point;
import com.codenjoy.dojo.services.Printer;
import com.codenjoy.dojo.snake.model.artifacts.Apple;
import com.codenjoy.dojo.snake.model.artifacts.Stone;

import static com.codenjoy.dojo.snake.model.PlotColor.*;

public class SnakePrinter implements Printer {

    private int size;
    private Board board;
    private PlotColor[][] plots;

    public SnakePrinter(Board board) {
        this.board = board;
        this.size = board.getSize();
        plots = new PlotColor[size][size];
    }

    void clean() {
        for (int x = 0 ; x < size; x++) {
            for (int y = 0 ; y < size; y++) {
                plots[x][y] = SPACE;
            }
        }
    }

    @Override
    public String print() {
        clean();
        printApple(board.getApple());
        printStone(board.getStone());
        printSnake(board.getSnake());
        printWalls(board.getWalls());
        return asString();
    }

    void printSnake(Snake snake) {
        for (Point point : snake) {
            draw(point, getColor(snake, point));
        }
    }

    void printStone(Stone stone) {
        draw(stone, BAD_APPLE);
    }

    void printWalls(Walls walls) {
        for (Point wall : walls) {
            draw(wall, BREAK);
        }
    }

    void printApple(Apple apple) {
        draw(apple, GOOD_APPLE);
    }

    public String asString() {
        String result = "";
        for (int y = 0; y < size; y++) {
            for (int x = 0; x < size; x++) {
                result += plots[x][size - 1 - y];
            }
            result += '\n';
        }
        return result;
    }

    private PlotColor getColor(Snake snake, Point point) {
        if (snake.itsMyHead(point)) {
            return getHeadColor(snake.getDirection());
        }

        if (snake.itsMyTail(point)) {
            return getTailColor(snake.getTailDirection());
        }

        return getBodyColor(snake.getBodyDirection(point));
    }

    private PlotColor getTailColor(Direction direction) {
        switch (direction) {
            case DOWN : return TAIL_END_DOWN;
            case UP : return TAIL_END_UP;
            case LEFT : return TAIL_END_LEFT;
            case RIGHT : return TAIL_END_RIGHT;
            default : return SPACE;
        }
    }

    private PlotColor getHeadColor(Direction direction) {
        switch (direction) {
            case DOWN : return HEAD_DOWN;
            case UP : return HEAD_UP;
            case LEFT : return HEAD_LEFT;
            case RIGHT : return HEAD_RIGHT;
            default : return SPACE;
        }
    }

    private PlotColor getBodyColor(BodyDirection bodyDirection) {
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

    private void draw(Point point, PlotColor color) {
        if (point != null && point.getX() != -1 && point.getY() != -1) {
            plots[point.getX()][point.getY()] = color;
        }
    }
}
