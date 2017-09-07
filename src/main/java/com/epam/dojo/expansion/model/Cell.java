package com.epam.dojo.expansion.model;

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
import com.epam.dojo.expansion.model.enums.FeatureItem;
import com.epam.dojo.expansion.model.interfaces.ICell;
import com.epam.dojo.expansion.model.interfaces.IItem;
import com.epam.dojo.expansion.model.items.Air;
import com.epam.dojo.expansion.model.items.HeroForces;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by Mikhail_Udalyi on 08.06.2016.
 */
public class Cell extends PointImpl implements ICell {

    private List<IItem> items = new ArrayList<>();

    public Cell(int x, int y) {
        super(x, y);
    }

    public Cell(Point point) {
        super(point);
    }

    @Override
    public void captureBy(HeroForces income) {
        addItem(income);
        preformAction(income, true);
    }

    @Override
    public void addItem(IItem item) {
        items.add(item);
        item.setCell(this);
    }

    private void preformAction(IItem coming, boolean comeInOrLeave) {
        for (IItem item : items) {
            preformAction(coming, item, comeInOrLeave);
        }
    }

    private void preformAction(IItem item1, IItem item2, boolean comeInOrLeave) {
        if (item2.equals(item1)) {
            return;
        }
        item2.action(item1, comeInOrLeave);
        item1.action(item2, comeInOrLeave);
    }

    @Override
    public boolean isPassable() {
        for (IItem item : items) {
            if (item.hasFeatures(new FeatureItem[]{FeatureItem.IMPASSABLE})) {
                return false;
            }
        }

        return true;
    }

    @Override
    public <T extends IItem> T getItem(Class<T> type) {
        for (IItem item : items) {
            if (item.getClass() == type) {
                return (T)item;
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
    public <T extends IItem> List<T> getItems(Class<T> clazz) {
        List<T> result = new LinkedList<>();
        for (IItem item : items) {
            // TODO to replace on class == class
            if (clazz.isAssignableFrom(item.getClass()) || item.getClass().isAssignableFrom(clazz)) {
                result.add((T)item);
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
        items.remove(item);
        preformAction(item, false);
    }

    @Override
    public String toString() {
        return String.format("Cell[%s,%s]=%s", x, y, items);
    }

}
