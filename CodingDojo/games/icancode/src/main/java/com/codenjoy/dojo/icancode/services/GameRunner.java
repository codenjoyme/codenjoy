package com.codenjoy.dojo.icancode.services;

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
import com.codenjoy.dojo.games.icancode.Element;
import com.codenjoy.dojo.icancode.model.ICanCode;
import com.codenjoy.dojo.icancode.model.Level;
import com.codenjoy.dojo.icancode.model.Player;
import com.codenjoy.dojo.services.AbstractGameType;
import com.codenjoy.dojo.services.EventListener;
import com.codenjoy.dojo.services.PlayerScores;
import com.codenjoy.dojo.services.Point;
import com.codenjoy.dojo.services.multiplayer.GameField;
import com.codenjoy.dojo.services.multiplayer.GamePlayer;
import com.codenjoy.dojo.services.multiplayer.MultiplayerType;
import com.codenjoy.dojo.services.printer.BoardReader;
import com.codenjoy.dojo.services.printer.CharElement;
import com.codenjoy.dojo.services.printer.PrinterFactory;
import com.codenjoy.dojo.services.printer.layeredview.PrinterData;
import com.codenjoy.dojo.services.settings.Parameter;
import org.json.JSONObject;

import static com.codenjoy.dojo.icancode.services.GameSettings.*;
import static com.codenjoy.dojo.icancode.services.GameSettings.Keys.*;
import static com.codenjoy.dojo.services.multiplayer.MultiplayerType.*;

public class GameRunner extends AbstractGameType<GameSettings> {

    @Override
    public GameSettings getSettings() {
        return new GameSettings();
    }

    @Override
    public PlayerScores getPlayerScores(Object score, GameSettings settings) {
        return new Scores(Integer.valueOf(score.toString()), settings);
    }

    @Override
    public GameField createGame(int levelNumber, GameSettings settings) {
        Level level = loadLevel(levelNumber, settings);
        boolean isSingle = levelNumber < getMultiplayerType(settings).getLevelsCount();
        boolean contest = isSingle ? ICanCode.TRAINING : ICanCode.CONTEST;
        return new ICanCode(level, getDice(), contest, settings);
    }

    @Override
    public Parameter<Integer> getBoardSize(GameSettings settings) {
        return settings.integerValue(VIEW_SIZE);
    }

    @Override
    public String name() {
        return "icancode";
    }

    @Override
    public MultiplayerType getMultiplayerType(GameSettings settings) {
        int count = Levels.all().size();
        int roomSize = settings.roomSize();

        switch (settings.string(GAME_MODE)) {
            default:
            case CLASSIC_TRAINING:
                return TRAINING.apply(count);

            case ALL_SINGLE:
                return SINGLE_LEVELS.apply(count);

            case ALL_IN_ROOMS:
                return MULTIPLE_LEVELS.apply(roomSize, count);

            case TRAINING_MULTIMAP:
                return MULTIPLE_LEVELS_MULTIROOM.apply(roomSize, count);
        }
    }

    public Level loadLevel(int level, GameSettings settings) {
        return Levels.loadLevel(level, settings);
    }

    @Override
    public CharElement[] getPlots() {
        return Element.values();
    }

    @Override
    public Class<? extends Solver> getAI() {
        // TODO to use AI solver here
        return null;
    }

    @Override
    public Class<? extends ClientBoard> getBoard() {
        return null;
    }

    @Override
    public GamePlayer createPlayer(EventListener listener, int teamId, String playerId, GameSettings settings) {
        if (settings.bool(IS_TRAINING_MODE)) { // TODO найти как это загрузить
//            int total = Levels.collectSingle().size();
//            save = "{'total':" + total + ",'current':0,'lastPassed':" + (total - 1) + ",'multiple':true}";
        }
        return new Player(listener, settings).inTeam(teamId);
    }

    @Override
    public PrinterFactory getPrinterFactory() {
        return PrinterFactory.get((BoardReader reader, Player player) -> {
            PrinterData data = player.getPrinter().print();

            JSONObject result = new JSONObject();
            result.put("layers", data.getLayers());
            // do not change 'offset' key - canvases working
            result.put("offset", toJson(data.getOffset()));
            result.put("heroPosition", toJson(player.getHeroOffset(data.getOffset())));
            result.put("levelFinished", player.isLevelFinished());
            result.put("showName", true);
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
