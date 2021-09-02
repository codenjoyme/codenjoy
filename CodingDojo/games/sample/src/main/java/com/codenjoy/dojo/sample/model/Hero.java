package com.codenjoy.dojo.sample.model;

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


import com.codenjoy.dojo.games.sample.Element;
import com.codenjoy.dojo.services.Direction;
import com.codenjoy.dojo.services.Point;
import com.codenjoy.dojo.services.State;
import com.codenjoy.dojo.services.multiplayer.PlayerHero;

/**
 * Это реализация героя. Обрати внимание, что он имплементит {@see Joystick}, а значит может быть управляем фреймворком
 * Так же он имплементит {@see Tickable}, что значит - есть возможность его оповещать о каждом тике игры.
 * Ну и конечно же он имплементит {@see State}, а значит может быть отрисован на поле.
 * Часть этих интерфейсов объявлены в {@see PlayerHero}, а часть явно тут.
 */
public class Hero extends PlayerHero<Field> implements State<Element, Player> {

    private boolean alive;
    private Direction direction;
    private boolean bomb;

    public Hero(Point xy) {
        super(xy);
        direction = null;
        bomb = false;
        alive = true;
    }

    @Override
    public void init(Field field) {
        super.init(field);

        field.heroes().add(this);
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

        bomb = true;
    }

    @Override
    public void tick() {
        if (!alive) return;

        if (bomb) {
            field.setBomb(this);
            bomb = false;
        }

        if (direction != null) {
            Point to = direction.change(this.copy());

            if (field.bombs().contains(to)) {
                alive = false;
                field.bombs().removeAt(to);
            }

            if (!field.isBarrier(to)) {
                move(to);
            }
        }
        direction = null;
    }

    @Override
    public boolean isAlive() {
        return alive;
    }

    @Override
    public Element state(Player player, Object... alsoAtPoint) {
        if (!isAlive()) {
            return Element.DEAD_HERO;
        }

        if (this == player.getHero()) {
            return Element.HERO;
        } else {
            return Element.OTHER_HERO;
        }
    }
}
