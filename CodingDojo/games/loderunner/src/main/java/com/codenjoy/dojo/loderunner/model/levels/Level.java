package com.codenjoy.dojo.loderunner.model.levels;

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


import com.codenjoy.dojo.games.loderunner.Element;
import com.codenjoy.dojo.loderunner.model.Hero;
import com.codenjoy.dojo.loderunner.model.items.*;
import com.codenjoy.dojo.loderunner.model.items.Pill.PillType;
import com.codenjoy.dojo.loderunner.model.items.enemy.AI;
import com.codenjoy.dojo.loderunner.model.items.enemy.Enemy;
import com.codenjoy.dojo.loderunner.model.items.enemy.EnemyAI;
import com.codenjoy.dojo.services.Dice;
import com.codenjoy.dojo.services.Direction;
import com.codenjoy.dojo.services.field.AbstractLevel;
import com.codenjoy.dojo.services.field.PointField;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.List;

import static com.codenjoy.dojo.games.loderunner.Element.*;

public class Level extends AbstractLevel {

    public Level(String map) {
        super(map);
    }

    @Override
    protected void fill(PointField field) {
        field.addAll(getBorders());
        field.addAll(getPipe());
        field.addAll(getLadder());
        field.addAll(getBricks());
        field.addAll(getPortals());
    }

    public List<Hero> getHeroes() {
        EnumSet<Element> left = EnumSet.of(
                HERO_DRILL_LEFT, HERO_LEFT, HERO_FALL_LEFT, HERO_PIPE_LEFT,
                HERO_SHADOW_DRILL_LEFT,
                HERO_SHADOW_LEFT, HERO_SHADOW_FALL_LEFT, HERO_SHADOW_PIPE_LEFT);

        EnumSet<Element> right = EnumSet.of(
                HERO_DRILL_RIGHT, HERO_RIGHT, HERO_FALL_RIGHT, HERO_PIPE_RIGHT,
                HERO_SHADOW_DRILL_RIGHT,
                HERO_SHADOW_RIGHT, HERO_SHADOW_FALL_RIGHT, HERO_SHADOW_PIPE_RIGHT);

        return find(new HashMap<>() {{
            left.forEach(element -> put(element, pt -> new Hero(pt, Direction.LEFT)));
            right.forEach(element -> put(element, pt -> new Hero(pt, Direction.RIGHT)));
        }});
    }

    public List<Brick> getBricks() {
        return find(Brick::new, BRICK);
    }

    public List<Border> getBorders() {
        return find(Border::new, UNDESTROYABLE_WALL);
    }

    public List<YellowGold> getYellowGold() {
        return find(YellowGold::new, YELLOW_GOLD);
    }

    public List<GreenGold> getGreenGold() {
        return find(GreenGold::new, GREEN_GOLD);
    }

    public List<RedGold> getRedGold() {
        return find(RedGold::new, RED_GOLD);
    }

    public List<Ladder> getLadder() {
        return find(Ladder::new, LADDER);
    }

    public List<Pipe> getPipe() {
        return find(Pipe::new, PIPE);
    }

    public List<Enemy> getEnemies() {
        return find(new HashMap<>() {{
            put(ENEMY_LEFT, pt -> new Enemy(pt, Direction.LEFT));
            put(ENEMY_RIGHT, pt -> new Enemy(pt, Direction.RIGHT));
        }});
    }

    public List<Pill> getPills() {
        return find(pt -> new Pill(pt, PillType.SHADOW_PILL), SHADOW_PILL);
    }

    public List<Portal> getPortals() {
        return find(Portal::new, PORTAL);
    }
}
