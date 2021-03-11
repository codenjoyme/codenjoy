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

import com.codenjoy.dojo.icancode.model.items.HeroItem;
import com.codenjoy.dojo.icancode.model.items.Laser;
import com.codenjoy.dojo.icancode.model.items.LaserMachine;
import com.codenjoy.dojo.icancode.model.items.perks.DeathRayPerk;
import com.codenjoy.dojo.icancode.model.items.perks.UnstoppableLaserPerk;
import com.codenjoy.dojo.services.Direction;
import com.codenjoy.dojo.services.Point;

import static com.codenjoy.dojo.icancode.services.GameSettings.Keys.DEATH_RAY_PERK_RANGE;

public class Shooter {

    private Field field;

    public Shooter(Field field) {
        this.field = field;
    }

    public void fire(Direction direction, Point from, FieldItem owner) {
        if (owner instanceof LaserMachine) {
            LaserMachine item = (LaserMachine) owner;
            fireByLaserMachine(direction, from, item);
        } else if (owner instanceof HeroItem) {
            HeroItem item = (HeroItem) owner;
            Hero hero = item.getHero();

            Laser laser = new Laser(hero, direction, field);
            if (hero.has(DeathRayPerk.class)) {
                fireDeathRayByHero(laser, from, item);
            } else {
                fireRegularLaserByHero(laser, item);
            }
        }
    }

    public void fireByLaserMachine(Direction direction, Point from, LaserMachine owner) {
        Point to = direction.change(from);
        if (!field.isBarrier(to)) {
            Laser laser = new Laser(owner, direction, field);
            field.move(laser, to);
        }
    }

    public void fireDeathRayByHero(Laser laser, Point from, HeroItem heroItem) {
        Hero hero = heroItem.getHero();
        boolean perk = hero.has(UnstoppableLaserPerk.class);
        laser.deathRay(true);
        Point to = laser.getDirection().change(from);
        field.getCell(to).add(laser);
        for (int range = 0; range < field.settings().integer(DEATH_RAY_PERK_RANGE) - 1; range++) {
            Cell cell = nextAvailable(laser, perk);
            if (cell == null) {
                break;
            }
            laser = new Laser(hero, laser.getDirection(), field);
            laser.deathRay(true);
            cell.add(laser);
        }
    }

    private Cell nextAvailable(Laser laser, boolean unstoppable) {
        Direction direction = laser.getDirection();
        Point to = direction.change(laser.getCell());
        while (!to.isOutOf(field.size())) {
            if (!field.isBarrier(to)) {
                return field.getCell(to);
            }

            if (field.isBarrier(to) && !unstoppable) {
                return null;
            }

            to = direction.change(to);
        }
        return null;
    }

    public void fireRegularLaserByHero(Laser laser, HeroItem heroItem) {
        laser.unstoppable(heroItem.getHero().has(UnstoppableLaserPerk.class));
        heroItem.getCell().add(laser);
    }
}
