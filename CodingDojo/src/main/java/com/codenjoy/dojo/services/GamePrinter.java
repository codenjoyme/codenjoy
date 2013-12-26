package com.codenjoy.dojo.services;

/**
 * Принтер доски может прежставить любой ее элемент как {@see com.codenjoy.dojo.sample.model.Elements}
 */
public interface GamePrinter {
    Enum get(int x, int y);
}
