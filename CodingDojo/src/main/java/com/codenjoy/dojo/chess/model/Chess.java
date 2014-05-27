package com.codenjoy.dojo.chess.model;


import com.codenjoy.dojo.chess.model.figures.Figure;
import com.codenjoy.dojo.chess.model.figures.Level;
import com.codenjoy.dojo.chess.services.ChessEvents;
import com.codenjoy.dojo.services.Dice;
import com.codenjoy.dojo.services.Tickable;

import java.util.LinkedList;
import java.util.List;

public class Chess implements Tickable, Field {

    private List<Figure> figures;

    private List<Player> players;

    private final int size;
    private Dice dice;

    public Chess(Level level, Dice dice) {
        this.dice = dice;
        size = level.getSize();
        players = new LinkedList<Player>();
        figures = new LinkedList<Figure>();
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

    public List<Figure> getFigures() {
        return figures;
    }
}
