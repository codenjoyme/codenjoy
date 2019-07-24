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
import com.codenjoy.dojo.excitebike.model.items.springboard.SpringboardElementType;
import com.codenjoy.dojo.services.Dice;
import org.junit.Test;

import java.util.List;
import java.util.Map;

import static com.codenjoy.dojo.excitebike.model.items.springboard.SpringboardElementType.SPRINGBOARD_LEFT;
import static com.codenjoy.dojo.excitebike.model.items.springboard.SpringboardElementType.SPRINGBOARD_LEFT_DOWN;
import static com.codenjoy.dojo.excitebike.model.items.springboard.SpringboardElementType.SPRINGBOARD_LEFT_UP;
import static com.codenjoy.dojo.excitebike.model.items.springboard.SpringboardElementType.SPRINGBOARD_RIGHT;
import static com.codenjoy.dojo.excitebike.model.items.springboard.SpringboardElementType.SPRINGBOARD_RIGHT_DOWN;
import static com.codenjoy.dojo.excitebike.model.items.springboard.SpringboardElementType.SPRINGBOARD_RIGHT_UP;
import static com.codenjoy.dojo.excitebike.model.items.springboard.SpringboardElementType.SPRINGBOARD_TOP;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Created by Pavel Bobylev 7/19/2019
 */
public class SpringboardGeneratorTest {

    @Test
    public void generate__shouldReturnCorrectSpringBoardWithWidth5__whenDiceReturnsWidth5() {
        //given
        Dice dice = mock(Dice.class);
        int xSize = 10;
        int ySize = 10;
        SpringboardGenerator generator = new SpringboardGenerator(dice, xSize, ySize);
        int expectedWidth = 5;
        when(dice.next(SpringboardGenerator.SPRINGBOARD_TOP_MAX_WIDTH)).thenReturn(expectedWidth - 2);

        //when
        Map<SpringboardElementType, List<Shiftable>> result = generator.generate();

        //then
        List<Shiftable> leftUps = result.get(SPRINGBOARD_LEFT_UP);
        assertThat(leftUps, hasSize(1));
        assertThat(leftUps.get(0).getX(), is(xSize));
        assertThat(leftUps.get(0).getY(), is(ySize - 1));

        List<Shiftable> lefts = result.get(SPRINGBOARD_LEFT);
        assertThat(lefts, hasSize(10));
        for (int i = 0; i < 10 - (expectedWidth - 2); i++) {
            assertThat(lefts.get(i).getX(), is(xSize));
            assertThat(lefts.get(i).getY(), is(i + 2));
        }
        for (int i = 10 - (expectedWidth - 2); i < 10; i++) {
            assertThat(lefts.get(i).getX(), is(xSize + i - (ySize - 4)));
            assertThat(lefts.get(i).getY(), is(1));
        }

        List<Shiftable> leftDowns = result.get(SPRINGBOARD_LEFT_DOWN);
        assertThat(leftDowns, hasSize(1));
        assertThat(leftDowns.get(0).getX(), is(xSize));
        assertThat(leftDowns.get(0).getY(), is(1));

        List<Shiftable> tops = result.get(SPRINGBOARD_TOP);
        assertThat(tops, hasSize(24));
        for (int i = 0; i < 8; i++) {
            assertThat(tops.get(i).getX(), is(xSize + 1));
            assertThat(tops.get(i).getY(), is(i + 2));
        }
        for (int i = 8; i < 16; i++) {
            assertThat(tops.get(i).getX(), is(xSize + 2));
            assertThat(tops.get(i).getY(), is(i - (ySize - 4)));
        }
        for (int i = 16; i < 24; i++) {
            assertThat(tops.get(i).getX(), is(xSize + 3));
            assertThat(tops.get(i).getY(), is(i - (ySize + 4)));
        }

        List<Shiftable> rightUps = result.get(SPRINGBOARD_RIGHT_UP);
        assertThat(rightUps, hasSize(1));
        assertThat(rightUps.get(0).getX(), is(xSize + 4));
        assertThat(rightUps.get(0).getY(), is(ySize - 1));

        List<Shiftable> rights = result.get(SPRINGBOARD_RIGHT);
        assertThat(rights, hasSize(ySize - 3));
        for (int i = 0; i < ySize - 3; i++) {
            assertThat(rights.get(i).getX(), is(xSize + 4));
            assertThat(rights.get(i).getY(), is(i + 2));
        }

        List<Shiftable> rightDowns = result.get(SPRINGBOARD_RIGHT_DOWN);
        assertThat(rightDowns, hasSize(1));
        assertThat(rightDowns.get(0).getX(), is(xSize + 4));
        assertThat(rightDowns.get(0).getY(), is(1));

        assertThat(generator.generationLockSize(), is(expectedWidth + SpringboardGenerator.CLEAR_LINES_AROUND_SPRINGBOARD * 2));
    }

    @Test
    public void generate__shouldReturnCorrectSpringBoardWithWidth2__whenDiceReturnsWidth2() {
        //given
        Dice dice = mock(Dice.class);
        int xSize = 10;
        int ySize = 10;
        SpringboardGenerator generator = new SpringboardGenerator(dice, xSize, ySize);
        int expectedWidth = 2;
        when(dice.next(SpringboardGenerator.SPRINGBOARD_TOP_MAX_WIDTH)).thenReturn(expectedWidth - 2);

        //when
        Map<SpringboardElementType, List<Shiftable>> result = generator.generate();

        //then
        List<Shiftable> leftUps = result.get(SPRINGBOARD_LEFT_UP);
        assertThat(leftUps, hasSize(1));
        assertThat(leftUps.get(0).getX(), is(xSize));
        assertThat(leftUps.get(0).getY(), is(ySize - 1));

        List<Shiftable> lefts = result.get(SPRINGBOARD_LEFT);
        assertThat(lefts, hasSize(7));
        for (int i = 0; i < 7; i++) {
            assertThat(lefts.get(i).getX(), is(xSize));
            assertThat(lefts.get(i).getY(), is(i + 2));
        }

        List<Shiftable> leftDowns = result.get(SPRINGBOARD_LEFT_DOWN);
        assertThat(leftDowns, hasSize(1));
        assertThat(leftDowns.get(0).getX(), is(xSize));
        assertThat(leftDowns.get(0).getY(), is(1));

        List<Shiftable> tops = result.get(SPRINGBOARD_TOP);
        assertThat(tops, is(nullValue()));

        List<Shiftable> rightUps = result.get(SPRINGBOARD_RIGHT_UP);
        assertThat(rightUps, hasSize(1));
        assertThat(rightUps.get(0).getX(), is(xSize + 1));
        assertThat(rightUps.get(0).getY(), is(ySize - 1));

        List<Shiftable> rights = result.get(SPRINGBOARD_RIGHT);
        assertThat(rights, hasSize(ySize - 3));
        for (int i = 0; i < ySize - 3; i++) {
            assertThat(rights.get(i).getX(), is(xSize + 1));
            assertThat(rights.get(i).getY(), is(i + 2));
        }

        List<Shiftable> rightDowns = result.get(SPRINGBOARD_RIGHT_DOWN);
        assertThat(rightDowns, hasSize(1));
        assertThat(rightDowns.get(0).getX(), is(xSize + 1));
        assertThat(rightDowns.get(0).getY(), is(1));

        assertThat(generator.generationLockSize(), is(expectedWidth + SpringboardGenerator.CLEAR_LINES_AROUND_SPRINGBOARD * 2));
    }
}
