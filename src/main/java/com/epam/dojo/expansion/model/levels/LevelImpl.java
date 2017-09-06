package com.epam.dojo.expansion.model.levels;

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


import com.codenjoy.dojo.services.LengthToXY;
import com.codenjoy.dojo.services.Point;
import com.epam.dojo.expansion.model.Cell;
import com.epam.dojo.expansion.model.Elements;
import com.epam.dojo.expansion.model.interfaces.ICell;
import com.epam.dojo.expansion.model.interfaces.IField;
import com.epam.dojo.expansion.model.interfaces.IItem;
import com.epam.dojo.expansion.model.interfaces.ILevel;
import com.epam.dojo.expansion.model.items.BaseItem;
import com.epam.dojo.expansion.model.items.ElementsMapper;
import com.epam.dojo.expansion.model.items.FieldItem;

import java.util.LinkedList;
import java.util.List;

import static com.epam.dojo.expansion.model.Elements.Layers.LAYER1;
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

                if (element.getLayer() != LAYER1) {
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
    public <T> List<T> getItems(Class<T> clazz) {
        List<T> result = new LinkedList<T>();
        for (ICell cell : cells) {
            for (IItem item : cell.getItems()) {
                if (clazz.isInstance(item)) {
                    result.add((T)item);
                }
            }
        }
        return result;
    }

    @Override
    public List<ICell> getCellsWith(Class with) {
        List<ICell> result = new LinkedList<ICell>();
        for (ICell cell : cells) {
            for (IItem item : cell.getItems()) {
                if (with.isInstance(item)) {
                    result.add(cell);
                    break;
                }
            }
        }
        return result;
    }

    @Override
    public List<ICell> getCellsWithout(Class without) {
        List<ICell> result = new LinkedList<ICell>();
        for (ICell cell : cells) {
            boolean bad = false;
            for (IItem item : cell.getItems()) {
                if (without.isInstance(item)) {
                    bad = true;
                    break;
                }
            }
            if (!bad) {
                result.add(cell);
            }
        }
        return result;
    }

    @Override
    public List<ICell> getCellsWithWithout(Class with, Class without) {
        List<ICell> result = new LinkedList<ICell>();
        for (ICell cell : cells) {
            boolean bad = false;
            for (IItem item : cell.getItems()) {
                if (!with.isInstance(item) || without.isInstance(item)) {
                    bad = true;
                    break;
                }
            }
            if (!bad) {
                result.add(cell);
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
