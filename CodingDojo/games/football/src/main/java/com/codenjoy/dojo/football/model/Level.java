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

import com.codenjoy.dojo.football.model.items.Ball;
import com.codenjoy.dojo.football.model.items.Goal;
import com.codenjoy.dojo.football.model.items.Hero;
import com.codenjoy.dojo.football.model.items.Wall;
import com.codenjoy.dojo.services.field.AbstractLevel;

import java.util.List;

import static com.codenjoy.dojo.games.football.Element.*;

public class Level extends AbstractLevel {

    public Level(String map) {
        super(map);
    }

    public List<Hero> getHero() {
        return find(Hero::new,
                HERO,
                HERO_W_BALL,
                TEAM_MEMBER,
                TEAM_MEMBER_W_BALL,
                ENEMY,
                ENEMY_W_BALL);
    }

    public List<Wall> getWalls() {
        return find(Wall::new, WALL);
    }

    public List<Ball> getBalls() {
        return find(Ball::new,
                BALL,
                STOPPED_BALL,
                HERO_W_BALL,
                TEAM_MEMBER_W_BALL,
                ENEMY_W_BALL,
                HITED_GOAL,
                HITED_MY_GOAL);
    }

    public List<Goal> getTopGoals() {
        return find(Goal::new, TOP_GOAL);
    }

    public List<Goal> getBottomGoals() {
        return find(Goal::new, BOTTOM_GOAL);
    }
}