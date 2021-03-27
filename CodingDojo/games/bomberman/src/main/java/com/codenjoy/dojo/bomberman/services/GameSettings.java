package com.codenjoy.dojo.bomberman.services;

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


import com.codenjoy.dojo.bomberman.model.*;
import com.codenjoy.dojo.bomberman.model.perks.PerksSettingsWrapper;
import com.codenjoy.dojo.services.Dice;
import com.codenjoy.dojo.services.round.RoundSettings;
import com.codenjoy.dojo.services.settings.Parameter;
import com.codenjoy.dojo.services.settings.SettingsImpl;
import com.codenjoy.dojo.services.settings.SettingsReader;
import com.google.common.base.Supplier;
import org.apache.commons.lang3.StringUtils;
import org.json.JSONObject;

import java.util.Arrays;

import static com.codenjoy.dojo.bomberman.services.GameSettings.Keys.*;
import static com.codenjoy.dojo.services.round.RoundSettings.Keys.*;

public class GameSettings extends SettingsImpl implements SettingsReader<GameSettings>, RoundSettings {

    public enum Keys implements Key {

        MULTIPLE("[Game] Is multiple or disposable"),
        PLAYERS_PER_ROOM("[Game] Players per room for disposable"),
        KILL_WALL_SCORE("[Score] Kill wall score"),
        KILL_MEAT_CHOPPER_SCORE("[Score] Kill meat chopper score"),
        KILL_OTHER_HERO_SCORE("[Score] Kill other hero score"),
        CATCH_PERK_SCORE("[Score] Catch perk score"),
        DIE_PENALTY("[Score] Your hero's death penalty"),
        WIN_ROUND_SCORE("[Score][Rounds] Win round score"),
        BIG_BADABOOM("[Level] Blast activate bomb"),
        BOMBS_COUNT("[Level] Bombs count"),
        BOMB_POWER("[Level] Bomb power"),
        BOARD_SIZE("[Level] Board size"),
        DESTROY_WALL_COUNT("[Level] Destroy wall count"),
        MEAT_CHOPPERS_COUNT("[Level] Meat choppers count"),
        PERK_DROP_RATIO("[Perks] Perks drop ratio in %"),
        PERK_PICK_TIMEOUT("[Perks] Perks pick timeout"),
        PERK_BOMB_BLAST_RADIUS_INC("[Perks] Bomb blast radius increase"),
        TIMEOUT_BOMB_BLAST_RADIUS_INC("[Perks] Bomb blast radius increase effect timeout"),
        PERK_BOMB_COUNT_INC("[Perks] Bomb count increase"),
        TIMEOUT_BOMB_COUNT_INC("[Perks] Bomb count effect timeout"),
        TIMEOUT_BOMB_IMMUNE("[Perks] Bomb immune effect timeout"),
        REMOTE_CONTROL_COUNT("[Perks] Number of Bomb remote controls (how many times player can use it)"),
        DEFAULT_PERKS("[Perks] Perks available in this game");

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
        integer(KILL_WALL_SCORE, 1);
        integer(KILL_MEAT_CHOPPER_SCORE, 10);
        integer(KILL_OTHER_HERO_SCORE, 20);
        integer(CATCH_PERK_SCORE, 5);
        integer(DIE_PENALTY, 30);
        integer(WIN_ROUND_SCORE, 30);

        bool(BIG_BADABOOM, false);
        integer(BOMBS_COUNT, 1);
        integer(BOMB_POWER, 3);
        int boardSize = 23;
        integer(BOARD_SIZE, boardSize);
        integer(DESTROY_WALL_COUNT, (boardSize * boardSize) / 10);
        integer(MEAT_CHOPPERS_COUNT, 5);

        bool(MULTIPLE, false);
        integer(PLAYERS_PER_ROOM, 5);
        // включен ли режим раундов
        bool(ROUNDS_ENABLED, true);
        // сколько тиков на 1 раунд
        integer(TIME_PER_ROUND, 200);
        // сколько тиков победитель будет сам оставаться после всех побежденных
        integer(TIME_FOR_WINNER, 1);
        // обратный отсчет перед началом раунда
        integer(TIME_BEFORE_START, 5);
        // сколько раундов (с тем же составом героев) на 1 матч
        integer(ROUNDS_PER_MATCH, 1);
        // сколько тиков должно пройти от начала раунда, чтобы засчитать победу
        integer(MIN_TICKS_FOR_WIN, 1);

        string(DEFAULT_PERKS, StringUtils.EMPTY);
        PerksSettingsWrapper perks =
                perksSettings()
                    .dropRatio(20) // Set value to 0% = perks is disabled.
                    .pickTimeout(30);
        int timeout = 30;
        perks.put(Elements.BOMB_REMOTE_CONTROL, 3, 1);
        perks.put(Elements.BOMB_BLAST_RADIUS_INCREASE, 2, timeout);
        perks.put(Elements.BOMB_IMMUNE, 0, timeout);
        perks.put(Elements.BOMB_COUNT_INCREASE, 4, timeout);
    }

    public Level getLevel() {
        return new Level() {
            @Override
            public int bombsCount() {
                return integer(BOMBS_COUNT);
            }
            
            @Override
            public int bombsPower() {
                return integer(BOMB_POWER);
            }

            @Override
            public int perksDropRate() {
                return integer(PERK_DROP_RATIO);
            }
        };
    }

    public Walls getWalls(Dice dice) {
        OriginalWalls originalWalls = new OriginalWalls(integerValue(BOARD_SIZE));
        MeatChoppers meatChoppers = new MeatChoppers(originalWalls, integerValue(MEAT_CHOPPERS_COUNT), dice);

        return new EatSpaceWalls(meatChoppers, integerValue(DESTROY_WALL_COUNT), dice);
    }

    public Hero getHero(Level level, Dice dice) {
        return new Hero(level, dice);
    }

    public PerksSettingsWrapper perksSettings() {
        return new PerksSettingsWrapper(this);
    }

    // TODO заинлайнить это все

    public GameSettings update(JSONObject json) {
        json.keySet().forEach(name -> {
            String key = Key.nameToKey(Keys.values(), name);
            getParameter(key).update(json.get(name));
        });
        return this;
    }

    public JSONObject asJson() {
        return new JSONObject(){{
            getParameters().forEach(parameter ->
                    put(Key.keyToName(Keys.values(), parameter.getName()), parameter.getValue()));
        }};
    }

}
