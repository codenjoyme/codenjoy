package com.epam.dojo.expansion.model.replay;

/*-
 * #%L
 * expansion - it's a dojo-like platform from developers to developers.
 * %%
 * Copyright (C) 2016 - 2017 EPAM
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


import com.codenjoy.dojo.services.*;
import com.codenjoy.dojo.services.hero.HeroData;
import org.apache.commons.lang.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.List;

import static com.epam.dojo.expansion.services.SettingsWrapper.data;

/**
 * Created by Oleksandr_Baglai on 2017-09-21.
 */
public class ReplayGame implements Game {

    private static final String START_FROM_TICK = "startFromTick";
    private static final String REPLAY_NAME = "replayName";
    private static final String PLAYER_NAME = "playerName";

    private final int startFrom;
    private int tick;
    private boolean start;

    private LoggerReader loggerReader;

    public static boolean isReplay(String save) {
        try {
            return save != null && new JSONObject(save).has(REPLAY_NAME);
        } catch (Exception e) {
            return false;
        }
    }

    public ReplayGame(JSONObject save) {
        String replayName = save.getString(REPLAY_NAME);
        startFrom = save.optInt(START_FROM_TICK, 0);
        String playerName = save.getString(PLAYER_NAME);

        this.loggerReader = getLoggerReader(replayName, playerName);
        start = false;
        tick = -1;
        if (!data.delayReplay()) {
            clearScore();
        }
    }

    @NotNull
    protected LoggerReader getLoggerReader(String replayName, String playerName) {
        return new LoggerReaderImpl(replayName, playerName);
    }

    @Override
    public Joystick getJoystick() {
        return NullJoystick.INSTANCE;
    }

    @Override
    public int getMaxScore() {
        return 0;
    }

    @Override
    public int getCurrentScore() {
        return 0;
    }

    @Override
    public boolean isGameOver() {
        return false;
    }

    @Override
    public void newGame() {
        // do nothing
    }

    @Override
    public Object getBoardAsString() {
        return loggerReader.getBoard(tick);
    }

    @Override
    public void destroy() {
        // do nothing
    }

    @Override
    public void clearScore() {
        start = true;
        tick = startFrom;
    }

    @Override
    public HeroData getHero() {
        return new GameHeroData();
    }

    public class GameHeroData implements HeroData {
        @Override
        public Point getCoordinate() {
            return new PointImpl(loggerReader.getBasePosition(tick));
        }

        @Override
        public boolean isSingleBoardGame() {
            return true;
        }

        @Override
        public int getLevel() {
            return 0;
        }

        @Override
        public Object getAdditionalData() {
            JSONObject result = new JSONObject();
            result.put("allLastActions", loggerReader.getAllLastActions(tick));
            result.put("heroesData", loggerReader.getAllBasePositions());
            return result;
        }

        @Override
        public List<Game> playersGroup() {
            return Arrays.asList(ReplayGame.this);
        }
    };

    @Override
    public String getSave() {
        return StringUtils.EMPTY;
    }

    public boolean noMoreTicks() {
        return tick == -1 || tick >= loggerReader.size();
    }

    @Override
    public void tick() {
        if (!start) {
            return;
        }
        if (noMoreTicks()) {
            return;
        }
        tick++;
    }
}
