package com.codenjoy.dojo.a2048.services;

public class A2048Events {

    private Event event;
    private int number;

    public enum Event {
        INC, GAME_OVER;
    }

    @Override
    public String toString() {
        return event + "(" + number + ")";
    }

    public A2048Events(Event event, int number) {
        this.event = event;
        this.number = number;
    }

    public int getNumber() {
        return number;
    }

    public Event getType() {
        return event;
    }
}
