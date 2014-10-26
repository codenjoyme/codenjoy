package com.codenjoy.dojo.bomberman.model;

import com.apofig.profiler.Profiler;
import com.codenjoy.dojo.services.GamePrinter;
import com.codenjoy.dojo.services.Point;
import com.codenjoy.dojo.services.State;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import static com.codenjoy.dojo.bomberman.model.Elements.*;

public class BombermanPrinter implements GamePrinter {

    private final IBoard board;
    private int size;
    private Player player;

    private Object[][] field;
    private byte[][] len;

    public BombermanPrinter(IBoard board, Player player) {
        this.board = board;
        this.player = player;
    }

    @Override
    public boolean init() {
        size = board.size();
        field = new Object[size][size];
        len = new byte[size][size];

        addAll(board.getBlasts());
        addAll(board.getBombermans());
        addAll(board.getWalls());
        addAll(board.getBombs());
        return false;
    }

    private void addAll(Iterable<? extends Point> elements) {
        for (Point el : elements) {
            int x = el.getX();
            int y = el.getY();

            Object[] existing = (Object[]) field[x][y];
            if (existing == null) {
                existing = new Object[7];
                field[x][y] = existing;
            }
            existing[len[x][y]] = el;
            len[x][y]++;
        }
    }

    @Override
    public char get(Point pt) {
        return EMPTY.ch;
    }

    @Override
    public void printAll(Filler filler) {
        for (int x = 0; x < size; x++) {
            for (int y = 0; y < size; y++) {
                Object[] elements = (Object[]) field[x][y];
                if (elements == null || len[x][y] == 0) {
                    filler.set(x, y, EMPTY.ch);
                    continue;
                }

                for (int index = 0; index < len[x][y]; index++) {
                    State<Elements, Player> state = (State<Elements, Player>)elements[index];
                    Elements el = state.state(player, elements);
                    if (el != null) {
                        filler.set(x, y, el.ch);
                        break;
                    }
                }
            }
        }
    }
}
