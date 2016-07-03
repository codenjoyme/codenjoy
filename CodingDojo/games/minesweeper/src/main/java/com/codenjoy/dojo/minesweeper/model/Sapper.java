package com.codenjoy.dojo.minesweeper.model;

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


import com.codenjoy.dojo.services.PointImpl;
import com.codenjoy.dojo.services.State;

/**
 * User: oleksii.morozov
 * Date: 10/14/12
 * Time: 12:39 PM
 */
public class Sapper extends PointImpl implements State<Elements, Object> {
    private boolean isDead = false;
    private MineDetector mineDetector;
    private Field board;

    public Sapper(int x, int y) {
        super(x, y);
    }

    public boolean isDead() {
        return isDead;
    }

    public void die() {
        isDead = true;
    }

    private void useMineDetector() {
        if (mineDetector.getCharge() > 0) {
            mineDetector.useMe();
        }
    }

    public void iWantToHaveMineDetectorWithChargeNumber(int charge) {
        this.mineDetector = new MineDetector(charge);
    }

    public boolean isEmptyCharge() {
        return mineDetector.getCharge() == 0;
    }

    public void tryToUseDetector(DetectorAction detectorAction) {
        if (isEmptyCharge()) {
            return;
        }

        useMineDetector();

        if (detectorAction != null) {
            detectorAction.used();
        }
    }

    public MineDetector getMineDetector() {
        return mineDetector;
    }

    @Override
    public Elements state(Object player, Object... alsoAtPoint) {
        if (board.isSapperOnMine()) {
            return Elements.BANG;
        } else {
            return Elements.DETECTOR;
        }
    }

    public void setBoard(Field board) {
        this.board = board;
    }
}
