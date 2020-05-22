package com.codenjoy.dojo.snakebattle.model.board.round;

import com.codenjoy.dojo.services.multiplayer.GameField;
import com.codenjoy.dojo.services.multiplayer.GamePlayer;
import com.codenjoy.dojo.services.multiplayer.PlayerHero;

import java.util.List;

public interface RoundField<T extends GamePlayer<? extends PlayerHero, ? extends GameField>> {

    List<T> aliveActive();

    void reset(T player);

    void start(int round);

    void print(String message);

    int score(T player);
}
