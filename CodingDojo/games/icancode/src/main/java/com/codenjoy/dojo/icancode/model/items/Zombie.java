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

import com.codenjoy.dojo.icancode.model.*;
import com.codenjoy.dojo.icancode.services.GameSettings;
import com.codenjoy.dojo.services.Direction;
import com.codenjoy.dojo.services.Point;
import com.codenjoy.dojo.services.Tickable;

import static com.codenjoy.dojo.icancode.model.Elements.*;
import static com.codenjoy.dojo.icancode.services.GameSettings.Keys.WALK_EACH_TICKS;

public class Zombie extends FieldItem implements Tickable {

    private ZombieBrain brain;
    private int ticks = 0;
    private boolean die;

    public Zombie(Elements gender) {
        super(gender);
        die = false;
    }

    public Zombie(boolean gender) {
        super(getElement(gender));
        die = false;
    }

    private static Elements getElement(boolean gender) {
        return (gender) ? MALE_ZOMBIE : FEMALE_ZOMBIE;
    }

    @Override
    public Elements state(Player player, Object... alsoAtPoint) {
        if (die) {
            return ZOMBIE_DIE;
        }
        return super.state(player, alsoAtPoint);
    }

    @Override
    public void action(Item item) {
        if (die) return;

        check(item, HeroItem.class)
                .ifPresent(heroItem -> {
                    Hero hero = heroItem.getHero();
                    if (!hero.isFlying()) {
                        removeFromCell();
                        hero.dieOnZombie();
                    }
                });
    }

    @Override
    public void tick() {
        if (die) {
            Cell cell = getCell();
            removeFromCell();
            field.dropNextPerk().ifPresent(cell::add);
            return;
        }

        if (ticks++ % field.settings().integer(WALK_EACH_TICKS) == 0) {
            return;
        }

        Direction direction = brain().whereToGo(getCell(), field);
        if (direction == null) {
            return;
        }
        Point to = direction.change(getCell());

        if (!field.isBarrier(to) && !field.isAt(to, Zombie.class)) {
            field.move(this, to);
        }
    }

    private ZombieBrain brain() {
        return (brain == null)
                ? brain = ((GameSettings)field.settings()).zombieBrain()
                : brain;
    }

    public void die() {
        this.die = true;
    }
}
