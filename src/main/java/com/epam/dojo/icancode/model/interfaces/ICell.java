package com.epam.dojo.icancode.model.interfaces;

import com.codenjoy.dojo.services.Point;

import java.util.List;

/**
 * Created by Mikhail_Udalyi on 08.06.2016.
 */
public interface ICell extends Point {

    void addItem(IItem item);

    void comeIn(IItem item);

    boolean isPassable();

    <T extends IItem> T getItem(T type);

    <T extends IItem> T getItem(int layer);

    <T extends IItem> List<T> getItems(Class clazz);

    <T extends IItem> List<T> getItems();

    void removeItem(IItem item);
}
