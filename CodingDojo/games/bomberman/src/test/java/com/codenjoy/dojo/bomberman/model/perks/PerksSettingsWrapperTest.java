package com.codenjoy.dojo.bomberman.model.perks;

/*-
 * #%L
 * Codenjoy - it's a dojo-like platform from developers to developers.
 * %%
 * Copyright (C) 2018 - 2020 Codenjoy
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

import com.codenjoy.dojo.bomberman.model.Elements;
import com.codenjoy.dojo.services.Dice;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

@RunWith(MockitoJUnitRunner.class)
public class PerksSettingsWrapperTest {
    private final int percents = 100;
    @Mock
    Dice dice;

    private static final Elements NO_PERK = Elements.DESTROYED_WALL;

    @Test
    public void dropPerk() {
        PerksSettingsWrapper.setDropRatio(20);

        PerksSettingsWrapper.setPerkSettings(Elements.BOMB_BLAST_RADIUS_INCREASE, 0, 0); // range 0..10
        PerksSettingsWrapper.setPerkSettings(Elements.BOMB_COUNT_INCREASE, 0, 0); // range 60..70

        Mockito.when(dice.next(percents)).thenReturn(0);
        Elements perk = PerksSettingsWrapper.nextPerkDrop(dice);
        assertEquals("Range 0..9 = perk", Elements.BOMB_BLAST_RADIUS_INCREASE, perk);

        Mockito.when(dice.next(percents)).thenReturn(10);
        perk = PerksSettingsWrapper.nextPerkDrop(dice);
        assertEquals("Range 10..60 = none", NO_PERK, perk);

        Mockito.when(dice.next(percents)).thenReturn(60);
        perk = PerksSettingsWrapper.nextPerkDrop(dice);
        assertEquals("Range 60..70 = perk", Elements.BOMB_COUNT_INCREASE, perk);

        Mockito.when(dice.next(percents)).thenReturn(69);
        perk = PerksSettingsWrapper.nextPerkDrop(dice);
        assertNotEquals("Range 60..70 (exclusive) = perk", NO_PERK, perk);

        Mockito.when(dice.next(percents)).thenReturn(70);
        perk = PerksSettingsWrapper.nextPerkDrop(dice);
        assertEquals("Range 70...100 = none", NO_PERK, perk);

    }
}
