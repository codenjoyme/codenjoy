package com.codenjoy.dojo.sokoban.model.itemsImpl;

/*-
 * #%L
 * Codenjoy - it's a dojo-like platform from developers to developers.
 * %%
 * Copyright (C) 2016 Codenjoy
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
import com.codenjoy.dojo.services.multiplayer.PlayerHero;
import com.codenjoy.dojo.sokoban.model.items.Field;
import com.codenjoy.dojo.sokoban.services.Player;

/**
 * Это реализация героя. Обрати внимание, что он имплементит {@see Joystick}, а значит может быть управляем фреймворком
 * Так же он имплементит {@see Tickable}, что значит - есть возможность его оповещать о каждом тике игры.
 * Ну и конечно же он имплементит {@see State}, а значит может быть отрисован на поле.
 * Часть этих интерфейсов объявлены в {@see PlayerHero}, а часть явно тут.
 */
public class Hero extends PlayerHero<Field> implements State<Elements, Player> {

    private boolean alive;
    private Direction direction;

    public Hero(Point xy) {
        super(xy);
        direction = null;
        alive = true;
    }

    @Override
    public void init(Field field) {
        this.field = field;
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
    public void act(int... p) {
        if (!alive) return;

        field.setBomb(x, y);
    }

    @Override
    public void tick() {
        if (!alive) return;

        if (direction != null) {
            int newX = direction.changeX(x);
            int newY = direction.changeY(y);
            int newNextX = direction.changeX(newX);
            int newNextY = direction.changeY(newY);

            if (field.isBomb(newX, newY)) {
                alive = false;
                field.removeBomb(newX, newY);
            }

            if (!(field.isBarrier(newNextX, newNextY)||field.isMark(newNextX,newNextY))) {
                if (field.isBox(newX, newY)) {
                    field.moveBox(newX, newY, newNextX, newNextY);
                }
                if (field.isBoxOnTheMark(newX, newY)) {
                        field.removeBoxOnTheMark(newX, newY);
                        field.setBox(newNextX, newNextY);
                        field.setMark(newX, newY);
                }
            }
            if (!field.isBarrier(newNextX, newNextY)&&field.isMark(newNextX,newNextY)) {
                field.removeBox(newX, newY);
                field.setBoxOnTheMark(newNextX, newNextY);
            }

                if (!field.isBarrier(newX, newY)) {
                move(newX, newY);
            }
        }
        direction = null;
    }

    public boolean isAlive() {
        return alive;
    }

    @Override
    public Elements state(Player player, Object... alsoAtPoint) {
        return Elements.HERO;
    }
}
