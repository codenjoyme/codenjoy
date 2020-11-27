package com.codenjoy.dojo.battlecity.model.items;

/*-
 * #%L
 * Codenjoy - it's a dojo-like platform from developers to developers.
 * %%
 * Copyright (C) 2018 - 2020 Codenjoy
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


import com.codenjoy.dojo.battlecity.model.Elements;
import com.codenjoy.dojo.battlecity.model.Player;
import com.codenjoy.dojo.services.*;

public class Prize extends PointImpl implements Tickable, State<Elements, Player> {

    public static final int CHANGE_EVERY_TICKS = 2;
    private Elements elements;
    private int timeout;
    private int timelimit;

    private boolean destroyed;
    private boolean active;

    public Prize(Point pt, int prizeOnField, int prizeWorking, Elements elements) {
        super(pt);
        this.elements = elements;
        timeout = prizeOnField;
        timelimit = prizeWorking;
        destroyed = false;
        active = true;
    }

    @Override
    public Elements state(Player player, Object... alsoAtPoint) {
        if (destroyed) {
            return Elements.BANG;
        }

        if (timeout % CHANGE_EVERY_TICKS == 0) {
            return elements;
        }

        return Elements.PRIZE;
    }

    @Override
    public void tick() {
        if (destroyed || !active) {
            return;
        }
        if (timeout == 0) {
            active = false;
        } else {
            timeout--;
        }
    }

    public void tickTaken() {
        timelimit--;
    }

    public int timelimit() {
        return timelimit;
    }

    public boolean isDestroyed() {
        return destroyed;
    }

    public boolean isActive() {
        return active;
    }

    public void kill() {
        destroyed = true;
    }

    public Elements getElements() {
        return elements;
    }
}