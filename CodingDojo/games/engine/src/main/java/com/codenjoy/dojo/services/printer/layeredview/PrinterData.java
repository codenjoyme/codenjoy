package com.codenjoy.dojo.services.printer.layeredview;

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


import com.codenjoy.dojo.services.Point;
import com.codenjoy.dojo.services.PointImpl;
import com.codenjoy.dojo.utils.JsonUtils;

import java.util.LinkedList;
import java.util.List;

public class PrinterData {

    private List<String> layers;
    private Point offset;
    private int mapSize;
    private int viewSize;

    public PrinterData() {
        this.layers = new LinkedList<>();
    }

    public void setOffset(Point point) {
        offset = new PointImpl(point);
    }

    public void addLayer(String layer) {
        layers.add(layer);
    }

    public List<String> getLayers() {
        return layers;
    }

    public Point getOffset() {
        return offset;
    }

    @Override
    public String toString() {
        return JsonUtils.toStringSorted(this);
    }

    public void setViewSize(int viewSize) {
        this.viewSize = viewSize;
    }

    public int getViewSize() {
        return viewSize;
    }

    public void setMapSize(int mapSize) {
        this.mapSize = mapSize;
    }

    public int getMapSize() {
        return mapSize;
    }
}
