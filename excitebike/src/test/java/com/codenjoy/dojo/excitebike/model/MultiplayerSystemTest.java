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


import com.codenjoy.dojo.excitebike.model.items.Bike;
import com.codenjoy.dojo.excitebike.services.SettingsHandler;
import com.codenjoy.dojo.excitebike.services.parse.MapParser;
import com.codenjoy.dojo.excitebike.services.parse.MapParserImpl;
import com.codenjoy.dojo.services.Dice;
import com.codenjoy.dojo.services.EventListener;
import com.codenjoy.dojo.services.Game;
import com.codenjoy.dojo.services.multiplayer.Single;
import com.codenjoy.dojo.services.printer.PrinterFactory;
import com.codenjoy.dojo.services.printer.PrinterFactoryImpl;
import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class MultiplayerSystemTest {

    private Game game1;
    private Game game2;
    private Game game3;
    private Dice dice;
    private GameFieldImpl field;

    private void init() {
        MapParser mapParser = new MapParserImpl("■■■■■■■" +
                "       " +
                "       " +
                "       " +
                "       " +
                "       " +
                "■■■■■■■");

        dice = mock(Dice.class);
        field = new GameFieldImpl(mapParser, dice, new SettingsHandler());
        PrinterFactory factory = new PrinterFactoryImpl();

        game1 = new Single(new Player(mock(EventListener.class)), factory);
        game1.on(field);

        game2 = new Single(new Player(mock(EventListener.class)), factory);
        game2.on(field);

        game3 = new Single(new Player(mock(EventListener.class)), factory);
        game3.on(field);

        game1.newGame();
        game2.newGame();
        game3.newGame();
    }

    @Test
    public void games__shouldInitializeCorrectly() {
        //given

        //when
        init();

        //then
        assertThat(game1.getBoardAsString(), is("■■■■■■■\n" +
                "       \n" +
                "       \n" +
                "Ḃ      \n" +
                " Ḃ     \n" +
                "B      \n" +
                "■■■■■■■\n"));
        assertThat(game2.getBoardAsString(), is("■■■■■■■\n" +
                "       \n" +
                "       \n" +
                "Ḃ      \n" +
                " B     \n" +
                "Ḃ      \n" +
                "■■■■■■■\n"));
        assertThat(game3.getBoardAsString(), is("■■■■■■■\n" +
                "       \n" +
                "       \n" +
                "B      \n" +
                " Ḃ     \n" +
                "Ḃ      \n" +
                "■■■■■■■\n"));
    }

    @Test
    public void shouldJoystick() {
        //given
        init();
        when(dice.next(anyInt())).thenReturn(5);

        game3.getJoystick().up();
        game2.getJoystick().left();
        game1.getJoystick().down();

        //when
        field.tick();

        //then
        String expected = "■■■■■■■\n" +
                "       \n" +
                "Ḃ      \n" +
                "       \n" +
                " Ḃ     \n" +
                "       \n" +
                "f■■■■■■\n";
        assertThat(game1.getBoardAsString(), is(expected));
    }

    @Test
    public void shouldRemove() {
        //given
        init();
        when(dice.next(anyInt())).thenReturn(5);

        //when
        game3.close();
        field.tick();

        //then
        String expected = "■■■■■■■\n" +
                "       \n" +
                "       \n" +
                "       \n" +
                " Ḃ     \n" +
                "B      \n" +
                "■■■■■■■\n";
        assertThat(game1.getBoardAsString(), is(expected));
    }

    @Test
    public void shouldCrushEnemyBikeAfterClash() {
        //given
        init();
        when(dice.next(anyInt())).thenReturn(5);
        Bike bike1 = (Bike) game1.getPlayer().getHero();
        bike1.setX(bike1.getX() + 1);
        Bike bike3 = (Bike) game3.getPlayer().getHero();
        bike3.setX(bike3.getX() + 1);

        //when
        game1.getJoystick().up();
        field.tick();

        //then
        String expected = "■■■■■■■\n" +
                "       \n" +
                "       \n" +
                " Ḃ     \n" +
                " K     \n" +
                "       \n" +
                "■■■■■■■\n";
        assertThat(game1.getBoardAsString(), is(expected));
        assertThat(game2.isGameOver(), is(true));
    }

    @Test
    public void shouldCrushEnemyBikeAfterClash2() {
        //given
        init();
        when(dice.next(anyInt())).thenReturn(5);
        Bike bike1 = (Bike) game1.getPlayer().getHero();
        bike1.setX(bike1.getX() + 1);
        Bike bike3 = (Bike) game3.getPlayer().getHero();
        bike3.setX(bike3.getX() + 1);

        //when
        game1.getJoystick().up();
        game2.getJoystick().up();
        field.tick();

        //then
        String expected = "■■■■■■■\n" +
                "       \n" +
                "       \n" +
                " Ḱ     \n" +
                " B     \n" +
                "       \n" +
                "■■■■■■■\n";
        assertThat(game1.getBoardAsString(), is(expected));
        assertThat(game3.isGameOver(), is(true));
    }

    @Test
    public void shouldCrushEnemyBikeAfterClash3() {
        //given
        init();
        when(dice.next(anyInt())).thenReturn(5);
        Bike bike1 = (Bike) game1.getPlayer().getHero();
        bike1.setX(bike1.getX() + 1);
        Bike bike3 = (Bike) game3.getPlayer().getHero();
        bike3.setX(bike3.getX() + 1);

        //when
        game1.getJoystick().down();
        game2.getJoystick().down();
        field.tick();

        //then
        String expected = "■■■■■■■\n" +
                "       \n" +
                "       \n" +
                " Ḃ     \n" +
                "       \n" +
                " B     \n" +
                "■ḟ■■■■■\n";
        assertThat(game2.getBoardAsString(), is(expected));
        assertThat(game1.isGameOver(), is(true));
    }

    @Test
    public void shouldDoNothingAfterBikesClashEachOther__bike1() {
        //given
        init();
        when(dice.next(anyInt())).thenReturn(5);
        Bike bike1 = (Bike) game1.getPlayer().getHero();
        bike1.setX(bike1.getX() + 1);
        Bike bike3 = (Bike) game3.getPlayer().getHero();
        bike3.setX(bike3.getX() + 1);

        //when
        game1.getJoystick().up();
        game2.getJoystick().down();
        field.tick();

        //then
        String expected = "■■■■■■■\n" +
                "       \n" +
                "       \n" +
                " Ḃ     \n" +
                " Ḃ     \n" +
                " B     \n" +
                "■■■■■■■\n";
        assertThat(game1.getBoardAsString(), is(expected));
    }

    @Test
    public void shouldDoNothingAfterBikesClashEachOther__bike1__tick2() {
        //given
        init();
        when(dice.next(anyInt())).thenReturn(5);
        Bike bike1 = (Bike) game1.getPlayer().getHero();
        bike1.setX(bike1.getX() + 1);
        Bike bike3 = (Bike) game3.getPlayer().getHero();
        bike3.setX(bike3.getX() + 1);
        game1.getJoystick().up();
        game2.getJoystick().down();
        field.tick();

        //when
        game1.getJoystick().up();
        game2.getJoystick().down();
        field.tick();

        //then
        String expected = "■■■■■■■\n" +
                "       \n" +
                "       \n" +
                " Ḃ     \n" +
                " Ḃ     \n" +
                " B     \n" +
                "■■■■■■■\n";
        assertThat(game1.getBoardAsString(), is(expected));
    }

    @Test
    public void shouldDoNothingAfterBikesClashEachOther__bike2() {
        //given
        init();
        when(dice.next(anyInt())).thenReturn(5);
        Bike bike1 = (Bike) game1.getPlayer().getHero();
        bike1.setX(bike1.getX() + 1);
        Bike bike3 = (Bike) game3.getPlayer().getHero();
        bike3.setX(bike3.getX() + 1);

        //when
        game1.getJoystick().up();
        game2.getJoystick().down();
        field.tick();

        //then
        String expected = "■■■■■■■\n" +
                "       \n" +
                "       \n" +
                " Ḃ     \n" +
                " B     \n" +
                " Ḃ     \n" +
                "■■■■■■■\n";
        assertThat(game2.getBoardAsString(), is(expected));
    }

    @Test
    public void shouldDoNothingAfterBikesClashEachOther__bike3() {
        //given
        init();
        when(dice.next(anyInt())).thenReturn(5);
        Bike bike1 = (Bike) game1.getPlayer().getHero();
        bike1.setX(bike1.getX() + 1);
        Bike bike3 = (Bike) game3.getPlayer().getHero();
        bike3.setX(bike3.getX() + 1);

        //when
        game1.getJoystick().up();
        game2.getJoystick().down();
        field.tick();

        //then
        String expected = "■■■■■■■\n" +
                "       \n" +
                "       \n" +
                " B     \n" +
                " Ḃ     \n" +
                " Ḃ     \n" +
                "■■■■■■■\n";
        assertThat(game3.getBoardAsString(), is(expected));
    }

    @Test
    public void shouldMoveBikesInAnyOrderOfCall() {
        //given
        init();
        when(dice.next(anyInt())).thenReturn(5);
        Bike bike1 = (Bike) game1.getPlayer().getHero();
        bike1.setX(bike1.getX() + 1);
        Bike bike3 = (Bike) game3.getPlayer().getHero();
        bike3.setX(bike3.getX() + 1);

        //when
        game1.getJoystick().up();
        game2.getJoystick().up();
        game3.getJoystick().up();
        field.tick();

        //then
        String expected = "■■■■■■■\n" +
                "       \n" +
                " Ḃ     \n" +
                " Ḃ     \n" +
                " B     \n" +
                "       \n" +
                "■■■■■■■\n";
        assertThat(game1.getBoardAsString(), is(expected));
    }

    @Test
    public void shouldIncrementYCoordinateForAllBikes() {
        //given
        int springboardWeight = 17;
        int springboardTopSize = 1;

        init();
        when(dice.next(anyInt())).thenReturn(springboardWeight, springboardTopSize);
        Bike bike1 = (Bike) game1.getPlayer().getHero();
        bike1.setX(bike1.getX() + 1);
        Bike bike3 = (Bike) game3.getPlayer().getHero();
        bike3.setX(bike3.getX() + 1);
        field.tick();
        field.tick();
        field.tick();
        field.tick();
        field.tick();
        field.tick();
        field.tick();

        //when
        field.tick();

        //then
        String expected = "╔═╗■■■■\n" +
                "/═\\    \n" +
                "/Ḃ\\    \n" +
                "/Ḃ\\    \n" +
                "/B\\    \n" +
                "╚/╝    \n" +
                "■■■■■■■\n";
        assertThat(game1.getBoardAsString(), is(expected));
    }

    @Test
    public void shouldIgnoreAllCommands_whenBikeBeforeSpringboardRise() {
        //given
        int springboardWeight = 17;
        int springboardTopSize = 1;

        init();
        when(dice.next(anyInt())).thenReturn(springboardWeight, springboardTopSize);
        Bike bike1 = (Bike) game1.getPlayer().getHero();
        bike1.setX(bike1.getX() + 1);
        Bike bike3 = (Bike) game3.getPlayer().getHero();
        bike3.setX(bike3.getX() + 1);
        field.tick();
        field.tick();
        field.tick();
        field.tick();
        field.tick();

        //when
        game1.getJoystick().down();
        game3.getJoystick().up();
        field.tick();

        //then
        String expected = "■■╔═╗■■\n" +
                "  /═\\  \n" +
                "  /═\\  \n" +
                " Ḃ/═\\  \n" +
                " Ḃ/═\\  \n" +
                " B╚/╝  \n" +
                "■■■■■■■\n";
        assertThat(game1.getBoardAsString(), is(expected));
    }

    @Test
    public void shouldNotSpawnBikeInFlight() {
        //given
        int springboardWeight = 17;
        int springboardTopSize = 1;

        init();
        when(dice.next(anyInt())).thenReturn(springboardWeight, springboardTopSize);
        field.tick();
        field.tick();
        field.tick();
        field.tick();
        field.tick();
        field.tick();
        ((Bike) game1.getPlayer().getHero()).crush();
        field.tick();
        field.tick();

        //when
        game1.newGame();
        field.tick();

        //then
        String expected = "═╗■■■■■\n" +
                "═\\     \n" +
                "Ḃ\\     \n" +
                "═\\     \n" +
                "═Ř     \n" +
                "/S     \n" +
                "■■■■■■■\n";
        assertThat(game1.getBoardAsString(), is(expected));
    }

    @Test
    public void shouldCrushBike_whenBikeTakeDownCommandOnSpringboardRise() {
        //given
        int springboardWeight = 17;
        int springboardTopSize = 1;

        init();
        when(dice.next(anyInt())).thenReturn(springboardWeight, springboardTopSize);
        Bike bike1 = (Bike) game1.getPlayer().getHero();
        bike1.setX(bike1.getX() + 1);
        Bike bike3 = (Bike) game3.getPlayer().getHero();
        bike3.setX(bike3.getX() + 1);
        field.tick();
        field.tick();
        field.tick();
        field.tick();
        field.tick();
        field.tick();
        field.tick();

        //when
        game1.getJoystick().down();
        field.tick();

        //then
        String expected = "╔═╗■■■■\n" +
                "/═\\    \n" +
                "/Ḃ\\    \n" +
                "/Ḃ\\    \n" +
                "/═\\    \n" +
                "╚F╝    \n" +
                "■■■■■■■\n";
        assertThat(game1.getBoardAsString(), is(expected));
    }

    @Test
    public void shouldCrushBike_whenBikeHitOtherBikeAtTopOfSpringboard() {
        //given
        int springboardWeight = 17;
        int springboardTopSize = 1;

        init();
        when(dice.next(anyInt())).thenReturn(springboardWeight, springboardTopSize);
        Bike bike1 = (Bike) game1.getPlayer().getHero();
        bike1.setX(bike1.getX() + 1);
        Bike bike3 = (Bike) game3.getPlayer().getHero();
        bike3.setX(bike3.getX() + 1);
        field.tick();
        field.tick();
        field.tick();
        field.tick();
        field.tick();
        field.tick();
        field.tick();

        //when
        game1.getJoystick().up();
        field.tick();

        //then
        String expected = "╔═╗■■■■\n" +
                "/═\\    \n" +
                "/Ḃ\\    \n" +
                "/K\\    \n" +
                "/═\\    \n" +
                "╚/╝    \n" +
                "■■■■■■■\n";
        assertThat(game1.getBoardAsString(), is(expected));
        assertThat(game2.isGameOver(), is(true));
    }

    @Test
    public void shouldShiftCrushedBikeAtSpringboardTop() {
        //given
        int springboardWeight = 17;
        int springboardTopSize = 1;

        init();
        when(dice.next(anyInt())).thenReturn(springboardWeight, springboardTopSize);
        Bike bike1 = (Bike) game1.getPlayer().getHero();
        bike1.setX(bike1.getX() + 1);
        Bike bike3 = (Bike) game3.getPlayer().getHero();
        bike3.setX(bike3.getX() + 1);
        field.tick();
        field.tick();
        field.tick();
        field.tick();
        field.tick();
        field.tick();
        field.tick();

        //when
        game1.getJoystick().up();
        field.tick();
        field.tick();

        //then
        String expected = "═╗■■■■■\n" +
                "═\\     \n" +
                "═\\     \n" +
                "ḃŘ     \n" +
                "═R     \n" +
                "/╝     \n" +
                "■■■■■■■\n";
        assertThat(game1.getBoardAsString(), is(expected));
        assertThat(game2.isGameOver(), is(true));
    }

    @Test
    public void shouldCrushOnFenceBikeAfterFlightFromSpringboard() {
        //given
        int springboardWeight = 17;
        int springboardTopSize = 1;

        init();
        when(dice.next(anyInt())).thenReturn(springboardWeight, springboardTopSize);
        Bike bike1 = (Bike) game1.getPlayer().getHero();
        bike1.setX(bike1.getX() + 1);
        Bike bike3 = (Bike) game3.getPlayer().getHero();
        bike3.setX(bike3.getX() + 1);
        field.tick();
        field.tick();
        field.tick();
        field.tick();
        field.tick();
        field.tick();
        field.tick();

        //when
        game1.getJoystick().down();
        field.tick();
        field.tick();

        //then
        String expected = "═╗■■■■■\n" +
                "═\\     \n" +
                "═\\     \n" +
                "═Ř     \n" +
                "═Ř     \n" +
                "/╝     \n" +
                "■f■■■■■\n";
        assertThat(game1.getBoardAsString(), is(expected));
    }

    @Test
    public void shouldDecrementYCoordinateForAllBikes() {
        //given
        int springboardWeight = 17;
        int springboardTopSize = 1;

        init();
        when(dice.next(anyInt())).thenReturn(springboardWeight, springboardTopSize);
        Bike bike1 = (Bike) game1.getPlayer().getHero();
        bike1.setX(bike1.getX() + 1);
        bike1.setY(bike1.getY() + 2);
        Bike bike3 = (Bike) game3.getPlayer().getHero();
        bike3.setX(bike3.getX() + 1);
        bike3.setY(bike3.getY() - 2);
        field.tick();
        field.tick();
        field.tick();
        field.tick();
        field.tick();
        field.tick();
        field.tick();
        field.tick();

        //when
        field.tick();

        //then
        String expected = "═╗■■■■■\n" +
                "═\\     \n" +
                "═\\     \n" +
                "═R     \n" +
                "═Ř     \n" +
                "/Ŝ     \n" +
                "■■■■■■■\n";
        assertThat(game1.getBoardAsString(), is(expected));
    }

    @Test
    public void shouldIgnoreAllCommands_whenBikeBeforeSpringboardDecent() {
        //given
        int springboardWeight = 17;
        int springboardTopSize = 1;

        init();
        when(dice.next(anyInt())).thenReturn(springboardWeight, springboardTopSize);
        Bike bike1 = (Bike) game1.getPlayer().getHero();
        bike1.setX(bike1.getX() + 1);
        bike1.setY(bike1.getY() + 2);
        Bike bike3 = (Bike) game3.getPlayer().getHero();
        bike3.setX(bike3.getX() + 1);
        bike3.setY(bike3.getY() - 2);
        field.tick();
        field.tick();
        field.tick();
        field.tick();
        field.tick();
        field.tick();
        field.tick();
        field.tick();

        //when
        game2.getJoystick().up();
        game1.getJoystick().up();
        field.tick();

        //then
        String expected = "═╗■■■■■\n" +
                "═\\     \n" +
                "═\\     \n" +
                "═R     \n" +
                "═Ř     \n" +
                "/Ŝ     \n" +
                "■■■■■■■\n";
        assertThat(game1.getBoardAsString(), is(expected));
    }

    @Test
    public void shouldIgnoreAllCommands_whenBikeBeforeSpringboardDecent2() {
        //given
        int springboardWeight = 17;
        int springboardTopSize = 1;

        init();
        when(dice.next(anyInt())).thenReturn(springboardWeight, springboardTopSize);
        Bike bike1 = (Bike) game1.getPlayer().getHero();
        bike1.setX(bike1.getX() + 1);
        bike1.setY(bike1.getY() + 2);
        Bike bike3 = (Bike) game3.getPlayer().getHero();
        bike3.setX(bike3.getX() + 1);
        bike3.setY(bike3.getY() - 2);
        field.tick();
        field.tick();
        field.tick();
        field.tick();
        field.tick();
        field.tick();
        field.tick();
        field.tick();

        //when
        game3.getJoystick().up();
        game1.getJoystick().up();
        game2.getJoystick().down();
        field.tick();

        //then
        String expected = "═╗■■■■■\n" +
                "═\\     \n" +
                "═\\     \n" +
                "═R     \n" +
                "═Ř     \n" +
                "/Ŝ     \n" +
                "■■■■■■■\n";
        assertThat(game1.getBoardAsString(), is(expected));
    }

    @Test
    public void shouldIgnoreAllCommands_whenBikeBeforeSpringboardDecent3() {
        //given
        int springboardWeight = 17;
        int springboardTopSize = 1;

        init();
        when(dice.next(anyInt())).thenReturn(springboardWeight, springboardTopSize);
        Bike bike1 = (Bike) game1.getPlayer().getHero();
        bike1.setX(bike1.getX() + 1);
        bike1.setY(bike1.getY() + 2);
        Bike bike3 = (Bike) game3.getPlayer().getHero();
        bike3.setX(bike3.getX() + 1);
        bike3.setY(bike3.getY() + 2);
        Bike bike2 = (Bike) game2.getPlayer().getHero();
        bike2.setY(bike2.getY() + 2);
        field.tick();
        field.tick();
        field.tick();
        field.tick();
        field.tick();
        field.tick();
        field.tick();
        field.tick();

        //when
        game3.getJoystick().up();
        game1.getJoystick().up();
        game2.getJoystick().down();
        field.tick();

        //then
        String expected = "═╗■■■■■\n" +
                "═Ř     \n" +
                "═Ř     \n" +
                "═R     \n" +
                "═\\     \n" +
                "/╝     \n" +
                "■■■■■■■\n";
        assertThat(game1.getBoardAsString(), is(expected));
    }

    @Test
    public void shouldCrushBike_whenBikeAtHighestLineAndTakeUpCommandAfterSpringboardRise() {
        //given
        int springboardWeight = 17;
        int springboardTopSize = 1;

        init();
        when(dice.next(anyInt())).thenReturn(springboardWeight, springboardTopSize);
        Bike bike1 = (Bike) game1.getPlayer().getHero();
        bike1.setX(bike1.getX() + 1);
        bike1.setY(bike1.getY() + 2);
        Bike bike3 = (Bike) game3.getPlayer().getHero();
        bike3.setX(bike3.getX() + 1);
        bike3.setY(bike3.getY() + 2);
        Bike bike2 = (Bike) game2.getPlayer().getHero();
        bike2.setY(bike2.getY() + 2);
        field.tick();
        field.tick();
        field.tick();
        field.tick();
        field.tick();
        field.tick();
        field.tick();

        //when
        game3.getJoystick().up();
        game2.getJoystick().up();
        game1.getJoystick().down();
        field.tick();

        //then
        String expected = "╔Ḃ╗■■■■\n" +
                "/═\\    \n" +
                "/═\\    \n" +
                "/B\\    \n" +
                "/═\\    \n" +
                "╚/╝    \n" +
                "■■■■■■■\n";
        assertThat(game1.getBoardAsString(), is(expected));
        assertThat(game3.isGameOver(), is(true));
    }

    @Test
    public void shouldCrushBike_whenBikeAtHighestLineAndTakeUpCommandAfterSpringboardDecent() {
        //given
        int springboardWeight = 17;
        int springboardTopSize = 1;

        init();
        when(dice.next(anyInt())).thenReturn(springboardWeight, springboardTopSize);
        Bike bike1 = (Bike) game1.getPlayer().getHero();
        bike1.setX(bike1.getX() + 1);
        bike1.setY(bike1.getY() + 2);
        Bike bike3 = (Bike) game3.getPlayer().getHero();
        bike3.setX(bike3.getX() + 1);
        bike3.setY(bike3.getY() + 2);
        Bike bike2 = (Bike) game2.getPlayer().getHero();
        bike2.setY(bike2.getY() + 2);
        field.tick();
        field.tick();
        field.tick();
        field.tick();
        field.tick();
        field.tick();
        field.tick();
        field.tick();
        field.tick();

        //when
        game3.getJoystick().up();
        game1.getJoystick().up();
        game2.getJoystick().down();
        field.tick();

        //then
        String expected = "╗ḟ■■■■■\n" +
                "\\      \n" +
                "\\Ḃ     \n" +
                "\\B     \n" +
                "\\      \n" +
                "╝      \n" +
                "■■■■■■■\n";
        assertThat(game1.getBoardAsString(), is(expected));
    }

    @Test
    public void shouldIgnoreAllCommands_whenBikeBeforeSpringboardDecent4() {
        //given
        int springboardWeight = 17;
        int springboardTopSize = 0;

        init();
        when(dice.next(anyInt())).thenReturn(springboardWeight, springboardTopSize);
        Bike bike1 = (Bike) game1.getPlayer().getHero();
        bike1.setX(bike1.getX() + 1);
        Bike bike3 = (Bike) game3.getPlayer().getHero();
        bike3.setX(bike3.getX() + 1);
        field.tick();
        field.tick();
        field.tick();
        field.tick();
        field.tick();
        field.tick();
        field.tick();

        //when
        game1.getJoystick().up();
        field.tick();

        //then
        String expected = "╔╗■■■■■\n" +
                "/\\     \n" +
                "/\\     \n" +
                "/Ř     \n" +
                "/Ř     \n" +
                "╚S     \n" +
                "■■■■■■■\n";

        assertThat(game1.getBoardAsString(), is(expected));
    }

    @Test
    public void shouldIgnoreAllCommands_whenBikeBeforeSpringboardDecent5() {
        //given
        int springboardWeight = 17;
        int springboardTopSize = 0;

        init();
        when(dice.next(anyInt())).thenReturn(springboardWeight, springboardTopSize);
        Bike bike1 = (Bike) game1.getPlayer().getHero();
        bike1.setX(bike1.getX() + 1);
        Bike bike3 = (Bike) game3.getPlayer().getHero();
        bike3.setX(bike3.getX() + 1);
        field.tick();
        field.tick();
        field.tick();
        field.tick();
        field.tick();
        field.tick();
        field.tick();

        //when
        game1.getJoystick().up();
        game3.getJoystick().up();
        field.tick();

        //then
        String expected = "╔╗■■■■■\n" +
                "/\\     \n" +
                "/\\     \n" +
                "/Ř     \n" +
                "/Ř     \n" +
                "╚S     \n" +
                "■■■■■■■\n";

        assertThat(game1.getBoardAsString(), is(expected));
    }

    @Test
    public void shouldIgnoreAllCommands_whenBikeBeforeSpringboardDecent6() {
        //given
        int springboardWeight = 17;
        int springboardTopSize = 0;

        init();
        when(dice.next(anyInt())).thenReturn(springboardWeight, springboardTopSize);
        Bike bike1 = (Bike) game1.getPlayer().getHero();
        bike1.setX(bike1.getX() + 1);
        bike1.setY(bike1.getY() + 2);
        Bike bike3 = (Bike) game3.getPlayer().getHero();
        bike3.setX(bike3.getX() + 1);
        bike3.setY(bike3.getY() + 2);
        Bike bike2 = (Bike) game2.getPlayer().getHero();
        bike2.setY(bike2.getY() + 2);
        field.tick();
        field.tick();
        field.tick();
        field.tick();
        field.tick();
        field.tick();
        field.tick();

        //when
        game1.getJoystick().up();
        game3.getJoystick().up();
        field.tick();

        //then
        String expected = "╔╗■■■■■\n" +
                "/Ř     \n" +
                "/Ř     \n" +
                "/R     \n" +
                "/\\     \n" +
                "╚╝     \n" +
                "■■■■■■■\n";

        assertThat(game1.getBoardAsString(), is(expected));
    }

}
