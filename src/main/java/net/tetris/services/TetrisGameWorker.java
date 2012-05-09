package net.tetris.services;

import net.tetris.dom.TetrisGame;
public class TetrisGameWorker implements GameWorker {

    private TetrisGame game;
    private SenderWorker sender;

    public TetrisGameWorker(TetrisGame game) {
        this.game = game;
    }

    @Override
    public void nextStep() {
        game.nextStep();
        sender.sendStatusToPlayer();
    }

    @Override
    public void control(String command) {
    }

    public void setSender(SenderWorker sender) {
        this.sender = sender;
    }
}
