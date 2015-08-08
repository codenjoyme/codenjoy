package com.codenjoy.dojo.pong.model;

import com.codenjoy.dojo.services.Point;
import com.codenjoy.dojo.services.PointImpl;
import com.codenjoy.dojo.services.State;
import com.codenjoy.dojo.services.Tickable;

public class Ball extends PointImpl implements Tickable, State<Elements, Player> {

    private Field field;

    private BallDirection direction;
    private int ballSpeed = 1;

    public Ball(Point pt) {
        super(pt);
    }

    public Ball(int x, int y) {
        super(x, y);
    }

    @Override
    public Elements state(Player player, Object... alsoAtPoint) {
        return Elements.BALL;
    }

    @Override
    public void tick() {
        for (int i = 0; i < ballSpeed; i++) {
            int supposedX =  direction.changeX(x);
            int supposedY =  direction.changeY(y);
            if (field.isBarrier(supposedX, supposedY) ) { //todo check all barriers between current position and supposed
                direction = direction.reflectedFrom(field.getBarrier(supposedX, supposedY));
                supposedX = direction.changeX(x);
                supposedY = direction.changeY(y);
            }
            move(supposedX, supposedY);
        }
    }

    public void setDirection(BallDirection direction) {
        this.direction = direction;
    }

    public void init(Field field) {
        this.field = field;
    }

    public BallDirection getDirection() {
        return direction;
    }
}
