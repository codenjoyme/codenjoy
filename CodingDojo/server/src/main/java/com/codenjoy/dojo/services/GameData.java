package com.codenjoy.dojo.services;

/*-
 * #%L
 * Codenjoy - it's a dojo-like platform from developers to developers.
 * %%
 * Copyright (C) 2016 Codenjoy
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

/**
 * Created by indigo on 2016-10-30.
 */
class GameData {
    private final int boardSize;
    private final GuiPlotColorDecoder decoder;
    private final JSONObject scores;
    private final JSONObject heroesData;

    public GameData(int boardSize, GuiPlotColorDecoder decoder, JSONObject scores, JSONObject heroesData) {
        this.boardSize = boardSize;
        this.decoder = decoder;
        this.scores = scores;
        this.heroesData = heroesData;
    }

    public GuiPlotColorDecoder getDecoder() {
        return decoder;
    }

    public int getBoardSize() {
        return boardSize;
    }

    public JSONObject getScores() {
        return scores;
    }

    public JSONObject getHeroesData() {
        return heroesData;
    }
}
