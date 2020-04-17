package com.codenjoy.dojo.services;

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


import com.codenjoy.dojo.client.Closeable;
import com.codenjoy.dojo.services.hero.HeroData;
import com.codenjoy.dojo.services.multiplayer.GameField;
import com.codenjoy.dojo.services.multiplayer.GamePlayer;
import com.codenjoy.dojo.services.multiplayer.LevelProgress;
import org.json.JSONObject;

public interface Game extends Closeable, Progressive {

    /**
     * @return Джойстик для управления ботом игрока
     */
    Joystick getJoystick();

    /**
     * @return true - если герой убит
     */
    boolean isGameOver();

    /**
     * @return true - если герой прошел уровень. TODO ##2 работает пока только с multiplayerType.isTraining()
     */
    boolean isWin();

    /**
     * @return true - если герой должен покинуть эту комнату (проиграл матч)
     *          Работает только с multiplayerType.isDisposable()
     */
    boolean shouldLeave();

    /**
     * Если герой убит, то в слудеющий такт фреймворк дернет за этот метод, чтобы создать новую игру для игрока.
     * То же происходит же при регистрации нового игрока.
     */
    void newGame();

    /**
     * Если игра имеет состояние и может быть сохранена,
     * то она может и загрузиться из этого состояния с помощью этого метода.
     * Этот метод вызывается сразу после созлания игры методом {@see #newGame()}
     * @param save Загружаемый save
     */
    void loadSave(JSONObject save);

    /**
     * Board =
     * "******" +
     * "*    *" +
     * "* ☺  *" +
     * "*    *" +
     * "*    *" +
     * "******";
     * @return строковое (или JSONObject) представление квадратной доски
     */
    Object getBoardAsString();

    /**
     * Если вдруг пользователь передумает играть и уйдет, от при выходе из игры фреймворк дернет этот метод.
     * Мало ли, вдруг ты хранишь всех игроков у себя (актуально для игры типа много игроков на одном поле).
     */
    void close();

    /**
     * Иногда ведущий игры сбрасывает очки участников. Тогда фреймворк дернет этот метод.
     */
    void clearScore();

    /**
     * @return Информация о игроке, которая может быть полезна клиенту
     */
    HeroData getHero();

    /**
     * @return Если игра сохраняется, то у нее должно быть состояние, иначе null
     *
     */
    JSONObject getSave();

    /**
     * @return Возвращает игрока играющего в эту игру
     */
    GamePlayer getPlayer();

    /**
     * @return Возвращает борду игры
     */
    GameField getField();

    /**
     * @param field указывает, что плеер хочет играть в эту игру
     */
    void on(GameField field);

}
