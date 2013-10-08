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
        for (Color color : colors) {
            result += color.value();
        }
        return result;
    }
}
