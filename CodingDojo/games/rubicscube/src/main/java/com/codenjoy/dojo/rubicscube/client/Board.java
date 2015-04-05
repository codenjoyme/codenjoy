package com.codenjoy.dojo.rubicscube.client;

import com.codenjoy.dojo.client.AbstractBoard;
import com.codenjoy.dojo.rubicscube.model.Elements;

/**
 * User: oleksandr.baglai
 */
public class Board extends AbstractBoard<Elements> {

    @Override
    public Elements valueOf(char ch) {
        return Elements.valueOf(ch);
    }

    @Override
    public String toString() {
        StringBuffer result = new StringBuffer();
        for (int y = 0; y < size - 3; y++) {
            for (int x = 0; x < size; x++) {
                result.append(field[x][y]);
            }
            result.append("\n");
        }
        return result.toString();
    }

}