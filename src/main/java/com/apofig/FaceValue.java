package com.apofig;

import java.util.Arrays;

/**
 * User: sanja
 * Date: 08.10.13
 * Time: 4:49
 */
public class FaceValue {
    private Color[] colors = new Color[9];

    public FaceValue(Color fill) {
        Arrays.fill(colors, fill);
    }

    @Override
    public String toString() {
        String result = "";
        for (int line = 0; line < 3; line ++) {
            result += getLine(line);
        }
        return result;
    }

    public String getLine(int line) {
        String result = "";
        for (int index = 0; index < 3; index ++) {
            result += colors[line*3 + index].value();
        }
        return result;
    }
}
