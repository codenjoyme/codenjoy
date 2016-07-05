package com.epam.dojo.icancode.client;

import com.codenjoy.dojo.client.AbstractBoard;
import com.epam.dojo.icancode.model.Elements;

public class Board extends AbstractBoard<Elements> {

    @Override
    public Elements valueOf(char ch) {
        return Elements.valueOf(ch);
    }

   }