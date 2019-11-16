package com.codenjoy.dojo.excitebike.services.generation;

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

import com.codenjoy.dojo.excitebike.model.elements.GameElementType;
import com.codenjoy.dojo.excitebike.model.items.Shiftable;
import com.codenjoy.dojo.excitebike.model.elements.SpringboardElementType;
import com.codenjoy.dojo.excitebike.services.generation.generator.SpringboardGenerator;
import com.codenjoy.dojo.services.Dice;
import com.codenjoy.dojo.services.printer.CharElements;
import org.junit.Test;

import java.util.List;
import java.util.Map;

import static com.codenjoy.dojo.excitebike.services.generation.GenerationOption.NOTHING;
import static com.codenjoy.dojo.excitebike.services.generation.GenerationOption.OBSTACLE_CHAIN;
import static com.codenjoy.dojo.excitebike.services.generation.GenerationOption.SINGLE_ELEMENT;
import static com.codenjoy.dojo.excitebike.services.generation.GenerationOption.SPRINGBOARD;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.hamcrest.core.IsNull.nullValue;
import static org.hamcrest.number.OrderingComparison.greaterThan;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Created by Pavel Bobylev 7/19/2019
 */
public class TrackStepGeneratorTest {

    @Test
    public void generate__shouldReturnSpringboardElements__ifDiceReturnedNumberCorrespondingToSpringboardAmongOtherOptions() {
        //given
        Dice dice = mock(Dice.class);
        int xSize = 10;
        int ySize = 10;
        WeightedRandomBag<GenerationOption> weightedRandomBag = getWeightedRandomgenerationOptionBag();
        TrackStepGenerator generator = new TrackStepGenerator(dice, xSize, ySize);
        when(dice.next(19)).thenReturn(16);
        when(dice.next(SpringboardGenerator.SPRINGBOARD_TOP_MAX_WIDTH)).thenReturn(3);

        //when
        Map<? extends CharElements, List<Shiftable>> result = generator.generate(weightedRandomBag);

        //then
        assertThat(result.values(), hasSize(7));
        assertThat(result.get(SpringboardElementType.SPRINGBOARD_LEFT_UP), hasSize(1));
        assertThat(result.get(SpringboardElementType.SPRINGBOARD_LEFT), hasSize(10));
        assertThat(result.get(SpringboardElementType.SPRINGBOARD_LEFT_DOWN), hasSize(1));
        assertThat(result.get(SpringboardElementType.SPRINGBOARD_TOP), hasSize(24));
        assertThat(result.get(SpringboardElementType.SPRINGBOARD_RIGHT_UP), hasSize(1));
        assertThat(result.get(SpringboardElementType.SPRINGBOARD_RIGHT), hasSize(7));
        assertThat(result.get(SpringboardElementType.SPRINGBOARD_RIGHT_DOWN), hasSize(1));
    }

    @Test
    public void generate__shouldReturnNullSevenTimes__afterItReturnedSpringboardWithWidthFive() {
        //given
        Dice dice = mock(Dice.class);
        int xSize = 10;
        int ySize = 10;
        WeightedRandomBag<GenerationOption> weightedRandomBag = getWeightedRandomgenerationOptionBag();
        TrackStepGenerator generator = new TrackStepGenerator(dice, xSize, ySize);
        when(dice.next(19)).thenReturn(16, 12);
        when(dice.next(SpringboardGenerator.SPRINGBOARD_TOP_MAX_WIDTH)).thenReturn(3);

        //when
        Map<? extends CharElements, List<Shiftable>> result = generator.generate(weightedRandomBag);

        //then
        assertThat(result.values(), hasSize(7));
        assertThat(result.get(SpringboardElementType.SPRINGBOARD_LEFT_UP), hasSize(1));
        assertThat(result.get(SpringboardElementType.SPRINGBOARD_LEFT), hasSize(10));
        assertThat(result.get(SpringboardElementType.SPRINGBOARD_LEFT_DOWN), hasSize(1));
        assertThat(result.get(SpringboardElementType.SPRINGBOARD_TOP), hasSize(24));
        assertThat(result.get(SpringboardElementType.SPRINGBOARD_RIGHT_UP), hasSize(1));
        assertThat(result.get(SpringboardElementType.SPRINGBOARD_RIGHT), hasSize(7));
        assertThat(result.get(SpringboardElementType.SPRINGBOARD_RIGHT_DOWN), hasSize(1));
        for (int i = 1; i <= 7; i++) {
            assertThat(generator.generate(weightedRandomBag), nullValue());
        }
        assertThat(generator.generate(weightedRandomBag).values(), hasSize(1));
    }

    @Test
    public void generate__shouldReturnNull__ifDiceReturnedNumberCorrespondingToNothingAmongOtherOptions() {
        //given
        Dice dice = mock(Dice.class);
        int xSize = 10;
        int ySize = 10;
        WeightedRandomBag<GenerationOption> weightedRandomBag = getWeightedRandomgenerationOptionBag();
        TrackStepGenerator generator = new TrackStepGenerator(dice, xSize, ySize);
        when(dice.next(19)).thenReturn(5);

        //when
        Map<? extends CharElements, List<Shiftable>> result = generator.generate(weightedRandomBag);

        //then
        assertThat(result, nullValue());
    }

    @Test
    public void generate__shouldReturnMapWithSingleElement__ifDiceReturnedNumberCorrespondingToSingleElementAmongOtherOptions() {
        //given
        Dice dice = mock(Dice.class);
        int xSize = 10;
        int ySize = 10;
        WeightedRandomBag<GenerationOption> weightedRandomBag = getWeightedRandomgenerationOptionBag();
        TrackStepGenerator generator = new TrackStepGenerator(dice, xSize, ySize);
        when(dice.next(19)).thenReturn(12);

        //when
        Map<? extends CharElements, List<Shiftable>> result = generator.generate(weightedRandomBag);

        //then
        assertThat(result.values(), hasSize(1));
        assertThat(generator.generate(weightedRandomBag).values(), hasSize(1));
        assertThat(result.get(GameElementType.ACCELERATOR), hasSize(1));
    }

    @Test
    public void generate__shouldReturnMapWithObstacleChain__ifDiceReturnedNumberCorrespondingToObstacleChainAmongOtherOptions() {
        //given
        Dice dice = mock(Dice.class);
        int xSize = 10;
        int ySize = 10;
        WeightedRandomBag<GenerationOption> weightedRandomBag = getWeightedRandomgenerationOptionBag();
        TrackStepGenerator generator = new TrackStepGenerator(dice, xSize, ySize);
        when(dice.next(19)).thenReturn(18);

        //when
        Map<? extends CharElements, List<Shiftable>> result = generator.generate(weightedRandomBag);

        //then
        assertThat(result.values(), hasSize(1));
        assertThat(result.get(GameElementType.OBSTACLE).size(), greaterThan(0));
    }

    private WeightedRandomBag<GenerationOption> getWeightedRandomgenerationOptionBag() {
        WeightedRandomBag<GenerationOption> weightedRandomBag = new WeightedRandomBag<>();
        weightedRandomBag.addEntry(NOTHING, 10);
        weightedRandomBag.addEntry(SINGLE_ELEMENT, 5);
        weightedRandomBag.addEntry(SPRINGBOARD, 2);
        weightedRandomBag.addEntry(OBSTACLE_CHAIN, 1);
        return weightedRandomBag;
    }
}
