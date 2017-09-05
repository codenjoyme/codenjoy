package com.epam.dojo.expansion.model.items;

/*-
 * #%L
 * iCanCode - it's a dojo-like platform from developers to developers.
 * %%
 * Copyright (C) 2016 EPAM
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


import com.epam.dojo.expansion.model.Elements;
import com.epam.dojo.expansion.model.Player;
import com.epam.dojo.expansion.model.interfaces.IItem;

/**
 * Created by Mikhail_Udalyi on 09.06.2016.
 */
public class Gold extends BaseItem {

    private boolean hidden;

    public Gold(Elements el) {
        super(el);
        hidden = false;
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
        if (hidden) {
            return Elements.FLOOR;
        } else {
            return super.state(player, alsoAtPoint);
        }
    }

    @Override
    public void action(IItem item) {
        if (hidden) return;

        if (item instanceof HeroForces) {
            HeroForces forces = (HeroForces) item;
            forces.pickUpGold();
            hidden = true;
        }
    }

    public void reset() {
        hidden = false;
    }

    public boolean isHidden() {
        return hidden;
    }
}
