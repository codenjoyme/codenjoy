package com.codenjoy.dojo.loderunner.model;

import com.codenjoy.dojo.services.Direction;
import com.codenjoy.dojo.services.Joystick;
import com.codenjoy.dojo.services.Point;
import com.codenjoy.dojo.services.PointImpl;

/**
 * User: sanja
 * Date: 17.12.13
 * Time: 5:10
 */
public class Hero extends PointImpl implements Joystick {

    private Direction direction;

    public Hero(Point xy, Direction direction) {
        super(xy);
        this.direction = direction;
    }

    @Override
    public void down() {
        
    }

    @Override
    public void up() {
        
    }

    @Override
    public void left() {
        
    }

    @Override
    public void right() {
        
    }

    @Override
    public void act() {
        
    }

    public Direction getDirection() {
        return direction;
    }
}
