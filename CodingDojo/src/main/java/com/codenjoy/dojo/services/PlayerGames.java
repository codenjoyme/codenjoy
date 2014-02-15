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

        controller.registerPlayerTransport(player, new LazyJoystick(game, spy));
        playerGames.add(new PlayerGame(player, game, controller));
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

    @Override
    public void tick() {  // TODO потестить еще отдельно
        statistics.tick();

        for (PlayerGame playerGame : playerGames) {
            Game game = playerGame.getGame();
            if (game.isGameOver()) {
                game.newGame();
            }
        }

        List<GameType> gameTypes = getGameTypes();
        for (GameType gameType : gameTypes) {
            List<PlayerGame> games = getAll(gameType.gameName());
            if (gameType.isSingleBoardGame()) {
                if (!games.isEmpty()) {
                    games.iterator().next().getGame().tick();
                }
            } else {
                for (PlayerGame playerGame : games) {
                    playerGame.getGame().tick();
                }
            }
        }
    }
}
