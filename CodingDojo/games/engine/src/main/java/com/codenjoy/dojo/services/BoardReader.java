package com.codenjoy.dojo.services;

/**
 * Этот абстракция над доской для Printer
 */
public interface BoardReader {

    int size();

    Iterable<? extends Point> elements();
}
