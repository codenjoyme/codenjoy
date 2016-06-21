package com.epam.dojo.icancode.model;

import com.codenjoy.dojo.services.LengthToXY;
import com.codenjoy.dojo.services.Point;
import com.epam.dojo.icancode.model.items.BaseItem;
import static org.fest.reflect.core.Reflection.*;

import java.util.LinkedList;
import java.util.List;

public class LevelImpl implements Level {
    private final LengthToXY xy;

    private ICell[] cells;
    private int size;

    public LevelImpl(String map) {
        cells = new ICell[map.length()];
        size = (int) Math.sqrt(map.length());
        xy = new LengthToXY(size);

        fillMap(map);
    }

    private void fillMap(String map) {
        for (int i = 0; i < this.cells.length; ++i) {

            this.cells[i] = new Cell(xy.getXY(i));
            Elements element = Elements.valueOf(map.charAt(i));
            BaseItem item = constructor()
                    .withParameterTypes(Elements.class)
                    .in(element.itsClass)
                    .newInstance(element);
            this.cells[i].addItem(item);
        }
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
    public List<Point> getElements(int layer) {
        List<Point> result = new LinkedList<Point>();

        for (int i = 0; i < cells.length; ++i) {
            result.add(cells[i].getItem(layer));
        }

        return result;
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
