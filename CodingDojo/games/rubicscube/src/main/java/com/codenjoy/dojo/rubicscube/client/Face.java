package com.codenjoy.dojo.rubicscube.client;

import com.codenjoy.dojo.services.Dice;

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

    private final int number;

    private Face(int number) {
        this.number = number;
    }

    public String toString() {
        return this.name();
    }

    public int number() {
        return number;
    }

    public static Face valueOf(int i) {
        for (Face d : Face.values()) {
            if (d.number == i) {
                return d;
            }
        }
        throw new IllegalArgumentException("No such Face for " + i);
    }

    public static Face random(Dice dice) {
        return Face.valueOf(dice.next(Face.values().length) + 1);
    }
}
