package com.codenjoy.dojo.expansion.model.levels.items;

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


import com.codenjoy.dojo.expansion.client.Command;
import com.codenjoy.dojo.expansion.model.*;
import com.codenjoy.dojo.expansion.services.CodeSaver;
import com.codenjoy.dojo.services.DLoggerFactory;
import com.codenjoy.dojo.services.Point;
import com.codenjoy.dojo.services.Tickable;
import com.codenjoy.dojo.services.joystick.MessageJoystick;
import com.codenjoy.dojo.services.multiplayer.PlayerHero;
import com.codenjoy.dojo.utils.JsonUtils;
import org.apache.commons.lang3.StringUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import static com.codenjoy.dojo.expansion.services.SettingsWrapper.data;

public class Hero extends PlayerHero<IField> implements MessageJoystick, Tickable {

    private static Logger logger = DLoggerFactory.getLogger(Hero.class);

    public static final String MOVEMENTS_KEY = Command.MOVEMENTS_KEY;
    public static final String INCREASE_KEY = Command.INCREASE_KEY;

    private boolean alive;
    private boolean win;
    private List<Gold> gold;
    private List<ForcesMoves> increase;
    private List<ForcesMoves> movements;
    private Point position;
    private JSONObject currentAction;
    private JSONObject lastAction;

    public Hero() {
        resetFlags();
    }

    private void resetFlags() {
        increase = null;
        movements = null;
        win = false;
        alive = true;
        gold = new LinkedList<>();
        position = null;
        lastAction = null;
        currentAction = null;
    }

    @Override
    public void init(IField field) {
        super.init(field);
        resetOn(field);
    }

    private void resetOn(IField field) {
        resetFlags();
        position = occupyFreeBase().getCell().copy();
        field.reset();

        field.startMoveForces(this, position.getX(), position.getY(), data.initialForce())
                .move();
    }

    public Start occupyFreeBase() {
        Start start = field.getFreeBase();
        if (start == null) {
            System.out.println(String.format(
                    "Hero %s wants to find new base for play on map %s, but cant do it {}",
                    lg.id(), field, lg.json()));

            throw new BusyMapException("Hero wants to find new base for play on map, but cant do it");
        }
        start.setOwner(this);
        return start;
    }

    public void reset() {
        die();
    }

    @Override
    public void act(int... p) {
        if (p.length == 0) return;

        if (p[0] == 0) {
            reset();
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
        // так как герои все у нас с координатами -1 -1 то различать их надо по старинке
        return parentHashCode();
    }

    @Override
    public boolean equals(Object o) {
        // так как герои все у нас с координатами -1 -1 то различать их надо по старинке
        return parentEquals(o);
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

    public void pickUpGold(Gold gold) {
        this.gold.add(gold);
    }

    public void forgotGold(Gold gold) {
        this.gold.remove(gold);
    }

    public boolean ownGold(Gold gold) {
        return this.gold.contains(gold);
    }

    public Point getPosition() {
        return position;
    }

    public Point getBasePosition() {
        return getBase().getCell().copy();
    }

    public int getForcesPerTick() {
        int result = data.increasePerTick() +
                data.goldScore() * gold.size();
        if (data.regionsScores() != 0) {
            result += data.regionsScores() * field.regionsCount(this) / field.totalRegions();
        }
        return result;
    }

    public JSONObject getCurrentAction() {
        return (currentAction != null) ? currentAction : new JSONObject();
    }

    public JSONObject getLastAction() {
        return (lastAction != null) ? lastAction : new JSONObject();
    }

    public void destroy() {
        field.removeFromCell(this);
    }

    // ----------- only for testing methods -------------

    public void remove(Forces forces) {
        Point region = forces.getRegion();
        field.removeForces(this, region.getX(), region.getY());
    }

    public void increase(final Forces... forces) {
        new Then().increase(forces).send();
    }

    public Then increaseAnd(final Forces... forces) {
        return new Then().increase(forces);
    }

    public Then moveAnd(final Forces... forces) {
        return new Then().move(forces);
    }

    public void move(final Forces... forces) {
        new Then().move(forces).send();
    }

    public void increaseAndMove(Forces forcesToIncrease, Forces forcesToMove) {
        new Then().increase(forcesToIncrease).move(forcesToMove).send();
    }

    public class Then {
        private List<Forces> increase;
        private List<Forces> move;

        public void send() {
            message(new JSONObject(){{
                put(INCREASE_KEY, new JSONArray(increase));
                put(MOVEMENTS_KEY, new JSONArray(move));
            }}.toString());
        }

        public Then increase(Forces... increase) {
            this.increase = Arrays.asList(increase);
            return this;
        }

        public Then move(Forces... move) {
            this.move = Arrays.asList(move);
            return this;
        }
    }

    // --------------- only for logging -------------------

    public class LogState {
        public JSONObject json() {
            return new JSONObject(){{
                put("id", id());
                put("forcesPerTick", getForcesPerTick());
                put("alive", alive);
                put("win", win);
                put("gold", gold);
                put("position", position);
                put("lastAction", lastAction);
                put("field", (field instanceof Expansion) ? ((Expansion)field).lg.id() : field.toString());
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
