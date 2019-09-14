package com.codenjoy.dojo.expansion.model.levels.items;

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


import com.codenjoy.dojo.expansion.model.Elements;
import com.codenjoy.dojo.expansion.model.Player;
import com.codenjoy.dojo.expansion.model.levels.Item;

/**
 * Created by Mikhail_Udalyi on 09.06.2016.
 */
public class Gold extends BaseItem {

    private boolean picked;

    public Gold(Elements el) {
        super(el);
        picked = false;
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    @Override
    public boolean equals(Object o) {
        return super.equals(o);
    }

    @Override
    public Elements state(Player player, Object... alsoAtPoint) {
//        if (picked) {
//            return Elements.FLOOR;
//        } else {
            return super.state(player, alsoAtPoint);
//        }
    }

    @Override
    public void action(Item item, boolean comeInOrLeave) {
        if (item instanceof HeroForces) {
            HeroForces forces = (HeroForces) item;
            if (comeInOrLeave) {
                forces.pickUpGold(this);
                picked = true;
            } else {
                if (forces.ownGold(this)) {
                    forces.forgotGold(this);
                    picked = false;
                }
            }
        }
    }

    public void reset() {
        picked = false;
    }

    public boolean isPicked() {
        return picked;
    }
}
