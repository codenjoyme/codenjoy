package com.codenjoy.dojo.minesweeper.client.ai.vaa25.logic.possibility;


import com.codenjoy.dojo.minesweeper.client.ai.vaa25.logic.Cell;

import java.util.List;

/**
 * @author Alexander Vlasov
 */
public class DeepPossibilityResolver {
    private boolean noIslands;
    private SummaryMinesAndCombinations summaryMinesAndCombinations = new SummaryMinesAndCombinations();

    public DeepPossibilityResolver(int islandAmount) {
        if (islandAmount <= 0) {
            noIslands = true;
            return;
        }
        noIslands = false;
    }

    public void add(MinesAndCombinationAmountsOfIsland mx0) {
        summaryMinesAndCombinations.add(mx0);
    }


    public double resolve(int wholeMinesLeft, int deepCellsAmount) {
        if (deepCellsAmount == 0) {
            return 100;
        }
        if (noIslands) {
            return 100.0 * wholeMinesLeft / deepCellsAmount;
        }
        summaryMinesAndCombinations.setDeepCellsAmount(deepCellsAmount);
        summaryMinesAndCombinations.setMinesLeft(wholeMinesLeft);
        summaryMinesAndCombinations.calculateSummary();
//        summaryMinesAndCombinations.calculateIslandsPossibilities();
        double result = summaryMinesAndCombinations.getDeepPossibility();
        return result;
    }

    public List<Cell> getMinPosCells() {
        return summaryMinesAndCombinations.getMinPosCells();
    }
}
