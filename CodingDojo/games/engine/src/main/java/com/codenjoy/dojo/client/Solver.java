package com.codenjoy.dojo.client;

/**
 * Любая реализация AI должна реализовать этот интерфейс.
 * @param <B> реализация {@see AbstractBoard} для текущей игры
 */
public interface Solver<B extends AbstractBoard> {

    /**
     * Каждую секунду сервер будет приганять сюда актуальное состояние доски.
     * @param board объект инкапсулирующий доску
     * @return команда, что делать серверу
     */
    String get(B board);
}
