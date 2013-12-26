package com.codenjoy.dojo.services;

import static com.codenjoy.dojo.services.PointImpl.pt;

/**
 * Этот малый умеет печатать состояние борды на экране.
 * @see Printer#toString()
 */
public class Printer {
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
                string.append(element.toString());
            }
            string.append("\n");
        }
        return string.toString();
    }

    private void fillField() {
        field = new Enum[size][size];

        for (int x = 0; x < size; x++) {
            for (int y = 0; y < size; y++) {
                set(pt(x, y), printer.get(x, y));
            }
        }
    }

    private void set(Point pt, Enum element) {
        if (pt.getY() == -1 || pt.getX() == -1) {
            return;
        }

        field[size - 1 - pt.getY()][pt.getX()] = element;
    }
}
