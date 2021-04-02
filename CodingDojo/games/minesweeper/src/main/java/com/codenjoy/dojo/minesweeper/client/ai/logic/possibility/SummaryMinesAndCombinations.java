//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.codenjoy.dojo.minesweeper.client.ai.logic.possibility;

import com.codenjoy.dojo.minesweeper.client.ai.logic.Sequence6;
import com.codenjoy.dojo.minesweeper.client.ai.logic.Sequence7;
import com.codenjoy.dojo.minesweeper.client.ai.logic.Cell;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class SummaryMinesAndCombinations {
    private List<MinesAndCombinationAmountsOfIsland> mx0s = new ArrayList();
    private int minesLeft;
    private int deepCellsAmount;
    private Map<Integer, Integer> summary;

    public SummaryMinesAndCombinations() {
    }

    public void setMinesLeft(int minesLeft) {
        this.minesLeft = minesLeft;
    }

    public void setDeepCellsAmount(int deepCellsAmount) {
        this.deepCellsAmount = deepCellsAmount;
    }

    public void add(MinesAndCombinationAmountsOfIsland mx0) {
        this.mx0s.add(mx0);
    }

    public Map<Integer, Integer> calculateSummary() {
        com.codenjoy.dojo.minesweeper.client.ai.logic.Sequence7 sequence7 = new Sequence7(this.getDigits());
        this.summary = new HashMap();

        while(sequence7.hasNext()) {
            int[] current = sequence7.next();
            int wholeMinesAmount = this.getWholeMinesAmount(current);
            int combinationsAmount = this.getCombinationsAmount(current);
            if (this.minesLeft - wholeMinesAmount <= this.deepCellsAmount && this.minesLeft >= wholeMinesAmount) {
                this.addWholeCombAmount(current, combinationsAmount);
                if (this.summary.containsKey(wholeMinesAmount)) {
                    combinationsAmount += (Integer)this.summary.get(wholeMinesAmount);
                }

                this.summary.put(wholeMinesAmount, combinationsAmount);
            }
        }

        return this.summary;
    }

    public void calculateIslandsPossibilities() {
        Map<Integer, Integer> deepSummary = new HashMap();
        Iterator i$ = this.summary.entrySet().iterator();

        while(i$.hasNext()) {
            Entry<Integer, Integer> entry = (Entry)i$.next();
            deepSummary.put(this.minesLeft - (Integer)entry.getKey(), (int) Sequence6.getAmount(this.minesLeft - (Integer)entry.getKey(), this.deepCellsAmount));
        }

        for(int i = 0; i < this.mx0s.size(); ++i) {
            ((MinesAndCombinationAmountsOfIsland)this.mx0s.get(i)).calculatePossibilities();
        }

    }

    private void addWholeCombAmount(int[] current, int combsAmount) {
        for(int i = 0; i < this.mx0s.size(); ++i) {
            ((MinesAndCombinationAmountsOfIsland)this.mx0s.get(i)).addWholeCombAmount(current[i], combsAmount);
        }

    }

    private int getCombinationsAmount(int[] current) {
        int result = 1;

        for(int i = 0; i < this.mx0s.size(); ++i) {
            result *= ((MinesAndCombinationAmountsOfIsland)this.mx0s.get(i)).getCombAmount(current[i]);
        }

        return result;
    }

    private int getWholeMinesAmount(int[] current) {
        int result = 0;

        for(int i = 0; i < this.mx0s.size(); ++i) {
            result += ((MinesAndCombinationAmountsOfIsland)this.mx0s.get(i)).getMinesAmount(current[i]);
        }

        return result;
    }

    private int[] getDigits() {
        int[] result = new int[this.mx0s.size()];

        for(int i = 0; i < result.length; ++i) {
            result[i] = ((MinesAndCombinationAmountsOfIsland)this.mx0s.get(i)).size();
        }

        return result;
    }

    public int getMinesInIslands() {
        Iterator<Entry<Integer, Integer>> iterator = this.summary.entrySet().iterator();

        int result;
        Entry entry;
        for(result = 0; iterator.hasNext(); result += (Integer)entry.getKey() * (Integer)entry.getValue()) {
            entry = (Entry)iterator.next();
        }

        return result;
    }

    public long getWholeAmountOfCombinations() {
        long result = 0L;

        Entry entry;
        for(Iterator i$ = this.summary.entrySet().iterator(); i$.hasNext(); result += (long)(Integer)entry.getValue()) {
            entry = (Entry)i$.next();
        }

        return result;
    }

    public double getDeepPossibility() {
        return 100.0D * ((double)this.minesLeft - 1.0D * (double)((long)this.getMinesInIslands()) / (double)this.getWholeAmountOfCombinations()) / (double)this.deepCellsAmount;
    }

    public List<Cell> getMinPosCells() {
        List<Cell> result = new ArrayList();
        double min = 100.0D;
        Iterator i$ = this.mx0s.iterator();

        MinesAndCombinationAmountsOfIsland mx0;
        while(i$.hasNext()) {
            mx0 = (MinesAndCombinationAmountsOfIsland)i$.next();
            double minMx0 = mx0.getMinPossibility();
            if (minMx0 < min) {
                min = minMx0;
            }
        }

        i$ = this.mx0s.iterator();

        while(i$.hasNext()) {
            mx0 = (MinesAndCombinationAmountsOfIsland)i$.next();
            result.addAll(mx0.getCellsByPossibility(min));
        }

        return result;
    }
}
