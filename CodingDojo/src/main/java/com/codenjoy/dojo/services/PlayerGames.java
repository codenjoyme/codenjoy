package com.codenjoy.dojo.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 * User: sanja
 * Date: 27.12.13
 * Time: 21:39
 */
@Component("playerGames")
public class PlayerGames implements Iterable<PlayerGame>, Tickable {

    public static final int TICKS_FOR_REMOVE = 60*15; // 15 минут без игры - дисквалификация
    private List<PlayerGame> playerGames = new LinkedList<PlayerGame>();

    @Autowired Statistics statistics;

    public void remove(Player player) {
        int index = playerGames.indexOf(player);
        if (index == -1) return;
        playerGames.remove(index).remove();
    }

    public PlayerGame get(String playerName) {
        for (PlayerGame playerGame : playerGames) {
            if (playerGame.getPlayer().getName().equals(playerName)) {
                return playerGame;
            }
        }
        return PlayerGame.NULL;
    }

    public void add(Player player, Game game, PlayerController controller) {
        PlayerSpy spy = statistics.newPlayer(player);

        LazyJoystick joystick = new LazyJoystick(game, spy);
        controller.registerPlayerTransport(player, joystick);
        playerGames.add(new PlayerGame(player, game, controller, joystick));
    }

    public boolean isEmpty() {
        return playerGames.isEmpty();
    }

    @Override
    public Iterator<PlayerGame> iterator() {
        return playerGames.iterator();
    }

    public List<Player> players() {
        List<Player> result = new ArrayList<Player>(playerGames.size());

        for (PlayerGame playerGame : playerGames) {
            result.add(playerGame.getPlayer());
        }

        return result;
    }

    public int size() {
        return playerGames.size();
    }

    public void clear() {
        for (PlayerGame playerGame : playerGames) {
            playerGame.remove();
        }
        playerGames.clear();
    }

    public List<PlayerGame> getAll(String gameType) {
        List<PlayerGame> result = new LinkedList<PlayerGame>();

        for (PlayerGame playerGame : playerGames) {
            if (playerGame.getPlayer().getGameName().equals(gameType)) {
                result.add(playerGame);
            }
        }

        return result;
    }

    public List<GameType> getGameTypes() {
        List<GameType> result = new LinkedList<GameType>();

        for (PlayerGame playerGame : playerGames) {
            GameType gameType = playerGame.getPlayer().getGameType();
            if (!result.contains(gameType)) {
                result.add(gameType);
            }
        }

        return result;
    }

    private void quietTick(Tickable tickable) {
        try {
            tickable.tick();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void tick() {
        for (PlayerGame playerGame : playerGames) {
            quietTick(playerGame);
        }

        quietTick(statistics);

        removeNotActivePlayers();

        for (PlayerGame playerGame : playerGames) {
            final Game game = playerGame.getGame();
            if (game.isGameOver()) {
                quietTick(new Tickable() {
                    @Override
                    public void tick() {
                        game.newGame();
                    }
                });
            }
        }

        List<GameType> gameTypes = getGameTypes();  // TODO потестить еще отдельно
        for (GameType gameType : gameTypes) {
            List<PlayerGame> games = getAll(gameType.gameName());
            if (gameType.isSingleBoardGame()) {
                if (!games.isEmpty()) {
                    quietTick(games.iterator().next().getGame());
                }
            } else {
                for (PlayerGame playerGame : games) {
                    quietTick(playerGame.getGame());
                }
            }
        }
    }

    private void removeNotActivePlayers() {
        for (Player player : statistics.getPlayers(Statistics.WAIT_TICKS_MORE_OR_EQUALS, TICKS_FOR_REMOVE)) {
            remove(player);
        }
    }
}
