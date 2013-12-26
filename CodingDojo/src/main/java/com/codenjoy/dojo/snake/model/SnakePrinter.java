package com.codenjoy.dojo.snake.model;

import com.codenjoy.dojo.services.Direction;
import com.codenjoy.dojo.services.GamePrinter;
import com.codenjoy.dojo.services.Point;
import static com.codenjoy.dojo.services.PointImpl.*;

import static com.codenjoy.dojo.snake.model.Elements.*;

public class SnakePrinter implements GamePrinter {

    private int size;
    private Board board;
    private Elements[][] plots;

    public SnakePrinter(Board board) {
        this.board = board;
        this.size = board.getSize();
        plots = new Elements[size][size];
    }

    private Elements getTailColor(Direction direction) {
        switch (direction) {
            case DOWN : return TAIL_END_DOWN;
            case UP : return TAIL_END_UP;
            case LEFT : return TAIL_END_LEFT;
            case RIGHT : return TAIL_END_RIGHT;
            default : return SPACE;
        }
    }

    private Elements getHead(Direction direction) {
        switch (direction) {
            case DOWN : return HEAD_DOWN;
            case UP : return HEAD_UP;
            case LEFT : return HEAD_LEFT;
            case RIGHT : return HEAD_RIGHT;
            default : return SPACE;
        }
    }

    private Elements getBody(BodyDirection bodyDirection) {
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

    private void draw(Point point, Elements color) {
        if (point != null && point.getX() != -1 && point.getY() != -1) {
            plots[point.getX()][point.getY()] = color;
        }
    }

    @Override
    public Enum get(int x, int y) {
        Point pt = pt(x, y);

        if (board.getApple().itsMe(pt)) {
            return GOOD_APPLE;
        }

        if (board.getStone().itsMe(pt)) {
            return BAD_APPLE;
        }

        Snake snake = board.getSnake();
        if (snake.itsMe(x, y)) {
            if (snake.itsMyHead(pt)) {
                return getHead(snake.getDirection());
            }

            if (snake.itsMyTail(pt)) {
                return getTailColor(snake.getTailDirection());
            }

            return getBody(snake.getBodyDirection(pt));
        }

        if (board.getWalls().itsMe(pt)) {
            return BREAK;
        }

        return SPACE;
    }
}
