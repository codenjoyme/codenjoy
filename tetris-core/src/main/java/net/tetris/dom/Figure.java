package net.tetris.dom;

import net.tetris.services.Plot;
import net.tetris.services.PlotColor;

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
