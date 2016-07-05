package com.epam.dojo.icancode.model.interfaces;

import com.codenjoy.dojo.services.Point;

import java.util.List;

public interface ILevel {

    ICell getCell(int x, int y);

    ICell getCell(Point point);

    int getSize();

    <T extends IItem> List<T> getItems(Class clazz);

    ICell[] getCells();

    boolean isBarrier(int x, int y);

    void setField(IField field);
}
