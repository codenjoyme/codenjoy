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
import com.codenjoy.dojo.lemonade.model.WeatherForecast;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class Board extends AbstractTextBoard {

    private ArrayList<DailyReport> history;

    private int day;
    private double lemonadePrice;
    private double assets;
    private WeatherForecast weatherForecast;
    private String messages;
    private boolean isBankrupt;

    @Override
    public boolean isGameOver() {
        return isBankrupt;
    }

    @Override
    public ClientBoard forString(String data) {
        this.data = data;
        JSONObject input = new JSONObject(data);
        JSONArray historyJson = input.getJSONArray("history");
        parseHistory(historyJson);
        parseData(input);
        return this;
    }

    private void parseData(JSONObject dataJson) {
        this.day = dataJson.getInt("day");
        this.lemonadePrice = dataJson.getFloat("lemonadePrice");
        this.assets = dataJson.getFloat("assets");
        this.weatherForecast = dataJson.getEnum(WeatherForecast.class, "weatherForecast");
        this.messages = dataJson.getString("messages");
        this.isBankrupt = dataJson.getBoolean("isBankrupt");
    }

    private void parseHistory(JSONArray historyJson) {
        this.history = new ArrayList<>();
        historyJson.forEach(reportJson -> {
            history.add(DailyReport.fromJson(reportJson));
        });
    }

    public List<DailyReport> getHistory(){
        return this.history;
    }

    public String getData() {
        return this.data;
    }

    public double getAssets() {
        return this.assets;
    }

    public String getMessages(){
        return this.messages;
    }

    public WeatherForecast getWeatherForecast(){
        return this.weatherForecast;
    }
}
