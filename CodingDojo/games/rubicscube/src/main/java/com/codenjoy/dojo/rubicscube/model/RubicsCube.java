package com.codenjoy.dojo.rubicscube.model;

/*-
 * #%L
 * Codenjoy - it's a dojo-like platform from developers to developers.
 * %%
 * Copyright (C) 2018 Codenjoy
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
import com.codenjoy.dojo.services.Point;
import com.codenjoy.dojo.services.printer.BoardReader;

import java.util.List;

public class RubicsCube implements Field {

    private Cube cube;
    private CellsAdapter cells;
    private Player player;

    private static final int size = 12;

    private boolean gameOver;
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
        String command = player.getHero().pullCommand();
        if (command == null) return;

        cube.doCommand(command);

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

    @Override
    public void newGame(Player player) {
        this.player = player;
        gameOver = false;
        player.newHero(this);

        cube.init();
        cube.doCommand(generator.next());
    }

    @Override
    public void remove(Player player) {
        this.player = null;
    }

    public List<Cell> getCells() {
        return cells.getCells();
    }

    @Override
    public boolean isGameOver() {
        return gameOver;
    }

    @Override
    public void gameOver() {
        gameOver = true;
    }

    @Override
    public boolean check(int i, int min, int max) {
        return (i > max || i < min);
    }

    @Override
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
