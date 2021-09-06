package com.codenjoy.dojo.quake2d.model;

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

import com.codenjoy.dojo.services.field.AbstractLevel;

import java.util.List;

import static com.codenjoy.dojo.games.quake2d.Element.*;

public class Level extends AbstractLevel {

    public Level(String map) {
        super(map);
    }

    public List<Hero> hero() {
        return find(Hero::new, HERO);
    }

    public List<Robot> robots(Field field) {
        return find(Robot::new, ROBOT);
    }

    public List<Hero> otherHero() {
        return find(Hero::new, OTHER_HERO);
    }

    public List<Ability> ability() {
        return find(Ability::new,
                SUPER_WEAPON,
                SUPER_DEFENCE);
    }

    public List<Wall> walls() {
        return find(Wall::new, WALL);
    }


}
