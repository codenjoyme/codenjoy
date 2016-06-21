package com.epam.dojo.icancode.model;

import com.codenjoy.dojo.services.Point;
import com.codenjoy.dojo.services.PointImpl;
import com.epam.dojo.icancode.model.items.Air;
import com.epam.dojo.icancode.model.items.BaseItem;
import com.epam.dojo.icancode.model.items.Hero;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by Mikhail_Udalyi on 08.06.2016.
 */
public class Cell extends PointImpl implements ICell, Fieldable {

    private List<BaseItem> items = new ArrayList<BaseItem>();

    public Cell(int x, int y) {
        super(x, y);
    }

    public Cell(Point point) {
        super(point);
    }

    @Override
    public void addItem(BaseItem item) {
        if (item.getCell() != null) {
            item.getCell().removeItem(item);
        }

        items.add(item);
        item.setCell(this);
    }

    @Override
    public BaseItem getItem(ItemLogicType type) {
        for (int i = 0; i < items.size(); ++i) {

            if (items.get(i).is(type)) {
                return items.get(i);
            }
        }

        return null;
    }

    @Override
    public BaseItem getItem(int layer) {
        return items.size() <= layer ? new Air(this) : items.get(layer);
    }

    @Override
    public void removeItem(BaseItem item) {
        for (int i = 0; i < items.size(); ++i) {
            if (items.get(i) == item) {
                items.remove(i);
                return;
            }
        }
        items.remove(item);
    }

    @Override
    public boolean isPassable() {
        return !is(ItemLogicType.IMPASSABLE);
    }

    @Override
    public void comeIn(BaseItem comingItem) {
        for (int i = 0; i < items.size() - 1; ++i) {
            BaseItem cellItem = items.get(i);
            if (cellItem != comingItem) {
                cellItem.action(comingItem);
                comingItem.action(cellItem);
            }
        }
    }

    public boolean is(ItemLogicType type) {
        for (int i = 0; i < items.size(); ++i) {

            if (items.get(i).is(type)) {
                return true;
            }
        }

        return false;
    }

    @Override
    public String toString() {
        return String.format("Cell[%s,%s]=%s", x, y, items);
    }

    @Override
    public void init(Field field) {
        for (BaseItem item : items) {
            item.init(field);
        }
    }
}
