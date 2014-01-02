package com.codenjoy.dojo.services;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 * User: sanja
 * Date: 27.12.13
 * Time: 21:39
 */
public class PlayerGames implements Iterable<PlayerGame> {

    private List<PlayerGame> playerGames = new LinkedList<PlayerGame>();

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
        controller.registerPlayerTransport(player, new LazyJoystick(game));
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

    public List<GameType> getGameTypes() {        // TODO потестить
        List<GameType> result = new LinkedList<GameType>();

        for (PlayerGame playerGame : playerGames) {
            GameType gameType = playerGame.getPlayer().getGameType();
            if (!result.contains(gameType)) {
                result.add(gameType);
            }
        }

        return result;
    }
}
