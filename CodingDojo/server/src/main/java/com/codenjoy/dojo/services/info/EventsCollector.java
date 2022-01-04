package com.codenjoy.dojo.services.info;

/*-
 * #%L
 * Codenjoy - it's a dojo-like platform from developers to developers.
 * %%
 * Copyright (C) 2012 - 2022 Codenjoy
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

public class EventsCollector implements Information {

    public static final String LEVEL = "Level ";

    private String playerId;
    private Collector all = new Collector();
    private List<MessagesListener> listeners = new LinkedList<>();

    public EventsCollector(String playerId) {
        this.playerId = playerId;
    }

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

        for (MessagesListener listener : listeners) {
            listener.accept(playerId, message);
        }
    }

    @Override
    public void add(MessagesListener listener) {
        listeners.add(listener);
    }

    @Override
    public void remove(MessagesListener listener) {
        listeners.remove(listener);
    }

}
