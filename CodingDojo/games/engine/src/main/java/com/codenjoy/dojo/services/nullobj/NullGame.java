package com.codenjoy.dojo.services.nullobj;

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


import com.codenjoy.dojo.services.Game;
import com.codenjoy.dojo.services.Joystick;
import com.codenjoy.dojo.services.hero.HeroData;
import com.codenjoy.dojo.services.multiplayer.GameField;
import com.codenjoy.dojo.services.multiplayer.GamePlayer;
import com.codenjoy.dojo.services.multiplayer.LevelProgress;
import com.codenjoy.dojo.services.multiplayer.MultiplayerType;
import org.apache.commons.lang3.StringUtils;
import org.json.JSONObject;

public final class NullGame implements Game {

    public static final Game INSTANCE = new NullGame();

    private NullGame() {
        // do nothing
    }

    @Override
    public Joystick getJoystick() {
        return NullJoystick.INSTANCE;
    }

    @Override
    public boolean isGameOver() {
        return false; 
    }

    @Override
    public boolean isWin() {
        return false;
    }

    @Override
    public boolean shouldLeave() {
        return false;
    }

    @Override
    public void newGame() {
        // do nothing
    }

    @Override
    public void loadSave(JSONObject save) {
        // do nothing
    }

    @Override
    public String getBoardAsString() {
        return StringUtils.EMPTY;
    }

    @Override
    public void close() {
        // do nothing
    }

    @Override
    public void clearScore() {
        // do nothing
    }

    @Override
    public HeroData getHero() {
        return HeroData.NULL;
    }

    @Override
    public JSONObject getSave() {
        return new JSONObject();
    }

    @Override
    public GamePlayer getPlayer() {
        return NullGamePlayer.INSTANCE;
    }

    @Override
    public GameField getField() {
        return NullGameField.INSTANCE;
    }

    @Override
    public void on(GameField field) {
        // do nothing
    }

    @Override
    public void setProgress(LevelProgress progress) {
        // do nothing
    }

    @Override
    public LevelProgress getProgress() {
        return MultiplayerType.SINGLE.progress();
    }
}
