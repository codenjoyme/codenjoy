package com.codenjoy.dojo.minesweeper.client.ai.vaa25.logic.possibility;

/**
 * @author Alexander Vlasov
 */
public class MxElement {
    private int mines;
    private int combAmount;
    private int wholeCombAmount;
    private int[] mxCountOfMines;

    public MxElement(int mines) {
        this.mines = mines;
        combAmount = 0;
    }

    public int[] getWholeMxCountOfMines() {
        int[] result = new int[mxCountOfMines.length];
        for (int i = 0; i < result.length; i++) {
            result[i] = mxCountOfMines[i] * wholeCombAmount;// /combAmount
        }
        return result;
    }

    public int[] getMxCountOfMines() {
        return mxCountOfMines;
    }

    public void addCombAmount(int value) {
        combAmount += value;
    }

    public int getCombAmount() {
        return combAmount;
    }

    public int getMinesAmount() {
        return mines;
    }

    public void addWholeCombAmount(int combs) {
        wholeCombAmount += combs;
    }

    public int getWholeCombAmount() {
        return wholeCombAmount;
    }

    public void addMxCountOfMines(int[] mxCountOfMines) {
        if (this.mxCountOfMines == null) {
            this.mxCountOfMines = mxCountOfMines;
        } else {
            for (int i = 0; i < mxCountOfMines.length; i++) {
                this.mxCountOfMines[i] += mxCountOfMines[i];
            }
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof MxElement)) return false;

        MxElement mxElement = (MxElement) o;

        if (mines != mxElement.mines) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return mines;
    }
}
