package com.codenjoy.dojo.spacerace.model;

/*-
 * #%L
 * Codenjoy - it's a dojo-like platform from developers to developers.
 * %%
 * Copyright (C) 2012 - 2022 Codenjoy
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

import com.codenjoy.dojo.services.EventListener;
import com.codenjoy.dojo.services.dice.MockDice;
import com.codenjoy.dojo.services.printer.PrinterFactory;
import com.codenjoy.dojo.services.printer.PrinterFactoryImpl;
import com.codenjoy.dojo.services.settings.SettingsReader;
import com.codenjoy.dojo.spacerace.services.GameSettings;
import com.codenjoy.dojo.utils.TestUtils;
import org.junit.Before;
import org.junit.Test;

import static com.codenjoy.dojo.spacerace.services.GameSettings.Keys.BULLETS_COUNT;
import static com.codenjoy.dojo.spacerace.services.GameSettings.Keys.TICKS_TO_RECHARGE;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;

public class GameTest {

    private Spacerace game;
    private BulletCharger charger;
    private Hero hero;
    private MockDice dice;
    private EventListener listener;
    private Player player;
    private PrinterFactory printer = new PrinterFactoryImpl();
    private GameSettings settings;

    @Before
    public void setup() {
        dice = new MockDice();
        settings = new GameSettings()
                .integer(TICKS_TO_RECHARGE, 100)
                .integer(BULLETS_COUNT, 1);
        charger = new BulletCharger(settings);
    }

    private void dice(Integer... next) {
        dice.then(next);
    }

    private void diceNew(int...ints) {
        if (ints.length == 0) { // we work just with nothing
            dice.then(-1);
        }

        if (ints.length == 1) { // we work just with stones
            dice.then(-1, -1, -1, -1, ints[0], -1);
        }

        if (ints.length == 2) { // we work with stones and bombs
            dice.then(-1, -1, -1, -1, ints[0], ints[1], -1);
        }

        if (ints.length == 4) { // we work stones, bombs and bulletPacks
            dice.then(ints[2], ints[3], ints[0], ints[1], -1);
        }
    }

    private void givenFl(String board) {
        Level level = new Level(board);
        hero = level.hero(charger).get(0);
        game = new Spacerace(level, dice, settings);
        listener = mock(EventListener.class);
        player = new Player(listener, settings);
        dice(hero.getX(), hero.getY()); // позиция рассчитывается рендомно из dice
        player.setHero(hero);
        game.newGame(player);
    }

    private void assertE(String expected) {
        assertEquals(TestUtils.injectN(expected),
                printer.getPrinter(game.reader(), player).print());
    }

    private void newBulletPackForHeroWithGivenBullets(int i) {
        SettingsReader settings = new GameSettings()
                .integer(TICKS_TO_RECHARGE, 1000)
                .integer(BULLETS_COUNT, i);

        charger = new BulletCharger(settings);
    }

    @Test
    public void shouldNoBulletsAfterFireWithEmptyBulletCharger() {

        newBulletPackForHeroWithGivenBullets(10);
        //Given
        givenFl("☼   ☼" +
                "☼   ☼" +
                "☼ ☺ ☼" +
                "☼   ☼" +
                "☼   ☼");

        //when
        diceNew(-1, -1, -1, -1);

        hero.act();
        game.tick();

        //Then
        assertE("☼   ☼" +
                "☼   ☼" +
                "☼ ☺ ☼" +
                "☼   ☼" +
                "☼   ☼");

        //when
        diceNew(-1, -1, 1, 0);
        game.tick();

        //Then
        assertE("☼   ☼" +
                "☼ 7 ☼" +
                "☼ ☺ ☼" +
                "☼   ☼" +
                "☼   ☼");

        //when
        hero.up();
        game.tick();
        hero.recharge();

        //then
        assertE("☼   ☼" +
                "☼ ☺ ☼" +
                "☼   ☼" +
                "☼   ☼" +
                "☼   ☼");

        //When
        hero.act();
        game.tick();

        //then
        assertE("☼ * ☼" +
                "☼ ☺ ☼" +
                "☼   ☼" +
                "☼   ☼" +
                "☼   ☼");

        //When
        hero.act();
        game.tick();

        //then
        assertE("☼ * ☼" +
                "☼ ☺ ☼" +
                "☼   ☼" +
                "☼   ☼" +
                "☼   ☼");
        hero.act();
        game.tick();

        //then
        assertE("☼ * ☼" + //TODO не присваивается новый BulletCharger
                "☼ ☺ ☼" +
                "☼   ☼" +
                "☼   ☼" +
                "☼   ☼");

        for (int i = 0; i < 8; i++) {
            hero.act();
            game.tick();
        }

        //Given
        assertE("☼   ☼" +   // todo по подстетам пуля должна быть (последняя), говорит, что нету
                "☼ ☺ ☼" +
                "☼   ☼" +
                "☼   ☼" +
                "☼   ☼");

        hero.act();
        game.tick();

        //Given
        assertE("☼   ☼" +
                "☼ ☺ ☼" +
                "☼   ☼" +
                "☼   ☼" +
                "☼   ☼");
    }



    // есть карта со мной
    @Test
    public void shouldFieldAtStart() {
        //Given
        givenFl("☼   ☼" +
                "☼   ☼" +
                "☼ ☺ ☼" +
                "☼   ☼" +
                "☼   ☼");

        assertE("☼   ☼" +
                "☼   ☼" +
                "☼ ☺ ☼" +
                "☼   ☼" +
                "☼   ☼");
    }

    // я могу двигаться
    @Test
    public void shouldFieldICanMove() {
        //given
        givenFl("☼   ☼" +
                "☼   ☼" +
                "☼ ☺ ☼" +
                "☼   ☼" +
                "☼   ☼");
        //when
        diceNew(); // выключаем генерацию каменей и мин
        hero.up();
        game.tick();

        //then
        assertE("☼   ☼" +
                "☼ ☺ ☼" +
                "☼   ☼" +
                "☼   ☼" +
                "☼   ☼");

        //When
        hero.right();
        game.tick();

        //Then
        assertE("☼   ☼" +
                "☼  ☺☼" +
                "☼   ☼" +
                "☼   ☼" +
                "☼   ☼");

        //When
        hero.down();
        game.tick();

        //Then
        assertE("☼   ☼" +
                "☼   ☼" +
                "☼  ☺☼" +
                "☼   ☼" +
                "☼   ☼");

        //When
        hero.left();
        game.tick();

        //Then
        assertE("☼   ☼" +
                "☼   ☼" +
                "☼ ☺ ☼" +
                "☼   ☼" +
                "☼   ☼");
    }

    // появляется новый комень
    @Test
    public void shouldNewStone() {
        //Given
        givenFl("☼   ☼" +
                "☼   ☼" +
                "☼☺  ☼" +
                "☼   ☼" +
                "☼   ☼");

        //When
        diceNew(1);
        game.tick();
        game.tick();
        game.tick();

        //Then
        assertE("☼ 0 ☼" +
                "☼   ☼" +
                "☼☺  ☼" +
                "☼   ☼" +
                "☼   ☼");
        //When
        diceNew(2);
        game.tick();
        game.tick();
        game.tick();

        //Then
        assertE("☼  0☼" +
                "☼   ☼" +
                "☼☺  ☼" +
                "☼ 0 ☼" +
                "☼   ☼");
    }

    @Test
    public void shouldStoneMove() {
        //Given
        givenFl("☼   ☼" +
                "☼   ☼" +
                "☼   ☼" +
                "☼   ☼" +
                "☼ ☺ ☼");

        //When
        diceNew(0);
        game.tick();
        game.tick();
        game.tick();

        //Then
        assertE("☼0  ☼" +
                "☼   ☼" +
                "☼   ☼" +
                "☼   ☼" +
                "☼ ☺ ☼");
        //When
        game.tick();

        //Then
        assertE("☼   ☼" +
                "☼0  ☼" +
                "☼   ☼" +
                "☼   ☼" +
                "☼ ☺ ☼");
    }

    @Test
    public void shouldStoneAppearsEvery3seconds() {
        //Given
        givenFl("☼   ☼" +
                "☼   ☼" +
                "☼   ☼" +
                "☼   ☼" +
                "☼ ☺ ☼");

        //When
        diceNew(1); // камень в первой колонке, мины нет, камень во второй колонке
        game.tick();
        game.tick();
        game.tick();
        diceNew(2);
        game.tick();
        game.tick();
        game.tick();

        //Then
        assertE("☼  0☼" +
                "☼   ☼" +
                "☼   ☼" +
                "☼ 0 ☼" +
                "☼ ☺ ☼");
    }

    @Test
    public void shouldHeroShoot() {
        //Given
        givenFl("☼   ☼" +
                "☼   ☼" +
                "☼   ☼" +
                "☼ ☺ ☼" +
                "☼   ☼");

        //When
        diceNew();
        hero.recharge();
        hero.act();
        game.tick();
        game.tick();

        //Then
        assertE("☼   ☼" +
                "☼ * ☼" +
                "☼   ☼" +
                "☼ ☺ ☼" +
                "☼   ☼");
    }

    @Test
    public void shouldBulletOutOfTheBoard() {
        //Given
        givenFl("☼   ☼" +
                "☼   ☼" +
                "☼ ☺ ☼" +
                "☼   ☼" +
                "☼   ☼");

        //When
        diceNew();
        hero.recharge();
        hero.act();
        game.tick();


        //Then
        assertE("☼   ☼" +
                "☼ * ☼" +
                "☼ ☺ ☼" +
                "☼   ☼" +
                "☼   ☼");

        game.tick();
        game.tick();

           //Then
        assertE("☼   ☼" +
                "☼   ☼" +
                "☼ ☺ ☼" +
                "☼   ☼" +
                "☼   ☼");
    }

    @Test
    public void shouldStoneIsDestroyedByBullet() {
        //Given
        givenFl("☼   ☼" +
                "☼   ☼" +
                "☼   ☼" +
                "☼   ☼" +
                "☼ ☺ ☼");
        //When
        diceNew(1);
        hero.recharge();
        hero.act();
        game.tick();
        game.tick();
        game.tick();

        //Then
        assertE("☼ 0 ☼" +
                "☼ * ☼" +
                "☼   ☼" +
                "☼   ☼" +
                "☼ ☺ ☼");

        //When
        game.tick();

        //Then
        assertE("☼ x ☼" +
                "☼   ☼" +
                "☼   ☼" +
                "☼   ☼" +
                "☼ ☺ ☼");

    }

    @Test
    public void shouldStoneIsDestroyedByBullet2() {
        //Given
        givenFl("☼   ☼" +
                "☼   ☼" +
                "☼   ☼" +
                "☼ ☺ ☼" +
                "☼   ☼");

        //When
        diceNew(1);
        hero.recharge();
        hero.act();
        game.tick();

        //Then
        assertE("☼   ☼" +
                "☼   ☼" +
                "☼ * ☼" +
                "☼ ☺ ☼" +
                "☼   ☼");

        //When
        game.tick();

        //Then
        assertE("☼   ☼" +
                "☼ * ☼" +
                "☼   ☼" +
                "☼ ☺ ☼" +
                "☼   ☼");

        //When
        game.tick();

        //Then
        assertE("☼ x ☼" +
                "☼   ☼" +
                "☼   ☼" +
                "☼ ☺ ☼" +
                "☼   ☼");

        //When
        game.tick();

        //Then
        assertE("☼   ☼" +
                "☼   ☼" +
                "☼   ☼" +
                "☼ ☺ ☼" +
                "☼   ☼");

    }

    // проявляем новую мину
    @Test
    public void shouldNewBomb() {
        //given
        givenFl("☼   ☼" +
                "☼   ☼" +
                "☼ ☺ ☼" +
                "☼   ☼" +
                "☼   ☼");

        //When
        diceNew(-1, 1); // камень не появляем, мину появляем, тоже на 3-м тике
        game.tick();
        game.tick();
        game.tick();


        //then
        assertE("☼ ♣ ☼" +
                "☼   ☼" +
                "☼ ☺ ☼" +
                "☼   ☼" +
                "☼   ☼");
    }

    @Test
    public void shouldNewBombAtRandomPlace() {
        //given
        givenFl("☼   ☼" +
                "☼   ☼" +
                "☼   ☼" +
                "☼   ☼" +
                "☼  ☺☼");

        //when
        diceNew(-1, 0);
        game.tick();
        game.tick();
        game.tick();

        //then
        assertE("☼♣  ☼" +
                "☼   ☼" +
                "☼   ☼" +
                "☼   ☼" +
                "☼  ☺☼");

        //when
        diceNew(-1, 2);
        game.tick();
        game.tick();
        game.tick();

        //then
        assertE("☼  ♣☼" +
                "☼   ☼" +
                "☼   ☼" +
                "☼♣  ☼" +
                "☼  ☺☼");
    }

    @Test
    public void shouldNewBombAndNewStoneAtNewPlace() {
        //given
        givenFl("☼   ☼" +
                "☼   ☼" +
                "☼   ☼" +
                "☼ ☺ ☼" +
                "☼   ☼");

        //when
        diceNew(0, 2);
        game.tick();
        game.tick();
        game.tick();
        game.tick();

        //then
        assertE("☼   ☼" +
                "☼0 ♣☼" +
                "☼   ☼" +
                "☼ ☺ ☼" +
                "☼   ☼");
    }

    @Test
    public void shouldBombRemovedWhenOutsideBorder() {
        //Given
        givenFl("☼   ☼" +
                "☼   ☼" +
                "☼   ☼" +
                "☼   ☼" +
                "☼☺  ☼");

        //When
        diceNew(-1, 2);
        game.tick();
        game.tick();
        game.tick();

        //Then
        assertE("☼  ♣☼" +
                "☼   ☼" +
                "☼   ☼" +
                "☼   ☼" +
                "☼☺  ☼");

        //When
        game.tick();
        game.tick();
        game.tick();
        game.tick();

        //Then
        assertE("☼   ☼" +
                "☼   ☼" +
                "☼   ☼" +
                "☼   ☼" +
                "☼☺ ♣☼");

        //When
        game.tick();

        //Then
        assertE("☼   ☼" +
                "☼   ☼" +
                "☼   ☼" +
                "☼   ☼" +
                "☼☺  ☼");
    }

    @Test
    public void shouldBombDestroyedByBullet() {
        // given

        givenFl("☼   ☼" +
                "☼   ☼" +
                "☼   ☼" +
                "☼   ☼" +
                "☼ ☺ ☼");


        diceNew(-1, 1);
        hero.recharge();
        hero.act();
        game.tick();
        game.tick();
        game.tick();


        assertE("☼ ♣ ☼" +
                "☼ * ☼" +
                "☼   ☼" +
                "☼   ☼" +
                "☼ ☺ ☼");

        game.tick();

        assertE("☼xxx☼" +
                "☼xxx☼" +
                "☼   ☼" +
                "☼   ☼" +
                "☼ ☺ ☼");
    }

    @Test
    public void shouldBombDestroyedByBullet2() {
        // given

        givenFl("☼   ☼" +
                "☼   ☼" +
                "☼   ☼" +
                "☼ ☺ ☼" +
                "☼   ☼");


        diceNew(-1, 1);
        hero.recharge();
        hero.act();
        game.tick();
        game.tick();


        assertE("☼   ☼" +
                "☼ * ☼" +
                "☼   ☼" +
                "☼ ☺ ☼" +
                "☼   ☼");

        game.tick();

        assertE("☼xxx☼" +
                "☼xxx☼" +
                "☼   ☼" +
                "☼ ☺ ☼" +
                "☼   ☼");

        game.tick();

        assertE("☼   ☼" +
                "☼   ☼" +
                "☼   ☼" +
                "☼ ☺ ☼" +
                "☼   ☼");
    }

    @Test
    public void shouldNewBombAndNewStoneAtNewPlace2() {
        // given
        givenFl("☼    ☼" +
                "☼    ☼" +
                "☼ ☺  ☼" +
                "☼    ☼" +
                "☼    ☼" +
                "☼    ☼");

        diceNew(1, 3);
        game.tick();
        game.tick();
        game.tick();

        // then
        assertE("☼ 0 ♣☼" +
                "☼    ☼" +
                "☼ ☺  ☼" +
                "☼    ☼" +
                "☼    ☼" +
                "☼    ☼");
    }

    @Test
    public void shouldBombDestroyedByBulletNew() {
        // given
        givenFl("☼    ☼" +
                "☼    ☼" +
                "☼    ☼" +
                "☼    ☼" +
                "☼    ☼" +
                "☼  ☺ ☼");

        diceNew(0, 2);
        hero.recharge();
        game.tick();
        game.tick();
        hero.act();
        game.tick();
        game.tick();

        // then
        assertE("☼    ☼" +
                "☼0 ♣ ☼" +
                "☼    ☼" +
                "☼  * ☼" +
                "☼    ☼" +
                "☼  ☺ ☼");

        game.tick();

        // then
        assertE("☼    ☼" +
                "☼ xxx☼" +
                "☼0xxx☼" +
                "☼ xxx☼" +
                "☼    ☼" +
                "☼  ☺ ☼");

    }

    @Test
    public void shouldBombDestroyHero() {
        // given
        givenFl("☼    ☼" +
                "☼    ☼" +
                "☼    ☼" +
                "☼    ☼" +
                "☼  ☺ ☼" +
                "☼    ☼");

        diceNew(0, 2);
        game.tick();
        game.tick();
        game.tick();
        game.tick();

        // then
        assertE("☼    ☼" +
                "☼0 ♣ ☼" +
                "☼    ☼" +
                "☼    ☼" +
                "☼  ☺ ☼" +
                "☼    ☼");

        game.tick();
        game.tick();

        // then
        assertE("☼    ☼" +
                "☼    ☼" +
                "☼ xxx☼" +
                "☼0xxx☼" +
                "☼ xxx☼" +
                "☼    ☼");

        game.tick();

        // then
        assertE("☼    ☼" +
                "☼    ☼" +
                "☼    ☼" +
                "☼    ☼" +
                "☼0 + ☼" +
                "☼    ☼");
    }

    @Test
    public void shouldBombDestroyHeroAndResurrectionHero() {
        // given
        givenFl("☼    ☼" +
                "☼    ☼" +
                "☼    ☼" +
                "☼    ☼" +
                "☼  ☺ ☼" +
                "☼    ☼");

        diceNew(0, 2);
        game.tick();
        game.tick();
        game.tick();
        game.tick();

        // then
        assertE("☼    ☼" +
                "☼0 ♣ ☼" +
                "☼    ☼" +
                "☼    ☼" +
                "☼  ☺ ☼" +
                "☼    ☼");

        game.tick();
        game.tick();

        // then
        assertE("☼    ☼" +
                "☼    ☼" +
                "☼ xxx☼" +
                "☼0xxx☼" +
                "☼ xxx☼" +
                "☼    ☼");

        game.tick();

        // then
        assertE("☼    ☼" +
                "☼    ☼" +
                "☼    ☼" +
                "☼    ☼" +
                "☼0 + ☼" +
                "☼    ☼");

        game.tick();

        // then
        assertE("☼    ☼" +
                "☼    ☼" +
                "☼    ☼" +
                "☼    ☼" +
                "☼  ☺ ☼" +
                "☼0   ☼");
    }

    @Test
    public void shouldBombDestroyHeroRightAndResurrectHero() {
        // given
        givenFl("☼    ☼" +
                "☼    ☼" +
                "☼    ☼" +
                "☼    ☼" +
                "☼  ☺ ☼" +
                "☼    ☼");

        diceNew(0, 3);

        game.tick();
        game.tick();
        game.tick();
        game.tick();

        // then
        assertE("☼    ☼" +
                "☼0  ♣☼" +
                "☼    ☼" +
                "☼    ☼" +
                "☼  ☺ ☼" +
                "☼    ☼");

        game.tick();
        game.tick();

        // then
        assertE("☼    ☼" +
                "☼    ☼" +
                "☼  xxx" +
                "☼0 xxx" +
                "☼  xxx" +
                "☼    ☼");

        game.tick();

        // then
        assertE("☼    ☼" +
                "☼    ☼" +
                "☼    ☼" +
                "☼    ☼" +
                "☼0 + ☼" +
                "☼    ☼");
        game.tick();

        // then
        assertE("☼    ☼" +
                "☼    ☼" +
                "☼    ☼" +
                "☼    ☼" +
                "☼  ☺ ☼" +
                "☼0   ☼");
    }

    @Test
    public void shouldStoneDestroyHero() {
        // given
        givenFl("☼    ☼" +
                "☼    ☼" +
                "☼    ☼" +
                "☼    ☼" +
                "☼  ☺ ☼" +
                "☼    ☼");

        diceNew(2);
        game.tick();
        game.tick();
        game.tick();
        game.tick();

        // then
        assertE("☼    ☼" +
                "☼  0 ☼" +
                "☼    ☼" +
                "☼    ☼" +
                "☼  ☺ ☼" +
                "☼    ☼");

        game.tick();
        game.tick();

        // then
        assertE("☼    ☼" +
                "☼    ☼" +
                "☼    ☼" +
                "☼  0 ☼" +
                "☼  ☺ ☼" +
                "☼    ☼");

        game.tick();

        // then
        assertE("☼    ☼" +
                "☼    ☼" +
                "☼    ☼" +
                "☼    ☼" +
                "☼  + ☼" +
                "☼    ☼");
    }

    @Test
    public void shouldBulletChargerOnField() {
        // given
        givenFl("☼    ☼" +
                "☼    ☼" +
                "☼    ☼" +
                "☼    ☼" +
                "☼  ☺ ☼" +
                "☼    ☼");

        diceNew(-1, -1, 1, 1);
        game.tick();
        game.tick();

        // then
        assertE("☼ 7  ☼" +
                "☼    ☼" +
                "☼    ☼" +
                "☼    ☼" +
                "☼  ☺ ☼" +
                "☼    ☼");

    }

    @Test
    public void shouldHeroPickUpBulletPack() {
        // given
        givenFl("☼    ☼" +
                "☼    ☼" +
                "☼ ☺  ☼" +
                "☼    ☼" +
                "☼    ☼" +
                "☼    ☼");

        diceNew(-1, -1, 1, 1);
        game.tick();
        game.tick();
        game.tick();
        game.tick();

        // then
        assertE("☼ 7  ☼" +
                "☼    ☼" +
                "☼ ☺  ☼" +
                "☼    ☼" +
                "☼    ☼" +
                "☼    ☼");

        hero.up();
        game.tick();

        assertE("☼ 7  ☼" +
                "☼ ☺  ☼" +
                "☼    ☼" +
                "☼    ☼" +
                "☼    ☼" +
                "☼    ☼");

        hero.up();
        game.tick();

        assertE("☼ ☺  ☼" +
                "☼    ☼" +
                "☼    ☼" +
                "☼    ☼" +
                "☼    ☼" +
                "☼    ☼");
    }

    @Test
    public void shouldNewBulletPackAfterHeroGetOldBulletPack() {
        // given
        givenFl("☼    ☼" +
                "☼ ☺  ☼" +
                "☼    ☼" +
                "☼    ☼" +
                "☼    ☼" +
                "☼    ☼");

        diceNew(-1, -1, 0, 1);
        game.tick();

        // then
        assertE("☼7   ☼" +
                "☼ ☺  ☼" +
                "☼    ☼" +
                "☼    ☼" +
                "☼    ☼" +
                "☼    ☼");

        hero.left();
        game.tick();

        // then
        assertE("☼7   ☼" +
                "☼☺   ☼" +
                "☼    ☼" +
                "☼    ☼" +
                "☼    ☼" +
                "☼    ☼");

        hero.up();
        game.tick();
        diceNew(-1, -1, 3, 0);
        game.tick();

        // then
        assertE("☼☺   ☼" +
                "☼   7☼" +
                "☼    ☼" +
                "☼    ☼" +
                "☼    ☼" +
                "☼    ☼");
    }

    @Test
    public void shouldHeroShootAfterRecharge() {
        //Given
        givenFl("☼   ☼" +
                "☼ ☺ ☼" +
                "☼   ☼" +
                "☼   ☼" +
                "☼   ☼");

        //when
        diceNew(-1, -1, 0, 0);
        game.tick();

        //Given
        assertE("☼   ☼" +
                "☼7☺ ☼" +
                "☼   ☼" +
                "☼   ☼" +
                "☼   ☼");

        //when
        hero.act();
        game.tick();

        //Given
        assertE("☼   ☼" +
                "☼7☺ ☼" +
                "☼   ☼" +
                "☼   ☼" +
                "☼   ☼");

        hero.left();
        game.tick();

        //Given
        assertE("☼   ☼" +
                "☼☺  ☼" +
                "☼   ☼" +
                "☼   ☼" +
                "☼   ☼");

      //When
        hero.act();
        game.tick();

        //Then
        assertE("☼*  ☼" +
                "☼☺  ☼" +
                "☼   ☼" +
                "☼   ☼" +
                "☼   ☼");
    }
}



    // появление на поле магазина патронов
    // итераторы
    // инструкция
    // золото/здоровье
    // лазер
    // выстрелы не каждый тик (?)
    // плюшка, которая позволяет стрелать каждый тик, пока она действует (время или количество снарядов)
    // узнать причину загадочного ексепшна при вылете игры и исправить
    // написать нормального бота

/*
    некорорые тесты сломались из-за акт(когда есть возможность выстрела - сказать, что нет патронов)
    если есть пули - более агрессивная политика
    если падает сверху мина - считать, куда уклоняться (сейчас вправо)
    как можно меньше времени проводить в самых верхних рядах
    уклонения от пуль
    стрелять в других игроков
    потестить все позиции мин рядом
    если меня убивают - вылетает эксепшин (вроде пофиксил)
    проверить баг полета навстречу камню
    вынести в настройки кол-во пэков
    переделать дайс и все тесты
    рефакторинг!!!!

    */

