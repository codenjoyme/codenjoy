package com.codenjoy.dojo.excitebike.model;

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


import com.codenjoy.dojo.excitebike.model.items.bike.Bike;
import com.codenjoy.dojo.excitebike.services.parse.MapParserImpl;
import com.codenjoy.dojo.services.Dice;
import com.codenjoy.dojo.services.EventListener;
import com.codenjoy.dojo.utils.TestUtils;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import static com.codenjoy.dojo.excitebike.TestUtils.parseBikes;
import static com.codenjoy.dojo.excitebike.TestUtils.printField;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class GameFieldImplSystemTest {

    private GameFieldImpl game;
    private Bike bike;
    private Dice dice;
    private Player player;

    @Before
    public void setup() {
        dice = mock(Dice.class);
    }

    private void init(String board) {
        Bike bike = parseBikes(board).get(0);
        game = new GameFieldImpl(new MapParserImpl(board), dice);
        player = new Player(mock(EventListener.class));
        game.newGame(player);
        player.setHero(bike);
        bike.init(game);
        this.bike = game.getBikes().get(0);
    }

    @Test
    public void init__shouldFillFieldCorrectly() {
        //given
        String board = "■■■■■" +
                " B ▼ " +
                "  >  " +
                " ▲ < " +
                "■■■■■";

        //when
        init(board);

        //then
        String expected = "■■■■■" +
                " B ▼ " +
                "  >  " +
                " ▲ < " +
                "■■■■■";
        assertThat(printField(game, player), is(TestUtils.injectN(expected)));
    }

    @Test
    public void tick__shouldShiftTrack() {
        //given
        String board = "■■■■■" +
                " B ▼ " +
                "  >  " +
                " ▲ < " +
                "■■■■■";
        init(board);
        when(dice.next(anyInt())).thenReturn(1, 5 , 1);

        //when
        game.tick();

        //then
        String expected = "■■■■■" +
                " B▼  " +
                " >  <" +
                "▲ <  " +
                "■■■■■";
        assertThat(printField(game, player), is(TestUtils.injectN(expected)));
    }

    @Test
    public void down__shouldMoveBikeToDown() {
        //given
        String board = "■■■■■" +
                " B   " +
                "     " +
                "     " +
                "■■■■■";
        init(board);
        when(dice.next(anyInt())).thenReturn(5);

        //when
        bike.down();
        game.tick();

        //then
        String expected = "■■■■■" +
                "     " +
                " B   " +
                "     " +
                "■■■■■";
        assertThat(printField(game, player), is(TestUtils.injectN(expected)));
    }

    @Test
    public void up__shouldMoveBikeToUp() {
        //given
        String board = "■■■■■" +
                "     " +
                " B   " +
                "     " +
                "■■■■■";
        init(board);
        when(dice.next(anyInt())).thenReturn(5);

        //when
        bike.up();
        game.tick();

        //then
        String expected = "■■■■■" +
                " B   " +
                "     " +
                "     " +
                "■■■■■";
        assertThat(printField(game, player), is(TestUtils.injectN(expected)));
    }

    @Test
    public void crush__shouldFallBike() {
        //given
        String board = "■■■■■" +
                "     " +
                "  B  " +
                "     " +
                "■■■■■";
        init(board);
        when(dice.next(anyInt())).thenReturn(5);

        //when
        bike.crush();
        game.tick();

        //then
        String expected = "■■■■■" +
                "     " +
                "  b  " +
                "     " +
                "■■■■■";
        assertThat(printField(game, player), is(TestUtils.injectN(expected)));
    }

    @Test
    public void tick__shouldIgnoreMovingAfterBikeIsFallen() {
        //given
        String board = "■■■■■" +
                "     " +
                "  B  " +
                "     " +
                "■■■■■";
        init(board);
        when(dice.next(anyInt())).thenReturn(5);
        bike.crush();
        game.tick();

        //when
        bike.up();
        game.tick();

        //then
        String expected = "■■■■■" +
                "     " +
                " b   " +
                "     " +
                "■■■■■";
        assertThat(printField(game, player), is(TestUtils.injectN(expected)));
    }

    @Test
    public void inhibitorTick1__shouldChangeBikeStateToAtInhibitor__atTheBeginning() {
        //given
        String board = "■■■■■" +
                "     " +
                "B<   " +
                "     " +
                "■■■■■";
        init(board);
        when(dice.next(anyInt())).thenReturn(5);

        //when
        game.tick();

        //then
        String expected = "■■■■■" +
                "     " +
                "I    " +
                "     " +
                "■■■■■";
        assertThat(printField(game, player), is(TestUtils.injectN(expected)));
    }

    @Test
    public void inhibitorTick2__shouldChangeBikeStateToNormal__atTheBeginning() {
        //given
        String board = "■■■■■" +
                "     " +
                "B<   " +
                "     " +
                "■■■■■";
        init(board);
        when(dice.next(anyInt())).thenReturn(5);
        game.tick();

        //when
        game.tick();

        //then
        String expected = "■■■■■" +
                "     " +
                "B    " +
                "     " +
                "■■■■■";
        assertThat(printField(game, player), is(TestUtils.injectN(expected)));
    }

    @Test
    public void inhibitor__shouldChangeBikeStateToAtInhibitor__atTheEnding() {
        //given
        String board = "■■■■■" +
                "     " +
                "   B<" +
                "     " +
                "■■■■■";
        init(board);
        when(dice.next(anyInt())).thenReturn(5);

        //when
        game.tick();

        //then
        String expected = "■■■■■" +
                "     " +
                "   I " +
                "     " +
                "■■■■■";
        assertThat(printField(game, player), is(TestUtils.injectN(expected)));
    }

    @Test
    public void inhibitor__shouldChangeBikeStateToAtInhibitor__afterBikeMoveUp() {
        //given
        String board = "■■■■■" +
                "   < " +
                "  B  " +
                "     " +
                "■■■■■";
        init(board);
        when(dice.next(anyInt())).thenReturn(5);

        //when
        bike.up();
        game.tick();

        //then
        String expected = "■■■■■" +
                "  I  " +
                "     " +
                "     " +
                "■■■■■";
        assertThat(printField(game, player), is(TestUtils.injectN(expected)));
    }

    @Test
    public void inhibitorTick1__shouldChangeStateToInhibited() {
        //given
        String board = "■■■■■" +
                "     " +
                "  B< " +
                "     " +
                "■■■■■";
        init(board);
        when(dice.next(anyInt())).thenReturn(5);

        //when
        game.tick();

        //then
        String expected = "■■■■■" +
                "     " +
                "  I  " +
                "     " +
                "■■■■■";
        assertThat(printField(game, player), is(TestUtils.injectN(expected)));
    }

    @Test
    public void inhibitorTick2__shouldMoveInhibitedBikeOneStepBack() {
        //given
        String board = "■■■■■" +
                "     " +
                "  B< " +
                "     " +
                "■■■■■";
        init(board);
        when(dice.next(anyInt())).thenReturn(5);
        game.tick();

        //when
        game.tick();

        //then
        String expected = "■■■■■" +
                "     " +
                " I   " +
                "     " +
                "■■■■■";
        assertThat(printField(game, player), is(TestUtils.injectN(expected)));
    }

    @Test
    public void inhibitorTick3__shouldChangeBikeStateToNormalAndMoveBackInhibitor() {
        //given
        String board = "■■■■■" +
                "     " +
                "  B< " +
                "     " +
                "■■■■■";
        init(board);
        when(dice.next(anyInt())).thenReturn(5);
        game.tick();
        game.tick();

        //when
        game.tick();

        //then
        String expected = "■■■■■" +
                "     " +
                "<B   " +
                "     " +
                "■■■■■";
        assertThat(printField(game, player), is(TestUtils.injectN(expected)));
    }

    @Test
    public void twoInhibitorsTick1__shouldChangeStateToInhibited() {
        //given
        String board = "■■■■■" +
                "     " +
                "  B<<" +
                "     " +
                "■■■■■";
        init(board);
        when(dice.next(anyInt())).thenReturn(5);

        //when
        game.tick();

        //then
        String expected = "■■■■■" +
                "     " +
                "  I< " +
                "     " +
                "■■■■■";
        assertThat(printField(game, player), is(TestUtils.injectN(expected)));
    }

    @Test
    public void twoInhibitorsTick2__shouldChangeKeepBikeStateInhibitedAndMoveItBack() {
        //given
        String board = "■■■■■" +
                "     " +
                "  B<<" +
                "     " +
                "■■■■■";
        init(board);
        when(dice.next(anyInt())).thenReturn(5);
        game.tick();

        //when
        game.tick();

        //then
        String expected = "■■■■■" +
                "     " +
                " I<  " +
                "     " +
                "■■■■■";
        assertThat(printField(game, player), is(TestUtils.injectN(expected)));
    }

    @Test
    public void twoInhibitorsTick3__shouldChangeKeepBikeStateInhibited() {
        //given
        String board = "■■■■■" +
                "     " +
                "  B<<" +
                "     " +
                "■■■■■";
        init(board);
        when(dice.next(anyInt())).thenReturn(5);
        game.tick();
        game.tick();

        //when
        game.tick();

        //then
        String expected = "■■■■■" +
                "     " +
                "<I   " +
                "     " +
                "■■■■■";
        assertThat(printField(game, player), is(TestUtils.injectN(expected)));
    }

    @Test
    public void twoInhibitorsTick4__shouldChangeBikeStateToNormal() {
        //given
        String board = "■■■■■" +
                "     " +
                "  B<<" +
                "     " +
                "■■■■■";
        init(board);
        when(dice.next(anyInt())).thenReturn(5);
        game.tick();
        game.tick();
        game.tick();

        //when
        game.tick();

        //then
        String expected = "■■■■■" +
                "     " +
                "<B   " +
                "     " +
                "■■■■■";
        assertThat(printField(game, player), is(TestUtils.injectN(expected)));
    }

    @Test
    public void accelerator__shouldChangeBikeStateToAtAccelerator__atTheBeginning() {
        //given
        String board = "■■■■■" +
                "     " +
                "B>   " +
                "     " +
                "■■■■■";
        init(board);
        when(dice.next(anyInt())).thenReturn(5);

        //when
        game.tick();

        //then
        String expected = "■■■■■" +
                "     " +
                "A    " +
                "     " +
                "■■■■■";
        assertThat(printField(game, player), is(TestUtils.injectN(expected)));
    }

    @Test
    public void accelerator__shouldChangeBikeStateToAtAccelerator__atTheEnding() {
        //given
        String board = "■■■■■" +
                "     " +
                "   B>" +
                "     " +
                "■■■■■";
        init(board);
        when(dice.next(anyInt())).thenReturn(5);

        //when
        game.tick();

        //then
        String expected = "■■■■■" +
                "     " +
                "   A " +
                "     " +
                "■■■■■";
        assertThat(printField(game, player), is(TestUtils.injectN(expected)));
    }

    @Test
    public void accelerator__shouldChangeBikeStateToAtAccelerator__afterBikeMoveUp() {
        //given
        String board = "■■■■■" +
                "   > " +
                "  B  " +
                "     " +
                "■■■■■";
        init(board);
        when(dice.next(anyInt())).thenReturn(5);

        //when
        bike.up();
        game.tick();

        //then
        String expected = "■■■■■" +
                "  A  " +
                "     " +
                "     " +
                "■■■■■";
        assertThat(printField(game, player), is(TestUtils.injectN(expected)));
    }

    @Test
    public void acceleratorTick1__shouldChangeStateToAccelerated() {
        //given
        String board = "■■■■■" +
                "     " +
                "  B> " +
                "     " +
                "■■■■■";
        init(board);
        when(dice.next(anyInt())).thenReturn(5);

        //when
        game.tick();

        //then
        String expected = "■■■■■" +
                "     " +
                "  A  " +
                "     " +
                "■■■■■";
        assertThat(printField(game, player), is(TestUtils.injectN(expected)));
    }

    @Test
    public void acceleratorTick2__shouldMoveAcceleratedBikeOneStepForwardAndChangeStateToNormalAndMoveAcceleratorBack() {
        //given
        String board = "■■■■■" +
                "     " +
                "  B> " +
                "     " +
                "■■■■■";
        init(board);
        when(dice.next(anyInt())).thenReturn(5);
        game.tick();

        //when
        game.tick();

        //then
        String expected = "■■■■■" +
                "     " +
                " > B " +
                "     " +
                "■■■■■";
        assertThat(printField(game, player), is(TestUtils.injectN(expected)));
    }

    @Test
    public void acceleratorTick3__shouldMoveBackAccelerator() {
        //given
        String board = "■■■■■" +
                "     " +
                "  B> " +
                "     " +
                "■■■■■";
        init(board);
        when(dice.next(anyInt())).thenReturn(5);
        game.tick();
        game.tick();

        //when
        game.tick();

        //then
        String expected = "■■■■■" +
                "     " +
                ">  B " +
                "     " +
                "■■■■■";
        assertThat(printField(game, player), is(TestUtils.injectN(expected)));
    }

    @Test
    public void twoAcceleratorsTick1__shouldChangeBikeStateToAccelerated() {
        //given
        String board = "■■■■■" +
                "     " +
                "B>>  " +
                "     " +
                "■■■■■";
        init(board);
        when(dice.next(anyInt())).thenReturn(5);

        //when
        game.tick();

        //then
        String expected = "■■■■■" +
                "     " +
                "A>   " +
                "     " +
                "■■■■■";
        assertThat(printField(game, player), is(TestUtils.injectN(expected)));
    }

    @Test
    public void twoAcceleratorsTick2__shouldMoveBikeForwardAndChangeStateToNormal() {
        //given
        String board = "■■■■■" +
                "     " +
                "B>>  " +
                "     " +
                "■■■■■";
        init(board);
        when(dice.next(anyInt())).thenReturn(5);
        game.tick();

        //when
        game.tick();

        //then
        String expected = "■■■■■" +
                "     " +
                ">B   " +
                "     " +
                "■■■■■";
        assertThat(printField(game, player), is(TestUtils.injectN(expected)));
    }

    @Test
    public void twoAcceleratorsTick3__shouldChangeKeepBikeStateNormalButMoveItForward() {
        //given
        String board = "■■■■■" +
                "     " +
                "B>>  " +
                "     " +
                "■■■■■";
        init(board);
        when(dice.next(anyInt())).thenReturn(5);
        game.tick();
        game.tick();

        //when
        game.tick();

        //then
        String expected = "■■■■■" +
                "     " +
                "  B  " +
                "     " +
                "■■■■■";
        assertThat(printField(game, player), is(TestUtils.injectN(expected)));
    }

    @Test
    public void obstacleTick1__shouldObstructBike() {
        //given
        String board = "■■■■■" +
                "     " +
                "   B|" +
                "     " +
                "■■■■■";
        init(board);
        when(dice.next(anyInt())).thenReturn(5);

        //when
        game.tick();

        //then
        String expected = "■■■■■" +
                "     " +
                "   o " +
                "     " +
                "■■■■■";
        assertThat(printField(game, player), is(TestUtils.injectN(expected)));
    }

    @Test
    public void obstacleTick2__shouldMoveObstructedBikeLeft() {
        //given
        String board = "■■■■■" +
                "     " +
                "   B|" +
                "     " +
                "■■■■■";
        init(board);
        when(dice.next(anyInt())).thenReturn(5);
        game.tick();

        //when
        game.tick();

        //then
        String expected = "■■■■■" +
                "     " +
                "  o  " +
                "     " +
                "■■■■■";
        assertThat(printField(game, player), is(TestUtils.injectN(expected)));
    }

    @Test
    public void obstacleTick3__shouldMoveObstructedBikeLeft() {
        //given
        String board = "■■■■■" +
                "     " +
                "   B|" +
                "     " +
                "■■■■■";
        init(board);
        when(dice.next(anyInt())).thenReturn(5);
        game.tick();
        game.tick();

        //when
        game.tick();

        //then
        String expected = "■■■■■" +
                "     " +
                " o   " +
                "     " +
                "■■■■■";
        assertThat(printField(game, player), is(TestUtils.injectN(expected)));
    }

    @Test
    public void obstacleTick4__shouldMoveObstructedBikeLeft() {
        //given
        String board = "■■■■■" +
                "     " +
                "   B|" +
                "     " +
                "■■■■■";
        init(board);
        when(dice.next(anyInt())).thenReturn(5);
        game.tick();
        game.tick();
        game.tick();

        //when
        game.tick();

        //then
        String expected = "■■■■■" +
                "     " +
                "o    " +
                "     " +
                "■■■■■";
        assertThat(printField(game, player), is(TestUtils.injectN(expected)));
    }

    @Test
    public void obstacleTick5__shouldRemoveObstructedBike__ifItIsLastCell() {
        //given
        String board = "■■■■■" +
                "     " +
                "   B|" +
                "     " +
                "■■■■■";
        init(board);
        when(dice.next(anyInt())).thenReturn(5);
        game.tick();
        game.tick();
        game.tick();
        game.tick();

        //when
        game.tick();

        //then
        String expected = "■■■■■" +
                "     " +
                "     " +
                "     " +
                "■■■■■";
        assertThat(printField(game, player), is(TestUtils.injectN(expected)));
    }

    @Test
    public void obstacleAndUpCommand__shouldMoveBikeUp() {
        //given
        String board = "■■■■■" +
                "     " +
                "   B|" +
                "     " +
                "■■■■■";
        init(board);
        when(dice.next(anyInt())).thenReturn(5);
        bike.up();

        //when
        game.tick();

        //then
        String expected = "■■■■■" +
                "   B " +
                "   | " +
                "     " +
                "■■■■■";
        assertThat(printField(game, player), is(TestUtils.injectN(expected)));
    }

    @Test
    public void tick2__shouldMoveObstructedBikeAndObstacleTogether() {
        //given
        String board = "■■■■■" +
                "     " +
                "   B|" +
                "     " +
                "■■■■■";
        init(board);
        when(dice.next(anyInt())).thenReturn(5);
        game.tick();

        //when
        game.tick();

        //then
        String expected = "■■■■■" +
                "     " +
                "  o  " +
                "     " +
                "■■■■■";
        assertThat(printField(game, player), is(TestUtils.injectN(expected)));
    }

    @Test
    public void tick1__shouldMakeBikeObstructed__IfBikeInteractedWithObstacle() {
        //given
        String board = "■■■■■" +
                "     " +
                "B|   " +
                "     " +
                "■■■■■";
        init(board);
        when(dice.next(anyInt())).thenReturn(5);

        //when
        game.tick();

        //then
        String expected = "■■■■■" +
                "     " +
                "o    " +
                "     " +
                "■■■■■";
        assertThat(printField(game, player), is(TestUtils.injectN(expected)));
    }

    @Test
    public void tick2__shouldMoveOutObstructedBikeWithObstacle__ifItIsFirstColumn() {
        //given
        String board = "■■■■■" +
                "     " +
                "B|   " +
                "     " +
                "■■■■■";
        init(board);
        when(dice.next(anyInt())).thenReturn(5);
        game.tick();

        //when
        game.tick();

        //then
        String expected = "■■■■■" +
                "     " +
                "     " +
                "     " +
                "■■■■■";
        assertThat(printField(game, player), is(TestUtils.injectN(expected)));
    }

    @Test
    public void acceleratorAndInhibitorTick1__shouldChangeBikeStateToAccelerated() {
        //given
        String board = "■■■■■" +
                "     " +
                "  B><" +
                "     " +
                "■■■■■";
        init(board);
        when(dice.next(anyInt())).thenReturn(5);

        //when
        game.tick();

        //then
        String expected = "■■■■■" +
                "     " +
                "  A< " +
                "     " +
                "■■■■■";
        assertThat(printField(game, player), is(TestUtils.injectN(expected)));
    }

    @Test
    public void acceleratorAndInhibitorTick2__shouldChangeBikeStateToInhibited() {
        //given
        String board = "■■■■■" +
                "     " +
                "  B><" +
                "     " +
                "■■■■■";
        init(board);
        when(dice.next(anyInt())).thenReturn(5);
        game.tick();

        //when
        game.tick();

        //then
        String expected = "■■■■■" +
                "     " +
                " >I  " +
                "     " +
                "■■■■■";
        assertThat(printField(game, player), is(TestUtils.injectN(expected)));
    }

    @Test
    public void acceleratorAndInhibitorTick3__shouldChangeBikeStateToNormal() {
        //given
        String board = "■■■■■" +
                "     " +
                "  B><" +
                "     " +
                "■■■■■";
        init(board);
        when(dice.next(anyInt())).thenReturn(5);
        game.tick();
        game.tick();

        //when
        game.tick();

        //then
        String expected = "■■■■■" +
                "     " +
                "><B  " +
                "     " +
                "■■■■■";
        assertThat(printField(game, player), is(TestUtils.injectN(expected)));
    }

    @Test
    public void acceleratorAndObstacleTick1__shouldChangeBikeStateToAccelerated() {
        //given
        String board = "■■■■■" +
                "     " +
                "  B>|" +
                "     " +
                "■■■■■";
        init(board);
        when(dice.next(anyInt())).thenReturn(5);

        //when
        game.tick();

        //then
        String expected = "■■■■■" +
                "     " +
                "  A| " +
                "     " +
                "■■■■■";
        assertThat(printField(game, player), is(TestUtils.injectN(expected)));
    }

    @Test
    public void acceleratorAndObstacleTick2__shouldChangeBikeStateToFallen() {
        //given
        String board = "■■■■■" +
                "     " +
                "  B>|" +
                "     " +
                "■■■■■";
        init(board);
        when(dice.next(anyInt())).thenReturn(5);
        game.tick();

        //when
        game.tick();

        //then
        String expected = "■■■■■" +
                "     " +
                " >o  " +
                "     " +
                "■■■■■";
        assertThat(printField(game, player), is(TestUtils.injectN(expected)));
    }

    @Test
    public void acceleratorAndLineChangerUpTick1__shouldChangeBikeStateToAccelerated() {
        //given
        String board = "■■■■■" +
                "     " +
                "  B>▲" +
                "     " +
                "■■■■■";
        init(board);
        when(dice.next(anyInt())).thenReturn(5);

        //when
        game.tick();

        //then
        String expected = "■■■■■" +
                "     " +
                "  A▲ " +
                "     " +
                "■■■■■";
        assertThat(printField(game, player), is(TestUtils.injectN(expected)));
    }

    @Test
    public void acceleratorAndLineChangerUpTick2__shouldMoveBikeForwardAndUp() {
        //given
        String board = "■■■■■" +
                "     " +
                "  B>▲" +
                "     " +
                "■■■■■";
        init(board);
        when(dice.next(anyInt())).thenReturn(5);
        game.tick();

        //when
        game.tick();

        //then
        String expected = "■■■■■" +
                "   B " +
                " >▲  " +
                "     " +
                "■■■■■";
        assertThat(printField(game, player), is(TestUtils.injectN(expected)));
    }

    @Test
    public void acceleratorAndLineChangerDownTick1__shouldChangeBikeStateToAccelerated() {
        //given
        String board = "■■■■■" +
                "     " +
                "  B>▼" +
                "     " +
                "■■■■■";
        init(board);
        when(dice.next(anyInt())).thenReturn(5);

        //when
        game.tick();

        //then
        String expected = "■■■■■" +
                "     " +
                "  A▼ " +
                "     " +
                "■■■■■";
        assertThat(printField(game, player), is(TestUtils.injectN(expected)));
    }

    @Test
    public void acceleratorAndLineChangerDownTick2__shouldMoveBikeForwardAndDown() {
        //given
        String board = "■■■■■" +
                "     " +
                "  B>▼" +
                "     " +
                "■■■■■";
        init(board);
        when(dice.next(anyInt())).thenReturn(5);
        game.tick();

        //when
        game.tick();

        //then
        String expected = "■■■■■" +
                "     " +
                " >▼  " +
                "   B " +
                "■■■■■";
        assertThat(printField(game, player), is(TestUtils.injectN(expected)));
    }

    @Test
    public void lineChangerUpTick1__shouldChangeBikeStateToAtLineChangerUp() {
        //given
        String board = "■■■■■" +
                "     " +
                "  B▲ " +
                "     " +
                "■■■■■";
        init(board);
        when(dice.next(anyInt())).thenReturn(5);

        //when
        game.tick();

        //then
        String expected = "■■■■■" +
                "     " +
                "  U  " +
                "     " +
                "■■■■■";
        assertThat(printField(game, player), is(TestUtils.injectN(expected)));
    }

    @Test
    public void lineChangerUpTick2__shouldMoveBikeUpAndChangeStateBackToNormal() {
        //given
        String board = "■■■■■" +
                "     " +
                "  B▲ " +
                "     " +
                "■■■■■";
        init(board);
        when(dice.next(anyInt())).thenReturn(5);
        game.tick();

        //when
        game.tick();

        //then
        String expected = "■■■■■" +
                "  B  " +
                " ▲   " +
                "     " +
                "■■■■■";
        assertThat(printField(game, player), is(TestUtils.injectN(expected)));
    }

    @Test
    public void lineChangerDownTick1__shouldChangeBikeStateToAtLineChangerDown() {
        //given
        String board = "■■■■■" +
                "     " +
                "  B▼ " +
                "     " +
                "■■■■■";
        init(board);
        when(dice.next(anyInt())).thenReturn(5);

        //when
        game.tick();

        //then
        String expected = "■■■■■" +
                "     " +
                "  D  " +
                "     " +
                "■■■■■";
        assertThat(printField(game, player), is(TestUtils.injectN(expected)));
    }

    @Test
    public void lineChangerDownTick2__shouldMoveBikeDownAndChangeStateBackToNormal() {
        //given
        String board = "■■■■■" +
                "     " +
                "  B▼ " +
                "     " +
                "■■■■■";
        init(board);
        when(dice.next(anyInt())).thenReturn(5);
        game.tick();

        //when
        game.tick();

        //then
        String expected = "■■■■■" +
                "     " +
                " ▼   " +
                "  B  " +
                "■■■■■";
        assertThat(printField(game, player), is(TestUtils.injectN(expected)));
    }

    @Test
    public void lineChangerUpAndCommandUpTick1__shouldMoveBikeUpAndKeepStateNormal() {
        //given
        String board = "■■■■■" +
                "     " +
                "  B▲ " +
                "     " +
                "■■■■■";
        init(board);
        when(dice.next(anyInt())).thenReturn(5);

        //when
        bike.up();
        game.tick();

        //then
        String expected = "■■■■■" +
                "  B  " +
                "  ▲  " +
                "     " +
                "■■■■■";
        assertThat(printField(game, player), is(TestUtils.injectN(expected)));
    }

    @Test
    public void lineChangerUpAndCommandUpTick2__shouldNotMoveBikeAndKeepState() {
        //given
        String board = "■■■■■" +
                "     " +
                "  B▲ " +
                "     " +
                "■■■■■";
        init(board);
        when(dice.next(anyInt())).thenReturn(5);
        bike.up();
        game.tick();

        //when
        game.tick();

        //then
        String expected = "■■■■■" +
                "  B  " +
                " ▲   " +
                "     " +
                "■■■■■";
        assertThat(printField(game, player), is(TestUtils.injectN(expected)));
    }

    @Test
    public void lineChangerUpTick2AndCommandDown__shouldNotMoveBikeAndChangeStateBackToNormal() {
        //given
        String board = "■■■■■" +
                "     " +
                "  B▲ " +
                "     " +
                "■■■■■";
        init(board);
        when(dice.next(anyInt())).thenReturn(5);
        game.tick();

        //when
        bike.down();
        game.tick();

        //then
        String expected = "■■■■■" +
                "     " +
                " ▲B  " +
                "     " +
                "■■■■■";
        assertThat(printField(game, player), is(TestUtils.injectN(expected)));
    }

    @Test
    public void lineChangerDownAndCommandDownTick1__shouldMoveBikeDownAndKeepStateNormal() {
        //given
        String board = "■■■■■" +
                "     " +
                "  B▼ " +
                "     " +
                "■■■■■";
        init(board);
        when(dice.next(anyInt())).thenReturn(5);

        //when
        bike.down();
        game.tick();

        //then
        String expected = "■■■■■" +
                "     " +
                "  ▼  " +
                "  B  " +
                "■■■■■";
        assertThat(printField(game, player), is(TestUtils.injectN(expected)));
    }

    @Test
    public void lineChangerDownAndCommandDownTick2__shouldNotMoveBikeAndChangeState() {
        //given
        String board = "■■■■■" +
                "     " +
                "  B▼ " +
                "     " +
                "■■■■■";
        init(board);
        when(dice.next(anyInt())).thenReturn(5);
        bike.down();
        game.tick();

        //when
        game.tick();

        //then
        String expected = "■■■■■" +
                "     " +
                " ▼   " +
                "  B  " +
                "■■■■■";
        assertThat(printField(game, player), is(TestUtils.injectN(expected)));
    }

    @Test
    public void lineChangerDownTick2AndCommandUp__shouldNotMoveBikeAndChangeStateBackToNormal() {
        //given
        String board = "■■■■■" +
                "     " +
                "  B▼ " +
                "     " +
                "■■■■■";
        init(board);
        when(dice.next(anyInt())).thenReturn(5);
        game.tick();

        //when
        bike.up();
        game.tick();

        //then
        String expected = "■■■■■" +
                "     " +
                " ▼B  " +
                "     " +
                "■■■■■";
        assertThat(printField(game, player), is(TestUtils.injectN(expected)));
    }

    @Test
    public void inhibitorAfterInhibitorTick1__shouldChangeStateToInhibited() {
        //given
        String board = "■■■■■■■■" +
                "        " +
                "   B<  <" +
                "        " +
                "        " +
                "        " +
                "        " +
                "■■■■■■■■";
        init(board);
        when(dice.next(anyInt())).thenReturn(5);

        //when
        game.tick();

        //then
        String expected = "■■■■■■■■" +
                "        " +
                "   I  < " +
                "        " +
                "        " +
                "        " +
                "        " +
                "■■■■■■■■";
        assertThat(printField(game, player), is(TestUtils.injectN(expected)));
    }

    @Test
    public void inhibitorAfterInhibitorTick2__shouldMoveBikeBackAndKeepStateInhibited() {
        //given
        String board = "■■■■■■■■" +
                "        " +
                "   B<  <" +
                "        " +
                "        " +
                "        " +
                "        " +
                "■■■■■■■■";
        init(board);
        when(dice.next(anyInt())).thenReturn(5);
        game.tick();

        //when
        game.tick();

        //then
        String expected = "■■■■■■■■" +
                "        " +
                "  I  <  " +
                "        " +
                "        " +
                "        " +
                "        " +
                "■■■■■■■■";
        assertThat(printField(game, player), is(TestUtils.injectN(expected)));
    }

    @Test
    public void inhibitorAfterInhibitorTick3__shouldChangeStateToNormal() {
        //given
        String board = "■■■■■■■■" +
                "        " +
                "   B<  <" +
                "        " +
                "        " +
                "        " +
                "        " +
                "■■■■■■■■";
        init(board);
        when(dice.next(anyInt())).thenReturn(5);
        game.tick();
        game.tick();

        //when
        game.tick();

        //then
        String expected = "■■■■■■■■" +
                "        " +
                " <B <   " +
                "        " +
                "        " +
                "        " +
                "        " +
                "■■■■■■■■";
        assertThat(printField(game, player), is(TestUtils.injectN(expected)));
    }

    @Test
    public void inhibitorAfterInhibitorTick4__shouldKeepStateNormal() {
        //given
        String board = "■■■■■■■■" +
                "        " +
                "   B<  <" +
                "        " +
                "        " +
                "        " +
                "        " +
                "■■■■■■■■";
        init(board);
        when(dice.next(anyInt())).thenReturn(5);
        game.tick();
        game.tick();
        game.tick();

        //when
        game.tick();

        //then
        String expected = "■■■■■■■■" +
                "        " +
                "< B<    " +
                "        " +
                "        " +
                "        " +
                "        " +
                "■■■■■■■■";
        assertThat(printField(game, player), is(TestUtils.injectN(expected)));
    }

    @Test
    public void inhibitorAfterInhibitorTick5__shouldChangeStateToInhibited() {
        //given
        String board = "■■■■■■■■" +
                "        " +
                "   B<  <" +
                "        " +
                "        " +
                "        " +
                "        " +
                "■■■■■■■■";
        init(board);
        when(dice.next(anyInt())).thenReturn(5);
        game.tick();
        game.tick();
        game.tick();
        game.tick();

        //when
        game.tick();

        //then
        String expected = "■■■■■■■■" +
                "        " +
                "  I     " +
                "        " +
                "        " +
                "        " +
                "        " +
                "■■■■■■■■";
        assertThat(printField(game, player), is(TestUtils.injectN(expected)));
    }

    @Test
    public void inhibitorAfterInhibitorTick6__shouldMoveInhibitedBikeBack() {
        //given
        String board = "■■■■■■■■" +
                "        " +
                "   B<  <" +
                "        " +
                "        " +
                "        " +
                "        " +
                "■■■■■■■■";
        init(board);
        when(dice.next(anyInt())).thenReturn(5);
        game.tick();
        game.tick();
        game.tick();
        game.tick();
        game.tick();

        //when
        game.tick();

        //then
        String expected = "■■■■■■■■" +
                "        " +
                " I      " +
                "        " +
                "        " +
                "        " +
                "        " +
                "■■■■■■■■";
        assertThat(printField(game, player), is(TestUtils.injectN(expected)));
    }

    @Test
    public void inhibitorAfterInhibitorTick7__shouldChangeStateToNormal() {
        //given
        String board = "■■■■■■■■" +
                "        " +
                "   B<  <" +
                "        " +
                "        " +
                "        " +
                "        " +
                "■■■■■■■■";
        init(board);
        when(dice.next(anyInt())).thenReturn(5);
        game.tick();
        game.tick();
        game.tick();
        game.tick();
        game.tick();
        game.tick();

        //when
        game.tick();

        //then
        String expected = "■■■■■■■■" +
                "        " +
                "<B      " +
                "        " +
                "        " +
                "        " +
                "        " +
                "■■■■■■■■";
        assertThat(printField(game, player), is(TestUtils.injectN(expected)));
    }

    @Test
    public void twoLineChangersUpTick1__shouldChangeBikeStateToAtLineChangerUp() {
        //given
        String board = "■■■■■" +
                "     " +
                "  B▲▲" +
                "     " +
                "■■■■■";
        init(board);
        when(dice.next(anyInt())).thenReturn(5);

        //when
        game.tick();

        //then
        String expected = "■■■■■" +
                "     " +
                "  U▲ " +
                "     " +
                "■■■■■";
        assertThat(printField(game, player), is(TestUtils.injectN(expected)));
    }

    @Test
    public void twoLineChangersUpTick2__shouldMoveBikeUpAndChangeStateToNormal() {
        //given
        String board = "■■■■■" +
                "     " +
                "  B▲▲" +
                "     " +
                "■■■■■";
        init(board);
        when(dice.next(anyInt())).thenReturn(5);
        game.tick();

        //when
        game.tick();

        //then
        String expected = "■■■■■" +
                "  B  " +
                " ▲▲  " +
                "     " +
                "■■■■■";
        assertThat(printField(game, player), is(TestUtils.injectN(expected)));
    }

    @Test
    public void lineChangerUpAndDownTick1__shouldChangeBikeStateToAtLineChangerUp() {
        //given
        String board = "■■■■■" +
                "     " +
                "  B▲▼" +
                "     " +
                "■■■■■";
        init(board);
        when(dice.next(anyInt())).thenReturn(5);

        //when
        game.tick();

        //then
        String expected = "■■■■■" +
                "     " +
                "  U▼ " +
                "     " +
                "■■■■■";
        assertThat(printField(game, player), is(TestUtils.injectN(expected)));
    }

    @Test
    public void lineChangerUpAndDownTick2__shouldMoveBikeUpAndChangeStateToNormal() {
        //given
        String board = "■■■■■" +
                "     " +
                "  B▲▼" +
                "     " +
                "■■■■■";
        init(board);
        when(dice.next(anyInt())).thenReturn(5);
        game.tick();

        //when
        game.tick();

        //then
        String expected = "■■■■■" +
                "  B  " +
                " ▲▼  " +
                "     " +
                "■■■■■";
        assertThat(printField(game, player), is(TestUtils.injectN(expected)));
    }

    @Test
    public void commandUpToAcceleratorTick1__shouldMoveBikeUpAndChangeStateToAccelerated() {
        //given
        String board = "■■■■■" +
                "   > " +
                "  B| " +
                "     " +
                "■■■■■";
        init(board);
        when(dice.next(anyInt())).thenReturn(5);

        //when
        bike.up();
        game.tick();

        //then
        String expected = "■■■■■" +
                "  A  " +
                "  |  " +
                "     " +
                "■■■■■";
        assertThat(printField(game, player), is(TestUtils.injectN(expected)));
    }

    @Test
    public void acceleratorWithSpaceAndObstacleTick1__shouldChangeBikeStateToAccelerated() {
        //given
        String board = "■■■■■" +
                "     " +
                " B> |" +
                "     " +
                "■■■■■";
        init(board);
        when(dice.next(anyInt())).thenReturn(5);

        //when
        game.tick();

        //then
        String expected = "■■■■■" +
                "     " +
                " A | " +
                "     " +
                "■■■■■";
        assertThat(printField(game, player), is(TestUtils.injectN(expected)));
    }

    @Test
    public void acceleratorWithSpaceAndObstacleTick2__shouldMoveBikeForwardAndChangeStateToObstructed() {
        //given
        String board = "■■■■■" +
                "     " +
                " B> |" +
                "     " +
                "■■■■■";
        init(board);
        when(dice.next(anyInt())).thenReturn(5);
        game.tick();

        //when
        game.tick();

        //then
        String expected = "■■■■■" +
                "     " +
                "> o  " +
                "     " +
                "■■■■■";
        assertThat(printField(game, player), is(TestUtils.injectN(expected)));
    }

    @Test
    public void shouldSpawnAcceleratorOnTheHighestLine() {
        //given
        String board = "■■■■■" +
                "     " +
                "     " +
                "B    " +
                "■■■■■";
        init(board);
        when(dice.next(anyInt())).thenReturn(0,5, 0, 2);

        //when
        game.tick();

        //then
        String expected = "■■■■■" +
                "    >" +
                "     " +
                "B    " +
                "■■■■■";
        assertThat(printField(game, player), is(TestUtils.injectN(expected)));
    }

    @Test
    public void left__shouldDoNothing() {
        //given
        String board = "■■■■■" +
                "     " +
                "  B  " +
                "     " +
                "■■■■■";
        init(board);
        when(dice.next(anyInt())).thenReturn(5);

        //when
        bike.left();
        game.tick();

        //then
        String expected = "■■■■■" +
                "     " +
                "  B  " +
                "     " +
                "■■■■■";
        assertThat(printField(game, player), is(TestUtils.injectN(expected)));
    }

    @Test
    public void right__shouldDoNothing() {
        //given
        String board = "■■■■■" +
                "     " +
                "  B  " +
                "     " +
                "■■■■■";
        init(board);
        when(dice.next(anyInt())).thenReturn(5);

        //when
        bike.right();
        game.tick();

        //then
        String expected = "■■■■■" +
                "     " +
                "  B  " +
                "     " +
                "■■■■■";
        assertThat(printField(game, player), is(TestUtils.injectN(expected)));
    }

    @Test
    public void act__shouldDoNothing() {
        //given
        String board = "■■■■■" +
                "     " +
                "  B  " +
                "     " +
                "■■■■■";
        init(board);
        when(dice.next(anyInt())).thenReturn(5);

        //when
        bike.act();
        game.tick();

        //then
        String expected = "■■■■■" +
                "     " +
                "  B  " +
                "     " +
                "■■■■■";
        assertThat(printField(game, player), is(TestUtils.injectN(expected)));
    }

    @Test
    public void tick__shouldMoveBikeUp_ifBikeCrossRiseOfSpringboard() {
        //given
        String board = "■■■╔^" +
                "   /^" +
                "   /^" +
                "  B╚/" +
                "■■■■■";
        init(board);
        when(dice.next(anyInt())).thenReturn(5);
        game.tick();

        //when
        game.tick();

        //then
        String expected = "■╔^  "+
                " /^  "+
                " /B  "+
                " ╚/  "+
                "■■■■■";
        assertThat(printField(game, player), is(TestUtils.injectN(expected)));
    }

    @Test
    public void tick__shouldMoveBikeUp_ifBikeCrossRiseOfSpringboard2() {
        //given
        String board = "■■■╔^" +
                "  B/^" +
                "   /^" +
                "   ╚/" +
                "■■■■■";
        init(board);
        when(dice.next(anyInt())).thenReturn(5);
        game.tick();

        //when
        game.tick();

        //then
        String expected = "■╔B  "+
                " /^  "+
                " /^  "+
                " ╚/  "+
                "■■■■■";
        assertThat(printField(game, player), is(TestUtils.injectN(expected)));
    }

    @Test
    public void tick__shouldMoveBikeUp_ifBikeCrossRiseOfSpringboard3() {
        //given
        String board = "■■■╔^" +
                "   /^" +
                "  B/^" +
                "   ╚/" +
                "■■■■■";
        init(board);
        when(dice.next(anyInt())).thenReturn(5);
        game.tick();

        //when
        game.tick();

        //then
        String expected = "■╔^  "+
                " /B  "+
                " /^  "+
                " ╚/  "+
                "■■■■■";
        assertThat(printField(game, player), is(TestUtils.injectN(expected)));
    }

    @Test
    public void tick__shouldSetStateBikeInFlight() {
        //given
        String board = "■■■╔^" +
                "  /^ " +
                "  /B " +
                "  ╚//" +
                "■■■■■";
        init(board);
        when(dice.next(anyInt())).thenReturn(5);

        //when
        bike.down();
        game.tick();

        //then
        String expected = "■■╔^ "+
                " /^  "+
                " /   "+
                " ╚/& "+
                "■■■■■";
        assertThat(printField(game, player), is(TestUtils.injectN(expected)));
    }

    @Test
    public void tick__shouldCrushBikeAtBorder_ifBikeWasInFlight() {
        //given
        String board = "■■■╔^" +
                "  /^ " +
                "  /B " +
                "  ╚//" +
                "■■■■■";
        init(board);
        when(dice.next(anyInt())).thenReturn(5);
        bike.down();
        game.tick();

        //when
        game.tick();

        //then
        String expected = "■╔^  "+
                "/^   "+
                "/    "+
                "╚//  "+
                "■■■c■";
        assertThat(printField(game, player), is(TestUtils.injectN(expected)));
    }

    @Test
    public void tick__shouldCrushBike_ifBikeMoveUpOnSpringboardBorder() {
        //given
        String board = "■■■╔^" +
                "  B/^" +
                "   /^" +
                "   ╚/" +
                "■■■■■";
        init(board);
        when(dice.next(anyInt())).thenReturn(5);
        game.tick();
        game.tick();
        bike.up();

        //when
        game.tick();

        //then
        String expected = "╔^■  "+
                "/^   "+
                "/^   "+
                "╚/   "+
                "■■■■■";
        assertThat(printField(game, player), is(TestUtils.injectN(expected)));
        assertThat(bike.isAlive(), is(false));
    }

    @Ignore
    @Test
    public void tick__shouldMoveBikeDown_ifBikeCrossDescentOfSpringboard() {
        //given
        String board = "■■■^╗" +
                "   ^\\" +
                "    B\\" +
                "   \\╝" +
                "■■■■■";
        init(board);
        when(dice.next(anyInt())).thenReturn(5);
        game.tick();

        //when
        game.tick();

        //then
        String expected = "■^╗  " +
                " ^\\ " +
                "  ^\\ " +
                " \\╝B" +
                "■■■■■";
        assertThat(printField(game, player), is(TestUtils.injectN(expected)));
    }

    @Ignore
    @Test
    public void tick__shouldMoveBikeDown_ifBikeCrossDescentOfSpringboard2() {
        //given
        String board = "■■■╔^" +
                "  B/^" +
                "   /^" +
                "   ╚/" +
                "■■■■■";
        init(board);
        when(dice.next(anyInt())).thenReturn(5);
        game.tick();

        //when
        game.tick();

        //then
        String expected = "■╔B  "+
                " /^  "+
                " /^  "+
                " ╚/  "+
                "■■■■■";
        assertThat(printField(game, player), is(TestUtils.injectN(expected)));
    }

    @Ignore
    @Test
    public void tick__shouldMoveBikeDown_ifBikeCrossDescentOfSpringboard3() {
        //given
        String board = "■■■╔^" +
                "   /^" +
                "  B/^" +
                "   ╚/" +
                "■■■■■";
        init(board);
        when(dice.next(anyInt())).thenReturn(5);
        game.tick();

        //when
        game.tick();

        //then
        String expected = "■╔^  "+
                " /B  "+
                " /^  "+
                " ╚/  "+
                "■■■■■";
        assertThat(printField(game, player), is(TestUtils.injectN(expected)));
    }

}
