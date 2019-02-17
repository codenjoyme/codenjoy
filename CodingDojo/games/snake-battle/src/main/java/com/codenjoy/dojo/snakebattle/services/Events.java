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

public class Events {

    public static final Events START = new Events("START");
    public static final Events WIN = new Events("WIN");
    public static final Events DIE = new Events("DIE");
    public static final Events APPLE = new Events("APPLE");
    public static final Events STONE = new Events("STONE");
    public static final Function<Integer, Events> EAT = amount -> new Events("EAT", amount);
    public static final Events GOLD = new Events("GOLD");

    private String type;
    private int amount;

    public Events(String type) {
        this.type = type;
        this.amount = 1;
    }

    public Events(String type, int amount) {
        this.type = type;
        this.amount = amount;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Events events = (Events) o;
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
        return this.equals(WIN);
    }

    public boolean isApple() {
        return this.equals(APPLE);
    }

    public boolean isGold() {
        return this.equals(GOLD);
    }

    public boolean isDie() {
        return this.equals(DIE);
    }

    public boolean isStone() {
        return this.equals(STONE);
    }

    public boolean isEat() {
        return type.equals("EAT");
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
