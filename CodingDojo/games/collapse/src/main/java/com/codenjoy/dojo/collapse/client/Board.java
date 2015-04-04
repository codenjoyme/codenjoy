package com.codenjoy.dojo.collapse.client;

import com.codenjoy.dojo.client.AbstractBoard;
import com.codenjoy.dojo.collapse.model.Elements;

public class Board extends AbstractBoard<Elements> {

    @Override
    public Elements valueOf(char ch) {
        return Elements.valueOf(ch);
    }

}