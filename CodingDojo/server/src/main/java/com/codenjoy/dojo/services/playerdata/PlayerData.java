package com.codenjoy.dojo.services.playerdata;

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


import com.codenjoy.dojo.services.hero.HeroData;
import com.codenjoy.dojo.transport.screen.ScreenData;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.apache.commons.lang3.StringUtils;

import java.util.List;
import java.util.Map;

@Getter
@AllArgsConstructor
public class PlayerData implements ScreenData {

    private int boardSize;
    private Object board;
    private String game;
    private Object score;
    private String info;
    private Map<String, Object> scores;
    private Map<String, HeroData> coordinates;
    private Map<String, String> readableNames;
    private List<String> group;
    private Integer lastChatMessage;

    public String getInfo() {
        return (info == null) ? StringUtils.EMPTY : info;
    }

    @Override
    public String toString() {
        return String.format(
                "PlayerData[BoardSize:%s, " +
                        "Board:'%s', " +
                        "Game:'%s', " +
                        "Score:%s, " +
                        "Info:'%s', " +
                        "Scores:'%s', " +
                        "Coordinates:'%s', " +
                        "ReadableNames:'%s', " +
                        "Group:%s, " +
                        "LastChatMessage:%s]",
                boardSize,
                board,
                game,
                score,
                getInfo(),
                scores,
                coordinates,
                readableNames,
                group,
                lastChatMessage);
    }

}
