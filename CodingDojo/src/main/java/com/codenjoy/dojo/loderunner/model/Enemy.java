package com.codenjoy.dojo.loderunner.model;

import com.codenjoy.dojo.services.Direction;
import com.codenjoy.dojo.services.Point;
import com.codenjoy.dojo.services.PointImpl;
import com.codenjoy.dojo.services.Tickable;

/**
 * User: sanja
 * Date: 07.01.14
 * Time: 16:00
 */
public class Enemy extends PointImpl implements Tickable, Fieldable {

    private Direction direction;
    private EnemyAI ai;
    private Field field;
    private boolean withGold;

    public Enemy(Point pt, Direction direction, EnemyAI ai) {
        super(pt);
        withGold = false;
        this.direction = direction;
        this.ai = ai;
    }

    @Override
    public void init(Field field) {
        this.field = field;
    }

    @Override
    public void tick() {
        if (isFall()) {
            if (field.isBrick(x, y - 1) && withGold) {
                withGold = false;
                field.leaveGold(x, y);
            }
            move(x, y - 1);
        } else if (field.isBrick(x, y)) {
            if (field.isFullBrick(x, y)) {
                move(Direction.UP.change(this));
            }
        } else {
            Direction direction = ai.getDirection(field, this);
            if (direction == null) return;

            if (direction == Direction.UP && !field.isLadder(x, y)) return;

            if (direction != Direction.DOWN) {
                this.direction = direction;
            }
            Point pt = direction.change(this);

            if (!field.isHeroAt(pt.getX(), pt.getY())
                    && field.isBarrier(pt.getX(), pt.getY())) return;

            move(pt);
        }
    }

    public boolean isFall() {
        return !field.isBrick(x, y)
                && field.isPit(x, y) && !field.isPipe(x, y) && !field.isLadder(x, y);
    }

    public Enum state() {
        if (field.isBrick(x, y)) {
            return Elements.ENEMY_PIT;
        }

        if (field.isLadder(x, y)) {
            return Elements.ENEMY_LADDER;
        }

        if (field.isPipe(x, y)) {
            if (direction.equals(Direction.LEFT)) {
                return Elements.ENEMY_PIPE_LEFT;
            } else {
                return Elements.ENEMY_PIPE_RIGHT;
            }
        }

        if (direction.equals(Direction.LEFT)) {
            return Elements.ENEMY_LEFT;
        }
        return Elements.ENEMY_RIGHT;
    }

    public void getGold() {
        withGold = true;
    }

    public boolean withGold() {
        return withGold;
    }
}
