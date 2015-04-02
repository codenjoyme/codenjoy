package com.codenjoy.dojo.sudoku.model;

import com.codenjoy.dojo.services.EventListener;
import com.codenjoy.dojo.services.Joystick;
import com.codenjoy.dojo.sudoku.services.SudokuEvents;

public class Player {

    private EventListener listener;
    private int maxScore;
    private int score;
    private Joystick joystick;

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

    public void event(SudokuEvents event) {
        switch (event) {
            case FAIL: gameOver(); break;
            case SUCCESS: increaseScore(); break;
            case WIN: gameOver(); break;
            case LOOSE: gameOver(); break;
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

    public Joystick getJoystick() {
        return joystick;
    }

    public void newHero(Field field) {
        joystick = field.getJoystick();
    }
}