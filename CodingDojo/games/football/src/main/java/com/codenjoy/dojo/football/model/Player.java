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


import com.codenjoy.dojo.football.model.elements.Hero;
import com.codenjoy.dojo.football.services.Events;
import com.codenjoy.dojo.services.EventListener;
import com.codenjoy.dojo.services.Point;
import com.codenjoy.dojo.services.multiplayer.GamePlayer;

public class Player extends GamePlayer<Hero, Field> {

    Hero hero;
    private boolean goalHited;
    private Elements myGoal;
    private String team;

    public Player(EventListener listener) {
        super(listener);
    }

    public void event(Events event) {
        switch (event) {
            case TOP_GOAL: 
                goalHited = true;
                if (myGoal != Elements.TOP_GOAL) {
                    super.event(Events.WIN);
                }
                break;
            case BOTTOM_GOAL: 
                goalHited = true;
                if (myGoal != Elements.BOTTOM_GOAL) {
                    super.event(Events.WIN);
                }
                break;
            case WIN:
                break;
        default:
            break;
        }
        
        super.event(event);
    }

    public Hero getHero() {
        return hero;
    }

    public void newHero(Field field) {
        Point pt = field.getFreeRandomOnMyHalf(this);
        hero = new Hero(pt);
        hero.setTeam(team);
        hero.init(field);
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

    public Elements getMyGoal() {
        return myGoal;
    }

    public void setMyGoal(Elements myGoal) {
        this.myGoal = myGoal;
    }

    public void setTeam(String team) {
        this.team = team;    
    }

}
