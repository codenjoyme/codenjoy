package com.codenjoy.dojo.services.room;

import com.codenjoy.dojo.services.GameType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.ToString;

@Data
@AllArgsConstructor
@ToString
public class RoomState {

    private String name;
    private GameType type;
    private Boolean active;

    public RoomState(RoomState state) {
        this(state.name, state.type, state.active);
    }
}
