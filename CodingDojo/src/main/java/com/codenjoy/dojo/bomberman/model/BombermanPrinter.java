package com.codenjoy.dojo.bomberman.model;

import com.apofig.profiler.Profiler;
import com.codenjoy.dojo.services.GamePrinter;
import com.codenjoy.dojo.services.Point;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import static com.codenjoy.dojo.bomberman.model.Elements.*;

public class BombermanPrinter implements GamePrinter {

    private final IBoard board;
    private int size;
    private Player player;

    private Object[][] field;

    public BombermanPrinter(IBoard board, Player player) {
        this.board = board;
        this.player = player;
    }

public static Profiler p = new Profiler();
    @Override
    public boolean init() {
p.start();
        size = board.size();
        field = new Object[size][size];

        addAll(board.getWalls());
        addAll(board.getBombermans());
        addAll(board.getBombs());
        addAll(board.getBlasts());
p.done("init");
        return false;
    }

    private void addAll(Iterable<? extends Point> elements) {
        for (Point el : elements) {
            Object[] existing = (Object[])field[el.getX()][el.getY()];
            int index = 0;
            if (existing == null) {
                existing = new Object[7];
                field[el.getX()][el.getY()] = existing;
                existing[0] = 0;
            } else {
                index = (Integer)existing[0];
            }
            index++;
            existing[index] = el;
            existing[0] = index;
        }
    }

    @Override
    public char get(Point pt) {
        return EMPTY.ch;
    }

    @Override
    public void printAll(Filler filler) {
p.start();
        for (int x = 0; x < size; x++) {
            for (int y = 0; y < size; y++) {
                Object[] elements = (Object[]) field[x][y];
                if (elements == null || (Integer)elements[0] == 0) {
                    filler.set(x, y, EMPTY.ch);
                    continue;
                }

                Blast blast = null;
                Bomberman bomberman = null;
                MeatChopper meatChopper = null;
                DestroyWall destroyWall = null;
                Wall wall = null;
                Bomb bomb = null;
                for (int index = 1; index <= (Integer)elements[0]; index++) {
                    Point element = (Point)elements[index];

                    if (element instanceof Blast) {
                        blast = (Blast)element;
                    }
                    if (element instanceof Bomberman) {
                        bomberman = (Bomberman)element;
                    }
                    if (element instanceof MeatChopper) {
                        meatChopper = (MeatChopper)element;
                    } else if (element instanceof DestroyWall) {
                        destroyWall = (DestroyWall)element;
                    } else if (element instanceof Wall) {
                        wall = (Wall)element;
                    }
                    if (element instanceof Bomb) {
                        bomb = (Bomb)element;
                    }
                }

                if (wall != null) {
                    filler.set(x, y, wall.state(player).ch);
                    continue;
                }

                if (blast != null && bomb == null) {
                    filler.set(x, y, blast.state(player, bomberman, meatChopper, destroyWall).ch);
                    continue;
                }

                if (destroyWall != null) {
                    filler.set(x, y, destroyWall.state(player).ch);
                    continue;
                }

                if (bomberman != null) {
                    filler.set(x, y, bomberman.state(player, bomb).ch);
                    continue;
                }

                if (meatChopper != null) {
                    filler.set(x, y, meatChopper.state(player).ch);
                    continue;
                }

                if (bomb != null) {
                    filler.set(x, y, bomb.state(player).ch);
                    continue;
                }

                throw new RuntimeException();
            }
        }
p.done("print");
    }
}
