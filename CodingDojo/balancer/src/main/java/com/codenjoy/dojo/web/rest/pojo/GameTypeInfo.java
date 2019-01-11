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
import com.codenjoy.dojo.services.settings.Parameter;

import java.util.List;

public class GameTypeInfo {
    private final String version;
    private final String info;
    private final int boardSize;
    private final List<Parameter<?>> parameters;
    private final MultiplayerType multiplayerType;

    public GameTypeInfo(GameType gameType) {
        version = gameType.getVersion();
        info = gameType.toString();
        boardSize = gameType.getBoardSize().getValue();
        parameters = gameType.getSettings().getParameters();
        multiplayerType = gameType.getMultiplayerType();
    }

    public String getVersion() {
        return version;
    }

    public String getInfo() {
        return info;
    }

    public int getBoardSize() {
        return boardSize;
    }

    public List<Parameter<?>> getParameters() {
        return parameters;
    }

    public MultiplayerType getMultiplayerType() {
        return multiplayerType;
    }
}
