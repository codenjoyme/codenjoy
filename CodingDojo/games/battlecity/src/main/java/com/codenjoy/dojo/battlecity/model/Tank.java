package com.codenjoy.dojo.battlecity.model;

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


import com.codenjoy.dojo.battlecity.model.items.Bullet;
import com.codenjoy.dojo.battlecity.model.items.Prize;
import com.codenjoy.dojo.battlecity.model.items.Prizes;
import com.codenjoy.dojo.battlecity.model.items.Tree;
import com.codenjoy.dojo.battlecity.services.Events;
import com.codenjoy.dojo.battlecity.services.GameSettings;
import com.codenjoy.dojo.games.battlecity.Element;
import com.codenjoy.dojo.services.*;
import com.codenjoy.dojo.services.round.RoundPlayerHero;
import com.codenjoy.dojo.services.round.Timer;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import static com.codenjoy.dojo.games.battlecity.Element.PRIZE_BREAKING_WALLS;
import static com.codenjoy.dojo.games.battlecity.Element.PRIZE_WALKING_ON_WATER;
import static com.codenjoy.dojo.battlecity.services.GameSettings.Keys.*;
import static com.codenjoy.dojo.services.StateUtils.filterOne;

public class Tank extends RoundPlayerHero<Field> implements State<Element, Player> {

    protected Direction direction;
    protected boolean moving;
    private boolean fire;
    private int score;

    private Gun gun;
    private Sliding sliding;

    private List<Bullet> bullets;
    private Prizes prizes;

    private Timer onWater;

    public Tank(Point pt, Direction direction) {
        super(pt);
        score = 0;
        this.direction = direction;
        bullets = new LinkedList<>();
        prizes = new Prizes();
    }

    @Override
    public GameSettings settings() {
        return (GameSettings) field.settings();
    }

    @Override
    public void up() {
        if (!isActiveAndAlive()) return;

        direction = Direction.UP;
        moving = true;
    }

    @Override
    public void down() {
        if (!isActiveAndAlive()) return;

        direction = Direction.DOWN;
        moving = true;
    }

    @Override
    public void right() {
        if (!isActiveAndAlive()) return;

        direction = Direction.RIGHT;
        moving = true;
    }

    @Override
    public void left() {
        if (!isActiveAndAlive()) return;

        direction = Direction.LEFT;
        moving = true;
    }

    public Direction getDirection() {
        return direction;
    }

    public void move() {
        moving = moving || field.isIce(this);
        if (!moving) return;

        if (sliding.active(this)) {
            direction = sliding.affect(direction);
        } else {
            sliding.reset(direction);
        }

        moving(direction.change(this));
    }

    public void moving(Point pt) {
        if (field.isBarrierFor(this, pt)) {
            sliding.stop();
        } else {
            move(pt);
        }
        moving = false;
    }

    @Override
    public void act(int... p) {
        if (!isActiveAndAlive()) return;

        fire = true;
    }

    public Collection<Bullet> getBullets() {
        return new LinkedList<>(bullets);
    }

    public void init(Field field) {
        super.init(field);

        gun = new Gun(settings());
        sliding = new Sliding(field, direction, settings());

        reset();
        setAlive(true);
    }

    protected int ticksPerShoot() {
        return settings().integer(TANK_TICKS_PER_SHOOT);
    }

    public void kill(Bullet bullet) {
        if (isAlive()) {
            die();
        }
    }

    public void removeBullets() {
        bullets.clear();
    }

    @Override
    public void tick() {
        // TODO добавить проверку if (!isActiveAndAlive()) return;

        gunType();

        gun.tick();
        prizes.tick();

        checkOnWater();
    }

    public void checkOnWater() {
        if (field.isRiver(this) && !prizes.contains(PRIZE_WALKING_ON_WATER)) {
            if (onWater == null || onWater.done()) {
                onWater = new Timer(settings().integerValue(PENALTY_WALKING_ON_WATER));
                onWater.start();
            }
            onWater.tick(() -> {});
        } else {
            onWater = null;
        }
    }

    @Override
    public Element state(Player player, Object... alsoAtPoint) {
        Element tree = player.getHero().treeState(this, alsoAtPoint);
        if (!isAlive()) {
            return Element.BANG;
        }

        if (tree != null) {
            return Element.TREE;
        }

        if (player.getHero() != this) {
            switch (direction) {
                case LEFT:  return Element.OTHER_TANK_LEFT;
                case RIGHT: return Element.OTHER_TANK_RIGHT;
                case UP:    return Element.OTHER_TANK_UP;
                case DOWN:  return Element.OTHER_TANK_DOWN;
                default:    throw new RuntimeException("Неправильное состояние танка!");
            }
        }

        switch (direction) {
            case LEFT:  return Element.TANK_LEFT;
            case RIGHT: return Element.TANK_RIGHT;
            case UP:    return Element.TANK_UP;
            case DOWN:  return Element.TANK_DOWN;
            default:    throw new RuntimeException("Неправильное состояние танка!");
        }
    }

    public Element treeState(Tank tank, Object[] alsoAtPoint) {
        Tree tree = filterOne(alsoAtPoint, Tree.class);
        if (tree == null) {
            return null;
        }

        if (prizes.contains(Element.PRIZE_VISIBILITY)) {
            return null;
        }

        if (tank == this && settings().bool(SHOW_MY_TANK_UNDER_TREE)) {
            return null;
        }

        return Element.TREE;
    }

    public void reset() {
        moving = false;
        fire = false;
        setAlive(true);
        gun.reset();
        bullets.clear();
        prizes.clear();
    }

    public void tryFire() {
        if (!fire) return;
        fire = false;

        if (!gun.tryToFire()) return;

        Direction bulletDirection = this.direction;
        if (sliding.active(this) && !sliding.lastSlipperiness()) {
            bulletDirection = sliding.getPreviousDirection();
        }
        Bullet bullet = new Bullet(field, bulletDirection, copy(), this, b -> Tank.this.bullets.remove(b));

        if (!bullets.contains(bullet)) {
            bullets.add(bullet);
        }
    }

    protected boolean withPrize() {
        return false;
    }

    public Prizes prizes() {
        return prizes;
    }

    public void take(Prize prize) {
        getPlayer().event(Events.CATCH_PRIZE.apply(Integer.valueOf("" + prize.elements().ch)));
        prizes.add(prize);
    }

    private void gunType() {
        if (prizes.contains(PRIZE_BREAKING_WALLS)) {
            gun.reset();
        }
    }

    public boolean canWalkOnWater() {
        return prizes.contains(PRIZE_WALKING_ON_WATER)
                || (onWater != null && onWater.done());
    }

    public int scores() {
        return score;
    }

    public void addScore(int added) {
        score = Math.max(0, score + added);
    }
}
