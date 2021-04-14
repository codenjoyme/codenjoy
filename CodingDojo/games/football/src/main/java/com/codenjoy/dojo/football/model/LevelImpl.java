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

import com.codenjoy.dojo.football.model.elements.Ball;
import com.codenjoy.dojo.football.model.elements.Goal;
import com.codenjoy.dojo.football.model.elements.Hero;
import com.codenjoy.dojo.football.model.elements.Wall;
import com.codenjoy.dojo.services.LengthToXY;
import com.codenjoy.dojo.utils.LevelUtils;

import java.util.List;

import static com.codenjoy.dojo.football.model.Elements.*;

public class LevelImpl implements Level {

    private LengthToXY xy;
    private String map;

    public LevelImpl(String map) {
        this.map = LevelUtils.clear(map);
        xy = new LengthToXY(getSize());
    }

    @Override
    public int getSize() {
        return (int) Math.sqrt(map.length());
    }

    @Override
    public List<Hero> getHero() {
        return LevelUtils.getObjects(xy, map,
                (pt, el) -> new Hero(pt),
                HERO,
                HERO_W_BALL,
                TEAM_MEMBER,
                TEAM_MEMBER_W_BALL,
                ENEMY,
                ENEMY_W_BALL);
    }

    @Override
    public List<Wall> getWalls() {
        return LevelUtils.getObjects(xy, map,
                (pt, el) -> new Wall(pt),
                WALL);
    }

    @Override
    public List<Ball> getBalls() {
        return LevelUtils.getObjects(xy, map,
                (pt, el) -> new Ball(pt),
                BALL,
                STOPPED_BALL,
                HERO_W_BALL,
                TEAM_MEMBER_W_BALL,
                ENEMY_W_BALL,
                HITED_GOAL,
                HITED_MY_GOAL);
    }

    @Override
    public List<Goal> getTopGoals() {
        return LevelUtils.getObjects(xy, map,
                Goal::new,
                TOP_GOAL);
    }

    @Override
    public List<Goal> getBottomGoals() {
        return LevelUtils.getObjects(xy, map,
                Goal::new,
                BOTTOM_GOAL);
    }
}
