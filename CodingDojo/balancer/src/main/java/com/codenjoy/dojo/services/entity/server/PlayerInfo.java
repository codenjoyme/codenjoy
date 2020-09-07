package com.codenjoy.dojo.services.entity.server;

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

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class PlayerInfo {

    private String gameType;
    private String callbackUrl;
    private String id;
    private String readableName;
    private String score;
    private String code;
    private boolean winner;

    public PlayerInfo(String id, String score) {
        this.id = id;
        this.score = score;
    }

    @Override
    public String toString() {
        return "PlayerInfo{" +
                "gameType='" + gameType + '\'' +
                ", callbackUrl='" + callbackUrl + '\'' +
                ", id='" + id + '\'' +
                ", readableName='" + readableName + '\'' +
                ", score='" + score + '\'' +
                ", code='" + code + '\'' +
                '}';
    }

    public void setId(String id) {
        this.id = id;
    }
}
