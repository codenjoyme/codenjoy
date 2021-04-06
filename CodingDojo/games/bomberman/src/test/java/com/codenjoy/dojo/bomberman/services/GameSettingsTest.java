package com.codenjoy.dojo.bomberman.services;

/*-
 * #%L
 * Codenjoy - it's a dojo-like platform from developers to developers.
 * %%
 * Copyright (C) 2018 - 2020 Codenjoy
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

import com.codenjoy.dojo.bomberman.model.Elements;
import com.codenjoy.dojo.bomberman.model.perks.PerkSettings;
import com.codenjoy.dojo.bomberman.model.perks.PerksSettingsWrapper;
import com.codenjoy.dojo.utils.JsonUtils;
import org.json.JSONObject;
import org.junit.Test;

import java.util.LinkedHashMap;

import static org.junit.Assert.assertEquals;

public class GameSettingsTest {

    @Test
    public void shouldDefaultPerkSettings() {
        GameSettings settings = new GameSettings();
        PerksSettingsWrapper perksSettings = settings.perksSettings();

        assertEquals("{BOMB_BLAST_RADIUS_INCREASE=PerkSettings{value=2, timeout=30}, " +
                        "BOMB_COUNT_INCREASE=PerkSettings{value=4, timeout=30}, " +
                        "BOMB_IMMUNE=PerkSettings{value=0, timeout=30}, " +
                        "BOMB_REMOTE_CONTROL=PerkSettings{value=3, timeout=1}}",
                allPerkSettings(perksSettings).toString());
    }

    public LinkedHashMap<String, PerkSettings> allPerkSettings(PerksSettingsWrapper perksSettings) {
        return new LinkedHashMap<>() {{
            Elements.perks().forEach(it -> put(it.name(), perksSettings.get(it)));
        }};
    }

    @Test
    public void testUpdate() {
        // given
        GameSettings settings = new GameSettings();

        assertEquals("{\n" +
                "  'BIG_BADABOOM':false,\n" +
                "  'BOARD_SIZE':23,\n" +
                "  'BOMBS_COUNT':1,\n" +
                "  'BOMB_POWER':3,\n" +
                "  'CATCH_PERK_SCORE':5,\n" +
                "  'DEFAULT_PERKS':'r+ic',\n" +
                "  'DESTROY_WALL_COUNT':52,\n" +
                "  'DIE_PENALTY':30,\n" +
                "  'KILL_MEAT_CHOPPER_SCORE':10,\n" +
                "  'KILL_OTHER_HERO_SCORE':20,\n" +
                "  'KILL_WALL_SCORE':1,\n" +
                "  'MEAT_CHOPPERS_COUNT':5,\n" +
                "  'PERK_BOMB_BLAST_RADIUS_INC':2,\n" +
                "  'PERK_BOMB_COUNT_INC':4,\n" +
                "  'PERK_DROP_RATIO':20,\n" +
                "  'PERK_PICK_TIMEOUT':30,\n" +
                "  'REMOTE_CONTROL_COUNT':3,\n" +
                "  'ROUNDS_ENABLED':true,\n" +
                "  'ROUNDS_MIN_TICKS_FOR_WIN':1,\n" +
                "  'ROUNDS_PER_MATCH':1,\n" +
                "  'ROUNDS_PLAYERS_PER_ROOM':5,\n" +
                "  'ROUNDS_TIME':200,\n" +
                "  'ROUNDS_TIME_BEFORE_START':5,\n" +
                "  'ROUNDS_TIME_FOR_WINNER':1,\n" +
                "  'SEMIFINAL_ENABLED':false,\n" +
                "  'SEMIFINAL_LIMIT':50,\n" +
                "  'SEMIFINAL_PERCENTAGE':true,\n" +
                "  'SEMIFINAL_RESET_BOARD':true,\n" +
                "  'SEMIFINAL_SHUFFLE_BOARD':true,\n" +
                "  'SEMIFINAL_TIMEOUT':900,\n" +
                "  'TIMEOUT_BOMB_BLAST_RADIUS_INC':30,\n" +
                "  'TIMEOUT_BOMB_COUNT_INC':30,\n" +
                "  'TIMEOUT_BOMB_IMMUNE':30,\n" +
                "  'WIN_ROUND_SCORE':30\n" +
                "}", JsonUtils.prettyPrint(settings.asJson()));

        // when
        settings.update(new JSONObject("{\n" +
                "  'DIE_PENALTY':12,\n" +
                "  'PERK_BOMB_BLAST_RADIUS_INC':4,\n" +
                "  'PERK_DROP_RATIO':23,\n" +
                "  'ROUNDS_ENABLED':false,\n" +
                "  'ROUNDS_TIME_BEFORE_START':10,\n" +
                "  'SEMIFINAL_LIMIT':150,\n" +
                "  'SEMIFINAL_PERCENTAGE':false,\n" +
                "  'TIMEOUT_BOMB_COUNT_INC':12,\n" +
                "}"));

        // then
        assertEquals("{\n" +
                "  'BIG_BADABOOM':false,\n" +
                "  'BOARD_SIZE':23,\n" +
                "  'BOMBS_COUNT':1,\n" +
                "  'BOMB_POWER':3,\n" +
                "  'CATCH_PERK_SCORE':5,\n" +
                "  'DEFAULT_PERKS':'r+ic',\n" +
                "  'DESTROY_WALL_COUNT':52,\n" +
                "  'DIE_PENALTY':12,\n" +
                "  'KILL_MEAT_CHOPPER_SCORE':10,\n" +
                "  'KILL_OTHER_HERO_SCORE':20,\n" +
                "  'KILL_WALL_SCORE':1,\n" +
                "  'MEAT_CHOPPERS_COUNT':5,\n" +
                "  'PERK_BOMB_BLAST_RADIUS_INC':4,\n" +
                "  'PERK_BOMB_COUNT_INC':4,\n" +
                "  'PERK_DROP_RATIO':23,\n" +
                "  'PERK_PICK_TIMEOUT':30,\n" +
                "  'REMOTE_CONTROL_COUNT':3,\n" +
                "  'ROUNDS_ENABLED':false,\n" +
                "  'ROUNDS_MIN_TICKS_FOR_WIN':1,\n" +
                "  'ROUNDS_PER_MATCH':1,\n" +
                "  'ROUNDS_PLAYERS_PER_ROOM':5,\n" +
                "  'ROUNDS_TIME':200,\n" +
                "  'ROUNDS_TIME_BEFORE_START':10,\n" +
                "  'ROUNDS_TIME_FOR_WINNER':1,\n" +
                "  'SEMIFINAL_ENABLED':false,\n" +
                "  'SEMIFINAL_LIMIT':150,\n" +
                "  'SEMIFINAL_PERCENTAGE':false,\n" +
                "  'SEMIFINAL_RESET_BOARD':true,\n" +
                "  'SEMIFINAL_SHUFFLE_BOARD':true,\n" +
                "  'SEMIFINAL_TIMEOUT':900,\n" +
                "  'TIMEOUT_BOMB_BLAST_RADIUS_INC':30,\n" +
                "  'TIMEOUT_BOMB_COUNT_INC':12,\n" +
                "  'TIMEOUT_BOMB_IMMUNE':30,\n" +
                "  'WIN_ROUND_SCORE':30\n" +
                "}", JsonUtils.prettyPrint(settings.asJson()));

        // when
        settings.update(new JSONObject("{}"));

        // then
        assertEquals("{\n" +
                "  'BIG_BADABOOM':false,\n" +
                "  'BOARD_SIZE':23,\n" +
                "  'BOMBS_COUNT':1,\n" +
                "  'BOMB_POWER':3,\n" +
                "  'CATCH_PERK_SCORE':5,\n" +
                "  'DEFAULT_PERKS':'r+ic',\n" +
                "  'DESTROY_WALL_COUNT':52,\n" +
                "  'DIE_PENALTY':12,\n" +
                "  'KILL_MEAT_CHOPPER_SCORE':10,\n" +
                "  'KILL_OTHER_HERO_SCORE':20,\n" +
                "  'KILL_WALL_SCORE':1,\n" +
                "  'MEAT_CHOPPERS_COUNT':5,\n" +
                "  'PERK_BOMB_BLAST_RADIUS_INC':4,\n" +
                "  'PERK_BOMB_COUNT_INC':4,\n" +
                "  'PERK_DROP_RATIO':23,\n" +
                "  'PERK_PICK_TIMEOUT':30,\n" +
                "  'REMOTE_CONTROL_COUNT':3,\n" +
                "  'ROUNDS_ENABLED':false,\n" +
                "  'ROUNDS_MIN_TICKS_FOR_WIN':1,\n" +
                "  'ROUNDS_PER_MATCH':1,\n" +
                "  'ROUNDS_PLAYERS_PER_ROOM':5,\n" +
                "  'ROUNDS_TIME':200,\n" +
                "  'ROUNDS_TIME_BEFORE_START':10,\n" +
                "  'ROUNDS_TIME_FOR_WINNER':1,\n" +
                "  'SEMIFINAL_ENABLED':false,\n" +
                "  'SEMIFINAL_LIMIT':150,\n" +
                "  'SEMIFINAL_PERCENTAGE':false,\n" +
                "  'SEMIFINAL_RESET_BOARD':true,\n" +
                "  'SEMIFINAL_SHUFFLE_BOARD':true,\n" +
                "  'SEMIFINAL_TIMEOUT':900,\n" +
                "  'TIMEOUT_BOMB_BLAST_RADIUS_INC':30,\n" +
                "  'TIMEOUT_BOMB_COUNT_INC':12,\n" +
                "  'TIMEOUT_BOMB_IMMUNE':30,\n" +
                "  'WIN_ROUND_SCORE':30\n" +
                "}", JsonUtils.prettyPrint(settings.asJson()));

    }
}
