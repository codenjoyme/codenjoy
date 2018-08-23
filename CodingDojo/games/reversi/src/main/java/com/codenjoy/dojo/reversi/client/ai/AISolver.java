package com.codenjoy.dojo.reversi.client.ai;

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


import com.codenjoy.dojo.client.Solver;
import com.codenjoy.dojo.reversi.client.Board;
import com.codenjoy.dojo.reversi.model.Elements;
import com.codenjoy.dojo.reversi.model.Flipper;
import com.codenjoy.dojo.reversi.model.GetChip;
import com.codenjoy.dojo.reversi.model.items.Chip;
import com.codenjoy.dojo.services.Dice;
import com.codenjoy.dojo.services.Point;

import java.util.List;

public class AISolver implements Solver<Board> {

    private Dice dice;

    public AISolver(Dice dice) {
        this.dice = dice;
    }

    @Override
    public String get(final Board board) {
        if (!board.isMyTurn()) return "";

        Flipper flipper = new Flipper(new GetChip() {
            @Override
            public Chip chip(Point point) {
                if (point.isOutOf(board.size())) {
                    return Chip.NULL; // TODO почему?
                }
                Elements element = board.getAt(point.getX(), point.getY());
                switch (element) {
                    case BLACK: return new Chip(false, point, this);
                    case BLACK_TURN: return new Chip(false, point, this);
                    case WHITE: return new Chip(true, point, this);
                    case WHITE_TURN: return new Chip(true, point, this);
                    default: return Chip.NULL;
                }
            }

            @Override
            public boolean currentColor() {
                return false; // do nothing
            }

            @Override
            public List<Point> freeSpaces() {
                return board.get(Elements.NONE);
            }
        });

        List<Flipper.Turn> turns = flipper.turns(board.myColor());
        if (turns.isEmpty()) return "";
        Flipper.Turn turn = turns.get(dice.next(turns.size()));

        return String.format("ACT(%s,%s)",
                turn.chip.getX(),
                turn.chip.getY());
    }
}
