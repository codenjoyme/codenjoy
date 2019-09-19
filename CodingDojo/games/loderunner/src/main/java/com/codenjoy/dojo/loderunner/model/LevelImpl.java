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
import com.codenjoy.dojo.services.Direction;
import com.codenjoy.dojo.services.LengthToXY;
import com.codenjoy.dojo.services.Point;

import java.util.EnumSet;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

import static com.codenjoy.dojo.loderunner.model.Elements.*;
import static java.util.stream.Collectors.toList;

public class LevelImpl implements Level {

    private UUID mapUuid;
    private LengthToXY xy;
    private EnemyAI ai;
    private String map;

    public LevelImpl(String map) {
        this.map = map;
        mapUuid = UUID.randomUUID();
        ai = new AI();
        xy = new LengthToXY(getSize());
    }

    @Override
    public int getSize() {
        return (int) Math.sqrt(map.length());
    }

    @Override
    public List<Hero> getHeroes() {
        return new LinkedList<Hero>() {{
            EnumSet<Elements> heroesGoingLeft = EnumSet
                    .of(HERO_DRILL_LEFT, HERO_LEFT, HERO_FALL_LEFT, HERO_PIPE_LEFT,
                            HERO_SHADOW_DRILL_LEFT,
                            HERO_SHADOW_LEFT, HERO_SHADOW_FALL_LEFT, HERO_SHADOW_PIPE_LEFT);
            heroesGoingLeft.forEach(heroBlocks ->
                    addAll(pointsOf(heroBlocks).stream()
                            .map(pt -> new Hero(pt, Direction.LEFT, () -> 1))
                            .collect(toList())));

            EnumSet<Elements> heroesGoingRight = EnumSet
                    .of(HERO_DRILL_RIGHT, HERO_RIGHT, HERO_FALL_RIGHT, HERO_PIPE_RIGHT,
                            HERO_SHADOW_DRILL_RIGHT,
                            HERO_SHADOW_RIGHT, HERO_SHADOW_FALL_RIGHT, HERO_SHADOW_PIPE_RIGHT);

            heroesGoingRight.forEach(heroBlocks ->
                    addAll(pointsOf(heroBlocks).stream()
                            .map(pt -> new Hero(pt, Direction.RIGHT, () -> 1))
                            .collect(toList())));
        }};
    }

    @Override
    public List<Brick> getBricks() {
        return pointsOf(BRICK).stream()
                .map(Brick::new)
                .collect(toList());
    }

    @Override
    public List<Border> getBorders() {
        return pointsOf(UNDESTROYABLE_WALL).stream()
                .map(Border::new)
                .collect(toList());
    }

    private List<Point> pointsOf(Elements el) {
        List<Point> result = new LinkedList<>();
        for (int index = 0; index < map.length(); index++) {
            if (map.charAt(index) == el.ch()) {
                result.add(xy.getXY(index));
            }
        }
        return result;
    }

    @Override
    public List<Gold> getGold() {
        return pointsOf(GOLD).stream()
                .map(Gold::new)
                .collect(toList());
    }

    @Override
    public List<Ladder> getLadder() {
        return pointsOf(Elements.LADDER).stream()
                .map(Ladder::new)
                .collect(toList());
    }

    @Override
    public List<Pipe> getPipe() {
        return pointsOf(PIPE).stream()
                .map(Pipe::new)
                .collect(toList());
    }

    @Override
    public List<Enemy> getEnemies() {
        return new LinkedList<Enemy>() {{
            addAll(pointsOf(ENEMY_LEFT).stream()
                    .map(pt -> new Enemy(pt, Direction.LEFT, ai))
                    .collect(toList()));

            addAll(pointsOf(ENEMY_RIGHT).stream()
                    .map(pt -> new Enemy(pt, Direction.RIGHT, ai))
                    .collect(toList()));
        }};
    }

    @Override
    public List<Pill> getPills() {
        return pointsOf(THE_SHADOW_PILL).stream() // TODO: add speed pills
                .map(kp -> new Pill(kp, PillType.SHADOW_PILL))
                .collect(toList());
    }

    @Override
    public List<Portal> getPortals() {
        return pointsOf(PORTAL).stream()
                .map(Portal::new)
                .collect(toList());
    }

    @Override
    public void refresh(String map) {
        this.map = map;
        mapUuid = UUID.randomUUID();
        this.xy = new LengthToXY(getSize());
    }

    @Override
    public UUID getMapUUID() {
        return mapUuid;
    }

    public void setAI(EnemyAI ai) {
        this.ai = ai;
    }
}
