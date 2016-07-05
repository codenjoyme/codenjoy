package com.epam.dojo.icancode.model;

import com.codenjoy.dojo.services.LengthToXY;
import com.codenjoy.dojo.services.Point;
import com.epam.dojo.icancode.model.interfaces.ICell;
import com.epam.dojo.icancode.model.interfaces.IField;
import com.epam.dojo.icancode.model.interfaces.IItem;
import com.epam.dojo.icancode.model.interfaces.ILevel;
import com.epam.dojo.icancode.model.items.BaseItem;
import com.epam.dojo.icancode.model.items.FieldItem;

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

                cells[xy.getLength(x, y)] = new Cell(x, y);
                Elements element = Elements.valueOf(map.charAt(indexChar));
                BaseItem item = constructor()
                        .withParameterTypes(Elements.class)
                        .in(element.itsClass)
                        .newInstance(element);
                cells[xy.getLength(x, y)].addItem(item);
                ++indexChar;
            }
        }
    }

    public int getSize() {
        return size;
    }

    public ICell getCell(int x, int y) {
        return cells[xy.getLength(x, y)];
    }

    public ICell getCell(Point point) {
        return getCell(point.getX(), point.getY());
    }

    public ICell[] getCells() {
        return cells;
    }

    public boolean isBarrier(int x, int y) {
        return x > size - 1 || x < 0 || y < 0 || y > size - 1
                || !getCell(x, y).isPassable();
    }

    public <T extends IItem> List<T> getItems(Class clazz)
    {
        List<T> result = new LinkedList<T>();
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

    public void setField(IField field) {
        List<FieldItem> items = getItems(FieldItem.class);

        for (int i = 0; i < items.size(); ++i) {
            items.get(i).setField(field);
        }
    }
}
