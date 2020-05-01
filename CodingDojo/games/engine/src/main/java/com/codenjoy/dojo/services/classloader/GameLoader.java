package com.codenjoy.dojo.services.classloader;

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

import com.codenjoy.dojo.services.GameType;
import com.codenjoy.dojo.services.Point;
import com.codenjoy.dojo.services.multiplayer.GameField;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.*;

import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toMap;

@Slf4j
public class GameLoader {

    public Map<String, Class> loadGames(File directory) {
        File[] array = directory.listFiles();
        if (array == null) {
            log.warn("Plugins folder '{}' not exists. Skipping...", directory);
            return new HashMap<>();
        }

        List<GameLocator> jars = Arrays.stream(array)
                .filter(it -> it.getName().endsWith(".jar"))
                .map(it -> new GameLocator(it.getName().split("[.-]")[0], it.getAbsolutePath()))
                .collect(toList());

        return loadAll(jars);
    }

    public Map<String, Class> loadAll(List<GameLocator> jars) {
        return jars.stream()
                .map(this::load)
                .collect(toMap(it -> it.getName(), it -> it.getClazz()));
    }

    @SneakyThrows
    public GameLocator load(GameLocator jar) {
        File myJar = new File(jar.getJarPath());
        URLClassLoader classLoader = new URLClassLoader(
                new URL[] {myJar.toURI().toURL()},
                GameLoader.class.getClassLoader()
        );
        jar.setClazz(Class.forName("com.codenjoy.dojo." + jar.getName()
                        + ".services.GameRunner", true, classLoader));
        return jar;
    }

    @SneakyThrows
    public static void main(String[] args) {
        File directory = new File("C:\\Java\\iCanCode\\codenjoy\\CodingDojo\\external");
        GameType gameType = (GameType)new GameLoader().loadGames(directory).get("a2048").newInstance();
        GameField game = gameType.createGame(0);
        game.newGame(gameType.createPlayer(event -> {}, "id"));
        game.tick();
        Iterator<? extends Point> iterator = game.reader().elements().iterator();
        System.out.println(iterator.next());
    }
}
