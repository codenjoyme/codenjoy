package com.codenjoy.dojo.services;

import static com.codenjoy.dojo.services.PointImpl.pt;

/**
 * Этот малый умеет печатать состояние борды на экране.
 * @see Printer#toString()
 */
public class Printer {
    public static final String ERROR_SYMBOL = "Ъ";
    private char[][] field;
    private final int size;
    private GamePrinter printer;

    public Printer(int size, GamePrinter printer) {
        this.printer = printer;
        this.size = size;
    }

    /**
     * @return Строковое представление борды будет отправлено фреймворку и на его основе будет отрисована игра на клиенте.
     * @see com.codenjoy.dojo.sample.model.Elements
     */
    @Override
    public String toString() {
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
        boolean mode = printer.init();

        if (mode) {
            for (int x = 0; x < size; x++) {
                for (int y = 0; y < size; y++) {
                    set(x, y, printer.get(pt(x, y)));
                }
            }
        } else {
            printer.printAll(new GamePrinter.Filler() {
                @Override
                public void set(int x, int y, char ch) {
                    Printer.this.set(x, y, ch);
                }
            });
        }
    }

    private void set(int x, int y, char ch) {
        if (x == -1 || y == -1) {
            return;
        }

        field[size - 1 - y][x] = ch;
    }

    public static class GamePrinterImpl<E extends CharElements, P> implements GamePrinter {

        private final BoardReader board;
        private int size;
        private P player;
        private char emptyChar;

        private Object[][] field;
        private byte[][] len;

        public GamePrinterImpl(BoardReader board, P player, char emptyChar) {
            this.board = board;
            this.player = player;
            this.emptyChar = emptyChar;
        }

        @Override
        public boolean init() {
            size = board.size();
            field = new Object[size][size];
            len = new byte[size][size];

            addAll(board.elements());
            return false;
        }

        private void addAll(Iterable<? extends Point> elements) {
            for (Point el : elements) {
                int x = el.getX();
                int y = el.getY();

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
        public char get(Point pt) {
            return emptyChar;
        }

        @Override
        public void printAll(GamePrinter.Filler filler) {
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
