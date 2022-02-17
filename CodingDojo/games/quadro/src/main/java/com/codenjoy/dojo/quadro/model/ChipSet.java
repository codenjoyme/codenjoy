package com.codenjoy.dojo.quadro.model;

/*-
 * #%L
 * Codenjoy - it's a dojo-like platform from developers to developers.
 * %%
 * Copyright (C) 2016 - 2018 Codenjoy
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

import com.codenjoy.dojo.quadro.model.items.Chip;
import com.codenjoy.dojo.services.Point;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

class ChipSet {

    private Map<Point, Chip> chips = new HashMap<>();

    void put(Chip chip) {
        chips.put(chip, chip);
    }

    void putAll(List<Chip> list) {
        list.forEach(chip -> put(chip));
    }

    int size() {
        return chips.size();
    }

    boolean contains(Point point) {
        // TODO do not use map.containsKey just check that map.get() != null
        return chips.containsKey(point);
    }

    Chip get(Point point) {
        return chips.get(point);
    }

    Collection<Chip> chips() {
        return chips.values();
    }

    void clear() {
        chips.clear();
    }
}
