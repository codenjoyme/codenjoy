package com.codenjoy.dojo.fifteen.model;

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

import com.codenjoy.dojo.services.*;
import com.codenjoy.dojo.services.Point;
import com.codenjoy.dojo.services.joystick.DirectionActJoystick;
import com.codenjoy.dojo.services.joystick.DirectionJoystick;
import com.codenjoy.dojo.services.multiplayer.GameField;
import com.codenjoy.dojo.services.multiplayer.PlayerHero;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class Hero extends PlayerHero<Field> implements State<Elements, Player>, DirectionJoystick {

    private List<Digit> digits;
    private int moveCount;
    private Player player;
    private boolean alive;
    private Direction direction;

    public void setPlayer(Player player) {
        this.player = player;
    }

    public Hero(Point xy) {
        super(xy);
        direction = null;
        alive = true;
        moveCount = 1;
        digits = new LinkedList<>();
    }

    @Override
    public void down() {
        if (!alive) return;

        direction = Direction.DOWN;
    }

    @Override
    public void up() {
        if (!alive) return;

        direction = Direction.UP;
    }

    @Override
    public void left() {
        if (!alive) return;

        direction = Direction.LEFT;
    }

    @Override
    public void right() {
        if (!alive) return;

        direction = Direction.RIGHT;
    }

    @Override
    public void tick() {
        if (!alive) return;

        if (direction != null) {
            int newX = direction.changeX(getX());
            int newY = direction.changeY(getY());

            if (!field.isBarrier(newX, newY)) {
                moveCount++;
                Digit digit = field.getDigit(newX, newY);
                digit.move(Hero.this);
                move(newX, newY);

                if (new DigitHandler().isRightPosition(digit)) {

                   if(!digits.contains(digit)){
                       int number = 1 + Arrays.asList(DigitHandler.DIGITS).indexOf(digit);
                       player.event(new Bonus(moveCount, number));
                       moveCount = 1;
                       digits.add(digit);
                   }

                }
            }

        }
        direction = null;
    }

    public boolean isAlive() {
        return alive;
    }

    public void die() {
        alive = false;
    }

    @Override
    public Elements state(Player player, Object... alsoAtPoint) {
        return Elements.HERO;
    }
}
