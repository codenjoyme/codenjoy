package com.codenjoy.dojo.lemonade.model;

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


import com.codenjoy.dojo.services.joystick.MessageJoystick;
import com.codenjoy.dojo.services.multiplayer.GameField;
import com.codenjoy.dojo.services.multiplayer.PlayerHero;

import java.util.Queue;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Это реализация героя. Обрати внимание, что он имплементит {@see Joystick}, а значит может быть управляем фреймворком
 * Так же он имплементит {@see Tickable}, что значит - есть возможность его оповещать о каждом тике игры.
 * Эти интерфейсы объявлены в {@see PlayerHero}.
 */
public class Hero extends PlayerHero<GameField<Player>> implements MessageJoystick {

    private static Pattern patternGo;
    private final ServerMessagesManager serverMessagesManager;
    private Simulator simulator;
    private final GameSettings gameSettings;
    private final Queue<SalesResult> history;
    private SalesResult salesResult;
    private boolean alive;

    static {
        patternGo = Pattern.compile(
                "go\\s*(-?[\\d]+)[,\\s]\\s*(-?[\\d]+)[,\\s]\\s*(-?[\\d]+)", Pattern.CASE_INSENSITIVE);
    }

    public Hero(long randomSeed, GameSettings gameSettings, Queue<SalesResult> history) {
        simulator = new Simulator(randomSeed);
        this.gameSettings = gameSettings;
        alive = true;
        serverMessagesManager = new ServerMessagesManager();
        this.history = history;
    }

    @Override
    public void init(GameField<Player> field) {
        simulator.reset();
        if (this.history != null)
            this.history.clear();
    }

    @Override
    public void message(String s) {
        serverMessagesManager.reset();
        String command = s.toLowerCase();

        if (command.contains("reset")) {
            if (this.history != null)
                this.history.clear();
            simulator.reset();
            return;
        }

        if (!isAlive() || isGameOver())
            return;

        if (simulator.isBankrupt()) {
            this.salesResult = null;
            return;
        }

        Matcher matcher = patternGo.matcher(command);
        if (matcher.matches()) {
            int day = simulator.getDay();
            double assetsBefore = simulator.getAssets();

            int lemonadeToMake = Integer.parseInt(matcher.group(1));
            int signsToMake = Integer.parseInt(matcher.group(2));
            int lemonadePriceCents = Integer.parseInt(matcher.group(3));

            simulate(lemonadeToMake, signsToMake, lemonadePriceCents);

            readSalesResult(day, assetsBefore);
        } else {
            serverMessagesManager.setCommandInvalid(true);
        }
    }

    private void readSalesResult(int day, double assetsBefore) {
        this.salesResult = new SalesResult(
                day,
                assetsBefore,
                simulator.getLemonadeSold(),
                simulator.getLemonadePrice(),
                simulator.getIncome(),
                simulator.getLemonadeMade(),
                simulator.getSignsMade(),
                simulator.getExpenses(),
                simulator.getProfit(),
                simulator.getAssets(),
                simulator.isBankrupt(),
                simulator.isInputError()
        );
    }

    @Override
    public void tick() {
        if (!alive) return;
    }

    public boolean isAlive() {
        // if return false, server will restart game - avoiding this behavior
        return true;
    }

    private boolean isGameOver() {
        return gameSettings.getLimitDays() != 0 && gameSettings.getLimitDays() < simulator.getDay();
    }

    public Question getNextQuestion() {
        int day = simulator.getDay();
        double lemonadeCost = simulator.getLemonadeCost();
        double assets = simulator.getAssets();
        WeatherForecast weatherForecast = Enum.valueOf(WeatherForecast.class, simulator.getWeatherForecast().replace(' ', '_'));
        boolean isBankrupt = simulator.isBankrupt();
        boolean isGameOver = isGameOver();

        serverMessagesManager.setGameOver(isGameOver);
        serverMessagesManager.setMessages(
                simulator.getStatusMessages(),
                simulator.getReportMessages(),
                simulator.getMorningMessages()
        );
        String messages = serverMessagesManager.getMessages();

        return new Question(day,
                lemonadeCost,
                assets,
                weatherForecast,
                messages,
                isBankrupt,
                isBankrupt || isGameOver);
    }

    private void simulate(int lemonadeToMake, int signsToMake, int lemonadePriceCents) {
        simulator.step(lemonadeToMake, signsToMake, lemonadePriceCents);
        //TODO
    }

    public SalesResult popSalesResult() {
        SalesResult result = this.salesResult;
        this.salesResult = null;
        return result;
    }
}
