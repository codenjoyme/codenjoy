package com.epam.dojo.icancode.model;

import com.codenjoy.dojo.services.Point;

import java.util.List;

/**
 * Я вот для простоты и удобства хочу указывать борду в тестовом виде, а реализация этого интерфейса позволяет мне это сделать
 */
public interface Level extends Fieldable {

    /**
     * @return Размер поля (обязательно квадратное)
     */
    int getSize();

    ICell getCell(int x, int y);

    ICell getCell(Point point);

    Iterable<? extends Point> getElements(int layer);

    List<ICell> getCells(ItemLogicType type);

    boolean isBarrier(int x, int y);
}
