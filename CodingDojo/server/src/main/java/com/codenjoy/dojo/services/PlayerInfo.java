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

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class PlayerInfo extends Player {

    private boolean saved;
    private boolean active;
    private boolean hidden;
    private boolean aiPlayer;

    private PlayerInfo(String id, String readableName, String code, String url, String roomName, String gameName, Object scoreValue, boolean saved) {
        setId(id);
        setReadableName(readableName);
        setCode(code);
        setCallbackUrl(url);
        setGameName(gameName);
        setRoomName(roomName);
        setScore(scoreValue);
        this.saved = saved;
        active = false;
        hidden = false;
    }

    public PlayerInfo(Player player) {
        this(player.getId(), player.getCode(), player.getCallbackUrl(), player.getGameName());
        aiPlayer = player.hasAi();
        setScore(player.getScore());
        setRoomName(player.getRoomName());
        setReadableName(player.getReadableName());
    }

    public PlayerInfo(String id, String code, String url, String gameName) {
        setId(id);
        setCode(code);
        setCallbackUrl(url);
        setGameName(gameName);
        saved = false;
        active = true;
    }

    public PlayerInfo(PlayerSave save, String readableName, String code) {
        this(save.getId(), readableName, code,
                save.getCallbackUrl(), save.getRoomName(),
                save.getGameName(), save.getScore(), true);
    }

}
