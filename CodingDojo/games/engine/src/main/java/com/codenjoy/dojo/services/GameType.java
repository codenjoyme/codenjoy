package com.codenjoy.dojo.services;

import com.codenjoy.dojo.services.settings.Parameter;
import com.codenjoy.dojo.services.settings.Settings;

/**
 * Это интерфейс указывает на тим игры. Как только ты его реулизуешь -
 * на админке (http://localhost:8080/codenjoy-contest/admin31415)
 * будет возможность переключиться на твою игру.
 */
public interface GameType {

    /**
     * @param score значения очков перед началом игры (используется например при загрузке игры из save)
     * @return Возвращается объект который умеет в зависимости от типа события на карте подчитывать очки игроков
     */
    PlayerScores getPlayerScores(int score);

    /**
     * Так фреймворк будет стартовать новую игру для каждого пользователя
     * @param listener Через этот интерфейс фреймворк будет слушать какие ивенты возникают в твоей игре
     * @param factory Через этот интерфейс фреймворк будет инджектить принтер для отрисовки поля
     * @return Экземпляр игры пользователя
     */
    Game newGame(EventListener listener, PrinterFactory factory);

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
    Enum[] getPlots();

    /**
     * @return Настройки игры
     * @see Settings
     */
    Settings getSettings();

    /**
     * Существует два режима игры. Для начала реализуй - каждый на своей отдельной доске.
     * Позже можешь пробовать мультиплеерную игру создать.
     * Смотри com.codenjoy.dojo.sample.model.Single
     * Смотри com.codenjoy.dojo.sample.model.Sample
     * @return false - если каждый будет играть на своей отдельной доске, true - если все на одной доске
     */
    boolean isSingleBoard();

    /**
     * Каждая игра должна предоставить своего AI который будет развлекать новопришедших игроков.
     * @param aiName имя бота
     */
    void newAI(String aiName);
}



















