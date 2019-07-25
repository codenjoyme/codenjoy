package com.codenjoy.dojo.excitebike.services.generation.generator;

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

import com.codenjoy.dojo.excitebike.model.items.Shiftable;
import com.codenjoy.dojo.services.Dice;
import com.codenjoy.dojo.services.printer.CharElements;
import org.junit.Test;

import java.util.List;
import java.util.Map;

import static com.codenjoy.dojo.excitebike.model.elements.GameElementType.OBSTACLE;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.hamcrest.core.Is.is;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Created by Pavel Bobylev 7/19/2019
 */
public class ObstacleChainGeneratorTest {

    @Test
    public void generate__shouldReturnStraightObstaclesLineWithOneExit__ifDiceReturnedNumbersAccordingToIt() {
        //given
        Dice dice = mock(Dice.class);
        int xSize = 10;
        int ySize = 10;
        ObstacleChainGenerator generator = new ObstacleChainGenerator(dice, xSize, ySize);
        when(dice.next(4)).thenReturn(0, 0);
        when(dice.next(10)).thenReturn(6, 7, 7, 8, 9, 1);

        //when
        Map<? extends CharElements, List<Shiftable>> result = generator.generate();

        //then
        assertThat(result.values(), hasSize(1));
        List<Shiftable> obstacles = result.get(OBSTACLE);
        assertThat(obstacles, hasSize(7));
        for (int i = 0; i < obstacles.size(); i++) {
            assertThat(obstacles.get(i).getX(), is(9));
            assertThat(obstacles.get(i).getY(), is(i + (i < 5 ? 1 : 2)));
        }
    }

    @Test
    public void generate__shouldReturnStraightObstaclesLineWithThreeExits__ifDiceReturnedNumbersAccordingToIt() {
        //given
        Dice dice = mock(Dice.class);
        int xSize = 10;
        int ySize = 10;
        ObstacleChainGenerator generator = new ObstacleChainGenerator(dice, xSize, ySize);
        when(dice.next(3)).thenReturn(0);
        when(dice.next(4)).thenReturn(2);
        when(dice.next(10)).thenReturn(1, 7, 6, 8, 2, 7, 9, 6);

        //when
        Map<? extends CharElements, List<Shiftable>> result = generator.generate();

        //then
        assertThat(result.values(), hasSize(1));
        List<Shiftable> obstacles = result.get(OBSTACLE);
        assertThat(obstacles, hasSize(5));
        for (int i = 0; i < obstacles.size(); i++) {
            assertThat(obstacles.get(i).getX(), is(9));
            assertThat(obstacles.get(i).getY(), is(i + (i < 3 ? 2 : 3)));
        }
    }

    @Test
    public void generate__shouldReturnLadderUpObstaclesLineWithOneExit__ifDiceReturnedNumbersAccordingToIt() {
        //given
        Dice dice = mock(Dice.class);
        int xSize = 10;
        int ySize = 10;
        ObstacleChainGenerator generator = new ObstacleChainGenerator(dice, xSize, ySize);
        when(dice.next(3)).thenReturn(1);
        when(dice.next(4)).thenReturn(0);
        when(dice.next(ySize - 2)).thenReturn(7);
        when(dice.next(10)).thenReturn(1, 7, 6, 8, 2, 7, 9, 6);

        //when
        Map<? extends CharElements, List<Shiftable>> result = generator.generate();

        //then
        assertThat(result.values(), hasSize(1));
        List<Shiftable> obstacles = result.get(OBSTACLE);
        assertThat(obstacles, hasSize(7));
        for (int i = 0; i < obstacles.size(); i++) {
            assertThat(obstacles.get(i).getX(), is(ySize + i));
            assertThat(obstacles.get(i).getY(), is(i + 2));
        }
    }

    @Test
    public void generate__shouldReturnLadderUpObstaclesLineWithThreeExits__ifDiceReturnedNumbersAccordingToIt() {
        //given
        Dice dice = mock(Dice.class);
        int xSize = 10;
        int ySize = 10;
        ObstacleChainGenerator generator = new ObstacleChainGenerator(dice, xSize, ySize);
        when(dice.next(3)).thenReturn(1);
        when(dice.next(4)).thenReturn(2);
        when(dice.next(ySize - 2)).thenReturn(7);
        when(dice.next(10)).thenReturn(1, 7, 6, 8, 2, 7, 9, 6);

        //when
        Map<? extends CharElements, List<Shiftable>> result = generator.generate();

        //then
        assertThat(result.values(), hasSize(1));
        List<Shiftable> obstacles = result.get(OBSTACLE);
        assertThat(obstacles, hasSize(5));
        for (int i = 0; i < obstacles.size(); i++) {
            assertThat(obstacles.get(i).getX(), is(ySize + (i < 3 ? i : i + 1)));
            assertThat(obstacles.get(i).getY(), is(i + (i < 3 ? 2 : 3)));
        }
    }

    @Test
    public void generate__shouldReturnUpToDownToUpLadderObstaclesLineWithWidth3AndOneExits__ifDiceReturnedNumbersAccordingToIt() {
        //given
        Dice dice = mock(Dice.class);
        int xSize = 10;
        int ySize = 10;
        ObstacleChainGenerator generator = new ObstacleChainGenerator(dice, xSize, ySize);
        when(dice.next(3)).thenReturn(1);
        when(dice.next(4)).thenReturn(0);
        when(dice.next(ySize - 2)).thenReturn(2);
        when(dice.next(10)).thenReturn(0, 1, 2, 3, 4, 7, 9, 6, 9);

        //when
        Map<? extends CharElements, List<Shiftable>> result = generator.generate();

        //then
        assertThat(result.values(), hasSize(1));
        List<Shiftable> obstacles = result.get(OBSTACLE);
        assertThat(obstacles, hasSize(7));
        for (int i = 0; i < obstacles.size(); i++) {
            int expectedX;
            if (i == 0 || i == 2 || i == 4 || i == 6) {
                expectedX = 10;
            } else if (i == 3) {
                expectedX = 9;
            } else {
                expectedX = 11;
            }
            assertThat(obstacles.get(i).getX(), is(expectedX));
            assertThat(obstacles.get(i).getY(), is(i + 2));
        }
    }

    @Test
    public void generate__shouldReturnLadderDownObstaclesLineWithOneExit__ifDiceReturnedNumbersAccordingToIt() {
        //given
        Dice dice = mock(Dice.class);
        int xSize = 10;
        int ySize = 10;
        ObstacleChainGenerator generator = new ObstacleChainGenerator(dice, xSize, ySize);
        when(dice.next(3)).thenReturn(2);
        when(dice.next(4)).thenReturn(0);
        when(dice.next(ySize - 2)).thenReturn(7);
        when(dice.next(10)).thenReturn(1, 7, 6, 8, 2, 7, 9, 6);
        int expectedX = 15;
        int expectedY = 2;

        //when
        Map<? extends CharElements, List<Shiftable>> result = generator.generate();

        //then
        assertThat(result.values(), hasSize(1));
        List<Shiftable> obstacles = result.get(OBSTACLE);
        assertThat(obstacles, hasSize(7));
        for (Shiftable obstacle : obstacles) {
            assertThat(obstacle.getX(), is(expectedX));
            assertThat(obstacle.getY(), is(expectedY));
            expectedX--;
            expectedY++;
        }
    }

    @Test
    public void generate__shouldReturnLadderDownObstaclesLineWithThreeExits__ifDiceReturnedNumbersAccordingToIt() {
        //given
        Dice dice = mock(Dice.class);
        int xSize = 10;
        int ySize = 10;
        ObstacleChainGenerator generator = new ObstacleChainGenerator(dice, xSize, ySize);
        when(dice.next(3)).thenReturn(2);
        when(dice.next(4)).thenReturn(2);
        when(dice.next(ySize - 2)).thenReturn(7);
        when(dice.next(10)).thenReturn(1, 7, 6, 8, 2, 7, 9, 6);
        int expectedX = 15;
        int expectedY = 2;

        //when
        Map<? extends CharElements, List<Shiftable>> result = generator.generate();

        //then
        assertThat(result.values(), hasSize(1));
        List<Shiftable> obstacles = result.get(OBSTACLE);
        assertThat(obstacles, hasSize(5));
        for (int i = 0; i <= obstacles.size() - 1; i++) {
            assertThat(obstacles.get(i).getX(), is(expectedX));
            assertThat(obstacles.get(i).getY(), is(expectedY));
            if (i == 2) {
                expectedX--;
                expectedY++;
            }
            expectedX--;
            expectedY++;
        }
    }

    @Test
    public void generate__shouldReturnDownToUpToDownLadderObstaclesLineWithWidth3AndOneExits__ifDiceReturnedNumbersAccordingToIt() {
        //given
        Dice dice = mock(Dice.class);
        int xSize = 10;
        int ySize = 10;
        ObstacleChainGenerator generator = new ObstacleChainGenerator(dice, xSize, ySize);
        when(dice.next(3)).thenReturn(2);
        when(dice.next(4)).thenReturn(0);
        when(dice.next(ySize - 2)).thenReturn(2);
        when(dice.next(10)).thenReturn(0, 1, 2, 3, 4, 7, 9, 6, 9);

        //when
        Map<? extends CharElements, List<Shiftable>> result = generator.generate();

        //then
        assertThat(result.values(), hasSize(1));
        List<Shiftable> obstacles = result.get(OBSTACLE);
        assertThat(obstacles, hasSize(7));
        for (int i = 0; i < obstacles.size(); i++) {
            int expectedX;
            if (i == 0 || i == 2 || i == 4 || i == 6) {
                expectedX = 10;
            } else if (i == 3) {
                expectedX = 11;
            } else {
                expectedX = 9;
            }
            assertThat(obstacles.get(i).getX(), is(expectedX));
            assertThat(obstacles.get(i).getY(), is(i + 2));
        }
    }
}
