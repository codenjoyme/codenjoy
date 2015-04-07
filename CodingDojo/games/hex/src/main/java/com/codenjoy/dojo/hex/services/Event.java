package com.codenjoy.dojo.hex.services;

public class Event {

    private EventEnum event;
    private int count;

    // TODO подумать в контексте фреймворка как сделать так, чтобы любой ивент мог передавать параметры
    public enum EventEnum {
        WIN, LOOSE;
    }

    @Override
    public String toString() {
        return event + ((count != 0)?("(" + count + ")"):"");
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Event event = (Event) o;

        if (count != event.count) return false;
        if (this.event != event.event) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return count;
    }

    public Event(EventEnum event, int count) {
        this.event = event;
        this.count = count;
    }

    public int getCount() {
        return count;
    }

    public EventEnum getType() {
        return event;
    }
}
