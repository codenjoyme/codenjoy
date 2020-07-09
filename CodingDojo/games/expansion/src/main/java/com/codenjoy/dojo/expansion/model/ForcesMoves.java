package com.codenjoy.dojo.expansion.model;

/*-
 * #%L
 * expansion - it's a dojo-like platform from developers to developers.
 * %%
 * Copyright (C) 2016 - 2020 Codenjoy
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


import com.codenjoy.dojo.services.QDirection;
import com.codenjoy.dojo.services.Point;
import org.json.JSONObject;

/**
 * Created by Oleksandr_Baglai on 2017-08-29.
 */
public class ForcesMoves extends Forces {

    private QDirection direction;

    public ForcesMoves(Point region, int count, QDirection direction) {
        super(region, count);
        this.direction = direction;
    }

    public ForcesMoves(JSONObject json) {
        super(json);
        if (json.has("direction")) {
            direction = QDirection.valueOf(json.getString("direction").toUpperCase());
        } else {
            direction = QDirection.NONE;
        }
    }

    public String getDirection() {
        return direction.toString();
    }

    public Point getDestination(Point from) {
        if (direction == QDirection.NONE) {
            return from;
        }
        return direction.change(from);
    }
}
