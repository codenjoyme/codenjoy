package com.codenjoy.dojo.battlecity.model;

import com.codenjoy.dojo.services.GamePrinter;
import com.codenjoy.dojo.services.Point;
import com.codenjoy.dojo.services.State;

import java.util.List;

public class BattlecityPrinter implements GamePrinter {

    private int size;
    private Field board;
    private Player player;
    private State[][] field;

    public BattlecityPrinter(Field board, Player player) {
        this.board = board;
        this.player = player;
    }

    @Override
    public boolean init() {
        size = board.size();
        field = new State[size][size];

        addAll(board.getBorders());
        addAll(board.getBullets());
        addAll(board.getConstructions());
        addAll(board.getTanks());

        return false;
    }

    private void addAll(List<? extends State> elements) {
        for (State el : elements) {
            field[((Point)el).getX()][((Point)el).getY()] = el;
        }
    }

    @Override
    public char get(Point pt) {
        return Elements.BATTLE_GROUND.ch;
    }

    @Override
    public void printAll(Filler filler) {
        for (int x = 0; x < size; x++) {
            for (int y = 0; y < size; y++) {
                State<Elements, Player> el = field[x][y];
                filler.set(x, y, (el == null)?Elements.BATTLE_GROUND.ch:el.state(player).ch);
            }
        }
    }
}
