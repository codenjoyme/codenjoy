package com.epam.dojo.icancode.model;

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


import com.codenjoy.dojo.services.Point;
import com.codenjoy.dojo.services.PointImpl;
import com.epam.dojo.icancode.model.enums.FeatureItem;
import com.epam.dojo.icancode.model.interfaces.ICell;
import com.epam.dojo.icancode.model.interfaces.IItem;
import com.epam.dojo.icancode.model.items.Air;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by Mikhail_Udalyi on 08.06.2016.
 */
public class Cell extends PointImpl implements ICell {

    private List<IItem> items = new ArrayList<>();

    //================================ Constructors ================================

    public Cell(int x, int y) {
        super(x, y);
    }

    public Cell(Point point) {
        super(point);
    }

    //================================ Implements ================================

    @Override
    public void addItem(IItem item) {
        if (item.getCell() != null) {
            item.getCell().removeItem(item);
        }

        items.add(item);
        item.setCell(this);
    }

    @Override
    public void comeIn(IItem comingItem) {
        for (int i = 0; i < items.size(); ++i) {
            IItem cellItem = items.get(i);

            if (!cellItem.equals(comingItem)) {
                cellItem.action(comingItem);
                comingItem.action(cellItem);
            }
        }
    }

    @Override
    public boolean isPassable() {
        for (int i = 0; i < items.size(); ++i) {
            if (items.get(i).hasFeatures(new FeatureItem[]{FeatureItem.IMPASSABLE})) {
                return false;
            }
        }

        return true;
    }

    @Override
    public <T extends IItem> T getItem(T type) {
        for (int i = 0; i < items.size(); ++i) {

            if (items.get(i).getClass() == type.getClass()) {
                return (T) items.get(i);
            }
        }

        return null;
    }

    @Override
    public <T extends IItem> T getItem(int layer) {
        if (items.size() <= layer) {
            return (T) new Air();
        }

        return (T) items.get(layer);
    }

    @Override
    public <T extends IItem> List<T> getItems(Class clazz) {
        List<T> result = new LinkedList<>();

        for (int i = 0; i < items.size(); ++i) {

            if (items.get(i).getClass() == clazz) {
                result.add((T) items.get(i));
            }
        }

        return result;
    }

    @Override
    public <T extends IItem> List<T> getItems() {
        return (List<T>) new LinkedList<>(items);
    }

    @Override
    public void removeItem(IItem item) {
        for (int i = 0; i < items.size(); ++i) {
            if (items.get(i) == item) {
                items.remove(i);
                return;
            }
        }
        items.remove(item);
    }

    //================================ Overrides ================================

    @Override
    public String toString() {
        return String.format("Cell[%s,%s]=%s", x, y, items);
    }

}
