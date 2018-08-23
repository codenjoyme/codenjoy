package com.codenjoy.dojo.reversi.model.items;

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


import com.codenjoy.dojo.reversi.model.Elements;
import com.codenjoy.dojo.reversi.model.GetChip;
import com.codenjoy.dojo.reversi.model.Player;
import com.codenjoy.dojo.services.*;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;

public class Chip extends PointImpl implements State<Elements, Player> {

    public static final Chip NULL = new Chip(false, pt(-1, -1), null);

    private boolean color;
    private GetChip field;

    public Chip(boolean color, Point point, GetChip field) {
        super(point);
        this.color = color;
        this.field = field;
    }

    @Override
    public Elements state(Player player, Object... alsoAtPoint) {
        boolean itsMyTurn = player.color() == field.currentColor();
        boolean itsMyChip = player.color() == color;
        if (!itsMyTurn && itsMyChip) {
            if (color) {
                return Elements.WHITE_STOP;
            } else {
                return Elements.BLACK_STOP;
            }
        }

        if (color == true) {
            if (field.currentColor() == color) {
                return Elements.WHITE_TURN;
            } else {
                return Elements.WHITE;
            }
        } else {
            if (field.currentColor() == color) {
                return Elements.BLACK_TURN;
            } else {
                return Elements.BLACK;
            }
        }
    }

    public void flip() {
        color = !color;
    }

    public boolean sameColor(Chip chip) {
        return color == chip.color;
    }

    public int flip(QDirection direction) {
        AtomicInteger count = new AtomicInteger(0);
        flip(this, direction, chip -> {
            chip.flip();
            count.incrementAndGet();
        });
        return count.get();
    }

    public boolean flip(Chip start, QDirection direction, Consumer<Chip> onflip) {
        if (this == Chip.NULL) {
            return false;
        }

        if (this != start) {
            if (start.distance(this) == 1) {
                if (start.sameColor(this)) {
                    return false;
                }
            } else {
                if (start.sameColor(this)) {
                    return true;
                }
            }
        }

        return flipNext(start, direction, onflip);
    }

    private boolean flipNext(Chip start, QDirection direction, Consumer<Chip> onflip) {
        Chip next = field.chip(direction.change(this));
        boolean flip = next.flip(start, direction, onflip);
        if (flip && this != start) {
            onflip.accept(this);
        }
        return flip;
    }

    public boolean color() {
        return color;
    }

    public boolean isBlack() {
        return !color;
    }

    public boolean isWhite() {
        return color;
    }
}
