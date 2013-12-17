package com.codenjoy.dojo.services;

import com.codenjoy.dojo.battlecity.services.BattlecityGame;
import com.codenjoy.dojo.bomberman.services.BombermanGame;
import com.codenjoy.dojo.loderunner.services.LoderunnerGame;
import com.codenjoy.dojo.minesweeper.services.MinesweeperGame;
import com.codenjoy.dojo.snake.services.SnakeGame;
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

    @Override
    public List<Class<? extends GameType>> getGames() {
        return Arrays.asList(SnakeGame.class,
                BombermanGame.class,
                MinesweeperGame.class,
                BattlecityGame.class,
                LoderunnerGame.class);
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
            return gameType.newInstance();
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
                players.removeAll();
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
