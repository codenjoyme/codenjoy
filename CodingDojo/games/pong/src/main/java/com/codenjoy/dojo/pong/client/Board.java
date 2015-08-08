package com.codenjoy.dojo.pong.client;

import com.codenjoy.dojo.client.AbstractBoard;
import com.codenjoy.dojo.pong.model.Elements;
import com.codenjoy.dojo.services.Point;

import java.util.List;

public class Board extends AbstractBoard<Elements> {

    @Override
    public Elements valueOf(char ch) {
        return Elements.valueOf(ch);
    }

    public List<Point> getMe() {
        return get(Elements.HERO);
    }

    public Point getBall() {
        List<Point> balls = get(Elements.BALL);
        if (!balls.isEmpty()) {
            return balls.get(0);
        } else {
            return null;
        }

    }

}