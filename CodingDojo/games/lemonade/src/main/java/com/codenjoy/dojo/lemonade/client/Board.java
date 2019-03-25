package com.codenjoy.dojo.lemonade.client;

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


import com.codenjoy.dojo.client.AbstractTextBoard;
import com.codenjoy.dojo.client.ClientBoard;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class Board extends AbstractTextBoard {

    private final ArrayList<DailyReport> history;
    private final InputData inputData;

    private Board(InputData inputData, ArrayList<DailyReport> history){
        this.inputData = inputData;
        this.history = history;
    }

    public Board(){
        history = null;
        inputData = null;
    }

    @Override
    public ClientBoard forString(String data) {
        JSONObject input = new JSONObject(data);
        JSONArray historyJson = input.getJSONArray("history");
        ArrayList<DailyReport> history = parseHistory(historyJson);
        InputData inputData = InputData.fromJson(input);
        return new Board(inputData, history);
    }

    private ArrayList<DailyReport> parseHistory(JSONArray historyJson) {
        ArrayList<DailyReport> history = new ArrayList<>();
        historyJson.forEach(reportJson -> {
            history.add(DailyReport.fromJson(reportJson));
        });
        return history;
    }

    public List<DailyReport> getHistory(){
        return history;
    }

    public String getData() {
        return data;
    }

    public InputData getInputData() {
        return inputData;
    }
}
