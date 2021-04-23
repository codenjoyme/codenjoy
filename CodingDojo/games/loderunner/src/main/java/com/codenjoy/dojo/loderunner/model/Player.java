package com.codenjoy.dojo.loderunner.model;

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


import com.codenjoy.dojo.loderunner.services.Events;
import com.codenjoy.dojo.loderunner.services.GameSettings;
import com.codenjoy.dojo.services.Direction;
import com.codenjoy.dojo.services.EventListener;
import com.codenjoy.dojo.services.Point;
import com.codenjoy.dojo.services.round.RoundGamePlayer;

import java.util.Optional;

import static com.codenjoy.dojo.services.PointImpl.pt;

public class Player extends RoundGamePlayer<Hero, Field> {

    public Player(EventListener listener, GameSettings settings) {
        super(listener, settings);
    }

    @Override
    public Hero getHero() {
        return hero;
    }

    @Override
    public void start(int round, Object startEvent) {
        super.start(round, startEvent);
        hero.clearScores();
    }

    @Override
    public void newHero(Field field) {
        Optional<Point> pt = field.getFreeRandom();
        hero = new Hero(pt.orElseGet(() -> pt(0, 0)), Direction.RIGHT);
        hero.setPlayer(this);
        hero.init(field);

        if (!roundsEnabled()) {
            hero.setActive(true);
        }
    }

    @Override
    public boolean isAlive() {
        return hero != null && hero.isAlive();
    }

    @Override
    public void event(Object event) {
        super.event(event);

        if (event instanceof Events) {
            switch ((Events) event) {
                case KILL_ENEMY:
                    hero.increaseScore();
                    break;
                case START_ROUND:
                case SUICIDE:
                case KILL_HERO:
                case WIN_ROUND:
                    hero.clearScores();
                    break;
            }
        }
    }

    // only for testing
    public void setHero(Hero hero) {
        this.hero = hero;
    }
}
