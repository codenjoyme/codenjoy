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
import com.codenjoy.dojo.client.LocalGameRunner;
import com.codenjoy.dojo.client.local.ws.LocalWSGameRunner;
import com.codenjoy.dojo.services.Dice;
import com.codenjoy.dojo.services.RandomDice;
import com.codenjoy.dojo.services.settings.SettingsImpl;
import com.codenjoy.dojo.utils.JsonUtils;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public class Main {

    private static SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");

    public static void main(String[] args) {
        String host = System.getProperty("host", "127.0.0.1");
        int port = Integer.valueOf(System.getProperty("port", "8080"));
        int timeout = Integer.valueOf(System.getProperty("timeout", "1000"));
        String log = System.getProperty("log", "output.txt");
        String showPlayers = System.getProperty("showPlayers", null);
        boolean logTime = Boolean.valueOf(System.getProperty("logTime", "true"));
        String settingsString = System.getProperty("settings", "{}");
        String game = "bomberman";

        File file = setupLog(log);
        LocalGameRunner.out = setupOutput(file, logTime);
        LocalGameRunner.out.accept("Log file is here: " + file.getAbsolutePath());

        Dice dice = new RandomDice();

        JSONObject settings = new JSONObject(settingsString);
        OptionGameSettings gameSettings = new OptionGameSettings(new SettingsImpl(), dice)
                .update(settings);

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

        LocalGameRunner.out.accept(String.format(
                "Run local WS server for %s on %s:%s with settings:\n%s",
                game, host, port, JsonUtils.prettyPrint(gameSettings.asJson())));

        LocalGameRunner.out.accept("If you want to change something, please use command:\n" +
                        "java -jar -Dhost=127.0.0.1 -Dport=8080 -Dtimeout=1000 " +
                        "-Dlog=\"output.txt\" -DlogTime=true -DshowPlayers=\"2,3\" " +
                        "-Dsettings=\"{'boardSize':11, 'bombPower':7}\"\n");

        LocalGameRunner.showPlayers = showPlayers;
        LocalWSGameRunner.run(gameType, host, port, timeout);
    }

    private static Consumer<String> setupOutput(File file, boolean logTime) {
        return message -> {
            String time = Main.format.format(Calendar.getInstance().getTime());

            if (logTime) {
                message = Arrays.stream(message.split("\n"))
                        .map(line -> time + ":\t" + line)
                        .collect(Collectors.joining("\n"));
            }

            message += "\n";

            System.out.print(message);
            try {
                Files.write(file.toPath(),
                        message.getBytes(Charset.forName("UTF8")),
                        StandardOpenOption.APPEND);
            } catch (IOException e) {
                e.printStackTrace();
            }
        };
    }

    private static File setupLog(String log) {
        File file = new File(log);

        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return file;
    }
}
