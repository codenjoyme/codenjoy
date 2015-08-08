package com.codenjoy.dojo.fifteen.client;

import com.codenjoy.dojo.client.AbstractBoard;
import com.codenjoy.dojo.fifteen.model.Elements;
import com.codenjoy.dojo.services.Point;

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

    //!!!!!!!!!!!
    public boolean isBarrierAt(int x, int y) {
        return isAt(x, y, Elements.WALL);
    }

    //!!!!!!!!!
    public Point getMe() {
        return get(Elements.HERO).get(0);
    }
//
//    public boolean isGameOver() {
//        return false;//!get(Elements.DEAD_HERO).isEmpty();
//    }
//
//    public boolean isBombAt(int x, int y) {
//        return false;//isAt(x, y, Elements.BOMB);
//    }
}