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
import com.codenjoy.dojo.services.Dice;
import com.codenjoy.dojo.services.Direction;
import com.codenjoy.dojo.services.EventListener;
import com.codenjoy.dojo.services.multiplayer.GamePlayer;

import static com.codenjoy.dojo.services.PointImpl.pt;

public class Player extends GamePlayer<Tank, Field> {

    public static final int TICKS_PER_BULLETS = 4;

    Tank hero;
    private Dice dice;
    private int killed;

    public Player(EventListener listener, Dice dice) {
        super(listener);
        this.dice = dice;
        reset();
    }

    public void reset() {
        killed = 0;
    }

    public Tank getHero() {
        return hero;
    }

    @Override
    public boolean isAlive() {
        return hero != null && hero.isAlive();
    }

    public void event(Events event) {
        if (event.isKillYourTank()) {
            hero.kill(null);
        }

        super.event(event);
    }

    public int killHero() {
        return killed++;
    }

    public void newHero(Field field) {
        hero = new Tank(pt(0, 0), Direction.UP, dice, TICKS_PER_BULLETS);
        hero.removeBullets();
        hero.init(field);
        reset();
    }

    public int score() {
        return killed;
    }

    void setKilled(int killed) {
        this.killed = killed;
    }
}
