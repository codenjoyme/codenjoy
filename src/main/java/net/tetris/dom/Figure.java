package net.tetris.dom;

public interface Figure {
    Type getType();

    Figure rotate(int times);

    public enum Type {
        I("I") {
            @Override
            public Figure createNewFigure() {
                return new TetrisFigure(0, 1, "#", "#", "#", "#");
            }
        }, J("J") {
            @Override
            public Figure createNewFigure() {
                return new TetrisFigure(1, 1, " #", " #", "##");
            }
        }, L("L") {
            @Override
            public Figure createNewFigure() {
                return new TetrisFigure(0, 1, "# ", "# ", "##");
            }
        }, O("O") {
            @Override
            public Figure createNewFigure() {
                return new TetrisFigure(0, 0, "##", "##");
            }
        }, S("S") {
            @Override
            public Figure createNewFigure() {
                return new TetrisFigure(1, 1, " ##", "## ");
            }
        }, T("T") {
            @Override
            public Figure createNewFigure() {
                return new TetrisFigure(1, 1, " # ", "###");
            }
        }, Z("Z") {
            @Override
            public Figure createNewFigure() {
                return new TetrisFigure(1, 1, "## ", " ##");
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
