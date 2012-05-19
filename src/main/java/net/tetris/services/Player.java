package net.tetris.services;

public class Player {
    private String name;
    private String callbackUrl;

    public Player() {
    }

    public Player(String name, String callbackUrl) {
        this.name = name;
        this.callbackUrl = callbackUrl;
    }

    public String getName() {
        return name;
    }

    public String getCallbackUrl() {
        return callbackUrl;
    }

    public void setCallbackUrl(String callbackUrl) {
        this.callbackUrl = callbackUrl;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }
}
