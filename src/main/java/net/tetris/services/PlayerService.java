package net.tetris.services;

import net.tetris.dom.TetrisGame;
import net.tetris.dom.TetrisGlass;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

@Component("playerService")
public class PlayerService {
    @Autowired
    private ScreenSender screenSender;

    private List<Player> players = new LinkedList<>();
    private List<TetrisGame> playerGames = new LinkedList<>();

    private ReadWriteLock lock = new ReentrantReadWriteLock();


    public Player addNewPlayer(final String name, final String callbackUrl) {
        lock.writeLock().lock();
        try {
            final TetrisGame game = new TetrisGame(new PlayerFigures(), new PlayerScores(),
                    new TetrisGlass(TetrisGame.GLASS_WIDTH, TetrisGame.GLASS_HEIGHT));
            Player player = new Player(name, callbackUrl);
            players.add(player);
            return player;
        } finally {
            lock.writeLock().unlock();
        }
    }

    public Map<Player, List<Plot>> nextStepForAllGames() {
        lock.writeLock().lock();
        try {
            HashMap<Player, List<Plot>> map = new HashMap<>();
            screenSender.sendUpdates(map);
        } finally {
            lock.writeLock().unlock();
        }
        return null;
    }

}
