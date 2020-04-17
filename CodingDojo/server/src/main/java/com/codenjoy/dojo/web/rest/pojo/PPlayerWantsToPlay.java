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

import lombok.Getter;

import java.util.List;

@Getter
public class PPlayerWantsToPlay {

    private String context;
    private PGameTypeInfo gameType;
    private boolean registered;
    private List<String> sprites;
    private String alphabet;
    private List<PlayerInfo> players;

    public PPlayerWantsToPlay(String context,
                              PGameTypeInfo gameType,
                              boolean registered,
                              List<String> sprites,
                              String alphabet,
                              List<PlayerInfo> players)
    {
        this.context = context;
        this.gameType = gameType;
        this.registered = registered;
        this.sprites = sprites;
        this.alphabet = alphabet;
        this.players = players;
    }

    @Override
    public String toString() {
        return "PPlayerWantsToPlay{" +
                "context='" + context + '\'' +
                ", gameType=" + gameType +
                ", registered=" + registered +
                ", sprites=" + sprites +
                ", alphabet='" + alphabet + '\'' +
                ", players=" + players +
                '}';
    }
}
