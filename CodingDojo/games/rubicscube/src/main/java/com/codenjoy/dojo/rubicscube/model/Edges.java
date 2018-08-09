package com.codenjoy.dojo.rubicscube.model;

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


import java.util.LinkedList;
import java.util.List;

public class Edges {

    private List<Edge> edges;

    public Edges() {
        edges = new LinkedList<>();
    }

    public void add(Face face1, Face face2, Elements color1, Elements color2) {
        edges.add(new Edge(face1, face2, color1, color2));
    }

    public Edge find(String colors) {
        for (Edge edge : edges) {
            if (edge.toString().contains(":" + colors)) {
                return edge;
            }
        }
        throw new IllegalArgumentException("Грань не найдена: " + colors);
    }

    @Override
    public String toString() {
        return edges.toString();
    }
}
