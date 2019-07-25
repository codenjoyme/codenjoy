package com.codenjoy.dojo.crossyroad.client;

/*-
 * #%L
 * Codenjoy - it's a dojo-like platform from developers to developers.
 * %%
 * Copyright (C) 2018 Codenjoy
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/gpl-3.0.html>.
 * #L%
 */


import com.codenjoy.dojo.client.AbstractBoard;
import com.codenjoy.dojo.crossyroad.model.Elements;
import com.codenjoy.dojo.services.Direction;
import com.codenjoy.dojo.services.Point;

import java.util.LinkedList;
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

    /**
    // TODO закончить
//    public boolean isBarrierAt(int x, int y) {
//        return isAt(x, y, Elements.WALL);
//    }
     */


    // возвращает элементы стены(wall) + камни(stone) + платформы(platform) (они должны находиться внизу этого класса)
    public List<Point> getBarriers() {
        List<Point> result = getCarLeftToRight();
        result.addAll(getCarRightToLeft());
        result.addAll(getStones());
        result.addAll(getWalls());
        return result;
    }

// геты для получения списков каждого типа элементов на доске
    public Point getMe() { return get(Elements.BLACK_HERO, Elements.HERO).get(0); } //герой
    public List<Point> getWalls(){ return get(Elements.WALL); }                     //стены
    public List<Point> getCarLeftToRight(){ return get(Elements.CARLEFTTORIGHT); }       //машина едет слева направо
    public List<Point> getCarRightToLeft(){ return get(Elements.CARRIGHTTOLEFT); }       //машина едет слева направо
    public List<Point> getStones(){ return get(Elements.STONE);}                    //камни










    /*
    public boolean isGameOver() {
        return !get(Elements.DEAD_HERO).isEmpty();
    }

    public boolean isBombAt(int x, int y) {
       return isAt(x, y, Elements.BOMB);
    }
    */

    /*
    //так как перс может иметь только три направления(лево,право,вверх), но вверх он не ходит, а падают машины
    public Direction trueRandom(){
        Direction dir = Direction.random();
        while(!(dir==Direction.UP)||!(dir==Direction.LEFT)||!(dir==Direction.RIGHT)){
            dir = Direction.random();
        }
        return dir;
    }
     */
}
