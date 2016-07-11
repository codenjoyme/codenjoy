package com.epam.dojo.icancode.model;

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
