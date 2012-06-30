package net.tetris.services;

import net.tetris.dom.Glass;
import net.tetris.dom.ScoreBoard;
import net.tetris.dom.TetrisGame;
import net.tetris.dom.TetrisGlass;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

@Component("playerService")
public class PlayerService {
    private static Logger logger = LoggerFactory.getLogger(PlayerService.class);

    @Autowired
    private ScreenSender screenSender;

    @Autowired
    private PlayerController playerController;

    private List<Player> players = new ArrayList<>();
    private List<Glass> glasses = new ArrayList<>();
    private List<TetrisGame> games = new ArrayList<>();
    private List<ScoreBoard> scores = new ArrayList<>();

    private ReadWriteLock lock = new ReentrantReadWriteLock();


    public Player addNewPlayer(final String name, final String callbackUrl) {
        lock.writeLock().lock();
        try {
            PlayerScores playerScores = new PlayerScores();
            TetrisGlass glass = new TetrisGlass(TetrisGame.GLASS_WIDTH, TetrisGame.GLASS_HEIGHT, playerScores);
            final TetrisGame game = new TetrisGame(new PlayerFigures(), playerScores,
                    glass);
            Player player = new Player(name, callbackUrl);
            players.add(player);
            glasses.add(glass);
            games.add(game);
            scores.add(playerScores);
            return player;
        } finally {
            lock.writeLock().unlock();
        }
    }

    public void nextStepForAllGames() {
        lock.writeLock().lock();
        try {
            for (TetrisGame game : games) {
                game.nextStep();
            }

            HashMap<Player, List<Plot>> map = new HashMap<>();
            for (int i = 0; i < glasses.size(); i++) {
                Glass glass = glasses.get(i);
                map.put(players.get(i), glass.getPlots());
            }

            screenSender.sendUpdates(map);

            for (int i = 0; i < players.size(); i++) {
                Player player = players.get(i);
                TetrisGame game = games.get(i);
                try {
                    if (game.getCurrentFigureType() == null) {
                        continue;
                    }
                    playerController.requestControl(player, game.getCurrentFigureType(), game.getCurrentFigureX(),
                            game.getCurrentFigureY(), game);
                } catch (IOException e) {
                    logger.error("Unable to send control request to player " + player.getName() +
                            " URL: " + player.getCallbackUrl(), e);
                }
            }
        } finally {
            lock.writeLock().unlock();
        }
    }


    public List<Player> getPlayers() {
        lock.readLock().lock();
        try {
            return Collections.unmodifiableList(players);
        } finally {
            lock.readLock().unlock();
        }
    }

    public boolean alreadyRegistered(String playerName) {
        lock.readLock().lock();
        try {
            return findPlayer(playerName) != null;
        } finally {
            lock.readLock().unlock();
        }
    }

    public Player findPlayer(String playerName) {
        lock.readLock().lock();
        try {
            for (Player player : players) {
                if (player.getName().equals(playerName)) {
                    return player;
                }
            }
            return null;
        } finally {
            lock.readLock().unlock();
        }
    }

    public void updatePlayer(Player player) {
        lock.writeLock().lock();
        try {
            for (Player playerToUpdate : players) {
                if (playerToUpdate.getName().equals(player.getName())) {
                    playerToUpdate.setCallbackUrl(player.getCallbackUrl());
                    return;
                }
            }
        } finally {
            lock.writeLock().unlock();
        }
    }

    public void clear() {
        lock.writeLock().lock();
        try {
            players.clear();
            games.clear();
            glasses.clear();
        } finally {
            lock.writeLock().unlock();
        }
    }
}
