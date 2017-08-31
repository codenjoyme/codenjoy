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


import com.codenjoy.dojo.services.Joystick;
import com.codenjoy.dojo.services.Point;
import com.codenjoy.dojo.services.Tickable;
import com.codenjoy.dojo.services.joystick.MessageJoystick;
import com.epam.dojo.expansion.model.Forces;
import com.epam.dojo.expansion.model.interfaces.ICell;
import com.epam.dojo.expansion.model.interfaces.IField;
import com.epam.dojo.expansion.services.CodeSaver;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class Hero extends MessageJoystick implements Joystick, Tickable {

    // TODO move to constant
    public static final int INITIAL_FORCES = 10;

    public static final String MOVEMENTS_KEY = "move";
    public static final String INCREASE_KEY = "tryIncreaseForces";

    public int forcesPerTick;
    private boolean alive;
    private boolean win;
    private Integer resetToLevel;
    private int goldCount;
    private List<Forces> increase;
    private List<Forces> movements;
    private IField field;
    private Point position;

    public Hero() {
        resetFlags();
    }

    private void resetFlags() {
        increase = null;
        movements = null;
        forcesPerTick = INITIAL_FORCES;
        win = false;
        resetToLevel = null;
        alive = true;
        goldCount = 0;
        position = null;
    }

    public void setField(IField field) {
        this.field = field;
        reset(field);
    }

    private void reset(IField field) {
        resetFlags();
        setPosition();
        field.reset();

        HeroForces forces = field.tryIncreaseForces(this, position.getX(), position.getY(), INITIAL_FORCES);
        forces.increase();
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

        if (p.length == 0) return;

        if (p[0] == 0) {
            if (p.length == 2) {
                resetToLevel = p[1];
            } else {
                resetToLevel = -1;
            }
        }
    }

    @Override
    public void message(String command) {
        if (command.contains("$%&")) {
            parseSaveCodeMessage(command);
        } else {
            parseMoveMessage(command);
        }
    }

    /* format
    {'tryIncreaseForces':
        [
            {region:{x:3, y:0}, count:1},
            {region:{x:2, y:2}, count:2},
            {region:{x:1, y:3}, count:2},
            {region:{x:0, y:4}, count:3}
        ],
    'move':
        [
            {region:{x:3, y:0}, direction:'right', count:1},
            {region:{x:2, y:2}, direction:'up', count:3},
            {region:{x:1, y:3}, direction:'right_down', count:20},
            {region:{x:0, y:4}, direction:'left_top', count:5}
        ]}
    */
    private void parseMoveMessage(String json) {
        JSONObject data = new JSONObject(json);
        increase = parseForces(data, INCREASE_KEY);
        movements = parseForces(data, MOVEMENTS_KEY);
    }

    private List<Forces> parseForces(JSONObject data, String key) {
        if (!data.has(key)) {
            return null;
        }
        List<Forces> result = new LinkedList<>();
        for (Object element : data.getJSONArray(key)) {
            JSONObject json = (JSONObject) element;
            Forces forces = new Forces(json);
            result.add(forces);
        }
        return result;
    }

    private void parseSaveCodeMessage(String command) {
        try { // TODO подумать и исправить это безобразие
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

        if (increase != null) {
            increase();
        }

        if (movements != null) {
            move();
        }

        setPosition();

        increase = null;
        movements = null;
    }

    private void move() {
        List<HeroForces> toIncrease = new LinkedList<>();
        for (Forces forces : movements) {
            Point from = forces.getRegion();
            Point to = forces.getDestination(from);

            if (from.equals(to)) continue;
            if (forces.getCount() < 0) continue;
            if (field.isBarrier(to.getX(), to.getY())) continue;

            int count = field.decreaseForces(this, from.getX(), from.getY(), forces.getCount());

            HeroForces heroForces = field.tryIncreaseForces(this, to.getX(), to.getY(), count);
            toIncrease.add(heroForces);
        }

        for (HeroForces forces : toIncrease) {
            forces.increase();
        }
    }

    private void increase() {
        int total = forcesPerTick;
        for (Forces forces : increase) {
            Point to = forces.getRegion();

            if (forces.getCount() < 0) continue;
            if (field.isBarrier(to.getX(), to.getY())) continue;

            int count = Math.min(total, forces.getCount());
            int actual = field.countForces(this, to.getX(), to.getY());
            if (actual > 0) {
                total -= count;
                HeroForces heroForces = field.tryIncreaseForces(this, to.getX(), to.getY(), count);
                heroForces.increase();
            }
        }
    }

    private void setPosition() {
        if (movements != null && !movements.isEmpty()) {
            Forces last = movements.get(movements.size() - 1);
            Point from = last.getRegion();
            Point to = last.getDestination(from);
            position = to;
        } else if (increase != null && !increase.isEmpty()) {
            Forces last = increase.get(increase.size() - 1);
            position = last.getRegion();
        } else {
            position = getBase();
        }
    }

    public ICell getBase() {
        return field.getBaseOf(this);
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

    public boolean isChangeLevel() {
        return resetToLevel != null;
    }

    public int getLevel() {
        int result = resetToLevel;
        resetToLevel = null;
        return result;
    }

    public Point getPosition() {
        return position;
    }


    public void increaseArmy() {
        forcesPerTick += goldCount;
        goldCount = 0;
    }

    // ----------- only for testing methods -------------

    public void remove(Forces forces) {
        Point region = forces.getRegion();
        field.removeForces(this, region.getX(), region.getY());
    }

    public void increase(final Forces... forces) {
        message(new JSONObject(){{
            put(INCREASE_KEY, new JSONArray(forces));
        }}.toString());
    }

    public void move(final Forces... forces) {
        message(new JSONObject(){{
            put(MOVEMENTS_KEY, new JSONArray(forces));
        }}.toString());
    }

    public void increaseAndMove(final Forces forcesToIncrease, final Forces forcesToMove) {
        message(new JSONObject(){{
            put(INCREASE_KEY, new JSONArray(Arrays.asList(forcesToIncrease)));
            put(MOVEMENTS_KEY, new JSONArray(Arrays.asList(forcesToMove)));
        }}.toString());
    }

    public int getForcesPerTick() {
        return forcesPerTick;
    }
}
