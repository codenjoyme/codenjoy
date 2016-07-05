package com.epam.dojo.icancode.model.interfaces;

public interface IField {

    boolean isBarrier(int x, int y);

    ICell getStartPosition();

    void move(IItem item, int x, int y);

    ICell getCell(int x, int y);

    void reset();
}
