package com.codenjoy.dojo.minesweeper.services.ai.logic;

/*-
 * #%L
 * Codenjoy - it's a dojo-like platform from developers to developers.
 * %%
 * Copyright (C) 2018 - 2021 Codenjoy
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

import com.codenjoy.dojo.games.minesweeper.Element;

import java.util.List;

import static com.codenjoy.dojo.games.minesweeper.Element.DETECTOR;
import static com.codenjoy.dojo.games.minesweeper.Element.NONE;
import static java.util.stream.Collectors.toList;

public class Group {

    private List<Cell> list;
    private Element element;

    public Group(List<Cell> cells, Element element) {
        this.element = element;

        list = cells.stream()
                    .map(Cell::copy)
                    .collect(toList());

        Action action = action();
        list.forEach(cell -> cell.action(action));
    }

    public List<Cell> list() {
        return list;
    }

    public int size() {
        return list.size();
    }

    private Action action() {
        if (element == NONE || element == DETECTOR) {
            return Action.GO;
        } else if (size() == element.value()) {
            return Action.MARK;
        } else {
            return Action.NOTHING;
        }
    }
}
