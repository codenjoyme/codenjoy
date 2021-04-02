

package com.codenjoy.dojo.minesweeper.client.ai.logic.possibility;

import com.codenjoy.dojo.minesweeper.client.ai.logic.Cell;

import java.util.List;

public class DeepPossibilityResolver {
    private boolean noIslands;
    private SummaryMinesAndCombinations summaryMinesAndCombinations = new SummaryMinesAndCombinations();

    public DeepPossibilityResolver(int islandAmount) {
        if (islandAmount <= 0) {
            this.noIslands = true;
        } else {
            this.noIslands = false;
        }
    }

    public void add(MinesAndCombinationAmountsOfIsland mx0) {
        this.summaryMinesAndCombinations.add(mx0);
    }

    public double resolve(int wholeMinesLeft, int deepCellsAmount) {
        if (deepCellsAmount == 0) {
            return 100.0D;
        } else if (this.noIslands) {
            return 100.0D * (double)wholeMinesLeft / (double)deepCellsAmount;
        } else {
            this.summaryMinesAndCombinations.setDeepCellsAmount(deepCellsAmount);
            this.summaryMinesAndCombinations.setMinesLeft(wholeMinesLeft);
            this.summaryMinesAndCombinations.calculateSummary();
            double result = this.summaryMinesAndCombinations.getDeepPossibility();
            return result;
        }
    }

    public List<Cell> getMinPosCells() {
        return this.summaryMinesAndCombinations.getMinPosCells();
    }
}
