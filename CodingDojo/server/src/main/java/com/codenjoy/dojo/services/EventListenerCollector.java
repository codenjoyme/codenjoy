package com.codenjoy.dojo.services;

/*-
 * #%L
 * Codenjoy - it's a dojo-like platform from developers to developers.
 * %%
 * Copyright (C) 2018 Codenjoy
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
import org.json.JSONObject;

import java.util.Deque;
import java.util.LinkedList;
import java.util.List;

public class EventListenerCollector implements EventListener, ChangeLevelListener, Information {

    private EventListener listener;
    private Collector collector = new Collector();

    public EventListenerCollector(EventListener listener) {
        this.listener = listener;
    }

    @Override
    public void event(Object event) {
        collector.put(event.toString());
        listener.event(event);
    }

    @Override
    public String getMessage() {
        return collector.toString();
    }

    @Override
    public void levelChanged(LevelProgress progress) {
        collector.put("LEVEL " + progress.getCurrent());
    }
}
