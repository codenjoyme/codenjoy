package com.codenjoy.dojo.hex.services;

public class HexEvent {

    private Event event;
    private int count;

    public enum Event {
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

        HexEvent hexEvent = (HexEvent) o;

        if (count != hexEvent.count) return false;
        if (event != hexEvent.event) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return count;
    }

    public HexEvent(Event event, int count) {
        this.event = event;
        this.count = count;
    }

    public int getCount() {
        return count;
    }

    public Event getType() {
        return event;
    }
}
