package com.codenjoy.dojo.excitebike.model.items;

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

import com.codenjoy.dojo.excitebike.model.GameFieldImpl;
import com.codenjoy.dojo.excitebike.model.Player;
import com.codenjoy.dojo.excitebike.model.elements.GameElementType;
import com.codenjoy.dojo.excitebike.services.SettingsHandler;
import com.codenjoy.dojo.excitebike.services.parse.MapParser;
import com.codenjoy.dojo.services.Dice;
import com.codenjoy.dojo.services.EventListener;
import com.codenjoy.dojo.services.PointImpl;
import com.codenjoy.dojo.services.State;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import static java.util.stream.Collectors.toList;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(Parameterized.class)
public class SpawnElementsSystemParameterizedTest {
    private GameElementType gameElementType;
    private Player player;
    private GameFieldImpl game;
    private Dice dice;

    public SpawnElementsSystemParameterizedTest(GameElementType gameElementType) {
        this.gameElementType = gameElementType;
    }

    @Parameterized.Parameters(name = "{0}")
    public static Collection data() {
        return Arrays.stream(GameElementType.values(), 2, GameElementType.values().length).collect(toList());
    }

    @Before
    public void init() {
        dice = mock(Dice.class);
        MapParser mapParser = mock(MapParser.class);
        when(mapParser.getXSize()).thenReturn(5);
        when(mapParser.getYSize()).thenReturn(5);
        game = new GameFieldImpl(mapParser, dice, new SettingsHandler());
        player = new Player(mock(EventListener.class));
        game.newGame(player);
    }

    @Test
    public void shouldGenerateElement() {
        //given
        when(dice.next(20)).thenReturn(12);
        when(dice.next(5)).thenReturn(gameElementType.ordinal() - 2);
        when(dice.next(3)).thenReturn(0);

        //when
        game.tick();

        //then
        PointImpl generatedElement = (PointImpl) ((List) game.reader().elements()).stream().filter(el -> !(el instanceof Bike || el instanceof Fence)).findFirst().get();
        assertThat(((State) generatedElement).state(player), is(gameElementType));

    }
}
