package com.codenjoy.dojo.battlecity.model.levels;

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


import com.codenjoy.dojo.battlecity.model.Tank;
import com.codenjoy.dojo.battlecity.model.items.*;
import com.codenjoy.dojo.services.Dice;
import com.codenjoy.dojo.services.LengthToXY;
import com.codenjoy.dojo.services.Point;
import com.codenjoy.dojo.services.printer.BoardReader;
import com.codenjoy.dojo.utils.LevelUtils;

import java.util.LinkedList;
import java.util.List;

import static com.codenjoy.dojo.battlecity.model.Elements.*;
import static com.codenjoy.dojo.services.Direction.*;

public class LevelImpl implements Level {

    private final LengthToXY xy;
    private final Dice dice;
    private final String map;

    public LevelImpl(String map, Dice dice) {
        this.map = map;
        this.dice = dice;
        xy = new LengthToXY(size());
    }

    @Override
    public int size() {
        return (int) Math.sqrt(map.length());
    }

    @Override
    public List<Wall> getWalls() {
        return LevelUtils.getObjects(xy, map,
                Wall::new,
                WALL);
    }

    @Override
    public List<River> getRivers() {
        return LevelUtils.getObjects(xy, map,
                River::new,
                RIVER);
    }

    @Override
    public List<Ice> getIce() {
        return LevelUtils.getObjects(xy, map,
                Ice::new,
                ICE);
    }

    @Override
    public List<Tree> getTrees() {
        return LevelUtils.getObjects(xy, map,
                Tree::new,
                TREE);
    }

    @Override
    public List<Tank> getAiTanks(int aiTicksPerShoot) {
        return new LinkedList<>(){{
            addAll(LevelUtils.getObjects(xy, map,
                    (pt, el) -> new AITank(pt, DOWN, aiTicksPerShoot, dice),
                    AI_TANK_DOWN));

            addAll(LevelUtils.getObjects(xy, map,
                    (pt, el) -> new AITank(pt, UP, aiTicksPerShoot, dice),
                    AI_TANK_UP));

            addAll(LevelUtils.getObjects(xy, map,
                    (pt, el) -> new AITank(pt, LEFT, aiTicksPerShoot, dice),
                    AI_TANK_LEFT));

            addAll(LevelUtils.getObjects(xy, map,
                    (pt, el) -> new AITank(pt, RIGHT, aiTicksPerShoot, dice),
                    AI_TANK_RIGHT));
        }};
    }

    @Override
    public List<Tank> getTanks(int ticksPerBullets) {
        return new LinkedList<>(){{
            addAll(LevelUtils.getObjects(xy, map,
                    (pt, el) -> new Tank(pt, DOWN, dice, ticksPerBullets),
                    TANK_DOWN, OTHER_TANK_DOWN));

            addAll(LevelUtils.getObjects(xy, map,
                    (pt, el) -> new Tank(pt, UP, dice, ticksPerBullets),
                    TANK_UP, OTHER_TANK_UP));

            addAll(LevelUtils.getObjects(xy, map,
                    (pt, el) -> new Tank(pt, LEFT, dice, ticksPerBullets),
                    TANK_LEFT, OTHER_TANK_LEFT));

            addAll(LevelUtils.getObjects(xy, map,
                    (pt, el) -> new Tank(pt, RIGHT, dice, ticksPerBullets),
                    TANK_RIGHT, OTHER_TANK_RIGHT));
        }};
    }

    @Override
    public List<Border> getBorders() {
        return LevelUtils.getObjects(xy, map,
                (pt, el) -> new Border(pt),
                BATTLE_WALL);
    }

    @Override
    public BoardReader reader() {
        return new BoardReader() {
            @Override
            public int size() {
                return LevelImpl.this.size();
            }

            @Override
            public Iterable<? extends Point> elements() {
                return new LinkedList<>() {{
                    addAll(LevelImpl.this.getBorders());
                    addAll(LevelImpl.this.getWalls());
                    addAll(LevelImpl.this.getAiTanks(0));
                    addAll(LevelImpl.this.getIce());
                    addAll(LevelImpl.this.getRivers());
                    addAll(LevelImpl.this.getTrees());
                }};
            }
        };
    }
}
