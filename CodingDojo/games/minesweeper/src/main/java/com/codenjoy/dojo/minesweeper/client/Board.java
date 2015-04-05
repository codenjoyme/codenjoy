package com.codenjoy.dojo.minesweeper.client;

import com.codenjoy.dojo.client.AbstractBoard;
import com.codenjoy.dojo.minesweeper.model.Elements;
import com.codenjoy.dojo.services.Point;

import java.util.Collection;
import java.util.List;

import static com.codenjoy.dojo.services.PointImpl.pt;

public class Board extends AbstractBoard<Elements> {

    @Override
    public Elements valueOf(char ch) {
        return Elements.valueOf(ch);
    }

    public boolean isBarrierAt(int x, int y) {
        return isAt(x, y, Elements.BORDER);
    }

    public Point getMe() {
        return get(Elements.DETECTOR).get(0);
    }

    public boolean isGameOver() {
    return !get(Elements.BANG).isEmpty();
    }
}