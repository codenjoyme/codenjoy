package com.codenjoy.dojo.services;

/*-
 * #%L
 * Codenjoy - it's a dojo-like platform from developers to developers.
 * %%
 * Copyright (C) 2016 Codenjoy
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


/**
 * Каждый инстанс игры для каждого игрока реализует этот интерфейс
 */
public interface Game extends Tickable {

    /**
     * @return Джойстик для управления ботом игрока
     */
    Joystick getJoystick();

    /**
     * @return Максимально количество полезных действий, которое удалось совершить игроком между двух смертей
     */
    int getMaxScore();

    /**
     * @return Текущее количество полезных действий, которое удалось совершить игроком между двух смертей
     */
    int getCurrentScore();

    /**
     * @return true - если герой убит
     */
    boolean isGameOver();

    /**
     * Если герой убит, то в слудеющий такт фреймворк дернет за этот метод, чтобы создать новую игру для игрока.
     * То же происходит же при регистрации нового игрока.
     */
    void newGame();

    /**
     * Board =
     * "******" +
     * "*    *" +
     * "* ☺  *" +
     * "*    *" +
     * "*    *" +
     * "******";
     * @return строковое представление квадратной доски
     */
    String getBoardAsString();

    /**
     * Если вдруг пользователь передумает играть и уйдет, от при выходе из игры фреймворк дернет этот метод.
     * Мало ли, вдруг ты хранишь всех игроков у себя (актуально для игры типа много игроков на одном поле).
     */
    void destroy();

    /**
     * Иногда ведущий игры сбрасывает очки участников. Тогда фреймворк дернет этот метод.
     */
    void clearScore();

    /**
     * @return Координаты игрока на поле из рассчета, что [0, 0] находится в левом нижнем углу
     */
    Point getHero();

    /**
     * @return Если игра сохраняется, то у нее должно быть состояние, иначе null
     */
    String getSave();
}
