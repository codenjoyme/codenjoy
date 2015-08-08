package com.codenjoy.dojo.football.client;


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
    public Elements valueOf(char ch) {
        return Elements.valueOf(ch);
    }

    public boolean isBarrierAt(int x, int y) {
        return isAt(x, y, Elements.WALL);
    }

    /*public List<Point> getMe() {
        return get(Elements.HERO, Elements.HERO_W_BALL);
    }*/
    public Point getMe() {
        return get(Elements.HERO, Elements.HERO_W_BALL).get(0);
    }
    
    public Point getBall() {
        return get(Elements.BALL, Elements.STOPPED_BALL, 
        			Elements.HERO_W_BALL, Elements.ENEMY_W_BALL,
        			Elements.HITED_GOAL, Elements.HITED_MY_GOAL ).get(0);
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