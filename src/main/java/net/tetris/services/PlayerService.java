package net.tetris.services;

import net.tetris.dom.Glass;
import net.tetris.dom.TetrisGame;
import net.tetris.dom.TetrisGlass;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

@Component("playerService")
public class PlayerService {
    @Autowired
    private ScreenSender screenSender;

    private List<Player> players = new ArrayList<>();
    private List<Glass> glasses = new ArrayList<>();
    private List<TetrisGame> games = new ArrayList<>();
    
    private ReadWriteLock lock = new ReentrantReadWriteLock();


    public Player addNewPlayer(final String name, final String callbackUrl) {
        lock.writeLock().lock();
        try {
            TetrisGlass glass = new TetrisGlass(TetrisGame.GLASS_WIDTH, TetrisGame.GLASS_HEIGHT);
            final TetrisGame game = new TetrisGame(new PlayerFigures(), new PlayerScores(),
                    glass);
            Player player = new Player(name, callbackUrl);
            players.add(player);
            glasses.add(glass);
            games.add(game);
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

            for (Player player : players) {
                requestControl(player);
            }
        } finally {
            lock.writeLock().unlock();
        }
    }

    private void requestControl(Player player) {

    }

}
