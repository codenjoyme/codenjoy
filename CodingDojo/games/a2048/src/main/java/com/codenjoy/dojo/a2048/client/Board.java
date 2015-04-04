package com.codenjoy.dojo.a2048.client;

import com.codenjoy.dojo.a2048.model.Elements;
import com.codenjoy.dojo.client.AbstractBoard;
import com.codenjoy.dojo.client.Direction;

public class Board extends AbstractBoard<Elements> {

    @Override
    public Elements valueOf(char ch) {
        return Elements.valueOf(ch);
    }

    public int getSumCountFor(Direction direction) {
        int result = 0;

        for (int y = 0; y < size; y++) {
            int fromX = 0;
            int toX = 0;
            while (fromX < size && toX < size - 1) {
                toX++;

                Elements at = getAt(fromX, y);
                Elements at2 = getAt(toX, y);
                if (at == Elements.NONE) {
                    fromX++;
                    continue;
                }
                if (at2 == Elements.NONE) {
                    continue;
                }

                if (at != Elements.NONE && at == at2) {
                    result++;
                    fromX = toX + 1;
                    toX = fromX;
                }
            }
        }
        return result;
    }

}