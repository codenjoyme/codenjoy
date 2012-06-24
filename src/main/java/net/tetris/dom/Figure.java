package net.tetris.dom;

public interface Figure {
    Type getType();

    public enum Type {
        I("I") , J("J") , L("L") , O("O") , S("S") , T("T") , Z("Z");


        private String name;

        Type(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }

    }

    int getLeft();

    int getRight();

    int getTop();

    int getBottom();

    int[] getRowCodes();

    int getWidth();
}
