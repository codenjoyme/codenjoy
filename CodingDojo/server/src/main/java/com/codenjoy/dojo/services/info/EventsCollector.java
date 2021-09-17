package com.codenjoy.dojo.services.info;

import com.codenjoy.dojo.services.multiplayer.LevelProgress;

import java.util.function.Consumer;

public class EventsCollector implements Information {

    public static final String LEVEL = "Level ";

    private Collector all = new Collector();
    private Consumer<String> onAdd;

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
        put(LEVEL + progress.getCurrent());
    }

    public String getAllMessages() {
        return all.popAll();
    }

    public void put(String message) {
        all.put(message);
        if (onAdd != null) {
            onAdd.accept(message);
        }
    }

    @Override
    public void onAdd(Consumer<String> onAdd) {
        this.onAdd = onAdd;
    }

}

