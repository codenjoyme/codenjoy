package com.codenjoy.dojo.football.model;

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


import com.codenjoy.dojo.football.model.items.Hero;
import com.codenjoy.dojo.football.services.GameSettings;
import com.codenjoy.dojo.games.football.Element;
import com.codenjoy.dojo.services.EventListener;
import com.codenjoy.dojo.services.Point;
import com.codenjoy.dojo.services.multiplayer.GamePlayer;

public class Player extends GamePlayer<Hero, Field> {

    private boolean goalHited;
    private Element myGoal;
    private String team;

    public Player(EventListener listener, GameSettings settings) {
        super(listener, settings);
    }

    @Override
    public Hero createHero(Point pt) {
        Hero hero = new Hero(pt);
        hero.setTeam(team);
        return hero;
    }

    @Override
    public boolean isAlive() {
        if (hero == null) return false;
        if (isGoalHited()) {
            clearGoalHited();
            return false;
        } else {
            return true;
        }
    }

    public boolean isGoalHited() {
        return goalHited;
    }

    public void clearGoalHited() {
        this.goalHited = false;
    }

    public Element getMyGoal() {
        return myGoal;
    }

    public void setMyGoal(Element myGoal) {
        this.myGoal = myGoal;
    }

    public void setTeam(String team) {
        this.team = team;    
    }

    public void goalHited(boolean goalHited) {
        this.goalHited = goalHited;
    }

    public Element myGoal() {
        return myGoal;
    }
}
