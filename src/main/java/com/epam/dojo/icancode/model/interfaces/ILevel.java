package com.epam.dojo.icancode.model.interfaces;

import com.codenjoy.dojo.services.Point;
import com.epam.dojo.icancode.model.items.BaseItem;

import java.util.List;

/**
 * Я вот для простоты и удобства хочу указывать борду в тестовом виде, а реализация этого интерфейса позволяет мне это сделать
 */
public interface ILevel extends Fieldable {

    ICell getCell(int x, int y);

    ICell getCell(Point point);

    int getSize();

    <T extends IItem> List<T> getItems(Class clazz);

    ICell[] getCells();

    boolean isBarrier(int x, int y);
}
