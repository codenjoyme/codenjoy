package com.epam.dojo.icancode.model;

import com.codenjoy.dojo.services.EventListener;
import com.epam.dojo.icancode.model.interfaces.IField;
import com.epam.dojo.icancode.model.items.Hero;
import com.epam.dojo.icancode.services.Events;

/**
 * Класс игрока. Тут кроме героя может подсчитываться очки. Тут же ивенты передабтся лиснеру фреймворка.
 */
public class Player {

    private EventListener listener;
    private int maxScore;
    private int score;
    Hero hero;
    private boolean nextLevel;

    /**
     * @param listener Это шпийон от фреймоврка. Ты должен все ивенты которые касаются конкретного пользователя сормить ему.
     */
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

    /**
     * Борда может файрить ивенты юзера с помощью этого метода
     * @param event тип ивента
     */
    public void event(Events event) {
        switch (event.getType()) {
            case LOOSE: gameOver(); break;
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

    public Hero getHero() {
        return hero;
    }

    /**
     * Когда создается новая игра для пользователя, кто-то должен создать героя
     * @param field борда
     */
    public void newHero(IField field) {
        if (hero == null) {
            hero = new Hero(Elements.ROBO);
        }
        clearNextLevel();
        hero.setField(field);
    }

    public boolean isNextLevel() {
        return nextLevel;
    }

    public void clearNextLevel() {
        nextLevel = false;
    }

    public void setNextLevel() {
        nextLevel = true;
    }
}