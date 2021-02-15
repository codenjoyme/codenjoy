package com.codenjoy.dojo.icancode.model.items;

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
import com.codenjoy.dojo.icancode.model.Player;
import com.codenjoy.dojo.icancode.model.perks.DeathRayPerk;
import com.codenjoy.dojo.icancode.model.perks.UnlimitedFirePerk;
import com.codenjoy.dojo.icancode.model.perks.UnstoppableLaserPerk;

import static com.codenjoy.dojo.services.StateUtils.filterOne;

public class Floor extends BaseItem {

    public Floor(Elements el) {
        super(el);
    }

    @Override
    public Elements state(Player player, Object... alsoAtPoint) {
        // TODO refactoring needed
        if (filterOne(alsoAtPoint, DeathRayPerk.class) != null) {
            return Elements.DEATH_RAY_PERK;
        }
        if (filterOne(alsoAtPoint, UnstoppableLaserPerk.class) != null) {
            return Elements.UNSTOPPABLE_LASER_PERK;
        }
        if (filterOne(alsoAtPoint, UnlimitedFirePerk.class) != null) {
            return Elements.UNLIMITED_FIRE_PERK;
        }
        if (filterOne(alsoAtPoint, Gold.class) != null) {
            return Elements.GOLD;
        }
        return super.state(player, alsoAtPoint);
    }
}
