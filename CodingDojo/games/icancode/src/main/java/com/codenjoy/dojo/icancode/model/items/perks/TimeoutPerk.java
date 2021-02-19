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

import com.codenjoy.dojo.icancode.model.*;
import com.codenjoy.dojo.icancode.services.SettingsWrapper;

public abstract class TimeoutPerk extends Perk {

    protected Timer availability;
    protected Timer activity;

    public TimeoutPerk(Elements element) {
        super(element);
        this.availability = new Timer(SettingsWrapper.data.perkAvailability());
        this.activity = new Timer(SettingsWrapper.data.perkActivity());
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
}
