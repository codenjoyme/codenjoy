package com.codenjoy.dojo.services;

/*-
 * #%L
 * Codenjoy - it's a dojo-like platform from developers to developers.
 * %%
 * Copyright (C) 2016 Codenjoy
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


import org.reflections.Reflections;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;

@Component("gameService")
public class GameServiceImpl implements GameService {

    @Autowired private TimerService timer;
    @Autowired private PlayerService players;

    private Map<String, GameType> cache = new TreeMap<>();

    public GameServiceImpl() {
        for (Class<? extends GameType> aClass : getGameClasses()) {
            GameType gameType = loadGameType(aClass);
            cache.put(gameType.name(), gameType);
        }
    }

    private List<Class<? extends GameType>> getGameClasses() {
        List<Class<? extends GameType>> games = new LinkedList<>();
        games.addAll(findInPackage("com"));
        games.addAll(findInPackage("org"));
        games.addAll(findInPackage("net"));
        Collections.sort(games, Comparator.comparing(Class::getName));
        games.remove(NullGameType.class);
        games.remove(AbstractGameType.class);
        return games;
    }

    private Collection<? extends Class<? extends GameType>> findInPackage(String packageName) {
        return new Reflections(packageName).getSubTypesOf(GameType.class);
    }

    @Override
    public Set<String> getGameNames() {
        return cache.keySet();
    }

    @Override
    public Map<String, List<String>> getSprites() {
        Map<String, List<String>> result = new TreeMap<String, List<String>>();
        for (Map.Entry<String, GameType> gameTypeEntry : cache.entrySet()) {
            List<String> sprites = new LinkedList<String>();

            GameType gameType = gameTypeEntry.getValue();

            for (Enum e : gameType.getPlots()) {
                sprites.add(e.name().toLowerCase());
            }

            result.put(gameType.name(), sprites);
        }
        return result;
    }

    private GameType loadGameType(Class<? extends GameType> gameType) {
        try {
            return gameType.newInstance();
        } catch (InstantiationException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public GameType getGame(String name) {   // TODO потестить
        if (cache.containsKey(name)) {
            return cache.get(name);
        }

        return NullGameType.INSTANCE;
    }
}
