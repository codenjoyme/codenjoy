package com.codenjoy.dojo.services;

import com.codenjoy.dojo.loderunner.services.LoderunnerGame;
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

    private GameType gameType;

    private GuiPlotColorDecoder decoder; // TODO как-то убрать его отсюда!

    @Autowired
    private TimerService timer;

    @Autowired
    private PlayerService players;

    public GameServiceImpl () {
        selectGame(LoderunnerGame.class.getSimpleName());
    }

    @Override
    public List<Class<? extends GameType>> getGames() {
        Reflections reflections = new Reflections("com.codenjoy.dojo");
        List<Class<? extends GameType>> games = new ArrayList<Class<? extends GameType>>(reflections.getSubTypesOf(GameType.class));
        Collections.sort(games, new Comparator<Class<? extends GameType>>() {
            @Override
            public int compare(Class<? extends GameType> o1, Class<? extends GameType> o2) {
                return o1.getName().compareTo(o2.getName());
            }
        });
        games.remove(LockedGameType.class);
        return games;
    }

    @Override
    public GameType getSelectedGame() {
        return gameType;
    }

    @Override
    public GuiPlotColorDecoder getDecoder() {
        return decoder;
    }

    @Override
    public List<String> getGameNames() {
        List<String> result = new LinkedList<String>();
        for (Class<? extends GameType> game : getGames()) {
            result.add(game.getSimpleName());
        }
        return result;
    }

    @Override
    public Map<String, List<String>> getSprites() {
        Map<String, List<String>> result = new HashMap<String, List<String>>();
        for (Class<? extends GameType> gameType : getGames()) {
            List<String> sprites = new LinkedList<String>();
            GameType game = loadGameType(gameType);

            for (Enum e : game.getPlots()) {
                sprites.add(e.name().toLowerCase());
            }

            result.put(game.gameName(), sprites);
        }
        return result;
    }

    private GameType loadGameType(Class<? extends GameType> gameType) {
        try {
            return new LockedGameType(gameType.newInstance());
        } catch (InstantiationException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void selectGame(String name) {
        for (Class<? extends GameType> game : getGames()) {
            if (game.getSimpleName().equals(name)) {
                if (players != null) {
                    players.removeAll();
                }
                try {
                    gameType = game.newInstance();
                    decoder = new GuiPlotColorDecoder(gameType.getPlots());
                    if (timer != null) {
                        timer.pause();
                    }
                } catch (InstantiationException e) {
                    throw new RuntimeException(e);
                } catch (IllegalAccessException e) {
                    throw new RuntimeException(e);
                }
                return;
            }
        }
    }
}
