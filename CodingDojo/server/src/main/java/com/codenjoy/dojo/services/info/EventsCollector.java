package com.codenjoy.dojo.services.info;

import com.codenjoy.dojo.services.multiplayer.LevelProgress;

public class EventsCollector implements Information {

    public static final String LEVEL = "Level ";

    private Collector all = new Collector();

    @Override
    public void event(Object event) {
        all.put(event.toString());
    }

    @Override
    public String getMessage() {
        return getAllMessages();
    }

    @Override
    public void levelChanged(LevelProgress progress) {
        all.put(LEVEL + progress.getCurrent());
    }

    public String getAllMessages() {
        return all.popAll();
    }

}

