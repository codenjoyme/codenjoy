package com.codenjoy.dojo.services.nullobj;

import com.codenjoy.dojo.services.multiplayer.GameField;
import com.codenjoy.dojo.services.multiplayer.GamePlayer;
import com.codenjoy.dojo.services.printer.BoardReader;

public class NullGameField implements GameField {

    public static final GameField INSTANCE = new NullGameField();

    @Override
    public BoardReader reader() {
        return NullBoardReader.INSTANCE;
    }

    @Override
    public void newGame(GamePlayer player) {
        // do nothing
    }

    @Override
    public void remove(GamePlayer player) {
        // do nothing
    }

    @Override
    public void tick() {
        // do nothing
    }
}
