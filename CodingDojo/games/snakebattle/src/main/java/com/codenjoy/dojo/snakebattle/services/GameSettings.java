package com.codenjoy.dojo.snakebattle.services;

/*-
 * #%L
 * Codenjoy - it's a dojo-like platform from developers to developers.
 * %%
 * Copyright (C) 2018 - 2021 Codenjoy
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

import com.codenjoy.dojo.services.round.RoundSettings;
import com.codenjoy.dojo.services.settings.SettingsImpl;
import com.codenjoy.dojo.services.settings.SettingsReader;
import com.codenjoy.dojo.snakebattle.model.level.Level;
import com.codenjoy.dojo.snakebattle.model.level.LevelImpl;

import static com.codenjoy.dojo.services.round.RoundSettings.Keys.*;
import static com.codenjoy.dojo.snakebattle.services.GameSettings.Keys.*;

public class GameSettings extends SettingsImpl implements SettingsReader<GameSettings>, RoundSettings {

    public enum Keys implements Key {

        PLAYERS_PER_ROOM("Players per Room"),
        FLYING_COUNT("Flying count"),
        FURY_COUNT("Fury count"),
        STONE_REDUCED("Stone reduced value"),
        WIN_SCORE("Win score"),
        APPLE_SCORE("Apple score"),
        GOLD_SCORE("Gold score"),
        DIE_PENALTY("Die penalty"),
        STONE_SCORE("Stone score"),
        EAT_SCORE("Eat enemy score"),
        LEVEL_MAP("Level map");

        private String key;

        Keys(String key) {
            this.key = key;
        }

        @Override
        public String key() {
            return key;
        }
    }
    
    public GameSettings() {
        bool(ROUNDS_ENABLED, true);
        integer(TIME_PER_ROUND, 300);
        integer(TIME_FOR_WINNER, 1);
        integer(TIME_BEFORE_START, 5);
        integer(ROUNDS_PER_MATCH, 3);
        integer(MIN_TICKS_FOR_WIN, 40);

        integer(PLAYERS_PER_ROOM, 5);
        integer(FLYING_COUNT, 10);
        integer(FURY_COUNT, 10);
        integer(STONE_REDUCED, 3);

        integer(WIN_SCORE, 50);
        integer(APPLE_SCORE, 1);
        integer(GOLD_SCORE, 10);
        integer(DIE_PENALTY, 0);
        integer(STONE_SCORE, 5);
        integer(EAT_SCORE, 10);

        multiline(LEVEL_MAP,
                "☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼" +
                "☼☼         ○                 ☼" +
                "☼#                           ☼" +
                "☼☼  ○   ☼#         ○         ☼" +
                "☼☼                      ○    ☼" +
                "☼# ○         ●               ☼" +
                "☼☼                ☼#        %☼" +
                "☼☼      ☼☼☼        ☼  ☼      ☼" +
                "☼#      ☼      ○   ☼  ☼      ☼" +
                "☼☼      ☼○         ☼  ☼      ☼" +
                "☼☼      ☼☼☼               ●  ☼" +
                "☼#              ☼#           ☼" +
                "☼☼○                         $☼" +
                "☼☼    ●              ☼       ☼" +
                "☼#             ○             ☼" +
                "☼☼                           ☼" +
                "☼☼   ○             ☼#        ☼" +
                "☼#       ☼☼ ☼                ☼" +
                "☼☼          ☼     ●     ○    ☼" +
                "☼☼       ☼☼ ☼                ☼" +
                "☼#          ☼               @☼" +
                "☼☼         ☼#                ☼" +
                "☼☼           ○               ☼" +
                "☼#                  ☼☼☼      ☼" +
                "☼☼                           ☼" +
                "☼☼      ○        ☼☼☼#    ○   ☼" +
                "☼#                           ☼" +
                "☼☼     ╘►        ○           ☼" +
                "☼☼                           ☼" +
                "☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼");
    }

    public Level level() {
        return new LevelImpl(string(LEVEL_MAP));
    }

}
