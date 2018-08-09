package com.codenjoy.dojo.football.model.elements;

/*-
 * #%L
 * Codenjoy - it's a dojo-like platform from developers to developers.
 * %%
 * Copyright (C) 2018 Codenjoy
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/gpl-3.0.html>.
 * #L%
 */

import com.codenjoy.dojo.football.model.Elements;
import com.codenjoy.dojo.football.model.Field;
import com.codenjoy.dojo.football.model.Player;
import com.codenjoy.dojo.services.Direction;
import com.codenjoy.dojo.services.Point;
import com.codenjoy.dojo.services.PointImpl;
import com.codenjoy.dojo.services.State;
import com.codenjoy.dojo.services.Tickable;

/**
 * Артефакт Мяч на поле
 */
public class Ball extends PointImpl implements State<Elements, Player>, Tickable {

    private static final int DEFAULT_IMPULSE = 3; //после удара игрока мяч летит сам 3 такта
    private static final int MAX_POWER = 3;       //после удара игрока мяч летит сразу на 3 клетки
    private Direction direction;
    private Field field;
    private int impulse;
    private int power;
    
    public Ball(int x, int y) {
        super(x, y);
    }

    public Ball(Point point) {
        super(point);
    }
    
    public void init(Field field) {
        this.field = field;
    }

    @Override
    public Elements state(Player player, Object... alsoAtPoint) {
        if(isBallMoving()) {
            return Elements.BALL;
        } else {
            return Elements.STOPPED_BALL;
        }
    }

    @Override
    public void tick() {
        
        if ( !isBallMoving() ){
            direction = null;
        }
        if (direction != null) {
            int newX = x;
            int newY = y;
            for(int i=1; i<=power; i++) {
                newX = direction.changeX(newX);
                newY = direction.changeY(newY);
            }

           if (!field.isWall(newX, newY)) {
                move(newX, newY);
           }
           
           //power = Math.max(power-1, 1);
           power = 1;
            
        }
        impulse--;
    }
    
    public boolean isBallMoving() {
        return impulse > 0;
    }

    public void down(int power) {

        direction = Direction.DOWN;
        setImpulse(DEFAULT_IMPULSE);
        setPower(power);
    }

    public void up(int power) {

        direction = Direction.UP;
        setImpulse(DEFAULT_IMPULSE);
        setPower(power);
    }

    public void left(int power) {

        direction = Direction.LEFT;
        setImpulse(DEFAULT_IMPULSE);
        setPower(power);
    }

    public void right(int power) {
        direction = Direction.RIGHT;
        setImpulse(DEFAULT_IMPULSE);
        setPower(power);
   }

    public int getImpulse() {
        return impulse;
    }

    public void setImpulse(int impulse) {
        this.impulse = impulse;
    }

    private void setPower(int power) {
        this.power = Math.min(power, MAX_POWER);
    }

    public void stop() {
        setImpulse(0);
        direction = null;
    }

    
}
