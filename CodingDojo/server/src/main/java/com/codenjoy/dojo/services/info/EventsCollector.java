package com.codenjoy.dojo.services.info;

import com.codenjoy.dojo.services.multiplayer.LevelProgress;

import java.util.LinkedList;
import java.util.List;
import java.util.function.Consumer;

public class EventsCollector implements Information {

    public static final String LEVEL = "Level ";

    private Collector all = new Collector();
    private Consumer<String> onAdd;
    private List<Consumer<String>> listeners = new LinkedList<>();

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

        for (Consumer<String> listener : listeners) {
            listener.accept(message);
        }
    }

    @Override
    public void onAdd(Consumer<String> listener) {
        listeners.add(listener);
    }

    @Override
    public void remove(Consumer<String> listener) {
        listeners.remove(listener);
    }

}
