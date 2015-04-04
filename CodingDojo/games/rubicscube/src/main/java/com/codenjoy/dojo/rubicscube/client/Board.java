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
        for (int i = 0; i <= size - 1 - 3; i++) {
            result.append(board.substring(i * size, (i + 1) * size));
            result.append("\n");
        }
        return result.toString();
    }

}