package com.codenjoy.dojo.services.helper;

import com.codenjoy.dojo.services.GameService;
import com.codenjoy.dojo.services.GameType;
import com.codenjoy.dojo.services.room.RoomService;
import com.codenjoy.dojo.services.settings.SettingsReader;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class RoomHelper {

    private final RoomService rooms;
    private final GameService games;

    public SettingsReader settings(String room, String game) {
        GameType type = rooms.create(room, games.getGameType(game));
        SettingsReader settings = (SettingsReader) type.getSettings();
        return settings;
    }

    public void removeAll() {
        rooms.removeAll();
        games.removeAll();
    }
}
