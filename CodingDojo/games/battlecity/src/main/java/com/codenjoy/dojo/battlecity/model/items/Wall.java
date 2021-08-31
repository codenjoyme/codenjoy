package com.codenjoy.dojo.battlecity.model.items;

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


import com.codenjoy.dojo.games.battlecity.Element;
import com.codenjoy.dojo.battlecity.model.Player;
import com.codenjoy.dojo.battlecity.services.GameSettings;
import com.codenjoy.dojo.services.*;

import static com.codenjoy.dojo.battlecity.services.GameSettings.Keys.WALL_REGENERATE_TIME;

public class Wall extends PointImpl implements Tickable, State<Element, Player> {

    private Element ch;
    private int timer;
    private boolean overDamage;
    private GameSettings settings;

    public Wall(Point pt) {
        super(pt);
        reset();
        overDamage = false;
    }

    public void init(GameSettings settings) {
        this.settings = settings;
    }

    public void destroy(Bullet bullet) {
        if (bullet.isHeavy()) {
            overDamage = true;
        }

        destroyFrom(bullet.getDirection());
    }

    public void destroyFrom(Direction bulletDirection) {
        if (ch.power() == 1 || overDamage) {
            ch = Element.WALL_DESTROYED;
            return;
        }
        if (bulletDirection.equals(Direction.UP)) {
            switch (ch) {
                case WALL: ch = Element.WALL_DESTROYED_DOWN; break;
                case WALL_DESTROYED_DOWN: ch = Element.WALL_DESTROYED_DOWN_TWICE; break;
                case WALL_DESTROYED_UP: ch = Element.WALL_DESTROYED_UP_DOWN; break;
                case WALL_DESTROYED_LEFT: ch = Element.WALL_DESTROYED_DOWN_LEFT; break;
                case WALL_DESTROYED_RIGHT: ch = Element.WALL_DESTROYED_DOWN_RIGHT; break;
            }
        } else if (bulletDirection.equals(Direction.RIGHT)) {
            switch (ch) {
                case WALL: ch = Element.WALL_DESTROYED_LEFT; break;
                case WALL_DESTROYED_LEFT: ch = Element.WALL_DESTROYED_LEFT_TWICE; break;
                case WALL_DESTROYED_RIGHT: ch = Element.WALL_DESTROYED_LEFT_RIGHT; break;
                case WALL_DESTROYED_UP: ch = Element.WALL_DESTROYED_UP_LEFT; break;
                case WALL_DESTROYED_DOWN: ch = Element.WALL_DESTROYED_DOWN_LEFT; break;
            }
        } else if (bulletDirection.equals(Direction.LEFT)) {
            switch (ch) {
                case WALL: ch = Element.WALL_DESTROYED_RIGHT; break;
                case WALL_DESTROYED_RIGHT: ch = Element.WALL_DESTROYED_RIGHT_TWICE; break;
                case WALL_DESTROYED_UP: ch = Element.WALL_DESTROYED_RIGHT_UP; break;
                case WALL_DESTROYED_DOWN: ch = Element.WALL_DESTROYED_DOWN_RIGHT; break;
                case WALL_DESTROYED_LEFT: ch = Element.WALL_DESTROYED_LEFT_RIGHT; break;
            }
        } else if (bulletDirection.equals(Direction.DOWN)) {
            switch (ch) {
                case WALL: ch = Element.WALL_DESTROYED_UP; break;
                case WALL_DESTROYED_UP: ch = Element.WALL_DESTROYED_UP_TWICE; break;
                case WALL_DESTROYED_RIGHT: ch = Element.WALL_DESTROYED_RIGHT_UP; break;
                case WALL_DESTROYED_DOWN: ch = Element.WALL_DESTROYED_UP_DOWN; break;
                case WALL_DESTROYED_LEFT: ch = Element.WALL_DESTROYED_UP_LEFT; break;
            }
        }
    }

    @Override
    public Element state(Player player, Object... alsoAtPoint) {
        if (!destroyed()) {
            return ch;
        } else  {
            return Element.NONE;
        }
    }

    @Override
    public void tick() {
        if (timer == settings.integer(WALL_REGENERATE_TIME)) {
            timer = 0;
            reset();
        }
        if (destroyed()) {
            timer++;
        }
    }

    public void reset() {
        ch = Element.WALL;
    }

    public boolean destroyed() {
        return ch.power() == 0;
    }

}
