package com.codenjoy.dojo.expansion.services;

/*-
 * #%L
 * iCanCode - it's a dojo-like platform from developers to developers.
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
import com.codenjoy.dojo.expansion.client.Board;
import com.codenjoy.dojo.expansion.client.ai.ApofigBotSolver;
import com.codenjoy.dojo.expansion.model.Elements;
import com.codenjoy.dojo.expansion.model.MultipleGameFactory;
import com.codenjoy.dojo.expansion.model.Single;
import com.codenjoy.dojo.expansion.model.Ticker;
import com.codenjoy.dojo.expansion.model.levels.Levels;
import com.codenjoy.dojo.expansion.model.lobby.PlayerLobby;
import com.codenjoy.dojo.expansion.model.replay.ReplayGame;
import com.codenjoy.dojo.services.*;
import com.codenjoy.dojo.services.multiplayer.GameField;
import com.codenjoy.dojo.services.multiplayer.GamePlayer;
import com.codenjoy.dojo.services.printer.PrinterFactory;
import com.codenjoy.dojo.services.settings.Parameter;
import org.json.JSONObject;
import org.slf4j.Logger;

import java.util.LinkedList;
import java.util.List;

import static com.codenjoy.dojo.services.settings.SimpleParameter.v;

/**
 * Генератор игор - реализация {@see GameType}
 * Обрати внимание на {@see GameRunner#SINGLE} - там реализовано переключение в режимы "все на одном поле"/"каждый на своем поле"
 */
public class GameRunner extends AbstractGameType implements GameType  {

    private static Logger logger = DLoggerFactory.getLogger(GameRunner.class);

    private Dice dice;
    private MultipleGameFactory gameFactory;
    private Ticker ticker;
    private int size;
    protected PlayerLobby lobby;
    protected List<Game> games;

    public GameRunner() {
        SettingsWrapper.setup(settings);

        ticker = new Ticker();
        dice = new RandomDice();
        games = new LinkedList<>();
    }

    private void initGameFactory() {
        if (gameFactory == null || settings.changed()) {
            settings.changesReacted();

            size = SettingsWrapper.data.boardSize();
            gameFactory = new MultipleGameFactory(dice,
                    Levels.collectSingle(SettingsWrapper.data.boardSize()),
                    Levels.collectMultiple(SettingsWrapper.data.boardSize(),
                            SettingsWrapper.data.levels().toArray(new String[0]))
            );
            loadAnotherLobby();
        }
    }

    private void loadAnotherLobby() {
        PlayerLobby newLobby = SettingsWrapper.data.getPlayerLobby(gameFactory);
        if (lobby != null) {
            lobby.saveStateTo(newLobby);
        }
        lobby = newLobby;
    }

    @Override
    public PlayerScores getPlayerScores(Object score) {
        return new Scores(score);
    }

    @Override
    public GameField createGame(int levelNumber) {
        return null; // TODO разрулить как-то это все
    }

    public Game newGame(EventListener listener, PrinterFactory factory, String save, String playerName) {
        if (logger.isDebugEnabled()) {
            logger.debug("Starting new game with save {}", save);
        }
        initGameFactory();

        Game game = null;
        if (!ReplayGame.isReplay(save)) {
            boolean isTrainingMode = false; // TODO load from game_settings via GameDataController
            if (!isTrainingMode) {
                int total = SettingsWrapper.data.totalSingleLevels();
                save = "{'total':" + total + ",'current':0,'lastPassed':" + (total - 1) + ",'multiple':true}";
            }

            // TODO разрулить как-то это все
//            game = new Single(gameFactory, () -> lobby, listener, factory, ticker, dice, save, playerName);
            game.newGame();
        } else {
//            game = new ReplayGame(new JSONObject(save));
        }

        games.add(game);
        return game;
    }

    @Override
    public GamePlayer createPlayer(EventListener listener, String playerName) {
        return null; // TODO разрулить как-то это все
    }

    @Override
    public Parameter<Integer> getBoardSize() {
        return v(size);
    }

    @Override
    public String name() {
        return "expansion";
    }

    @Override
    public Enum[] getPlots() {
        return Elements.values();
    }

    @Override
    public Class<? extends Solver> getAI() {
        return ApofigBotSolver.class;
    }

    @Override
    public Class<? extends ClientBoard> getBoard() {
        return Board.class;
    }

    public void setDice(Dice dice) {
        this.dice = dice;
    }

    @Override
    public void tick() {
        processAdminCommands();
        initGameFactory();
        lobby.tick();
    }

    private void processAdminCommands() {
        new CommandParser(this).parse(settings);
    }
}
