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


import com.codenjoy.dojo.icancode.model.items.ZombieBrain;
import com.codenjoy.dojo.icancode.services.levels.Level;
import com.codenjoy.dojo.services.settings.SettingsImpl;
import com.codenjoy.dojo.services.settings.SettingsReader;

import java.util.Arrays;
import java.util.List;

import static com.codenjoy.dojo.icancode.services.GameSettings.Keys.*;

public class GameSettings extends SettingsImpl implements SettingsReader<GameSettings> {

    public static final String CLASSIC_TRAINING = "Single training & all in one final";
    public static final String ALL_SINGLE = "All levels are single";
    public static final String ALL_IN_ROOMS = "All levels in rooms";
    public static final String TRAINING_MULTIMAP = "Single training & final in rooms";

    public enum Keys implements Key {

        IS_TRAINING_MODE("[Game] Is training mode"),
        GAME_MODE("[Game] Game mode"),
        ROOM_SIZE("[Game] Room size"),
        VIEW_SIZE("[Game] Map view size"),
        LEVELS_COUNT("[Game] Levels count"),
        CHEATS("[Game] Cheats enabled"),

        GUN_RECHARGE("[Gun] Heroes gun recharge"),
        GUN_SHOT_QUEUE("[Gun] Heroes gun need to relax after a series of shots"),
        GUN_REST_TIME("[Gun] Heroes gun rest time(ticks)"),

        TICKS_PER_NEW_ZOMBIE("[Zombie] Ticks per new zombie"),
        COUNT_ZOMBIES_ON_MAP("[Zombie] Count zombies"),
        WALK_EACH_TICKS("[Zombie] Zombie walks tick timeout"),

        DEFAULT_PERKS("[Perk] Default hero perks on training and contest"),
        PERK_DROP_RATIO("[Perk] Drop ratio"),
        PERK_AVAILABILITY("[Perk] Availability"),
        PERK_ACTIVITY("[Perk] Activity"),
        DEATH_RAY_PERK_RANGE("[Perk] Death-Ray perk range"),

        WIN_SCORE("[Score] Win score"),
        GOLD_SCORE("[Score] Gold score"),
        KILL_ZOMBIE_SCORE("[Score] Kill zombie score"),
        ENABLE_KILL_SCORE("[Score] Enable score for kill"),
        KILL_HERO_SCORE("[Score] Kill hero score"),
        LOSE_PENALTY("[Score] Lose penalty");

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
        bool(IS_TRAINING_MODE, true);
        options(GAME_MODE,
                Arrays.asList(
                        CLASSIC_TRAINING,
                        ALL_SINGLE,
                        ALL_IN_ROOMS,
                        TRAINING_MULTIMAP
                ),
                CLASSIC_TRAINING);
        integer(ROOM_SIZE, 5);
        integer(VIEW_SIZE, 20);
        integer(LEVELS_COUNT, 0);
        bool(CHEATS, false);

        integer(GUN_RECHARGE, 2);
        integer(GUN_SHOT_QUEUE, 10);
        integer(GUN_REST_TIME, 10);

        integer(TICKS_PER_NEW_ZOMBIE, 20);
        integer(COUNT_ZOMBIES_ON_MAP, 4);
        integer(WALK_EACH_TICKS, 2);

        string(DEFAULT_PERKS, ",ajm");
        integer(PERK_DROP_RATIO, 50);
        integer(PERK_AVAILABILITY, 10);
        integer(PERK_ACTIVITY, 10);
        integer(DEATH_RAY_PERK_RANGE, 10);

        integer(WIN_SCORE, 25);
        integer(GOLD_SCORE, 10);
        integer(KILL_ZOMBIE_SCORE, 5);
        bool(ENABLE_KILL_SCORE, true);
        integer(KILL_HERO_SCORE, 10);
        integer(LOSE_PENALTY, 5);

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

    public ZombieBrain zombieBrain() {
        return new ZombieBrain();
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
