package com.codenjoy.dojo.services;

/**
 * Через этот интефейс движок подслушивает за ивентами, которые возникают в игре.
 * Все ивенты приводят к насислению очков, иначе нет смысла что либо генерировать.
 */
public interface EventListener {
    void event(Object event);
}
