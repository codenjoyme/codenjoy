package com.codenjoy.dojo.minesweeper.model;

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


import com.codenjoy.dojo.services.Direction;
import com.codenjoy.dojo.services.PointImpl;
import com.codenjoy.dojo.services.QDirection;
import com.codenjoy.dojo.services.State;
import com.codenjoy.dojo.services.multiplayer.PlayerHero;

/**
 * User: oleksii.morozov
 * Date: 10/14/12
 * Time: 12:39 PM
 */
public class Sapper extends PlayerHero<Field> implements State<Elements, Object> {

    private boolean isDead = false;
    private MineDetector mineDetector;
    private Direction nextStep;
    private boolean useDetector;

    public Sapper(int x, int y) {
        super(x, y);
        useDetector = false;
    }

    public boolean isDead() {
        return isDead;
    }

    public void die() {
        isDead = true;
    }

    public boolean isAlive() {
        return !isDead() &&
                !field.isEmptyDetectorButPresentMines() &&
                !field.isWin();
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
        if (field.isSapperOnMine()) {
            return Elements.BANG;
        } else {
            return Elements.DETECTOR;
        }
    }
    @Override
    public void down() {
        nextStep = Direction.DOWN;
    }

    @Override
    public void up() {
        nextStep = Direction.UP;
    }

    @Override
    public void left() {
        nextStep = Direction.LEFT;
    }

    @Override
    public void right() {
        nextStep = Direction.RIGHT;
    }

    @Override
    public void act(int... p) {
        useDetector = true;
    }

    @Override
    public void tick() {
        if (nextStep == null) {
            return;
        }

        if (useDetector) {
            field.useMineDetectorToGivenDirection(nextStep);
            useDetector = false;
        } else {
            field.sapperMoveTo(nextStep);
        }

        nextStep = null;
    }
}
