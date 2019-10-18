package com.codenjoy.dojo.icancode.model;

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
import com.codenjoy.dojo.icancode.model.interfaces.IField;
import com.codenjoy.dojo.icancode.model.interfaces.IItem;
import com.codenjoy.dojo.icancode.model.interfaces.ILevel;
import com.codenjoy.dojo.icancode.model.items.BaseItem;
import com.codenjoy.dojo.icancode.model.items.ElementsMapper;
import com.codenjoy.dojo.icancode.model.items.FieldItem;
import com.codenjoy.dojo.services.LengthToXY;
import com.codenjoy.dojo.services.Point;

import java.util.LinkedList;
import java.util.List;

import static org.fest.reflect.core.Reflection.constructor;

public class LevelImpl implements ILevel {
    private ICell[] cells;
    private int size;
    private LengthToXY xy;

    public LevelImpl(String map) {
        cells = new ICell[map.length()];
        size = (int) Math.sqrt(map.length());
        xy = new LengthToXY(size);
        if (size*size != map.length()) {
            throw new IllegalArgumentException("map must be square! " + size + "^2 != " + map.length());
        }

        fillMap(map);
    }

    private void fillMap(String map) {
        int indexChar = 0;

        for (int y = size - 1; y > -1; --y) {
            for (int x = 0; x < size; ++x) {

                Cell cell = new Cell(x, y);
                Elements element = Elements.valueOf(map.charAt(indexChar));
                BaseItem item = getBaseItem(element);

                if (element.getLayer() != Elements.Layers.LAYER1) {
                    Elements atBottom = Elements.valueOf(Elements.FLOOR.ch());
                    cell.addItem(getBaseItem(atBottom));
                }

                cell.addItem(item);
                cells[xy.getLength(x, y)] = cell;
                ++indexChar;
            }
        }
    }

    private BaseItem getBaseItem(Elements element) {
        return constructor()
                            .withParameterTypes(Elements.class)
                            .in(ElementsMapper.getItsClass(element))
                            .newInstance(element);
    }

    @Override
    public int getSize() {
        return size;
    }

    @Override
    public ICell getCell(int x, int y) {
        return cells[xy.getLength(x, y)];
    }

    @Override
    public ICell getCell(Point point) {
        return getCell(point.getX(), point.getY());
    }

    @Override
    public ICell[] getCells() {
        return cells.clone();
    }

    @Override
    public boolean isBarrier(int x, int y) {
        boolean isAbroad = x > size - 1 || x < 0 || y < 0 || y > size - 1;

        return isAbroad || !getCell(x, y).isPassable();
    }

    @Override
    public <T extends IItem> List<T> getItems(Class clazz) {
        List<T> result = new LinkedList<>();
        List<T> items;

        for (int i = 0; i < cells.length; ++i) {
            items = cells[i].getItems();

            for (int j = 0; j < items.size(); ++j) {
                if (clazz.isInstance(items.get(j))) {
                    result.add(items.get(j));
                }
            }
        }

        return result;
    }



    @Override
    public void setField(IField field) {
        List<FieldItem> items = getItems(FieldItem.class);

        for (int i = 0; i < items.size(); ++i) {
            items.get(i).setField(field);
        }
    }
}
