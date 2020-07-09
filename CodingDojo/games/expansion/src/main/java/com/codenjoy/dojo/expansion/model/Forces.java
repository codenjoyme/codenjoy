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


import com.codenjoy.dojo.services.Point;
import com.codenjoy.dojo.services.PointImpl;
import com.codenjoy.dojo.utils.JsonUtils;
import org.json.JSONObject;

import static com.codenjoy.dojo.services.PointImpl.pt;

/**
 * Created by Oleksandr_Baglai on 2017-08-29.
 */
public class Forces {

    private int count;
    private Point region;

    public Forces(Point region, int count) {
        this.region = new PointImpl(region);
        this.count = count;
    }

    public Forces(JSONObject json) {
        count = json.getInt("count");
        JSONObject pt = json.getJSONObject("region");
        region = pt(pt.getInt("x"), pt.getInt("y"));
    }

    public Point getRegion() {
        return region;
    }

    public int getCount() {
        return count;
    }

    public String json() {
        return JsonUtils.toStringSorted(this);
    }

    @Override
    public String toString() {
        return String.format("[%s,%s]=%s", region.getX(), region.getY(), count);
    }
}
