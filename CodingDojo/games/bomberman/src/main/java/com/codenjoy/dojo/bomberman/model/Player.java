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


import com.codenjoy.dojo.bomberman.services.Events;
import com.codenjoy.dojo.services.EventListener;
import com.codenjoy.dojo.services.round.RoundGamePlayer;
import com.codenjoy.dojo.services.settings.Parameter;

public class Player extends RoundGamePlayer<Hero, Field> {

    protected GameSettings settings;

    public Player(EventListener listener, Parameter<Boolean> roundsEnabled) {
        super(listener, roundsEnabled);
    }

    @Override
    public Hero getHero() {
        return (Hero)hero;
    }

    public void newHero(Field board) {
        settings = board.settings();
        if (hero != null) {
            hero.setPlayer(null);
        }
        hero = settings.getHero(settings.getLevel());
        hero.setPlayer(this);
        hero.init(board);

        if (!roundsEnabled()) {
            hero.setActive(true);
        }
    }

    @Override
    public void event(Object event) {
        getHero().addScore(getScoreFor(event));
        super.event(event);
    }

    private int getScoreFor(Object event) {
        if (event == Events.KILL_DESTROY_WALL) {
            return settings.killWallScore().getValue();
        }

        if (event == Events.KILL_MEAT_CHOPPER) {
            return settings.killMeatChopperScore().getValue();
        }

        if (event == Events.KILL_OTHER_HERO) {
            return settings.killOtherHeroScore().getValue();
        }

        if (event == Events.CATCH_PERK) {
            return settings.catchPerkScore().getValue();
        }

        return 0;
    }

    public GameSettings getSettings() {
        return settings;
    }
}
