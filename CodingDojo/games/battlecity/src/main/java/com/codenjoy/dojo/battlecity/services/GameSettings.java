package com.codenjoy.dojo.battlecity.services;

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


import com.codenjoy.dojo.battlecity.model.Elements;
import com.codenjoy.dojo.battlecity.model.levels.Level;
import com.codenjoy.dojo.battlecity.model.levels.LevelImpl;
import com.codenjoy.dojo.services.Dice;
import com.codenjoy.dojo.services.round.RoundSettings;
import com.codenjoy.dojo.services.semifinal.SemifinalSettings;
import com.codenjoy.dojo.services.settings.Chance;
import com.codenjoy.dojo.services.settings.SettingsImpl;
import com.codenjoy.dojo.services.settings.SettingsReader;

import java.util.Arrays;
import java.util.List;

import static com.codenjoy.dojo.battlecity.model.Elements.*;
import static com.codenjoy.dojo.battlecity.services.GameSettings.Keys.*;
import static com.codenjoy.dojo.services.settings.Chance.CHANCE_RESERVED;

public class GameSettings extends SettingsImpl
        implements SettingsReader<GameSettings>,
        RoundSettings<GameSettings>,
                SemifinalSettings<GameSettings> {

    public enum Keys implements Key {

        AI_TICKS_PER_SHOOT("[Game] Ticks until the next AI Tank shoot"),
        TANK_TICKS_PER_SHOOT("[Game] Ticks until the next Tank shoot"),
        SLIPPERINESS("[Game] Value of tank sliding on ice"),
        PENALTY_WALKING_ON_WATER("[Game] Penalty time when walking on water"),
        SHOW_MY_TANK_UNDER_TREE("[Game] Show my tank under tree"),

        SPAWN_AI_PRIZE("[Prize] Count spawn for AI Tank with prize"),
        KILL_HITS_AI_PRIZE("[Prize] Hits to kill AI Tank with prize"),
        PRIZE_ON_FIELD("[Prize] The period of prize validity on the field after the appearance"),
        PRIZE_WORKING("[Prize] Working time of the prize after catch up"),
        AI_PRIZE_LIMIT("[Prize] The total number of prize tanks and prizes on the board"),

        CHANCE_IMMORTALITY("[Chance] Prize immortality"),
        CHANCE_BREAKING_WALLS("[Chance] Prize breaking walls"),
        CHANCE_WALKING_ON_WATER("[Chance] Prize walking on water"),
        CHANCE_VISIBILITY("[Chance] Prize visibility"),
        CHANCE_NO_SLIDING("[Chance] Prize no sliding"),

        KILL_YOUR_TANK_PENALTY("[Score] Kill your tank penalty"),
        KILL_OTHER_HERO_TANK_SCORE("[Score] Kill other hero tank score"),
        KILL_OTHER_AI_TANK_SCORE("[Score] Kill other AI tank score"),

        LEVEL_MAP("[Level] map");

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
        initSemifinal();

        // сколько участников в комнате
        playersPerRoom().update(20);

        integer(AI_TICKS_PER_SHOOT, 10);
        integer(TANK_TICKS_PER_SHOOT, 4);
        integer(SLIPPERINESS, 3);
        integer(PENALTY_WALKING_ON_WATER, 2);
        bool(SHOW_MY_TANK_UNDER_TREE, false);

        integer(SPAWN_AI_PRIZE, 4);
        integer(KILL_HITS_AI_PRIZE, 3);
        integer(PRIZE_ON_FIELD, 50);
        integer(PRIZE_WORKING, 30);
        integer(AI_PRIZE_LIMIT, 3);

        integer(CHANCE_RESERVED, 30);
        integer(CHANCE_IMMORTALITY, 20);
        integer(CHANCE_BREAKING_WALLS, 20);
        integer(CHANCE_WALKING_ON_WATER, 20);
        integer(CHANCE_VISIBILITY, 20);
        integer(CHANCE_NO_SLIDING, 20);

        integer(KILL_YOUR_TANK_PENALTY, 0);
        integer(KILL_OTHER_HERO_TANK_SCORE, 50);
        integer(KILL_OTHER_AI_TANK_SCORE, 25);


        multiline(LEVEL_MAP,
                "☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼\n" +
                "☼ ¿    ¿    ¿        ¿    ¿    ¿ ☼\n" +
                "☼                                ☼\n" +
                "☼  ╬╬╬  ╬╬╬  ╬╬╬  ╬╬╬  ╬╬╬  ╬╬╬  ☼\n" +
                "☼ #╬╬╬# ╬╬╬ #╬╬╬##╬╬╬# ╬╬╬ #╬╬╬# ☼\n" +
                "☼ #╬╬╬# ╬╬╬ #╬╬╬##╬╬╬# ╬╬╬ #╬╬╬# ☼\n" +
                "☼ #╬╬╬# ╬╬╬ #╬╬╬##╬╬╬# ╬╬╬ #╬╬╬# ☼\n" +
                "☼ #╬╬╬# ╬╬╬ #╬╬╬☼☼╬╬╬# ╬╬╬ #╬╬╬# ☼\n" +
                "☼ #╬╬╬# ╬╬╬ #╬╬╬☼☼╬╬╬# ╬╬╬ #╬╬╬# ☼\n" +
                "☼ #╬╬╬# ╬╬╬ #╬╬╬  ╬╬╬# ╬╬╬ #╬╬╬# ☼\n" +
                "☼ #╬╬╬# ╬╬╬  ╬╬╬  ╬╬╬  ╬╬╬ #╬╬╬# ☼\n" +
                "☼ #╬╬╬# ╬╬╬            ╬╬╬ #╬╬╬# ☼\n" +
                "☼  ╬╬╬  ╬╬╬   ~    ~   ╬╬╬  ╬╬╬  ☼\n" +
                "☼  ~~~       ╬╬╬  ╬╬╬       ~~~  ☼\n" +
                "☼  ~~        ╬╬╬  ╬╬╬        ~~  ☼\n" +
                "☼     ╬╬╬╬╬  ╬╬╬  ╬╬╬  ╬╬╬╬╬     ☼\n" +
                "☼☼☼   ╬╬╬╬╬            ╬╬╬╬╬   ☼☼☼\n" +
                "☼ ~~          %%%%%%          ~~ ☼\n" +
                "☼           ~╬╬╬%%╬╬╬~           ☼\n" +
                "☼  ╬╬╬  ╬╬╬ ~╬╬╬%%╬╬╬~ ╬╬╬  ╬╬╬  ☼\n" +
                "☼  ╬╬╬  ╬╬╬  ╬╬╬  ╬╬╬  ╬╬╬  ╬╬╬  ☼\n" +
                "☼  ╬╬╬~ ╬╬╬  ╬╬╬╬╬╬╬╬  ╬╬╬ ~╬╬╬  ☼\n" +
                "☼  ╬╬╬  ╬╬╬  ╬╬╬╬╬╬╬╬  ╬╬╬  ╬╬╬  ☼\n" +
                "☼ %╬╬╬  ╬╬╬  ╬╬╬%%╬╬╬  ╬╬╬  ╬╬╬% ☼\n" +
                "☼ %╬╬╬  ╬╬╬~ ╬╬╬%%╬╬╬ ~╬╬╬  ╬╬╬% ☼\n" +
                "☼ %╬╬╬  ╬╬╬~ ╬╬╬%%╬╬╬ ~╬╬╬  ╬╬╬% ☼\n" +
                "☼ %╬╬╬ ~╬╬╬  ╬╬╬%%╬╬╬  ╬╬╬~ ╬╬╬% ☼\n" +
                "☼ %╬╬╬  %%%            %%%  ╬╬╬% ☼\n" +
                "☼  ╬╬╬  %%%    ~~~~    %%%  ╬╬╬  ☼\n" +
                "☼  ╬╬╬  %%%  ╬╬╬╬╬╬╬╬  %%%  ╬╬╬  ☼\n" +
                "☼  ╬╬╬       ╬╬╬╬╬╬╬╬       ╬╬╬  ☼\n" +
                "☼            ╬╬    ╬╬            ☼\n" +
                "☼  %%%%%%    ╬╬    ╬╬    %%%%%%  ☼\n" +
                "☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼\n");
    }

    public Chance<Elements> chance(Dice dice) {
        return new Chance<Elements>(dice, this)
            .put(CHANCE_IMMORTALITY, PRIZE_IMMORTALITY)
            .put(CHANCE_BREAKING_WALLS, PRIZE_BREAKING_WALLS)
            .put(CHANCE_WALKING_ON_WATER, PRIZE_WALKING_ON_WATER)
            .put(CHANCE_VISIBILITY, PRIZE_VISIBILITY)
            .put(CHANCE_NO_SLIDING, PRIZE_NO_SLIDING)
            .run();
    }

    public Level level(Dice dice) {
        return new LevelImpl(string(LEVEL_MAP), dice);
    }

}
