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


import com.codenjoy.dojo.services.annotations.PerformanceOptimized;
import com.codenjoy.dojo.services.hero.HeroData;
import com.codenjoy.dojo.services.serializer.JSONObjectSerializer;
import com.codenjoy.dojo.transport.screen.ScreenData;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.SneakyThrows;
import org.apache.commons.lang3.StringUtils;
import org.json.JSONObject;

import java.util.List;
import java.util.Map;

@Getter
@AllArgsConstructor
public class PlayerData implements ScreenData {

    private static final ObjectMapper mapper;
    static {
        mapper = new ObjectMapper();
        SimpleModule module = new SimpleModule();
        module.addSerializer(JSONObject.class, new JSONObjectSerializer());
        mapper.registerModule(module);
    }

    private int boardSize;
    private String board;
    private String game;
    private Object score;
    private String info;
    private Map<String, Object> scores;
    private Map<String, Integer> teams;
    private Map<String, HeroData> coordinates;
    private Map<String, String> readableNames;
    private List<String> group;

    public String getInfo() {
        return (info == null) ? StringUtils.EMPTY : info;
    }

    @PerformanceOptimized
    public String asJson() {
        StringBuilder builder = new StringBuilder()
                .append("{\"boardSize\":")
                .append(boardSize)
                .append(",\"board\":");

        if (board.startsWith("{")) {
            builder.append(board);
        } else {
            builder.append("\"")
                    .append(board)
                    .append("\"");
        }

        builder.append(",\"game\":\"")
            .append(game)
            .append("\",\"score\":")
            .append(score)
            .append(",\"teams\":")
            .append(toJson(teams))
            .append(",\"info\":\"")
            .append(getInfo())
            .append("\",\"scores\":")
            .append(toJson(scores))
            .append(",\"coordinates\":")
            .append(toJson(coordinates))
            .append(",\"readableNames\":")
            .append(toJson(readableNames))
            .append(",\"group\":")
            .append(toJson(group))
            .append("}");

        return builder.toString();
    }

    @Override
    public String toString() {
        return String.format(
                "PlayerData[BoardSize:%s, " +
                        "Board:'%s', " +
                        "Game:'%s', " +
                        "Score:%s, " +
                        "Teams:%s, " +
                        "Info:'%s', " +
                        "Scores:'%s', " +
                        "Coordinates:'%s', " +
                        "ReadableNames:'%s', " +
                        "Group:%s]",
                boardSize,
                board,
                game,
                score,
                teams,
                getInfo(),
                scores,
                coordinates,
                readableNames,
                group);
    }

    @SneakyThrows
    private String toJson(Object data) {
        return mapper.writeValueAsString(data);
    }

}
