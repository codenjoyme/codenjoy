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

import java.util.Arrays;
import java.util.List;

import static com.codenjoy.dojo.snakebattle.services.GameSettings.Keys.*;

public class GameSettings extends SettingsImpl implements SettingsReader<GameSettings>, RoundSettings<GameSettings> {

    public enum Keys implements Key {

        FLYING_COUNT("[Game] Flying count"),
        FURY_COUNT("[Game] Fury count"),
        STONE_REDUCED("[Game] Stone reduced value"),
        WIN_SCORE("[Score] Win score"),
        APPLE_SCORE("[Score] Apple score"),
        GOLD_SCORE("[Score] Gold score"),
        DIE_PENALTY("[Score] Die penalty"),
        STONE_SCORE("[Score] Stone score"),
        EAT_SCORE("[Score] Eat enemy score"),
        LEVEL_MAP("[Level] Map");

        private String key;

        Keys(String key) {
            this.key = key;
        }

        @Override
        public String key() {
            return key;
        }
    }

    @Override
    public List<Key> allKeys() {
        return Arrays.asList(Keys.values());
    }
    
    public GameSettings() {
        initRound();
        // сколько тиков на 1 раунд
        timePerRound().update(300);
        // сколько раундов (с тем же составом героев) на 1 матч
        roundsPerMatch().update(3);
        // // сколько тиков должно пройти от начала раунда, чтобы засчитать победу
        minTicksForWin().update(40);

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
                "☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼\n" +
                "☼☼     ●   ○             ●   ☼\n" +
                "☼#              ●            ☼\n" +
                "☼☼  ○   ☼#         ○   ●     ☼\n" +
                "☼☼                      ○    ☼\n" +
                "☼# ○         ●               ☼\n" +
                "☼☼                ☼#    ●   %☼\n" +
                "☼☼  ●   ☼☼☼        ☼  ☼      ☼\n" +
                "☼#      ☼      ○   ☼  ☼      ☼\n" +
                "☼☼      ☼○         ☼  ☼      ☼\n" +
                "☼☼      ☼☼☼               ●  ☼\n" +
                "☼#              ☼#           ☼\n" +
                "☼☼○         ●               $☼\n" +
                "☼☼    ●              ☼       ☼\n" +
                "☼#             ○             ☼\n" +
                "☼☼         ●             ●   ☼\n" +
                "☼☼   ○             ☼#        ☼\n" +
                "☼#       ☼☼ ☼                ☼\n" +
                "☼☼ ●        ☼     ●     ○    ☼\n" +
                "☼☼       ☼☼ ☼                ☼\n" +
                "☼#          ☼               @☼\n" +
                "☼☼   ●     ☼#    ●     ●     ☼\n" +
                "☼☼           ○               ☼\n" +
                "☼#                  ☼☼☼      ☼\n" +
                "☼☼                           ☼\n" +
                "☼☼  ●   ○        ☼☼☼#    ○   ☼\n" +
                "☼#           ●               ☼\n" +
                "☼☼               ○      ●    ☼\n" +
                "☼☼      ●         ●          ☼\n" +
                "☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼\n");
    }

    public Level level() {
        return new LevelImpl(string(LEVEL_MAP));
    }

}
