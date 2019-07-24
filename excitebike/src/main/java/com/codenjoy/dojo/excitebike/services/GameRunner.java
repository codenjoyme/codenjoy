package com.codenjoy.dojo.excitebike.services;

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
import com.codenjoy.dojo.excitebike.client.Board;
import com.codenjoy.dojo.excitebike.client.ai.AISolver;
import com.codenjoy.dojo.excitebike.model.GameFieldImpl;
import com.codenjoy.dojo.excitebike.model.Player;
import com.codenjoy.dojo.excitebike.model.items.GameElementType;
import com.codenjoy.dojo.excitebike.model.items.bike.BikeType;
import com.codenjoy.dojo.excitebike.model.items.springboard.SpringboardElementType;
import com.codenjoy.dojo.excitebike.services.parse.MapParser;
import com.codenjoy.dojo.excitebike.services.parse.MapParserImpl;
import com.codenjoy.dojo.services.AbstractGameType;
import com.codenjoy.dojo.services.EventListener;
import com.codenjoy.dojo.services.GameType;
import com.codenjoy.dojo.services.PlayerScores;
import com.codenjoy.dojo.services.multiplayer.GameField;
import com.codenjoy.dojo.services.multiplayer.GamePlayer;
import com.codenjoy.dojo.services.multiplayer.MultiplayerType;
import com.codenjoy.dojo.services.settings.Parameter;
import com.codenjoy.dojo.services.settings.SettingsImpl;
import com.google.common.collect.ObjectArrays;

import static com.codenjoy.dojo.services.settings.SimpleParameter.v;

public class GameRunner extends AbstractGameType implements GameType {

    //TODO add to settings TASK - 34-admin-functional-and-settings
    public static final int FIELD_HEIGHT = 12;
    private final MapParser mapParser;
    private SettingsHandler settingsHandler;

    public GameRunner() {
        mapParser = new MapParserImpl(getMap());
    }

    protected String getMap() {
        StringBuilder sb = new StringBuilder();
        appendElementManyTimes(sb, GameElementType.FENCE, FIELD_HEIGHT);
        for (int i = 0; i < FIELD_HEIGHT - 2; i++) {
            appendElementManyTimes(sb, GameElementType.NONE, FIELD_HEIGHT);
        }
        appendElementManyTimes(sb, GameElementType.FENCE, FIELD_HEIGHT);
        return sb.toString();
    }

    private void appendElementManyTimes(StringBuilder sb, GameElementType element, int times) {
        for (int i = 0; i < times; i++) {
            sb.append(element);
        }
    }

    @Override
    public PlayerScores getPlayerScores(Object score) {
        return new Scores((Integer) score, settingsHandler);
    }

    @Override
    public GameField createGame(int levelNumber) {
        return new GameFieldImpl(mapParser, getDice(), settingsHandler);
    }

    @Override
    protected SettingsImpl createSettings() {
        settingsHandler = new SettingsHandler();
        return settingsHandler.getSettings();
    }

    @Override
    public Parameter<Integer> getBoardSize() {
        return v(mapParser.getXSize());
    }

    @Override
    public String name() {
        return "excitebike";
    }

    @Override
    public Enum[] getPlots() {
        Enum[] result = ObjectArrays.concat(GameElementType.values(), SpringboardElementType.values(), Enum.class);
        result = ObjectArrays.concat(result, BikeType.values(), Enum.class);
        return result;
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
    public GamePlayer createPlayer(EventListener listener, String playerName) {
        return new Player(listener);
    }
}
