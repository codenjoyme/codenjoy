

package com.codenjoy.dojo.minesweeper.client.ai.logic.possibility;

public class MxElement {
    private int mines;
    private int combAmount;
    private int wholeCombAmount;
    private int[] mxCountOfMines;

    public MxElement(int mines) {
        this.mines = mines;
        this.combAmount = 0;
    }

    public int[] getWholeMxCountOfMines() {
        int[] result = new int[this.mxCountOfMines.length];

        for(int i = 0; i < result.length; ++i) {
            result[i] = this.mxCountOfMines[i] * this.wholeCombAmount;
        }

        return result;
    }

    public int[] getMxCountOfMines() {
        return this.mxCountOfMines;
    }

    public void addCombAmount(int value) {
        this.combAmount += value;
    }

    public int getCombAmount() {
        return this.combAmount;
    }

    public int getMinesAmount() {
        return this.mines;
    }

    public void addWholeCombAmount(int combs) {
        this.wholeCombAmount += combs;
    }

    public int getWholeCombAmount() {
        return this.wholeCombAmount;
    }

    public void addMxCountOfMines(int[] mxCountOfMines) {
        if (this.mxCountOfMines == null) {
            this.mxCountOfMines = mxCountOfMines;
        } else {
            for(int i = 0; i < mxCountOfMines.length; ++i) {
                int[] var10000 = this.mxCountOfMines;
                var10000[i] += mxCountOfMines[i];
            }
        }

    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        } else if (!(o instanceof MxElement)) {
            return false;
        } else {
            MxElement mxElement = (MxElement)o;
            return this.mines == mxElement.mines;
        }
    }

    public int hashCode() {
        return this.mines;
    }
}
