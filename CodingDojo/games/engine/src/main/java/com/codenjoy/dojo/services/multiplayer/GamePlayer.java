package com.codenjoy.dojo.services.multiplayer;

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


import com.codenjoy.dojo.services.CustomMessage;
import com.codenjoy.dojo.services.EventListener;
import com.codenjoy.dojo.services.Joystick;
import com.codenjoy.dojo.services.hero.HeroData;

/**
 * Игрок внутри игры представлен в виде наследника этого класса.
 * @param <H> Герой на поле, которого представляет игрок.
 * @param <F> Само поле
 */
public abstract class GamePlayer<H extends PlayerHero, F extends GameField> {

    protected EventListener listener;
    private LevelProgress progress;

    /**
     * @param listener Это шпийон от фреймоврка. Ты должен все ивенты
     *                 которые касаются конкретного пользователя скормить ему.
     */
    public GamePlayer(EventListener listener) {
        this.listener = listener;
    }

    /**
     * Борда может файрить ивенты юзера с помощью этого метода
     * @param event тип ивента
     */
    public void event(Object event) {
        if (progress != null && progress.getCurrent() <= progress.getPassed()) {
            return; // TODO test me
        }

        if (listener != null) {
            listener.event(event);
        }
    }

    /**
     * @param message Сообщение, которое будет напечатано на борде игрока в этом тике
     */
    public void printMessage(String message) {
        event(new CustomMessage(message));
    }

    /**
     * @return Герой игрока, готорым можно управлять через {@see Joystick}
     */
    public abstract H getHero();

    /**
     * Ты можешь переопределить этот метод, и тогда у этих данных будет приоритет.
     * Иначе {@see Joystick} будет построен на основе объекта {@see #getHero()}
     * @return Джойстик для управления героем
     */
    public Joystick getJoystick() {
        return null;
    }

    /**
     * Ты можешь переопределить этот метод, и тогда у этих данных будет приоритет.
     * Иначе {@see HeroData} будет построен на основе объекта {@see #getHero()}
     * @return данные для отрисовки дополнительной информации на UI
     */
    public HeroData getHeroData() {
        return null;
    }

    /**
     * Когда создается новая игра для пользователя, кто-то должен создать героя
     * @param field борда
     */
    public abstract void newHero(F field);

    /**
     * @return Жив ли герой. Обычно делегируется герою.
     */
    public abstract boolean isAlive();

    /**
     * @return Победил ли герой на этом уровне. TODO ##2 работает пока только с multiplayerType.isTraining()
     */
    public boolean isWin() {
        return false;
    }

    /**
     * @return Проиграл ли герой этот матч и должен ли покинуть борду.
     *          Работает только с multiplayerType.isDisposable()
     */
    public boolean shouldLeave() {
        return false;
    }

    /**
     * @return В случае если герой остался один на карте и должен покинуть ее
     *          этим флагом он может сообщить о своем нежелании.
     *          // TODO подумать хорошенько, а то тут флагов уже как тараканов
     */
    public boolean wantToStay() {
        return false;
    }

    /**
     * Никогда не переопределяй этот метод
     */
    @Override
    public boolean equals(Object o) {
        return this == o;
    }

    /**
     * Никогда не переопределяй этот метод
     */
    @Override
    public int hashCode() {
        return super.hashCode();
    }

    public void setProgress(LevelProgress progress) {
        this.progress = progress;
    }
}
