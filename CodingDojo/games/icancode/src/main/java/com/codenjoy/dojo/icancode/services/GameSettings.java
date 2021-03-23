package com.codenjoy.dojo.icancode.services;

/*-
 * #%L
 * expansion - it's a dojo-like platform from developers to developers.
 * %%
 * Copyright (C) 2016 - 2020 Codenjoy
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


import com.codenjoy.dojo.icancode.services.levels.Level;
import com.codenjoy.dojo.services.settings.SettingsImpl;
import com.codenjoy.dojo.services.settings.SettingsReader;

import java.util.Arrays;

import static com.codenjoy.dojo.icancode.services.GameSettings.Keys.*;

public final class GameSettings extends SettingsImpl implements SettingsReader<GameSettings> {

    public static final String CLASSIC_TRAINING = "Single training & all in one final";
    public static final String ALL_SINGLE = "All levels are single";
    public static final String ALL_IN_ROOMS = "All levels in rooms";
    public static final String TRAINING_MULTIMAP = "Single training & final in rooms";

    public enum Keys implements Key {

        PERK_DROP_RATIO("Perk drop ratio"),
        PERK_AVAILABILITY("Perk availability"),
        PERK_ACTIVITY("Perk activity"),
        DEATH_RAY_PERK_RANGE("Death-Ray perk range"),
        WIN_SCORE("Win score"),
        GOLD_SCORE("Gold score"),
        KILL_ZOMBIE_SCORE("Kill zombie score"),
        KILL_HERO_SCORE("Kill hero score"),
        ENABLE_KILL_SCORE("Enable score for kill"),
        LOOSE_PENALTY("Loose penalty"),
        IS_TRAINING_MODE("Is training mode"),
        GUN_RECHARGE("Heroes gun recharge"),
        GUN_SHOT_QUEUE("Heroes gun need to relax after a series of shots"),
        GUN_REST_TIME("Heroes gun rest time(ticks)"),
        DEFAULT_PERKS("Default hero perks on training and contest"),
        GAME_MODE("Game mode"),
        ROOM_SIZE("Room size"),
        LEVELS_COUNT("Levels count");

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
        integer(PERK_DROP_RATIO, 50);
        integer(PERK_AVAILABILITY, 10);
        integer(PERK_ACTIVITY, 10);
        integer(DEATH_RAY_PERK_RANGE, 10);
        integer(WIN_SCORE, 25);
        integer(GOLD_SCORE, 10);
        integer(KILL_ZOMBIE_SCORE, 5);
        integer(KILL_HERO_SCORE, 10);
        bool(ENABLE_KILL_SCORE, true);
        integer(LOOSE_PENALTY, 5);
        bool(IS_TRAINING_MODE, true);

        integer(GUN_RECHARGE, 2);
        integer(GUN_SHOT_QUEUE, 10);
        integer(GUN_REST_TIME, 10);

        string(DEFAULT_PERKS, ",ajm");

        options(GAME_MODE, Arrays.asList(
                CLASSIC_TRAINING, ALL_SINGLE, ALL_IN_ROOMS, TRAINING_MULTIMAP),
                CLASSIC_TRAINING);
        integer(ROOM_SIZE, 5);

        integer(LEVELS_COUNT, 0);
        Levels.setup(this);
    }

    public int roomSize() {
        return integer(ROOM_SIZE) == 0
                ? Integer.MAX_VALUE
                : integer(ROOM_SIZE);
    }

    public String levelMap(int index) {
        String prefix = levelPrefix(index);
        return string(() -> prefix + "map");
    }

    public GameSettings addLevel(int index, Level level) {
        integer(LEVELS_COUNT, index);

        String prefix = levelPrefix(index);
        multiline(() -> prefix + "map", level.map());
        multiline(() -> prefix + "help", level.help());
        multiline(() -> prefix + "default code", level.defaultCode());
        multiline(() -> prefix + "refactoring code", level.refactoringCode());
        multiline(() -> prefix + "win code", level.winCode());
        multiline(() -> prefix + "autocomplete", level.autocomplete().replace("'", "\""));
        multiline(() -> prefix + "befunge commands", String.join("\n", level.befungeCommands()));
        return this;
    }

    private String levelPrefix(int index) {
        return "Level" + index + " ";
    }

}
