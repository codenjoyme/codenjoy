package com.codenjoy.dojo.icancode.model.perks;

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

import com.codenjoy.dojo.icancode.model.BaseItem;
import com.codenjoy.dojo.icancode.model.Elements;
import com.codenjoy.dojo.icancode.services.SettingsWrapper;
import com.codenjoy.dojo.services.Tickable;
import org.apache.commons.lang3.StringUtils;

// TODO refactoring needed
// TODO to use FieldItem instead of BaseItem
public abstract class AbstractPerk extends BaseItem implements Tickable {

    private final String value;
    private final Timer availability;
    private final Timer activity;

    public AbstractPerk(Elements element) {
        this(element, StringUtils.EMPTY);
    }

    public AbstractPerk(Elements element, String value) {
        super(element);
        this.value = value;
        this.availability = new Timer(SettingsWrapper.data.perkAvailability());
        this.activity = new Timer(SettingsWrapper.data.perkActivity());
    }

    public String getValue() {
        return value;
    }

    public boolean isAvailable() {
        return !availability.isTimeUp();
    }

    public boolean isActive() {
        return !activity.isTimeUp();
    }

    @Override
    public void tick() {
        availability.tick();
        activity.tick();
    }
}
