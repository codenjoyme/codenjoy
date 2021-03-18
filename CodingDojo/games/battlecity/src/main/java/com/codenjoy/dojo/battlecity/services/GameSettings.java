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
import com.codenjoy.dojo.services.settings.Chance;
import com.codenjoy.dojo.services.settings.SettingsImpl;
import com.codenjoy.dojo.services.settings.SettingsReader;

import static com.codenjoy.dojo.battlecity.services.GameSettings.Keys.*;

public final class GameSettings extends SettingsImpl implements SettingsReader<GameSettings> {

    public enum Keys implements Key {

        KILL_YOUR_TANK_PENALTY("Kill your tank penalty"),
        KILL_OTHER_HERO_TANK_SCORE("Kill other hero tank score"),
        KILL_OTHER_AI_TANK_SCORE("Kill other AI tank score"),
        SPAWN_AI_PRIZE("Count spawn for AI Tank with prize"),
        KILL_HITS_AI_PRIZE("Hits to kill AI Tank with prize"),
        PRIZE_ON_FIELD("The period of prize validity on the field after the appearance"),
        PRIZE_WORKING("Working time of the prize after catch up"),
        AI_TICKS_PER_SHOOT("Ticks until the next AI Tank shoot"),
        TANK_TICKS_PER_SHOOT("Ticks until the next Tank shoot"),
        SLIPPERINESS("Value of tank sliding on ice"),
        AI_PRIZE_LIMIT("The total number of prize tanks and prizes on the board"),
        LEVEL_MAP("Level map"),
        IMMORTALITY("probability of appearing on the field prize immortality"),
        BREAKING_WALLS("probability of appearing on the field prize breaking walls"),
        WALKING_ON_WATER("probability of appearing on the field prize walking on water"),
        VISIBILITY("probability of appearing on the field prize visibility");

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
        integer(KILL_YOUR_TANK_PENALTY, 0);
        integer(KILL_OTHER_HERO_TANK_SCORE, 50);
        integer(KILL_OTHER_AI_TANK_SCORE, 25);

        integer(SPAWN_AI_PRIZE, 4);
        integer(KILL_HITS_AI_PRIZE, 3);
        integer(PRIZE_ON_FIELD, 50);
        integer(PRIZE_WORKING, 30);
        integer(AI_TICKS_PER_SHOOT, 10);
        integer(TANK_TICKS_PER_SHOOT, 4);
        integer(SLIPPERINESS, 3);
        integer(AI_PRIZE_LIMIT, 3);

        integer(IMMORTALITY, 50);
        integer(BREAKING_WALLS, 10);
        integer(WALKING_ON_WATER, 0);
        integer(VISIBILITY, -1);

        Chance chance = chance();
        chance.put(Elements.PRIZE_IMMORTALITY, integerValue(IMMORTALITY));
        chance.put(Elements.PRIZE_BREAKING_WALLS, integerValue(BREAKING_WALLS));
        chance.put(Elements.PRIZE_WALKING_ON_WATER, integerValue(WALKING_ON_WATER));
        chance.put(Elements.PRIZE_VISIBILITY, integerValue(VISIBILITY));
        chance.run();

        multiline(LEVEL_MAP,
                "☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼" +
                "☼ ¿    ¿    ¿        ¿    ¿    ¿ ☼" +
                "☼                                ☼" +
                "☼  ╬╬╬  ╬╬╬  ╬╬╬  ╬╬╬  ╬╬╬  ╬╬╬  ☼" +
                "☼ #╬╬╬# ╬╬╬ #╬╬╬##╬╬╬# ╬╬╬ #╬╬╬# ☼" +
                "☼ #╬╬╬# ╬╬╬ #╬╬╬##╬╬╬# ╬╬╬ #╬╬╬# ☼" +
                "☼ #╬╬╬# ╬╬╬ #╬╬╬##╬╬╬# ╬╬╬ #╬╬╬# ☼" +
                "☼ #╬╬╬# ╬╬╬ #╬╬╬☼☼╬╬╬# ╬╬╬ #╬╬╬# ☼" +
                "☼ #╬╬╬# ╬╬╬ #╬╬╬☼☼╬╬╬# ╬╬╬ #╬╬╬# ☼" +
                "☼ #╬╬╬# ╬╬╬ #╬╬╬  ╬╬╬# ╬╬╬ #╬╬╬# ☼" +
                "☼ #╬╬╬# ╬╬╬  ╬╬╬  ╬╬╬  ╬╬╬ #╬╬╬# ☼" +
                "☼ #╬╬╬# ╬╬╬            ╬╬╬ #╬╬╬# ☼" +
                "☼  ╬╬╬  ╬╬╬   ~    ~   ╬╬╬  ╬╬╬  ☼" +
                "☼  ~~~       ╬╬╬  ╬╬╬       ~~~  ☼" +
                "☼  ~~        ╬╬╬  ╬╬╬        ~~  ☼" +
                "☼     ╬╬╬╬╬  ╬╬╬  ╬╬╬  ╬╬╬╬╬     ☼" +
                "☼☼☼   ╬╬╬╬╬            ╬╬╬╬╬   ☼☼☼" +
                "☼ ~~          %%%%%%          ~~ ☼" +
                "☼           ~╬╬╬%%╬╬╬~           ☼" +
                "☼  ╬╬╬  ╬╬╬ ~╬╬╬%%╬╬╬~ ╬╬╬  ╬╬╬  ☼" +
                "☼  ╬╬╬  ╬╬╬  ╬╬╬  ╬╬╬  ╬╬╬  ╬╬╬  ☼" +
                "☼  ╬╬╬~ ╬╬╬  ╬╬╬╬╬╬╬╬  ╬╬╬ ~╬╬╬  ☼" +
                "☼  ╬╬╬  ╬╬╬  ╬╬╬╬╬╬╬╬  ╬╬╬  ╬╬╬  ☼" +
                "☼ %╬╬╬  ╬╬╬  ╬╬╬%%╬╬╬  ╬╬╬  ╬╬╬% ☼" +
                "☼ %╬╬╬  ╬╬╬~ ╬╬╬%%╬╬╬ ~╬╬╬  ╬╬╬% ☼" +
                "☼ %╬╬╬  ╬╬╬~ ╬╬╬%%╬╬╬ ~╬╬╬  ╬╬╬% ☼" +
                "☼ %╬╬╬ ~╬╬╬  ╬╬╬%%╬╬╬  ╬╬╬~ ╬╬╬% ☼" +
                "☼ %╬╬╬  %%%            %%%  ╬╬╬% ☼" +
                "☼  ╬╬╬  %%%    ~~~~    %%%  ╬╬╬  ☼" +
                "☼  ╬╬╬  %%%  ╬╬╬╬╬╬╬╬  %%%  ╬╬╬  ☼" +
                "☼  ╬╬╬       ╬╬╬╬╬╬╬╬       ╬╬╬  ☼" +
                "☼            ╬╬    ╬╬            ☼" +
                "☼  %%%%%%    ╬╬    ╬╬    %%%%%%  ☼" +
                "☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼");
    }

    public Chance chance() {
        return new Chance();
    }

    public Level level(Dice dice) {
        return new LevelImpl(string(LEVEL_MAP), dice);
    }

}
