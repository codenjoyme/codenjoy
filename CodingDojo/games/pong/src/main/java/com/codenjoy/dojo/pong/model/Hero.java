package com.codenjoy.dojo.pong.model;

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

import com.codenjoy.dojo.services.*;
import com.codenjoy.dojo.services.joystick.NoActJoystick;
import com.codenjoy.dojo.services.multiplayer.PlayerHero;

import java.util.LinkedList;
import java.util.List;

public class Hero extends PlayerHero<Field> implements NoActJoystick {

    private Direction direction;
    private List<Panel> panel = new LinkedList<>();

    public Hero(Point xy) {
        super(xy);
        // hero - panel of three parts
        panel.add(new Panel(x, y - 1, this));
        panel.add(new Panel(x, y, this));
        panel.add(new Panel(x, y + 1, this));

        direction = null;
    }

    public void init(Field field) {
        this.field = field;
    }

    @Override
    public void down() {
        direction = Direction.DOWN;
    }

    @Override
    public void up() {
        direction = Direction.UP;
    }

    @Override
    public void left() {
        // do nothing, this should never happen
    }

    @Override
    public void right() {
        // do nothing, this should never happen
    }

    @Override
    public void tick() {
        if (direction != null) {
            changePanelPosition(direction);
        }

        direction = null;
    }

    private void changePanelPosition(Direction direction) {
        // TODO magic :)
        Panel bottom = panel.get(0);
        Panel top = panel.get(panel.size() - 1);
        Panel head = direction == Direction.DOWN ? bottom : top;
        if (isValidDirection(head, direction)) {
            for (Panel panel : panel) {
                Point pt = direction.change(panel.copy());
                panel.move(pt);
            }
        }
    }

    private boolean isValidDirection(Panel head, Direction direction) {
        Point pt = direction.change(head.copy());
        return !field.isBarrier(pt);
    }

    public List<Panel> getPanel() {
        return panel;
    }

    public boolean isAlive() {
        return true;
    }
}
