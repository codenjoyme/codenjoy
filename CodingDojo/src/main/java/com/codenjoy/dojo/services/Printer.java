package com.codenjoy.dojo.services;

import static com.codenjoy.dojo.services.PointImpl.pt;

/**
 * Этот малый умеет печатать состояние борды на экране.
 * @see Printer#toString()
 */
public class Printer {
    public static final String ERROR_SYMBOL = "Ъ";
    private Enum[][] field;
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
        for (Enum[] currentRow : field) {
            for (Enum element : currentRow) {
                String symbol = (element == null) ? ERROR_SYMBOL : element.toString();
                string.append(symbol);
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
        field = new Enum[size][size];
        printer.init();

        for (int x = 0; x < size; x++) {
            for (int y = 0; y < size; y++) {
                set(x, y, printer.get(pt(x, y)));
            }
        }
    }

    private void set(int x, int y, Enum element) {
        if (x == -1 || y == -1) {
            return;
        }

        field[size - 1 - y][x] = element;
    }
}
