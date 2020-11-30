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


import com.codenjoy.dojo.icancode.model.items.Box;
import com.codenjoy.dojo.icancode.model.items.Gold;
import com.codenjoy.dojo.icancode.model.items.HeroItem;
import com.codenjoy.dojo.icancode.model.items.LaserMachine;
import com.codenjoy.dojo.icancode.services.CodeSaver;
import com.codenjoy.dojo.services.Direction;
import com.codenjoy.dojo.services.Point;
import com.codenjoy.dojo.services.State;
import com.codenjoy.dojo.services.multiplayer.PlayerHero;

import java.util.Arrays;

public class Hero extends PlayerHero<Field> implements State<Elements, Player> {

    private boolean alive;
    private boolean win;
    private Direction direction;
    private boolean jump;
    private boolean pull;
    private boolean flying;
    private boolean reset;
    private boolean laser;
    private boolean fire;
    private Direction fireDirection;
    private boolean hole;
    private boolean landOn;
    private int goldCount;
    private int killZombieCount;
    private int killHeroCount;
    private HeroItem item;

    public void removeFromCell() {
        item.removeFromCell();
    }

    public FieldItem getItem() {
        return item;
    }

    public Hero(Elements el) {
        item = new HeroItem(el);
        item.init(this);

        resetFlags();
    }

    private void resetFlags() {
        direction = null;
        win = false;
        jump = false;
        fire = false;
        pull = false;
        landOn = false;
        reset = false;
        flying = false;
        laser = false;
        alive = true;
        goldCount = 0;
        resetZombieKillCount();
        resetHeroKillCount();
    }

    public void resetZombieKillCount() {
        killZombieCount = 0;
    }

    public void resetHeroKillCount() {
        killHeroCount = 0;
    }

    @Override
    public void init(Field field) {
        super.init(field);
        item.setField(field);
        reset(field);
    }

    private void reset(Field field) {
        resetFlags();
        field.getStartPosition().add(this.item);
        field.reset();
    }

    @Override
    public Elements state(Player player, Object... alsoAtPoint) {
        if (player.getHero() == this || Arrays.asList(alsoAtPoint).contains(player.getHero().item)) {
            if (flying) {
                return Elements.ROBO_FLYING;
            }
            if (laser) {
                return Elements.ROBO_LASER;
            }
            if (hole) {
                return Elements.ROBO_FALLING;
            }
            return Elements.ROBO;
        } else {
            if (flying) {
                return Elements.ROBO_OTHER_FLYING;
            }
            if (laser) {
                return Elements.ROBO_OTHER_LASER;
            }

            if (hole) {
                return Elements.ROBO_OTHER_FALLING;
            }
            return Elements.ROBO_OTHER;
        }
    }

    @Override
    public void down() {
        if (!alive) {
            return;
        }

        if (!flying) {
            direction = Direction.DOWN;
        }
    }

    @Override
    public void up() {
        if (!alive) {
            return;
        }

        if (!flying) {
            direction = Direction.UP;
        }
    }

    @Override
    public void left() {
        if (!alive) {
            return;
        }

        if (!flying) {
            direction = Direction.LEFT;
        }
    }

    @Override
    public void right() {
        if (!alive) {
            return;
        }

        if (!flying) {
            direction = Direction.RIGHT;
        }
    }

    public void reset() {
        act(0);
    }

    public void jump() {
        act(1);
    }

    public void pull() {
        act(2);
    }

    public void fire() {
        act(3);
    }

    @Override
    public void act(int... p) {
        if (!alive) {
            return;
        }

        if (p.length == 0 || p[0] == 1) {
            if (!flying) {
                jump = true;
            }
        } else if (p.length == 1 && p[0] == 2) {
            pull = true;
        } else if (p.length == 1 && p[0] == 3) { // TODO test me
            fire = true;
        } else if (p[0] == 0) {
            reset = true;
        } else if (p[0] == -1) { // TODO test me
            Cell end = field.getEndPosition();
            field.move(item, end.getX(), end.getY());
        }
    }

    @Override
    public void message(String command) {
        try {
            String[] parts = command.split("\\|\\$\\%\\&\\|");
            String user = parts[0];
            long date = Long.valueOf(parts[1]);
            int index = Integer.valueOf(parts[2]);
            int count = Integer.valueOf(parts[3]);
            String part = parts[4];
            CodeSaver.save(user, date, index, count, part);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Direction getDirection() {
        return direction;
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    @Override
    public boolean equals(Object o) {
        return super.equals(o);
    }

    @Override
    public void tick() {
        laser = false;
        hole = false;
        if (!alive) {
            return;
        }

        if (reset) {
            reset = false;
            reset(field);
            return;
        }

        if (flying) {
            flying = false;
            landOn = true;
        }

        if (jump) {
            flying = true;
            jump = false;
        }

        if (fire) {
            fireDirection = direction;
            fire = false;
            direction = null;
            fireLaser();
        }

        if (direction != null) {
            int x = item.getCell().getX();
            int y = item.getCell().getY();

            int newX = direction.changeX(x);
            int newY = direction.changeY(y);

            if (flying && (field.isAt(newX, newY, Box.class) || field.isAt(newX, newY, LaserMachine.class))) {
                int nextX = direction.changeX(newX);
                int nextY = direction.changeY(newY);
                if (!field.isBarrier(nextX, nextY)) {
                    field.move(item, newX, newY);
                }
            } else {
                if (pull && !landOn) {
                    if (tryPushBox(newX, newY)) {
                        pull = false;
                    }
                }

                if (field.isBarrier(newX, newY)) {
                    if (landOn) {
                        item.getCell().comeIn(item);
                    }
                } else {
                    if (pull) {
                        if (tryPullBox(x, y)) {
                            pull = false;
                        }
                    }
                    field.move(item, newX, newY);
                }
            }
        }
        if (!flying) {
            direction = null;
        }
        landOn = false;
        pull = false;
    }

    public void fixLayer() {
        if (flying) {
            item.getCell().jump(item);
        } else {
            item.getCell().landOn(item);
        }
    }

    private boolean tryPullBox(int x, int y) {
        int boxX = direction.inverted().changeX(x);
        int boxY = direction.inverted().changeY(y);

        Item item = field.getIfPresent(Box.class, boxX, boxY);
        if (item == null) {
            return false;
        }

        field.move(item, x, y);
        return true;
    }

    private boolean tryPushBox(int x, int y) {
        Item item = field.getIfPresent(Box.class, x, y);

        if (item == null) {
            return false;
        }

        int newX = direction.changeX(x);
        int newY = direction.changeY(y);

        if (field.isBarrier(newX, newY)) {
            return false;
        }

        if (field.isAt(newX, newY, HeroItem.class)) {
            return false;
        }

        Gold gold = (Gold) field.getIfPresent(Gold.class, newX, newY);
        if (gold != null && !gold.getHidden()) {
            return false;
        }

        field.move(item, newX, newY);
        return true;
    }

    public Point getPosition() {
        return item.getCell();
    }

    public boolean isAlive() {
        return alive;
    }

    public void setWin() {
        win = true;
    }

    public void die() {
        alive = false;
    }

    public boolean isWin() {
        return win;
    }

    public void pickUpGold() {
        goldCount++;
    }

    public void addZombieKill() {
        killZombieCount++;
    }

    public void addHeroKill() {
        killHeroCount++;
    }

    public int getGoldCount() {
        return goldCount;
    }

    public int getKillZombieCount() {
        return killZombieCount;
    }

    public int getKillHeroCount() {
        return killHeroCount;
    }

    public boolean isFlying() {
        return flying;
    }

    public void dieOnHole() {
        hole = true;
        die();
    }

    public void dieOnLaser() {
        laser = true;
        die();
    }

    public void dieOnZombie() {
        laser = true; // TODO может сделать зеленым его?
        die();
    }

    private void fireLaser() {
        if (fireDirection != null) {
            field.fire(this, fireDirection, item.getCell());
            fireDirection = null;
        }
    }
}
