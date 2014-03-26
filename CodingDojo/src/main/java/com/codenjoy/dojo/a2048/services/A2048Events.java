package com.codenjoy.dojo.a2048.services;

public class A2048Events {

    private Event event;
    private int number;

    public enum Event {
        INC, GAME_OVER, WIN;
    }

    @Override
    public String toString() {
        return event + ((number != 0)?("(" + number + ")"):"");
    }

    public A2048Events(Event event) {
        this.event = event;
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
