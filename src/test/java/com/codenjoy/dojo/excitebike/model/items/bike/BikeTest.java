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
import com.codenjoy.dojo.excitebike.model.Player;
import com.codenjoy.dojo.excitebike.services.Events;
import com.codenjoy.dojo.excitebike.model.elements.BikeType;
import com.codenjoy.dojo.excitebike.model.items.Bike;
import org.junit.Before;
import org.junit.Test;

import java.util.Optional;

import static com.codenjoy.dojo.excitebike.TestUtils.getPlayer;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.*;

public class BikeTest {

    private Bike bike;
    private GameField gameField;

    @Before
    public void init() {
        bike = new Bike(5, 5);
        gameField = mock(GameField.class);
        when(gameField.xSize()).thenReturn(10);
        bike.init(gameField);
    }

    @Test
    public void tick__shouldNotShiftBike__ifBikeIsNotAlive() {
        //given
        Player player = mock(Player.class);
        when(gameField.getPlayerOfBike(bike)).thenReturn(player);
        bike.crush();

        //when
        bike.tick();

        //then
        assertThat(bike.getX(), is(5));
        assertThat(bike.getY(), is(5));
    }

    @Test
    public void tickWithUpCommand__shouldMoveBikeToUp__ifUpperPositionIsFree() {
        //given
        when(gameField.isFence(anyInt(), anyInt())).thenReturn(false);
        when(gameField.getEnemyBike(anyInt(), anyInt(), any(Player.class))).thenReturn(Optional.empty());
        bike.up();

        //when
        bike.tick();

        //then
        assertThat(bike.getX(), is(5));
        assertThat(bike.getY(), is(6));
    }

    @Test
    public void tickWithUpCommand__shouldMoveAndCrushBike__ifUpperPositionIsFence() {
        //given
        Player player = mock(Player.class);
        when(gameField.getPlayerOfBike(bike)).thenReturn(player);
        when(gameField.isFence(anyInt(), anyInt())).thenReturn(true);
        when(gameField.getEnemyBike(anyInt(), anyInt(), any(Player.class))).thenReturn(Optional.empty());
        bike.up();

        //when
        bike.tick();

        //then
        assertThat(bike.getX(), is(5));
        assertThat(bike.getY(), is(6));
        assertThat(bike.state(getPlayer(bike)), is(BikeType.BIKE_FALLEN_AT_FENCE));
    }

    @Test
    public void tickWithUpCommand__shouldMoveBikeAndChangeItsStateToKilledEnemy__ifUpperPositionIsOtherBike() {
        //given
        when(gameField.isFence(anyInt(), anyInt())).thenReturn(false);
        Bike enemy = new Bike(5, 6);
        Player player = mock(Player.class);
        when(gameField.getPlayerOfBike(bike)).thenReturn(player);
        when(gameField.getEnemyBike(5, 6, player)).thenReturn(Optional.of(enemy));
        when(gameField.getPlayerOfBike(enemy)).thenReturn(player);
        enemy.init(gameField);
        bike.up();

        //when
        bike.tick();

        //then
        assertThat(bike.getX(), is(5));
        assertThat(bike.getY(), is(6));
        assertThat(bike.state(getPlayer(bike)), is(BikeType.BIKE_AT_KILLED_BIKE));
    }

    @Test
    public void tickWithDownCommand__shouldMoveBikeToDown__ifLowerPositionIsFree() {
        //given
        when(gameField.isFence(anyInt(), anyInt())).thenReturn(false);
        when(gameField.getEnemyBike(anyInt(), anyInt(), any(Player.class))).thenReturn(Optional.empty());
        bike.down();

        //when
        bike.tick();

        //then
        assertThat(bike.getX(), is(5));
        assertThat(bike.getY(), is(4));
    }

    @Test
    public void tickWithDownCommand__shouldMoveBikeAndCrushIt__ifLowerPositionIsFence() {
        //given
        Player player = mock(Player.class);
        when(gameField.getPlayerOfBike(bike)).thenReturn(player);
        bike.setY(1);
        when(gameField.isFence(anyInt(), anyInt())).thenReturn(true);
        when(gameField.getEnemyBike(anyInt(), anyInt(), any(Player.class))).thenReturn(Optional.empty());
        bike.down();

        //when
        bike.tick();

        //then
        assertThat(bike.getX(), is(5));
        assertThat(bike.getY(), is(0));
        assertThat(bike.state(getPlayer(bike)), is(BikeType.BIKE_FALLEN_AT_FENCE));
    }

    @Test
    public void tickWithDownCommand__shouldMoveBikeAndChangeItsStateToKilledEnemy__ifLowerPositionIsOtherBike() {
        //given
        when(gameField.isFence(anyInt(), anyInt())).thenReturn(false);
        Bike enemy = new Bike(5, 4);
        Player player = mock(Player.class);
        when(gameField.getPlayerOfBike(bike)).thenReturn(player);
        when(gameField.getEnemyBike(5, 4, player)).thenReturn(Optional.of(enemy));
        when(gameField.getPlayerOfBike(enemy)).thenReturn(player);
        enemy.init(gameField);
        bike.down();

        //when
        bike.tick();

        //then
        assertThat(bike.getX(), is(5));
        assertThat(bike.getY(), is(4));
        assertThat(bike.state(getPlayer(bike)), is(BikeType.BIKE_AT_KILLED_BIKE));
    }

    @Test
    public void tick1__shouldNotChangeBikePositionAndChangeStateToAccelerated__ifBikeTakeAccelerator() {
        //given
        when(gameField.isAccelerator(anyInt(), anyInt())).thenReturn(true);
        when(gameField.xSize()).thenReturn(8);

        //when
        bike.tick();

        //then
        assertThat(bike.getX(), is(5));
        assertThat(bike.getY(), is(5));
        assertThat(bike.state(getPlayer(bike)), is(BikeType.BIKE_AT_ACCELERATOR));
    }

    @Test
    public void tick2__shouldSetBikeXCoordinateToMaxPossible__ifBikePositionAfterAccelerationIsOutOfFieldBound() {
        //given
        when(gameField.isAccelerator(5, 5)).thenReturn(true);
        when(gameField.xSize()).thenReturn(6);
        bike.tick();

        //when
        bike.tick();

        //then
        assertThat(bike.getX(), is(5));
        assertThat(bike.getY(), is(5));
        assertThat(bike.state(getPlayer(bike)), is(BikeType.BIKE_AT_ACCELERATOR));
    }

    @Test
    public void tick1__shouldNotChangeBikePosition_ifBikeTakeInhibitor() {
        //given
        when(gameField.isInhibitor(anyInt(), anyInt())).thenReturn(true);

        //when
        bike.tick();

        //then
        assertThat(bike.getX(), is(5));
        assertThat(bike.getY(), is(5));
        assertThat(bike.state(getPlayer(bike)), is(BikeType.BIKE_AT_INHIBITOR));
    }

    @Test
    public void tick2__shouldMoveBikeBackAndKeepInhibitedState__ifBikeTakeInhibitor() {
        //given
        when(gameField.isInhibitor(anyInt(), anyInt())).thenReturn(true);
        bike.tick();

        //when
        bike.tick();

        //then
        assertThat(bike.getX(), is(4));
        assertThat(bike.getY(), is(5));
        assertThat(bike.state(getPlayer(bike)), is(BikeType.BIKE_AT_INHIBITOR));
    }

    @Test
    public void tick3__shouldNotMoveBikeBackAndChangeStateToNormal__ifBikeTakeInhibitor() {
        //given
        when(gameField.isInhibitor(5, 5)).thenReturn(true);
        bike.tick();
        bike.tick();

        //when
        bike.tick();

        //then
        assertThat(bike.getX(), is(4));
        assertThat(bike.getY(), is(5));
        assertThat(bike.state(getPlayer(bike)), is(BikeType.BIKE));
    }

    @Test
    public void tick2__shouldSetBikeXCoordinateToMinPossible_ifBikePositionAfterInhibitionIsOutOfFieldBound() {
        //given
        bike.setX(1);
        when(gameField.isInhibitor(anyInt(), anyInt())).thenReturn(true);
        bike.tick();

        //when
        bike.tick();

        //then
        assertThat(bike.getX(), is(0));
        assertThat(bike.getY(), is(5));
    }

    @Test
    public void tick__shouldFallBike_ifBikeEncounterWithObstacle() {
        //given
        Player player = mock(Player.class);
        when(gameField.getPlayerOfBike(bike)).thenReturn(player);
        when(gameField.isObstacle(anyInt(), anyInt())).thenReturn(true);

        //when
        bike.tick();

        //then
        assertThat(bike.state(getPlayer(bike)), is(BikeType.BIKE_FALLEN_AT_OBSTACLE));
    }

    @Test
    public void tick1__shouldNotChangeBikePosition__ifBikeTakeUpLineChangerAndUpperPositionIsFree() {
        //given
        when(gameField.isUpLineChanger(anyInt(), anyInt())).thenReturn(true);
        when(gameField.isFence(anyInt(), anyInt())).thenReturn(false);
        when(gameField.getEnemyBike(anyInt(), anyInt(), any(Player.class))).thenReturn(Optional.empty());

        //when
        bike.tick();

        //then
        assertThat(bike.getX(), is(5));
        assertThat(bike.getY(), is(5));
    }

    @Test
    public void tick2__shouldMoveBikeUpper__ifBikeTakeUpLineChangerAndUpperPositionIsFree() {
        //given
        when(gameField.isUpLineChanger(anyInt(), anyInt())).thenReturn(true);
        when(gameField.isFence(anyInt(), anyInt())).thenReturn(false);
        when(gameField.getEnemyBike(anyInt(), anyInt(), any(Player.class))).thenReturn(Optional.empty());
        bike.tick();

        //when
        bike.tick();

        //then
        assertThat(bike.getX(), is(5));
        assertThat(bike.getY(), is(6));
    }

    @Test
    public void tick1__shouldNotMoveAndCrushBike__ifBikeTakeUpLineChangerAndUpperPositionIsFence() {
        //given
        when(gameField.isUpLineChanger(anyInt(), anyInt())).thenReturn(true);
        when(gameField.isFence(anyInt(), anyInt())).thenReturn(true);
        when(gameField.getEnemyBike(anyInt(), anyInt(), any(Player.class))).thenReturn(Optional.empty());

        //when
        bike.tick();

        //then
        assertThat(bike.getX(), is(5));
        assertThat(bike.getY(), is(5));
        assertThat(bike.state(getPlayer(bike)), is(BikeType.BIKE_AT_LINE_CHANGER_UP));
    }

    @Test
    public void tick2__shouldMoveAndCrushBike__ifBikeTakeUpLineChangerAndUpperPositionIsFence() {
        //given
        Player player = mock(Player.class);
        when(gameField.getPlayerOfBike(bike)).thenReturn(player);
        when(gameField.isUpLineChanger(5, 5)).thenReturn(true);
        when(gameField.isFence(5, 6)).thenReturn(true);
        when(gameField.getEnemyBike(anyInt(), anyInt(), any(Player.class))).thenReturn(Optional.empty());
        bike.tick();

        //when
        bike.tick();

        //then
        assertThat(bike.getX(), is(5));
        assertThat(bike.getY(), is(6));
        assertThat(bike.state(getPlayer(bike)), is(BikeType.BIKE_FALLEN_AT_FENCE));
    }

    @Test
    public void tick1__shouldNotMoveBike__ifBikeTakeUpLineChangerAndUpperPositionIsOtherBike() {
        //given
        when(gameField.isUpLineChanger(anyInt(), anyInt())).thenReturn(true);
        when(gameField.isFence(anyInt(), anyInt())).thenReturn(false);
        Bike enemy = new Bike(5, 6);
        Player player = mock(Player.class);
        when(gameField.getPlayerOfBike(bike)).thenReturn(player);
        when(gameField.getEnemyBike(5, 6, player)).thenReturn(Optional.of(enemy));
        enemy.init(gameField);

        //when
        bike.tick();

        //then
        assertThat(bike.getX(), is(5));
        assertThat(bike.getY(), is(5));
    }

    @Test
    public void tick2__shouldMoveBike_ifBikeTakeUpLineChangerAndUpperPositionIsOtherBike() {
        //given
        when(gameField.isUpLineChanger(anyInt(), anyInt())).thenReturn(true);
        when(gameField.isFence(anyInt(), anyInt())).thenReturn(false);
        Bike enemy = new Bike(5, 6);
        Player player = mock(Player.class);
        when(gameField.getPlayerOfBike(bike)).thenReturn(player);
        when(gameField.getEnemyBike(5, 6, player)).thenReturn(Optional.of(enemy));
        when(gameField.getPlayerOfBike(enemy)).thenReturn(player);
        enemy.init(gameField);
        bike.tick();

        //when
        bike.tick();

        //then
        assertThat(bike.getX(), is(5));
        assertThat(bike.getY(), is(6));
    }

    @Test
    public void tick1__shouldNotMoveBike__ifBikeTakeDownLineChangerAndLowerPositionIsFree() {
        //given
        when(gameField.isDownLineChanger(anyInt(), anyInt())).thenReturn(true);
        when(gameField.isFence(anyInt(), anyInt())).thenReturn(false);
        when(gameField.getEnemyBike(anyInt(), anyInt(), any(Player.class))).thenReturn(Optional.empty());

        //when
        bike.tick();

        //then
        assertThat(bike.getX(), is(5));
        assertThat(bike.getY(), is(5));
    }

    @Test
    public void tick2__shouldMoveBikeDown__ifBikeTakeDownLineChangerAndLowerPositionIsFree() {
        //given
        when(gameField.isDownLineChanger(anyInt(), anyInt())).thenReturn(true);
        when(gameField.isFence(anyInt(), anyInt())).thenReturn(false);
        when(gameField.getEnemyBike(anyInt(), anyInt(), any(Player.class))).thenReturn(Optional.empty());
        bike.tick();

        //when
        bike.tick();

        //then
        assertThat(bike.getX(), is(5));
        assertThat(bike.getY(), is(4));
    }

    @Test
    public void tick2__shouldMoveAndCrushBike__ifBikeTakeDownLineChangerAndLowerPositionIsFence() {
        //given
        Player player = mock(Player.class);
        when(gameField.getPlayerOfBike(bike)).thenReturn(player);
        when(gameField.isDownLineChanger(5, 5)).thenReturn(true);
        when(gameField.isFence(5, 4)).thenReturn(true);
        bike.tick();

        //when
        bike.tick();

        //then
        assertThat(bike.getX(), is(5));
        assertThat(bike.getY(), is(4));
        assertThat(bike.isAlive(), is(false));
        assertThat(bike.state(getPlayer(bike)), is(BikeType.BIKE_FALLEN_AT_FENCE));
    }

    @Test
    public void tick1__shouldNotMoveBike__ifBikeTakeDownLineChangerAndLowerPositionIsOtherBike() {
        //given
        when(gameField.isDownLineChanger(5, 5)).thenReturn(true);
        Bike enemy = new Bike(5, 6);
        Player player = mock(Player.class);
        when(gameField.getPlayerOfBike(bike)).thenReturn(player);
        when(gameField.getEnemyBike(5, 6, player)).thenReturn(Optional.of(enemy));
        enemy.init(gameField);

        //when
        bike.tick();

        //then
        assertThat(bike.getX(), is(5));
        assertThat(bike.getY(), is(5));
        assertThat(bike.state(getPlayer(bike)), is(BikeType.BIKE_AT_LINE_CHANGER_DOWN));
    }

    @Test
    public void tick2__shouldMoveBikeDown__ifBikeTakeDownLineChangerAndLowerPositionIsOtherBike() {
        //given
        when(gameField.isDownLineChanger(5, 5)).thenReturn(true);
        Bike enemy = new Bike(5, 4);
        Player player = mock(Player.class);
        when(gameField.getPlayerOfBike(bike)).thenReturn(player);
        when(gameField.getEnemyBike(5, 4, player)).thenReturn(Optional.of(enemy));
        when(gameField.getPlayerOfBike(enemy)).thenReturn(player);
        enemy.init(gameField);
        bike.tick();

        //when
        bike.tick();

        //then
        assertThat(bike.getX(), is(5));
        assertThat(bike.getY(), is(4));
        assertThat(bike.state(getPlayer(bike)), is(BikeType.BIKE_AT_KILLED_BIKE));
    }

    @Test
    public void tick__shouldCrushBike__ifBikeCollideOtherCrushedBike() {
        //given
        Bike enemyBike = new Bike(5, 5);
        enemyBike.crush();
        Player player = mock(Player.class);
        when(gameField.getPlayerOfBike(bike)).thenReturn(player);
        when(gameField.getEnemyBike(5, 5, player)).thenReturn(Optional.of(enemyBike));

        //when
        bike.tick();

        //then
        assertThat(bike.isAlive(), is(false));
        assertThat(bike.getX(), is(5));
        assertThat(bike.getY(), is(5));
        assertThat(enemyBike.getX(), is(5));
        assertThat(enemyBike.getY(), is(5));
    }

    @Test
    public void downCommandToEnemyBikeTick__shouldCrushOtherBike__ifOtherBikeDidNotChangeLine() {
        //given
        Bike enemy = new Bike(5, 4);
        Player player = mock(Player.class);
        when(gameField.getPlayerOfBike(bike)).thenReturn(player);
        when(gameField.getPlayerOfBike(enemy)).thenReturn(player);
        when(gameField.getEnemyBike(5, 4, player)).thenReturn(Optional.of(enemy));
        enemy.init(gameField);

        //when
        bike.down();
        bike.tick();
        enemy.tick();

        //then
        assertThat(bike.isAlive(), is(true));
        assertThat(enemy.isAlive(), is(false));
        assertThat(bike.getX(), is(5));
        assertThat(bike.getY(), is(4));
        assertThat(enemy.getX(), is(5));
        assertThat(enemy.getY(), is(4));
        assertThat(bike.state(getPlayer(bike)), is(BikeType.BIKE_AT_KILLED_BIKE));
    }

    @Test
    public void tick2__shouldCrushBikeAndChangeEnemyBikeStateToAtKilledBike__ifBikeAfterAcceleratorCollidesEnemyBike() {
        //given
        when(gameField.isAccelerator(5, 5)).thenReturn(true);
        when(gameField.xSize()).thenReturn(10);
        Bike enemy = new Bike(6, 5);
        Player player = mock(Player.class);
        when(gameField.getPlayerOfBike(bike)).thenReturn(player);
        when(gameField.getEnemyBike(6, 5, player)).thenReturn(Optional.of(enemy));
        enemy.init(gameField);
        bike.tick();
        //enemy.tick();

        //when
        bike.tick();

        //then
        assertThat(bike.getX(), is(6));
        assertThat(bike.getY(), is(5));
        assertThat(bike.isAlive(), is(false));
        assertThat(enemy.state(getPlayer(bike)), is(BikeType.OTHER_BIKE_AT_KILLED_BIKE));
        assertThat(enemy.state(getPlayer(enemy)), is(BikeType.BIKE_AT_KILLED_BIKE));
    }

    @Test
    public void tick__shouldMoveBikeDownToFlight__ifBikeGoToSpringboardDarkElementTypeUnderRoad() {
        //given
        bike.setY(2);
        bike.down();
        when(gameField.isSpringboardLeftOrDownElement(anyInt(), anyInt())).thenReturn(true);
        bike.tick();

        //when
        bike.tick();

        //then
        assertThat(bike.getX(), is(5));
        assertThat(bike.getY(), is(0));
    }

    @Test
    public void tick__shouldFireLoseEventAfterCrush() {
        //given
        Player player = mock(Player.class);
        when(gameField.getPlayerOfBike(bike)).thenReturn(player);
        when(gameField.isObstacle(bike.getX(), bike.getY())).thenReturn(true);

        //when
        bike.tick();

        //then
        verify(player).event(Events.LOSE);
    }

}
