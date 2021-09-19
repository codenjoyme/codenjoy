package com.codenjoy.dojo.services.info;

/*-
 * #%L
 * Codenjoy - it's a dojo-like platform from developers to developers.
 * %%
 * Copyright (C) 2018 - 2021 Codenjoy
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/gpl-3.0.html>.
 * #L%
 */

import com.codenjoy.dojo.services.multiplayer.LevelProgress;

import java.util.LinkedList;
import java.util.List;
import java.util.function.Consumer;

public class EventsCollector implements Information {

    public static final String LEVEL = "Level ";

    private Collector all = new Collector();
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
    public void add(Consumer<String> listener) {
        listeners.add(listener);
    }

    @Override
    public void remove(Consumer<String> listener) {
        listeners.remove(listener);
    }

}
