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

import lombok.Builder;

public class Filter {

    private String room;
    private Integer count;
    private Integer afterId;
    private Integer beforeId;
    private Boolean inclusive;

    @Builder(builderMethodName = "with", buildMethodName = "get")
    public Filter(String room, Integer count, Integer afterId, Integer beforeId, Boolean inclusive) {
        this.room = room;
        this.count = count;
        this.afterId = afterId;
        this.beforeId = beforeId;
        this.inclusive = inclusive;
    }

    public static FilterBuilder room(String room) {
        return Filter.with().room(room);
    }

    public String room() {
        return room;
    }

    public Integer count() {
        return count;
    }

    public Integer afterId() {
        return afterId;
    }

    public Integer beforeId() {
        return beforeId;
    }

    public Boolean inclusive() {
        return inclusive;
    }
}
