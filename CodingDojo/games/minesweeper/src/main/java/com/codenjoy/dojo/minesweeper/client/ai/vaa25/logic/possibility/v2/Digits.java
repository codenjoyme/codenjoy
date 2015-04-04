package com.codenjoy.dojo.minesweeper.client.ai.vaa25.logic.possibility.v2;


import com.codenjoy.dojo.minesweeper.client.ai.vaa25.logic.Sequence7;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Alexander Vlasov
 */
public class Digits {
    private List<IslandMinesCombs> list;

    public Digits(List<IslandMinesCombs> list) {
        this.list = list;
    }

    private int[] getDigits() {
        int[] result = new int[list.size()];
        for (int i = 0; i < result.length; i++) {
            result[i] = list.get(i).size();
        }
        return result;
    }

    public List<MinesCombs> getMinesCombs(int[] code) {
        List<MinesCombs> result = new ArrayList<MinesCombs>();
        for (int i = 0; i < code.length; i++) {
            result.add(list.get(i).get(code[i]));
        }
        return result;
    }

    public Table getTable() {
        Sequence7 sequence7 = new Sequence7(getDigits());
        Table result = new Table();
        while (sequence7.hasNext()) {
            int[] code = sequence7.next();
            List<MinesCombs> minesCombsList = getMinesCombs(code);
            TableCell tableCell = new TableCell(minesCombsList);
            int mines = tableCell.getMines();
            if (Archipelag.isValid(mines)) {
                result.add(tableCell);
            }
        }
        return result;
    }

}
