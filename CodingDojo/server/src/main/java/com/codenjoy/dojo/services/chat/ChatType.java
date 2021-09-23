package com.codenjoy.dojo.services.chat;

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

import java.util.Arrays;

public enum ChatType {

    ROOM(1),
    TOPIC(2),
    FIELD(3);

    private final int id;

    ChatType(int id) {
        this.id = id;
    }

    public static ChatType valueOf(int id) {
        return Arrays.stream(values())
                .filter(type -> type.id() == id)
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Bad chat type: " + id));
    }

    public int id() {
        return id;
    }
}
