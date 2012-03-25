package net.tetris.services;

import akka.actor.ActorSystem;
import akka.actor.TypedActor;
import akka.actor.TypedProps;
import akka.japi.Creator;
import net.tetris.dom.TetrisGame;
import net.tetris.dom.TetrisGlass;

import java.util.ArrayList;
import java.util.List;

public class PlayerService {
    private List<Player> players = new ArrayList<>();

    private ActorSystem system;

    public void init() {
        system = ActorSystem.create("tetris");
    }

    public Player addNewPlayer(final String name, final String callbackUrl) {

        final PlayerConsole console = new PlayerConsole();
        final TetrisGame game = new TetrisGame(console, new PlayerFigures(), new PlayerScores(),
                new TetrisGlass(TetrisGame.GLASS_WIDTH, TetrisGame.GLASS_HEIGHT));

        TetrisGameWorker tetrisGameWorker = new TetrisGameWorker(game);

        final GameWorker gameWorker = TypedActor.get(system).typedActorOf(new TypedProps<>(GameWorker.class,
                new TetrisGameWorkerCreator(tetrisGameWorker)), "gameWorker_" + name);

        SenderWorker sendWorker = TypedActor.get(system).typedActorOf(new TypedProps<>(SenderWorker.class,
                new StatusSenderWorkerCreator(name, callbackUrl, gameWorker, console)), "senderWorker_" + name);
        tetrisGameWorker.setSender(sendWorker);

        Player player = new Player(name, callbackUrl, gameWorker);
        players.add(player);
        return player;
    }

    public void destroy() {
        system.shutdown();
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
        private final PlayerConsole console;

        public StatusSenderWorkerCreator(String name, String callbackUrl, GameWorker gameWorker, PlayerConsole console) {
            this.name = name;
            this.callbackUrl = callbackUrl;
            this.gameWorker = gameWorker;
            this.console = console;
        }

        @Override
        public StatusSenderWorker create() {
            return new StatusSenderWorker(name, callbackUrl, gameWorker, console);
        }
    }
}
