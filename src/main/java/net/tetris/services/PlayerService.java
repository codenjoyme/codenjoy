package net.tetris.services;

import akka.actor.ActorSystem;
import akka.japi.Creator;
import net.tetris.dom.Console;
import net.tetris.dom.TetrisGame;
import net.tetris.dom.TetrisGlass;

import java.util.*;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class PlayerService {
    private List<Player> players = new LinkedList<>();
    private List<Console> playerConsoles = new LinkedList<>();
    private List<TetrisGame> playerGames = new LinkedList<>();

    private ActorSystem system;

    private ReadWriteLock lock = new ReentrantReadWriteLock();

    public void init() {
        system = ActorSystem.create("tetris");
    }

    public Player addNewPlayer(final String name, final String callbackUrl) {
        lock.writeLock().lock();
        try {
            final PlayerConsole screen = new PlayerConsole();
            final TetrisGame game = new TetrisGame(screen, new PlayerFigures(), new PlayerScores(),
                    new TetrisGlass(TetrisGame.GLASS_WIDTH, TetrisGame.GLASS_HEIGHT));
/*

            TetrisGameWorker tetrisGameWorker = new TetrisGameWorker(game);

            final GameWorker gameWorker = TypedActor.get(system).typedActorOf(new TypedProps<>(GameWorker.class,
                    new TetrisGameWorkerCreator(tetrisGameWorker)), "gameWorker_" + name);

            SenderWorker sendWorker = TypedActor.get(system).typedActorOf(new TypedProps<>(SenderWorker.class,
                    new StatusSenderWorkerCreator(name, callbackUrl, gameWorker, screen)), "senderWorker_" + name);
            tetrisGameWorker.setSender(sendWorker);

*/
            Player player = new Player(name, callbackUrl);
            players.add(player);
            return player;
        } finally {
            lock.writeLock().unlock();
        }
    }

    public void destroy() {
        system.shutdown();
    }

    public Map<Player, List<Plot>> nextStepForAllGames() {
        lock.writeLock().lock();
        try {
            Map<Player, List<Plot>> playersScreens = new HashMap<>();
            for (int i = 0; i < players.size(); i++) {
                Player player = players.get(i);
                playerGames.get(i).nextStep();
                Console console = playerConsoles.get(i);
                console.showChangesToPlayer();
                playersScreens.put(player, console.getPlots());
            }
            return playersScreens;
        } finally {
            lock.writeLock().unlock();
        }
    }

    private static class TetrisGameWorkerCreator implements Creator<TetrisGameWorker> {
        private final TetrisGameWorker tetrisGameWorker;

        public TetrisGameWorkerCreator(TetrisGameWorker tetrisGameWorker) {
            this.tetrisGameWorker = tetrisGameWorker;
        }

        @Override
        public TetrisGameWorker create() {
            return tetrisGameWorker;
        }
    }

    private static class StatusSenderWorkerCreator implements Creator<StatusSenderWorker> {
        private final String name;
        private final String callbackUrl;
        private final GameWorker gameWorker;
        private final PlayerConsole screen;

        public StatusSenderWorkerCreator(String name, String callbackUrl, GameWorker gameWorker, PlayerConsole screen) {
            this.name = name;
            this.callbackUrl = callbackUrl;
            this.gameWorker = gameWorker;
            this.screen = screen;
        }

        @Override
        public StatusSenderWorker create() {
            return new StatusSenderWorker(name, callbackUrl, gameWorker, screen);
        }
    }
}
