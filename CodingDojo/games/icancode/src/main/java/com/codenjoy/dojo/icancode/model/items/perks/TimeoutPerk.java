package com.codenjoy.dojo.icancode.model.items.perks;

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

import com.codenjoy.dojo.games.icancode.Element;
import com.codenjoy.dojo.icancode.services.GameSettings;

import static com.codenjoy.dojo.icancode.services.GameSettings.Keys.PERK_ACTIVITY;
import static com.codenjoy.dojo.icancode.services.GameSettings.Keys.PERK_AVAILABILITY;

public abstract class TimeoutPerk extends Perk {

    protected Timer availability;
    protected Timer activity;

    public TimeoutPerk(Element element) {
        super(element);
    }

    private void initTimers() {
        this.availability = new Timer(settings.integer(PERK_AVAILABILITY));
        this.activity = new Timer(settings.integer(PERK_ACTIVITY));
    }

    @Override
    public boolean isAvailable() {
        return !availability.isTimeUp();
    }

    @Override
    public boolean isActive() {
        return !activity.isTimeUp();
    }

    @Override
    public void tick() {
        availability.tick();
        activity.tick();
    }

    @Override
    public void reset() {
        super.reset();
        initTimers();
    }

    @Override
    public void init(GameSettings settings) {
        super.init(settings);
        initTimers();
    }
}
