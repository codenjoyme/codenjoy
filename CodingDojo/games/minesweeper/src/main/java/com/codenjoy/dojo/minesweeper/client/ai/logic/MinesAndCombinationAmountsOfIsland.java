package com.codenjoy.dojo.minesweeper.client.ai.logic;

import java.util.ArrayList;
import java.util.List;

public class MinesAndCombinationAmountsOfIsland {

    private List<MxElement> elements = new ArrayList();

    public void addMxCountOfMines(int mines, int[] mxCountOfMines) {
        MxElement element = getMxElementByMines(mines);
        element.addMxCountOfMines(mxCountOfMines);
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

    private MxElement getMxElement(int i) {
        return elements.get(i);
    }

}
