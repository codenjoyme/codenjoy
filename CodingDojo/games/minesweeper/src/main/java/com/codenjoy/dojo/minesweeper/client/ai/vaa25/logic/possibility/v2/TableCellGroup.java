package com.codenjoy.dojo.minesweeper.client.ai.vaa25.logic.possibility.v2;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Alexander Vlasov
 */
public class TableCellGroup {
    private int mines;
    private int combs;
    private List<TableCell> tableCells = new ArrayList<TableCell>();

    public TableCellGroup(int mines) {
        this.mines = mines;
    }

    public void add(TableCell tableCell) {
        tableCells.add(tableCell);
        combs += tableCell.getCombs();
    }

    public int getCombs() {
        return combs;
    }

    public int getMines() {
        return mines;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append(mines).append("->").append(combs);
        return sb.toString();
    }
}
