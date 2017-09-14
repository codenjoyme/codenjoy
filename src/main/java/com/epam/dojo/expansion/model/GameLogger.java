package com.epam.dojo.expansion.model;

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


import com.codenjoy.dojo.services.DLoggerFactory;
import com.codenjoy.dojo.utils.JsonUtils;
import com.epam.dojo.expansion.model.levels.items.Hero;
import org.slf4j.Logger;

import java.io.*;
import java.util.Calendar;

/**
 * Created by Oleksandr_Baglai on 2017-09-14.
 */
public class GameLogger {

    private static Logger logger = DLoggerFactory.getLogger(GameLogger.class);

    private BufferedWriter writer;
    private Expansion expansion;

    public GameLogger(Expansion expansion) {
        this.expansion = expansion;
    }

    public void start() {
        if (writer != null) {
            stop();
        }
        File file = new File("game-" + expansion.id() + "-" + Calendar.getInstance().getTimeInMillis() + ".txt");
        try {
            FileOutputStream fos = new FileOutputStream(file);
            writer = new BufferedWriter(new OutputStreamWriter(fos));
            write("Game started");
        } catch (Exception e) {
            logger.error("Error printing logging game state", e);
        }
    }

    public void logState() {
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
                    String command = JsonUtils.toStringSorted(hero.getCurrentAction());
                    write(String.format("Hero %s of player %s received command:'%s'",
                            hero.lg.id(),
                            player.lg.id(),
                            command));
                } catch (Exception e) {
                    logger.error("Error printing hero game state", e);
                }
            });
            write(String.format("Board:'%s'", expansion.lg.printer()));
            write("--------------------------------------------------------------");
        } catch (Exception e) {
            logger.error("Error printing logging game state", e);
        }
    }

    private void write(String format) throws IOException {
        writer.write(format);
        writer.newLine();
        writer.flush();
    }

    public void stop() {
        try {
            write("Game finished");
            writer.close();
        } catch (Exception e) {
            logger.error("Error closing file for logging game", e);
        }
    }

}
