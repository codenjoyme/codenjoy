package com.codenjoy.dojo.excitebike.model;

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

import com.codenjoy.dojo.excitebike.model.items.Accelerator;
import com.codenjoy.dojo.excitebike.model.items.Inhibitor;
import com.codenjoy.dojo.excitebike.model.items.LineChanger;
import com.codenjoy.dojo.excitebike.model.items.Obstacle;
import com.codenjoy.dojo.excitebike.model.items.bike.Bike;
import com.codenjoy.dojo.excitebike.services.parse.MapParser;
import com.codenjoy.dojo.services.Dice;
import com.codenjoy.dojo.services.EventListener;
import org.hamcrest.CoreMatchers;
import org.hamcrest.MatcherAssert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.Optional;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.assertThat;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class GameFieldImplTest {

    private GameField gameField;
    private MapParser mapParser;
    private Dice dice;

    @Before
    public void init() {
        mapParser = mock(MapParser.class);
        dice = mock(Dice.class);
    }

    @Test
    public void isFence__shouldReturnTrue__IfYEqualsZero() {
        //given
        int x = 1, y = 0;
        gameField = new GameFieldImpl(mapParser, dice);

        //when
        boolean result = gameField.isFence(x, y);

        //then
        assertThat(result, is(true));
    }

    @Test
    public void isFence__shouldReturnTrue__IfYEqualsMaxPossibleValue() {
        //given
        int x = 1, y = 2;
        when(mapParser.getYSize()).thenReturn(3);
        gameField = new GameFieldImpl(mapParser, dice);

        //when
        boolean result = gameField.isFence(x, y);

        //then
        assertThat(result, is(true));
    }

    @Test
    public void isFence__shouldReturnFalse__IfYIsNotZeroAndMaxPossibleValue() {
        //given
        int x = 1, y = 1;
        when(mapParser.getYSize()).thenReturn(3);
        gameField = new GameFieldImpl(mapParser, dice);

        //when
        boolean result = gameField.isFence(x, y);

        //then
        assertThat(result, is(false));
    }

    @Test
    public void isInhibitor__shouldReturnTrue() {
        //given
        int x = 1, y = 1;
        Inhibitor inhibitor = new Inhibitor(x, y);
        when(mapParser.getInhibitors()).thenReturn(Collections.singletonList(inhibitor));
        gameField = new GameFieldImpl(mapParser, dice);

        //when
        boolean result = gameField.isInhibitor(x, y);

        //then
        assertThat(result, is(true));
    }

    @Test
    public void isAccelerator__shouldReturnTrue() {
        //given
        int x = 1, y = 1;
        Accelerator accelerator = new Accelerator(x, y);
        when(mapParser.getAccelerators()).thenReturn(Collections.singletonList(accelerator));
        gameField = new GameFieldImpl(mapParser, dice);

        //when
        boolean result = gameField.isAccelerator(x, y);

        //then
        assertThat(result, is(true));
    }

    @Test
    public void isObstacle__shouldReturnTrue() {
        //given
        int x = 1, y = 1;
        Obstacle obstacle = new Obstacle(x, y);
        when(mapParser.getObstacles()).thenReturn(Collections.singletonList(obstacle));
        gameField = new GameFieldImpl(mapParser, dice);

        //when
        boolean result = gameField.isObstacle(x, y);

        //then
        assertThat(result, is(true));
    }

    @Test
    public void isUpLineChanger__shouldReturnTrue() {
        //given
        int x = 1, y = 1;
        LineChanger lineChanger = new LineChanger(x, y, true);
        when(mapParser.getLineUpChangers()).thenReturn(Collections.singletonList(lineChanger));
        gameField = new GameFieldImpl(mapParser, dice);

        //when
        boolean result = gameField.isUpLineChanger(x, y);

        //then
        assertThat(result, is(true));
    }

    @Test
    public void isDownLineChanger__shouldReturnTrue() {
        //given
        int x = 1, y = 1;
        LineChanger lineChanger = new LineChanger(x, y, false);
        when(mapParser.getLineDownChangers()).thenReturn(Collections.singletonList(lineChanger));
        gameField = new GameFieldImpl(mapParser, dice);

        //when
        boolean result = gameField.isDownLineChanger(x, y);

        //then
        assertThat(result, is(true));
    }

    @Test
    public void getEnemyBike__shouldReturnEmptyOptional__ifThereIsOnlyThisBikeAtGivenCoordinates() {
        //given
        gameField = new GameFieldImpl(mapParser, dice);
        Player player = new Player(mock(EventListener.class));
        gameField.newGame(player);

        //when
        Optional<Bike> result = gameField.getEnemyBike(player.getHero().getX(), player.getHero().getY(), player);

        //then
        assertThat(result.isPresent(), is(false));
    }

    @Test
    public void getEnemyBike__shouldReturnEmptyOptional__ifGivenPlayerIsNull() {
        //given
        gameField = new GameFieldImpl(mapParser, dice);
        Player player = new Player(mock(EventListener.class));
        gameField.newGame(player);

        //when
        Optional<Bike> result = gameField.getEnemyBike(player.getHero().getX(), player.getHero().getY(), null);

        //then
        assertThat(result.isPresent(), is(false));
    }

    @Test
    public void getEnemyBike__shouldReturnOptionalWithEnemyBike__ifThereIsOneAtGivenCoordinates() {
        //given
        gameField = new GameFieldImpl(mapParser, dice);
        int x = 1;
        int y = 1;
        Bike thisBike = new Bike(x, y);
        Bike enemyBike = new Bike(x, y);
        Player thisPlayer = new Player(mock(EventListener.class));
        thisPlayer.setHero(thisBike);
        Player enemyPlayer = new Player(mock(EventListener.class));
        enemyPlayer.setHero(enemyBike);
        gameField.newGame(thisPlayer);
        gameField.newGame(enemyPlayer);

        //when
        Optional<Bike> result = gameField.getEnemyBike(x, y, thisPlayer);

        //then
        assertThat(result.isPresent(), is(true));
    }

    @Test
    public void tick__shouldShiftAllShiftableElementsAndRemoveTheseAreOutOfBound() {
        //given
        Accelerator accelerator = new Accelerator(0, 1);
        Inhibitor inhibitor = new Inhibitor(1, 1);
        Obstacle obstacle = new Obstacle(2, 1);
        LineChanger upperLineChanger = new LineChanger(0, 2, true);
        LineChanger lowerLineChanger = new LineChanger(1, 2, false);

        when(mapParser.getAccelerators()).thenReturn(new ArrayList<>(Collections.singletonList(accelerator)));
        when(mapParser.getInhibitors()).thenReturn(new ArrayList<>(Collections.singletonList(inhibitor)));
        when(mapParser.getObstacles()).thenReturn(new ArrayList<>(Collections.singletonList(obstacle)));
        when(mapParser.getLineUpChangers()).thenReturn(new ArrayList<>(Collections.singletonList(upperLineChanger)));
        when(mapParser.getLineDownChangers()).thenReturn(new ArrayList<>(Collections.singletonList(lowerLineChanger)));

        gameField = new GameFieldImpl(mapParser, dice);

        when(dice.next(anyInt())).thenReturn(5);

        //when
        gameField.tick();

        //then
        assertThat(accelerator.getX(), is(-1));
        assertThat(gameField.isAccelerator(accelerator.getX(), accelerator.getY()), is(false));
        assertThat(inhibitor.getX(), is(0));
        assertThat(obstacle.getX(), is(1));
        assertThat(upperLineChanger.getX(), is(-1));
        assertThat(gameField.isUpLineChanger(upperLineChanger.getX(), upperLineChanger.getY()), is(false));
        assertThat(lowerLineChanger.getX(), is(0));
    }

    @Test
    public void tick__shouldGenerateAccelerator() {
        //given
        int xSize = 5;
        int generateChance = 1; //if less than 5 then element will be generated
        int nonFenceElementOrdinal = 0;
        int nonFenceLaneNumber = 0;

        gameField = new GameFieldImpl(mapParser, dice);
        when(mapParser.getXSize()).thenReturn(xSize);
        when(dice.next(anyInt())).thenReturn(generateChance,5, nonFenceElementOrdinal, nonFenceLaneNumber);

        //when
        gameField.tick();

        //then
        assertThat(gameField.isAccelerator(xSize - 1, 1), is(true));
    }

    @Test
    public void tick__shouldSetAllPlayersBikesToNull() {
        //given
        Player player2 = new Player(mock(EventListener.class));

        gameField = new GameFieldImpl(mapParser, dice);
        gameField.newGame(new Player(mock(EventListener.class)));
        gameField.newGame(player2);

        when(dice.next(anyInt())).thenReturn(5);

        //when
        player2.getHero().crush();
        gameField.tick();

        //then
        assertThat(gameField.getPlayers().stream().allMatch(player -> player.getHero() == null), is(true));
    }

    @Test
    public void tick__shouldClearAllShiftableElements() {
        //given
        when(dice.next(anyInt())).thenReturn(5);
        when(mapParser.getInhibitors()).thenReturn(new LinkedList<Inhibitor>() {
            {
                add(new Inhibitor(1, 1));
            }
        });
        when(mapParser.getAccelerators()).thenReturn(new LinkedList<Accelerator>() {
            {
                add(new Accelerator(1, 2));
            }
        });
        when(mapParser.getObstacles()).thenReturn(new LinkedList<Obstacle>() {
            {
                add(new Obstacle(1, 3));
            }
        });
        when(mapParser.getLineUpChangers()).thenReturn(new LinkedList<LineChanger>() {
            {
                add(new LineChanger(1, 4, true));
            }
        });
        when(mapParser.getLineDownChangers()).thenReturn(new LinkedList<LineChanger>() {
            {
                add(new LineChanger(1, 5, false));
            }
        });
        Player player2 = new Player(mock(EventListener.class));

        gameField = new GameFieldImpl(mapParser, dice);
        gameField.newGame(new Player(mock(EventListener.class)));
        gameField.newGame(player2);

        //when
        player2.getHero().crush();
        gameField.tick();

        //then
        assertThat(gameField.isInhibitor(0, 1), is(false));
        assertThat(gameField.isAccelerator(0, 2), is(false));
        assertThat(gameField.isObstacle(0, 3), is(false));
        assertThat(gameField.isUpLineChanger(0, 4), is(false));
        assertThat(gameField.isDownLineChanger(0, 5), is(false));
    }

    @Test
    public void getPlayerOfBike__shouldReturnNull__ifThereIsNoPlayerWithGivenBike() {
        //given
        gameField = new GameFieldImpl(mapParser, dice);
        Player player = new Player(mock(EventListener.class));
        gameField.newGame(player);

        //when
        Player result = gameField.getPlayerOfBike(new Bike(2, 22));

        //then
        assertThat(result, nullValue());
    }

    @Test
    public void getPlayerOfBike__shouldReturnOptionalWithEnemyBike__ifThereIsOneAtGivenCoordinates() {
        //given
        gameField = new GameFieldImpl(mapParser, dice);
        int x = 1;
        int y = 1;
        Bike givenBike = new Bike(x, y);
        Player givenPlayer = new Player(mock(EventListener.class));
        givenPlayer.setHero(givenBike);
        gameField.newGame(givenPlayer);
        //when
        Player result = gameField.getPlayerOfBike(givenBike);

        //then
        assertThat(result, is(givenPlayer));
    }

}
