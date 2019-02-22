package com.codenjoy.dojo.sokoban.model.items;

/*-
 * #%L
 * Codenjoy - it's a dojo-like platform from developers to developers.
 * %%
 * Copyright (C) 2018 - 2019 Codenjoy
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
import com.codenjoy.dojo.services.Tickable;
import com.codenjoy.dojo.services.multiplayer.GameField;
import org.json.JSONObject;

public abstract class PointEnriched  <F extends GameField> extends PointImpl implements Tickable{
    protected F field;

    public PointEnriched(int x, int y) {
        super(x, y);
    }

    public PointEnriched(Point point) {
        super(point);
    }

    public PointEnriched(JSONObject json) {
        super(json);
    }

    public void init(F field) {
        this.field = field;
    }
}
