package com.codenjoy.dojo.tetris.model;

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


public interface Figure {
    Type getType();

    Figure rotate(int times);

    public enum Type {
        I("I", PlotColor.BLUE) {
            @Override
            public Figure createNewFigure() {
                return new TetrisFigure(0, 1, I, "#", "#", "#", "#");
            }
        }, J("J", PlotColor.CYAN) {
            @Override
            public Figure createNewFigure() {
                return new TetrisFigure(1, 1, J, " #", " #", "##");
            }
        }, L("L", PlotColor.ORANGE) {
            @Override
            public Figure createNewFigure() {
                return new TetrisFigure(0, 1, L, "# ", "# ", "##");
            }
        }, O("O", PlotColor.YELLOW) {
            @Override
            public Figure createNewFigure() {
                return new TetrisFigure(0, 0, O, "##", "##");
            }
        }, S("S", PlotColor.GREEN) {
            @Override
            public Figure createNewFigure() {
                return new TetrisFigure(1, 1, S, " ##", "## ");
            }
        }, T("T", PlotColor.PURPLE) {
            @Override
            public Figure createNewFigure() {
                return new TetrisFigure(1, 1, T, " # ", "###");
            }
        }, Z("Z", PlotColor.RED) {
            @Override
            public Figure createNewFigure() {
                return new TetrisFigure(1, 1, Z, "## ", " ##");
            }
        };


        private String name;
        private PlotColor color;

        Type(String name, PlotColor color) {
            this.name = name;
            this.color = color;
        }


        public String getName() {
            return name;
        }

        public PlotColor getColor() {
            return color;
        }

        public abstract Figure createNewFigure();
    }

    int getLeft();

    int getRight();

    int getTop();

    int getBottom();

    int[] getRowCodes(boolean ignoreColors);

    int getWidth();

    Figure getCopy();
}
