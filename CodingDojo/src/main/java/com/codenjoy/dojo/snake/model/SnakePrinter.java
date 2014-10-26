package com.codenjoy.dojo.snake.model;

import com.codenjoy.dojo.services.Direction;
import com.codenjoy.dojo.services.GamePrinter;
import com.codenjoy.dojo.services.Point;
import com.codenjoy.dojo.snake.model.artifacts.Apple;
import com.codenjoy.dojo.snake.model.artifacts.Stone;

import static com.codenjoy.dojo.snake.model.Elements.*;

public class SnakePrinter implements GamePrinter {

    private int size;
    private Field board;
    private Elements[][] plots;

    private Walls walls;
    private Apple apple;
    private Stone stone;
    private Hero snake;

    public SnakePrinter(Field board) {
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

    @Override
    public boolean init() {
        walls = board.getWalls();
        apple = board.getApple();
        stone = board.getStone();
        snake = board.getSnake();
        return true;
    }

    @Override
    public char get(Point pt) {
        if (apple.itsMe(pt)) {
            return GOOD_APPLE.ch;
        }

        if (stone.itsMe(pt)) {
            return BAD_APPLE.ch;
        }

        if (snake.itsMe(pt)) {
            if (snake.itsMyHead(pt)) {
                return getHead(snake.getDirection()).ch;
            }

            if (snake.itsMyTail(pt)) {
                return getTailColor(snake.getTailDirection()).ch;
            }

            return getBody(snake.getBodyDirection(pt)).ch;
        }

        if (walls.itsMe(pt)) {
            return BREAK.ch;
        }

        return SPACE.ch;
    }

    @Override
    public void printAll(Filler filler) {
        // TODO использовать этот метод
    }
}
