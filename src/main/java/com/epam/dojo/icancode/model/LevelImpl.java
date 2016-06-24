package com.epam.dojo.icancode.model;

import com.codenjoy.dojo.services.Point;
import com.epam.dojo.icancode.model.items.BaseItem;
import com.epam.dojo.icancode.services.LengthConverter;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import static org.fest.reflect.core.Reflection.constructor;

public class LevelImpl implements ILevel {
    private ICell[] cells;
    private int size;

    public LevelImpl(String map) {
        cells = new ICell[map.length()];
        size = (int) Math.sqrt(map.length());
        if (size*size != map.length()) {
            throw new IllegalArgumentException("map must be square! " + size + "^2 != " + map.length());
        }

        fillMap(map);
    }

    private void fillMap(String map) {
        int indexChar = 0;

        for (int y = size - 1; y > -1; --y) {
            for (int x = 0; x < size; ++x) {

                cells[LengthConverter.getLength(x, y, size)] = new Cell(x, y);
                Elements element = Elements.valueOf(map.charAt(indexChar));
                BaseItem item = constructor()
                        .withParameterTypes(Elements.class)
                        .in(element.itsClass)
                        .newInstance(element);
                cells[LengthConverter.getLength(x, y, size)].addItem(item);
                ++indexChar;
            }
        }

        /*for (int i = 0; i < this.cells.length; ++i) {

            this.cells[i] = new Cell(LengthConverter.getXY(i, size));
            Elements element = Elements.valueOf(map.charAt(i));
            BaseItem item = constructor()
                    .withParameterTypes(Elements.class)
                    .in(element.itsClass)
                    .newInstance(element);
            this.cells[i].addItem(item);
        }*/
    }

    @Override
    public int getSize() {
        return size;
    }

    @Override
    public ICell getCell(int x, int y) {
        return cells[LengthConverter.getLength(x, y, size)];
    }

    @Override
    public ICell getCell(Point point) {
        return getCell(point.getX(), point.getY());
    }

    @Override
    public List<ICell> getCells(ItemLogicType type) {
        List<ICell> result = new LinkedList<ICell>();

        for (int i = 0; i < cells.length; ++i) {
            if (cells[i].is(type)) {
                result.add(cells[i]);
            }
        }
        return result;
    }

    @Override
    public ICell[] getCells() {
        return cells;
    }

    @Override
    public boolean isBarrier(int x, int y) {
        return x > size - 1 || x < 0 || y < 0 || y > size - 1
                || !getCell(x, y).isPassable();
    }

    @Override
    public void init(Field field) {
        for (ICell cell : cells) {
            cell.init(field);
        }
    }
}
