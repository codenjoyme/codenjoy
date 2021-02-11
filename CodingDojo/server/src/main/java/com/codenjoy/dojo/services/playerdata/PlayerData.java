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


import com.codenjoy.dojo.transport.screen.ScreenData;
import com.codenjoy.dojo.utils.JsonUtils;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.apache.commons.lang3.StringUtils;
import org.json.JSONObject;

@Getter
@AllArgsConstructor
public class PlayerData implements ScreenData {

    private int boardSize;
    private Object board;
    private String gameName;
    private Object score;
    private String info;
    private JSONObject scores;
    private JSONObject heroesData;

    public String getInfo() {
        return (info == null) ? StringUtils.EMPTY : info;
    }

    @Override
    public String toString() {
        return String.format(
                "PlayerData[BoardSize:%s, " +
                        "Board:'%s', " +
                        "GameName:'%s', " +
                        "Score:%s, " +
                        "Info:'%s', " +
                        "Scores:'%s', " +
                        "HeroesData:'%s']",
                boardSize,
                board,
                gameName,
                score,
                getInfo(),
                JsonUtils.toStringSorted(scores.toString()),
                JsonUtils.toStringSorted(heroesData.toString()));
    }

}
