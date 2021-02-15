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
import org.fest.util.Lists;

import java.util.List;
import java.util.Optional;

// TODO refactoring needed
public class Shooter {

    private final Field field;

    public Shooter(Field field) {
        this.field = field;
    }

    public void fire(Direction direction, Point from, FieldItem owner) {
        if (owner instanceof LaserMachine) {
            LaserMachine laserMachine = (LaserMachine) owner;
            fireByLaserMachine(direction, from, laserMachine);
        } else if (owner instanceof HeroItem) {
            HeroItem heroItem = (HeroItem) owner;
            Laser laser = new Laser(heroItem.getHero(), direction, field);
            if (heroItem.getHero().has(DeathRayPerk.class)) {
                fireDeathRayByHero(laser, from, heroItem);
            } else {
                fireRegularLaserByHero(laser, heroItem);
            }
        }
    }

    public void fireByLaserMachine(Direction direction, Point from, LaserMachine owner) {
        Point to = direction.change(from);
        if (!field.isBarrier(to.getX(),to.getY())) {
            field.move(new Laser(owner, direction, field), to.getX(), to.getY());
        }
    }

    public void fireDeathRayByHero(Laser laser, Point from, HeroItem heroItem) {
        boolean perk = heroItem.getHero().has(UnstoppableLaserPerk.class);
        Laser topLaser = laser;
        topLaser.setDeathRay(true);
        List<Laser> lasers = Lists.newArrayList(topLaser);

        Point to = topLaser.getDirection().change(from);
        field.getCell(to.getX(), to.getY()).add(topLaser);
        for (int i = 0; i < SettingsWrapper.data.getDeathRayRange() - 1; i++) {
            Optional<Cell> nextCell = findNextAvailableCell(topLaser, perk);
            if (!nextCell.isPresent()) {
                break;
            }
            topLaser = new Laser(heroItem.getHero(), topLaser.getDirection(), field);
            topLaser.setDeathRay(true);
            nextCell.get().add(topLaser);
            lasers.add(topLaser);
        }
    }

    private Optional<Cell> findNextAvailableCell(Laser laser, boolean unstoppableLaser) {
        Point point = laser.getDirection().change(laser.getCell());
        while (!point.isOutOf(field.size())) {
            if (!field.isBarrier(point.getX(), point.getY())) {
                return Optional.of(field.getCell(point.getX(), point.getY()));
            } else if (field.isBarrier(point.getX(), point.getY()) && !unstoppableLaser) {
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
