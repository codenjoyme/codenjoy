package com.epam.dojo.icancode.services;

/*-
 * #%L
 * iCanCode - it's a dojo-like platform from developers to developers.
 * %%
 * Copyright (C) 2016 EPAM
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

import java.util.LinkedList;
import java.util.List;

/**
 * Created by Mikhail_Udalyi on 22.06.2016.
 */

public class PrinterData {

    private List<String> layers;
    private List<Point> heroes;

    public PrinterData() {
        this.layers = new LinkedList<>();
        this.heroes = new LinkedList<>();
    }

    public void add(Point hero) {
        heroes.add(new PointImpl(hero));
    }

    public void add(String layer) {
        layers.add(layer.replaceAll("\n", ""));
    }

    public List<String> getLayers() {
        return layers;
    }

    public List<Point> getHeroes() {
        return heroes;
    }
}
