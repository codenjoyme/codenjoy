package com.codenjoy.dojo.web.rest.pojo;

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

import com.codenjoy.dojo.services.multiplayer.MultiplayerType;

public class PMuptiplayerType {
    private String type;
    private int roomSize;
    private int levelsCount;

    public PMuptiplayerType() {
        // do nothing
    }

    public PMuptiplayerType(MultiplayerType multiplayer) {
        type = multiplayer.getType();
        roomSize = multiplayer.getRoomSize();
        levelsCount = multiplayer.getLevelsCount();
    }

    public int getLevelsCount() {
        return levelsCount;
    }

    public int getRoomSize() {
        return roomSize;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setRoomSize(int roomSize) {
        this.roomSize = roomSize;
    }

    public void setLevelsCount(int levelsCount) {
        this.levelsCount = levelsCount;
    }
}
