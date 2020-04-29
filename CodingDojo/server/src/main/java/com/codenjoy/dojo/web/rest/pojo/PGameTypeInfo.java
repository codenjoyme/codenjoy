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

import com.codenjoy.dojo.services.GameType;
import com.codenjoy.dojo.services.multiplayer.MultiplayerType;
import lombok.Getter;

import java.util.List;

@Getter
public class PGameTypeInfo {
    
    private String version;
    private String info;
    private int boardSize;
    private List<PParameter> parameters;
    private MultiplayerType multiplayerType;
    private String helpUrl;
    private String clientUrl;
    private String wsUrl;
    private PSprites sprites;

    public PGameTypeInfo(GameType type, String helpUrl, 
                         String clientUrl, String wsUrl,
                         PSprites sprites) 
    {
        version = type.getVersion();
        info = type.toString();
        boardSize = type.getBoardSize().getValue();
        parameters = new PParameters(type.getSettings().getParameters()).getParameters();
        multiplayerType = type.getMultiplayerType();
        this.helpUrl = helpUrl;
        this.clientUrl = clientUrl;
        this.wsUrl = wsUrl;
        this.sprites = sprites;
    }
}
