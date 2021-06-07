package com.codenjoy.dojo.icancode.model;

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


import com.codenjoy.dojo.games.icancode.Element;
import com.codenjoy.dojo.icancode.model.items.*;
import com.codenjoy.dojo.icancode.model.items.perks.*;

import java.util.EnumMap;
import java.util.Map;
import java.util.function.Function;

import static com.codenjoy.dojo.games.icancode.Element.*;

public class ElementMapper {

    public static Map<Element, Function<Element, ? extends BaseItem>> map =
            new EnumMap<>(Element.class)
    {{
        put(EMPTY, el -> new Air());
        put(FLOOR, el -> new Floor());

        put(ANGLE_IN_LEFT, Wall::new);
        put(WALL_FRONT, Wall::new);
        put(ANGLE_IN_RIGHT, Wall::new);
        put(WALL_RIGHT, Wall::new);
        put(ANGLE_BACK_RIGHT, Wall::new);
        put(WALL_BACK, Wall::new);
        put(ANGLE_BACK_LEFT, Wall::new);
        put(WALL_LEFT, Wall::new);
        put(WALL_BACK_ANGLE_LEFT, Wall::new);
        put(WALL_BACK_ANGLE_RIGHT, Wall::new);
        put(ANGLE_OUT_RIGHT, Wall::new);
        put(ANGLE_OUT_LEFT, Wall::new);
        put(SPACE, Wall::new);

        put(LASER_MACHINE_CHARGING_LEFT, LaserMachine::new);
        put(LASER_MACHINE_CHARGING_RIGHT, LaserMachine::new);
        put(LASER_MACHINE_CHARGING_UP, LaserMachine::new);
        put(LASER_MACHINE_CHARGING_DOWN, LaserMachine::new);

        put(LASER_MACHINE_READY_LEFT, LaserMachine::new);
        put(LASER_MACHINE_READY_RIGHT, LaserMachine::new);
        put(LASER_MACHINE_READY_UP, LaserMachine::new);
        put(LASER_MACHINE_READY_DOWN, LaserMachine::new);

        put(START, el -> new Start());
        put(EXIT, el -> new Exit());
        put(HOLE, el -> new Hole());
        put(BOX, el -> new Box());
        put(GOLD, el -> new Gold());

        put(ROBO, HeroItem::new);
        put(ROBO_FALLING, HeroItem::new);
        put(ROBO_FLYING, HeroItem::new);
        put(ROBO_LASER, HeroItem::new);

        put(ROBO_OTHER, HeroItem::new);
        put(ROBO_OTHER_FALLING, HeroItem::new);
        put(ROBO_OTHER_FLYING, HeroItem::new);
        put(ROBO_OTHER_LASER, HeroItem::new);

        put(LASER_LEFT, Laser::new);
        put(LASER_RIGHT, Laser::new);
        put(LASER_UP, Laser::new);
        put(LASER_DOWN, Laser::new);

        put(ZOMBIE_START, el -> new ZombiePot());
        put(FEMALE_ZOMBIE, Zombie::new);
        put(MALE_ZOMBIE, Zombie::new);

        put(UNSTOPPABLE_LASER_PERK, el -> new UnstoppableLaserPerk());
        put(DEATH_RAY_PERK, el -> new DeathRayPerk());
        put(UNLIMITED_FIRE_PERK, el -> new UnlimitedFirePerk());
        put(FIRE_PERK, el -> new FirePerk());
        put(JUMP_PERK, el -> new JumpPerk());
        put(MOVE_BOXES_PERK, el -> new MoveBoxesPerk());

        put(FOG, Wall::new);
        put(BACKGROUND, Wall::new);
    }};

    public static BaseItem get(Element element) {
        Function<Element, ? extends BaseItem> result = map.get(element);
        if (result == null) {
            throw new IllegalArgumentException("Please add element class: " + element.getClass());
        }
        return result.apply(element);
    }

}
