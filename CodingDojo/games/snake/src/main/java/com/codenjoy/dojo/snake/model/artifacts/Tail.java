package com.codenjoy.dojo.snake.model.artifacts;

import com.codenjoy.dojo.services.Direction;
import com.codenjoy.dojo.services.PointImpl;
import com.codenjoy.dojo.services.State;
import com.codenjoy.dojo.snake.model.BodyDirection;
import com.codenjoy.dojo.snake.model.Elements;
import com.codenjoy.dojo.snake.model.Hero;

import static com.codenjoy.dojo.snake.model.Elements.*;

public class Tail extends PointImpl implements State<Elements, Object> {

    private Hero snake;

    public Tail(int x, int y, Hero snake) {
        super(x, y);
        this.snake = snake;
    }

    private Elements getTailColor(Direction direction) {
        switch (direction) {
            case DOWN : return TAIL_END_DOWN;
            case UP : return TAIL_END_UP;
            case LEFT : return TAIL_END_LEFT;
            case RIGHT : return TAIL_END_RIGHT;
            default : return NONE;
        }
    }

    private Elements getHead(Direction direction) {
        switch (direction) {
            case DOWN : return HEAD_DOWN;
            case UP : return HEAD_UP;
            case LEFT : return HEAD_LEFT;
            case RIGHT : return HEAD_RIGHT;
            default : return NONE;
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
            default : return NONE;
        }
    }

    @Override
    public Elements state(Object player, Object... alsoAtPoint) {
        if (snake.itsMyHead(this)) {
            return getHead(snake.getDirection());
        }

        if (snake.itsMyTail(this)) {
            return getTailColor(snake.getTailDirection());
        }

        return getBody(snake.getBodyDirection(this));
    }
}
