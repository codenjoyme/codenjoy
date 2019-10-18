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


import com.codenjoy.dojo.icancode.model.interfaces.ICell;
import com.codenjoy.dojo.icancode.model.interfaces.IItem;
import com.codenjoy.dojo.icancode.model.Elements;
import com.codenjoy.dojo.icancode.model.Player;
import com.codenjoy.dojo.icancode.model.enums.FeatureItem;

import java.util.Arrays;
import java.util.List;

/**
 * Created by Mikhail_Udalyi on 08.06.2016.
 */
public abstract class BaseItem implements IItem {

    private ICell cell;
    private FeatureItem[] features;
    private Elements element;

    public BaseItem(Elements element) {
        this.element = element;
        this.features = new FeatureItem[0];
    }

    public BaseItem(Elements element, FeatureItem[] features) {
        this.element = element;
        this.features = features.clone();
    }

    @Override
    public void action(IItem item) {
        // do nothing
    }

    @Override
    public ICell getCell() {
        return cell;
    }

    @Override
    public List<IItem> getItemsInSameCell() {
        if (cell == null) {
            return Arrays.asList();
        }
        List<IItem> items = cell.getItems();
        items.remove(this);
        return items;
    }

    protected <T> T getIf(Object item, Class<T> clazz) {
        if (item == null) {
            return null;
        }
        if (item.getClass().equals(clazz)) {
            return (T)item;
        }
        return null;
    }

    public Elements getState() {
        return element;
    }

    @Override
    public void setCell(ICell value) {
        cell = value;
    }

    @Override
    public void removeFromCell() {
        if (getCell() != null) {
            getCell().removeItem(this);
            setCell(null);
        }
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
    public boolean hasFeatures(FeatureItem[] features) {
        for (int i = 0; i < this.features.length; ++i) {
            for (int j = 0; j < features.length; ++j) {
                if (this.features[i] == features[j]) {
                    return true;
                }
            }
        }

        return false;
    }

    @Override
    public int hashCode() {
        return element.hashCode();
    }
}
