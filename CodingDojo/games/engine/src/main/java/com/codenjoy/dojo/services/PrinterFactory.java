package com.codenjoy.dojo.services;

public interface PrinterFactory {
    <E extends CharElements, P> Printer getPrinter(BoardReader reader, P player);
}
