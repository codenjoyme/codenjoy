package com.codenjoy.dojo.services;

import com.apofig.profiler.Profiler;

import java.util.Calendar;

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
}
