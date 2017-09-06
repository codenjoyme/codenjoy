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


import com.codenjoy.dojo.services.DLoggerFactory;
import com.codenjoy.dojo.services.Joystick;
import com.codenjoy.dojo.services.Point;
import com.codenjoy.dojo.services.Tickable;
import com.codenjoy.dojo.services.joystick.MessageJoystick;
import com.codenjoy.dojo.utils.JsonUtils;
import com.epam.dojo.expansion.client.Command;
import com.epam.dojo.expansion.model.Expansion;
import com.epam.dojo.expansion.model.Forces;
import com.epam.dojo.expansion.model.ForcesMoves;
import com.epam.dojo.expansion.model.interfaces.IField;
import com.epam.dojo.expansion.services.CodeSaver;
import org.apache.commons.lang.StringUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class Hero extends MessageJoystick implements Joystick, Tickable {

    private static Logger logger = DLoggerFactory.getLogger(Hero.class);

    // TODO move to constant
    public static final int INITIAL_FORCES = 10;

    public static final String MOVEMENTS_KEY = Command.MOVEMENTS_KEY;
    public static final String INCREASE_KEY = Command.INCREASE_KEY;

    public int forcesPerTick;
    private boolean alive;
    private boolean win;
    private Integer resetToLevel;
    private int goldCount;
    private List<ForcesMoves> increase;
    private List<ForcesMoves> movements;
    private IField field;
    private Point position;
    private JSONObject currentAction;
    private JSONObject lastAction;

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
        lastAction = null;
        currentAction = null;
    }

    public void setField(IField field) {
        this.field = field;
        resetOn(field);
    }

    private void resetOn(IField field) {
        resetFlags();
        position = occupyFreeBase().getCell().copy();
        field.reset();

        field.startMoveForces(this, position.getX(), position.getY(), INITIAL_FORCES)
                .move();
    }

    public Start occupyFreeBase() {
        Start start = field.getFreeBase();
        if (start == null) {
            // TODO this should never happen :)
            System.out.println(String.format(
                    "Hero %s wants to find new base for play on map %s, but cant do it",
                    this, field));

            return null;
        }
        start.setOwner(this);
        return start;
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
        if (logger.isDebugEnabled()) {
            logger.debug("Hero {} gets message from client {}", lg.id(), command);
        }

        if (StringUtils.isEmpty(command)) {
            return;
        }
        if (command.contains("$%&")) {
            parseSaveCodeMessage(command);
        } else {
            parseMoveMessage(command);
        }
    }

    private void parseMoveMessage(String json) {
        if (!alive) {
            return;
        }

        currentAction = new JSONObject(json);
        increase = parseForces(currentAction, INCREASE_KEY);
        movements = parseForces(currentAction, MOVEMENTS_KEY);
    }

    private List<ForcesMoves> parseForces(JSONObject data, String key) {
        if (!data.has(key)) {
            return null;
        }
        List<ForcesMoves> result = new LinkedList<>();
        for (Object element : data.getJSONArray(key)) {
            JSONObject json = (JSONObject) element;
            ForcesMoves forces = new ForcesMoves(json);
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
        if (lastAction == currentAction) {
            lastAction = null;
            currentAction = null;
        } else {
            lastAction = currentAction;
        }
        if (!alive) {
            return;
        }

        if (resetToLevel != null) {
            resetToLevel = null;
            resetOn(field);
            return;
        }

        if (increase != null) {
            field.increase(this, increase);
        }

        if (movements != null) {
            field.move(this, movements);
        }

        setPosition();

        increase = null;
        movements = null;
    }

    private void setPosition() {
        if (movements != null && !movements.isEmpty()) {
            ForcesMoves last = movements.get(movements.size() - 1);
            Point from = last.getRegion();
            Point to = last.getDestination(from);
            position = to;
        } else if (increase != null && !increase.isEmpty()) {
            Forces last = increase.get(increase.size() - 1);
            position = last.getRegion();
        } else {
            position = getBasePosition();
        }
    }

    public Start getBase() {
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

    public Point getBasePosition() {
        return getBase().getCell().copy();
    }

    public void applyGold() {
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

    public JSONObject getCurrentAction() {
        return (currentAction != null) ? currentAction : new JSONObject();
    }

    public void destroy() {
        field.removeFromCell(this);
    }

    public class LogState {
        public JSONObject json() {
            return new JSONObject(){{
                put("id", id());
                put("forcesPerTick", forcesPerTick);
                put("alive", alive);
                put("win", win);
                put("goldCount", goldCount);
                put("resetToLevel", resetToLevel);
                put("position", position);
                put("lastAction", lastAction);
                put("field", ((Expansion)field).lg.id());
            }};
        }

        public String id() {
            return "H@" + Integer.toHexString(Hero.this.hashCode());
        }
    }

    public LogState lg = new LogState();

    @Override
    public String toString() {
        return JsonUtils.toStringSorted(lg.json());
    }

}
