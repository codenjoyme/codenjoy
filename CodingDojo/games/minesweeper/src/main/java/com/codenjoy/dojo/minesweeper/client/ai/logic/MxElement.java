package com.codenjoy.dojo.minesweeper.client.ai.logic;

public class MxElement {

    private int mines;
    private int[] count;

    public MxElement(int mines) {
        this.mines = mines;
    }

    public void add(int[] count) {
        if (this.count == null) {
            this.count = count;
        } else {
            for (int i = 0; i < count.length; ++i) {
                this.count[i] += count[i];
            }
        }
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (!(o instanceof MxElement)) {
            return false;
        }

        return mines == ((MxElement) o).mines;
    }

    public int hashCode() {
        return mines;
    }
}
