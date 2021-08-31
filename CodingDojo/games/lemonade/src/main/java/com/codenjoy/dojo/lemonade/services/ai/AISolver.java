package com.codenjoy.dojo.lemonade.services.ai;

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
import com.codenjoy.dojo.client.Solver;
import com.codenjoy.dojo.games.lemonade.Board;
import com.codenjoy.dojo.games.lemonade.WeatherForecast;
import com.codenjoy.dojo.services.Dice;

public class AISolver implements Solver<Board> {

    private Dice dice;
    private AbstractTextBoard board;

    public AISolver(Dice dice) {
        this.dice = dice;
    }

    @Override
    public String get(Board board) {
        if (board.isGameOver())
            return "message('go reset')";

        double lemonadeCost = board.getLemonadeCost();
        // let's spend some of our assets on the lemonade
        double divider = 3.2;
        if (board.getWeatherForecast() == WeatherForecast.CLOUDY)
            divider = 4.0;
        else if (board.getWeatherForecast() == WeatherForecast.HOT_AND_DRY)
            divider = 2.2;
        int lemonadeToMake = (int)Math.floor(board.getAssets() / divider / lemonadeCost);
        // let's make one sign for every 8 glasses of lemonade
        int signsToMake = lemonadeToMake / 7;
        if (board.getAssets() < 0.15)
            signsToMake = 0;
        if (board.getAssets() - lemonadeToMake * lemonadeCost < signsToMake * 0.15)
            signsToMake = (int)Math.floor((board.getAssets() - lemonadeToMake * lemonadeCost) / 0.15);
        if (signsToMake < 0)
            signsToMake = 0;
        // make the price depending on weather report
        int lemonadePriceCents = 8;
        if (board.getWeatherForecast() == WeatherForecast.CLOUDY)
            lemonadePriceCents = 5;
        else if (board.getWeatherForecast() == WeatherForecast.HOT_AND_DRY)
            lemonadePriceCents = 10;

        String answer = toAnswerString(lemonadeToMake, signsToMake, lemonadePriceCents);
        return answer;
    }

    private String toAnswerString(int lemonadeToMake, int signsToMake, int lemonadePriceCents) {
        return String.format("message('go %d,%d,%d')", lemonadeToMake, signsToMake, lemonadePriceCents);
    }
}
