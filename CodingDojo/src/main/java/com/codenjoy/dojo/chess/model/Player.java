package com.codenjoy.dojo.chess.model;

import com.codenjoy.dojo.chess.model.figures.Figure;
import com.codenjoy.dojo.chess.services.ChessEvents;
import com.codenjoy.dojo.services.EventListener;
import com.codenjoy.dojo.services.Joystick;

import java.util.LinkedList;
import java.util.List;

public class Player implements Joystick {

    private EventListener listener;
    private int maxScore;
    private int score;
    List<Figure> figures = new LinkedList<Figure>();

    public Player(EventListener listener) {
        this.listener = listener;
        clearScore();
    }

    private void increaseScore() {
        score = score + 1;
        maxScore = Math.max(maxScore, score);
    }

    public int getMaxScore() {
        return maxScore;
    }

    public int getScore() {
        return score;
    }

    public void event(ChessEvents event) {
        switch (event) {
            case WIN: increaseScore(); break;
        }

        if (listener != null) {
            listener.event(event);
        }
    }

    private void gameOver() {
        score = 0;
    }

    public void clearScore() {
        score = 0;
        maxScore = 0;
    }

    public void initFigures(Field field) {
        for (Figure figure : figures) {
            figure.init(field);
        }
    }

    public List<Figure> getFigures() {
        return figures;
    }

    public boolean isAlive() {
        return true; // TODO
    }

    @Override
    public void down() {
        // do nothing
    }

    @Override
    public void up() {
        // do nothing
    }

    @Override
    public void left() {
        // do nothing
    }

    @Override
    public void right() {
        // do nothing
    }

    @Override
    public void act(int... p) {
        // TODO
    }
}