package com.codenjoy.dojo.services;

/**
 * Created by indigo on 18.03.2015.
 */
public class PrinterFactoryImpl implements PrinterFactory {
    @Override
    public <E extends CharElements, P> Printer getPrinter(BoardReader reader, P player) {
        return PrinterImpl.getPrinter(reader, player);
    }
}
