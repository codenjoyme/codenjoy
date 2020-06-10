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
import com.codenjoy.dojo.bomberman.services.DefaultGameSettings;
import com.codenjoy.dojo.services.*;
import com.codenjoy.dojo.services.multiplayer.Single;
import com.codenjoy.dojo.services.printer.PrinterFactory;
import com.codenjoy.dojo.services.printer.PrinterFactoryImpl;
import com.codenjoy.dojo.services.round.RoundSettingsWrapper;
import com.codenjoy.dojo.services.settings.SimpleParameter;
import org.junit.Before;
import org.mockito.stubbing.OngoingStubbing;

import java.util.Collections;
import java.util.List;

import static com.codenjoy.dojo.bomberman.model.Bomberman.ALL;
import static com.codenjoy.dojo.services.settings.SimpleParameter.v;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class AbstractBombermanTest {


    public int SIZE = 5;
    protected Game game;
    protected Hero hero;
    protected Level level;
    private WallsImpl walls;
    protected GameSettings settings;
    protected EventListener listener;
    protected Dice meatChppperDice;
    protected Dice bombermanDice;
    protected Player player;
    protected List bombermans;
    protected Bomberman field;
    private final PrinterFactory printer = new PrinterFactoryImpl();

    @Before
    public void setUp() {
        meatChppperDice = mock(Dice.class);
        bombermanDice = mock(Dice.class);

        level = mock(Level.class);
        canDropBombs(1);
        bombsPower(1);
        walls = mock(WallsImpl.class);
        when(walls.iterator()).thenReturn(Collections.emptyIterator());
        settings = mock(GameSettings.class);
        listener = mock(EventListener.class);

        when(settings.getWalls(any(Bomberman.class))).thenReturn(walls);
        when(settings.getLevel()).thenReturn(level);
        when(settings.getRoundSettings()).thenReturn(getRoundSettings());
        when(settings.killOtherHeroScore()).thenReturn(v(200));
        when(settings.killMeatChopperScore()).thenReturn(v(100));
        when(settings.killWallScore()).thenReturn(v(10));

        initBomberman();
        givenBoard(SIZE);
        PerksSettingsWrapper.clear();
    }

    protected void initBomberman() {
        dice(bombermanDice, 0, 0);
        Hero hero = new Hero(level, bombermanDice);
        when(settings.getBomberman(level)).thenReturn(hero);
        this.hero = hero;
    }

    public static RoundSettingsWrapper getRoundSettings() {
        return new DefaultGameSettings(mock(Dice.class)).getRoundSettings();
    }

    protected void givenBoard(int size) {
        when(settings.getBoardSize()).thenReturn(v(size));
        field = new Bomberman(settings);
        player = new Player(listener, getRoundSettings().roundsEnabled());
        game = new Single(player, printer);
        game.on(field);
        dice(bombermanDice, 0, 0);
        game.newGame();
        hero = (Hero)game.getJoystick();
    }

    protected SimpleParameter<Boolean> getRoundsEnabled() {
        return new SimpleParameter<>(false);
    }

    protected void assertBombermanAt(int x, int y) {
        assertEquals(x, player.getHero().getX());
        assertEquals(y, player.getHero().getY());
    }

    protected void gotoMaxUp() {
        for (int y = 0; y <= SIZE + 1; y++) {
            hero.up();
            field.tick();
        }
    }

    protected void canDropBombs(int countBombs) {
        reset(level);
        when(level.bombsCount()).thenReturn(countBombs);
    }

    protected void assertBombermanDie() {
        assertEquals("Expected game over", true, game.isGameOver());
    }

    protected void assertBombermanAlive() {
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
        givenBoard(size);
    }

    protected void givenBoardWithDestroyWalls() {
        givenBoardWithDestroyWalls(SIZE);
    }

    protected void givenBoardWithDestroyWalls(int size) {
        withWalls(new DestroyWalls(new OriginalWalls(v(size))));
        givenBoard(size);
    }

    protected void withWalls(Walls walls) {
        when(settings.getWalls(any(Bomberman.class))).thenReturn(walls);
    }

    protected void givenBoardWithOriginalWalls() {
        givenBoardWithOriginalWalls(SIZE);
    }

    protected void givenBoardWithOriginalWalls(int size) {
        withWalls(new OriginalWalls(v(size)));
        givenBoard(size);
    }

    protected void bombsPower(int power) {
        when(level.bombsPower()).thenReturn(power);
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
        OngoingStubbing<Integer> when = when(dice.next(anyInt()));
        for (int value : values) {
            when = when.thenReturn(value);
        }
    }

    protected void givenBoardWithMeatChopper(int size) {
        dice(meatChppperDice, size - 2, size - 2);

        Field temp = mock(Field.class);
        when(temp.size()).thenReturn(size);
        MeatChoppers walls = new MeatChoppers(new OriginalWalls(v(size)), temp, v(1), meatChppperDice);
        bombermans = mock(List.class);
        when(bombermans.contains(anyObject())).thenReturn(false);
        when(temp.heroes(ALL)).thenReturn(bombermans);
        withWalls(walls);
        walls.regenerate();
        givenBoard(size);

        dice(meatChppperDice, 1, Direction.UP.value());  // Чертик будет упираться в стенку и стоять на месте
    }

    protected void givenBoardWithDestroyWallsAt(int x, int y) {
        withWalls(new AbstractBombermanTest.DestroyWallAt(x, y, new WallsImpl()));
        givenBoard(SIZE);
    }


    protected void givenBoardWithMeatChopperAt(int x, int y) {
        withWalls(new AbstractBombermanTest.MeatChopperAt(x, y, new WallsImpl()));
        givenBoard(SIZE);
    }


    static class DestroyWallAt extends WallsDecorator {

        public DestroyWallAt(int x, int y, Walls walls) {
            super(walls);
            walls.add(new DestroyWall(x, y));
        }

        @Override
        public Wall destroy(Point pt) {   // неразрушаемая стенка
            return walls.get(pt);
        }

    }

    static class MeatChopperAt extends WallsDecorator {

        public MeatChopperAt(int x, int y, Walls walls) {
            super(walls);
            walls.add(new MeatChopper(x, y));
        }

        @Override
        public Wall destroy(Point pt) {   // неубиваемый монстрик
            return walls.get(pt);
        }

    }

}
