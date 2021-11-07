package com.codenjoy.dojo.icancode.model.items.perks;

/*-
 * #%L
 * Codenjoy - it's a dojo-like platform from developers to developers.
 * %%
 * Copyright (C) 2018 - 2021 Codenjoy
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
import com.codenjoy.dojo.icancode.model.Customizable;
import com.codenjoy.dojo.icancode.model.items.RenewableItem;
import com.codenjoy.dojo.icancode.services.GameSettings;
import com.codenjoy.dojo.services.Tickable;

public abstract class Perk extends RenewableItem implements Tickable, Customizable {

    protected GameSettings settings;

    public Perk(Element element) {
        super(element);
    }

    @Override
    public void tick() {
        // do nothing
    }

    public boolean isAvailable() {
        return true;
    }

    public boolean isActive() {
        return true;
    }

    @Override
    public GameSettings settings() {
        return settings;
    }

    @Override
    public void init(GameSettings settings) {
        this.settings = settings;
    }
}
