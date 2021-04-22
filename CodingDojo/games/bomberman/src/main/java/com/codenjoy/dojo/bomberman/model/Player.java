package com.codenjoy.dojo.bomberman.model;

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


import com.codenjoy.dojo.bomberman.services.GameSettings;
import com.codenjoy.dojo.bomberman.services.Scores;
import com.codenjoy.dojo.services.Dice;
import com.codenjoy.dojo.services.EventListener;
import com.codenjoy.dojo.services.round.RoundGamePlayer;

public class Player extends RoundGamePlayer<Hero, Field> {

    private Dice dice;

    public Player(EventListener listener, Dice dice, GameSettings settings) {
        super(listener, settings);
        this.dice = dice;
    }

    @Override
    public Hero getHero() {
        return hero;
    }

    @Override
    public void start(int round, Object startEvent) {
        super.start(round, startEvent);
        // hero.clearScores(); TODO test me
    }

    public void newHero(Field board) {
        if (hero != null) {
            hero.setPlayer(null);
        }
        hero = settings().getHero(settings().getLevel(), dice);
        hero.setPlayer(this);
        hero.init(board);

        if (!roundsEnabled()) { // TODO test me
            hero.setActive(true);
        }
    }

    @Override
    public void event(Object event) {
        getHero().addScore(Scores.scoreFor(settings(), event));
        super.event(event);
    }

    private GameSettings settings() {
        return (GameSettings) settings;
    }
}