package com.codenjoy.dojo.loderunner.model;

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


import com.codenjoy.dojo.loderunner.TestSettings;
import com.codenjoy.dojo.loderunner.model.items.Brick;
import com.codenjoy.dojo.loderunner.model.items.Pill;
import com.codenjoy.dojo.loderunner.model.items.enemy.EnemyAI;
import com.codenjoy.dojo.loderunner.model.levels.LevelImpl;
import com.codenjoy.dojo.loderunner.services.Events;
import com.codenjoy.dojo.loderunner.services.GameSettings;
import com.codenjoy.dojo.services.*;
import com.codenjoy.dojo.services.joystick.DirectionActJoystick;
import com.codenjoy.dojo.services.printer.PrinterFactory;
import com.codenjoy.dojo.services.printer.PrinterFactoryImpl;
import com.codenjoy.dojo.utils.TestUtils;
import com.codenjoy.dojo.utils.events.EventsListenersAssert;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.stubbing.OngoingStubbing;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import static com.codenjoy.dojo.loderunner.services.GameSettings.Keys.*;
import static com.codenjoy.dojo.services.PointImpl.pt;
import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;

public class GameTest {

    private Loderunner game;
    private Hero hero;
    private List<Hero> heroes = new LinkedList<>();
    private List<Player> players = new LinkedList<>();
    private Dice dice;
    private EventListener listener;
    private Player player;
    private EnemyAI ai;
    private Joystick enemy;
    private PrinterFactory printer;
    private GameSettings settings;
    protected EventsListenersAssert events;

    @Before
    public void setup() {
        dice = mock(Dice.class);
        ai = mock(EnemyAI.class);
        printer = new PrinterFactoryImpl();
        enemy = new EnemyJoystick();
        settings = new TestSettings();
        events = new EventsListenersAssert(() -> Arrays.asList(listener), Events.class);
        Brick.DRILL_TIMER = 13;
    }

    @After
    public void tearDown() {
        events.verifyNoEvents();
    }

    private void dice(int... ints) {
        OngoingStubbing<Integer> when = when(dice.next(anyInt()));
        for (int i : ints) {
            when = when.thenReturn(i);
        }
    }

    private void givenFl(String board) {
        LevelImpl level = getLevel(board, settings, dice);
        level.setAI(ai);

        if (level.getHeroes().isEmpty()) {
            throw new IllegalStateException("Нет героя!");
        }

        game = new Loderunner(level, dice, settings);
        listener = mock(EventListener.class);

        for (Hero hero : level.getHeroes()) {
            dice(hero.getX(), hero.getY());
            Player player = new Player(listener, settings);
            players.add(player);
            game.newGame(player);
            heroes.add(player.getHero());
            player.getHero().direction = hero.direction;
        }
        this.hero = heroes.get(0);
        this.player = players.get(0);
        dice(0); // всегда дальше выбираем нулевой индекс
    }

    public static LevelImpl getLevel(String board, GameSettings settings, Dice dice) {
        LevelImpl level = new LevelImpl(board, dice);
        settings.integer(GOLD_COUNT_YELLOW, level.getYellowGold().size())
                .integer(GOLD_COUNT_RED, level.getRedGold().size())
                .integer(GOLD_COUNT_GREEN, level.getGreenGold().size())
                .integer(PORTALS_COUNT, level.getPortals().size())
                .integer(ENEMIES_COUNT, level.getEnemies().size())
                .integer(SHADOW_PILLS_COUNT, level.getPills().size());
        return level;
    }

    private Hero hero(int index) {
        return heroes.get(index);
    }

    private void assertE(String expected) {
        assertEquals(TestUtils.injectN(expected),
                printer.getPrinter(game.reader(), player).print());
    }

    // есть карта со мной
    @Test
    public void shouldFieldAtStart() {
        givenFl("☼☼☼☼☼" +
                "☼   ☼" +
                "☼ ◄ ☼" +
                "☼###☼" +
                "☼☼☼☼☼");

        assertE("☼☼☼☼☼" +
                "☼   ☼" +
                "☼ ◄ ☼" +
                "☼###☼" +
                "☼☼☼☼☼");
    }

    // я могу сверлить дырки
    @Test
    public void shouldDrillLeft() {
        givenFl("☼☼☼☼☼" +
                "☼   ☼" +
                "☼ ◄ ☼" +
                "☼###☼" +
                "☼☼☼☼☼");

        hero.act();
        hero.left();
        game.tick();

        assertE("☼☼☼☼☼" +
                "☼   ☼" +
                "☼ Я ☼" +
                "☼*##☼" +
                "☼☼☼☼☼");

        game.tick();

        assertE("☼☼☼☼☼" +
                "☼   ☼" +
                "☼ Я ☼" +
                "☼ ##☼" +
                "☼☼☼☼☼");
    }

    @Test
    public void shouldDrillCounter() {
        shouldDrillLeft();

        game.tick();
        game.tick();
        game.tick();
        game.tick();
        game.tick();
        game.tick();

        assertE("☼☼☼☼☼" +
                "☼   ☼" +
                "☼ Я ☼" +
                "☼ ##☼" +
                "☼☼☼☼☼");

        game.tick();

        assertE("☼☼☼☼☼" +
                "☼   ☼" +
                "☼ Я ☼" +
                "☼4##☼" +
                "☼☼☼☼☼");

        game.tick();

        assertE("☼☼☼☼☼" +
                "☼   ☼" +
                "☼ Я ☼" +
                "☼3##☼" +
                "☼☼☼☼☼");

        game.tick();

        assertE("☼☼☼☼☼" +
                "☼   ☼" +
                "☼ Я ☼" +
                "☼2##☼" +
                "☼☼☼☼☼");

        game.tick();

        assertE("☼☼☼☼☼" +
                "☼   ☼" +
                "☼ Я ☼" +
                "☼1##☼" +
                "☼☼☼☼☼");

        game.tick();

        assertE("☼☼☼☼☼" +
                "☼   ☼" +
                "☼ Я ☼" +
                "☼###☼" +
                "☼☼☼☼☼");
    }

    @Test
    public void shouldDrillRight() {
        givenFl("☼☼☼☼☼" +
                "☼   ☼" +
                "☼ ◄ ☼" +
                "☼###☼" +
                "☼☼☼☼☼");

        hero.act();
        hero.right();
        game.tick();

        assertE("☼☼☼☼☼" +
                "☼   ☼" +
                "☼ R ☼" +
                "☼##*☼" +
                "☼☼☼☼☼");

        game.tick();

        assertE("☼☼☼☼☼" +
                "☼   ☼" +
                "☼ R ☼" +
                "☼## ☼" +
                "☼☼☼☼☼");
    }

    // я могу ходить влево и вправо
    @Test
    public void shouldMoveLeft() {
        givenFl("☼☼☼☼☼" +
                "☼   ☼" +
                "☼ ◄ ☼" +
                "☼###☼" +
                "☼☼☼☼☼");

        hero.left();
        game.tick();

        assertE("☼☼☼☼☼" +
                "☼   ☼" +
                "☼◄  ☼" +
                "☼###☼" +
                "☼☼☼☼☼");
    }

    @Test
    public void shouldMoveRight() {
        givenFl("☼☼☼☼☼" +
                "☼   ☼" +
                "☼ ◄ ☼" +
                "☼###☼" +
                "☼☼☼☼☼");

        hero.right();
        game.tick();

        assertE("☼☼☼☼☼" +
                "☼   ☼" +
                "☼  ►☼" +
                "☼###☼" +
                "☼☼☼☼☼");
    }

    // если небыло команды я никуда не иду
    @Test
    public void shouldStopWhenNoMoreRightCommand() {
        shouldMoveRight();

        game.tick();

        assertE("☼☼☼☼☼" +
                "☼   ☼" +
                "☼  ►☼" +
                "☼###☼" +
                "☼☼☼☼☼");
    }

    // я останавливаюсь возле границы
    @Test
    public void shouldStopWhenWallRight() {
        shouldMoveRight();

        hero.right();
        game.tick();

        assertE("☼☼☼☼☼" +
                "☼   ☼" +
                "☼  ►☼" +
                "☼###☼" +
                "☼☼☼☼☼");
    }

    @Test
    public void shouldStopWhenWallLeft() {
        shouldMoveLeft();

        hero.left();
        game.tick();

        assertE("☼☼☼☼☼" +
                "☼   ☼" +
                "☼◄  ☼" +
                "☼###☼" +
                "☼☼☼☼☼");
    }

    // яма заростает со временем
    @Test
    public void shouldDrillFilled() {
        shouldDrillLeft();

        game.tick();

        assertE("☼☼☼☼☼" +
                "☼   ☼" +
                "☼ Я ☼" +
                "☼ ##☼" +
                "☼☼☼☼☼");

        game.tick();
        game.tick();
        game.tick();
        game.tick();
        game.tick();
        game.tick();

        assertE("☼☼☼☼☼" +
                "☼   ☼" +
                "☼ Я ☼" +
                "☼4##☼" +
                "☼☼☼☼☼");

        game.tick();

        assertE("☼☼☼☼☼" +
                "☼   ☼" +
                "☼ Я ☼" +
                "☼3##☼" +
                "☼☼☼☼☼");

        game.tick();

        assertE("☼☼☼☼☼" +
                "☼   ☼" +
                "☼ Я ☼" +
                "☼2##☼" +
                "☼☼☼☼☼");

        game.tick();

        assertE("☼☼☼☼☼" +
                "☼   ☼" +
                "☼ Я ☼" +
                "☼1##☼" +
                "☼☼☼☼☼");

        game.tick();

        assertE("☼☼☼☼☼" +
                "☼   ☼" +
                "☼ Я ☼" +
                "☼###☼" +
                "☼☼☼☼☼");
    }

    // В просверленную яму я легко могу упасть
    @Test
    public void shouldFallInPitLeft() {
        shouldDrillLeft();

        assertE("☼☼☼☼☼" +
                "☼   ☼" +
                "☼ Я ☼" +
                "☼ ##☼" +
                "☼☼☼☼☼");

        hero.left();
        game.tick();

        assertE("☼☼☼☼☼" +
                "☼   ☼" +
                "☼]  ☼" +
                "☼ ##☼" +
                "☼☼☼☼☼");

        game.tick();

        assertE("☼☼☼☼☼" +
                "☼   ☼" +
                "☼   ☼" +
                "☼◄##☼" +
                "☼☼☼☼☼");
    }

    @Test
    public void shouldFallInPitRight() {
        shouldDrillRight();

        assertE("☼☼☼☼☼" +
                "☼   ☼" +
                "☼ R ☼" +
                "☼## ☼" +
                "☼☼☼☼☼");

        hero.right();
        game.tick();

        assertE("☼☼☼☼☼" +
                "☼   ☼" +
                "☼  [☼" +
                "☼## ☼" +
                "☼☼☼☼☼");

        game.tick();

        assertE("☼☼☼☼☼" +
                "☼   ☼" +
                "☼   ☼" +
                "☼##►☼" +
                "☼☼☼☼☼");
    }

    // я если упал то не могу передвигаться влево и вправо поскольку мне мешают стены
    @Test
    public void shouldCantGoLeftIfWall() {
        shouldFallInPitRight();

        hero.left();
        game.tick();

        assertE("☼☼☼☼☼" +
                "☼   ☼" +
                "☼   ☼" +
                "☼##◄☼" +
                "☼☼☼☼☼");
    }

    @Test
    public void shouldCantGoRightIfWall() {
        shouldFallInPitLeft();

        hero.right();
        game.tick();

        assertE("☼☼☼☼☼" +
                "☼   ☼" +
                "☼   ☼" +
                "☼►##☼" +
                "☼☼☼☼☼");
    }

    // я если упал, то могу перемещаться влево и вправо, если мне не мешают стены
    @Test
    public void shouldСanGoInPitIfNoWall() {
        givenFl("☼☼☼☼☼" +
                "☼   ☼" +
                "☼ ◄ ☼" +
                "☼###☼" +
                "☼☼☼☼☼");

        hero.act();
        game.tick();

        assertE("☼☼☼☼☼" +
                "☼   ☼" +
                "☼ Я ☼" +
                "☼*##☼" +
                "☼☼☼☼☼");

        hero.right();
        game.tick();

        assertE("☼☼☼☼☼" +
                "☼   ☼" +
                "☼  ►☼" +
                "☼ ##☼" +
                "☼☼☼☼☼");

        hero.left();
        hero.act();
        game.tick();

        assertE("☼☼☼☼☼" +
                "☼   ☼" +
                "☼  Я☼" +
                "☼ *#☼" +
                "☼☼☼☼☼");

        hero.left();
        game.tick();

        assertE("☼☼☼☼☼" +
                "☼   ☼" +
                "☼ ] ☼" +
                "☼  #☼" +
                "☼☼☼☼☼");

        hero.left();  // при падении я не могу передвигаться
        game.tick();

        assertE("☼☼☼☼☼" +
                "☼   ☼" +
                "☼   ☼" +
                "☼ ◄#☼" +
                "☼☼☼☼☼");

        hero.left(); // а вот в яме куда угодно, пока есть место
        game.tick();

        assertE("☼☼☼☼☼" +
                "☼   ☼" +
                "☼   ☼" +
                "☼◄ #☼" +
                "☼☼☼☼☼");

        hero.right(); // пока стенки не заростут
        game.tick();

        assertE("☼☼☼☼☼" +
                "☼   ☼" +
                "☼   ☼" +
                "☼ ►#☼" +
                "☼☼☼☼☼");

        hero.right();
        game.tick();
        game.tick();

        assertE("☼☼☼☼☼" +
                "☼   ☼" +
                "☼   ☼" +
                "☼4►#☼" +
                "☼☼☼☼☼");

        hero.left();
        game.tick();

        assertE("☼☼☼☼☼" +
                "☼   ☼" +
                "☼   ☼" +
                "☼◄ #☼" +
                "☼☼☼☼☼");

        hero.right();
        game.tick();

        assertE("☼☼☼☼☼" +
                "☼   ☼" +
                "☼   ☼" +
                "☼2►#☼" +
                "☼☼☼☼☼");

        hero.left();
        game.tick();

        assertE("☼☼☼☼☼" +
                "☼   ☼" +
                "☼   ☼" +
                "☼◄3#☼" +
                "☼☼☼☼☼");

        hero.right();
        game.tick();

        assertE("☼☼☼☼☼" +
                "☼   ☼" +
                "☼   ☼" +
                "☼#►#☼" +
                "☼☼☼☼☼");

    }

    // если стенка замуровывается вместе со мной, то я умираю и появляюсь в рендомном месте
    @Test
    public void shouldIDieIPitFillWithMe() {
        shouldDrillLeft();

        game.tick();
        game.tick();
        game.tick();
        game.tick();
        game.tick();
        game.tick();
        game.tick();
        game.tick();
        game.tick();

        assertE("☼☼☼☼☼" +
                "☼   ☼" +
                "☼ Я ☼" +
                "☼2##☼" +
                "☼☼☼☼☼");

        hero.left();
        game.tick();

        assertE("☼☼☼☼☼" +
                "☼   ☼" +
                "☼]  ☼" +
                "☼1##☼" +
                "☼☼☼☼☼");

        game.tick();

        assertE("☼☼☼☼☼" +
                "☼   ☼" +
                "☼   ☼" +
                "☼Ѡ##☼" +
                "☼☼☼☼☼");

        events.verifyAllEvents("[KILL_HERO]");

        dice(2, 3);
        game.tick();         // ну а после смерти он появляется в рендомном месте
        game.newGame(player);

        assertE("☼☼☼☼☼" +
                "☼ [ ☼" +
                "☼   ☼" +
                "☼###☼" +
                "☼☼☼☼☼");
    }

    @Test
    public void shouldIDieIPitFillWithMe2() {
        shouldDrillLeft();

        game.tick();
        game.tick();
        game.tick();
        game.tick();
        game.tick();
        game.tick();
        game.tick();
        game.tick();

        assertE("☼☼☼☼☼" +
                "☼   ☼" +
                "☼ Я ☼" +
                "☼3##☼" +
                "☼☼☼☼☼");

        hero.left();
        game.tick();

        assertE("☼☼☼☼☼" +
                "☼   ☼" +
                "☼]  ☼" +
                "☼2##☼" +
                "☼☼☼☼☼");

        game.tick();

        assertE("☼☼☼☼☼" +
                "☼   ☼" +
                "☼   ☼" +
                "☼◄##☼" +
                "☼☼☼☼☼");

        game.tick();

        assertE("☼☼☼☼☼" +
                "☼   ☼" +
                "☼   ☼" +
                "☼Ѡ##☼" +
                "☼☼☼☼☼");

        events.verifyAllEvents("[KILL_HERO]");

        dice(2, 3);
        game.tick();         // ну а после смерти он появляется в рендомном месте
        game.newGame(player);

        assertE("☼☼☼☼☼" +
                "☼ [ ☼" +
                "☼   ☼" +
                "☼###☼" +
                "☼☼☼☼☼");

    }

    // я не могу сверлить уже отсверленную дырку, я даже не делаю вид что пытался
    @Test
    public void shouldCantDrillPit() {
        shouldDrillLeft();

        hero.right();
        game.tick();
        hero.left();
        game.tick();

        assertE("☼☼☼☼☼" +
                "☼   ☼" +
                "☼ ◄ ☼" +
                "☼ ##☼" +
                "☼☼☼☼☼");

        hero.left();
        hero.act();
        game.tick();

        assertE("☼☼☼☼☼" +
                "☼   ☼" +
                "☼ ◄ ☼" +
                "☼ ##☼" +
                "☼☼☼☼☼");
    }

    // выполнения команд left + act не зависят от порядка - если они сделаны в одном тике, то будет дырка слева без перемещения
    @Test
    public void shouldDrillLeft_otherCommandSequence() {
        givenFl("☼☼☼☼☼" +
                "☼   ☼" +
                "☼ ◄ ☼" +
                "☼###☼" +
                "☼☼☼☼☼");

//        hero.act();
        hero.left();
        hero.act();

        game.tick();

        assertE("☼☼☼☼☼" +
                "☼   ☼" +
                "☼ Я ☼" +
                "☼*##☼" +
                "☼☼☼☼☼");

        game.tick();

        assertE("☼☼☼☼☼" +
                "☼   ☼" +
                "☼ Я ☼" +
                "☼ ##☼" +
                "☼☼☼☼☼");
    }

    // если я повернут в какую-то сторону и просто нажимаю сверлить то будет с той стороны дырка
    @Test
    public void shouldDrillLeft_onyActCommand() {
        givenFl("☼☼☼☼☼" +
                "☼   ☼" +
                "☼ ◄ ☼" +
                "☼###☼" +
                "☼☼☼☼☼");

        hero.act();
        game.tick();

        assertE("☼☼☼☼☼" +
                "☼   ☼" +
                "☼ Я ☼" +
                "☼*##☼" +
                "☼☼☼☼☼");

        game.tick();

        assertE("☼☼☼☼☼" +
                "☼   ☼" +
                "☼ Я ☼" +
                "☼ ##☼" +
                "☼☼☼☼☼");
    }

    @Test
    public void shouldDrillRight_onyActCommand() {
        givenFl("☼☼☼☼☼" +
                "☼   ☼" +
                "☼ ► ☼" +
                "☼###☼" +
                "☼☼☼☼☼");

        hero.act();
        game.tick();

        assertE("☼☼☼☼☼" +
                "☼   ☼" +
                "☼ R ☼" +
                "☼##*☼" +
                "☼☼☼☼☼");

        game.tick();

        assertE("☼☼☼☼☼" +
                "☼   ☼" +
                "☼ R ☼" +
                "☼## ☼" +
                "☼☼☼☼☼");
    }

    // на карте появляется золото, если я его беру то получаю +
    @Test
    public void shouldGoldOnMap_iCanGetIt() {
        givenFl("☼☼☼☼☼" +
                "☼   ☼" +
                "☼ ►$☼" +
                "☼###☼" +
                "☼☼☼☼☼");

        dice(2, 3);
        hero.right();
        game.tick();
        events.verifyAllEvents("[GET_YELLOW_GOLD]");

        assertE("☼☼☼☼☼" +
                "☼ $ ☼" +
                "☼  ►☼" +
                "☼###☼" +
                "☼☼☼☼☼");

        hero.left();
        game.tick();

        assertE("☼☼☼☼☼" +
                "☼ $ ☼" +
                "☼ ◄ ☼" +
                "☼###☼" +
                "☼☼☼☼☼");
    }

    // проверить, что если новому обекту не где появится то программа не зависает - там бесконечный цикл потенциальный есть
    @Test(timeout = 1000)
    public void shouldNoDeadLoopWhenNewObjectCreation() {
        givenFl("☼☼☼☼☼" +
                "☼   ☼" +
                "☼ ►$☼" +
                "☼###☼" +
                "☼☼☼☼☼");

        dice(3, 3);
        hero.right();
        game.tick();
        events.verifyAllEvents("[GET_YELLOW_GOLD]");

        assertE("☼☼☼☼☼" +
                "☼  $☼" +
                "☼  ►☼" +
                "☼###☼" +
                "☼☼☼☼☼");
    }

    // на карте появляются лестницы, я могу зайти на нее и выйти обратно
    @Test
    public void shouldICanGoOnLadder() {
        givenFl("☼☼☼☼☼" +
                "☼  H☼" +
                "☼  H☼" +
                "☼ ►H☼" +
                "☼☼☼☼☼");

        hero.right();
        game.tick();

        assertE("☼☼☼☼☼" +
                "☼  H☼" +
                "☼  H☼" +
                "☼  Y☼" +
                "☼☼☼☼☼");

        hero.left();
        game.tick();

        assertE("☼☼☼☼☼" +
                "☼  H☼" +
                "☼  H☼" +
                "☼ ◄H☼" +
                "☼☼☼☼☼");
    }

    // я могу карабкаться по лестнице вверх
    @Test
    public void shouldICanGoOnLadderUp() {
        givenFl("☼☼☼☼☼" +
                "☼  H☼" +
                "☼  H☼" +
                "☼ ►H☼" +
                "☼☼☼☼☼");

        hero.right();
        game.tick();

        assertE("☼☼☼☼☼" +
                "☼  H☼" +
                "☼  H☼" +
                "☼  Y☼" +
                "☼☼☼☼☼");

        hero.up();
        game.tick();

        assertE("☼☼☼☼☼" +
                "☼  H☼" +
                "☼  Y☼" +
                "☼  H☼" +
                "☼☼☼☼☼");

        hero.up();
        game.tick();

        assertE("☼☼☼☼☼" +
                "☼  Y☼" +
                "☼  H☼" +
                "☼  H☼" +
                "☼☼☼☼☼");
    }

    // я не могу вылезти с лестницей за границы
    @Test
    public void shouldICantGoOnBarrierFromLadder() {
        shouldICanGoOnLadderUp();

        assertE("☼☼☼☼☼" +
                "☼  Y☼" +
                "☼  H☼" +
                "☼  H☼" +
                "☼☼☼☼☼");

        hero.up();
        game.tick();

        assertE("☼☼☼☼☼" +
                "☼  Y☼" +
                "☼  H☼" +
                "☼  H☼" +
                "☼☼☼☼☼");

        hero.right();
        game.tick();

        assertE("☼☼☼☼☼" +
                "☼  Y☼" +
                "☼  H☼" +
                "☼  H☼" +
                "☼☼☼☼☼");
    }

    // я могу спустится вниз, но не дальше границы экрана
    @Test
    public void shouldICanGoOnLadderDown() {
        shouldICanGoOnLadderUp();

        assertE("☼☼☼☼☼" +
                "☼  Y☼" +
                "☼  H☼" +
                "☼  H☼" +
                "☼☼☼☼☼");

        hero.down();
        game.tick();

        assertE("☼☼☼☼☼" +
                "☼  H☼" +
                "☼  Y☼" +
                "☼  H☼" +
                "☼☼☼☼☼");

        hero.down();
        game.tick();

        assertE("☼☼☼☼☼" +
                "☼  H☼" +
                "☼  H☼" +
                "☼  Y☼" +
                "☼☼☼☼☼");

        hero.down();
        game.tick();

        assertE("☼☼☼☼☼" +
                "☼  H☼" +
                "☼  H☼" +
                "☼  Y☼" +
                "☼☼☼☼☼");
    }

    // я мошгу в любой момент спрыгнуть с лестницы и я буду падать до тех пор пока не наткнусь на препятствие
    @Test
    public void shouldICanFly() {
        shouldICanGoOnLadderUp();

        assertE("☼☼☼☼☼" +
                "☼  Y☼" +
                "☼  H☼" +
                "☼  H☼" +
                "☼☼☼☼☼");

        hero.left();
        game.tick();

        assertE("☼☼☼☼☼" +
                "☼ ]H☼" +
                "☼  H☼" +
                "☼  H☼" +
                "☼☼☼☼☼");

        game.tick();

        assertE("☼☼☼☼☼" +
                "☼  H☼" +
                "☼ ]H☼" +
                "☼  H☼" +
                "☼☼☼☼☼");

        game.tick();

        assertE("☼☼☼☼☼" +
                "☼  H☼" +
                "☼  H☼" +
                "☼ ◄H☼" +
                "☼☼☼☼☼");
    }

    // под стеной я не могу сверлить
    @Test
    public void shouldICantDrillUnderLadder() {
        givenFl("☼☼☼☼☼" +
                "☼  H☼" +
                "☼ ►H☼" +
                "☼###☼" +
                "☼☼☼☼☼");

        hero.act();
        game.tick();

        assertE("☼☼☼☼☼" +
                "☼  H☼" +
                "☼ ►H☼" +
                "☼###☼" +
                "☼☼☼☼☼");
    }

    // под золотом я не могу сверлить
    @Test
    public void shouldICantDrillUnderGold() {
        givenFl("☼☼☼☼☼" +
                "☼   ☼" +
                "☼ ►$☼" +
                "☼###☼" +
                "☼☼☼☼☼");

        hero.act();
        game.tick();

        assertE("☼☼☼☼☼" +
                "☼   ☼" +
                "☼ ►$☼" +
                "☼###☼" +
                "☼☼☼☼☼");
    }

    // я могу поднятся по лестнице и зайти на площадку
    @Test
    public void shouldICanGoFromLadderToArea() {
        givenFl("☼☼☼☼☼" +
                "☼H  ☼" +
                "☼H# ☼" +
                "☼H◄ ☼" +
                "☼☼☼☼☼");

        hero.left();
        game.tick();

        assertE("☼☼☼☼☼" +
                "☼H  ☼" +
                "☼H# ☼" +
                "☼Y  ☼" +
                "☼☼☼☼☼");

        hero.up();
        game.tick();

        assertE("☼☼☼☼☼" +
                "☼H  ☼" +
                "☼Y# ☼" +
                "☼H  ☼" +
                "☼☼☼☼☼");

        hero.up();
        game.tick();

        assertE("☼☼☼☼☼" +
                "☼Y  ☼" +
                "☼H# ☼" +
                "☼H  ☼" +
                "☼☼☼☼☼");

        hero.right();
        game.tick();

        assertE("☼☼☼☼☼" +
                "☼H► ☼" +
                "☼H# ☼" +
                "☼H  ☼" +
                "☼☼☼☼☼");

        // и упасть
        hero.right();
        game.tick();

        assertE("☼☼☼☼☼" +
                "☼H [☼" +
                "☼H# ☼" +
                "☼H  ☼" +
                "☼☼☼☼☼");

        game.tick();

        assertE("☼☼☼☼☼" +
                "☼H  ☼" +
                "☼H#[☼" +
                "☼H  ☼" +
                "☼☼☼☼☼");

        game.tick();

        assertE("☼☼☼☼☼" +
                "☼H  ☼" +
                "☼H# ☼" +
                "☼H ►☼" +
                "☼☼☼☼☼");

    }

    // я не могу сверлить бетон
    @Test
    public void shouldICantDrillWall() {
        givenFl("☼☼☼☼☼" +
                "☼   ☼" +
                "☼ ► ☼" +
                "☼☼☼☼☼" +
                "☼☼☼☼☼");

        hero.act();
        game.tick();

        assertE("☼☼☼☼☼" +
                "☼   ☼" +
                "☼ ► ☼" +
                "☼☼☼☼☼" +
                "☼☼☼☼☼");
    }

    // пока я падаю я не могу двигаться влево и справо, даже если там есть площадки
    @Test
    public void shouldICantMoveWhenFall() {
        givenFl("☼☼☼☼☼☼☼" +
                "☼  ►  ☼" +
                "☼     ☼" +
                "☼     ☼" +
                "☼     ☼" +
                "☼## ##☼" +
                "☼☼☼☼☼☼☼");

        hero.left();
        game.tick();

        assertE("☼☼☼☼☼☼☼" +
                "☼     ☼" +
                "☼  ]  ☼" +
                "☼     ☼" +
                "☼     ☼" +
                "☼## ##☼" +
                "☼☼☼☼☼☼☼");

        hero.right();
        game.tick();

        assertE("☼☼☼☼☼☼☼" +
                "☼     ☼" +
                "☼     ☼" +
                "☼  [  ☼" +
                "☼     ☼" +
                "☼## ##☼" +
                "☼☼☼☼☼☼☼");

        hero.left();
        game.tick();

        assertE("☼☼☼☼☼☼☼" +
                "☼     ☼" +
                "☼     ☼" +
                "☼     ☼" +
                "☼  ]  ☼" +
                "☼## ##☼" +
                "☼☼☼☼☼☼☼");

        hero.left();
        game.tick();

        assertE("☼☼☼☼☼☼☼" +
                "☼     ☼" +
                "☼     ☼" +
                "☼     ☼" +
                "☼     ☼" +
                "☼##◄##☼" +
                "☼☼☼☼☼☼☼");
    }

    // появляются на карте трубы, если я с площадки захожу на трубу то я ползу по ней
    @Test
    public void shouldIPipe() {
        givenFl("☼☼☼☼☼☼☼" +
                "☼     ☼" +
                "☼►~~~ ☼" +
                "☼#    ☼" +
                "☼     ☼" +
                "☼     ☼" +
                "☼☼☼☼☼☼☼");

        hero.right();
        game.tick();

        assertE("☼☼☼☼☼☼☼" +
                "☼     ☼" +
                "☼ }~~ ☼" +
                "☼#    ☼" +
                "☼     ☼" +
                "☼     ☼" +
                "☼☼☼☼☼☼☼");

        hero.right();
        game.tick();

        assertE("☼☼☼☼☼☼☼" +
                "☼     ☼" +
                "☼ ~}~ ☼" +
                "☼#    ☼" +
                "☼     ☼" +
                "☼     ☼" +
                "☼☼☼☼☼☼☼");

        hero.right();
        game.tick();

        assertE("☼☼☼☼☼☼☼" +
                "☼     ☼" +
                "☼ ~~} ☼" +
                "☼#    ☼" +
                "☼     ☼" +
                "☼     ☼" +
                "☼☼☼☼☼☼☼");
    }

    // с трубы я могу спрыгунть и тогда я буду падать до препятствия
    @Test
    public void shouldIFallFromPipe() {
        shouldIPipe();

        assertE("☼☼☼☼☼☼☼" +
                "☼     ☼" +
                "☼ ~~} ☼" +
                "☼#    ☼" +
                "☼     ☼" +
                "☼     ☼" +
                "☼☼☼☼☼☼☼");

        hero.right();
        game.tick();

        assertE("☼☼☼☼☼☼☼" +
                "☼     ☼" +
                "☼ ~~~[☼" +
                "☼#    ☼" +
                "☼     ☼" +
                "☼     ☼" +
                "☼☼☼☼☼☼☼");

        game.tick();

        assertE("☼☼☼☼☼☼☼" +
                "☼     ☼" +
                "☼ ~~~ ☼" +
                "☼#   [☼" +
                "☼     ☼" +
                "☼     ☼" +
                "☼☼☼☼☼☼☼");

        game.tick();

        assertE("☼☼☼☼☼☼☼" +
                "☼     ☼" +
                "☼ ~~~ ☼" +
                "☼#    ☼" +
                "☼    [☼" +
                "☼     ☼" +
                "☼☼☼☼☼☼☼");

        game.tick();

        assertE("☼☼☼☼☼☼☼" +
                "☼     ☼" +
                "☼ ~~~ ☼" +
                "☼#    ☼" +
                "☼     ☼" +
                "☼    ►☼" +
                "☼☼☼☼☼☼☼");
    }

    // если по дороге я встречаюсь с золотом, то я его захвачу
    @Test
    public void shouldIGetGoldWhenFallenFromPipe() {
        givenFl("☼☼☼☼☼☼☼" +
                "☼     ☼" +
                "☼ $~~◄☼" +
                "☼ $  #☼" +
                "☼ $   ☼" +
                "☼ $   ☼" +
                "☼☼☼☼☼☼☼");

        hero.left();
        game.tick();

        assertE("☼☼☼☼☼☼☼" +
                "☼     ☼" +
                "☼ $~{ ☼" +
                "☼ $  #☼" +
                "☼ $   ☼" +
                "☼ $   ☼" +
                "☼☼☼☼☼☼☼");

        hero.left();
        game.tick();

        assertE("☼☼☼☼☼☼☼" +
                "☼     ☼" +
                "☼ ${~ ☼" +
                "☼ $  #☼" +
                "☼ $   ☼" +
                "☼ $   ☼" +
                "☼☼☼☼☼☼☼");

        dice(1, 5);
        hero.left();
        game.tick();

        assertE("☼☼☼☼☼☼☼" +
                "☼$    ☼" +
                "☼ ]~~ ☼" +
                "☼ $  #☼" +
                "☼ $   ☼" +
                "☼ $   ☼" +
                "☼☼☼☼☼☼☼");

        events.verifyAllEvents("[GET_YELLOW_GOLD]");

        dice(2, 5);
        game.tick();

        assertE("☼☼☼☼☼☼☼" +
                "☼$$   ☼" +
                "☼  ~~ ☼" +
                "☼ ]  #☼" +
                "☼ $   ☼" +
                "☼ $   ☼" +
                "☼☼☼☼☼☼☼");

        events.verifyAllEvents("[GET_YELLOW_GOLD]");

        dice(3, 5);
        game.tick();

        assertE("☼☼☼☼☼☼☼" +
                "☼$$$  ☼" +
                "☼  ~~ ☼" +
                "☼    #☼" +
                "☼ ]   ☼" +
                "☼ $   ☼" +
                "☼☼☼☼☼☼☼");

        events.verifyAllEvents("[GET_YELLOW_GOLD]");

        dice(4, 5);
        game.tick();

        assertE("☼☼☼☼☼☼☼" +
                "☼$$$$ ☼" +
                "☼  ~~ ☼" +
                "☼    #☼" +
                "☼     ☼" +
                "☼ ◄   ☼" +
                "☼☼☼☼☼☼☼");

        events.verifyAllEvents("[GET_YELLOW_GOLD]");
    }

    // если я просверлил дырку и падаю в нее, а под ней ничего нет - то я падаю пока не найду препятствие
    @Test
    public void shouldIFallWhenUnderPitIsFree() {
        givenFl("☼☼☼☼☼☼☼" +
                "☼  ◄  ☼" +
                "☼#####☼" +
                "☼     ☼" +
                "☼     ☼" +
                "☼     ☼" +
                "☼☼☼☼☼☼☼");

        hero.act();
        game.tick();

        assertE("☼☼☼☼☼☼☼" +
                "☼  Я  ☼" +
                "☼#*###☼" +
                "☼     ☼" +
                "☼     ☼" +
                "☼     ☼" +
                "☼☼☼☼☼☼☼");

        hero.left();
        game.tick();

        assertE("☼☼☼☼☼☼☼" +
                "☼ ]   ☼" +
                "☼# ###☼" +
                "☼     ☼" +
                "☼     ☼" +
                "☼     ☼" +
                "☼☼☼☼☼☼☼");

        game.tick();

        assertE("☼☼☼☼☼☼☼" +
                "☼     ☼" +
                "☼#]###☼" +
                "☼     ☼" +
                "☼     ☼" +
                "☼     ☼" +
                "☼☼☼☼☼☼☼");

        game.tick();

        assertE("☼☼☼☼☼☼☼" +
                "☼     ☼" +
                "☼# ###☼" +
                "☼ ]   ☼" +
                "☼     ☼" +
                "☼     ☼" +
                "☼☼☼☼☼☼☼");

        game.tick();

        assertE("☼☼☼☼☼☼☼" +
                "☼     ☼" +
                "☼# ###☼" +
                "☼     ☼" +
                "☼ ]   ☼" +
                "☼     ☼" +
                "☼☼☼☼☼☼☼");

        game.tick();

        assertE("☼☼☼☼☼☼☼" +
                "☼     ☼" +
                "☼# ###☼" +
                "☼     ☼" +
                "☼     ☼" +
                "☼ ◄   ☼" +
                "☼☼☼☼☼☼☼");
    }

    // если в процессе падения я вдург наткнулся на трубу то я повисаю на ней
    @Test
    public void shouldIPipeWhenFall() {
        givenFl("☼☼☼☼☼☼☼" +
                "☼  ◄  ☼" +
                "☼# ###☼" +
                "☼     ☼" +
                "☼ ~~~ ☼" +
                "☼     ☼" +
                "☼☼☼☼☼☼☼");

        hero.left();
        game.tick();

        assertE("☼☼☼☼☼☼☼" +
                "☼ ]   ☼" +
                "☼# ###☼" +
                "☼     ☼" +
                "☼ ~~~ ☼" +
                "☼     ☼" +
                "☼☼☼☼☼☼☼");

        game.tick();

        assertE("☼☼☼☼☼☼☼" +
                "☼     ☼" +
                "☼#]###☼" +
                "☼     ☼" +
                "☼ ~~~ ☼" +
                "☼     ☼" +
                "☼☼☼☼☼☼☼");

        game.tick();

        assertE("☼☼☼☼☼☼☼" +
                "☼     ☼" +
                "☼# ###☼" +
                "☼ ]   ☼" +
                "☼ ~~~ ☼" +
                "☼     ☼" +
                "☼☼☼☼☼☼☼");

        game.tick();

        assertE("☼☼☼☼☼☼☼" +
                "☼     ☼" +
                "☼# ###☼" +
                "☼     ☼" +
                "☼ {~~ ☼" +
                "☼     ☼" +
                "☼☼☼☼☼☼☼");

        game.tick();

        assertE("☼☼☼☼☼☼☼" +
                "☼     ☼" +
                "☼# ###☼" +
                "☼     ☼" +
                "☼ {~~ ☼" +
                "☼     ☼" +
                "☼☼☼☼☼☼☼");
    }

    // TODO я могу просверлить дырку под лестницей, а потом спуститься туда

    // я не могу просверлить дырку под другим камнем
    @Test
    public void shouldICantDrillUnderBrick() {
        givenFl("☼☼☼☼☼" +
                "☼   ☼" +
                "☼ ►#☼" +
                "☼###☼" +
                "☼☼☼☼☼");

        hero.act();
        game.tick();

        assertE("☼☼☼☼☼" +
                "☼   ☼" +
                "☼ ►#☼" +
                "☼###☼" +
                "☼☼☼☼☼");
    }

    // я могу спрыгнуть с трубы
    @Test
    public void shouldCanJumpFromPipe() {
        givenFl("☼☼☼☼☼☼☼" +
                "☼  ◄  ☼" +
                "☼     ☼" +
                "☼ ~~~ ☼" +
                "☼     ☼" +
                "☼     ☼" +
                "☼☼☼☼☼☼☼");

        game.tick();

        assertE("☼☼☼☼☼☼☼" +
                "☼     ☼" +
                "☼  ]  ☼" +
                "☼ ~~~ ☼" +
                "☼     ☼" +
                "☼     ☼" +
                "☼☼☼☼☼☼☼");

        game.tick();

        assertE("☼☼☼☼☼☼☼" +
                "☼     ☼" +
                "☼     ☼" +
                "☼ ~{~ ☼" +
                "☼     ☼" +
                "☼     ☼" +
                "☼☼☼☼☼☼☼");

        hero.down();
        game.tick();

        assertE("☼☼☼☼☼☼☼" +
                "☼     ☼" +
                "☼     ☼" +
                "☼ ~~~ ☼" +
                "☼  ]  ☼" +
                "☼     ☼" +
                "☼☼☼☼☼☼☼");

        game.tick();

        assertE("☼☼☼☼☼☼☼" +
                "☼     ☼" +
                "☼     ☼" +
                "☼ ~~~ ☼" +
                "☼     ☼" +
                "☼  ◄  ☼" +
                "☼☼☼☼☼☼☼");
    }

    // бага: мне нельзя спускаться с лестницы в бетон, так же как и подниматься
    // плюс я должен иметь возможность спустится по лестнице
    @Test
    public void shouldCantWalkThroughWallDown() {
        givenFl("☼☼☼☼☼" +
                "☼ ◄ ☼" +
                "☼ H ☼" +
                "☼☼☼☼☼" +
                "☼☼☼☼☼");

        hero.down();
        game.tick();

        assertE("☼☼☼☼☼" +
                "☼   ☼" +
                "☼ Y ☼" +
                "☼☼☼☼☼" +
                "☼☼☼☼☼");

        hero.down();
        game.tick();

        assertE("☼☼☼☼☼" +
                "☼   ☼" +
                "☼ Y ☼" +
                "☼☼☼☼☼" +
                "☼☼☼☼☼");
    }

    @Test
    public void shouldCantWalkThroughWallUp() {
        givenFl("☼☼☼☼☼" +
                "☼☼☼☼☼" +
                "☼ H ☼" +
                "☼ H◄☼" +
                "☼☼☼☼☼");

        hero.left();
        game.tick();

        assertE("☼☼☼☼☼" +
                "☼☼☼☼☼" +
                "☼ H ☼" +
                "☼ Y ☼" +
                "☼☼☼☼☼");

        hero.up();
        game.tick();

        assertE("☼☼☼☼☼" +
                "☼☼☼☼☼" +
                "☼ Y ☼" +
                "☼ H ☼" +
                "☼☼☼☼☼");

        hero.up();
        game.tick();

        assertE("☼☼☼☼☼" +
                "☼☼☼☼☼" +
                "☼ Y ☼" +
                "☼ H ☼" +
                "☼☼☼☼☼");
    }

    // бага: мне нельзя проходить с лестницы через бетон направо или налево
    @Test
    public void shouldCantWalkThroughWallLeftRight() {
        givenFl("☼☼☼☼☼" +
                "☼☼☼☼☼" +
                "☼☼H☼☼" +
                "☼ H◄☼" +
                "☼☼☼☼☼");

        hero.left();
        game.tick();

        assertE("☼☼☼☼☼" +
                "☼☼☼☼☼" +
                "☼☼H☼☼" +
                "☼ Y ☼" +
                "☼☼☼☼☼");

        hero.up();
        game.tick();

        assertE("☼☼☼☼☼" +
                "☼☼☼☼☼" +
                "☼☼Y☼☼" +
                "☼ H ☼" +
                "☼☼☼☼☼");

        hero.left();
        game.tick();

        assertE("☼☼☼☼☼" +
                "☼☼☼☼☼" +
                "☼☼Y☼☼" +
                "☼ H ☼" +
                "☼☼☼☼☼");

        hero.right();
        game.tick();

        assertE("☼☼☼☼☼" +
                "☼☼☼☼☼" +
                "☼☼Y☼☼" +
                "☼ H ☼" +
                "☼☼☼☼☼");
    }

    // бага: мне нельзя проходить через бетон
    @Test
    public void shouldCantWalkThroughWallLeftRight2() {
        givenFl("☼☼☼☼☼" +
                "☼   ☼" +
                "☼   ☼" +
                "☼☼◄☼☼" +
                "☼☼☼☼☼");

        hero.left();
        game.tick();

        assertE("☼☼☼☼☼" +
                "☼   ☼" +
                "☼   ☼" +
                "☼☼◄☼☼" +
                "☼☼☼☼☼");

        hero.right();
        game.tick();

        assertE("☼☼☼☼☼" +
                "☼   ☼" +
                "☼   ☼" +
                "☼☼►☼☼" +
                "☼☼☼☼☼");
    }

    // бага: мне нельзя спрыгивать с трубы что сразу над бетоном, протелая сквозь него
    @Test
    public void shouldCantJumpThroughWall() {
        givenFl("☼☼☼☼☼☼" +
                "☼  ◄ ☼" +
                "☼  ~ ☼" +
                "☼☼☼☼☼☼" +
                "☼    ☼" +
                "☼☼☼☼☼☼");

        game.tick();

        assertE("☼☼☼☼☼☼" +
                "☼    ☼" +
                "☼  { ☼" +
                "☼☼☼☼☼☼" +
                "☼    ☼" +
                "☼☼☼☼☼☼");

        hero.down();
        game.tick();

        assertE("☼☼☼☼☼☼" +
                "☼    ☼" +
                "☼  { ☼" +
                "☼☼☼☼☼☼" +
                "☼    ☼" +
                "☼☼☼☼☼☼");
    }

    @Test
    public void shouldBoardIsFree() {
        givenFl("☼☼☼☼☼☼" +
                "☼~◄$H☼" +
                "☼####☼" +
                "☼☼☼☼☼☼" +
                "☼☼☼☼☼☼" +
                "☼☼☼☼☼☼");

        for (int x = 0; x < game.size(); x++) {
            for (int y = 0; y < game.size(); y++) {
                Point pt = pt(x, y);
                assertEquals("At:" + pt, false, game.isFree(pt));
            }

        }
    }

    // Есть хитрый хак, когда спрыгиваешь с трубы, то можно при падении просверливать под собой
    // но она многоптточная
    @Test
    public void shouldDrillDown() {
        givenFl("☼☼☼☼☼☼" +
                "☼  ◄ ☼" +
                "☼  ~ ☼" +
                "☼    ☼" +
                "☼####☼" +
                "☼☼☼☼☼☼");

        game.tick();

        assertE("☼☼☼☼☼☼" +
                "☼    ☼" +
                "☼  { ☼" +
                "☼    ☼" +
                "☼####☼" +
                "☼☼☼☼☼☼");

        hero.down();
        hero.act();
        game.tick();

        assertE("☼☼☼☼☼☼" +
                "☼    ☼" +
                "☼  { ☼" +
                "☼    ☼" +
                "☼####☼" +
                "☼☼☼☼☼☼");

        hero.down();
        game.tick();

        assertE("☼☼☼☼☼☼" +
                "☼    ☼" +
                "☼  ~ ☼" +
                "☼  ◄ ☼" +
                "☼####☼" +
                "☼☼☼☼☼☼");

        hero.act();
        game.tick();

        assertE("☼☼☼☼☼☼" +
                "☼    ☼" +
                "☼  ~ ☼" +
                "☼  Я ☼" +
                "☼#*##☼" +
                "☼☼☼☼☼☼");
    }

    // если я сам себя закопаю, то получу ли я за это очки? не должен!
    @Test
    public void shouldNoScoreWhenKamikadze() {
        givenFl("☼☼☼☼" +
                "☼ ◄☼" +
                "☼##☼" +
                "☼☼☼☼");

        hero.act();
        game.tick();
        hero.left();
        game.tick();
        game.tick();

        assertE("☼☼☼☼" +
                "☼  ☼" +
                "☼◄#☼" +
                "☼☼☼☼");

        for (int c = 3; c < Brick.DRILL_TIMER; c++) {
            game.tick();
        }

        assertE("☼☼☼☼" +
                "☼  ☼" +
                "☼Ѡ#☼" +
                "☼☼☼☼");

        game.tick();

        assertE("☼☼☼☼" +
                "☼  ☼" +
                "☼Ѡ#☼" +
                "☼☼☼☼");

        events.verifyAllEvents("[KILL_HERO]");
    }

    // я могу сверлить стенки под стенками, если те разрушены
    @Test
    public void shouldDrillUnderDrilledBrick() {
        givenFl("☼☼☼☼☼☼" +
                "☼    ☼" +
                "☼ ◄  ☼" +
                "☼####☼" +
                "☼####☼" +
                "☼☼☼☼☼☼");

        hero.act();
        game.tick();

        hero.right();
        game.tick();

        hero.left();
        hero.act();
        game.tick();

        hero.right();
        game.tick();

        hero.left();
        hero.act();
        game.tick();

        assertE("☼☼☼☼☼☼" +
                "☼    ☼" +
                "☼   Я☼" +
                "☼  *#☼" +
                "☼####☼" +
                "☼☼☼☼☼☼");

        hero.left();
        game.tick();
        game.tick();

        hero.left();
        game.tick();

        assertE("☼☼☼☼☼☼" +
                "☼    ☼" +
                "☼    ☼" +
                "☼ ◄ #☼" +
                "☼####☼" +
                "☼☼☼☼☼☼");

        hero.right();
        hero.act();
        game.tick();

        hero.left();
        hero.act();
        game.tick();

        game.tick();

        assertE("☼☼☼☼☼☼" +
                "☼    ☼" +
                "☼    ☼" +
                "☼2Я #☼" +
                "☼ # #☼" +
                "☼☼☼☼☼☼");

        hero.left();
        game.tick();

        assertE("☼☼☼☼☼☼" +
                "☼    ☼" +
                "☼    ☼" +
                "☼]3 #☼" +
                "☼ # #☼" +
                "☼☼☼☼☼☼");

        hero.left();
        game.tick();

        assertE("☼☼☼☼☼☼" +
                "☼    ☼" +
                "☼    ☼" +
                "☼#24#☼" +
                "☼◄# #☼" +
                "☼☼☼☼☼☼");
    }

    // если я спрыгива с последней секции лестницы, то как-то неудачно этто делаю. Бага!
    @Test
    public void shouldJumpFromLadderDown() {
        givenFl("☼☼☼☼☼☼" +
                "☼ ◄  ☼" +
                "☼ H##☼" +
                "☼#H  ☼" +
                "☼    ☼" +
                "☼☼☼☼☼☼");

        hero.down();
        game.tick();

        hero.down();
        game.tick();

        assertE("☼☼☼☼☼☼" +
                "☼    ☼" +
                "☼ H##☼" +
                "☼#Y  ☼" +
                "☼    ☼" +
                "☼☼☼☼☼☼");

        game.tick();

        assertE("☼☼☼☼☼☼" +
                "☼    ☼" +
                "☼ H##☼" +
                "☼#Y  ☼" +
                "☼    ☼" +
                "☼☼☼☼☼☼");

        hero.right();
        game.tick();

        assertE("☼☼☼☼☼☼" +
                "☼    ☼" +
                "☼ H##☼" +
                "☼#H[ ☼" +
                "☼    ☼" +
                "☼☼☼☼☼☼");

        game.tick();

        assertE("☼☼☼☼☼☼" +
                "☼    ☼" +
                "☼ H##☼" +
                "☼#H  ☼" +
                "☼  ► ☼" +
                "☼☼☼☼☼☼");
    }

    private void ai(Direction value) {
        when(ai.getDirection(any(Field.class), any(), any())).thenReturn(value, null);
    }

    // чертик двигается так же как и обычный игрок - мжет ходить влево и вправо
    @Test
    public void shouldEnemyMoveLeft() {
        givenFl("☼☼☼☼►" +
                "☼   ☼" +
                "☼ « ☼" +
                "☼###☼" +
                "☼☼☼☼☼");

        enemy.left();
        game.tick();

        assertE("☼☼☼☼►" +
                "☼   ☼" +
                "☼«  ☼" +
                "☼###☼" +
                "☼☼☼☼☼");
    }

    @Test
    public void shouldEnemyMoveRight() {
        givenFl("☼☼☼☼►" +
                "☼   ☼" +
                "☼ « ☼" +
                "☼###☼" +
                "☼☼☼☼☼");

        enemy.right();
        game.tick();

        assertE("☼☼☼☼►" +
                "☼   ☼" +
                "☼  »☼" +
                "☼###☼" +
                "☼☼☼☼☼");
    }

    // если небыло команды чертик никуда не идет
    @Test
    public void shouldEnemyStopWhenNoMoreRightCommand() {
        givenFl("☼☼☼☼►" +
                "☼   ☼" +
                "☼  «☼" +
                "☼###☼" +
                "☼☼☼☼☼");

        enemy.left();
        game.tick();

        assertE("☼☼☼☼►" +
                "☼   ☼" +
                "☼ « ☼" +
                "☼###☼" +
                "☼☼☼☼☼");

        game.tick();

        assertE("☼☼☼☼►" +
                "☼   ☼" +
                "☼ « ☼" +
                "☼###☼" +
                "☼☼☼☼☼");
    }

    // Чертик останавливается возле границы
    @Test
    public void shouldEnemyStopWhenWallRight() {
        givenFl("☼☼☼☼►" +
                "☼   ☼" +
                "☼  »☼" +
                "☼###☼" +
                "☼☼☼☼☼");

        enemy.right();
        game.tick();

        assertE("☼☼☼☼►" +
                "☼   ☼" +
                "☼  »☼" +
                "☼###☼" +
                "☼☼☼☼☼");
    }

    @Test
    public void shouldEnemyStopWhenWallLeft() {
        givenFl("☼☼☼☼►" +
                "☼   ☼" +
                "☼«  ☼" +
                "☼###☼" +
                "☼☼☼☼☼");

        enemy.left();
        game.tick();

        assertE("☼☼☼☼►" +
                "☼   ☼" +
                "☼«  ☼" +
                "☼###☼" +
                "☼☼☼☼☼");
    }

    // В просверленную яму чертик легко может упасть
    @Test
    public void shouldEnemyFallInPitLeft() {
        givenFl("☼☼☼☼☼" +
                "☼   ☼" +
                "☼« ◄☼" +
                "☼###☼" +
                "☼☼☼☼☼");

        hero.act();
        game.tick();

        enemy.right();
        game.tick();

        assertE("☼☼☼☼☼" +
                "☼   ☼" +
                "☼ »Я☼" +
                "☼# #☼" +
                "☼☼☼☼☼");

        game.tick();

        assertE("☼☼☼☼☼" +
                "☼   ☼" +
                "☼  Я☼" +
                "☼#X#☼" +
                "☼☼☼☼☼");
    }

    @Test
    public void shouldEnemyFallInPitRight() {
        givenFl("☼☼☼☼☼" +
                "☼   ☼" +
                "☼◄ «☼" +
                "☼###☼" +
                "☼☼☼☼☼");

        hero.right();
        hero.act();
        game.tick();

        assertE("☼☼☼☼☼" +
                "☼   ☼" +
                "☼R «☼" +
                "☼#*#☼" +
                "☼☼☼☼☼");

        enemy.left();
        game.tick();

        assertE("☼☼☼☼☼" +
                "☼   ☼" +
                "☼R« ☼" +
                "☼# #☼" +
                "☼☼☼☼☼");

        game.tick();

        assertE("☼☼☼☼☼" +
                "☼   ☼" +
                "☼R  ☼" +
                "☼#X#☼" +
                "☼☼☼☼☼");
    }

    // при падении чертик не может передвигаться влево и вправо - ему мешают стены
    @Test
    public void shouldEnemyCantGoLeftIfWall() {
        shouldEnemyFallInPitRight();

        enemy.left();
        game.tick();

        assertE("☼☼☼☼☼" +
                "☼   ☼" +
                "☼R  ☼" +
                "☼#X#☼" +
                "☼☼☼☼☼");
    }

    @Test
    public void shouldEnemyCantGoRightIfWall() {
        shouldEnemyFallInPitLeft();

        enemy.right();
        game.tick();

        assertE("☼☼☼☼☼" +
                "☼   ☼" +
                "☼  Я☼" +
                "☼#X#☼" +
                "☼☼☼☼☼");
    }

    // монстр сидит в ямке некоторое количество тиков, потом он вылазит
    @Test
    public void shouldMonsterGetUpFromPit() {
        shouldEnemyFallInPitLeft();

        game.tick();
        game.tick();
        game.tick();
        game.tick();
        game.tick();
        game.tick();
        game.tick();
        game.tick();
        game.tick();
        game.tick();

        assertE("☼☼☼☼☼" +
                "☼   ☼" +
                "☼  Я☼" +
                "☼#X#☼" +
                "☼☼☼☼☼");

        game.tick();

        assertE("☼☼☼☼☼" +
                "☼   ☼" +
                "☼ »Я☼" +
                "☼###☼" +
                "☼☼☼☼☼");

        enemy.left(); // после этого он может двигаться
        game.tick();

        assertE("☼☼☼☼☼" +
                "☼   ☼" +
                "☼« Я☼" +
                "☼###☼" +
                "☼☼☼☼☼");
    }

    // если чертик попадает на героя - тот погибает
    @Test
    public void shouldHeroDieWhenMeetWithEnemy() {
        givenFl("☼☼☼☼☼" +
                "☼   ☼" +
                "☼◄ «☼" +
                "☼###☼" +
                "☼☼☼☼☼");

        hero.right();
        enemy.left();
        game.tick();

        assertE("☼☼☼☼☼" +
                "☼   ☼" +
                "☼ Ѡ ☼" +
                "☼###☼" +
                "☼☼☼☼☼");

        events.verifyAllEvents("[KILL_HERO]");

        dice(1, 3);
        game.tick();         // ну а после смерти он появляется в рендомном месте причем чертик остается на своем месте
        game.newGame(player);

        assertE("☼☼☼☼☼" +
                "☼[  ☼" +
                "☼ « ☼" +
                "☼###☼" +
                "☼☼☼☼☼");
    }

    // другой кейс, когда оба двигаются на встречу друг к другу
    @Test
    public void shouldHeroDieWhenMeetWithEnemy2() {
        givenFl("☼☼☼☼☼" +
                "☼   ☼" +
                "☼◄« ☼" +
                "☼###☼" +
                "☼☼☼☼☼");

        hero.right();
        enemy.left();
        game.tick();

        assertE("☼☼☼☼☼" +
                "☼   ☼" +
                "☼«Ѡ ☼" +
                "☼###☼" +
                "☼☼☼☼☼");

        events.verifyAllEvents("[KILL_HERO]");

        dice(0,  // охотимся за первым игроком
            3, 3);
        game.tick();         // ну а после смерти он появляется в рендомном месте причем чертик остается на своем месте
        game.newGame(player);

        assertE("☼☼☼☼☼" +
                "☼  [☼" +
                "☼«  ☼" +
                "☼###☼" +
                "☼☼☼☼☼");
    }

    // другой кейс, когда игрок идет на чертика
    @Test
    public void shouldHeroDieWhenMeetWithEnemy_whenHeroWalk() {
        givenFl("☼☼☼☼☼" +
                "☼   ☼" +
                "☼◄« ☼" +
                "☼###☼" +
                "☼☼☼☼☼");

        hero.right();
        game.tick();

        assertE("☼☼☼☼☼" +
                "☼   ☼" +
                "☼ Ѡ ☼" +
                "☼###☼" +
                "☼☼☼☼☼");

        events.verifyAllEvents("[KILL_HERO]");

        dice(3, 3);
        game.tick();         // ну а после смерти он появляется в рендомном месте причем чертик остается на своем месте
        game.newGame(player);

        assertE("☼☼☼☼☼" +
                "☼  [☼" +
                "☼ « ☼" +
                "☼###☼" +
                "☼☼☼☼☼");
    }

    // другой кейс, когда чертик идет на игрока
    @Test
    public void shouldHeroDieWhenMeetWithEnemy_whenEnemyWalk() {
        givenFl("☼☼☼☼☼" +
                "☼   ☼" +
                "☼◄« ☼" +
                "☼###☼" +
                "☼☼☼☼☼");

        enemy.left();
        game.tick();

        assertE("☼☼☼☼☼" +
                "☼   ☼" +
                "☼Ѡ  ☼" +
                "☼###☼" +
                "☼☼☼☼☼");

        events.verifyAllEvents("[KILL_HERO]");

        dice(0,  // охотимся за первым игроком
            3, 3);
        game.tick();         // ну а после смерти он появляется в рендомном месте причем чертик остается на своем месте
        game.newGame(player);

        assertE("☼☼☼☼☼" +
                "☼  [☼" +
                "☼«  ☼" +
                "☼###☼" +
                "☼☼☼☼☼");
    }

    // Чертик может зайти на лестницу и выйти обратно
    @Test
    public void shouldEnemyCanGoOnLadder() {
        givenFl("☼☼☼☼►" +
                "☼  H☼" +
                "☼  H☼" +
                "☼ «H☼" +
                "☼☼☼☼☼");

        enemy.right();
        game.tick();

        assertE("☼☼☼☼►" +
                "☼  H☼" +
                "☼  H☼" +
                "☼  Q☼" +
                "☼☼☼☼☼");

        enemy.left();
        game.tick();

        assertE("☼☼☼☼►" +
                "☼  H☼" +
                "☼  H☼" +
                "☼ «H☼" +
                "☼☼☼☼☼");
    }

    // Чертик может карабкаться по лестнице вверх
    @Test
    public void shouldEnemyCanGoOnLadderUp() {
        givenFl("☼☼☼☼►" +
                "☼  H☼" +
                "☼  H☼" +
                "☼ «H☼" +
                "☼☼☼☼☼");

        enemy.right();
        game.tick();

        assertE("☼☼☼☼►" +
                "☼  H☼" +
                "☼  H☼" +
                "☼  Q☼" +
                "☼☼☼☼☼");

        enemy.up();
        game.tick();

        assertE("☼☼☼☼►" +
                "☼  H☼" +
                "☼  Q☼" +
                "☼  H☼" +
                "☼☼☼☼☼");

        enemy.up();
        game.tick();

        assertE("☼☼☼☼►" +
                "☼  Q☼" +
                "☼  H☼" +
                "☼  H☼" +
                "☼☼☼☼☼");
    }

    // Чертик не может вылезти с лестницей за границы
    @Test
    public void shouldEnemyCantGoOnBarrierFromLadder() {
        shouldEnemyCanGoOnLadderUp();

        assertE("☼☼☼☼►" +
                "☼  Q☼" +
                "☼  H☼" +
                "☼  H☼" +
                "☼☼☼☼☼");

        enemy.up();
        game.tick();

        assertE("☼☼☼☼►" +
                "☼  Q☼" +
                "☼  H☼" +
                "☼  H☼" +
                "☼☼☼☼☼");

        enemy.right();
        game.tick();

        assertE("☼☼☼☼►" +
                "☼  Q☼" +
                "☼  H☼" +
                "☼  H☼" +
                "☼☼☼☼☼");
    }

    // Чертик может спустится вниз, но не дальше границы экрана
    @Test
    public void shouldEnemyCanGoOnLadderDown() {
        shouldEnemyCanGoOnLadderUp();

        assertE("☼☼☼☼►" +
                "☼  Q☼" +
                "☼  H☼" +
                "☼  H☼" +
                "☼☼☼☼☼");

        enemy.down();
        game.tick();

        assertE("☼☼☼☼►" +
                "☼  H☼" +
                "☼  Q☼" +
                "☼  H☼" +
                "☼☼☼☼☼");

        enemy.down();
        game.tick();

        assertE("☼☼☼☼►" +
                "☼  H☼" +
                "☼  H☼" +
                "☼  Q☼" +
                "☼☼☼☼☼");

        enemy.down();
        game.tick();

        assertE("☼☼☼☼►" +
                "☼  H☼" +
                "☼  H☼" +
                "☼  Q☼" +
                "☼☼☼☼☼");
    }

    // Чертик может в любой момент спрыгнуть с лестницы и будет падать до тех пор пока не наткнется на препятствие
    @Test
    public void shouldEnemyCanFly() {
        shouldEnemyCanGoOnLadderUp();

        assertE("☼☼☼☼►" +
                "☼  Q☼" +
                "☼  H☼" +
                "☼  H☼" +
                "☼☼☼☼☼");

        enemy.left();
        game.tick();

        assertE("☼☼☼☼►" +
                "☼ «H☼" +
                "☼  H☼" +
                "☼  H☼" +
                "☼☼☼☼☼");

        game.tick();

        assertE("☼☼☼☼►" +
                "☼  H☼" +
                "☼ «H☼" +
                "☼  H☼" +
                "☼☼☼☼☼");

        game.tick();

        assertE("☼☼☼☼►" +
                "☼  H☼" +
                "☼  H☼" +
                "☼ «H☼" +
                "☼☼☼☼☼");
    }

    // Чертик может поднятся по лестнице и зайти на площадку
    @Test
    public void shouldEnemyCanGoFromLadderToArea() {
        givenFl("☼☼☼☼►" +
                "☼H  ☼" +
                "☼H# ☼" +
                "☼H« ☼" +
                "☼☼☼☼☼");

        enemy.left();
        game.tick();

        assertE("☼☼☼☼►" +
                "☼H  ☼" +
                "☼H# ☼" +
                "☼Q  ☼" +
                "☼☼☼☼☼");

        enemy.up();
        game.tick();

        assertE("☼☼☼☼►" +
                "☼H  ☼" +
                "☼Q# ☼" +
                "☼H  ☼" +
                "☼☼☼☼☼");

        enemy.up();
        game.tick();

        assertE("☼☼☼☼►" +
                "☼Q  ☼" +
                "☼H# ☼" +
                "☼H  ☼" +
                "☼☼☼☼☼");

        enemy.right();
        game.tick();

        assertE("☼☼☼☼►" +
                "☼H» ☼" +
                "☼H# ☼" +
                "☼H  ☼" +
                "☼☼☼☼☼");

        // и упасть
        enemy.right();
        game.tick();

        assertE("☼☼☼☼►" +
                "☼H »☼" +
                "☼H# ☼" +
                "☼H  ☼" +
                "☼☼☼☼☼");

        game.tick();

        assertE("☼☼☼☼►" +
                "☼H  ☼" +
                "☼H#»☼" +
                "☼H  ☼" +
                "☼☼☼☼☼");

        game.tick();

        assertE("☼☼☼☼►" +
                "☼H  ☼" +
                "☼H# ☼" +
                "☼H »☼" +
                "☼☼☼☼☼");
    }

    // пока чертик падает он не может двигаться влево и справо, даже если там есть площадки
    @Test
    public void shouldEnemyCantMoveWhenFall() {
        givenFl("☼☼☼☼☼☼►" +
                "☼  »  ☼" +
                "☼     ☼" +
                "☼     ☼" +
                "☼     ☼" +
                "☼## ##☼" +
                "☼☼☼☼☼☼☼");

        enemy.left();
        game.tick();

        assertE("☼☼☼☼☼☼►" +
                "☼     ☼" +
                "☼  »  ☼" +
                "☼     ☼" +
                "☼     ☼" +
                "☼## ##☼" +
                "☼☼☼☼☼☼☼");

        enemy.right();
        game.tick();

        assertE("☼☼☼☼☼☼►" +
                "☼     ☼" +
                "☼     ☼" +
                "☼  »  ☼" +
                "☼     ☼" +
                "☼## ##☼" +
                "☼☼☼☼☼☼☼");

        enemy.left();
        game.tick();

        assertE("☼☼☼☼☼☼►" +
                "☼     ☼" +
                "☼     ☼" +
                "☼     ☼" +
                "☼  »  ☼" +
                "☼## ##☼" +
                "☼☼☼☼☼☼☼");

        enemy.left();
        game.tick();

        assertE("☼☼☼☼☼☼►" +
                "☼     ☼" +
                "☼     ☼" +
                "☼     ☼" +
                "☼     ☼" +
                "☼##»##☼" +
                "☼☼☼☼☼☼☼");
    }

    // если чертик с площадки заходит на трубу то он ползет по ней
    @Test
    public void shouldEnemyPipe() {
        givenFl("☼☼☼☼☼☼►" +
                "☼     ☼" +
                "☼»~~~ ☼" +
                "☼#    ☼" +
                "☼     ☼" +
                "☼     ☼" +
                "☼☼☼☼☼☼☼");

        enemy.right();
        game.tick();

        assertE("☼☼☼☼☼☼►" +
                "☼     ☼" +
                "☼ >~~ ☼" +
                "☼#    ☼" +
                "☼     ☼" +
                "☼     ☼" +
                "☼☼☼☼☼☼☼");

        enemy.right();
        game.tick();

        assertE("☼☼☼☼☼☼►" +
                "☼     ☼" +
                "☼ ~>~ ☼" +
                "☼#    ☼" +
                "☼     ☼" +
                "☼     ☼" +
                "☼☼☼☼☼☼☼");

        enemy.right();
        game.tick();

        assertE("☼☼☼☼☼☼►" +
                "☼     ☼" +
                "☼ ~~> ☼" +
                "☼#    ☼" +
                "☼     ☼" +
                "☼     ☼" +
                "☼☼☼☼☼☼☼");
    }

    // с трубы чертик может спрыгунть и тогда он будет падать до препятствия
    @Test
    public void shouldEnemyFallFromPipe() {
        shouldEnemyPipe();

        assertE("☼☼☼☼☼☼►" +
                "☼     ☼" +
                "☼ ~~> ☼" +
                "☼#    ☼" +
                "☼     ☼" +
                "☼     ☼" +
                "☼☼☼☼☼☼☼");

        enemy.right();
        game.tick();

        assertE("☼☼☼☼☼☼►" +
                "☼     ☼" +
                "☼ ~~~»☼" +
                "☼#    ☼" +
                "☼     ☼" +
                "☼     ☼" +
                "☼☼☼☼☼☼☼");

        game.tick();

        assertE("☼☼☼☼☼☼►" +
                "☼     ☼" +
                "☼ ~~~ ☼" +
                "☼#   »☼" +
                "☼     ☼" +
                "☼     ☼" +
                "☼☼☼☼☼☼☼");

        game.tick();

        assertE("☼☼☼☼☼☼►" +
                "☼     ☼" +
                "☼ ~~~ ☼" +
                "☼#    ☼" +
                "☼    »☼" +
                "☼     ☼" +
                "☼☼☼☼☼☼☼");

        game.tick();

        assertE("☼☼☼☼☼☼►" +
                "☼     ☼" +
                "☼ ~~~ ☼" +
                "☼#    ☼" +
                "☼     ☼" +
                "☼    »☼" +
                "☼☼☼☼☼☼☼");
    }

    // чертик может похитить 1 золото в падении
    @Test
    public void shouldEnemyGetGoldWhenFallenFromPipe() {
        givenFl("☼☼☼☼☼☼☼☼" +
                "☼      ☼" +
                "☼  $~~«☼" +
                "☼  $  #☼" +
                "☼  $   ☼" +
                "☼  $  ◄☼" +
                "☼######☼" +
                "☼☼☼☼☼☼☼☼");

        enemy.left();
        game.tick();

        assertE("☼☼☼☼☼☼☼☼" +
                "☼      ☼" +
                "☼  $~< ☼" +
                "☼  $  #☼" +
                "☼  $   ☼" +
                "☼  $  ◄☼" +
                "☼######☼" +
                "☼☼☼☼☼☼☼☼");

        enemy.left();
        game.tick();

        assertE("☼☼☼☼☼☼☼☼" +
                "☼      ☼" +
                "☼  $<~ ☼" +
                "☼  $  #☼" +
                "☼  $   ☼" +
                "☼  $  ◄☼" +
                "☼######☼" +
                "☼☼☼☼☼☼☼☼");

        enemy.left();
        game.tick();

        assertE("☼☼☼☼☼☼☼☼" +
                "☼      ☼" +
                "☼  «~~ ☼" +
                "☼  $  #☼" +
                "☼  $   ☼" +
                "☼  $  ◄☼" +
                "☼######☼" +
                "☼☼☼☼☼☼☼☼");

        game.tick();      // чертик не берет больше 1 кучки золота

        assertE("☼☼☼☼☼☼☼☼" +
                "☼      ☼" +
                "☼   ~~ ☼" +
                "☼  «  #☼" +
                "☼  $   ☼" +
                "☼  $  ◄☼" +
                "☼######☼" +
                "☼☼☼☼☼☼☼☼");

        game.tick();

        assertE("☼☼☼☼☼☼☼☼" +
                "☼      ☼" +
                "☼   ~~ ☼" +
                "☼  $  #☼" +
                "☼  «   ☼" +
                "☼  $  ◄☼" +
                "☼######☼" +
                "☼☼☼☼☼☼☼☼");

        game.tick();

        assertE("☼☼☼☼☼☼☼☼" +
                "☼      ☼" +
                "☼   ~~ ☼" +
                "☼  $  #☼" +
                "☼  $   ☼" +
                "☼  «  ◄☼" +
                "☼######☼" +
                "☼☼☼☼☼☼☼☼");
    }

    @Test
    public void shouldEnemyLeaveGoldWhenFallInPit() {
        shouldEnemyGetGoldWhenFallenFromPipe();

        enemy.right();
        hero.act();
        game.tick();

        assertE("☼☼☼☼☼☼☼☼" +
                "☼      ☼" +
                "☼   ~~ ☼" +
                "☼  $  #☼" +
                "☼  $   ☼" +
                "☼  $» Я☼" +
                "☼####*#☼" +
                "☼☼☼☼☼☼☼☼");

        enemy.right();     // если чертик с золотом падает в ямку - он оставляет золото на поверхности
        game.tick();

        assertE("☼☼☼☼☼☼☼☼" +
                "☼      ☼" +
                "☼   ~~ ☼" +
                "☼  $  #☼" +
                "☼  $   ☼" +
                "☼  $ »Я☼" +
                "☼#### #☼" +
                "☼☼☼☼☼☼☼☼");

        enemy.right();
        game.tick();

        assertE("☼☼☼☼☼☼☼☼" +
                "☼      ☼" +
                "☼   ~~ ☼" +
                "☼  $  #☼" +
                "☼  $   ☼" +
                "☼  $ $Я☼" +
                "☼####X#☼" +
                "☼☼☼☼☼☼☼☼");
    }

    @Test
    public void shouldIWalkOnEnemyInPitAndGetGold() {
        shouldEnemyLeaveGoldWhenFallInPit();

        hero.left();     //я могу пройти по нему сверху и забрать золото
        dice(1, 6);
        game.tick();

        assertE("☼☼☼☼☼☼☼☼" +
                "☼$     ☼" +
                "☼   ~~ ☼" +
                "☼  $  #☼" +
                "☼  $   ☼" +
                "☼  $ ◄ ☼" +
                "☼####X#☼" +
                "☼☼☼☼☼☼☼☼");

        events.verifyAllEvents("[GET_YELLOW_GOLD]");

        hero.left();
        game.tick();

        assertE("☼☼☼☼☼☼☼☼" +
                "☼$     ☼" +
                "☼   ~~ ☼" +
                "☼  $  #☼" +
                "☼  $   ☼" +
                "☼  $◄  ☼" +
                "☼####X#☼" +
                "☼☼☼☼☼☼☼☼");

        hero.left();
        dice(2, 6);
        game.tick();

        events.verifyAllEvents("[GET_YELLOW_GOLD]");

        assertE("☼☼☼☼☼☼☼☼" +
                "☼$$    ☼" +
                "☼   ~~ ☼" +
                "☼  $  #☼" +
                "☼  $   ☼" +
                "☼  ◄   ☼" +
                "☼####X#☼" +
                "☼☼☼☼☼☼☼☼");
    }

    @Test
    public void shouldNoMoreGoldAEnemy() {
        shouldIWalkOnEnemyInPitAndGetGold();

        for (int c = 4; c < Brick.DRILL_TIMER; c++) { // враг вылазит
            game.tick();
        }

        enemy.left();
        hero.right();
        hero.act();
        game.tick();

        assertE("☼☼☼☼☼☼☼☼" +
                "☼$$    ☼" +
                "☼   ~~ ☼" +
                "☼  $  #☼" +
                "☼  $   ☼" +
                "☼  R « ☼" +
                "☼###*##☼" +
                "☼☼☼☼☼☼☼☼");

        enemy.left();
        game.tick();

        assertE("☼☼☼☼☼☼☼☼" +
                "☼$$    ☼" +
                "☼   ~~ ☼" +
                "☼  $  #☼" +
                "☼  $   ☼" +
                "☼  R«  ☼" +
                "☼### ##☼" +
                "☼☼☼☼☼☼☼☼");

        game.tick();  // золота у него больше нет

        assertE("☼☼☼☼☼☼☼☼" +
                "☼$$    ☼" +
                "☼   ~~ ☼" +
                "☼  $  #☼" +
                "☼  $   ☼" +
                "☼  R   ☼" +
                "☼###X##☼" +
                "☼☼☼☼☼☼☼☼");
    }

    // я могу ходить по монстру, который в ямке
    @Test
    public void shouldIWalkOnEnemy() {
        shouldEnemyLeaveGoldWhenFallInPit();

        assertE("☼☼☼☼☼☼☼☼" +
                "☼      ☼" +
                "☼   ~~ ☼" +
                "☼  $  #☼" +
                "☼  $   ☼" +
                "☼  $ $Я☼" +
                "☼####X#☼" +
                "☼☼☼☼☼☼☼☼");

        dice(1, 6);
        hero.left();
        game.tick();

        events.verifyAllEvents("[GET_YELLOW_GOLD]");

        assertE("☼☼☼☼☼☼☼☼" +
                "☼$     ☼" +
                "☼   ~~ ☼" +
                "☼  $  #☼" +
                "☼  $   ☼" +
                "☼  $ ◄ ☼" +
                "☼####X#☼" +
                "☼☼☼☼☼☼☼☼");

        hero.left();
        game.tick();

        assertE("☼☼☼☼☼☼☼☼" +
                "☼$     ☼" +
                "☼   ~~ ☼" +
                "☼  $  #☼" +
                "☼  $   ☼" +
                "☼  $◄  ☼" +
                "☼####X#☼" +
                "☼☼☼☼☼☼☼☼");

        dice(2, 6);
        hero.left();
        game.tick();

        assertE("☼☼☼☼☼☼☼☼" +
                "☼$$    ☼" +
                "☼   ~~ ☼" +
                "☼  $  #☼" +
                "☼  $   ☼" +
                "☼  ◄   ☼" +
                "☼####X#☼" +
                "☼☼☼☼☼☼☼☼");

        game.tick();

        events.verifyAllEvents("[GET_YELLOW_GOLD]");

        game.tick();
        game.tick();
        game.tick();
        game.tick();
        game.tick();
        game.tick();
        game.tick();

        assertE("☼☼☼☼☼☼☼☼" +
                "☼$$    ☼" +
                "☼   ~~ ☼" +
                "☼  $  #☼" +
                "☼  $   ☼" +
                "☼  ◄ » ☼" +
                "☼######☼" +
                "☼☼☼☼☼☼☼☼");
    }

    // я не могу просверлить дырку непосредственно под монстром
    // TODO сделать так, чтобы мог
    @Test
    public void shouldICantDrillUnderEnemy() {
        givenFl("☼☼☼☼☼" +
                "☼   ☼" +
                "☼ ►»☼" +
                "☼###☼" +
                "☼☼☼☼☼");

        hero.act();
        game.tick();

        assertE("☼☼☼☼☼" +
                "☼   ☼" +
                "☼ ►»☼" +
                "☼###☼" +
                "☼☼☼☼☼");
    }

    // если я просверлил дырку монстр падает в нее, а под ней ничего нет - монстр не проваливается сквозь
    @Test
    public void shouldEnemyStayOnPitWhenUnderPitIsFree() {
        givenFl("☼☼☼☼☼☼☼" +
                "☼» ◄  ☼" +
                "☼#####☼" +
                "☼     ☼" +
                "☼     ☼" +
                "☼     ☼" +
                "☼☼☼☼☼☼☼");

        hero.act();
        game.tick();

        assertE("☼☼☼☼☼☼☼" +
                "☼» Я  ☼" +
                "☼#*###☼" +
                "☼     ☼" +
                "☼     ☼" +
                "☼     ☼" +
                "☼☼☼☼☼☼☼");

        enemy.right();
        game.tick();

        assertE("☼☼☼☼☼☼☼" +
                "☼ »Я  ☼" +
                "☼# ###☼" +
                "☼     ☼" +
                "☼     ☼" +
                "☼     ☼" +
                "☼☼☼☼☼☼☼");

        game.tick();

        assertE("☼☼☼☼☼☼☼" +
                "☼  Я  ☼" +
                "☼#X###☼" +
                "☼     ☼" +
                "☼     ☼" +
                "☼     ☼" +
                "☼☼☼☼☼☼☼");

        game.tick();
        game.tick();
        game.tick();
        game.tick();
        game.tick();
        game.tick();
        game.tick();
        game.tick();
        game.tick();
        game.tick();

        assertE("☼☼☼☼☼☼☼" +
                "☼  Я  ☼" +
                "☼#X###☼" +
                "☼     ☼" +
                "☼     ☼" +
                "☼     ☼" +
                "☼☼☼☼☼☼☼");

        game.tick();

        assertE("☼☼☼☼☼☼☼" +
                "☼ »Я  ☼" +
                "☼#####☼" +
                "☼     ☼" +
                "☼     ☼" +
                "☼     ☼" +
                "☼☼☼☼☼☼☼");
    }

    // если в процессе падения чертик вдург наткнулся на трубу то он повисаю на ней
    @Test
    public void shouldEnemyPipeWhenFall() {
        givenFl("☼☼☼☼☼☼►" +
                "☼  »  ☼" +
                "☼# ###☼" +
                "☼     ☼" +
                "☼ ~~~ ☼" +
                "☼     ☼" +
                "☼☼☼☼☼☼☼");

        enemy.left();
        game.tick();

        assertE("☼☼☼☼☼☼►" +
                "☼ «   ☼" +
                "☼# ###☼" +
                "☼     ☼" +
                "☼ ~~~ ☼" +
                "☼     ☼" +
                "☼☼☼☼☼☼☼");

        game.tick();

        assertE("☼☼☼☼☼☼►" +
                "☼     ☼" +
                "☼#«###☼" +
                "☼     ☼" +
                "☼ ~~~ ☼" +
                "☼     ☼" +
                "☼☼☼☼☼☼☼");

        game.tick();

        assertE("☼☼☼☼☼☼►" +
                "☼     ☼" +
                "☼# ###☼" +
                "☼ «   ☼" +
                "☼ ~~~ ☼" +
                "☼     ☼" +
                "☼☼☼☼☼☼☼");

        game.tick();

        assertE("☼☼☼☼☼☼►" +
                "☼     ☼" +
                "☼# ###☼" +
                "☼     ☼" +
                "☼ <~~ ☼" +
                "☼     ☼" +
                "☼☼☼☼☼☼☼");

        game.tick();

        assertE("☼☼☼☼☼☼►" +
                "☼     ☼" +
                "☼# ###☼" +
                "☼     ☼" +
                "☼ <~~ ☼" +
                "☼     ☼" +
                "☼☼☼☼☼☼☼");
    }

    // чертик может спрыгнуть с трубы
    @Test
    public void shouldEnemyCanJumpFromPipe() {
        givenFl("☼☼☼☼☼☼►" +
                "☼  «  ☼" +
                "☼     ☼" +
                "☼ ~~~ ☼" +
                "☼     ☼" +
                "☼     ☼" +
                "☼☼☼☼☼☼☼");

        game.tick();

        assertE("☼☼☼☼☼☼►" +
                "☼     ☼" +
                "☼  «  ☼" +
                "☼ ~~~ ☼" +
                "☼     ☼" +
                "☼     ☼" +
                "☼☼☼☼☼☼☼");

        game.tick();

        assertE("☼☼☼☼☼☼►" +
                "☼     ☼" +
                "☼     ☼" +
                "☼ ~<~ ☼" +
                "☼     ☼" +
                "☼     ☼" +
                "☼☼☼☼☼☼☼");

        enemy.down();
        game.tick();

        assertE("☼☼☼☼☼☼►" +
                "☼     ☼" +
                "☼     ☼" +
                "☼ ~~~ ☼" +
                "☼  «  ☼" +
                "☼     ☼" +
                "☼☼☼☼☼☼☼");

        game.tick();

        assertE("☼☼☼☼☼☼►" +
                "☼     ☼" +
                "☼     ☼" +
                "☼ ~~~ ☼" +
                "☼     ☼" +
                "☼  «  ☼" +
                "☼☼☼☼☼☼☼");
    }

    // чертику нельзя спускаться с лестницы в бетон, так же как и подниматься
    // плюс чертик должен иметь возможность спустится по лестнице
    @Test
    public void shouldEnemyCantWalkThroughWallDown() {
        givenFl("☼☼☼☼►" +
                "☼ « ☼" +
                "☼ H ☼" +
                "☼☼☼☼☼" +
                "☼☼☼☼☼");

        enemy.down();
        game.tick();

        assertE("☼☼☼☼►" +
                "☼   ☼" +
                "☼ Q ☼" +
                "☼☼☼☼☼" +
                "☼☼☼☼☼");

        enemy.down();
        game.tick();

        assertE("☼☼☼☼►" +
                "☼   ☼" +
                "☼ Q ☼" +
                "☼☼☼☼☼" +
                "☼☼☼☼☼");
    }

    @Test
    public void shouldEnemyCantWalkThroughWallUp() {
        settings.integer(ENEMIES_COUNT, 1);
        givenFl("☼☼☼☼►" +
                "☼☼☼☼☼" +
                "☼ H ☼" +
                "☼ H«☼" +
                "☼☼☼☼☼");

        enemy.left();
        game.tick();

        assertE("☼☼☼☼►" +
                "☼☼☼☼☼" +
                "☼ H ☼" +
                "☼ Q ☼" +
                "☼☼☼☼☼");

        enemy.up();
        game.tick();

        assertE("☼☼☼☼►" +
                "☼☼☼☼☼" +
                "☼ Q ☼" +
                "☼ H ☼" +
                "☼☼☼☼☼");

        enemy.up();
        game.tick();

        assertE("☼☼☼☼►" +
                "☼☼☼☼☼" +
                "☼ Q ☼" +
                "☼ H ☼" +
                "☼☼☼☼☼");
    }

    // Чертику нельзя проходить с лестницы через бетон направо или налево
    @Test
    public void shouldEnemyCantWalkThroughWallLeftRight() {
        givenFl("☼☼☼☼►" +
                "☼☼☼☼☼" +
                "☼☼H☼☼" +
                "☼ H«☼" +
                "☼☼☼☼☼");

        enemy.left();
        game.tick();

        assertE("☼☼☼☼►" +
                "☼☼☼☼☼" +
                "☼☼H☼☼" +
                "☼ Q ☼" +
                "☼☼☼☼☼");

        enemy.up();
        game.tick();

        assertE("☼☼☼☼►" +
                "☼☼☼☼☼" +
                "☼☼Q☼☼" +
                "☼ H ☼" +
                "☼☼☼☼☼");

        enemy.left();
        game.tick();

        assertE("☼☼☼☼►" +
                "☼☼☼☼☼" +
                "☼☼Q☼☼" +
                "☼ H ☼" +
                "☼☼☼☼☼");

        enemy.right();
        game.tick();

        assertE("☼☼☼☼►" +
                "☼☼☼☼☼" +
                "☼☼Q☼☼" +
                "☼ H ☼" +
                "☼☼☼☼☼");
    }

    // чертику нельзя проходить через бетон
    @Test
    public void shouldEnemyCantWalkThroughWallLeftRight2() {
        givenFl("☼☼☼☼►" +
                "☼   ☼" +
                "☼   ☼" +
                "☼☼«☼☼" +
                "☼☼☼☼☼");

        enemy.left();
        game.tick();

        assertE("☼☼☼☼►" +
                "☼   ☼" +
                "☼   ☼" +
                "☼☼«☼☼" +
                "☼☼☼☼☼");

        enemy.right();
        game.tick();

        assertE("☼☼☼☼►" +
                "☼   ☼" +
                "☼   ☼" +
                "☼☼»☼☼" +
                "☼☼☼☼☼");
    }

    // Чертику нельзя спрыгивать с трубы что сразу над бетоном, протелая сквозь него
    @Test
    public void shouldEnemyCantJumpThroughWall() {
        givenFl("☼☼☼☼☼►" +
                "☼  » ☼" +
                "☼  ~ ☼" +
                "☼☼☼☼☼☼" +
                "☼    ☼" +
                "☼☼☼☼☼☼");

        game.tick();

        assertE("☼☼☼☼☼►" +
                "☼    ☼" +
                "☼  > ☼" +
                "☼☼☼☼☼☼" +
                "☼    ☼" +
                "☼☼☼☼☼☼");

        enemy.down();
        game.tick();

        assertE("☼☼☼☼☼►" +
                "☼    ☼" +
                "☼  > ☼" +
                "☼☼☼☼☼☼" +
                "☼    ☼" +
                "☼☼☼☼☼☼");
    }

    // если чертик спрыгивает с последней секции лестницы
    @Test
    public void shouldEnemyJumpFromLadderDown() {
        givenFl("☼☼☼☼☼►" +
                "☼ »  ☼" +
                "☼ H##☼" +
                "☼#H  ☼" +
                "☼    ☼" +
                "☼☼☼☼☼☼");

        enemy.down();
        game.tick();

        enemy.down();
        game.tick();

        assertE("☼☼☼☼☼►" +
                "☼    ☼" +
                "☼ H##☼" +
                "☼#Q  ☼" +
                "☼    ☼" +
                "☼☼☼☼☼☼");

        game.tick();

        assertE("☼☼☼☼☼►" +
                "☼    ☼" +
                "☼ H##☼" +
                "☼#Q  ☼" +
                "☼    ☼" +
                "☼☼☼☼☼☼");

        enemy.right();
        game.tick();

        assertE("☼☼☼☼☼►" +
                "☼    ☼" +
                "☼ H##☼" +
                "☼#H» ☼" +
                "☼    ☼" +
                "☼☼☼☼☼☼");

        game.tick();

        assertE("☼☼☼☼☼►" +
                "☼    ☼" +
                "☼ H##☼" +
                "☼#H  ☼" +
                "☼  » ☼" +
                "☼☼☼☼☼☼");
    }

    // Чертик не может прыгять вверх :)
    @Test
    public void shouldEnemyCantJump() {
        givenFl("☼☼☼☼☼►" +
                "☼    ☼" +
                "☼    ☼" +
                "☼    ☼" +
                "☼ »  ☼" +
                "☼☼☼☼☼☼");

        enemy.up();
        game.tick();

        assertE("☼☼☼☼☼►" +
                "☼    ☼" +
                "☼    ☼" +
                "☼    ☼" +
                "☼ »  ☼" +
                "☼☼☼☼☼☼");
    }

    // я могу прыгнуть на голову монстру и мне ничего не будет
    @Test
    public void shouldICanJumpAtEnemyHead() {
        givenFl("☼☼☼☼☼" +
                "☼ ◄ ☼" +
                "☼   ☼" +
                "☼ » ☼" +
                "☼☼☼☼☼");

        game.tick();

        assertE("☼☼☼☼☼" +
                "☼   ☼" +
                "☼ ◄ ☼" +
                "☼ » ☼" +
                "☼☼☼☼☼");

        game.tick();

        assertE("☼☼☼☼☼" +
                "☼   ☼" +
                "☼ ◄ ☼" +
                "☼ » ☼" +
                "☼☼☼☼☼");

        enemy.right();     // если он отойдет - я упаду дальше
        game.tick();

        assertE("☼☼☼☼☼" +
                "☼   ☼" +
                "☼ ] ☼" +
                "☼  »☼" +
                "☼☼☼☼☼");

        game.tick();

        assertE("☼☼☼☼☼" +
                "☼   ☼" +
                "☼   ☼" +
                "☼ ◄»☼" +
                "☼☼☼☼☼");
    }

    // если чертик после того, как упал в ямку оставил золото, то когда он выберется - он свое золото заберет
    // А когда снова упадет, то оставит
    @Test
    public void shouldGetGoldWheExitFromPit() {
        shouldEnemyLeaveGoldWhenFallInPit();

        assertE("☼☼☼☼☼☼☼☼" +
                "☼      ☼" +
                "☼   ~~ ☼" +
                "☼  $  #☼" +
                "☼  $   ☼" +
                "☼  $ $Я☼" +
                "☼####X#☼" +
                "☼☼☼☼☼☼☼☼");

        for (int c = 3; c < Brick.DRILL_TIMER; c++) { // враг вылазит
            game.tick();
        }
        game.tick();

        assertE("☼☼☼☼☼☼☼☼" +
                "☼      ☼" +
                "☼   ~~ ☼" +
                "☼  $  #☼" +
                "☼  $   ☼" +
                "☼  $ »Я☼" +
                "☼######☼" +
                "☼☼☼☼☼☼☼☼");

        enemy.left();
        game.tick();

        hero.act();
        game.tick();

        assertE("☼☼☼☼☼☼☼☼" +
                "☼      ☼" +
                "☼   ~~ ☼" +
                "☼  $  #☼" +
                "☼  $   ☼" +
                "☼  $« Я☼" +
                "☼####*#☼" +
                "☼☼☼☼☼☼☼☼");

        enemy.right();
        game.tick();
        game.tick();

        assertE("☼☼☼☼☼☼☼☼" +
                "☼      ☼" +
                "☼   ~~ ☼" +
                "☼  $  #☼" +
                "☼  $   ☼" +
                "☼  $ $Я☼" +
                "☼####X#☼" +
                "☼☼☼☼☼☼☼☼");
    }

    // Если чертик упал на другого чертика который был на трубе, то они складываются в один :)
    @Test
    public void shouldEnemyStayOnOtherAtThePipe() {
        givenFl("☼☼☼☼☼☼☼☼" +
                "☼  «   ☼" +
                "☼      ☼" +
                "☼  «   ☼" +
                "☼  ~   ☼" +
                "☼     ◄☼" +
                "☼######☼" +
                "☼☼☼☼☼☼☼☼");

        game.tick();

        assertE("☼☼☼☼☼☼☼☼" +
                "☼      ☼" +
                "☼  «   ☼" +
                "☼      ☼" +
                "☼  <   ☼" +
                "☼     ◄☼" +
                "☼######☼" +
                "☼☼☼☼☼☼☼☼");

        game.tick();
        game.tick();
        game.tick();

        assertE("☼☼☼☼☼☼☼☼" +
                "☼      ☼" +
                "☼      ☼" +
                "☼  «   ☼" +
                "☼  <   ☼" +
                "☼     ◄☼" +
                "☼######☼" +
                "☼☼☼☼☼☼☼☼");

        enemy.down();
        game.tick();

        assertE("☼☼☼☼☼☼☼☼" +
                "☼      ☼" +
                "☼      ☼" +
                "☼      ☼" +
                "☼  <   ☼" +
                "☼     ◄☼" +
                "☼######☼" +
                "☼☼☼☼☼☼☼☼");

        enemy.left();
        game.tick();
        game.tick();

        assertE("☼☼☼☼☼☼☼☼" +
                "☼      ☼" +
                "☼      ☼" +
                "☼      ☼" +
                "☼  <   ☼" +
                "☼ «   ◄☼" +
                "☼######☼" +
                "☼☼☼☼☼☼☼☼");
    }

    @Test
    public void shouldEnemyDontStopOnPipe() {
        givenFl("☼☼☼☼☼☼☼☼" +
                "☼      ☼" +
                "☼      ☼" +
                "☼  ««  ☼" +
                "☼  ~~  ☼" +
                "☼     ◄☼" +
                "☼######☼" +
                "☼☼☼☼☼☼☼☼");

        game.tick();

        assertE("☼☼☼☼☼☼☼☼" +
                "☼      ☼" +
                "☼      ☼" +
                "☼      ☼" +
                "☼  <<  ☼" +
                "☼     ◄☼" +
                "☼######☼" +
                "☼☼☼☼☼☼☼☼");

        enemy.right();
        game.tick();

        assertE("☼☼☼☼☼☼☼☼" +
                "☼      ☼" +
                "☼      ☼" +
                "☼      ☼" +
                "☼  ~>  ☼" +
                "☼     ◄☼" +
                "☼######☼" +
                "☼☼☼☼☼☼☼☼");

        enemy.right();
        game.tick();
        game.tick();

        assertE("☼☼☼☼☼☼☼☼" +
                "☼      ☼" +
                "☼      ☼" +
                "☼      ☼" +
                "☼  ~<  ☼" +
                "☼    »◄☼" +
                "☼######☼" +
                "☼☼☼☼☼☼☼☼");


    }

    // Чертик должен сам проваливаться на героя, а не впрыгивать в него
    @Test
    public void shouldEnemyStayOnHeroAtThePipe() {
        givenFl("☼☼☼☼☼☼☼☼" +
                "☼  «   ☼" +
                "☼      ☼" +
                "☼  ◄   ☼" +
                "☼  ~   ☼" +
                "☼      ☼" +
                "☼######☼" +
                "☼☼☼☼☼☼☼☼");

        game.tick();

        assertE("☼☼☼☼☼☼☼☼" +
                "☼      ☼" +
                "☼  «   ☼" +
                "☼      ☼" +
                "☼  {   ☼" +
                "☼      ☼" +
                "☼######☼" +
                "☼☼☼☼☼☼☼☼");

        game.tick();
        game.tick();

        events.verifyAllEvents("[KILL_HERO]");

        assertE("☼☼☼☼☼☼☼☼" +
                "☼      ☼" +
                "☼      ☼" +
                "☼      ☼" +
                "☼  Ѡ   ☼" +
                "☼      ☼" +
                "☼######☼" +
                "☼☼☼☼☼☼☼☼");

        enemy.down();
        dice(2, 3);
        game.newGame(player);
        dice(0);  // охотимся за первым игроком
        game.tick();

        assertE("☼☼☼☼☼☼☼☼" +
                "☼      ☼" +
                "☼      ☼" +
                "☼      ☼" +
                "☼  ~   ☼" +
                "☼ ►«   ☼" +
                "☼######☼" +
                "☼☼☼☼☼☼☼☼");
    }

    // Чертик проваливается в яму за героем и там его находит
    @Test
    public void shouldEnemyFindHeroAtPit() {
        givenFl("☼☼☼☼☼☼☼☼" +
                "☼      ☼" +
                "☼      ☼" +
                "☼      ☼" +
                "☼      ☼" +
                "☼  « ◄ ☼" +
                "☼######☼" +
                "☼☼☼☼☼☼☼☼");

        hero.act();
        game.tick();

        assertE("☼☼☼☼☼☼☼☼" +
                "☼      ☼" +
                "☼      ☼" +
                "☼      ☼" +
                "☼      ☼" +
                "☼  « Я ☼" +
                "☼###*##☼" +
                "☼☼☼☼☼☼☼☼");

        hero.left();
        game.tick();
        game.tick();

        assertE("☼☼☼☼☼☼☼☼" +
                "☼      ☼" +
                "☼      ☼" +
                "☼      ☼" +
                "☼      ☼" +
                "☼  «   ☼" +
                "☼###◄##☼" +
                "☼☼☼☼☼☼☼☼");

        enemy.right();
        game.tick();
        game.tick();

        events.verifyAllEvents("[KILL_HERO]");

        assertE("☼☼☼☼☼☼☼☼" +
                "☼      ☼" +
                "☼      ☼" +
                "☼      ☼" +
                "☼      ☼" +
                "☼      ☼" +
                "☼###Ѡ##☼" +
                "☼☼☼☼☼☼☼☼");

        dice(2, 3);
        game.newGame(player);
        dice(0);  // охотимся за первым игроком
        game.tick();

        assertE("☼☼☼☼☼☼☼☼" +
                "☼      ☼" +
                "☼      ☼" +
                "☼      ☼" +
                "☼      ☼" +
                "☼ ►    ☼" +
                "☼###X##☼" +
                "☼☼☼☼☼☼☼☼");

        for (int c = 5; c < Brick.DRILL_TIMER; c++) { // враг вылазит
            game.tick();
        }

        assertE("☼☼☼☼☼☼☼☼" +
                "☼      ☼" +
                "☼      ☼" +
                "☼      ☼" +
                "☼      ☼" +
                "☼ ► »  ☼" +
                "☼######☼" +
                "☼☼☼☼☼☼☼☼");

    }

    @Test
    public void iLooseScoresWhenDoHarakiri() {
        givenFl("☼☼☼☼☼" +
                "☼   ☼" +
                "☼ ◄ ☼" +
                "☼###☼" +
                "☼☼☼☼☼");

        hero.act(0);
        game.tick();

        assertE("☼☼☼☼☼" +
                "☼   ☼" +
                "☼ Ѡ ☼" +
                "☼###☼" +
                "☼☼☼☼☼");

        events.verifyAllEvents("[KILL_HERO, SUICIDE]");
    }

    @Test
    public void iCanJumpThroughPortals() {
        settings.integer(PORTALS_COUNT, 2);

        dice(1, 2,
            3, 3);
        givenFl("☼☼☼☼☼" +
                "☼  ⊛☼" +
                "☼⊛◄ ☼" +
                "☼###☼" +
                "☼☼☼☼☼");

        hero.left();
        game.tick();

        assertE("☼☼☼☼☼" +
                "☼  ]☼" +
                "☼⊛  ☼" +
                "☼###☼" +
                "☼☼☼☼☼");

        game.tick();

        assertE("☼☼☼☼☼" +
                "☼  ⊛☼" +
                "☼⊛ ◄☼" +
                "☼###☼" +
                "☼☼☼☼☼");

        game.tick();

        assertE("☼☼☼☼☼" +
                "☼  ⊛☼" +
                "☼⊛ ◄☼" +
                "☼###☼" +
                "☼☼☼☼☼");
    }

    @Test
    public void portalsAreRecreatedEveryFewTicks() {
        settings.integer(PORTALS_COUNT, 2)
                .integer(PORTAL_TICKS, 5);

        givenFl("☼☼☼☼☼" +
                "☼  ⊛☼" +
                "☼⊛◄ ☼" +
                "☼###☼" +
                "☼☼☼☼☼");

        assertEquals(5, game.getPortalsTimer());

        hero.left();
        game.tick();

        assertEquals(4, game.getPortalsTimer());

        assertE("☼☼☼☼☼" +
                "☼  ]☼" +
                "☼⊛  ☼" +
                "☼###☼" +
                "☼☼☼☼☼");

        game.tick();

        assertEquals(3, game.getPortalsTimer());

        assertE("☼☼☼☼☼" +
                "☼  ⊛☼" +
                "☼⊛ ◄☼" +
                "☼###☼" +
                "☼☼☼☼☼");

        game.tick();

        assertEquals(2, game.getPortalsTimer());

        game.tick();

        assertEquals(1, game.getPortalsTimer());

        game.tick();

        assertEquals(0, game.getPortalsTimer());

        assertE("☼☼☼☼☼" +
                "☼  ⊛☼" +
                "☼⊛ ◄☼" +
                "☼###☼" +
                "☼☼☼☼☼");

        dice(1, 3,  // new portals
            2, 3);
        game.tick();

        assertEquals(5, game.getPortalsTimer());

        assertE("☼☼☼☼☼" +
                "☼⊛⊛ ☼" +
                "☼  ◄☼" +
                "☼###☼" +
                "☼☼☼☼☼");
    }

    private class EnemyJoystick implements Joystick, DirectionActJoystick {
        @Override
        public void down() {
            ai(Direction.DOWN);
        }

        @Override
        public void up() {
            ai(Direction.UP);
        }

        @Override
        public void left() {
            ai(Direction.LEFT);
        }

        @Override
        public void right() {
            ai(Direction.RIGHT);
        }

        @Override
        public void act(int... p) {
            ai(null);
        }
    }

    @Test
    public void shouldResetHeroAndEnemy_whenClearBoard() {
        // given
        givenFl("☼☼☼☼☼☼☼☼" +
                "☼      ☼" +
                "☼      ☼" +
                "☼      ☼" +
                "☼      ☼" +
                "☼  « ◄ ☼" +
                "☼######☼" +
                "☼☼☼☼☼☼☼☼");

        game.tick();

        assertE("☼☼☼☼☼☼☼☼" +
                "☼      ☼" +
                "☼      ☼" +
                "☼      ☼" +
                "☼      ☼" +
                "☼  « ◄ ☼" +
                "☼######☼" +
                "☼☼☼☼☼☼☼☼");

        enemy.left();
        game.tick();

        assertE("☼☼☼☼☼☼☼☼" +
                "☼      ☼" +
                "☼      ☼" +
                "☼      ☼" +
                "☼      ☼" +
                "☼ «  ◄ ☼" +
                "☼######☼" +
                "☼☼☼☼☼☼☼☼");

        // when
        dice(1, 2); // new hero coordinates
        game.clearScore();

        // then
        assertE("☼☼☼☼☼☼☼☼" +
                "☼      ☼" +
                "☼      ☼" +
                "☼      ☼" +
                "☼      ☼" +
                "☼► «   ☼" +
                "☼######☼" +
                "☼☼☼☼☼☼☼☼");
    }

    @Test
    public void shouldResetHeroScores_whenClearBoard() {
        // given
        Brick.DRILL_TIMER = 4;

        givenFl("☼☼☼☼☼☼☼☼" +
                "☼      ☼" +
                "☼      ☼" +
                "☼      ☼" +
                "☼      ☼" +
                "☼  ◄ ◄ ☼" +
                "☼######☼" +
                "☼☼☼☼☼☼☼☼");

        game.tick();

        assertE("☼☼☼☼☼☼☼☼" +
                "☼      ☼" +
                "☼      ☼" +
                "☼      ☼" +
                "☼      ☼" +
                "☼  ◄ ) ☼" +
                "☼######☼" +
                "☼☼☼☼☼☼☼☼");

        hero(0).act();
        hero(0).right();
        hero(1).left();
        game.tick();

        assertE("☼☼☼☼☼☼☼☼" +
                "☼      ☼" +
                "☼      ☼" +
                "☼      ☼" +
                "☼      ☼" +
                "☼  R⊐  ☼" +
                "☼###*##☼" +
                "☼☼☼☼☼☼☼☼");

        game.tick();

        assertE("☼☼☼☼☼☼☼☼" +
                "☼      ☼" +
                "☼      ☼" +
                "☼      ☼" +
                "☼      ☼" +
                "☼  R   ☼" +
                "☼###)##☼" +
                "☼☼☼☼☼☼☼☼");

        game.tick();

        events.verifyNoEvents();

        assertEquals(0, hero(0).scores());
        assertEquals(0, hero(1).scores());
        assertEquals(true, hero(0).isAlive());
        assertEquals(true, hero(1).isAlive());

        assertE("☼☼☼☼☼☼☼☼" +
                "☼      ☼" +
                "☼      ☼" +
                "☼      ☼" +
                "☼      ☼" +
                "☼  R   ☼" +
                "☼###)##☼" +
                "☼☼☼☼☼☼☼☼");

        game.tick();

        events.verifyAllEvents("[KILL_HERO, KILL_ENEMY]");

        assertEquals(1, hero(0).scores());
        assertEquals(0, hero(1).scores());
        assertEquals(true, hero(0).isAlive());
        assertEquals(false, hero(1).isAlive());

        assertE("☼☼☼☼☼☼☼☼" +
                "☼      ☼" +
                "☼      ☼" +
                "☼      ☼" +
                "☼      ☼" +
                "☼  R   ☼" +
                "☼###Z##☼" +
                "☼☼☼☼☼☼☼☼");

        // when
        dice(1, 2,
            6, 2);
        game.clearScore();
        reloadAllHeroes();

        // then
        assertEquals(0, hero(0).scores());
        assertEquals(0, hero(1).scores());
        assertEquals(true, hero(0).isAlive());
        assertEquals(true, hero(1).isAlive());

        assertE("☼☼☼☼☼☼☼☼" +
                "☼      ☼" +
                "☼      ☼" +
                "☼      ☼" +
                "☼      ☼" +
                "☼►    (☼" +
                "☼######☼" +
                "☼☼☼☼☼☼☼☼");
    }

    @Test
    public void shouldCollectAllGold() {
        // given
        givenFl("☼☼☼☼☼☼☼☼" +
                "☼      ☼" +
                "☼      ☼" +
                "☼      ☼" +
                "☼      ☼" +
                "☼►$$&@@☼" +
                "☼######☼" +
                "☼☼☼☼☼☼☼☼");

        game.tick();

        assertE("☼☼☼☼☼☼☼☼" +
                "☼      ☼" +
                "☼      ☼" +
                "☼      ☼" +
                "☼      ☼" +
                "☼►$$&@@☼" +
                "☼######☼" +
                "☼☼☼☼☼☼☼☼");

        hero.right();
        game.tick();

        verify(listener).event(Events.GET_YELLOW_GOLD);
        verifyNoMoreInteractions(listener);
        reset(listener);

        assertE("☼☼☼☼☼☼☼☼" +
                "☼      ☼" +
                "☼      ☼" +
                "☼      ☼" +
                "☼      ☼" +
                "☼ ►$&@@☼" +
                "☼######☼" +
                "☼☼☼☼☼☼☼☼");

        hero.right();
        game.tick();

        verify(listener).event(Events.GET_YELLOW_GOLD);
        verifyNoMoreInteractions(listener);
        reset(listener);

        assertE("☼☼☼☼☼☼☼☼" +
                "☼      ☼" +
                "☼      ☼" +
                "☼      ☼" +
                "☼      ☼" +
                "☼  ►&@@☼" +
                "☼######☼" +
                "☼☼☼☼☼☼☼☼");

        hero.right();
        game.tick();

        verify(listener).event(Events.GET_GREEN_GOLD);
        verifyNoMoreInteractions(listener);
        reset(listener);

        assertE("☼☼☼☼☼☼☼☼" +
                "☼      ☼" +
                "☼      ☼" +
                "☼      ☼" +
                "☼      ☼" +
                "☼   ►@@☼" +
                "☼######☼" +
                "☼☼☼☼☼☼☼☼");

        hero.right();
        game.tick();

        verify(listener).event(Events.GET_RED_GOLD);
        verifyNoMoreInteractions(listener);
        reset(listener);

        assertE("☼☼☼☼☼☼☼☼" +
                "☼      ☼" +
                "☼      ☼" +
                "☼      ☼" +
                "☼      ☼" +
                "☼    ►@☼" +
                "☼######☼" +
                "☼☼☼☼☼☼☼☼");

        hero.right();
        game.tick();

        verify(listener).event(Events.GET_RED_GOLD);
        verifyNoMoreInteractions(listener);
        reset(listener);

        assertE("☼☼☼☼☼☼☼☼" +
                "☼      ☼" +
                "☼      ☼" +
                "☼      ☼" +
                "☼      ☼" +
                "☼     ►☼" +
                "☼######☼" +
                "☼☼☼☼☼☼☼☼");
    }

    @Test
    public void shouldResetGold_whenClearBoard() {
        // given
        shouldCollectAllGold();

        // when
        dice(1, 2);
        game.clearScore();
        reloadAllHeroes();

        assertE("☼☼☼☼☼☼☼☼" +
                "☼      ☼" +
                "☼      ☼" +
                "☼      ☼" +
                "☼      ☼" +
                "☼►$$&@@☼" +
                "☼######☼" +
                "☼☼☼☼☼☼☼☼");

        // when
        // добавим еще золота
        settings.integer(GOLD_COUNT_YELLOW, settings.integer(GOLD_COUNT_YELLOW) + 2)
                .integer(GOLD_COUNT_RED,    settings.integer(GOLD_COUNT_RED) + 3)
                .integer(GOLD_COUNT_GREEN,  settings.integer(GOLD_COUNT_GREEN) + 1);
        dice(
            2, 3, // yellow
            3, 3, // yellow
            4, 3, // green
            5, 3, // red
            6, 3, // red
            6, 4, // red
            1, 6  // герой
        );
        game.clearScore();
        reloadAllHeroes();

        assertE("☼☼☼☼☼☼☼☼" +
                "☼[     ☼" +
                "☼      ☼" +
                "☼     @☼" +
                "☼ $$&@@☼" +
                "☼ $$&@@☼" +
                "☼######☼" +
                "☼☼☼☼☼☼☼☼");

        // when
        // удалим золота
        settings.integer(GOLD_COUNT_YELLOW, 1)
                .integer(GOLD_COUNT_RED,    1)
                .integer(GOLD_COUNT_GREEN,  1);

        dice(2, 6);  // герой
        game.clearScore();
        reloadAllHeroes();

        assertE("☼☼☼☼☼☼☼☼" +
                "☼ [    ☼" +
                "☼      ☼" +
                "☼      ☼" +
                "☼      ☼" +
                "☼ $ &@ ☼" +
                "☼######☼" +
                "☼☼☼☼☼☼☼☼");
    }

    @Test
    public void shouldEndlesslyWalkThroughThePortals_untilExit() {
        // given
        settings.integer(PORTALS_COUNT, 3);

        givenFl("☼☼☼☼☼☼☼☼" +
                "☼   ⊛  ☼" +
                "☼      ☼" +
                "☼   ⊛  ☼" +
                "☼      ☼" +
                "☼►⊛    ☼" +
                "☼######☼" +
                "☼☼☼☼☼☼☼☼");

        game.tick();

        assertE("☼☼☼☼☼☼☼☼" +
                "☼   ⊛  ☼" +
                "☼      ☼" +
                "☼   ⊛  ☼" +
                "☼      ☼" +
                "☼►⊛    ☼" +
                "☼######☼" +
                "☼☼☼☼☼☼☼☼");

        hero.right();
        game.tick();

        assertE("☼☼☼☼☼☼☼☼" +
                "☼   [  ☼" +
                "☼      ☼" +
                "☼   ⊛  ☼" +
                "☼      ☼" +
                "☼ ⊛    ☼" +
                "☼######☼" +
                "☼☼☼☼☼☼☼☼");

        game.tick();

        assertE("☼☼☼☼☼☼☼☼" +
                "☼   ⊛  ☼" +
                "☼   [  ☼" +
                "☼   ⊛  ☼" +
                "☼      ☼" +
                "☼ ⊛    ☼" +
                "☼######☼" +
                "☼☼☼☼☼☼☼☼");

        game.tick();

        assertE("☼☼☼☼☼☼☼☼" +
                "☼   ⊛  ☼" +
                "☼      ☼" +
                "☼   ⊛  ☼" +
                "☼      ☼" +
                "☼ ►    ☼" +
                "☼######☼" +
                "☼☼☼☼☼☼☼☼");

        // что интересно, если ты не выйдешь из портала то в следующий тик отправишься дальше
        game.tick();

        assertE("☼☼☼☼☼☼☼☼" +
                "☼   [  ☼" +
                "☼      ☼" +
                "☼   ⊛  ☼" +
                "☼      ☼" +
                "☼ ⊛    ☼" +
                "☼######☼" +
                "☼☼☼☼☼☼☼☼");

        game.tick();

        assertE("☼☼☼☼☼☼☼☼" +
                "☼   ⊛  ☼" +
                "☼   [  ☼" +
                "☼   ⊛  ☼" +
                "☼      ☼" +
                "☼ ⊛    ☼" +
                "☼######☼" +
                "☼☼☼☼☼☼☼☼");

        game.tick();

        assertE("☼☼☼☼☼☼☼☼" +
                "☼   ⊛  ☼" +
                "☼      ☼" +
                "☼   ⊛  ☼" +
                "☼      ☼" +
                "☼ ►    ☼" +
                "☼######☼" +
                "☼☼☼☼☼☼☼☼");

        hero.right();
        game.tick();

        assertE("☼☼☼☼☼☼☼☼" +
                "☼   ⊛  ☼" +
                "☼      ☼" +
                "☼   ⊛  ☼" +
                "☼      ☼" +
                "☼ ⊛►   ☼" +
                "☼######☼" +
                "☼☼☼☼☼☼☼☼");
    }

    @Test
    public void shouldResetPortals_whenClearBoard() {
        shouldEndlesslyWalkThroughThePortals_untilExit();

        assertE("☼☼☼☼☼☼☼☼" +
                "☼   ⊛  ☼" +
                "☼      ☼" +
                "☼   ⊛  ☼" +
                "☼      ☼" +
                "☼ ⊛►   ☼" +
                "☼######☼" +
                "☼☼☼☼☼☼☼☼");

        // when
        // добавим еще один портал
        settings.integer(PORTALS_COUNT, settings.integer(PORTALS_COUNT) + 1);
        dice(2, 4, // new portal
            1, 2); // hero
        game.clearScore();
        reloadAllHeroes();

        assertE("☼☼☼☼☼☼☼☼" +
                "☼   ⊛  ☼" +
                "☼      ☼" +
                "☼ ⊛ ⊛  ☼" +
                "☼      ☼" +
                "☼►⊛    ☼" +
                "☼######☼" +
                "☼☼☼☼☼☼☼☼");

        // when
        // оставим два портала
        settings.integer(PORTALS_COUNT, 2);
        dice(1, 2); // hero
        game.clearScore();
        reloadAllHeroes();

        assertE("☼☼☼☼☼☼☼☼" +
                "☼   ⊛  ☼" +
                "☼      ☼" +
                "☼   ⊛  ☼" +
                "☼      ☼" +
                "☼ ►    ☼" +
                "☼######☼" +
                "☼☼☼☼☼☼☼☼");
    }

    @Test
    public void shouldResetPills_whenClearBoard() {
        // given
        settings.integer(SHADOW_PILLS_COUNT, 3);

        givenFl("☼☼☼☼☼☼☼☼" +
                "☼      ☼" +
                "☼      ☼" +
                "☼      ☼" +
                "☼      ☼" +
                "☼►S S S☼" +
                "☼######☼" +
                "☼☼☼☼☼☼☼☼");

        game.tick();

        assertE("☼☼☼☼☼☼☼☼" +
                "☼      ☼" +
                "☼      ☼" +
                "☼      ☼" +
                "☼      ☼" +
                "☼►S S S☼" +
                "☼######☼" +
                "☼☼☼☼☼☼☼☼");

        assertEquals(false, hero.under(Pill.PillType.SHADOW_PILL));

        hero.right();
        game.tick();

        hero.right();
        game.tick();

        hero.right();
        game.tick();

        hero.right();
        game.tick();

        hero.right();
        game.tick();

        assertE("☼☼☼☼☼☼☼☼" +
                "☼      ☼" +
                "☼      ☼" +
                "☼      ☼" +
                "☼      ☼" +
                "☼     ⊳☼" +
                "☼######☼" +
                "☼☼☼☼☼☼☼☼");

        assertEquals(true, hero.under(Pill.PillType.SHADOW_PILL));

        // when
        // почистим все
        dice(1, 2);  // hero
        game.clearScore();
        reloadAllHeroes();

        assertEquals(false, hero.under(Pill.PillType.SHADOW_PILL));

        assertE("☼☼☼☼☼☼☼☼" +
                "☼      ☼" +
                "☼      ☼" +
                "☼      ☼" +
                "☼      ☼" +
                "☼►S S S☼" +
                "☼######☼" +
                "☼☼☼☼☼☼☼☼");

        // when
        // добавим еще
        settings.integer(SHADOW_PILLS_COUNT, settings.integer(SHADOW_PILLS_COUNT) + 2);
        dice(
                3, 3, // new pills
                5, 3,
                1, 6  // hero
        );
        game.clearScore();
        reloadAllHeroes();

        assertE("☼☼☼☼☼☼☼☼" +
                "☼[     ☼" +
                "☼      ☼" +
                "☼      ☼" +
                "☼  S S ☼" +
                "☼ S S S☼" +
                "☼######☼" +
                "☼☼☼☼☼☼☼☼");

        // when
        // оставим только 1
        settings.integer(SHADOW_PILLS_COUNT, 1);
        dice(1, 2);  // hero
        game.clearScore();
        reloadAllHeroes();

        assertE("☼☼☼☼☼☼☼☼" +
                "☼      ☼" +
                "☼      ☼" +
                "☼      ☼" +
                "☼      ☼" +
                "☼►S    ☼" +
                "☼######☼" +
                "☼☼☼☼☼☼☼☼");
    }

    @Test
    public void shouldRemoveOldWalls_whenClearBoard() {
        // given
        givenFl("☼☼☼☼☼☼☼☼" +
                "☼      ☼" +
                "☼      ☼" +
                "☼      ☼" +
                "☼      ☼" +
                "☼     ►☼" +
                "☼######☼" +
                "☼☼☼☼☼☼☼☼");

        game.tick();

        assertE("☼☼☼☼☼☼☼☼" +
                "☼      ☼" +
                "☼      ☼" +
                "☼      ☼" +
                "☼      ☼" +
                "☼     ►☼" +
                "☼######☼" +
                "☼☼☼☼☼☼☼☼");

        assertEquals(7 * 4, game.borders().size());

        // when
        dice(1, 2); // new hero
        game.clearScore();

        // then
        assertEquals(7 * 4, game.borders().size());

        assertE("☼☼☼☼☼☼☼☼" +
                "☼      ☼" +
                "☼      ☼" +
                "☼      ☼" +
                "☼      ☼" +
                "☼►     ☼" +
                "☼######☼" +
                "☼☼☼☼☼☼☼☼");

        // when
        dice(2, 2); // new hero
        game.clearScore();

        // then
        assertEquals(7 * 4, game.borders().size());

        assertE("☼☼☼☼☼☼☼☼" +
                "☼      ☼" +
                "☼      ☼" +
                "☼      ☼" +
                "☼      ☼" +
                "☼ ►    ☼" +  // TODO героя приходится смещать, потому что при очистке его прошлое место занято им самим
                "☼######☼" +
                "☼☼☼☼☼☼☼☼");
    }

    @Test
    public void shouldResetPortalsTimeout_whenClearBoard() {
        portalsAreRecreatedEveryFewTicks();

        assertE("☼☼☼☼☼" +
                "☼⊛⊛ ☼" +
                "☼  ◄☼" +
                "☼###☼" +
                "☼☼☼☼☼");

        assertEquals(5, game.getPortalsTimer());

        game.tick();
        game.tick();
        game.tick();

        assertEquals(2, game.getPortalsTimer());

        // when
        dice(3, 3, // new portals
            3, 2,
            1, 2); // new hero
        game.clearScore();

        // then
        assertEquals(5, game.getPortalsTimer());
    }

    private void reloadAllHeroes() {
        players = game.players();
        player = players.get(0);

        heroes = game.allHeroes();
        hero = heroes.get(0);
    }

    // сверлить находясь на трубе нельзя, в оригинале только находясь на краю трубы

    // карта намного больше, чем квардартик вьюшка, и я подходя к границе просто передвигаю вьюшку
    // повляется многопользовательский режим игры в формате "стенка на стенку"

}
