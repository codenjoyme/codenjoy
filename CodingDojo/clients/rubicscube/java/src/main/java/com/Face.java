package com;

/**
 * User: oleksandr.baglai
 */
public enum Face {
    LEFT(1),
    FRONT(2),
    RIGHT(3),
    BACK(4),
    UP(5),
    DOWN(6);

    final int number;

    Face(int number) {
        this.number = number;
    }

    public String toString() {
        return this.name();
    }

    public static Face valueOf(int i) {
        for (Face d : Face.values()) {
            if (d.number == i) {
                return d;
            }
        }
        throw new IllegalArgumentException("No such Face for " + i);
    }
}
