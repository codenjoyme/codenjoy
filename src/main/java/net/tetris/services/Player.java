package net.tetris.services;

public class Player {
    private String name;
    private String callbackUrl;

    public Player(String name, String callbackUrl) {
        this.name = name;
        this.callbackUrl = callbackUrl;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return name;
    }
}
