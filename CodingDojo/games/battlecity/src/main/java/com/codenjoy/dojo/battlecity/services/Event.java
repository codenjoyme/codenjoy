package com.codenjoy.dojo.battlecity.services;

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


import com.codenjoy.dojo.services.event.EventObject;

import java.util.Objects;
import java.util.function.Function;

public class Event implements EventObject<Event.Type, Integer> {

    public static final Event KILL_YOUR_TANK = new Event(Type.KILL_YOUR_TANK);
    public static final Function<Integer, Event> KILL_OTHER_HERO_TANK = amount -> new Event(Type.KILL_OTHER_HERO_TANK, amount);
    public static final Event KILL_OTHER_AI_TANK = new Event(Type.KILL_OTHER_AI_TANK);
    public static final Event START_ROUND = new Event(Type.START_ROUND);
    public static final Event WIN_ROUND = new Event(Type.WIN_ROUND);
    public static final Function<Integer, Event> CATCH_PRIZE = type -> new Event(Type.CATCH_PRIZE, type);

    private Type type;
    private int value;

    enum Type {
        KILL_YOUR_TANK,
        KILL_OTHER_HERO_TANK,
        KILL_OTHER_AI_TANK,
        START_ROUND,
        WIN_ROUND,
        CATCH_PRIZE
    }

    public Event(Type type) {
        this.type = type;
        this.value = 1;
    }

    public Event(Type type, int value) {
        this.type = type;
        this.value = value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Event events = (Event) o;
        if (isKillOtherHeroTank()) {
            return Objects.equals(type, events.type) &&
                    value == events.value;
        }
        return Objects.equals(type, events.type);
    }

    @Override
    public int hashCode() {
        return Objects.hash(type);
    }

    public boolean isKillYourTank() {
        return type.equals(KILL_YOUR_TANK.type);
    }

    public boolean isCatchPrize() {
        return type.equals(CATCH_PRIZE.apply(1).type);
    }

    public boolean isKillOtherHeroTank() {
        return type.equals(KILL_OTHER_HERO_TANK.apply(1).type);
    }

    public boolean isKillOtherAITank() {
        return type.equals(KILL_OTHER_AI_TANK.type);
    }

    @Override
    public String toString() {
        if (isKillOtherHeroTank() || isCatchPrize()) {
            return String.format("%s[%s]", type, value);
        } else {
            return type.name();
        }
    }

    @Override
    public Integer value() {
        return value;
    }

    @Override
    public Type type() {
        return type;
    }
}
