package com.codenjoy.dojo.tetris.client;

import com.codenjoy.dojo.client.AbstractBoard;
import com.codenjoy.dojo.services.Point;
import com.codenjoy.dojo.tetris.model.Elements;

import java.util.List;

public class GlassBoard extends AbstractBoard<Elements> {

    @Override
    public Elements valueOf(char ch) {
        return Elements.valueOf(ch);
    }

    @Override
    protected int inversionY(int y) {
        return size() - 1 - y;
    }

    public boolean isFree(int x, int y) {
        return isAt(x, y, Elements.NONE);
    }

    public List<Point> getFigures() {
        return get(
                Elements.BLUE,
                Elements.CYAN,
                Elements.ORANGE,
                Elements.YELLOW,
                Elements.GREEN,
                Elements.PURPLE,
                Elements.RED
        );
    }

    public List<Point> getFreeSpace() {
        return get(Elements.NONE);
    }
}
