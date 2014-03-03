package com.codenjoy.dojo.services;

import com.codenjoy.dojo.hex.services.HexGame;
import com.codenjoy.dojo.services.lock.LockedGameType;
import org.reflections.Reflections;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;

/**
 * User: sanja
 * Date: 17.12.13
 * Time: 18:46
 */
@Component("gameService")
public class GameServiceImpl implements GameService {

    @Autowired private TimerService timer;
    @Autowired private PlayerService players;

    private final Reflections reflections = new Reflections("com.codenjoy.dojo");
    private Map<String, GameType> cache = new HashMap<String, GameType>();

    public GameServiceImpl() {
        for (Class<? extends GameType> aClass : getGameClasses()) {
            GameType gameType = loadGameType(aClass);
            cache.put(gameType.gameName(), gameType);
        }
    }

    private List<Class<? extends GameType>> getGameClasses() {
        List<Class<? extends GameType>> games = new LinkedList<Class<? extends GameType>>(reflections.getSubTypesOf(GameType.class));
        Collections.sort(games, new Comparator<Class<? extends GameType>>() {
            @Override
            public int compare(Class<? extends GameType> o1, Class<? extends GameType> o2) {
                return o1.getName().compareTo(o2.getName());
            }
        });
        games.remove(LockedGameType.class);
        games.remove(GameType.NULL.getClass());
        games.remove(HexGame.class); // TODO когда игрульку доделаем - включим ее!
        return games;
    }

    @Override
    public Set<String> getGameNames() {
        return cache.keySet();
    }

    @Override
    public Map<String, List<String>> getSprites() {
        Map<String, List<String>> result = new HashMap<String, List<String>>();
        for (Map.Entry<String, GameType> gameTypeEntry : cache.entrySet()) {
            List<String> sprites = new LinkedList<String>();

            GameType gameType = gameTypeEntry.getValue();

            for (Enum e : gameType.getPlots()) {
                sprites.add(e.name().toLowerCase());
            }

            result.put(gameType.gameName(), sprites);
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
            return new LockedGameType(cache.get(name));
        }

        return GameType.NULL;
    }
}
