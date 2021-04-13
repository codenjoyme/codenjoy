package com.codenjoy.dojo.battlecity.model;

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


import com.codenjoy.dojo.battlecity.services.Events;
import com.codenjoy.dojo.battlecity.services.GameSettings;
import com.codenjoy.dojo.battlecity.services.Scores;
import com.codenjoy.dojo.services.Dice;
import com.codenjoy.dojo.services.Direction;
import com.codenjoy.dojo.services.EventListener;
import com.codenjoy.dojo.services.round.RoundGamePlayer;

import static com.codenjoy.dojo.services.PointImpl.pt;

public class Player extends RoundGamePlayer<Tank, Field> {

    private Dice dice;
    private int killed;

    public Player(EventListener listener, Dice dice, GameSettings settings){
        super(listener, settings);
        this.dice = dice;
        reset();
    }

    public void reset() {
        killed = 0;
    }

    public Tank getHero() {
        return hero;
    }

    public boolean isDestroyed() {
        return !isAlive();
    }

    public void event(Events event) {
        getHero().addScore(Scores.scoreFor(settings(), event));
        super.event(event);
    }

    public int killHero() {
        return killed++;
    }

    public void newHero(Field field) {
        hero = new Tank(pt(0, 0), Direction.UP, dice);
        hero.setPlayer(this);
        hero.removeBullets();
        hero.init(field);
        reset();

        if (!roundsEnabled()) { // TODO test me
            hero.setActive(true);
        }
    }

    public int score() {
        return killed;
    }

    void setKilled(int killed) {
        this.killed = killed;
    }

    private GameSettings settings() {
        return (GameSettings) settings;
    }
}
