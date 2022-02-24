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
import com.codenjoy.dojo.icancode.model.items.perks.Perk;

import static com.codenjoy.dojo.services.StateUtils.filterOne;

public class Floor extends BaseItem {

    public Floor() {
        super(Elements.FLOOR);
    }

    @Override
    public Elements state(Player player, Object... alsoAtPoint) {
        Perk perk = filterOne(alsoAtPoint, Perk.class);
        if (perk != null) {
            return perk.state(player, alsoAtPoint);
        }

        Gold gold = filterOne(alsoAtPoint, Gold.class);
        if (gold != null) {
            return gold.state(player, alsoAtPoint);
        }

        return super.state(player, alsoAtPoint);
    }
}