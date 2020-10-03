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
import com.codenjoy.dojo.services.printer.*;
import com.codenjoy.dojo.services.settings.Parameter;
import com.codenjoy.dojo.services.settings.Settings;
import com.codenjoy.dojo.tetris.client.Board;
import com.codenjoy.dojo.tetris.client.ai.AISolver;
import com.codenjoy.dojo.tetris.model.*;
import com.codenjoy.dojo.tetris.model.levels.LevelsFactory;
import com.codenjoy.dojo.tetris.model.levels.level.ProbabilityWithoutOverflownLevels;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class GameRunner extends AbstractGameType implements GameType {

    private Parameter<String> gameLevels;
    private Parameter<Integer> glassSize;

    public GameRunner() {
        gameLevels = settings.addSelect("Game Levels", (List)levels())
                .type(String.class)
                .def(ProbabilityWithoutOverflownLevels.class.getSimpleName());
        glassSize = settings.addEditBox("Glass Size").type(Integer.class).def(18);
    }

    private List<String> levels() {
        LevelsFactory factory = new LevelsFactory();
        return factory.allLevels();
    }

    @Override
    public PlayerScores getPlayerScores(Object score) {
        return new Scores((Integer)score, settings);
    }

    @Override
    public GameField createGame(int level) {
        Figures queue = new Figures();
        Levels levels = loadLevelsFor(queue, gameLevels.getValue());
        levels.gotoLevel(level - LevelProgress.levelsStartsFrom1);
        return new Tetris(levels, queue, glassSize.getValue());
    }

    private Levels loadLevelsFor(FigureQueue queue, String levelName) {
        return new LevelsFactory().createLevels(levelName, getDice(), queue);
    }

    @Override
    public Parameter<Integer> getBoardSize() {
        return glassSize;
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
    public Settings getSettings() {
        return settings;
    }

    @Override
    public MultiplayerType getMultiplayerType() {
        // TODO слишком много тут делается для получения количества уровней
        Levels levels = loadLevelsFor(NullFigureQueue.INSTANCE, gameLevels.getValue());

        return MultiplayerType.SINGLE_LEVELS.apply(levels.count());
    }

    @Override
    public GamePlayer createPlayer(EventListener listener, String playerId) {
        return new Player(listener);
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
        PrinterFactoryImpl graphic = new PrinterFactoryImpl();

        return PrinterFactory.get((BoardReader reader, Player player) -> {
            JSONObject result = new JSONObject();

            Hero hero = player.getHero();

            Printer<String> graphicPrinter = graphic.getPrinter(new BoardReader() {
                @Override
                public int size() {
                    return hero.boardSize();
                }

                @Override
                public Iterable<? extends Point> elements() {
                    return new LinkedList<Point>() {{
                        List<Plot> droppedPlots = hero.dropped();
                        List<Plot> currentFigurePlots = hero.currentFigure();
                        droppedPlots.removeAll(currentFigurePlots);
                        addAll(droppedPlots);
                        addAll(currentFigurePlots);
                    }};
                }
            }, player);
            String board = graphicPrinter.print().replace("\n", "").replace(" ", ".");
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
