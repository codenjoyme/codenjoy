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


import com.codenjoy.dojo.battlecity.model.levels.Level;
import com.codenjoy.dojo.battlecity.model.levels.LevelImpl;
import com.codenjoy.dojo.services.Dice;
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
        addEditBox(KILL_YOUR_TANK_PENALTY.key()).type(Integer.class).def(0);
        addEditBox(KILL_OTHER_HERO_TANK_SCORE.key()).type(Integer.class).def(50);
        addEditBox(KILL_OTHER_AI_TANK_SCORE.key()).type(Integer.class).def(25);

        addEditBox(SPAWN_AI_PRIZE.key()).type(Integer.class).def(4);
        addEditBox(KILL_HITS_AI_PRIZE.key()).type(Integer.class).def(3);
        addEditBox(PRIZE_ON_FIELD.key()).type(Integer.class).def(50);
        addEditBox(PRIZE_WORKING.key()).type(Integer.class).def(30);
        addEditBox(AI_TICKS_PER_SHOOT.key()).type(Integer.class).def(10);
        addEditBox(TANK_TICKS_PER_SHOOT.key()).type(Integer.class).def(4);
        addEditBox(SLIPPERINESS.key()).type(Integer.class).def(3);
        addEditBox(AI_PRIZE_LIMIT.key()).type(Integer.class).def(3);

        addEditBox(LEVEL_MAP.key()).multiline().type(String.class)
                .def("☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼" +
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

    public Level level(Dice dice) {
        return new LevelImpl(string(LEVEL_MAP), dice);
    }

}
