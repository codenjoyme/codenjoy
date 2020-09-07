package com.codenjoy.dojo;

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

import com.codenjoy.dojo.bomberman.model.GameSettings;
import com.codenjoy.dojo.bomberman.services.GameRunner;
import com.codenjoy.dojo.bomberman.services.OptionGameSettings;
import com.codenjoy.dojo.client.local.LocalGameRunner;
import com.codenjoy.dojo.client.local.ws.LocalWSGameServer;
import com.codenjoy.dojo.services.Dice;
import com.codenjoy.dojo.services.RandomDice;
import com.codenjoy.dojo.services.settings.SettingsImpl;
import com.codenjoy.dojo.utils.JsonUtils;
import org.apache.commons.lang3.StringUtils;
import org.json.JSONObject;

public class Main {

    public static void main(String[] args) {
        String game = "bomberman";
        String settingsString = System.getProperty("settings", "{}");
        String randomSoul = System.getProperty("random", null);

        Dice dice = getDice(randomSoul);

        JSONObject settings = new JSONObject(settingsString);

        OptionGameSettings gameSettings = new OptionGameSettings(new SettingsImpl(), dice)
                .update(settings);

        if (!settings.has("isMultiple") && !settings.has("roundSettings")) {
            String json = "{\n" +
                    "  'isMultiple':true,\n" +
                    "  'roundSettings':{\n" +
                    "    'roundsEnabled':false,\n" +
                    "  },\n" +
                    "}";
            LocalGameRunner.out.accept("Simple mode! Hardcoded: \n" + json);
            gameSettings.update(new JSONObject(json));
        }

        LocalGameRunner.out.accept("Current settings:\n" +
                JsonUtils.prettyPrint(gameSettings.asJson()));

        GameRunner gameType = new GameRunner() {
            @Override
            public Dice getDice() {
                return dice;
            }

            @Override
            protected GameSettings getGameSettings() {
                return gameSettings;
            }
        };

        LocalWSGameServer.startGame(game, gameType);
    }

    private static Dice getDice(String randomSoul) {
        if (StringUtils.isEmpty(randomSoul)) {
            return new RandomDice();
        } else {
            LocalGameRunner.printDice = false;
            LocalGameRunner.printConversions = false;
            return LocalGameRunner.getDice(LocalGameRunner.generateXorShift(randomSoul, 100, 10000));
        }
    }

}
