package com.codenjoy.dojo.web.controller;

/*-
 * #%L
 * Codenjoy - it's a dojo-like platform from developers to developers.
 * %%
 * Copyright (C) 2018 - 2019 Codenjoy
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

import org.apache.commons.collections4.BidiMap;
import org.apache.commons.collections4.bidimap.DualLinkedHashBidiMap;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Set;

@Component
public class RoomsAliaser {

    private BidiMap<String, String> rooms;

    public RoomsAliaser() {
        rooms = new DualLinkedHashBidiMap();
        rooms.put("battlecity1", "Haifa");
        rooms.put("battlecity2", "Jerusalem");
        rooms.put("battlecity3", "Co-studying");
        rooms.put("battlecity4", "Semi Final");
        rooms.put("battlecity5", "Final");
    }

    public String getAlias(String gameName) {
        return rooms.get(gameName);
    }

    public Set<String> alises() {
        return rooms.values();
    }

    public String getGameName(String alias) {
        return rooms.getKey(alias);
    }

    public Set<String> gameNames() {
        return rooms.keySet();
    }

    public Set<Map.Entry<String, String>> all() {
        return rooms.entrySet();
    }
}
