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

import com.codenjoy.dojo.services.Dice;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Created by Pavel Bobylev 7/19/2019
 */
public class WeightedRandomBagTest {

    @Test
    public void getRandom__shouldReturnNull__ifNoOptionsWereSet() {
        //given
        WeightedRandomBag<GenerationOption> bag = new WeightedRandomBag<>();
        Dice dice = mock(Dice.class);
        when(dice.next(1)).thenReturn(0);

        //when
        GenerationOption result = bag.getRandom(dice);

        //then
        assertThat(result, nullValue());
    }

    @Test
    public void getRandom__shouldReturnTheOnlyOptionSet__ifItIsTheOnlyOptionAndDiceReturnsCorrespondingWeight() {
        //given
        WeightedRandomBag<GenerationOption> bag = new WeightedRandomBag<>();
        bag.addEntry(GenerationOption.NOTHING, 50);
        Dice dice = mock(Dice.class);
        when(dice.next(51)).thenReturn(25);

        //when
        GenerationOption result = bag.getRandom(dice);

        //then
        assertThat(result, is(GenerationOption.NOTHING));
    }

    @Test
    public void getRandom__shouldReturnOptionOne__ifTwoOptionsWereSetAndDiceReturnsWeightCorrespondingToOptionOne() {
        //given
        WeightedRandomBag<GenerationOption> bag = new WeightedRandomBag<>();
        bag.addEntry(GenerationOption.NOTHING, 50);
        bag.addEntry(GenerationOption.OBSTACLE_CHAIN, 32);
        Dice dice = mock(Dice.class);
        when(dice.next(84)).thenReturn(1);

        //when
        GenerationOption result = bag.getRandom(dice);

        //then
        assertThat(result, is(GenerationOption.NOTHING));
    }

    @Test
    public void getRandom__shouldReturnOptionTwo__ifTwoOptionsWereSetAndDiceReturnsWeightCorrespondingToOptionTwo() {
        //given
        WeightedRandomBag<GenerationOption> bag = new WeightedRandomBag<>();
        bag.addEntry(GenerationOption.NOTHING, 50);
        bag.addEntry(GenerationOption.OBSTACLE_CHAIN, 27);
        Dice dice = mock(Dice.class);
        when(dice.next(78)).thenReturn(58);

        //when
        GenerationOption result = bag.getRandom(dice);

        //then
        assertThat(result, is(GenerationOption.OBSTACLE_CHAIN));
    }

    @Test
    public void getRandom__shouldReturnOptionOne__ifThreeOptionsWereSetAndDiceReturnsWeightCorrespondingToOptionOne() {
        //given
        WeightedRandomBag<GenerationOption> bag = new WeightedRandomBag<>();
        bag.addEntry(GenerationOption.SPRINGBOARD, 50);
        bag.addEntry(GenerationOption.SINGLE_ELEMENT, 32);
        bag.addEntry(GenerationOption.OBSTACLE_CHAIN, 111);
        Dice dice = mock(Dice.class);
        when(dice.next(194)).thenReturn(1);

        //when
        GenerationOption result = bag.getRandom(dice);

        //then
        assertThat(result, is(GenerationOption.SPRINGBOARD));
    }

    @Test
    public void getRandom__shouldReturnOptionTwo__ifThreeOptionsWereSetAndDiceReturnsWeightCorrespondingToOptionTwo() {
        //given
        WeightedRandomBag<GenerationOption> bag = new WeightedRandomBag<>();
        bag.addEntry(GenerationOption.SPRINGBOARD, 50);
        bag.addEntry(GenerationOption.SINGLE_ELEMENT, 32);
        bag.addEntry(GenerationOption.OBSTACLE_CHAIN, 111);
        Dice dice = mock(Dice.class);
        when(dice.next(194)).thenReturn(75);

        //when
        GenerationOption result = bag.getRandom(dice);

        //then
        assertThat(result, is(GenerationOption.SINGLE_ELEMENT));
    }

    @Test
    public void getRandom__shouldReturnOptionThree__ifThreeOptionsWereSetAndDiceReturnsWeightCorrespondingToOptionThree() {
        //given
        WeightedRandomBag<GenerationOption> bag = new WeightedRandomBag<>();
        bag.addEntry(GenerationOption.SPRINGBOARD, 50);
        bag.addEntry(GenerationOption.SINGLE_ELEMENT, 32);
        bag.addEntry(GenerationOption.OBSTACLE_CHAIN, 111);
        Dice dice = mock(Dice.class);
        when(dice.next(194)).thenReturn(184);

        //when
        GenerationOption result = bag.getRandom(dice);

        //then
        assertThat(result, is(GenerationOption.OBSTACLE_CHAIN));
    }
}
