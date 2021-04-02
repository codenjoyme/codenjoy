package com.codenjoy.dojo.minesweeper.client.ai.logic;

import java.util.ArrayList;
import java.util.List;

public class MinesAndCombinationAmountsOfIsland {

    private List<MxElement> elements = new ArrayList();

    public void add(int mines, int[] count) {
        getBy(mines).add(count);
    }

    private MxElement getBy(int mines) {
        MxElement element = new MxElement(mines);
        int index = elements.indexOf(element);
        if (index == -1) {
            elements.add(element);
        } else {
            element = get(index);
        }

        return element;
    }

    public int size() {
        return elements.size();
    }

    private MxElement get(int i) {
        return elements.get(i);
    }

}
