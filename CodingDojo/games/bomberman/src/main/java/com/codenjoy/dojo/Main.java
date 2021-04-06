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

import com.codenjoy.dojo.bomberman.services.GameRunner;
import com.codenjoy.dojo.bomberman.services.GameSettings;
import com.codenjoy.dojo.client.local.LocalGameRunner;
import com.codenjoy.dojo.client.local.ws.LocalWSGameServer;
import com.codenjoy.dojo.services.Dice;
import com.codenjoy.dojo.services.RandomDice;
import com.codenjoy.dojo.services.round.RoundSettings;
import com.codenjoy.dojo.services.settings.SettingsReader;
import com.codenjoy.dojo.utils.JsonUtils;
import org.apache.commons.lang3.StringUtils;
import org.json.JSONObject;

import static com.codenjoy.dojo.services.round.RoundSettings.Keys.ROUNDS_ENABLED;

public class Main {

    public static void main(String[] args) {
        LocalGameRunner.out.accept("Please run this stuff with VM options:\n" +
                "\t\t-Dsettings={'ROUNDS_ENABLED':false, ...}\n" +
                "\t\t-Drandom=SEED_STRING\n");

        String game = "bomberman";
        String settingsString = System.getProperty("settings", "{}");
        String randomSeed = System.getProperty("random", null);

        Dice dice = getDice(randomSeed);

        JSONObject settings = new JSONObject(settingsString);

        GameSettings gameSettings = new GameSettings()
                .update(settings);

        if (!contains(settings, ROUNDS_ENABLED)) {
            String json = "{\n" +
                    "  'ROUNDS_ENABLED':false\n" +
                    "}\n";
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
            public GameSettings getSettings() {
                return gameSettings;
            }
        };

        LocalWSGameServer.startGame(game, gameType);
    }

    private static boolean contains(JSONObject settings, SettingsReader.Key key) {
        return settings.has(SettingsReader.keyToName(RoundSettings.allRoundsKeys(), key.key()));
    }

    private static Dice getDice(String randomSeed) {
        if (StringUtils.isEmpty(randomSeed)) {
            return new RandomDice();
        } else {
            LocalGameRunner.printDice = false;
            LocalGameRunner.printConversions = false;
            return LocalGameRunner.getDice(randomSeed, 100, 10000);
        }
    }

}
