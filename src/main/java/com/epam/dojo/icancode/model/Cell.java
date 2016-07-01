package com.epam.dojo.icancode.model;

import com.codenjoy.dojo.services.Point;
import com.codenjoy.dojo.services.PointImpl;
import com.epam.dojo.icancode.model.enums.FeatureItem;
import com.epam.dojo.icancode.model.interfaces.Field;
import com.epam.dojo.icancode.model.interfaces.Fieldable;
import com.epam.dojo.icancode.model.interfaces.ICell;
import com.epam.dojo.icancode.model.interfaces.IItem;
import com.epam.dojo.icancode.model.items.Air;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by Mikhail_Udalyi on 08.06.2016.
 */
public class Cell extends PointImpl implements ICell, Fieldable {

    private List<IItem> items = new ArrayList<IItem>();

    //================================ Constructors ================================

    public Cell(int x, int y) {
        super(x, y);
    }

    public Cell(Point point) {
        super(point);
    }

    //================================ Implements ================================

    public void addItem(IItem item) {
        if (item.getCell() != null) {
            item.getCell().removeItem(item);
        }

        items.add(item);
        item.setCell(this);
    }

    public void comeIn(IItem comingItem) {
        for (int i = 0; i < items.size(); ++i) {
            IItem cellItem = items.get(i);

            if (cellItem != comingItem) {
                cellItem.action(comingItem);
                comingItem.action(cellItem);
            }
        }
    }

    public void init(Field field) {
        for (IItem item : items) {
            item.init(field);
        }
    }

    public boolean isPassable() {
        for (int i = 0; i < items.size(); ++i) {
            if (items.get(i).hasFeatures(new FeatureItem[]{FeatureItem.IMPASSABLE})) {
                return false;
            }
        }

        return true;
    }

    public <T extends IItem> T getItem(T type) {
        for (int i = 0; i < items.size(); ++i) {

            if (items.get(i).getClass() == type.getClass()) {
                return (T) items.get(i);
            }
        }

        return null;
    }

    public <T extends IItem> T getItem(int layer) {
        return (T) (items.size() <= layer ? new Air() : items.get(layer));
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

    public <T extends IItem> List<T> getItems() {
        return (List<T>) new LinkedList<>(items);
    }

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
