package com.codenjoy.dojo.sokoban.model.items;

/*-
 * #%L
 * Codenjoy - it's a dojo-like platform from developers to developers.
 * %%
 * Copyright (C) 2016 Codenjoy
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/gpl-3.0.html>.
 * #L%
 */


import com.codenjoy.dojo.services.Point;
import com.codenjoy.dojo.services.multiplayer.GameField;
import com.codenjoy.dojo.sokoban.services.Player;

/**
 * Так случилось что доска знает про героя, а герой про доску.
 * И чтобы герой не знал про всю доску, я ему даю вот эту часть доски.
 */
public interface Field extends GameField<Player> {

    boolean isBarrier(Point pt);

    boolean isBox(Point pt);

    boolean isBoxOnTheMark(Point pt);

    void moveBox(Point pt, Point newPt);

    void setBox(Point pt);

    boolean isMark(Point pt);

    Point getFreeRandom();

    boolean isFree(Point pt);

    boolean isBomb(Point pt);

    void setBomb(Point pt);

    void setBoxOnTheMark(Point pt);

    void removeBoxOnTheMark(Point pt);

    void removeBox(Point pt);

    void removeBomb(Point pt);

    void setMark(Point pt);
}
