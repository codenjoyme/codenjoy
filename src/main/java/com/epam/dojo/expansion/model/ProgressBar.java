package com.epam.dojo.expansion.model;

/*-
 * #%L
 * iCanCode - it's a dojo-like platform from developers to developers.
 * %%
 * Copyright (C) 2016 EPAM
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


import com.codenjoy.dojo.services.DLoggerFactory;
import com.codenjoy.dojo.services.Game;
import com.codenjoy.dojo.utils.JsonUtils;
import com.epam.dojo.expansion.model.levels.Level;
import com.epam.dojo.expansion.model.lobby.PlayerLobby;
import com.epam.dojo.expansion.model.lobby.WaitForAllPlayerLobby;
import com.epam.dojo.expansion.services.Printer;
import org.apache.commons.lang.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.json.JSONObject;
import org.slf4j.Logger;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Predicate;

/**
 * Created by oleksandr.baglai on 27.06.2016.
 */
public class ProgressBar {

    private static Logger logger = DLoggerFactory.getLogger(ProgressBar.class);

    private PlayerLobby lobby;
    private GameFactory factory;
    private Player player;

    private int currentLevel;
    private int lastPassedLevel;
    private boolean finished;
    private boolean nextLevel;

    private Integer backToSingleLevel;

    private PlayerBoard single;
    private PlayerBoard current;
    private Printer printer;
    private Single gameOwner;

    public ProgressBar(GameFactory factory, PlayerLobby lobby) {
        this.factory = factory;
        this.lobby = lobby;
        single = factory.get(Expansion.SINGLE, level -> true);
    }

    protected void setNextLevel() {
        nextLevel = true;
    }

    private void buildPrinter() {
        printer = new Printer(current, current.getViewSize());
    }

    private Integer getBackToSingleLevel() {
        Integer result = backToSingleLevel;
        backToSingleLevel = null;
        return result;
    }

    protected void checkLevel() {
        if (nextLevel) {
            nextLevel();
        } else if (!player.getHero().isAlive()) {
            if (!isMultiple()) {
                createHeroToPlayer();
            }
        } else if (player.getHero().isChangeLevel()) {
            changeLevel();
        }
    }

    private void changeLevel() {
        int level = player.getHero().getLevel();
        if (!current.isMultiple()) {
            if (level == -1) {
                level = currentLevel;
            }
            if (level > lastPassedLevel + 1) {
                return;
            }
            if (level >= current.levelsCount()) {
                finished = true;
                return;
            }
            loadLevel(level);
            createHeroToPlayer();
        } else {
            if (level == -1) {
                loadMultiple();
            } else if (level < single.levelsCount()) {
                backToSingleLevel = level;
            }
        }
    }

    private void nextLevel() {
        if (currentLevel < current.levelsCount() - 1) {
            if (lastPassedLevel < currentLevel) {
                lastPassedLevel = currentLevel;
            }
            loadLevel(currentLevel + 1);
        } else if (!current.isMultiple()) {
            if (lastPassedLevel < currentLevel) {
                lastPassedLevel = currentLevel;
            }
            finished = true;
        }
        createHeroToPlayer();
    }

    private void createHeroToPlayer() {
        remove(player);
        start(player);
        nextLevel = false;
    }

    private void start(Player player) {
        if (current != null) {
            current.newGame(player);
        }
    }

    public void remove(Player player) {
        if (current != null) {
            current.remove(player);
        }
    }

    public void tick() {
        if (logger.isDebugEnabled()) {
            logger.debug("ProgressBar before tick {}", this.toString());
        }

        lobby.tick();
        current.tick();
        if (isMultiple()) {
            Integer level = getBackToSingleLevel();
            if (level != null) {
                if (level > single.levelsCount()) {
                    return;
                }
                loadSingle(level);
            }
        } else {
            if (finished) {
                loadMultiple();
            }
        }

        if (logger.isDebugEnabled()) {
            logger.debug("ProgressBar after tick {}", this.toString());
        }
    }

    private void loadMultiple() {
        remove(player);
        current = lobby.start(player, () -> factory.get(Expansion.MULTIPLE, getLevelChose()));
        processCurrent(0);
    }

    private void processCurrent(int level) {
        loadLevel(level);
        finished = false;
        buildPrinter();
        try {
            start(player);
        } catch (BusyMapException e) {
            remove(player); // TODO и что дальше?
        }
    }

    @NotNull
    private Predicate<Level> getLevelChose() {
//        String levelName = (current != null) ? current.getCurrentLevel().getName() : null;
//        return level -> !level.getName().equals(levelName);
        return level -> true; // TODO продолжить тут
    }

    private void loadSingle(Integer level) {
        remove(player);
        current = single;
        processCurrent(level);
    }

    private void loadLevel(Integer level) {
        currentLevel = level;
        player.destroyHero();
        current.loadLevel(level);
    }

    // TODO test me
    public List<Game> getPlayerRoom() {
        List<Player> players = current.getPlayers();
        if (!isMultiple()) {
            if (players.size() != 1) {
                logger.warn("Expected one player in single room!");
            }
            return Arrays.asList(players.get(0).getGame());
        }
        List<Game> result = new LinkedList<>();
        for (Player player : players) {
            result.add(player.getGame());
        }
        return result;
    }

    protected void setPlayer(Player player) {
        this.player = player;
        lobby.addPlayer(player);
    }

    public void start(String save) {
        if (!StringUtils.isEmpty(save)) {
            loadProgress(save);
        } else {
            boolean isMultiple = single.levelsCount() == 0;
            load(isMultiple, 0);
            backToSingleLevel = null;
            lastPassedLevel = -1;
        }
    }

    public Printer getPrinter() {
        return printer;
    }

    public JSONObject printProgress() {
        JSONObject object = new JSONObject();
        object.put("current", currentLevel);
        object.put("lastPassed", lastPassedLevel);
        object.put("total", single.levelsCount());
        object.put("multiple", isMultiple());
        object.put("scores", enableWinScore());
        return object;
    }

    public boolean isMultiple() {
        return current != single;
    }

    private void loadProgress(String save) {
        try {
            JSONObject object = new JSONObject(save);
            currentLevel = object.getInt("current");
            lastPassedLevel = object.getInt("lastPassed");
            boolean isMultiple = object.getBoolean("multiple");
            load(isMultiple, currentLevel);
        } catch (Exception e) {
            logger.error("Error during loadProgress from save {}", save, e);
        }
    }

    private void load(boolean isMultiple, int currentLevel) {
        if (isMultiple) {
            loadMultiple();
        } else {
            loadSingle(currentLevel);
        }
    }

    public boolean enableWinScore() {
        return isMultiple() || (currentLevel > lastPassedLevel);
    }

    public PlayerBoard getCurrent() {
        return current;
    }

    public void setGameOwner(Single gameOwner) {
        this.gameOwner = gameOwner;
    }

    public Single getGameOwner() {
        return gameOwner;
    }

    public int getRoundTicks() {
        return current.getRoundTicks();
    }

    public void setCurrent(PlayerBoard current) {
        this.current = current;
        if (current != null) {
            processCurrent(0);
        }
    }

    public class LogState {
        public JSONObject json() {
            return new JSONObject(){{
                put("id", id());
                put("player", (player != null) ? player.lg.id() : "null");
                put("player.hero", (player != null && player.hero != null) ? player.hero.lg.id() : "null");
                put("currentLevel", currentLevel);
                put("lastPassedLevel", lastPassedLevel);
                put("finished", finished);
                put("nextLevel", nextLevel);
                put("backToSingleLevel", backToSingleLevel);
                put("single", (single != null) ? single.id() : "null");
                put("current", (current != null) ? current.id() : "null");
                put("gameOwner", (gameOwner != null) ? gameOwner.lg.id() : "null");
            }};
        }

        public String id() {
            return "PB@" + Integer.toHexString(ProgressBar.this.hashCode());
        }
    }

    public LogState lg = new LogState();

    @Override
    public String toString() {
        return JsonUtils.toStringSorted(lg.json());
    }
}
