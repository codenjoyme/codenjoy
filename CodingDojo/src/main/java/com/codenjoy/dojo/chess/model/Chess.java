package com.codenjoy.dojo.chess.model;


import com.codenjoy.dojo.chess.model.figures.Figure;
import com.codenjoy.dojo.chess.model.figures.Level;
import com.codenjoy.dojo.services.BoardReader;
import com.codenjoy.dojo.services.Dice;
import com.codenjoy.dojo.services.Point;
import com.codenjoy.dojo.services.Tickable;

import java.util.LinkedList;
import java.util.List;

public class Chess implements Tickable, Field {

    private List<Figure> white;
    private List<Figure> black;

    private List<Player> players;

    private final int size;
    private Dice dice;

    public Chess(Level level, Dice dice) {
        this.dice = dice;
        size = level.getSize();
        players = new LinkedList<Player>();
        white = level.getFigures(true);
        black = level.getFigures(false);
    }

    @Override
    public void tick() {
        for (Player player : players) {
            for (Figure figure : player.getFigures()) {
                figure.tick();
            }
        }
    }

    public int getSize() {
        return size;
    }

    public void newGame(Player player) {
        if (!players.contains(player)) {
            players.add(player);
        }
        player.initFigures(this);
    }

    public void remove(Player player) {
        players.remove(player);
    }

    public List<Figure> getWhite() {
        return white;
    }

    public BoardReader reader() {
        return new BoardReader() {
            private int size = Chess.this.size;

            @Override
            public int size() {
                return size;
            }

            @Override
            public Iterable<? extends Point> elements() {
                List<Point> result = new LinkedList<Point>();

                result.addAll(white);
                result.addAll(black);

                return result;
            }
        };
    }

    @Override
    public List<Figure> getFigures(boolean isWhite) {
        if (isWhite) {
            return white;
        } else {
            return black;
        }
    }
}
