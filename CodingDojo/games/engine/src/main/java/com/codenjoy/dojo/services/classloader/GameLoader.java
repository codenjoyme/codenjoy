package com.codenjoy.dojo.services.classloader;

import com.codenjoy.dojo.services.GameType;
import com.codenjoy.dojo.services.Point;
import com.codenjoy.dojo.services.multiplayer.GameField;
import lombok.SneakyThrows;

import java.io.File;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import static java.util.stream.Collectors.toList;

public class GameLoader {

    public List<GameType> loadGames() {
        String dir = "C:\\Java\\iCanCode\\codenjoy\\CodingDojo\\games";
        return loadAll(
                new GameLocator("a2048", dir + "\\a2048\\target\\a2048-engine.jar"),
                new GameLocator("battlecity", dir + "\\battlecity\\target\\battlecity-engine.jar")
        );
    }

    public List<GameType> loadAll(GameLocator... jars) {
        return Arrays.stream(jars)
                .map(this::load)
                .collect(toList());
    }

    @SneakyThrows
    public GameType load(GameLocator jar) {
        File myJar = new File(jar.getJarPath());
        URLClassLoader classLoader = new URLClassLoader(
                new URL[] {myJar.toURI().toURL()},
                GameLoader.class.getClassLoader()
        );
        Class clazz = Class.forName("com.codenjoy.dojo." + jar.getName()
                        + ".services.GameRunner", true, classLoader);
        return (GameType)clazz.newInstance();
    }

    public static void main(String[] args) {
        GameType gameType = new GameLoader().loadGames().get(0);
        GameField game = gameType.createGame(0);
        game.newGame(gameType.createPlayer(event -> {}, "id"));
        game.tick();
        Iterator<? extends Point> iterator = game.reader().elements().iterator();
        System.out.println(iterator.next());
    }
}
