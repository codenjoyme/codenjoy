package com.codenjoy.dojo.tetris.model;

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
}
