package com.codenjoy.dojo.services.info;

import com.codenjoy.dojo.services.multiplayer.LevelProgress;

public abstract class EventsCollector implements Information {

    public static final String LEVEL = "Level ";

    protected Collector all = new Collector();

    @Override
    public void event(Object event) {
        all.put(event.toString());
    }

    @Override
    public String getMessage() {
        return getAllMessages();
    }

    @Override
    public abstract void levelChanged(LevelProgress progress);

    public String getAllMessages() {
        return all.popAll();
    }

    public void put(String message) {
        all.put(message);
    }

}

