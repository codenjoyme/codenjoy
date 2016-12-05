package com.codenjoy.dojo.services.hero;

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
import com.codenjoy.dojo.services.PointImpl;

/**
 * Created by indigo on 2016-10-30.
 */
public class HeroDataImpl implements HeroData {

    private final Point coordinate;
    private final boolean isSingleMode;
    private final Object additionalData;

    HeroDataImpl(Point coordinate, boolean isSingleMode, Object additionalData) {
        if (coordinate == null) {
            this.coordinate = PointImpl.pt(-1, -1);
        } else {
            this.coordinate = new PointImpl(coordinate);
        }
        this.isSingleMode = isSingleMode;
        this.additionalData = additionalData;
    }

    @Override
    public String toString() {
        return "HeroData[" +
                "coordinate=" + coordinate +
                ", isSingleMode=" + isSingleMode +
                ", additionalData=" + additionalData +
                ']';
    }

    HeroDataImpl(Point coordinate, boolean isSingleMode) {
        this(coordinate, isSingleMode, null);
    }

    HeroDataImpl(boolean isSingleMode) {
        this(null, isSingleMode, null);
    }

    @Override
    public Point getCoordinate() {
        return coordinate;
    }

    @Override
    public Object getAdditionalData() {
        return additionalData;
    }

    @Override
    public boolean isSingleBoardGame() {
        return isSingleMode;
    }

}
