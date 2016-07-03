package com.codenjoy.dojo.rubicscube.model;

/*-
 * #%L
 * Codenjoy - it's a dojo-like platform from developers to developers.
 * %%
 * Copyright (C) 2016 Codenjoy
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/gpl-3.0.html>.
 * #L%
 */


import com.codenjoy.dojo.rubicscube.services.Events;
import com.codenjoy.dojo.services.BoardReader;
import com.codenjoy.dojo.services.Joystick;
import com.codenjoy.dojo.services.Point;
import com.codenjoy.dojo.services.Tickable;

import java.util.List;

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
            player.event(Events.SUCCESS);
        }
    }

    public int size() {
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
                    player.event(Events.FAIL);
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
