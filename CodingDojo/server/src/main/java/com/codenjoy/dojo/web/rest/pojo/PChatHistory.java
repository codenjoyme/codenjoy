package com.codenjoy.dojo.web.rest.pojo;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class PChatHistory {
    private final LocalDateTime from;
    private final LocalDateTime to;
    private final List<PMessage> messages;
}
