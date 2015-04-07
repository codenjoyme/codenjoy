package com.codenjoy.dojo.moebius.services;

public class Events {

    private Event event;
    private int lines;

    public enum Event {
        GAME_OVER, WIN;
    }

    @Override
    public String toString() {
        return event + ((lines != 0)?("(" + lines + ")"):"");
    }

    public Events(Event event) {
        this.event = event;
    }

    public Events(Event event, int lines) {
        this.event = event;
        this.lines = lines;
    }

    public int getLines() {
        return lines;
    }

    public Event getType() {
        return event;
    }
}
