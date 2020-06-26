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


import org.json.JSONObject;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by Oleksandr_Baglai on 2017-09-21.
 */
public class TickData {

    public static final TickData NULL = new TickData() {
        @Override
        public JSONObject getAct(String playerName) {
            return new JSONObject();
        }

        @Override
        public JSONObject getBoard() {
            return new JSONObject("{" +
                    "\"forces\":\"-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#\",\"inLobby\":true," +
                    "\"layers\":[\"" +
                    "                    " +
                    "                    " +
                    "   OOO  OOOO OOO    " +
                    "   O  O O    O  O   " +
                    "   O  O O    O  O   " +
                    "   OOO  OOO  OOO    " +
                    "   OO   O    O      " +
                    "   O O  O    O      " +
                    "   O  O OOOO O      " +
                    "                    " +
                    "                    " +
                    "    O     OO  O  O  " +
                    "    O    O  O O  O  " +
                    "    O    O  O O  O  " +
                    "    O    OOOO  OOO  " +
                    "    O    O  O    O  " +
                    "    O    O  O   O   " +
                    "    OOOO O  O OO    " +
                    "                    " +
                    "                    \"," +
                    "\"----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------\"]," +
                    "\"offset\":{\"x\":0,\"y\":0}}");
        }
    };

    private Map<String, JSONObject> heroes = new LinkedHashMap<>();
    private JSONObject board;

    public void heroAct(String player, String command) {
        heroes.put(player, new JSONObject(command));
    }

    public void board(String board) {
        this.board = new JSONObject(board);
    }

    public JSONObject getAct(String playerName) {
        return heroes.get(playerName);
    }

    public Map<String, JSONObject> getActs() {
        return heroes;
    }

    public JSONObject getBoard() {
        return new JSONObject(board.toString());
    }
}
