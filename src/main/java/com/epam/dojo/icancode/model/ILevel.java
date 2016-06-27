package com.epam.dojo.icancode.model;

import com.codenjoy.dojo.services.Point;
import com.epam.dojo.icancode.model.items.BaseItem;

import java.util.List;

/**
 * Я вот для простоты и удобства хочу указывать борду в тестовом виде, а реализация этого интерфейса позволяет мне это сделать
 */
public interface ILevel extends Fieldable {

    /**
     * @return Размер поля (обязательно квадратное)
     */
    int getSize();

    ICell getCell(int x, int y);

    ICell getCell(Point point);

    List<ICell> getCells(ItemLogicType type);

    ICell[] getCells();

    boolean isBarrier(int x, int y);

    List<BaseItem> getItems(Class clazz);
}
