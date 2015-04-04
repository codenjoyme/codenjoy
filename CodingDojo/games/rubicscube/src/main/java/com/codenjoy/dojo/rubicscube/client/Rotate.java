package com.codenjoy.dojo.rubicscube.client;

import com.codenjoy.dojo.services.Dice;

/**
 * User: oleksandr.baglai
 */
public enum Rotate {
    CLOCKWISE(1),
    TWICE(2),
    CONTR_CLOCKWISE(-1);

    private final int rotate;

    private Rotate(int rotate) {
        this.rotate = rotate;
    }

    public String toString() {
        return this.name();
    }

    public static Rotate valueOf(int i) {
        for (Rotate d : Rotate.values()) {
            if (d.rotate == i) {
                return d;
            }
        }
        throw new IllegalArgumentException("No such Rotate for " + i);
    }

    public int rotate() {
        return rotate;
    }

    public static Rotate random(Dice dice) {
        return Rotate.values()[dice.next(Rotate.values().length)];
    }
}
