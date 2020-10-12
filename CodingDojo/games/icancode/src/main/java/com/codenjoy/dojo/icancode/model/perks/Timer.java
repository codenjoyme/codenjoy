package com.codenjoy.dojo.icancode.model.perks;

import com.codenjoy.dojo.services.Tickable;

public class Timer implements Tickable {

    private final int timeout;
    private int value;

    public Timer(int timeout) {
        this.timeout = timeout;
        this.value = timeout;
    }

    public static Timer empty() {
        return new Timer(0);
    }

    public boolean isTimeUp() {
        return value == 0;
    }

    @Override
    public void tick() {
        if (value > 0) {
            value--;
        }
    }

    public void reset() {
        value = timeout;
    }

    @Override
    public String toString() {
        return "Timer{" +
                "timeout=" + timeout +
                ", value=" + value +
                '}';
    }
}
