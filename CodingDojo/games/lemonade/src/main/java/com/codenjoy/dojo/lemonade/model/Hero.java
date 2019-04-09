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
import com.codenjoy.dojo.services.multiplayer.PlayerHero;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Это реализация героя. Обрати внимание, что он имплементит {@see Joystick}, а значит может быть управляем фреймворком
 * Так же он имплементит {@see Tickable}, что значит - есть возможность его оповещать о каждом тике игры.
 * Эти интерфейсы объявлены в {@see PlayerHero}.
 */
public class Hero extends PlayerHero<Field> implements MessageJoystick {

    private static Pattern patternGo;
    private Simulator simulator;
    private SalesResult salesResult;
    private boolean alive;
    private String answer;

    static {
        patternGo = Pattern.compile(
                "go\\s*(-?[\\d]+)[,\\s]\\s*(-?[\\d]+)[,\\s]\\s*(-?[\\d]+)", Pattern.CASE_INSENSITIVE);
    }

    public Hero() {
        simulator = new Simulator((int) System.currentTimeMillis());
        alive = true;
    }

    @Override
    public void init(Field field) {
        simulator.reset();
        //simulator.step(0,0,0);

        this.field = field;
    }

    @Override
    public void message(String s) {
        this.answer = s;

        String command = s.toLowerCase();

        if(command.contains("reset")) {
            simulator.reset();
            return;
        }

        Matcher matcher = patternGo.matcher(command);
        if (matcher.matches()) {
            int lemonadeToMake = Integer.parseInt(matcher.group(1));
            int signsToMake = Integer.parseInt(matcher.group(2));
            int lemonadePriceCents = Integer.parseInt(matcher.group(3));
            simulate(lemonadeToMake, signsToMake, lemonadePriceCents);
            readSalesResult();
            return;
        }
    }

    private void readSalesResult() {
        int day = simulator.getDay();
        int lemonadeSold = simulator.getLemonadeSold();
        double lemonadePrice = simulator.getLemonadePrice();
        double income = simulator.getIncome();
        int lemonadeMade = simulator.getLemonadeMade();
        int signsMade = simulator.getSignsMade();
        double expenses = simulator.getExpenses();
        double profit = simulator.getProfit();
        double assets = simulator.getAssets();
        boolean isBunkrupt = simulator.isBankrupt();
        this.salesResult = new SalesResult(day,
                lemonadeSold,
                lemonadePrice,
                income,
                lemonadeMade,
                signsMade,
                expenses,
                profit,
                assets,
                isBunkrupt
                );
    }

    @Override
    public void tick() {
        if (!alive) return;
    }

    public boolean isAlive() {
        return alive;
    }

    public String popAnswer() {
        String answer = this.answer;
        this.answer = null;
        return answer;
    }

    public Question getNextQuestion(){
        int day = simulator.getDay();
        double lemonadePrice = simulator.getLemonadePrice();
        double assets = simulator.getAssets();
        WeatherForecast weatherForecast = Enum.valueOf(WeatherForecast.class, simulator.getWeatherForecast().replace(' ', '_'));
        String messages = simulator.getMessages();
        return new Question(day,
                lemonadePrice,
                assets,
                weatherForecast,
                messages);
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
