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
import com.codenjoy.dojo.battlecity.services.GameSettings;
import com.codenjoy.dojo.services.*;

import java.util.function.Consumer;

import static com.codenjoy.dojo.battlecity.services.GameSettings.Keys.PRIZE_SPRITE_CHANGE_TICKS;

public class Prize extends PointImpl implements Tickable, State<Elements, Player> {

    private final Elements elements;
    private final int prizeOnField;
    private final int prizeWorking;

    private boolean destroyed;
    private boolean active;
    private Consumer<Object> onDestroy;
    private int timeout;
    private int ticks;
    private GameSettings settings;

    public Prize(Point pt, int prizeOnField, int prizeWorking, Elements elements) {
        super(pt);
        this.elements = elements;
        this.prizeOnField = prizeOnField;
        this.prizeWorking = prizeWorking;
        destroyed = false;
        active = true;
    }

    public void init(GameSettings settings) {
        this.settings = settings;
    }

    @Override
    public Elements state(Player player, Object... alsoAtPoint) {
        if (destroyed) {
            return Elements.BANG;
        }

        if (ticks % changeEveryTicks() == 0) {
            return Elements.PRIZE;
        }

        return elements;
    }

    public int changeEveryTicks() {
        return settings.integer(PRIZE_SPRITE_CHANGE_TICKS);
    }

    @Override
    public void tick() {
        if (destroyed || !active) {
            return;
        }
        if (ticks == timeout) {
            active = false;

            if (onDestroy != null) {
                onDestroy.accept(this);
            }
        } else {
            ticks++;
        }
    }

    public boolean isDestroyed() {
        return destroyed;
    }

    public void kill() {
        destroyed = true;
    }

    public Elements elements() {
        return elements;
    }

    public void taken(Consumer<Object> onDestroy) {
        if (this.onDestroy == null) {
            timeout = prizeOnField;
        } else {
            timeout = prizeWorking;
        }
        ticks = 0;
        this.onDestroy = onDestroy;
    }
}