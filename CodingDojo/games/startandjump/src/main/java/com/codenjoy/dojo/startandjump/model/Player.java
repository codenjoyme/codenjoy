package com.codenjoy.dojo.startandjump.model;

import com.codenjoy.dojo.startandjump.services.Events;
import com.codenjoy.dojo.services.EventListener;
import com.codenjoy.dojo.services.PointImpl;

/**
 * Класс игрока. Тут кроме героя может подсчитываться очки. Тут же ивенты передабтся лиснеру фреймворка.
 */
public class Player {

    private EventListener listener;
    private int maxScore;
    private int score;
    Hero hero;

    /**
     * @param listener Это шпийон от фреймоврка. Ты должен все ивенты которые касаются конкретного пользователя сормить ему.
     */
    public Player(EventListener listener) {
        this.listener = listener;
        clearScore();
    }

    private void increaseScore() {
        score = score + hero.getY();
        maxScore = Math.max(maxScore, score);
    }

    public int getMaxScore() {
        return maxScore;
    }

    public int getScore() {
        return score;
    }

    /**
     * Борда может файрить ивенты юзера с помощью этого метода
     * @param event тип ивента
     */
    public void event(Events event) {
        System.out.println("MAX SCORE = " + maxScore);
        System.out.println("SCORE = " + score);
        switch (event) {
            case LOSE: gameOver(); break;
            case STILL_ALIVE: increaseScore(); break;
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

    public Hero getHero() {
        return hero;
    }

    /**
     * Когда создается новая игра для пользователя, кто-то должен создать героя
     */
    public void newHero() {
        hero = new Hero(PointImpl.pt(0, 3));
    }

}