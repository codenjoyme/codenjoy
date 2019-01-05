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

public enum Type {

    I("I", Elements.BLUE) {
        @Override
        public Figure create() {
            return new FigureImpl(0, 1, I, "#", "#", "#", "#");
        }
    },

    J("J", Elements.CYAN) {
        @Override
        public Figure create() {
            return new FigureImpl(1, 1, J, " #", " #", "##");
        }
    },

    L("L", Elements.ORANGE) {
        @Override
        public Figure create() {
            return new FigureImpl(0, 1, L, "# ", "# ", "##");
        }
    },

    O("O", Elements.YELLOW) {
        @Override
        public Figure create() {
            return new FigureImpl(0, 0, O, "##", "##");
        }
    },

    S("S", Elements.GREEN) {
        @Override
        public Figure create() {
            return new FigureImpl(1, 1, S, " ##", "## ");
        }
    },

    T("T", Elements.PURPLE) {
        @Override
        public Figure create() {
            return new FigureImpl(1, 1, T, " # ", "###");
        }
    },

    Z("Z", Elements.RED) {
        @Override
        public Figure create() {
            return new FigureImpl(1, 1, Z, "## ", " ##");
        }
    };

    private String name;
    private Elements color;

    Type(String name, Elements color) {
        this.name = name;
        this.color = color;
    }

    public String getName() {
        return name;
    }

    public Elements getColor() {
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
