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
import com.codenjoy.dojo.icancode.model.perks.DeathRayPerk;
import com.codenjoy.dojo.icancode.model.perks.UnstoppableLaserPerk;
import com.codenjoy.dojo.icancode.services.SettingsWrapper;
import com.codenjoy.dojo.services.Direction;
import com.codenjoy.dojo.services.Point;

import java.util.Optional;

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
        if (!field.isBarrier(to.getX(), to.getY())) {
            Laser laser = new Laser(owner, direction, field);
            field.move(laser, to.getX(), to.getY());
        }
    }

    public void fireDeathRayByHero(Laser laser, Point from, HeroItem heroItem) {
        Hero hero = heroItem.getHero();
        boolean perk = hero.has(UnstoppableLaserPerk.class);

        laser.setDeathRay(true);

        Point to = laser.getDirection().change(from);
        field.getCell(to.getX(), to.getY()).add(laser);
        for (int i = 0; i < SettingsWrapper.data.getDeathRayRange() - 1; i++) {
            Optional<Cell> next = findAvailable(laser, perk);
            if (!next.isPresent()) {
                break;
            }
            laser = new Laser(hero, laser.getDirection(), field);
            laser.setDeathRay(true);
            next.get().add(laser);
        }
    }

    private Optional<Cell> findAvailable(Laser laser, boolean unstoppable) {
        Point point = laser.getDirection().change(laser.getCell());
        while (!point.isOutOf(field.size())) {
            if (!field.isBarrier(point.getX(), point.getY())) {
                return Optional.of(field.getCell(point.getX(), point.getY()));
            } else if (field.isBarrier(point.getX(), point.getY()) && !unstoppable) {
                return Optional.empty();
            }
            point = laser.getDirection().change(point);
        }
        return Optional.empty();
    }

    public void fireRegularLaserByHero(Laser laser, HeroItem heroItem) {
        laser.setUnstoppable(heroItem.getHero().has(UnstoppableLaserPerk.class));
        heroItem.getCell().add(laser);
    }
}
