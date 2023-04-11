package com.codenjoy.dojo.expansion.model.replay;

/*-
 * #%L
 * expansion - it's a dojo-like platform from developers to developers.
 * %%
 * Copyright (C) 2012 - 2022 Codenjoy
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


import com.codenjoy.dojo.expansion.model.Expansion;
import com.codenjoy.dojo.expansion.model.Player;
import com.codenjoy.dojo.expansion.model.levels.items.Hero;
import com.codenjoy.dojo.expansion.services.GameSettings;
import com.codenjoy.dojo.games.expansion.ElementUtils;
import com.codenjoy.dojo.utils.JsonUtils;
import org.jetbrains.annotations.NotNull;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.nio.charset.StandardCharsets;

public class GameLoggerImpl implements GameLogger {

    private static final Logger log = LoggerFactory.getLogger(GameLoggerImpl.class);

    private BufferedWriter writer;
    private Expansion expansion;
    private String replayName;
    private GameSettings settings;

    @Override
    public void start(Expansion expansion) {
        settings = expansion.settings();
        if (!settings.gameLoggingEnable()) return;

        this.expansion = expansion;

        if (writer != null) {
            stop();
        }

        File file = getEmptyFile(expansion);

        try {
            FileOutputStream fos = new FileOutputStream(file);
            writer = new BufferedWriter(new OutputStreamWriter(fos, StandardCharsets.UTF_8));
            write("Game started");
        } catch (Exception e) {
            log.error("Error printing logging game state", e);
        }
    }

    @NotNull
    private File getEmptyFile(Expansion expansion) {
        File file;
        int index = 0;
        do {
            replayName = "game-" + expansion.id() + "-" + (++index);
            file = getReplayFile(replayName);
        } while (file.exists());

        file.getParentFile().mkdirs();

        return file;
    }

    @NotNull
    public static File getReplayFile(String replayName) {
        return new File("gameData/" + replayName + ".txt");
    }

    @Override
    public void register(Player player) {
        if (doNotRecord()) return;

        try {
            try {
                Hero hero = player.getHero();
                if (hero == null) return;
                write(String.format("New player %s registered with hero %s with " +
                                "base at '%s' and color '%s' for user '%s'",
                        player.lg.id(),
                        hero.lg.id(),
                        new JSONObject(hero.getBasePosition()),
                        ElementUtils.index(hero.getBase().element()),
                        player.getName()));
                write(String.format("// Please run \"http://127.0.0.1:8080/codenjoy-contest" +
                                "/admin31415?player=%s&gameName=expansion&data=" +
                                "{'startFromTick':0,'replayName':'%s','playerName':'%s'}\"",
                        player.getName(),
                        replayName,
                        player.lg.id()));
            } catch (Exception e) {
                log.error("Error printing hero game state", e);
            }
        } catch (Exception e) {
            log.error("Error printing logging game state", e);
        }
    }

    private boolean doNotRecord() {
        return !settings.gameLoggingEnable() || expansion == null;
    }

    @Override
    public void logState() {
        if (doNotRecord()) return;

        try {
            expansion.getPlayers().forEach(player -> {
                try {
                    Hero hero = player.getHero();
                    if (hero == null) return;
                    if (!hero.isAlive()) {
                        write(String.format("Hero %s of player %s is not alive",
                                hero.lg.id(),
                                player.lg.id()));
                    }
                    String command = JsonUtils.toStringSorted(hero.getLastAction());
                    write(String.format("Hero %s of player %s received command:'%s'",
                            hero.lg.id(),
                            player.lg.id(),
                            command));
                } catch (Exception e) {
                    log.error("Error printing hero game state", e);
                }
            });
            write(String.format("Board:'%s'", expansion.lg.printer()));
            write("--------------------------------------------------------------");
        } catch (Exception e) {
            log.error("Error printing logging game state", e);
        }
    }

    private void write(String format) throws IOException {
        if (doNotRecord()) return;

        writer.write(format);
        writer.newLine();
        writer.flush();
    }

    private void stop() {
        if (doNotRecord()) return;

        try {
            write("Game finished");
            writer.close();
        } catch (Exception e) {
            log.error("Error closing file for logging game", e);
        }
    }

}
