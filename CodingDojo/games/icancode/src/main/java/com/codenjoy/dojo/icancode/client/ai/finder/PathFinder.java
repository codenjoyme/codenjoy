package com.codenjoy.dojo.icancode.client.ai.finder;

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


/**
 * Created by Mikhail_Udalyi on 07.10.2016.
 */
public abstract class PathFinder {
    int distance = Integer.MAX_VALUE;

    IPathGrid mygrid;

    IPathGrid doneGrid;

    public PathFinder() {
        this(30000);
    }

    public PathFinder(int maxDistance) {
        this.distance = maxDistance;
    }

    public int getDistance() {
        return distance;
    }

    public abstract int[] findPath(IPathGrid grid, int x1, int y1, int x2, int y2);

    protected boolean hasProblem(IPathGrid grid, int x1, int y1, int x2, int y2) {
        boolean problem = false;

        if (grid == null || grid.getWidth() == 0) {
            return true;
        }

        mygrid = grid;
        doneGrid = new PathGrid(mygrid.getWidth(), mygrid.getHeight());

        if (mygrid.getGrid(x1, y1) || mygrid.getGrid(x2, y2)) {
            problem = true;
        }

        return problem;
    }
}
