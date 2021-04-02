//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.codenjoy.dojo.minesweeper.client.ai.logic.possibility.v2;

import java.util.ArrayList;
import java.util.List;

public class TableCellGroup {
    private int mines;
    private int combs;
    private List<com.codenjoy.dojo.minesweeper.client.ai.logic.possibility.v2.TableCell> tableCells = new ArrayList();

    public TableCellGroup(int mines) {
        this.mines = mines;
    }

    public void add(TableCell tableCell) {
        this.tableCells.add(tableCell);
        this.combs += tableCell.getCombs();
    }

    public int getCombs() {
        return this.combs;
    }

    public int getMines() {
        return this.mines;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(this.mines).append("->").append(this.combs);
        return sb.toString();
    }
}
