package com.codenjoy.dojo.client.local.ws;

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

import com.codenjoy.dojo.client.local.LocalGameRunner;
import com.codenjoy.dojo.services.AbstractGameType;

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

public class LocalWSGameServer {

    private static SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");

    public static void startGame(String game, AbstractGameType gameType) {
        String host = System.getProperty("host", "127.0.0.1");
        int port = Integer.valueOf(System.getProperty("port", "8080"));
        int timeout = Integer.valueOf(System.getProperty("timeout", "1000"));
        int waitForPlayers = Integer.valueOf(System.getProperty("waitFor", "0"));
        String log = System.getProperty("log", "output.txt");
        String showPlayers = System.getProperty("showPlayers", null);
        boolean logTime = Boolean.valueOf(System.getProperty("logTime", "true"));
        boolean logDisable = Boolean.valueOf(System.getProperty("logDisable", "false"));

        File file = setupLog(log);
        LocalGameRunner.out = setupOutput(file, logTime, logDisable);
        LocalGameRunner.out.accept("Log file is here: " + file.getAbsolutePath());

        LocalGameRunner.out.accept(String.format(
                "Run local WS server for %s on %s:%s\n",
                game, host, port));

        LocalGameRunner.out.accept("If you want to change something, please use command:\n" +
                "\twindows\n" +
                "\t\tjava -jar -Dhost=127.0.0.1 -Dport=8080 -Dtimeout=1000 " +
                "-DlogDisable=false -Dlog=\"output.txt\" -DlogTime=true -DshowPlayers=\"2,3\" " +
                "-Drandom=\"random-soul-string\" -DwaitFor=2 " +
                "-Dsettings=\"{'boardSize':11, 'bombPower':7}\"\n" +
                "\tlinux\n" +
                "\t\tjava -jar --host=127.0.0.1 --port=8080 --timeout=1000 " +
                "--logDisable=false --log=\"output.txt\" --logTime=true --showPlayers=\"2,3\" " +
                "--random=\"random-soul-string\" --waitFor=2 " +
                "--settings=\"{'boardSize':11, 'bombPower':7}\"\n");

        LocalGameRunner.waitForPlayers = waitForPlayers;
        LocalGameRunner.showPlayers = showPlayers;
        LocalWSGameRunner.run(gameType, host, port, timeout);
    }

    private static Consumer<String> setupOutput(File file, boolean logTime, boolean logDisable) {
        if (logDisable) {
            return message -> {};
        }

        return message -> {
            String time = format.format(Calendar.getInstance().getTime());

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
