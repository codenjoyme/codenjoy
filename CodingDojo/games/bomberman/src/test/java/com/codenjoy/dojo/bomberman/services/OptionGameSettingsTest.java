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
import com.codenjoy.dojo.services.Dice;
import com.codenjoy.dojo.services.RandomDice;
import com.codenjoy.dojo.services.settings.Settings;
import com.codenjoy.dojo.services.settings.SettingsImpl;
import com.codenjoy.dojo.utils.JsonUtils;
import org.json.JSONObject;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.junit.Assert.assertEquals;

@RunWith(MockitoJUnitRunner.class)
public class OptionGameSettingsTest {
    @Mock
    private Dice dice;
    @Mock
    Settings settings;

    @Test
    @Ignore("TODO: mock GameSettings properly")
    public void shouldBombermanContainPerksSettings_whenCreated() {
        PerkSettings perkSettings = PerksSettingsWrapper.getPerkSettings(Elements.BOMB_IMMUNE);

        assertPerkSettings(Elements.BOMB_BLAST_RADIUS_INCREASE, perkSettings, 2, 10);
        assertPerkSettings(Elements.BOMB_COUNT_INCREASE, perkSettings, 3, 10);
        assertPerkSettings(Elements.BOMB_IMMUNE, perkSettings, 0, 10);
        assertPerkSettings(Elements.BOMB_REMOTE_CONTROL, perkSettings, 0, 3);
    }

    private boolean assertPerkSettings(Elements perk, PerkSettings perkSettings, int value, int timeout) {
        String message = "";
        String defaultValueErrorPattern = "%s expected default value = %d, but found it = %d \n";
        String defaultTimeoutErrorPattern = "%s expected default timeout = %d, but found it = %d \n";

        if(perkSettings.value() != value) {
            message += String.format(defaultValueErrorPattern, perk.name(), value, perkSettings.value());
        }

        if(perkSettings.timeout() != timeout) {
            message += String.format(defaultTimeoutErrorPattern, perk.name(), timeout, perkSettings.timeout());
        }

        if(message.isEmpty()) {
            return true;
        } else {
            throw  new AssertionError(message);
        }
    }

    @Test
    public void testUpdate() {
        // given
        OptionGameSettings settings = new OptionGameSettings(new SettingsImpl(), new RandomDice());

        assertEquals("{\n" +
                "  'boardSize':23,\n" +
                "  'bombPower':3,\n" +
                "  'bombsCount':1,\n" +
                "  'catchPerkScore':5,\n" +
                "  'destroyWallCount':52,\n" +
                "  'diePenalty':30,\n" +
                "  'isBigBadaboom':false,\n" +
                "  'isMultiple':false,\n" +
                "  'killMeatChopperScore':10,\n" +
                "  'killOtherHeroScore':20,\n" +
                "  'killWallScore':1,\n" +
                "  'meatChoppersCount':5,\n" +
                "  'perkBombBlastRadiusInc':2,\n" +
                "  'perkBombCountInc':4,\n" +
                "  'perkDropRatio':20,\n" +
                "  'perkPickTimeout':30,\n" +
                "  'playersPerRoom':5,\n" +
                "  'remoteControlCount':3,\n" +
                "  'roundSettings':{\n" +
                "    'minTicksForWin':1,\n" +
                "    'roundsEnabled':true,\n" +
                "    'roundsPerMatch':1,\n" +
                "    'timeBeforeStart':5,\n" +
                "    'timeForWinner':1,\n" +
                "    'timePerRound':200\n" +
                "  },\n" +
                "  'timeoutBombBlastRadiusInc':30,\n" +
                "  'timeoutBombCountInc':30,\n" +
                "  'timeoutBombImmune':30,\n" +
                "  'winRoundScore':30\n" +
                "}", JsonUtils.prettyPrint(settings.asJson()));

        // when
        settings.update(new JSONObject("{\n" +
                "  'diePenalty':12,\n" +
                "  'isMultiple':true,\n" +
                "  'perkBombBlastRadiusInc':4,\n" +
                "  'perkDropRatio':23,\n" +
                "  'roundSettings':{\n" +
                "    'roundsEnabled':false,\n" +
                "    'timeBeforeStart':10,\n" +
                "  },\n" +
                "  'timeoutBombCountInc':12,\n" +
                "}"));

        // then
        assertEquals("{\n" +
                "  'boardSize':23,\n" +
                "  'bombPower':3,\n" +
                "  'bombsCount':1,\n" +
                "  'catchPerkScore':5,\n" +
                "  'destroyWallCount':52,\n" +
                "  'diePenalty':12,\n" +
                "  'isBigBadaboom':false,\n" +
                "  'isMultiple':true,\n" +
                "  'killMeatChopperScore':10,\n" +
                "  'killOtherHeroScore':20,\n" +
                "  'killWallScore':1,\n" +
                "  'meatChoppersCount':5,\n" +
                "  'perkBombBlastRadiusInc':4,\n" +
                "  'perkBombCountInc':4,\n" +
                "  'perkDropRatio':23,\n" +
                "  'perkPickTimeout':30,\n" +
                "  'playersPerRoom':5,\n" +
                "  'remoteControlCount':3,\n" +
                "  'roundSettings':{\n" +
                "    'minTicksForWin':1,\n" +
                "    'roundsEnabled':false,\n" +
                "    'roundsPerMatch':1,\n" +
                "    'timeBeforeStart':10,\n" +
                "    'timeForWinner':1,\n" +
                "    'timePerRound':200\n" +
                "  },\n" +
                "  'timeoutBombBlastRadiusInc':30,\n" +
                "  'timeoutBombCountInc':12,\n" +
                "  'timeoutBombImmune':30,\n" +
                "  'winRoundScore':30\n" +
                "}", JsonUtils.prettyPrint(settings.asJson()));

        // when
        settings.update(new JSONObject("{}"));

        // then
        assertEquals("{\n" +
                "  'boardSize':23,\n" +
                "  'bombPower':3,\n" +
                "  'bombsCount':1,\n" +
                "  'catchPerkScore':5,\n" +
                "  'destroyWallCount':52,\n" +
                "  'diePenalty':12,\n" +
                "  'isBigBadaboom':false,\n" +
                "  'isMultiple':true,\n" +
                "  'killMeatChopperScore':10,\n" +
                "  'killOtherHeroScore':20,\n" +
                "  'killWallScore':1,\n" +
                "  'meatChoppersCount':5,\n" +
                "  'perkBombBlastRadiusInc':4,\n" +
                "  'perkBombCountInc':4,\n" +
                "  'perkDropRatio':23,\n" +
                "  'perkPickTimeout':30,\n" +
                "  'playersPerRoom':5,\n" +
                "  'remoteControlCount':3,\n" +
                "  'roundSettings':{\n" +
                "    'minTicksForWin':1,\n" +
                "    'roundsEnabled':false,\n" +
                "    'roundsPerMatch':1,\n" +
                "    'timeBeforeStart':10,\n" +
                "    'timeForWinner':1,\n" +
                "    'timePerRound':200\n" +
                "  },\n" +
                "  'timeoutBombBlastRadiusInc':30,\n" +
                "  'timeoutBombCountInc':12,\n" +
                "  'timeoutBombImmune':30,\n" +
                "  'winRoundScore':30\n" +
                "}", JsonUtils.prettyPrint(settings.asJson()));

    }
}
