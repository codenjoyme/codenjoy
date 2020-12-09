package com.codenjoy.dojo.icancode.model;

import com.codenjoy.dojo.icancode.model.items.HeroItem;
import com.codenjoy.dojo.icancode.model.items.Laser;
import com.codenjoy.dojo.icancode.model.items.LaserMachine;
import com.codenjoy.dojo.icancode.services.SettingsWrapper;
import com.codenjoy.dojo.services.Direction;
import com.codenjoy.dojo.services.Point;
import org.fest.util.Lists;

import java.util.List;
import java.util.Optional;

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
            if (heroItem.getHero().hasDeathRayPerk()) {
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
        boolean unstoppableLaserPerk = heroItem.getHero().hasUnstoppableLaserPerk();
        Laser topLaser = laser;
        topLaser.setDeathRay(true);
        List<Laser> lasers = Lists.newArrayList(topLaser);

        Point to = topLaser.getDirection().change(from);
        field.getCell(to.getX(), to.getY()).add(topLaser);
        for (int i = 0; i < SettingsWrapper.data.getDeathRayRange() - 1; i++) {
            Optional<Cell> nextCell = findNextAvailableCell(topLaser, unstoppableLaserPerk);
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
        laser.setUnstoppable(heroItem.getHero().hasUnstoppableLaserPerk());
        heroItem.getCell().add(laser);
    }
}
