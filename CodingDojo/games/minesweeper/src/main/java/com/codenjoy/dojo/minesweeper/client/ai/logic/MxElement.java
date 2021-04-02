package com.codenjoy.dojo.minesweeper.client.ai.logic;

public class MxElement {

    private final int mines;
    private int[] mxCountOfMines;

    public MxElement(int mines) {
        this.mines = mines;
    }

    public void addMxCountOfMines(int[] count) {
        if (mxCountOfMines == null) {
            mxCountOfMines = count;
        } else {
            for (int i = 0; i < count.length; ++i) {
                int[] var10000 = mxCountOfMines;
                var10000[i] += count[i];
            }
        }
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        } else if (!(o instanceof MxElement)) {
            return false;
        } else {
            MxElement mxElement = (MxElement) o;
            return mines == mxElement.mines;
        }
    }

    public int hashCode() {
        return mines;
    }
}
