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
import com.codenjoy.dojo.battlecity.model.levels.LevelImpl;
import com.codenjoy.dojo.battlecity.model.levels.Level;
import com.codenjoy.dojo.client.ClientBoard;
import com.codenjoy.dojo.client.Solver;
import com.codenjoy.dojo.services.*;
import com.codenjoy.dojo.services.multiplayer.GameField;
import com.codenjoy.dojo.services.multiplayer.GamePlayer;
import com.codenjoy.dojo.services.multiplayer.MultiplayerType;
import com.codenjoy.dojo.services.settings.Parameter;
import org.apache.commons.lang3.StringUtils;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.List;

import static com.codenjoy.dojo.services.settings.SimpleParameter.v;

public class GameRunner extends AbstractGameType implements GameType {

    public static final String NUMBER_OF_PLAYERS = "Number of players for team";
    public static final String IS_NEED_AI = "Is need AI";
    public static final String CUSTOM_MAP_PATH = "Custom map path";
    public static final String MULTIPLAYER_TYPE = "Multiplayer Type";

    private Level level;
    private Parameter<String> multiplayerType;
    private Parameter<Integer> numberOfPlayers;
    private Parameter<Boolean> needAI;
    private Parameter<String> customMapPath;

    public GameRunner() {
        new Scores(0, settings); // TODO сеттринги разделены по разным классам, продумать архитектуру
        needAI = settings.addCheckBox(IS_NEED_AI).type(Boolean.class).def(true);
        numberOfPlayers = settings.addEditBox(NUMBER_OF_PLAYERS).type(Integer.class).def(10);
        customMapPath = settings.addEditBox(CUSTOM_MAP_PATH).type(String.class).def("");
        multiplayerType = settings.addSelect(MULTIPLAYER_TYPE, Arrays.asList("MULTIPLAYER", "TEAM"))
                .type(String.class)
                .def("TEAM");
        level = new LevelImpl(MapLoader.loadMapFromFile(customMapPath.getValue()), getDice());
    }

    @Override
    public PlayerScores getPlayerScores(Object score) {
        return new Scores((Integer) score, settings);
    }

    @Override
    public GameField createGame(int levelNumber) {
        return new Battlecity(level,
                getDice(),
                settings);
    }

    @Override
    public Parameter<Integer> getBoardSize() {
        return v(level.getSize());
    }

    @Override
    public String name() {
        return "battlecity";
    }

    @Override
    public Enum[] getPlots() {
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
        switch (multiplayerType.getValue()){
            case "MULTIPLAYER":
                return MultiplayerType.MULTIPLE;
            case "TEAM": {
                return MultiplayerType.TEAM.apply(numberOfPlayers.getValue(), !MultiplayerType.DISPOSABLE);
            }
            default:
                return MultiplayerType.MULTIPLE; //все на одной карте
        }
    }

    @Override
    public GamePlayer createPlayer(EventListener listener, String playerName) {
        return new Player(listener, getDice());
    }

    @Override
    public void quietTick(){
        if (this.settings.changed()){
            for (String param: this.settings.whatChanged()){
                if (param.equalsIgnoreCase(CUSTOM_MAP_PATH)){
                    String customMapPath = settings.<String>getParameter(CUSTOM_MAP_PATH).getValue();
                    level.refresh(MapLoader.loadMapFromFile(customMapPath));
                }
            }
            this.settings.changesReacted();
        }
    }

}
