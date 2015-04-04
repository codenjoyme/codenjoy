package com.codenjoy.dojo.minesweeper.client.ai.vaa25.logic.possibility;


import com.codenjoy.dojo.minesweeper.client.ai.vaa25.logic.Cell;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Alexander Vlasov
 */
public class MinesAndCombinationAmountsOfIsland {
    private List<MxElement> elements;
    private List<Cell> indefiniteCells;

    public MinesAndCombinationAmountsOfIsland() {
        elements = new ArrayList<MxElement>();
    }

    public void calculatePossibilities() {
        int wholeCombAmountSum = getWholeCombAmountSum();
        int[] wholeMxCountOfMines = getWholeMxCountOfMines();
        for (int i = 0; i < wholeMxCountOfMines.length; i++) {
            int mxCountOfMinesSum = getMxCountOfMinesSum(i);
            indefiniteCells.get(i).setPossibility(
                    100.0 * wholeMxCountOfMines[i] / mxCountOfMinesSum / wholeCombAmountSum);
            //todo неправильно считает вероятность. Нужно это выражение умножить на wholeCombAmount
            //и поделить на
        }
    }

    private int[] getWholeMxCountOfMines() {
        int[] result = new int[indefiniteCells.size()];
        for (MxElement element : elements) {
            int[] wholeMxCountOfMines = element.getWholeMxCountOfMines();
            for (int i = 0; i < wholeMxCountOfMines.length; i++) {
                result[i] += wholeMxCountOfMines[i];
            }
        }
        return result;
    }

    private int getMxCountOfMinesSum(int index) {
        int result = 0;
        for (MxElement element : elements) {
            result += element.getMxCountOfMines()[index];
        }
        return result;
    }

    public void addMxCountOfMines(int mines, int[] mxCountOfMines) {
        MxElement element = getMxElementByMines(mines);
        element.addMxCountOfMines(mxCountOfMines);
    }

    public void inc(int mines) {
        add(mines, 1);
    }

    public void add(int mines, int combinations) {

        MxElement element = getMxElementByMines(mines);
        element.addCombAmount(combinations);
    }

    private MxElement getMxElementByMines(int mines) {
        MxElement element = new MxElement(mines);
        int index = elements.indexOf(element);
        if (index == -1) {
            elements.add(element);
        } else {
            element = getMxElement(index);
        }
        return element;
    }

    public int size() {
        return elements.size();
    }

    public void addWholeCombAmount(int i, int combs) {
        getMxElement(i).addWholeCombAmount(combs);
    }

    public int getCombAmount(int i) {
        return getMxElement(i).getCombAmount();
    }

    private int getWholeCombAmount(int i) {
        return getMxElement(i).getWholeCombAmount();
    }

    public int getMinesAmount(int i) {
        return getMxElement(i).getMinesAmount();
    }

    private MxElement getMxElement(int i) {
        return elements.get(i);
    }

    public int getWholeCombAmountSum() {
        int result = 0;
        for (MxElement element : elements) {
            result += element.getWholeCombAmount();
        }
        return result;
    }

    public void setIndefiniteCells(List<Cell> indefiniteCells) {
        this.indefiniteCells = indefiniteCells;
    }

    public double getMinPossibility() {
        double min = 100;
        for (Cell cell : indefiniteCells) {
            if (cell.getPossibility() < min) {
                min = cell.getPossibility();
            }
        }
        return min;
    }

    public List<Cell> getCellsByPossibility(double pos) {
        List<Cell> result = new ArrayList<Cell>();
        for (Cell cell : indefiniteCells) {
            if (cell.getPossibility() == pos) {
                result.add(cell);
            }
        }
        return result;
    }
}
