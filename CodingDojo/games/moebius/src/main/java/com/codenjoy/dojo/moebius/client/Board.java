package com.codenjoy.dojo.moebius.client;

import com.codenjoy.dojo.client.AbstractBoard;
import com.codenjoy.dojo.moebius.model.Elements;

public class Board extends AbstractBoard<Elements> {

    @Override
    public Elements valueOf(char ch) {
        return Elements.valueOf(ch);
    }

    public boolean isGameOver() {
        return get(Elements.EMPTY).isEmpty();
    }

}