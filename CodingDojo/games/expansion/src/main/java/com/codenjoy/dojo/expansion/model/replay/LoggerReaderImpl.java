package com.codenjoy.dojo.expansion.model.replay;

/*-
 * #%L
 * expansion - it's a dojo-like platform from developers to developers.
 * %%
 * Copyright (C) 2016 - 2020 Codenjoy
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


import com.codenjoy.dojo.expansion.model.Elements;
import com.codenjoy.dojo.expansion.model.levels.Levels;
import com.codenjoy.dojo.services.Point;
import com.codenjoy.dojo.services.PointImpl;
import org.json.JSONObject;

import java.io.File;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.codenjoy.dojo.services.PointImpl.pt;
import static com.codenjoy.dojo.expansion.services.SettingsWrapper.data;

/**
 * Created by Oleksandr_Baglai on 2017-09-21.
 */
public class LoggerReaderImpl implements LoggerReader {

    private List<TickData> boards = new LinkedList<>();
    private String playerId;
    private String hero;
    private Point basePosition;
    private Elements baseColor;
    private Map<String, String> playerNames = new LinkedHashMap<>();
    private Map<String, JSONObject> bases = new LinkedHashMap<>();

    public LoggerReaderImpl(String replayName, String playerId) {
        this.playerId = playerId;

        File file = GameLoggerImpl.getReplayFile(replayName);
        List<String> strings = Levels.loadLines(
                file.getAbsolutePath(),
                LinkedList::new,
                (container, line) -> {
                    container.add(line);
                    return container;
                }
        );

        TickData board = null;
        for (String line : strings) {
            if (line.equals("Game started")) {
                board = new TickData();
                continue;
            }

            if (line.startsWith("------")) {
                boards.add(board);
                board = new TickData();
            }

            if (line.startsWith("New player")) {
                List<String> parts = values("New player (.*) registered with hero (.*) with base at '(.*)' and color '(.*)' for user '(.*)'", line);
                String player = parts.get(0);
                String hero = parts.get(1);
                String base = parts.get(2);
                String color = parts.get(3);
                String name = parts.get(4);
                newPlayer(player, hero, base, color, name);
            }

            if (line.startsWith("Hero")) {
                List<String> parts = values("Hero (.*) of player (.*) received command:'(.*)'", line);
                if (!parts.isEmpty()) {
                    String player = playerNames.get(parts.get(1));
                    String command = parts.get(2);
                    board.heroAct(player, command);
                } else {
                    if (line.endsWith("is not alive")) {
                        // do nothing
                    }
                }
            }

            if (line.startsWith("Board:")) {
                List<String> parts = values("Board:'(.*)'", line);
                board.board(parts.get(0));
            }

        }
    }

    private void newPlayer(String player, String hero, String base, String color, String name) {
        playerNames.put(player, name);

        bases.put(name, getCoordinateJSON(base));

        if (player.equals(playerId)) {
            this.hero = hero;
            basePosition = new PointImpl(new JSONObject(base));
            baseColor = Elements.getForce(Integer.valueOf(color));
        }
    }

    private JSONObject getCoordinateJSON(String base) {
        JSONObject result = new JSONObject();
        result.put("coordinate", new JSONObject(base));
        result.put("singleBoardGame", true);
        return result;
    }

    private List<String> values(String patternString, String data) {
        Pattern pattern = Pattern.compile(patternString, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(data);
        List<String> result = new LinkedList<>();
        while (matcher.find()) {
            for (int i = 1; i <= matcher.groupCount(); i++) {
                result.add(matcher.group(i));
            }
        }
        return result;
    }

    @Override
    public Map<String, JSONObject> getAllLastActions(int tick) {
        return getTickData(tick).getActs();
    }

    @Override
    public Map<String, JSONObject> getAllBasePositions() {
        return bases;
    }

    @Override
    public Point getBasePosition(int tick) {
        if (isOutOf(tick)) {
            return pt(-1, -1);
        }
        return basePosition.copy();
    }

    @Override
    public int size() {
        return boards.size();
    }

    @Override
    public JSONObject getBoard(int tick) {
        boolean inLobby = isOutOf(tick);
        JSONObject result = getTickData(tick).getBoard();
        result.put("rounds", (inLobby) ? -1 : data.roundTicks());
        result.put("round", (inLobby) ? -1 : data.roundTicks() - tick);
        result.put("myBase", new JSONObject(getBasePosition(tick)));
        result.put("myColor", baseColor.getIndex());
        result.put("tick", (inLobby) ? 0 : tick);
        result.put("available", 0);
        result.put("showName", true);
        result.put("onlyMyName", false);
        result.put("levelProgress", new JSONObject()); // TODO implement me
        // TODO почему-то по завершению риплея перед самим выходом в риплей-лобби в скрипте возникает ошибка
        // TypeError: Cannot read property 'coordinate' of null
        // at expansion.js:385
        // at drawName (canvases.js:238)
        // at Object.drawPlayerNames (
        result.put("inLobby", inLobby);

        return result;
    }

    private boolean isOutOf(int tick) {
        return tick < 0 || tick >= boards.size();
    }

    private TickData getTickData(int tick) {
        if (isOutOf(tick)) {
            return TickData.NULL;
        }
        return boards.get(tick);
    }
}
