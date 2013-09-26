package com.codenjoy.dojo.transport.screen;

/**
 * User: serhiy.zelenin
 * Date: 9/26/13
 * Time: 2:43 PM
 */
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
