package com.codenjoy.dojo.web.rest.pojo;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder(toBuilder = true)
public class PMessage {
    private String id;
    private final String text;
    private final String roomId;
    private final String playerId;
    private final LocalDateTime timestamp;
}
