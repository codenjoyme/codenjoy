package com.codenjoy.dojo.bomberman.model.perks;

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

import com.codenjoy.dojo.bomberman.model.Elements;
import com.codenjoy.dojo.bomberman.model.Player;
import com.codenjoy.dojo.services.PointImpl;
import com.codenjoy.dojo.services.State;
import com.codenjoy.dojo.services.Tickable;

import java.util.Objects;

public abstract class Perk extends PointImpl implements Tickable, State<Elements, Player> {

    private final String name;
    private final Elements element;
    private int value;

    /**
     * Maximum timer value
     * */
    private int timeout;

    /**
     * Countdown with every tick.
     * When timer becomes = 0, then perk should be disabled
     * */
    private int timer;

    /**
     * After timeout disappears from the board if wasn't picked up.
     * */
    private int pickTimeout;

    public Perk(Elements element, int value, int timeout) {
        this.element = element;
        this.name = element.name();
        this.value = value;
        this.timeout = timeout;
        this.timer = timeout;
    }

    public Perk(String name, Elements element, int value, int timeout, int pickTimeout) {
        this.name = name;
        this.element = element;
        this.value = value;
        this.timeout = timeout;
        this.pickTimeout = pickTimeout;
    }

    /**
     * This is for trigger like perks, e.g. nuke button.
     *
     * @see Perk#Perk(Elements, int, int)
     */
    public Perk(Elements element, int timeout) {
        this(element, 0, timeout);
    }

    public int getPickTimeout() {
        return pickTimeout;
    }

    public void setPickTimeout(int pickTimeout) {
        this.pickTimeout = pickTimeout;
    }

    public boolean isActive() {
        return timer > 0;
    }

    @Override
    public Elements state(Player player, Object... alsoAtPoint) {
        return isActive() ? this.element : Elements.NONE;
    }

    @Override
    public void tick() {
        timer--;
    }

    /**
     * Perk implementation must resolve the situation when player already has this kind of perk.
     * E.g. strategy can be to reset timer or increase effect power etc.
     * Though, one can implement more complex situations, like combo: time + power etc.
     *
     * @param perk to combine with.
     * @return resulting perk.
     */
    public abstract Perk combine(Perk perk);

    public String getName() {
        return name;
    }

    public Elements getElement() {
        return element;
    }

    public int getValue() {
        return value;
    }

    public int getTimeout() {
        return timeout;
    }

    public int getTimer() {
        return timer;
    }

    protected void setTimer(int timer) {
        this.timer = timer;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Perk perk = (Perk) o;
        return getName() == perk.getName();
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), getName());
    }

    @Override
    public String toString() {
        return String.format("{%s('%s') value=%s, timeout=%s, timer=%s, pick=%s}",
                name, element, value, timeout, timer, pickTimeout);
    }

    public void tickPick() {
        pickTimeout--;
    }

    public void decrease() {
        value--;
        if (value <= 0) {
            timer = 0;
            timeout = 0;
        }
    }
}
