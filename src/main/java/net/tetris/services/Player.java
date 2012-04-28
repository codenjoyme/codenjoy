package net.tetris.services;

public class Player {
    private String name;
    private String callbackUrl;
    private GameWorker gameWorker;

    public Player(String name, String callbackUrl, GameWorker gameWorker) {
        this.name = name;
        this.callbackUrl = callbackUrl;
        this.gameWorker = gameWorker;
    }

    public void tick() {
        gameWorker.nextStep();
    }
}
