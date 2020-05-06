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
import com.codenjoy.dojo.bomberman.model.Wall;
import com.codenjoy.dojo.services.State;

public class PerkOnBoard extends Wall implements State<Elements, Player> {

    private final Perk perk;

    public PerkOnBoard(int x, int y, Perk perk) {
        super(x, y);
        this.perk = perk;
    }

    @Override
    public Wall copy() {
        return new PerkOnBoard(this.x, this.y, perk);
    }

    @Override
    public Elements state(Player player, Object... alsoAtPoint) {
        return perk != null ? this.perk.state(player, alsoAtPoint) : Elements.NONE;
    }

    public Perk getPerk() {
        return perk;
    }
}
