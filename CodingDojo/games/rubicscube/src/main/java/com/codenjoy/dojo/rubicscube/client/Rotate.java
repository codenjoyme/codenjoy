package com.codenjoy.dojo.rubicscube.client;

/**
 * User: oleksandr.baglai
 */
public enum Rotate {
    CLOCKWISE(1),
    TWICE(2),
    CONTR_CLOCKWISE(-1);

    final int rotate;

    Rotate(int rotate) {
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
}
