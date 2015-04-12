package com.codenjoy.dojo.services;

/**
 * В модельке игры класс отвечающий за подсчет очков, должен реализовать этот интерфейс.
 */
public interface PlayerScores extends EventListener {

    /**
     * @return текущее значение очков, что успел набрать пользователь
     */
    int getScore();

    /**
     * @return очистка очков с возвращением последнего значения.
     */
    int clear();
}
