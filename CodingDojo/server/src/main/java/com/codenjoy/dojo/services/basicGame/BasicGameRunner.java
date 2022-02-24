package com.codenjoy.dojo.services.basicGame;

/*-
 * #%L
 * Codenjoy - it's a dojo-like platform from developers to developers.
 * %%
 * Copyright (C) 2018 - 2022 Codenjoy
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
import com.codenjoy.dojo.services.AbstractGameType;
import com.codenjoy.dojo.services.EventListener;
import com.codenjoy.dojo.services.PlayerScores;
import com.codenjoy.dojo.services.multiplayer.GameField;
import com.codenjoy.dojo.services.multiplayer.GamePlayer;
import com.codenjoy.dojo.services.printer.CharElements;
import com.codenjoy.dojo.services.settings.Parameter;

public class BasicGameRunner extends AbstractGameType<BasicGameSettings> {

    private String gameName;

    public BasicGameRunner(String gameName) {
        this.gameName = gameName;
    }

    @Override
    public PlayerScores getPlayerScores(Object score, BasicGameSettings settings) {
        return null;
    }

    @Override
    public GameField createGame(int level, BasicGameSettings settings) {
        return null;
    }

    @Override
    public Parameter<Integer> getBoardSize(BasicGameSettings settings) {
        return null;
    }

    @Override
    public String name() {
        return gameName;
    }

    @Override
    public CharElements[] getPlots() {
        return new CharElements[0];
    }

    @Override
    public BasicGameSettings getSettings() {
        return null;
    }

    @Override
    public Class<? extends Solver> getAI() {
        return null;
    }

    @Override
    public Class<? extends ClientBoard> getBoard() {
        return null;
    }

    @Override
    public GamePlayer createPlayer(EventListener listener, String playerId, BasicGameSettings settings) {
        return null;
    }
}
