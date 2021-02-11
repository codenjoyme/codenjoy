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

/**
 * Created by Mikhail_Udalyi on 08.06.2016.
 */

public class Start extends BaseItem {

    private Hero owner;

    public Start(Elements el) {
        super(el);
    }

    public Elements element() {
        return Elements.getForce(index());
    }

    public int index() {
        return element.getIndex();
    }

    public boolean isFree() {
        return getCell().getItems(HeroForces.class).isEmpty();
    }

    public boolean isOwnedBy(Hero hero) {
        if (owner == null) {
            return false;
        }
        return owner.equals(hero);
    }

    public void setOwner(Hero owner) {
        this.owner = owner;
    }
}
