package com.codenjoy.dojo.client.local.ws;

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
                "-Dsettings=\"{'boardSize':11, 'bombPower':7}\"\n" +
                "\tlinux\n" +
                "\t\tjava -jar --host=127.0.0.1 --port=8080 --timeout=1000 " +
                "--logDisable=false --log=\"output.txt\" --logTime=true --showPlayers=\"2,3\" " +
                "--settings=\"{'boardSize':11, 'bombPower':7}\"\n");

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
