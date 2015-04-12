package com.codenjoy.dojo.services;

/**
 * Фектори для PrinterImpl
 */
public class PrinterFactoryImpl implements PrinterFactory {
    @Override
    public <E extends CharElements, P> Printer getPrinter(BoardReader reader, P player) {
        return PrinterImpl.getPrinter(reader, player);
    }
}
