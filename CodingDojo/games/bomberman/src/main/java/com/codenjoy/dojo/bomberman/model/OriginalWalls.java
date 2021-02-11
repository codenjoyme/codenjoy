package com.codenjoy.dojo.bomberman.model;

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


import com.codenjoy.dojo.services.settings.Parameter;

import java.util.List;

import static com.codenjoy.dojo.services.PointImpl.pt;

public class OriginalWalls extends WallsImpl implements Iterable<Wall> {

    private int currentSize;
    private Parameter<Integer> size;

    public OriginalWalls(Parameter<Integer> size) {
        super();
        currentSize = size.getValue();
        this.size = size;

        regenerate();
    }

    private void regenerate() {
        for (int x = 0; x < size.getValue(); x++) {
            add(x, 0);
            add(x, size.getValue() - 1);
        }

        final int D = 1;
        for (int y = D; y < size.getValue() - D; y++) {
            add(0, y);
            add(size.getValue() - 1, y);
        }

        for (int x = 2; x <= size.getValue() - 2; x++) {
            for (int y = 2; y <= size.getValue() - 2; y++) {
                if (y % 2 != 0 || x % 2 != 0) {
                    continue;
                }
                add(x, y);
            }
        }
    }

    private void add(int x, int y) {
        add(pt(x, y));
    }

    @Override
    public void tick() {
        super.tick(); // TODO протестить эту строчку

        if (currentSize != size.getValue()) {
            currentSize = size.getValue();
            List<Wall> walls = this.listSubtypes(Wall.class);
            for (Wall wall : walls.toArray(new Wall[0])) {
                destroy(wall);
            }

            regenerate();
        }
    }

}
