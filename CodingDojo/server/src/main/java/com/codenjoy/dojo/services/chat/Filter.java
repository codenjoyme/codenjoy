package com.codenjoy.dojo.services.chat;

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
