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

public class Edge {
    public Elements color1;
    public Elements color2;
    public Face face1;
    public Face face2;

    public Edge(Face face1, Face face2, Elements color1, Elements color2) {
        this.face1 = face1;
        this.face2 = face2;
        this.color1 = color1;
        this.color2 = color2;
    }

    @Override
    public String toString() {
        return String.format("[%s-%s:%s%s]",
                face1.name(),
                face2.name(),
                color1.value(),
                color2.value());
    }
}
