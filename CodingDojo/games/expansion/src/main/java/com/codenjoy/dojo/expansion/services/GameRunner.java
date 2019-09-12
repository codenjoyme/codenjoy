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
import com.codenjoy.dojo.expansion.model.*;
import com.codenjoy.dojo.expansion.model.levels.Levels;
import com.codenjoy.dojo.services.*;
import com.codenjoy.dojo.services.multiplayer.GameField;
import com.codenjoy.dojo.services.multiplayer.GamePlayer;
import com.codenjoy.dojo.services.printer.BoardReader;
import com.codenjoy.dojo.services.printer.PrinterFactory;
import com.codenjoy.dojo.services.printer.layeredview.PrinterData;
import com.codenjoy.dojo.services.settings.Parameter;
import com.codenjoy.dojo.utils.JsonUtils;
import org.json.JSONObject;
import org.slf4j.Logger;

import java.util.List;

import static com.codenjoy.dojo.services.settings.SimpleParameter.v;

public class GameRunner extends AbstractGameType implements GameType  {

    private static Logger logger = DLoggerFactory.getLogger(GameRunner.class);

    private Dice dice;
    private MultipleGameFactory gameFactory;
    private Ticker ticker;
    private int size;

    public GameRunner() {
        SettingsWrapper.setup(settings);

        ticker = new Ticker();
        dice = getDice();

        initGameFactory();
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
        }
    }

    @Override
    public PlayerScores getPlayerScores(Object score) {
        return new Scores(score);
    }

    @Override
    public GameField createGame(int levelNumber) {
        if (logger.isDebugEnabled()) {
            logger.debug("Creating GameField for {}", levelNumber);
        }

// TODO понять как достать тут сейв
//        if (ReplayGame.isReplay(save)) {
//            return new ReplayGame(new JSONObject(save));
//        }

        return gameFactory.single();
    }

    @Override
    public GamePlayer createPlayer(EventListener listener, String playerName) {
        if (logger.isDebugEnabled()) {
            logger.debug("Creating GamePlayer for {}", playerName);
        }

        String save = null;
        boolean isTrainingMode = false; // TODO load from game_settings via GameDataController
        if (!isTrainingMode) {
            int total = SettingsWrapper.data.totalSingleLevels();
            save = "{'total':" + total + ",'current':0,'lastPassed':" + (total - 1) + ",'multiple':true}";
        }

        ProgressBar progressBar = new ProgressBar(gameFactory);
        Player player = new Player(listener, progressBar, playerName);

        if (logger.isDebugEnabled()) {
            logger.debug("Starts new game for {}", player.lg.id());
        }

        progressBar.start(save);
        return player;
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
    }

    private void processAdminCommands() {
        new CommandParser(this).parse(settings);
    }

    @Override
    public PrinterFactory getPrinterFactory() {
        return PrinterFactory.get((BoardReader reader, Player player) -> {
            PrinterData data = player.getPrinter().print();
            ProgressBar progressBar = player.getProgress();

            JSONObject result = new JSONObject();
            List<String> layers = data.getLayers();
            String forces = layers.remove(2);
            result.put("layers", layers);
            result.put("forces", forces);
            result.put("myBase", new JSONObject(player.getBasePosition()));
            result.put("myColor", player.getForcesColor());
            result.put("tick", ticker.get());
            result.put("round", progressBar.getRoundTicks());
            result.put("rounds", SettingsWrapper.data.roundTicks());
            result.put("available", player.getForcesPerTick());
            result.put("offset", data.getOffset());
            JSONObject progress = progressBar.printProgress();
            result.put("showName", true);
            result.put("onlyMyName", !progress.getBoolean("multiple"));
            result.put("levelProgress", progress);

            if (logger.isDebugEnabled()) {
                logger.debug("getBoardAsString for game {} prepare {}",
                        progressBar.lg.id(),
                        JsonUtils.toStringSorted(result));
            }

            return result;
        });
    }

    private JSONObject toJson(Point point) {
        JSONObject result = new JSONObject();
        result.put("x", point.getX());
        result.put("y", point.getY());
        return result;
    }
}
