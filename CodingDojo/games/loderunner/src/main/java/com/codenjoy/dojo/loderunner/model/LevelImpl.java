package com.codenjoy.dojo.loderunner.model;

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


import com.codenjoy.dojo.loderunner.model.Pill.PillType;
import com.codenjoy.dojo.services.Dice;
import com.codenjoy.dojo.services.Direction;
import com.codenjoy.dojo.services.LengthToXY;
import com.codenjoy.dojo.utils.LevelUtils;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.List;

import static com.codenjoy.dojo.loderunner.model.Elements.*;

public class LevelImpl implements Level {

    private Dice dice;
    private LengthToXY xy;
    private EnemyAI ai;
    private String map;

    public LevelImpl(String map, Dice dice) {
        this.map = map;
        this.dice = dice;
        ai = new AI();
        xy = new LengthToXY(getSize());
    }

    @Override
    public int getSize() {
        return (int) Math.sqrt(map.length());
    }

    @Override
    public List<Hero> getHeroes() {
        EnumSet<Elements> left = EnumSet.of(
                HERO_DRILL_LEFT, HERO_LEFT, HERO_FALL_LEFT, HERO_PIPE_LEFT,
                HERO_SHADOW_DRILL_LEFT,
                HERO_SHADOW_LEFT, HERO_SHADOW_FALL_LEFT, HERO_SHADOW_PIPE_LEFT);

        EnumSet<Elements> right = EnumSet.of(
                HERO_DRILL_RIGHT, HERO_RIGHT, HERO_FALL_RIGHT, HERO_PIPE_RIGHT,
                HERO_SHADOW_DRILL_RIGHT,
                HERO_SHADOW_RIGHT, HERO_SHADOW_FALL_RIGHT, HERO_SHADOW_PIPE_RIGHT);

        return LevelUtils.getObjects(xy, map,
                new HashMap<>(){{
                    left.forEach(element ->
                            put(element, pt -> new Hero(pt, Direction.LEFT)));
                    right.forEach(element ->
                            put(element, pt -> new Hero(pt, Direction.RIGHT)));
                }});
    }

    @Override
    public List<Brick> getBricks() {
        return LevelUtils.getObjects(xy, map,
                Brick::new,
                BRICK);
    }

    @Override
    public List<Border> getBorders() {
        return LevelUtils.getObjects(xy, map,
                Border::new,
                UNDESTROYABLE_WALL);
    }

    @Override
    public List<YellowGold> getYellowGold() {
        return LevelUtils.getObjects(xy, map,
                YellowGold::new,
                YELLOW_GOLD);
    }

    @Override
    public List<GreenGold> getGreenGold() {
        return LevelUtils.getObjects(xy, map,
                GreenGold::new,
                GREEN_GOLD);
    }

    @Override
    public List<RedGold> getRedGold() {
        return  LevelUtils.getObjects(xy, map,
                RedGold::new,
                RED_GOLD);
    }

    @Override
    public List<Ladder> getLadder() {
        return LevelUtils.getObjects(xy, map,
                Ladder::new,
                LADDER);
    }

    @Override
    public List<Pipe> getPipe() {
        return LevelUtils.getObjects(xy, map,
                Pipe::new,
                PIPE);
    }

    @Override
    public List<Enemy> getEnemies() {
        return LevelUtils.getObjects(xy, map,
                new HashMap<>(){{
                    put(ENEMY_LEFT, pt -> new Enemy(pt, Direction.LEFT, ai, dice));
                    put(ENEMY_RIGHT, pt -> new Enemy(pt, Direction.RIGHT, ai, dice));
                }});
    }

    @Override
    public List<Pill> getPills() {
        return LevelUtils.getObjects(xy, map,
                pt -> new Pill(pt, PillType.SHADOW_PILL),
                SHADOW_PILL);
    }

    @Override
    public List<Portal> getPortals() {
        return LevelUtils.getObjects(xy, map,
                Portal::new,
                PORTAL);
    }

    public void setAI(EnemyAI ai) {
        this.ai = ai;
    }

    @Override
    public EnemyAI getAi() {
        return this.ai;
    }
}
