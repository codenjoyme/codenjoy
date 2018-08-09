package com.codenjoy.dojo.football.client;

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
import com.codenjoy.dojo.football.model.Elements;
import com.codenjoy.dojo.services.Point;

/**
 * Класс, обрабатывающий строковое представление доски.
 * Содержит ряд унаследованных методов {@see AbstractBoard},
 * но ты можешь добавить сюда любые свои методы на их основе.
 */
public class Board extends AbstractBoard<Elements> {

    @Override
    protected int inversionY(int y) { // TODO исправить это
        return size() - 1 - y;
    }

    @Override
    public Elements valueOf(char ch) {
        return Elements.valueOf(ch);
    }

    public boolean isBarrierAt(int x, int y) {
        return isAt(x, y, Elements.WALL);
    }

    public Point getMe() {
        return get(Elements.HERO, Elements.HERO_W_BALL).get(0);
    }
    
    public Point getBall() {
        return get(Elements.BALL, Elements.STOPPED_BALL, 
                    Elements.HERO_W_BALL, Elements.ENEMY_W_BALL,
                    Elements.HITED_GOAL, Elements.HITED_MY_GOAL).get(0);
    }
    
    public Point getMyGoal() {
        return get(Elements.MY_GOAL).get(0);
    }
    
    public Point getEnemyGoal() {
        return get(Elements.ENEMY_GOAL).get(0);
    }
    
    public boolean isBallOnMyTeam() {
        return get(Elements.TEAM_MEMBER_W_BALL).size() > 0;
    }
    
    public boolean isGameOver() {
        return !get(Elements.HITED_GOAL, Elements.HITED_MY_GOAL).isEmpty();
    }


}
