package com.codenjoy.dojo.chess.model.figures;

import com.codenjoy.dojo.chess.model.Field;
import com.codenjoy.dojo.chess.model.Player;
import com.codenjoy.dojo.services.Point;
import com.codenjoy.dojo.services.PointImpl;
import com.codenjoy.dojo.services.Tickable;

public class Figure extends PointImpl implements Tickable {

    private boolean isWhite;
    private Field field;
    private boolean alive;
    private Player player;

    public Figure(Point xy, boolean isWhite) {
        super(xy);
        this.isWhite = isWhite;
        alive = true;
    }

    public void init(Field field) {
        this.field = field;
    }


    @Override
    public void tick() {
        if (!alive) return;

        // TODO
    }

    public boolean isAlive() {
        return alive;
    }

    public boolean hasPlayer() {
        return player != null;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public boolean isWhite() {
        return isWhite;
    }
}
