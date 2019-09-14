package com.codenjoy.dojo.expansion.model.levels;

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


import com.codenjoy.dojo.expansion.model.IField;
import com.codenjoy.dojo.services.Point;

import java.util.List;
import java.util.function.Predicate;

public interface Level {

    Cell getCell(int x, int y);

    Cell getCell(Point point);

    int getSize();

    int getViewSize();

    <T> List<T> getItems(Class<T> clazz);

    Cell[] getCells();

    boolean isBarrier(int x, int y);

    List<Cell> getCellsWith(Class clazz);

    List<Cell> getCellsWith(Predicate<Cell> is);

    void setField(IField field);

    String getName();
}
