package com.codenjoy.dojo.reversi.client;

/*-
 * #%L
 * Codenjoy - it's a dojo-like platform from developers to developers.
 * %%
 * Copyright (C) 2018 Codenjoy
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


import com.codenjoy.dojo.client.AbstractBoard;
import com.codenjoy.dojo.reversi.model.Elements;
import com.codenjoy.dojo.services.Point;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import static com.codenjoy.dojo.services.PointImpl.pt;

/**
 * Класс, обрабатывающий строковое представление доски.
 * Содержит ряд унаследованных методов {@see AbstractBoard},
 * но ты можешь добавить сюда любые свои методы на их основе.
 */
public class Board extends AbstractBoard<Elements> {

    @Override
    public Elements valueOf(char ch) {
        return Elements.valueOf(ch);
    }

    public boolean isWhite(int x, int y) {
        return isAt(x, y, Elements.WHITE)
                || isAt(x, y, Elements.WHITE_TURN)
                || isAt(x, y, Elements.WHITE_STOP);
    }

    public boolean isBlack(int x, int y) {
        return isAt(x, y, Elements.BLACK)
                || isAt(x, y, Elements.BLACK_TURN)
                || isAt(x, y, Elements.BLACK_STOP);
    }

    public boolean isMyTurn() {
        return get(Elements.WHITE_STOP, Elements.BLACK_STOP).isEmpty();
    }

    @Override
    protected int inversionY(int y) { // TODO почему тут ивертирование
        return size - 1 - y;
    }

    public boolean myColor() {
        if (isMyTurn()) {
            return !get(Elements.WHITE_TURN).isEmpty();
        } else {
            return get(Elements.WHITE_TURN).isEmpty();
        }
    }
}
