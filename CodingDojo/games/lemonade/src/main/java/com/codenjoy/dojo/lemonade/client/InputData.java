package com.codenjoy.dojo.lemonade.client;

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

import com.codenjoy.dojo.lemonade.model.WeatherForecast;
import org.json.JSONObject;

public class InputData {
    private final int day;
    private final double lemonadePrice;
    private final double assets;
    private final WeatherForecast weatherForecast;
    private final String messages;

    public String getMessages(){
        return messages;
    }

    public InputData(int day, float lemonadePrice, float assets, WeatherForecast weatherForecast, String messages) {

        this.day = day;
        this.lemonadePrice = lemonadePrice;
        this.assets = assets;
        this.weatherForecast = weatherForecast;
        this.messages = messages;
    }

    public static InputData fromJson(JSONObject inputJson){
        int day = inputJson.getInt("day");
        float lemonadePrice = inputJson.getFloat("lemonadePrice");
        float assets = inputJson.getFloat("assets");
        WeatherForecast weatherForecast = inputJson.getEnum(WeatherForecast.class, "weatherForecast");
        String messages = inputJson.getString("messages");
        return new InputData(day, lemonadePrice, assets, weatherForecast, messages);
    }

    public double getAssets() {
        return assets;
    }
}
