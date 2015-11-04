package com.codenjoy.dojo.transport.screen;

public class FakePlayer implements ScreenRecipient {
    private String name;

    public FakePlayer(String name) {
        this.name = name;
    }

    @Override
    public String getName() {
        return name;
    }

    public String toString() {
        return name;
    }

}
