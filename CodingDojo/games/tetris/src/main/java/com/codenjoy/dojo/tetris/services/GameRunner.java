package com.codenjoy.dojo.tetris.services;

/*-
 * #%L
 * Codenjoy - it's a dojo-like platform from developers to developers.
 * %%
 * Copyright (C) 2016 Codenjoy
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
import com.codenjoy.dojo.services.*;
import com.codenjoy.dojo.services.multiplayer.GameField;
import com.codenjoy.dojo.services.multiplayer.GamePlayer;
import com.codenjoy.dojo.services.multiplayer.LevelProgress;
import com.codenjoy.dojo.services.multiplayer.MultiplayerType;
import com.codenjoy.dojo.services.printer.BoardReader;
import com.codenjoy.dojo.services.printer.CharElements;
import com.codenjoy.dojo.services.printer.Printer;
import com.codenjoy.dojo.services.printer.PrinterFactory;
import com.codenjoy.dojo.services.settings.Parameter;
import com.codenjoy.dojo.tetris.client.Board;
import com.codenjoy.dojo.tetris.client.ai.AISolver;
import com.codenjoy.dojo.tetris.model.*;
import com.codenjoy.dojo.tetris.model.levels.LevelsFactory;
import com.codenjoy.dojo.tetris.services.scores.CumulativeScores;
import com.codenjoy.dojo.tetris.services.scores.MaxScores;
import com.codenjoy.dojo.utils.LevelUtils;
import org.json.JSONObject;

import java.util.Arrays;

import static com.codenjoy.dojo.tetris.services.GameSettings.Keys.*;

public class GameRunner extends AbstractGameType<GameSettings> {

    @Override
    public GameSettings getSettings() {
        return new GameSettings();
    }

    @Override
    public PlayerScores getPlayerScores(Object score, GameSettings settings) {
        Integer initial = Integer.valueOf(score.toString());
        if (settings.bool(SCORE_MODE)) {
            return new CumulativeScores(initial, settings);
        } else {
            return new MaxScores(initial, settings);
        }
    }

    @Override
    public GameField createGame(int level, GameSettings settings) {
        Figures queue = new Figures();
        Levels levels = loadLevelsFor(queue, settings.string(GAME_LEVELS));
        levels.gotoLevel(level - LevelProgress.levelsStartsFrom1);
        return new Tetris(levels, queue, settings.integer(GLASS_SIZE), settings);
    }

    private Levels loadLevelsFor(FigureQueue queue, String levelsType) {
        return new LevelsFactory().createLevels(levelsType, getDice(), queue);
    }

    @Override
    public Parameter<Integer> getBoardSize(GameSettings settings) {
        return settings.integerValue(GLASS_SIZE);
    }

    @Override
    public String name() {
        return "tetris";
    }

    @Override
    public CharElements[] getPlots() {
        return Elements.values();
    }

    @Override
    public MultiplayerType getMultiplayerType(GameSettings settings) {
        // TODO слишком много тут делается для получения количества уровней
        String levelsType = settings.string(GAME_LEVELS);
        Levels levels = loadLevelsFor(NullFigureQueue.INSTANCE, levelsType);

        return MultiplayerType.SINGLE_LEVELS.apply(levels.count());
    }

    @Override
    public GamePlayer createPlayer(EventListener listener, String playerId, GameSettings settings) {
        return new Player(listener, settings);
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
    public PrinterFactory getPrinterFactory() {
        return PrinterFactory.get((BoardReader reader, Printer<String> printer, Player player) -> {
            String data = printer.print();
            String board = LevelUtils.clear(data).replace(" ", ".");

            Hero hero = player.getHero();

            JSONObject result = new JSONObject();

            result.put("layers", Arrays.asList(board));

            result.put("currentFigureType", hero.currentFigureType());

            Point point = hero.currentFigurePoint();
            result.put("currentFigurePoint",
                    (point == null) ? null : new JSONObject(new PointImpl(point)));

            result.put("futureFigures", hero.future());

            return result;
        });
    }
}
