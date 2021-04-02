//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.codenjoy.dojo.minesweeper.client.ai.logic.possibility.v2;

import com.codenjoy.dojo.minesweeper.client.ai.logic.Sequence7;

import java.util.ArrayList;
import java.util.List;

public class Digits {
    private List<com.codenjoy.dojo.minesweeper.client.ai.logic.possibility.v2.IslandMinesCombs> list;

    public Digits(List<com.codenjoy.dojo.minesweeper.client.ai.logic.possibility.v2.IslandMinesCombs> list) {
        this.list = list;
    }

    private int[] getDigits() {
        int[] result = new int[this.list.size()];

        for(int i = 0; i < result.length; ++i) {
            result[i] = ((com.codenjoy.dojo.minesweeper.client.ai.logic.possibility.v2.IslandMinesCombs)this.list.get(i)).size();
        }

        return result;
    }

    public List<com.codenjoy.dojo.minesweeper.client.ai.logic.possibility.v2.MinesCombs> getMinesCombs(int[] code) {
        List<com.codenjoy.dojo.minesweeper.client.ai.logic.possibility.v2.MinesCombs> result = new ArrayList();

        for(int i = 0; i < code.length; ++i) {
            result.add(((IslandMinesCombs)this.list.get(i)).get(code[i]));
        }

        return result;
    }

    public com.codenjoy.dojo.minesweeper.client.ai.logic.possibility.v2.Table getTable() {
        com.codenjoy.dojo.minesweeper.client.ai.logic.Sequence7 sequence7 = new Sequence7(this.getDigits());
        com.codenjoy.dojo.minesweeper.client.ai.logic.possibility.v2.Table result = new Table();

        while(sequence7.hasNext()) {
            int[] code = sequence7.next();
            List<MinesCombs> minesCombsList = this.getMinesCombs(code);
            com.codenjoy.dojo.minesweeper.client.ai.logic.possibility.v2.TableCell tableCell = new TableCell(minesCombsList);
            int mines = tableCell.getMines();
            if (Archipelag.isValid(mines)) {
                result.add(tableCell);
            }
        }

        return result;
    }
}
