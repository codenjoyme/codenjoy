package com.codenjoy.dojo.battlecity.services;

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


import com.codenjoy.dojo.battlecity.client.Board;
import com.codenjoy.dojo.battlecity.client.ai.AISolver;
import com.codenjoy.dojo.battlecity.model.Battlecity;
import com.codenjoy.dojo.battlecity.model.Elements;
import com.codenjoy.dojo.battlecity.model.Player;
import com.codenjoy.dojo.battlecity.model.levels.Level;
import com.codenjoy.dojo.battlecity.model.levels.LevelImpl;
import com.codenjoy.dojo.client.ClientBoard;
import com.codenjoy.dojo.client.Solver;
import com.codenjoy.dojo.services.*;
import com.codenjoy.dojo.services.multiplayer.GameField;
import com.codenjoy.dojo.services.multiplayer.GamePlayer;
import com.codenjoy.dojo.services.multiplayer.MultiplayerType;
import com.codenjoy.dojo.services.printer.CharElements;
import com.codenjoy.dojo.services.settings.Parameter;

import static com.codenjoy.dojo.services.settings.SimpleParameter.v;

public class GameRunner extends AbstractGameType implements GameType {

    private GameSettings gameSettings;

    public GameRunner() {
        gameSettings = getGameSettings();
    }

    @Override
    public PlayerScores getPlayerScores(Object score) {
        return new Scores((Integer) score, gameSettings);
    }

    @Override
    public GameField createGame(int levelNumber) {
        Level level = getLevel();
        Battlecity game = new Battlecity(level.size(), getDice(),
                gameSettings.spawnAiPrize(),
                gameSettings.hitKillsAiPrize());

        game.addBorder(level.getBorders());
        game.addWall(level.getWalls());
        game.addAiTanks(level.getAiTanks());
        game.addRiver(level.getRivers());
        game.addTree(level.getTrees());
        game.addIce(level.getIce());
        return game;
    }

    public Level getLevel() {
        return new LevelImpl(getMap(), getDice());
    }

    @Override
    public Parameter<Integer> getBoardSize() {
        // TODO взять size из другого места
        return v(getLevel().size());
    }

    @Override
    public String name() {
        return "battlecity";
    }

    @Override
    public CharElements[] getPlots() {
        return Elements.values();
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
        return MultiplayerType.MULTIPLE;
    }

    @Override
    public GamePlayer createPlayer(EventListener listener, String playerId) {
        return new Player(listener, getDice());
    }

    public String getMap() {
        return
                "☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼" +
                "☼ ¿    ¿    ¿        ¿    ¿    ¿ ☼" +
                "☼                                ☼" +
                "☼  ╬╬╬  ╬╬╬  ╬╬╬  ╬╬╬  ╬╬╬  ╬╬╬  ☼" +
                "☼ █╬╬╬█ ╬╬╬ █╬╬╬██╬╬╬█ ╬╬╬ █╬╬╬█ ☼" +
                "☼ █╬╬╬█ ╬╬╬ █╬╬╬██╬╬╬█ ╬╬╬ █╬╬╬█ ☼" +
                "☼ █╬╬╬█ ╬╬╬ █╬╬╬██╬╬╬█ ╬╬╬ █╬╬╬█ ☼" +
                "☼ █╬╬╬█ ╬╬╬ █╬╬╬☼☼╬╬╬█ ╬╬╬ █╬╬╬█ ☼" +
                "☼ █╬╬╬█ ╬╬╬ █╬╬╬☼☼╬╬╬█ ╬╬╬ █╬╬╬█ ☼" +
                "☼ █╬╬╬█ ╬╬╬ █╬╬╬  ╬╬╬█ ╬╬╬ █╬╬╬█ ☼" +
                "☼ █╬╬╬█ ╬╬╬  ╬╬╬  ╬╬╬  ╬╬╬ █╬╬╬█ ☼" +
                "☼ █╬╬╬█ ╬╬╬            ╬╬╬ █╬╬╬█ ☼" +
                "☼  ╬╬╬  ╬╬╬   ▓    ▓   ╬╬╬  ╬╬╬  ☼" +
                "☼  ▓▓▓       ╬╬╬  ╬╬╬       ▓▓▓  ☼" +
                "☼  ▓▓        ╬╬╬  ╬╬╬        ▓▓  ☼" +
                "☼     ╬╬╬╬╬  ╬╬╬  ╬╬╬  ╬╬╬╬╬     ☼" +
                "☼☼☼   ╬╬╬╬╬            ╬╬╬╬╬   ☼☼☼" +
                "☼ ▓▓          ▒▒▒▒▒▒          ▓▓ ☼" +
                "☼           ▓╬╬╬▒▒╬╬╬▓           ☼" +
                "☼  ╬╬╬  ╬╬╬ ▓╬╬╬▒▒╬╬╬▓ ╬╬╬  ╬╬╬  ☼" +
                "☼  ╬╬╬  ╬╬╬  ╬╬╬  ╬╬╬  ╬╬╬  ╬╬╬  ☼" +
                "☼  ╬╬╬▓ ╬╬╬  ╬╬╬╬╬╬╬╬  ╬╬╬ ▓╬╬╬  ☼" +
                "☼  ╬╬╬  ╬╬╬  ╬╬╬╬╬╬╬╬  ╬╬╬  ╬╬╬  ☼" +
                "☼ ▒╬╬╬  ╬╬╬  ╬╬╬▒▒╬╬╬  ╬╬╬  ╬╬╬▒ ☼" +
                "☼ ▒╬╬╬  ╬╬╬▓ ╬╬╬▒▒╬╬╬ ▓╬╬╬  ╬╬╬▒ ☼" +
                "☼ ▒╬╬╬  ╬╬╬▓ ╬╬╬▒▒╬╬╬ ▓╬╬╬  ╬╬╬▒ ☼" +
                "☼ ▒╬╬╬ ▓╬╬╬  ╬╬╬▒▒╬╬╬  ╬╬╬▓ ╬╬╬▒ ☼" +
                "☼ ▒╬╬╬  ▒▒▒            ▒▒▒  ╬╬╬▒ ☼" +
                "☼  ╬╬╬  ▒▒▒    ▓▓▓▓    ▒▒▒  ╬╬╬  ☼" +
                "☼  ╬╬╬  ▒▒▒  ╬╬╬╬╬╬╬╬  ▒▒▒  ╬╬╬  ☼" +
                "☼  ╬╬╬       ╬╬╬╬╬╬╬╬       ╬╬╬  ☼" +
                "☼            ╬╬    ╬╬            ☼" +
                "☼  ▒▒▒▒▒▒    ╬╬    ╬╬    ▒▒▒▒▒▒  ☼" +
                "☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼";
    }

    protected GameSettings getGameSettings() {
        return new OptionGameSettings(settings, getDice());
    }
}
