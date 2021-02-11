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

import com.codenjoy.dojo.services.Game;
import com.codenjoy.dojo.services.Player;
import com.codenjoy.dojo.services.PlayerSave;
import com.codenjoy.dojo.services.dao.Registration;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class PlayerDetailInfo {

    private String id;
    private String readableName;
    private String callbackUrl;
    private String gameType;
    private String roomName;
    private PMuptiplayerType multiplayer;
    private String score;
    private String save;
    private PHeroData hero;
    private PLevelProgress progress;
    private List<String> group;
    private PUser registration;

    public PlayerDetailInfo(Player player, Registration.User registration,
                            String roomName, Game game, List<String> group)
    {
        gameType = player.getGameType().name();
        this.roomName = roomName;
        multiplayer = new PMuptiplayerType(player.getGameType().getMultiplayerType());

        callbackUrl = player.getCallbackUrl();
        score = String.valueOf(player.getScore());
        id = player.getId();

        this.registration = new PUser(registration);

        progress = new PLevelProgress(game.getProgress());
        save = game.getSave().toString();
        hero = new PHeroData(game.getHero());
        this.group = group;
    }

    public PlayerSave buildPlayerSave() {
        if (StringUtils.isEmpty(roomName)) { // TODO test me
            roomName = gameType;
        }
        return new PlayerSave(id, callbackUrl, roomName, gameType, Integer.valueOf(score), save);
    }
}
