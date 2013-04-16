package com.codenjoy.dojo.bomberman.model;

import com.codenjoy.dojo.services.EventListener;

public class Player {
    protected Bomberman bomberman;
    EventListener listener;
    int maxScore;
    int score;

    public Player() {
    }
}