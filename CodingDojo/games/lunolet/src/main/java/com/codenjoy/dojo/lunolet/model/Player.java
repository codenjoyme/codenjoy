package com.codenjoy.dojo.lunolet.model;

/*-
 * #%L
 * Codenjoy - it's a dojo-like platform from developers to developers.
 * %%
 * Copyright (C) 2016 - 2018 Codenjoy
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


import com.codenjoy.dojo.services.EventListener;

public class Player {

    private EventListener listener;
    private int maxScore;
    private int score;
    private Level level;
    private Hero hero;

    public Player(EventListener listener) {
        this.listener = listener;
        clearScore();
    }

    public Hero getHero() {
        return hero;
    }

    public int getMaxScore() {
        return maxScore;
    }

    public int getScore() {
        return score;
    }

    public void clearScore() {
        score = 0;
    }

    public void newHero(Level level) {
        hero = new Hero();
        this.level = level;
        hero.init(level);
    }
}
