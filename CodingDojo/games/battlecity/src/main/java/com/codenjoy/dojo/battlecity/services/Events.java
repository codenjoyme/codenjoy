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


import java.util.Objects;
import java.util.function.Function;

public class Events {

    public static final Events KILL_YOUR_TANK = new Events("KILL_YOUR_TANK");
    public static final Function<Integer, Events> KILL_OTHER_HERO_TANK = amount -> new Events("KILL_OTHER_HERO_TANK", amount);
    public static final Events KILL_OTHER_AI_TANK = new Events("KILL_OTHER_AI_TANK");

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
        if (isKillOtherHeroTank()) {
            return Objects.equals(type, events.type) &&
                    amount == events.amount;
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

    public boolean isKillOtherHeroTank() {
        return type.equals(KILL_OTHER_HERO_TANK.apply(1).type);
    }

    public boolean isKillOtherAITank() {
        return type.equals(KILL_OTHER_AI_TANK.type);
    }

    @Override
    public String toString() {
        if (isKillOtherHeroTank()) {
            return String.format("%s[%s]", type, amount);
        } else {
            return type;
        }
    }

    public int getAmount() {
        return amount;
    }
}
