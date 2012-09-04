package net.tetris.dom;

public interface Figure {
    Type getType();

    Figure rotate(int times);

    public enum Type {
        I("I") {
            @Override
            public Figure createNewFigure() {
                return new TetrisFigure(0, 1, I, "#", "#", "#", "#");
            }
        }, J("J") {
            @Override
            public Figure createNewFigure() {
                return new TetrisFigure(1, 1, J, " #", " #", "##");
            }
        }, L("L") {
            @Override
            public Figure createNewFigure() {
                return new TetrisFigure(0, 1, L, "# ", "# ", "##");
            }
        }, O("O") {
            @Override
            public Figure createNewFigure() {
                return new TetrisFigure(0, 0, O, "##", "##");
            }
        }, S("S") {
            @Override
            public Figure createNewFigure() {
                return new TetrisFigure(1, 1, S, " ##", "## ");
            }
        }, T("T") {
            @Override
            public Figure createNewFigure() {
                return new TetrisFigure(1, 1, T, " # ", "###");
            }
        }, Z("Z") {
            @Override
            public Figure createNewFigure() {
                return new TetrisFigure(1, 1, Z, "## ", " ##");
            }
        };


        private String name;

        Type(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }

        public abstract Figure createNewFigure();
    }

    int getLeft();

    int getRight();

    int getTop();

    int getBottom();

    int[] getRowCodes();

    int getWidth();

    Figure getCopy();
}
