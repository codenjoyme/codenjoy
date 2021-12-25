package com.codenjoy.dojo.lemonade.services;

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

public class Event implements EventObject<Event.Type, Event> {

    public Type type;
    public double profit;
    public double assetsAfter;

    public enum Type {
        WIN,
        LOSE
    }

    public Event(Type type, double profit, double assetsAfter) {
        this.type = type;
        this.profit = profit;
        this.assetsAfter = assetsAfter;
    }

    public double profit() {
        return profit;
    }

    public double assetsAfter() {
        return assetsAfter;
    }

    @Override
    public Type type() {
        return type;
    }

    @Override
    public String toString() {
        return String.format("%s (%.2f, %.2f)", type.toString(), profit, assetsAfter);
    }
}
