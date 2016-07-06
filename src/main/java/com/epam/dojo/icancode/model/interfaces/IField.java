package com.epam.dojo.icancode.model.interfaces;

public interface IField {

    boolean isBarrier(int x, int y);

    ICell getStartPosition();

    ICell getEndPosition();

    void move(IItem item, int x, int y);

    ICell getCell(int x, int y);

    void reset();
}
