package com.epam.dojo.icancode.model;

import com.codenjoy.dojo.services.Point;
import com.epam.dojo.icancode.model.items.BaseItem;

/**
 * Так случилось что у меня доска знает про героя, а герой про доску. И чтобы герой не знал про всю доску, я ему даю вот эту часть доски.
 */
public interface Field {

    boolean isBarrier(int x, int y);

    ICell getStartPosition();

    void move(BaseItem item, int x, int y);

    ICell getCell(int x, int y);

    void reset();
}
