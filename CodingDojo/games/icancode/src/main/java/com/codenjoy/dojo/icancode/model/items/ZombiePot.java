package com.codenjoy.dojo.icancode.model.items;

/*-
 * #%L
 * Codenjoy - it's a dojo-like platform from developers to developers.
 * %%
 * Copyright (C) 2016 - 2020 Codenjoy
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
import com.codenjoy.dojo.icancode.model.FieldItem;
import com.codenjoy.dojo.services.Tickable;

import static com.codenjoy.dojo.icancode.services.GameSettings.Keys.COUNT_ZOMBIES_ON_MAP;
import static com.codenjoy.dojo.icancode.services.GameSettings.Keys.TICKS_PER_NEW_ZOMBIE;

public class ZombiePot extends FieldItem implements Tickable {

    private int time = 0;

    public ZombiePot() {
        super(Element.ZOMBIE_START);
    }

    @Override
    public void tick() {
        if (++time % field.settings().integer(TICKS_PER_NEW_ZOMBIE) == 0) {
            if (field.zombies().size() < field.settings().integer(COUNT_ZOMBIES_ON_MAP)) {
                field.move(newZombie(), this.getCell());
            }
        }
    }

    private Zombie newZombie() {
        boolean gender = field.dice().next(2) == 0;
        Zombie zombie = new Zombie(gender);
        zombie.setField(field);
        return zombie;
    }

    public void reset() {
        time = 0;
        field.zombies().forEach(Zombie::die);
    }
}
