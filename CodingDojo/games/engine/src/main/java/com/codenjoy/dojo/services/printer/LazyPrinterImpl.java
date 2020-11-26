package com.codenjoy.dojo.services.printer;

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


import com.codenjoy.dojo.services.multiplayer.GamePlayer;

import java.util.function.Supplier;

/**
 * Этот малый умеет печатать состояние борды на экране,
 * но инициализируется в момент первого вызова
 * @see PrinterImpl
 * @see LazyPrinterImpl#print(Object...)
  */
public class LazyPrinterImpl<E extends CharElements, P extends GamePlayer> implements Printer<String> {

    private Supplier<BoardReader> reader;
    private Supplier<P> player;
    private Printer<String> printer;

    public static Printer getPrinter(Supplier<BoardReader> reader, Supplier player) {
        return new LazyPrinterImpl(reader, player);
    }

    public LazyPrinterImpl(Supplier<BoardReader> reader, Supplier<P> player) {
        this.reader = reader;
        this.player = player;
    }

    @Override
    public String print(Object... parameters) {
        if (printer == null) {
            printer = PrinterImpl.getPrinter(reader.get(), player.get());
        }

        return printer.print(parameters);
    }
}
