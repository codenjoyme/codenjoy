package com.codenjoy.dojo.tetris.model;

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

import com.codenjoy.dojo.games.tetris.Element;

public enum Type {

    I("I", Element.BLUE) {
        @Override
        public Figure create() {
            return new FigureImpl(0, 1, I, "#", "#", "#", "#");
        }
    },

    J("J", Element.CYAN) {
        @Override
        public Figure create() {
            return new FigureImpl(1, 1, J, " #", " #", "##");
        }
    },

    L("L", Element.ORANGE) {
        @Override
        public Figure create() {
            return new FigureImpl(0, 1, L, "# ", "# ", "##");
        }
    },

    O("O", Element.YELLOW) {
        @Override
        public Figure create() {
            return new FigureImpl(0, 0, O, "##", "##");
        }
    },

    S("S", Element.GREEN) {
        @Override
        public Figure create() {
            return new FigureImpl(1, 1, S, " ##", "## ");
        }
    },

    T("T", Element.PURPLE) {
        @Override
        public Figure create() {
            return new FigureImpl(1, 1, T, " # ", "###");
        }
    },

    Z("Z", Element.RED) {
        @Override
        public Figure create() {
            return new FigureImpl(1, 1, Z, "## ", " ##");
        }
    };

    private String name;
    private Element color;

    Type(String name, Element color) {
        this.name = name;
        this.color = color;
    }

    public String getName() {
        return name;
    }

    public Element getColor() {
        return color;
    }

    public abstract Figure create();

    public static Type getByIndex(int figureIndex) {
        for (Type type : values()) {
            if (type.getColor().index() == figureIndex) {
                return type;
            }
        }
        throw new RuntimeException("Type not found by FigureIndex:" + figureIndex);
    }
}
