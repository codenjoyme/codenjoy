package com.codenjoy.dojo.rubicscube.model;

import com.codenjoy.dojo.rubicscube.services.RubicsCubeEvents;
import com.codenjoy.dojo.services.*;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class RubicsCube implements Tickable, Field {

    private Cube cube;
    private CellsAdapter cells;
    private Player player;

    private static final int size = 12;

    private boolean gameOver;
    private String command;
    private RandomCommand generator;

    public RubicsCube(RandomCommand generator) {
        this.generator = generator;
        cube = new Cube();
        cells = new CellsAdapter(cube);
        gameOver = false;
    }

    @Override
    public void tick() {
        if (gameOver) return;
        if (command == null) return;

        cube.doCommand(command);

        command = null;

        checkIsWin();
    }

    private void checkIsWin() {
        if (cube.isSolved()) {
            gameOver = true;
            player.event(RubicsCubeEvents.SUCCESS);
        }
    }

    public int getSize() {
        return size;
    }

    public void newGame(Player player) {
        this.player = player;
        gameOver = false;
        player.newHero(this);

        cube.init();
        cube.doCommand(generator.next());
    }

    public void remove(Player player) {
        this.player = null;
    }

    public List<Cell> getCells() {
        return cells.getCells();
    }

    public boolean isGameOver() {
        return gameOver;
    }

    @Override
    public Joystick getJoystick() {
        return new Joystick() {
            @Override
            public void down() {
                // do nothing
            }

            @Override
            public void up() {
                // do nothing
            }

            @Override
            public void left() {
                // do nothing
            }

            @Override
            public void right() {
                // do nothing
            }

            @Override
            public void act(int... p) { // TODO test me
                if (gameOver) return;

                if (p.length == 1 && p[0] == 0) {
                    gameOver = true;
                    player.event(RubicsCubeEvents.FAIL);
                    return;
                }

                if (p.length != 2) {
                    return;
                }

                int faceNumber = p[0];
                if (check(faceNumber, 1, 6)) return;

                int rotateCount = p[1];
                if (check(rotateCount, -1, 2)) return;

                command = "";
                switch (faceNumber) {
                    case 1 : command = "L"; break;
                    case 2 : command = "F"; break;
                    case 3 : command = "R"; break;
                    case 4 : command = "B"; break;
                    case 5 : command = "U"; break;
                    case 6 : command = "D"; break;
                }

                switch (rotateCount) {
                    case -1 : command += "'"; break;
                    case 0 : command = "";
                    case 1 : break;
                    case 2 : command += "2"; break;
                }
            }
        };
    }

    private boolean check(int i, int min, int max) {
        return (i > max || i < min);
    }

    public BoardReader reader() {
        return new BoardReader() {
            private int size = RubicsCube.this.size;

            @Override
            public int size() {
                return size;
            }

            @Override
            public Iterable<? extends Point> elements() {
                return RubicsCube.this.cells.getCells();
            }
        };
    }
}
