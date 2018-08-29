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


import com.codenjoy.dojo.services.GamePrinter;
import com.codenjoy.dojo.services.Point;
import com.codenjoy.dojo.services.PointImpl;
import com.codenjoy.dojo.services.State;

import static com.codenjoy.dojo.services.PointImpl.pt;

/**
 * Этот малый умеет печатать состояние борды на экране.
 * @see PrinterImpl#print(Object...)
  */
class PrinterImpl implements Printer<String> {
    public static final String ERROR_SYMBOL = "Ъ";
    private char[][] field;
    private final int size;
    private GamePrinter printer;

    public static <E extends CharElements, P> Printer getPrinter(BoardReader reader, P player) {
        return new PrinterImpl(reader.size(),
                new GamePrinterImpl<E, P>(reader, player));
    }

    public PrinterImpl(int size, GamePrinter printer) {
        this.printer = printer;
        this.size = size;
    }

    /**
     * @return Строковое представление борды будет отправлено фреймворку
     * и на его основе будет отрисована игра на клиенте.
     */
    @Override
    public String print(Object... parameters) {
        fillField();

        StringBuilder string = new StringBuilder();
        for (char[] currentRow : field) {
            for (char ch : currentRow) {
                string.append(ch);
            }
            string.append("\n");
        }

        String result = string.toString();
        if (result.contains(ERROR_SYMBOL)) {
            throw new IllegalArgumentException("Обрати внимание на поле - в месте 'Ъ' появился " +
                    "null Element. И как только он туда попал?\n" + result);
        }

        return result;
    }

    private void fillField() {
        field = new char[size][size];
        printer.init();

        printer.printAll((x, y, ch) -> PrinterImpl.this.set(x, y, ch));
    }

    private void set(int x, int y, char ch) {
        if (x == -1 || y == -1) { // TODO убрать это
            return;
        }

        field[size - 1 - y][x] = ch;
    }

    static class GamePrinterImpl<E extends CharElements, P> implements GamePrinter {

        private final BoardReader board;
        private int size;
        private P player;
        private char emptyChar;

        private Object[][] field;
        private byte[][] len;

        public GamePrinterImpl(BoardReader board, P player) {
            this.board = board;
            this.player = player;
            this.emptyChar = ' ';
        }

        @Override
        public void init() {
            size = board.size();
            field = new Object[size][size];
            len = new byte[size][size];

            addAll(board.elements());
        }

        private void addAll(Iterable<? extends Point> elements) {
            for (Point el : elements) {
                int x = el.getX();
                int y = el.getY();

                if (pt(x, y).isOutOf(field.length)) {
                    continue; // TODO test me (пропускаем элементы за пределами борды)
                }
                Object[] existing = (Object[]) field[x][y];
                if (existing == null) {
                    existing = new Object[7];
                    field[x][y] = existing;
                }
                existing[len[x][y]] = el;
                len[x][y]++;
            }
        }

        @Override
        public void printAll(Filler filler) {
            for (int x = 0; x < size; x++) {
                for (int y = 0; y < size; y++) {
                    Object[] elements = (Object[]) field[x][y];
                    if (elements == null || len[x][y] == 0) {
                        filler.set(x, y, emptyChar);
                        continue;
                    }

                    for (int index = 0; index < len[x][y]; index++) {
                        State<E, P> state = (State<E, P>)elements[index];
                        E el = state.state(player, elements);
                        if (el != null) {
                            filler.set(x, y, el.ch());
                            break;
                        }
                    }
                }
            }
        }
    }
}
