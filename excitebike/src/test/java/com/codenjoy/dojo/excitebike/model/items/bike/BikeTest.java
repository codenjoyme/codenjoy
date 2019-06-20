package com.codenjoy.dojo.excitebike.model.items.bike;

/*-
 * #%L
 * Codenjoy - it's a dojo-like platform from developers to developers.
 * %%
 * Copyright (C) 2018 - 2019 Codenjoy
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

import com.codenjoy.dojo.excitebike.model.GameField;
import org.junit.Before;
import org.junit.Test;

import java.util.Optional;

import static org.junit.Assert.*;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class BikeTest {
    Bike bike;
    GameField gameField;

    @Before
    public void init() {
        bike = new Bike(5, 5);
        gameField = mock(GameField.class);
        bike.init(gameField);
    }

    @Test
    public void tick__shouldShiftBike_ifBikeIsNotAlive() {
        //given
        bike.crush();

        //when
        bike.tick();

        //then
        assertEquals(4, bike.getX());
        assertEquals(5, bike.getY());
    }

    @Test
    public void tick__shouldMoveBikeToUp_ifUpperPositionIsFree() {
        //given
        when(gameField.isBorder(anyInt(), anyInt())).thenReturn(false);
        when(gameField.getEnemyBike(anyInt(), anyInt())).thenReturn(Optional.empty());
        bike.up();

        //when
        bike.tick();

        //then
        assertEquals(5, bike.getX());
        assertEquals(6, bike.getY());
    }

    @Test
    public void tick__shouldNotMoveBike_ifUpperPositionIsBorder() {
        //given
        when(gameField.isBorder(anyInt(), anyInt())).thenReturn(true);
        when(gameField.getEnemyBike(anyInt(), anyInt())).thenReturn(Optional.empty());
        bike.up();

        //when
        bike.tick();

        //then
        assertEquals(5, bike.getX());
        assertEquals(5, bike.getY());
    }

    @Test
    public void tick__shouldNotMoveBike_ifUpperPositionIsOtherBike() {
        //given
        when(gameField.isBorder(anyInt(), anyInt())).thenReturn(false);
        when(gameField.getEnemyBike(anyInt(), anyInt())).thenReturn(Optional.of(new Bike(0,0)));
        bike.up();

        //when
        bike.tick();

        //then
        assertEquals(5, bike.getX());
        assertEquals(5, bike.getY());
    }

    @Test
    public void tick__shouldMoveBikeToDown_ifLowerPositionIsFree() {
        //given
        when(gameField.isBorder(anyInt(), anyInt())).thenReturn(false);
        when(gameField.getEnemyBike(anyInt(), anyInt())).thenReturn(Optional.empty());
        bike.down();

        //when
        bike.tick();

        //then
        assertEquals(5, bike.getX());
        assertEquals(4, bike.getY());
    }

    @Test
    public void tick__shouldNotMoveBike_ifLowerPositionIsBorder() {
        //given
        when(gameField.isBorder(anyInt(), anyInt())).thenReturn(true);
        when(gameField.getEnemyBike(anyInt(), anyInt())).thenReturn(Optional.empty());
        bike.down();

        //when
        bike.tick();

        //then
        assertEquals(5, bike.getX());
        assertEquals(5, bike.getY());
    }

    @Test
    public void tick__shouldNotMoveBike_ifLowerPositionIsOtherBike() {
        //given
        when(gameField.isBorder(anyInt(), anyInt())).thenReturn(false);
        when(gameField.getEnemyBike(anyInt(), anyInt())).thenReturn(Optional.of(new Bike(0,0)));
        bike.up();

        //when
        bike.tick();

        //then
        assertEquals(5, bike.getX());
        assertEquals(5, bike.getY());
    }

    @Test
    public void tick__shouldAccelerateBike_ifBikeTakeAccelerator() {
        //given
        when(gameField.isAccelerator(anyInt(), anyInt())).thenReturn(true);
        when(gameField.size()).thenReturn(8);

        //when
        bike.tick();

        //then
        assertEquals(7, bike.getX());
        assertEquals(5, bike.getY());
    }

    @Test
    public void tick__shouldSetBikeXCoordinateToMaxPossible_ifBikePositionAfterAccelerationIsOutOfFieldBound() {
        //given
        when(gameField.isAccelerator(anyInt(), anyInt())).thenReturn(true);
        when(gameField.size()).thenReturn(7);

        //when
        bike.tick();

        //then
        assertEquals(6, bike.getX());
        assertEquals(5, bike.getY());
    }

    @Test
    public void tick__shouldInhibitBike_ifBikeTakeInhibitor() {
        //given
        when(gameField.isInhibitor(anyInt(), anyInt())).thenReturn(true);

        //when
        bike.tick();

        //then
        assertEquals(3, bike.getX());
        assertEquals(5, bike.getY());
    }

    @Test
    public void tick__shouldSetBikeXCoordinateToMinPossible_ifBikePositionAfterInhibitionIsOutOfFieldBound() {
        //given
        bike.setX(1);
        when(gameField.isInhibitor(anyInt(), anyInt())).thenReturn(true);

        //when
        bike.tick();

        //then
        assertEquals(0, bike.getX());
        assertEquals(5, bike.getY());
    }

    @Test
    public void tick__shouldFallBike_ifBikeEncounterWithObstacle() {
        //given
        when(gameField.isObstacle(anyInt(), anyInt())).thenReturn(true);

        //when
        bike.tick();

        //then
        assertFalse(bike.isAlive());
    }

    @Test
    public void tick__shouldChangeLineToUp_ifBikeTakeUpLineChangerAndUpperPositionIsFree() {
        //given
        when(gameField.isUpLineChanger(anyInt(), anyInt())).thenReturn(true);
        when(gameField.isBorder(anyInt(), anyInt())).thenReturn(false);
        when(gameField.getEnemyBike(anyInt(), anyInt())).thenReturn(Optional.empty());

        //when
        bike.tick();

        //then
        assertEquals(5, bike.getX());
        assertEquals(6, bike.getY());
    }

    @Test
    public void tick__shouldNotMoveBike_ifBikeTakeUpLineChangerAndUpperPositionIsBorder() {
        //given
        when(gameField.isUpLineChanger(anyInt(), anyInt())).thenReturn(true);
        when(gameField.isBorder(anyInt(), anyInt())).thenReturn(true);
        when(gameField.getEnemyBike(anyInt(), anyInt())).thenReturn(Optional.empty());

        //when
        bike.tick();

        //then
        assertEquals(5, bike.getX());
        assertEquals(5, bike.getY());
    }

    @Test
    public void tick__shouldNotMoveBike_ifBikeTakeUpLineChangerAndUpperPositionIsOtherBike() {
        //given
        when(gameField.isUpLineChanger(anyInt(), anyInt())).thenReturn(true);
        when(gameField.isBorder(anyInt(), anyInt())).thenReturn(false);
        when(gameField.getEnemyBike(anyInt(), anyInt())).thenReturn(Optional.of(new Bike(0,0)));

        //when
        bike.tick();

        //then
        assertEquals(5, bike.getX());
        assertEquals(5, bike.getY());
    }

    @Test
    public void tick__shouldChangeLineToDown_ifBikeTakeDownLineChangerAndLowerPositionIsFree() {
        //given
        when(gameField.isDownLineChanger(anyInt(), anyInt())).thenReturn(true);
        when(gameField.isBorder(anyInt(), anyInt())).thenReturn(false);
        when(gameField.getEnemyBike(anyInt(), anyInt())).thenReturn(Optional.empty());

        //when
        bike.tick();

        //then
        assertEquals(5, bike.getX());
        assertEquals(4, bike.getY());
    }

    @Test
    public void tick__shouldNotMoveBike_ifBikeTakeDownLineChangerAndLowerPositionIsBorder() {
        //given
        when(gameField.isDownLineChanger(anyInt(), anyInt())).thenReturn(true);
        when(gameField.isBorder(anyInt(), anyInt())).thenReturn(true);
        when(gameField.getEnemyBike(anyInt(), anyInt())).thenReturn(Optional.empty());

        //when
        bike.tick();

        //then
        assertEquals(5, bike.getX());
        assertEquals(5, bike.getY());
    }

    @Test
    public void tick__shouldNotMoveBike_ifBikeTakeDownLineChangerAndLowerPositionIsOtherBike() {
        //given
        when(gameField.isDownLineChanger(anyInt(), anyInt())).thenReturn(true);
        when(gameField.isBorder(anyInt(), anyInt())).thenReturn(false);
        when(gameField.getEnemyBike(anyInt(), anyInt())).thenReturn(Optional.of(new Bike(0,0)));

        //when
        bike.tick();

        //then
        assertEquals(5, bike.getX());
        assertEquals(5, bike.getY());
    }

}
