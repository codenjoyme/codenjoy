package com.codenjoy.dojo.minesweeper.client.ai.vaa25.logic.possibility;


import com.codenjoy.dojo.minesweeper.client.ai.vaa25.logic.Cell;
import com.codenjoy.dojo.minesweeper.client.ai.vaa25.logic.Sequence6;
import com.codenjoy.dojo.minesweeper.client.ai.vaa25.logic.Sequence7;

import java.util.*;

/**
 * @author Alexander Vlasov
 */
public class SummaryMinesAndCombinations {
    private List<MinesAndCombinationAmountsOfIsland> mx0s = new ArrayList<MinesAndCombinationAmountsOfIsland>();
    private int minesLeft, deepCellsAmount;
    private Map<Integer, Integer> summary;

    public void setMinesLeft(int minesLeft) {
        this.minesLeft = minesLeft;
    }

    public void setDeepCellsAmount(int deepCellsAmount) {
        this.deepCellsAmount = deepCellsAmount;
    }

    public void add(MinesAndCombinationAmountsOfIsland mx0) {
        mx0s.add(mx0);
    }

    public Map<Integer, Integer> calculateSummary() {
        Sequence7 sequence7 = new Sequence7(getDigits());
        summary = new HashMap<Integer, Integer>();
        while (sequence7.hasNext()) {
            int[] current = sequence7.next();
            int wholeMinesAmount = getWholeMinesAmount(current);
            int combinationsAmount = getCombinationsAmount(current);
            if ((minesLeft - wholeMinesAmount <= deepCellsAmount) && (minesLeft >= wholeMinesAmount)) {
                addWholeCombAmount(current, combinationsAmount);
                if (summary.containsKey(wholeMinesAmount)) {
                    combinationsAmount += summary.get(wholeMinesAmount);
                }
                summary.put(wholeMinesAmount, combinationsAmount);
            }
        }
        return summary;
    }

    public void calculateIslandsPossibilities() {
        Map<Integer, Integer> deepSummary = new HashMap<Integer, Integer>();
        for (Map.Entry<Integer, Integer> entry : summary.entrySet()) {
            deepSummary.put(minesLeft - entry.getKey(), (int) Sequence6.getAmount(minesLeft - entry.getKey(), deepCellsAmount));
        }

        for (int i = 0; i < mx0s.size(); i++) {
            mx0s.get(i).calculatePossibilities();
        }
    }

    private void addWholeCombAmount(int[] current, int combsAmount) {
        for (int i = 0; i < mx0s.size(); i++) {
            mx0s.get(i).addWholeCombAmount(current[i], combsAmount);
        }
    }

    private int getCombinationsAmount(int[] current) {
        int result = 1;
        for (int i = 0; i < mx0s.size(); i++) {
            result *= mx0s.get(i).getCombAmount(current[i]);
        }
        return result;
    }

    private int getWholeMinesAmount(int[] current) {
        int result = 0;
        for (int i = 0; i < mx0s.size(); i++) {
            result += mx0s.get(i).getMinesAmount(current[i]);
        }
        return result;
    }

    private int[] getDigits() {
        int[] result = new int[mx0s.size()];
        for (int i = 0; i < result.length; i++) {
            result[i] = mx0s.get(i).size();
        }
        return result;
    }

    public int getMinesInIslands() {
        Iterator<Map.Entry<Integer, Integer>> iterator = summary.entrySet().iterator();
        int result = 0;
        while (iterator.hasNext()) {
            Map.Entry<Integer, Integer> entry = iterator.next();
            result += entry.getKey() * entry.getValue();
        }
        return result;
    }

    public long getWholeAmountOfCombinations() {
        long result = 0;
        for (Map.Entry<Integer, Integer> entry : summary.entrySet()) {
            result += entry.getValue();
        }
        return result;
    }

    public double getDeepPossibility() {
        return 100.0 * (minesLeft - 1.0 * (long) getMinesInIslands() / getWholeAmountOfCombinations()) / deepCellsAmount;
    }

    public List<Cell> getMinPosCells() {
        List<Cell> result = new ArrayList<Cell>();
        double min = 100;
        for (MinesAndCombinationAmountsOfIsland mx0 : mx0s) {
            double minMx0 = mx0.getMinPossibility();
            if (minMx0 < min) {
                min = minMx0;
            }
        }
        for (MinesAndCombinationAmountsOfIsland mx0 : mx0s) {
            result.addAll(mx0.getCellsByPossibility(min));
        }
        return result;
    }
}
