package com.codenjoy.dojo.expansion.services;

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
import com.codenjoy.dojo.expansion.client.Board;
import com.codenjoy.dojo.expansion.client.ai.ApofigBotSolver;
import com.codenjoy.dojo.expansion.model.*;
import com.codenjoy.dojo.expansion.model.levels.Level;
import com.codenjoy.dojo.expansion.model.levels.Levels;
import com.codenjoy.dojo.expansion.model.levels.LevelsFactory;
import com.codenjoy.dojo.expansion.model.replay.GameLoggerImpl;
import com.codenjoy.dojo.services.*;
import com.codenjoy.dojo.services.multiplayer.GameField;
import com.codenjoy.dojo.services.multiplayer.GamePlayer;
import com.codenjoy.dojo.services.multiplayer.MultiplayerType;
import com.codenjoy.dojo.services.printer.BoardReader;
import com.codenjoy.dojo.services.printer.CharElements;
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
    private int size;
    private LevelsFactory singleLevels;
    private LevelsFactory multipleLevels;

    public GameRunner() {
        SettingsWrapper.setup(settings);

        dice = getDice();
    }

    private void initGameFactory() {
        if (singleLevels == null || settings.changed()) {
            settings.changesReacted();

            size = SettingsWrapper.data.boardSize();

            singleLevels = Levels.collectLevels(SettingsWrapper.data.boardSize(),
                    SettingsWrapper.data.singleLevels());

            multipleLevels = Levels.collectLevels(SettingsWrapper.data.boardSize(),
                    SettingsWrapper.data.multipleLevels());
        }
    }

    @Override
    public MultiplayerType getMultiplayerType() {
        initGameFactory();
        if (SettingsWrapper.data.singleTrainingMode()) {
            return MultiplayerType.TRAINING.apply(singleLevels.get().size());
        } else {
            return MultiplayerType.QUADRO;
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

        initGameFactory();

        if (SettingsWrapper.data.singleTrainingMode()) {
            boolean isSingle = levelNumber < getMultiplayerType().getLevelsCount();
            if (isSingle) {
                Level level = singleLevels.get().get(levelNumber);
                return new Expansion(level, new Ticker(), dice,
                        new GameLoggerImpl(), Expansion.SINGLE);
            }
        }

        List<Level> levels = multipleLevels.get();
        Level level = levels.get(dice.next(levels.size()));
        return new Expansion(level, new Ticker(), dice,
                new GameLoggerImpl(), Expansion.MULTIPLE);
    }

    @Override
    public GamePlayer createPlayer(EventListener listener, String playerId) {
        if (logger.isDebugEnabled()) {
            logger.debug("Creating GamePlayer for {}", playerId);
        }

        // TODO понять как достать тут сейв
//        if (ReplayGame.isReplay(save)) {
//            return new ReplayGame(new JSONObject(save));
//        }

//        String save = null;
//        boolean isTrainingMode = false; // TODO load from game_settings via GameDataController
//        if (!isTrainingMode) {
//            int total = SettingsWrapper.data.singleLevels().size();
//            save = "{'total':" + total + ",'current':0,'lastPassed':" + (total - 1) + ",'multiple':true}";
//        }

        Player player = new Player(listener, playerId);

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
    public CharElements[] getPlots() {
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

            JSONObject result = new JSONObject();
            List<String> layers = data.getLayers();
            String forces = layers.remove(2);
            result.put("layers", layers);
            result.put("forces", forces);
            result.put("myBase", new JSONObject(player.getBasePosition()));
            result.put("myColor", player.getForcesColor());
            result.put("tick", player.getField().ticker());
            result.put("round", player.getRoundTicks());
            result.put("rounds", SettingsWrapper.data.roundTicks());
            result.put("available", player.getForcesPerTick());
            result.put("offset", new JSONObject(data.getOffset()));

            if (logger.isDebugEnabled()) {
                logger.debug("getBoardAsString for player {} and field {} prepare {}",
                        player.lg.id(),
                        player.getField().id(),
                        JsonUtils.toStringSorted(result));
            }

            return result;
        });
    }
}
