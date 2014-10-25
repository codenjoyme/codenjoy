package com.codenjoy.dojo.bomberman.model;

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

    private Bomberman myBomberman;

    private Object[][] field;

    public BombermanPrinter(IBoard board, Player player) {
        this.board = board;
        this.player = player;
    }

    @Override
    public boolean init() {
        size = board.size();
        field = new Object[size][size];
        for (int x = 0; x < size; x++) {
            for (int y = 0; y < size; y++) {
                field[x][y] = new ArrayList<Point>(3);
            }
        }

        for (Wall wall : board.getWalls()) {
            add(wall);
        }
        for (Bomberman bomberman : board.getBombermans()) {
            add(bomberman);
        }
        myBomberman = player.getBomberman();
        for (Bomb bomb : board.getBombs()) {
            add(bomb);
        }
        for (Point blast : board.getBlasts()) {
            add(blast);
        }
        return false;
    }

    private void add(Point element) {
        ((List<Point>)field[element.getX()][element.getY()]).add(element);
    }

    @Override
    public Enum get(Point pt) {
        return EMPTY;
    }

    @Override
    public void printAll(Filler filler) {
        for (int x = 0; x < size; x++) {
            for (int y = 0; y < size; y++) {
                List<Point> elements = (List<Point>) field[x][y];
                if (elements.isEmpty()) {
                    filler.set(x, y, EMPTY);
                    continue;
                }

                Blast blast = null;
                Bomberman bomberman = null;
                MeatChopper meatChopper = null;
                DestroyWall destroyWall = null;
                Wall wall = null;
                Bomb bomb = null;
                for (Point element : elements) {
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
                    filler.set(x, y, WALL);
                    continue;
                }

                if (blast != null && bomb == null) {
                    if (bomberman != null) {
                        if (bomberman == myBomberman) {
                            filler.set(x, y, DEAD_BOMBERMAN);
                        } else {
                            filler.set(x, y, OTHER_DEAD_BOMBERMAN);
                        }
                    } else if (meatChopper != null) {
                        filler.set(x, y, DEAD_MEAT_CHOPPER);
                    } else if (destroyWall != null) {
                        filler.set(x, y, DESTROYED_WALL);
                    } else {
                        filler.set(x, y, BOOM);
                    }
                    continue;
                }

                if (destroyWall != null) {
                    filler.set(x, y, DESTROY_WALL);
                    continue;
                }

                if (meatChopper != null) {
                    if (bomberman != null) {
                        if (bomberman == myBomberman) {
                            filler.set(x, y, DEAD_BOMBERMAN);
                        } else {
                            filler.set(x, y, OTHER_DEAD_BOMBERMAN);
                        }
                    } else {
                        filler.set(x, y, MEAT_CHOPPER);
                    }
                    continue;
                }

                if (bomberman != null) {
                    if (bomberman.isAlive()) {
                        if (bomberman == myBomberman) {
                            if (bomb != null) {
                                filler.set(x, y, BOMB_BOMBERMAN);
                            } else {
                                filler.set(x, y, BOMBERMAN);
                            }
                        } else {
                            if (bomb != null) {
                                filler.set(x, y, OTHER_BOMB_BOMBERMAN);
                            } else {
                                filler.set(x, y, OTHER_BOMBERMAN);
                            }
                        }
                    } else {
                        if (bomberman == myBomberman) {
                            filler.set(x, y, DEAD_BOMBERMAN);
                        } else {
                            filler.set(x, y, OTHER_DEAD_BOMBERMAN);
                        }
                    }
                    continue;
                }

                if (bomb != null) {
                    filler.set(x, y, bomb.state());
                    continue;
                }

                throw new RuntimeException();
            }
        }
    }
}
