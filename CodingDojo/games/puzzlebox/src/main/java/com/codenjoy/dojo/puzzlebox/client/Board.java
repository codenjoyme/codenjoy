package com.codenjoy.dojo.puzzlebox.client;

import com.codenjoy.dojo.client.AbstractBoard;
import com.codenjoy.dojo.puzzlebox.model.Elements;
import com.codenjoy.dojo.services.Point;

import java.util.List;

/**
 * Класс, обрабатывающий строковое представление доски.
 * Содержит ряд унаследованных методов {@see AbstractBoard},
 * но ты можешь добавить сюда любые свои методы на их основе.
 */
public class Board extends AbstractBoard<Elements> {

    @Override
    public Elements valueOf(char ch) {
        return Elements.valueOf(ch);
    }

    // TODO
    public boolean isBarrierAt(int x, int y) {
        return isAt(x, y, Elements.WALL);
    }

    public Point getMe() {
        return get(Elements.BOX).get(0);
    }

    public List<Point> getAllMyBoxes() {
        return get(Elements.BOX);
    }
//
//    public boolean isGameOver() {
//        return !get(Elements.DEAD_HERO).isEmpty();
//    }
//
//    public boolean isBombAt(int x, int y) {
//        return isAt(x, y, Elements.BOMB);
//    }
}