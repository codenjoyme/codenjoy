package com.codenjoy.dojo.snakebattle.services;

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


import java.util.Objects;
import java.util.function.Function;

public class Event {

    public static final Event START = new Event("START");
    public static final Event WIN = new Event("WIN");
    public static final Event DIE = new Event("DIE");
    public static final Event APPLE = new Event("APPLE");
    public static final Event STONE = new Event("STONE");
    public static final Function<Integer, Event> EAT = amount -> new Event("EAT", amount);
    public static final Event GOLD = new Event("GOLD");
    public static final Event FLYING = new Event("FLYING");
    public static final Event FURY = new Event("FURY");

    private String type;
    private int amount;

    public Event(String type) {
        this.type = type;
        this.amount = 1;
    }

    public Event(String type, int amount) {
        this.type = type;
        this.amount = amount;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Event events = (Event) o;
        if (isEat()) {
            return Objects.equals(type, events.type) &&
                    amount == events.amount;
        }
        return Objects.equals(type, events.type);
    }

    @Override
    public int hashCode() {
        return Objects.hash(type);
    }

    public boolean isWin() {
        return type.equals(WIN.type);
    }

    public boolean isApple() {
        return type.equals(APPLE.type);
    }

    public boolean isGold() {
        return type.equals(GOLD.type);
    }

    public boolean isDie() {
        return type.equals(DIE.type);
    }

    public boolean isStone() {
        return type.equals(STONE.type);
    }

    public boolean isEat() {
        return type.equals(EAT.apply(1).type);
    }

    public int getAmount() {
        return amount;
    }

    @Override
    public String toString() {
        if (isEat()) {
            return String.format("%s[%s]", type, amount);
        } else {
            return type;
        }
    }
}
