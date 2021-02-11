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
import com.codenjoy.dojo.expansion.model.levels.Cell;
import com.codenjoy.dojo.expansion.model.levels.Item;

import java.util.Arrays;
import java.util.List;

/**
 * Created by Mikhail_Udalyi on 08.06.2016.
 */
public abstract class BaseItem implements Item {
    private Cell cell;
    private FeatureItem feature;
    protected Elements element;

    public BaseItem(Elements element) {
        this.element = element;
    }

    protected void impassable() {
        feature = FeatureItem.IMPASSABLE;
    }

    @Override
    public void action(Item item, boolean comeInOrLeave) {
        // do nothing
    }

    @Override
    public Cell getCell() {
        return cell;
    }

    @Override
    public List<Item> getItemsInSameCell() {
        if (cell == null) {
            return Arrays.asList();
        }
        List<Item> items = cell.getItems();
        items.remove(this);
        return items;
    }

    public Elements getState() {
        return element;
    }

    @Override
    public void setCell(Cell value) {
        cell = value;
    }

    @Override
    public Cell removeFromCell() {
        Cell cell = getCell();
        if (cell != null) {
            cell.removeItem(this);
            setCell(null);
        }
        return cell;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        BaseItem baseItem = (BaseItem) o;

        if (cell == null && baseItem.cell != null) {
            return false;
        }
        if (cell != null && baseItem.cell == null) {
            return false;
        }

        if (cell == null) {
            if (baseItem.cell == null) {
                return element == baseItem.element;
            } else {
                return false;
            }
        } else {
            return element == baseItem.element && cell.equals(baseItem.cell);
        }
    }

    @Override
    public Elements state(Player player, Object... alsoAtPoint) {
        return element;
    }

    @Override
    public String toString() {
        return String.format("'%s'", element);
    }

    @Override
    public boolean hasFeature(FeatureItem feature) {
        return feature != null && feature.equals(this.feature);
    }

    @Override
    public int hashCode() {
        return element.hashCode();
    }
}
