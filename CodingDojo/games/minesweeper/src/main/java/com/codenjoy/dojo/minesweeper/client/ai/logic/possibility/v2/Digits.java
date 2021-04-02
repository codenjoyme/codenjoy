

package com.codenjoy.dojo.minesweeper.client.ai.logic.possibility.v2;

import com.codenjoy.dojo.minesweeper.client.ai.logic.Sequence7;

import java.util.ArrayList;
import java.util.List;

public class Digits {
    private List<IslandMinesCombs> list;

    public Digits(List<IslandMinesCombs> list) {
        this.list = list;
    }

    private int[] getDigits() {
        int[] result = new int[this.list.size()];

        for(int i = 0; i < result.length; ++i) {
            result[i] = this.list.get(i).size();
        }

        return result;
    }

    public List<MinesCombs> getMinesCombs(int[] code) {
        List<MinesCombs> result = new ArrayList();

        for(int i = 0; i < code.length; ++i) {
            result.add((this.list.get(i)).get(code[i]));
        }

        return result;
    }

    public Table getTable() {
        Sequence7 sequence7 = new Sequence7(this.getDigits());
        Table result = new Table();

        while(sequence7.hasNext()) {
            int[] code = sequence7.next();
            List<MinesCombs> minesCombsList = this.getMinesCombs(code);
            TableCell tableCell = new TableCell(minesCombsList);
            int mines = tableCell.getMines();
            if (Archipelag.isValid(mines)) {
                result.add(tableCell);
            }
        }

        return result;
    }
}
