package com.epam.dojo.expansion.model.items;

/*-
 * #%L
 * iCanCode - it's a dojo-like platform from developers to developers.
 * %%
 * Copyright (C) 2016 EPAM
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


import com.codenjoy.dojo.services.Direction;
import com.codenjoy.dojo.services.Joystick;
import com.codenjoy.dojo.services.Point;
import com.codenjoy.dojo.services.Tickable;
import com.epam.dojo.expansion.model.Elements;
import com.epam.dojo.expansion.model.Player;
import com.epam.dojo.expansion.model.interfaces.ICell;
import com.epam.dojo.expansion.model.interfaces.IField;
import com.epam.dojo.expansion.model.interfaces.IItem;
import com.epam.dojo.expansion.services.CodeSaver;

import java.util.Arrays;

/**
 * Это реализация героя. Обрати внимание, что он имплементит {@see Joystick}, а значит может быть управляем фреймворком
 * Так же он имплементит {@see Tickable}, что значит - есть возможность его оповещать о каждом тике игры.
 */
public class Hero extends FieldItem implements Joystick, Tickable {

    private boolean alive;
    private boolean win;
    private Direction direction;
    private boolean pull;
    private Integer resetToLevel;
    private int goldCount;

    public Hero(Elements el) {
        super(el);

        resetFlags();
    }

    private void resetFlags() {
        direction = null;
        win = false;
        pull = false;
        resetToLevel = null;
        alive = true;
        goldCount = 0;
    }

    @Override
    public void setField(IField field) {
        super.setField(field);
        reset(field);
    }

    private void reset(IField field) {
        resetFlags();
        field.getStartPosition().addItem(this);
        field.reset();
    }

    @Override
    public Elements state(Player player, Object... alsoAtPoint) {
        if (player.getHero() == this || Arrays.asList(alsoAtPoint).contains(player.getHero())) {
            return Elements.ROBO;
        } else {
            return Elements.ROBO_OTHER;
        }
    }

    @Override
    public void down() {
        if (!alive) {
            return;
        }

        direction = Direction.DOWN;
    }

    @Override
    public void up() {
        if (!alive) {
            return;
        }

        direction = Direction.UP;
    }

    @Override
    public void left() {
        if (!alive) {
            return;
        }

        direction = Direction.LEFT;
    }

    @Override
    public void right() {
        if (!alive) {
            return;
        }

        direction = Direction.RIGHT;
    }

    public void reset() {
        act(0);
    }

    public void loadLevel(int level) {
        act(0, level);
    }

    @Override
    public void act(int... p) {
        if (!alive) {
            return;
        }

        if (p.length == 0 || p[0] == 1) {
            // do nothing
        } else if (p.length == 1 && p[0] == 2) {
            pull = true;
        } else if (p[0] == 0) {
            if (p.length == 2) {
                resetToLevel = p[1];
            } else {
                resetToLevel = -1;
            }
        } else if (p[0] == -1) { // TODO test me
            ICell end = field.getEndPosition();
            field.move(this, end.getX(), end.getY());
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
        if (!alive) {
            return;
        }

        if (resetToLevel != null) {
            resetToLevel = null;
            reset(field);
            return;
        }

        if (direction != null) {
            int x = getCell().getX();
            int y = getCell().getY();

            int newX = direction.changeX(x);
            int newY = direction.changeY(y);

            boolean wasPush = pushBox(newX, newY);

            if (!field.isBarrier(newX, newY)) {
                if (!wasPush) {
                    pullBox(x, y);
                }
                field.move(this, newX, newY);

            }
        }
        direction = null;
    }

    private void pullBox(int x, int y) {
        if (!pull) {
            return;
        }
        int boxX = direction.inverted().changeX(x);
        int boxY = direction.inverted().changeY(y);

        IItem item = field.getIfPresent(Box.class, boxX, boxY);
        if (item == null) {
            return;
        }

        field.move(item, x, y);
        pull = false;
    }

    private boolean pushBox(int x, int y) {
        if (!pull) {
            return false;
        }

        IItem item = field.getIfPresent(Box.class, x, y);

        if (item == null) {
            return false;
        }

        int newX = direction.changeX(x);
        int newY = direction.changeY(y);

        if (field.isBarrier(newX, newY)) {
            return false;
        }

        if (field.isAt(newX, newY, Gold.class, Hero.class)) {
            return false;
        }

        field.move(item, newX, newY);
        return true;
    }

    public Point getPosition() {
        return getCell();
    }

    public boolean isAlive() {
        return alive;
    }

    @Override
    public void action(IItem item) {
        //empty
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

    public int getGoldCount() {
        return goldCount;
    }

    public boolean isChangeLevel() {
        return resetToLevel != null;
    }

    public int getLevel() {
        int result = resetToLevel;
        resetToLevel = null;
        return result;
    }

}
