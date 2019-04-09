package com.codenjoy.dojo.lemonade.services;

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


import com.codenjoy.dojo.client.ClientBoard;
import com.codenjoy.dojo.client.Solver;
import com.codenjoy.dojo.lemonade.client.Board;
import com.codenjoy.dojo.lemonade.client.ai.AISolver;
import com.codenjoy.dojo.lemonade.model.*;
import com.codenjoy.dojo.services.*;
import com.codenjoy.dojo.services.multiplayer.GamePlayer;
import com.codenjoy.dojo.services.multiplayer.MultiplayerType;
import com.codenjoy.dojo.services.printer.BoardReader;
import com.codenjoy.dojo.services.printer.PrinterFactory;
import com.codenjoy.dojo.services.settings.Parameter;
import org.json.JSONObject;

import static com.codenjoy.dojo.services.settings.SimpleParameter.v;

/**
 * Генератор игор - реализация {@see GameType}
 */
public class GameRunner extends AbstractGameType implements GameType {

    private final Level level;
    private Lemonade game;

    public GameRunner() {
        new Scores(0, settings);
        level = new LevelImpl(
                "question1=answer1",
                "question2=answer2",
                "question3=answer3",
                "question4=answer4",
                "question5=answer5",
                "question6=answer6",
                "question7=answer7",
                "question8=answer8",
                "question9=answer9",
                "question10=answer10",
                "question11=answer11",
                "question12=answer12",
                "question13=answer13",
                "question14=answer14",
                "question15=answer15",
                "question16=answer16",
                "question17=answer17",
                "question18=answer18",
                "question19=answer19",
                "question20=answer20"
        );
    }

    @Override
    public Lemonade createGame(int levelNumber) {
        return new Lemonade(level, getDice());
    }

    @Override
    public PlayerScores getPlayerScores(Object score) {
        return new Scores((Integer)score, settings);
    }

    @Override
    public Parameter<Integer> getBoardSize() {
        return v(25);
    }

    @Override
    public String name() {
        return "lemonade";
    }

    @Override
    public Enum[] getPlots() {
        return WeatherForecast.values();
    }

    @Override
    public Class<? extends Solver> getAI() {
        return AISolver.class;
    }

    @Override
    public Class<? extends ClientBoard> getBoard() {
        return Board.class;
    }

    @Override
    public MultiplayerType getMultiplayerType() {
        return MultiplayerType.SINGLE;
    }

    @Override
    public GamePlayer createPlayer(EventListener listener, String playerName) {
        return new Player(listener);
    }

    @Override
    public PrinterFactory getPrinterFactory() {
        return PrinterFactory.get((BoardReader reader, Player player) -> {
            JSONObject result = player.getNextQuestion();
            result.put("history", player.getHistoryJson());
            //System.out.println(result.toString());
            return result;
        });
    }
}
