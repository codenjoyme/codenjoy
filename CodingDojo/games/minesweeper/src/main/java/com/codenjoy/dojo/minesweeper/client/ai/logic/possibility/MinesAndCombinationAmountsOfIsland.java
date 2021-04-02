

package com.codenjoy.dojo.minesweeper.client.ai.logic.possibility;

import com.codenjoy.dojo.minesweeper.client.ai.logic.Cell;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class MinesAndCombinationAmountsOfIsland {
    private List<MxElement> elements = new ArrayList();
    private List<Cell> indefiniteCells;

    public MinesAndCombinationAmountsOfIsland() {
    }

    public void calculatePossibilities() {
        int wholeCombAmountSum = this.getWholeCombAmountSum();
        int[] wholeMxCountOfMines = this.getWholeMxCountOfMines();

        for(int i = 0; i < wholeMxCountOfMines.length; ++i) {
            int mxCountOfMinesSum = this.getMxCountOfMinesSum(i);
            (this.indefiniteCells.get(i)).setPossibility(100.0D * (double)wholeMxCountOfMines[i] / (double)mxCountOfMinesSum / (double)wholeCombAmountSum);
        }

    }

    private int[] getWholeMxCountOfMines() {
        int[] result = new int[this.indefiniteCells.size()];
        Iterator i$ = this.elements.iterator();

        while(i$.hasNext()) {
            com.codenjoy.dojo.minesweeper.client.ai.logic.possibility.MxElement element = (com.codenjoy.dojo.minesweeper.client.ai.logic.possibility.MxElement)i$.next();
            int[] wholeMxCountOfMines = element.getWholeMxCountOfMines();

            for(int i = 0; i < wholeMxCountOfMines.length; ++i) {
                result[i] += wholeMxCountOfMines[i];
            }
        }

        return result;
    }

    private int getMxCountOfMinesSum(int index) {
        int result = 0;

        MxElement element;
        for(Iterator i$ = this.elements.iterator(); i$.hasNext(); result += element.getMxCountOfMines()[index]) {
            element = (MxElement)i$.next();
        }

        return result;
    }

    public void addMxCountOfMines(int mines, int[] mxCountOfMines) {
        MxElement element = this.getMxElementByMines(mines);
        element.addMxCountOfMines(mxCountOfMines);
    }

    public void inc(int mines) {
        this.add(mines, 1);
    }

    public void add(int mines, int combinations) {
        MxElement element = this.getMxElementByMines(mines);
        element.addCombAmount(combinations);
    }

    private MxElement getMxElementByMines(int mines) {
        MxElement element = new MxElement(mines);
        int index = this.elements.indexOf(element);
        if (index == -1) {
            this.elements.add(element);
        } else {
            element = this.getMxElement(index);
        }

        return element;
    }

    public int size() {
        return this.elements.size();
    }

    public void addWholeCombAmount(int i, int combs) {
        this.getMxElement(i).addWholeCombAmount(combs);
    }

    public int getCombAmount(int i) {
        return this.getMxElement(i).getCombAmount();
    }

    private int getWholeCombAmount(int i) {
        return this.getMxElement(i).getWholeCombAmount();
    }

    public int getMinesAmount(int i) {
        return this.getMxElement(i).getMinesAmount();
    }

    private MxElement getMxElement(int i) {
        return (MxElement)this.elements.get(i);
    }

    public int getWholeCombAmountSum() {
        int result = 0;

        MxElement element;
        for(Iterator i$ = this.elements.iterator(); i$.hasNext(); result += element.getWholeCombAmount()) {
            element = (MxElement)i$.next();
        }

        return result;
    }

    public void setIndefiniteCells(List<Cell> indefiniteCells) {
        this.indefiniteCells = indefiniteCells;
    }

    public double getMinPossibility() {
        double min = 100.0D;
        Iterator i$ = this.indefiniteCells.iterator();

        while(i$.hasNext()) {
            Cell cell = (Cell)i$.next();
            if (cell.getPossibility() < min) {
                min = cell.getPossibility();
            }
        }

        return min;
    }

    public List<Cell> getCellsByPossibility(double pos) {
        List<Cell> result = new ArrayList();
        Iterator i$ = this.indefiniteCells.iterator();

        while(i$.hasNext()) {
            Cell cell = (Cell)i$.next();
            if (cell.getPossibility() == pos) {
                result.add(cell);
            }
        }

        return result;
    }
}
