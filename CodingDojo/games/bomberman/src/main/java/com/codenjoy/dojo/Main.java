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
import com.codenjoy.dojo.client.local.ws.LocalWSGameRunner;
import com.codenjoy.dojo.services.Dice;
import com.codenjoy.dojo.services.RandomDice;
import com.codenjoy.dojo.services.settings.SettingsImpl;
import com.codenjoy.dojo.utils.JsonUtils;
import org.json.JSONObject;

public class Main {

    public static void main(String[] args) {
        String host = System.getProperty("host", "127.0.0.1");
        int port = Integer.valueOf(System.getProperty("port", "8080"));
        int timeout = Integer.valueOf(System.getProperty("timeout", "1000"));
        String settingsJson = System.getProperty("settings", "{}");
        String game = "bomberman";

        Dice dice = new RandomDice();

        OptionGameSettings gameSettings = new OptionGameSettings(new SettingsImpl(), dice)
                .update(new JSONObject(settingsJson));

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

        gameSettings.update(new JSONObject("{\n" +
                "  'isMultiple':true,\n" +
                "  'roundSettings':{\n" +
                "    'roundsEnabled':false,\n" +
                "  },\n" +
                "}"));

        System.out.printf("Run local WS server for %s on %s:%s with settings:\n" +
                        "%s\n" +
                        "If you want to change something, please use command:\n" +
                        "java -jar -Dhost=127.0.0.1 -Dport=8080 -Dtimeout=1000 -Dsettings={'boardSize':11,'bombPower':7}\n\n",
                game, host, port, JsonUtils.prettyPrint(gameSettings.asJson()));

        LocalWSGameRunner.run(gameType, host, port, timeout);
    }
}
