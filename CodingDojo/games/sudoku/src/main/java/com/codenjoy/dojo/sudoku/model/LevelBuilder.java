package com.codenjoy.dojo.sudoku.model;

import com.codenjoy.dojo.services.Dice;
import com.codenjoy.dojo.services.LengthToXY;

/**
 * Created by Sanja on 16.02.14.
 */
public class LevelBuilder {

    private final LengthToXY xy;
    private final int size;
    private char[] mask;

    private static final String FIELD =
            "         " +
            "         " +
            "         " +
            "         " +
            "         " +
            "         " +
            "         " +
            "         " +
            "         ";

    private int openCount;
    private Dice dice;

    public LevelBuilder(int openCount, Dice dice) {
        this.openCount = openCount;
        this.dice = dice;
        this.size = (int)Math.sqrt(FIELD.length());
        this.xy = new LengthToXY(size);
    }

    public String getMask() {
        return withBorders(String.valueOf(mask));
    }

    private String withBorders(String board) {
        StringBuffer result = new StringBuffer();

        result.append("☼☼☼☼☼☼☼☼☼☼☼☼☼");
        for (int i = 0; i <= size - 1; i++) {
            result.append('☼');
            String line = board.substring(i * size, (i + 1) * size);
            result.append(line.substring(0, 3)).append('☼');
            result.append(line.substring(3, 6)).append('☼');
            result.append(line.substring(6, 9)).append('☼');
            if ((i + 1) % 3 == 0) {
                result.append("☼☼☼☼☼☼☼☼☼☼☼☼☼");
            }
        }
        return result.toString();
    }

    public String getBoard() {
        return withBorders("534678912" +
                        "672195348" +
                        "198342567" +
                        "859761423" +
                        "426853791" +
                        "713924856" +
                        "961537284" +
                        "287419635" +
                        "345286179");
    }

    public void build() {
        mask = FIELD.replaceAll(" ", "?").toCharArray();

        int index = 0;
        while (index < openCount) {
            int x = dice.next(9);
            int y = dice.next(9);

            int l = xy.getLength(x, y);
            if (mask[l] == ' ') continue;

            mask[l] = ' ';
            index++;
        }
    }
}
