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


import com.codenjoy.dojo.client.ClientBoard;
import com.codenjoy.dojo.client.Solver;
import com.codenjoy.dojo.services.multiplayer.GameField;
import com.codenjoy.dojo.services.multiplayer.GamePlayer;
import com.codenjoy.dojo.services.multiplayer.MultiplayerType;
import com.codenjoy.dojo.services.printer.CharElements;
import com.codenjoy.dojo.services.printer.PrinterFactory;
import com.codenjoy.dojo.services.settings.Parameter;
import com.codenjoy.dojo.services.settings.Settings;

/**
 * Это интерфейс указывает на тип игры. Как только ты его реулизуешь -
 * на админке (http://localhost:8080/codenjoy-contest/admin)
 * будет возможность переключиться на твою игру.
 */
public interface GameType extends Tickable {

    /**
     * @param score значения очков перед началом игры (используется например при загрузке игры из save)
     * @return Возвращается объект который умеет в зависимости от типа события на карте подчитывать очки игроков
     */
    PlayerScores getPlayerScores(Object score);

    /**
     * Так фреймворк будет стартовать новую игру для каждого пользователя
     * @level уровень игры (опциональное поле, обычно начинается с 1
     *          {@see LevelProgress#levelsStartsFrom1})
     * @return Экземпляр игры пользователя
     */
    GameField createGame(int levelNumber);

    /**
     * @return Размер доски. Важно, чтобы у всех пользователей были одинаковые по размеру поля
     */
    Parameter<Integer> getBoardSize();

    /**
     * @return Имя твоей игры
     */
    String name();

    /**
     * @return Список элементов отображаеммых на доске
     * Смотри класс com.codenjoy.dojo.sample.model.Elements конкретной игры
     */
    CharElements[] getPlots();

    /**
     * @return Настройки игры
     * @see Settings
     */
    Settings getSettings();

    /**
     * @return каждая игра должна предоставить своего AI который будет развлекать новопришедших игроков
     */
    Class<? extends Solver> getAI();

    /**
     * @return А это борда клиентская для игры
     */
    Class<? extends ClientBoard> getBoard();

    /**
     * Если подложить в 'src\main\resources\gameName\version.properties' игры строчку '${project.version}_${build.time}'
     * то ее потом мождно будет прочитать с помощью VersionReader.getCurrentVersion();
     * @return версия игры
     */
    String getVersion();

    /**
     * @return Возвращает тип мультиплеера для этой игы
     */
    MultiplayerType getMultiplayerType();

    /**
     * Метод для создания игрового пользователя внутри игры
     * @param listener Через этот интерфейс фреймворк будет слушать какие ивенты возникают в твоей игре
     * @param playerId Имейл игровка зарегавшегося на сервере
     * @return Игрок
     */
    GamePlayer createPlayer(EventListener listener, String playerId);

    /**
     * @return нормальный Random, но ты можешь переопределить его, например, для тестовых целей
     */
    Dice getDice();

    /**
     * @return Вовзращает фабрику принтеров, которая создаст
     * в нужный момент принтер {@see Printer}, который возьмет
     * на себя представление борды в виде строчки.
     * Если хочешь использовать кастомный принтер - {@see PrinterFactory#get(GraphicPrinter)}
     */
    PrinterFactory getPrinterFactory();
}



















