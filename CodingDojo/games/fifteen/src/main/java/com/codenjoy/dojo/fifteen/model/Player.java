package com.codenjoy.dojo.fifteen.model;

import com.codenjoy.dojo.fifteen.services.Events;
import com.codenjoy.dojo.services.EventListener;

/**
 * Класс игрока. Тут кроме героя может подсчитываться очки. Тут же ивенты передабтся лиснеру фреймворка.
 */
public class Player {

    private EventListener listener;
    Hero hero;

    /**
     * @param listener Это шпийон от фреймоврка. Ты должен все ивенты которые касаются конкретного пользователя сормить ему.
     */
    public Player(EventListener listener) {
        this.listener = listener;
    }

    /**
     * Борда может файрить ивенты юзера с помощью этого метода
     * @param event тип ивента
     */
    public void event(Events event) {
        if (listener != null) {
            listener.event(event);
        }
    }

    public void event(Bonus bonus) {
        if (listener != null) {
            listener.event(bonus);
        }
    }

    public Hero getHero() {
        return hero;
    }

    public void setHero(Hero hero) {
        this.hero = hero;
        this.hero.setPlayer(this);
    }
}