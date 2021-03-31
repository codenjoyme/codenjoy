package com.codenjoy.dojo.bomberman.model;

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

import com.codenjoy.dojo.bomberman.model.perks.PerksSettingsWrapper;
import com.codenjoy.dojo.bomberman.TestGameSettings;
import com.codenjoy.dojo.bomberman.services.GameSettings;
import com.codenjoy.dojo.services.*;
import com.codenjoy.dojo.services.multiplayer.Single;
import com.codenjoy.dojo.services.printer.PrinterFactory;
import com.codenjoy.dojo.services.printer.PrinterFactoryImpl;
import org.junit.Before;
import org.mockito.stubbing.OngoingStubbing;

import java.util.Arrays;

import static com.codenjoy.dojo.bomberman.model.EventsListenersAssert.getEvents;
import static com.codenjoy.dojo.bomberman.services.GameSettings.Keys.*;
import static com.codenjoy.dojo.services.PointImpl.pt;
import static com.codenjoy.dojo.services.settings.SimpleParameter.v;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.mockito.Mockito.*;

public class AbstractGameTest {

    public int SIZE = 5;
    protected Game game;
    protected Hero hero;
    protected Walls walls = new WallsImpl();
    protected GameSettings settings;
    protected EventListener listener;
    protected Dice chopperDice;
    protected Dice dice;
    protected Player player;
    protected Bomberman field;
    private PrinterFactory printer;
    protected PerksSettingsWrapper perks;

    @Before
    public void setUp() {
        printer = new PrinterFactoryImpl();
        settings = spy(new TestGameSettings());
        perks = settings.perksSettings();
        bombsPower(1);

        givenWalls();

        chopperDice = mock(Dice.class);
        dice = mock(Dice.class);

        listener = mock(EventListener.class);

        withWalls(walls);

        givenBoard(SIZE, 0, 0);
    }

    protected void initHero(int x, int y) {
        dice(dice, x, y);
        Level level = settings.getLevel();
        Hero hero = new Hero(level);
        when(settings.getHero(any(Level.class))).thenReturn(hero);
        this.hero = hero;
    }

    protected void givenBoard(int size, int x, int y) {
        settings.integer(BOARD_SIZE, size);
        field = new Bomberman(dice, settings);
        player = new Player(listener, settings);
        game = new Single(player, printer);
        game.on(field);
        initHero(x, y); // hero позиция
        game.newGame();
        hero = (Hero)game.getJoystick();
    }

    protected void gotoMaxUp() {
        for (int y = 0; y <= SIZE + 1; y++) {
            hero.up();
            field.tick();
        }
    }

    protected void newGameForDied() {
        if (!player.isAlive()) {
            field.newGame(player);
        }
        hero = player.getHero();
        hero.setAlive(true);
    }

    protected void canDropBombs(int count) {
        settings.integer(BOMBS_COUNT, count);
    }

    protected void assertHeroDie() {
        assertEquals("Expected game over", true, game.isGameOver());
    }

    protected void assertHeroAlive() {
        assertFalse(game.isGameOver());
    }

    protected void gotoBoardCenter() {
        for (int y = 0; y < SIZE / 2; y++) {
            hero.up();
            field.tick();
            hero.right();
            field.tick();
        }
    }

    protected void asrtBrd(String expected) {
        assertEquals(expected, printer.getPrinter(
                field.reader(), player).print());
    }


    protected void givenBoardWithWalls() {
        givenBoardWithWalls(SIZE);
    }

    protected void givenBoardWithWalls(int size) {
        withWalls(new OriginalWalls(v(size)));
        givenBoard(size, 1, 1); // hero в левом нижнем углу с учетом стен
    }

    protected void givenBoardWithDestroyWalls() {
        givenBoardWithDestroyWalls(SIZE);
    }

    protected void givenBoardWithDestroyWalls(int size) {
        withWalls(new MeatChoppers(new DestroyWalls(new OriginalWalls(v(size))), v(0), dice));
        givenBoard(size, 1, 1); // hero в левом нижнем углу с учетом стен
    }

    protected void withWalls(Walls walls) {
        when(settings.getWalls(dice)).thenReturn(walls);
    }

    protected void givenBoardWithOriginalWalls() {
        givenBoardWithOriginalWalls(SIZE);
    }

    protected void givenBoardWithOriginalWalls(int size) {
        withWalls(new OriginalWalls(v(size)));
        givenBoard(size, 1, 1); // hero в левом нижнем углу с учетом стен
    }

    protected void bombsPower(int power) {
        settings.integer(BOMB_POWER, power);
    }

    protected void assertBombPower(int power, String expected) {
        givenBoardWithOriginalWalls(9);
        bombsPower(power);

        hero.act();
        goOut();
        field.tick();

        asrtBrd(expected);
    }

    protected void goOut() {
        hero.right();
        field.tick();
        hero.right();
        field.tick();
        hero.up();
        field.tick();
        hero.up();
        field.tick();
    }

    protected void dice(Dice dice, int... values) {
        reset(dice);
        OngoingStubbing<Integer> when = when(dice.next(anyInt()));
        for (int value : values) {
            when = when.thenReturn(value);
        }
    }

    protected void givenBoardWithMeatChopper(int size) {
        dice(chopperDice, size - 2, size - 2);

        SIZE = size;
        MeatChoppers walls = new MeatChoppers(new OriginalWalls(v(size)), v(1), chopperDice);
        withWalls(walls);

        givenBoard(size, 1, 1); // hero в левом нижнем углу с учетом стен

        walls.init(field);
        walls.regenerate();

        this.walls = walls;

        dice(chopperDice, 1, Direction.UP.value());  // Чертик будет упираться в стенку и стоять на месте
    }

    protected DestroyWall destroyWallAt(int x, int y) {
        DestroyWall wall = new DestroyWall(pt(x, y));
        walls.add(wall);
        return wall;
    }

    private void givenWalls(Wall... input) {
        Arrays.asList(input).forEach(walls::add);
    }

    protected MeatChopper meatChopperAt(int x, int y) {
        MeatChopper chopper = new MeatChopper(pt(x, y), field, chopperDice);
        chopper.stop();
        walls.add(chopper);
        return chopper;
    }

    protected void verifyAllEvents(String expected) {
        assertEquals(expected, getEvents(listener));
    }
}
