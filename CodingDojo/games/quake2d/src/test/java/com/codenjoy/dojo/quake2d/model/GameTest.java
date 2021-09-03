package com.codenjoy.dojo.quake2d.model;

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

import com.codenjoy.dojo.quake2d.services.GameSettings;
import com.codenjoy.dojo.services.Dice;
import com.codenjoy.dojo.services.EventListener;
import com.codenjoy.dojo.services.printer.PrinterFactory;
import com.codenjoy.dojo.services.printer.PrinterFactoryImpl;
import com.codenjoy.dojo.utils.TestUtils;
import org.junit.Before;
import org.junit.Test;
import org.mockito.stubbing.OngoingStubbing;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class GameTest {

    private Quake2D game;
    private Hero hero;
    private Dice dice;
    private EventListener listener;
    private Player player;
    private Player otherPlayer;
    private Hero otherHero;
    private PrinterFactory printer;
    private GameSettings settings;

    @Before
    public void setup() {
        dice = mock(Dice.class);
        printer = new PrinterFactoryImpl();
        settings = new GameSettings();
    }

    private void dice(int...ints) {
        OngoingStubbing<Integer> when = when(dice.next(anyInt()));
        for (int i : ints) {
            when = when.thenReturn(i);
        }
    }

    private void givenFl(String board) {
        Level level = new Level(board);
        hero = level.getHero().get(0);

        game = new Quake2D(level, dice, settings);
        listener = mock(EventListener.class);
        player = new Player(listener, settings);
        dice(hero.getX(), hero.getY()); // позиция рассчитывается рендомно из dice
        player.setHero(hero);
        game.newGame(player);
        hero.setHealth(Hero.START_HEALTH - hero.getHealth());


        if (level.getOtherHero().size() != 0){
            otherHero = level.getOtherHero().get(0);
            otherPlayer = new Player(listener, settings);
            otherPlayer.setHero(otherHero);
            game.newGame(otherPlayer);
            otherHero.setHealth(Hero.START_HEALTH - otherHero.getHealth());
        }


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
                "☼ ☺ ☼" +
                "☼   ☼" +
                "☼☼☼☼☼");

        assertE("☼☼☼☼☼" +
                "☼   ☼" +
                "☼ ☺ ☼" +
                "☼   ☼" +
                "☼☼☼☼☼");
    }

    // я ходить
    @Test
    public void shouldWalk() {
        givenFl("☼☼☼☼☼" +
                "☼   ☼" +
                "☼ ☺ ☼" +
                "☼   ☼" +
                "☼☼☼☼☼");

        hero.left();
        game.tick();

        assertE("☼☼☼☼☼" +
                "☼   ☼" +
                "☼☺  ☼" +
                "☼   ☼" +
                "☼☼☼☼☼");

        hero.right();
        game.tick();

        assertE("☼☼☼☼☼" +
                "☼   ☼" +
                "☼ ☺ ☼" +
                "☼   ☼" +
                "☼☼☼☼☼");

        hero.up();
        game.tick();

        assertE("☼☼☼☼☼" +
                "☼ ☺ ☼" +
                "☼   ☼" +
                "☼   ☼" +
                "☼☼☼☼☼");

        hero.down();
        game.tick();

        assertE("☼☼☼☼☼" +
                "☼   ☼" +
                "☼ ☺ ☼" +
                "☼   ☼" +
                "☼☼☼☼☼");
    }

    // если небыло команды я никуда не иду
    @Test
    public void shouldStopWhenNoMoreRightCommand() {
        givenFl("☼☼☼☼☼" +
                "☼   ☼" +
                "☼  ☺☼" +
                "☼   ☼" +
                "☼☼☼☼☼");

        hero.left();
        game.tick();

        assertE("☼☼☼☼☼" +
                "☼   ☼" +
                "☼ ☺ ☼" +
                "☼   ☼" +
                "☼☼☼☼☼");

        game.tick();

        assertE("☼☼☼☼☼" +
                "☼   ☼" +
                "☼ ☺ ☼" +
                "☼   ☼" +
                "☼☼☼☼☼");
    }

    // я останавливаюсь возле границы
    @Test
    public void shouldStopWhenWallRight() {
        givenFl("☼☼☼☼☼" +
                "☼   ☼" +
                "☼  ☺☼" +
                "☼   ☼" +
                "☼☼☼☼☼");

        hero.right();
        game.tick();

        assertE("☼☼☼☼☼" +
                "☼   ☼" +
                "☼  ☺☼" +
                "☼   ☼" +
                "☼☼☼☼☼");
    }

    @Test
    public void shouldStopWhenWallLeft() {
        givenFl("☼☼☼☼☼" +
                "☼   ☼" +
                "☼☺  ☼" +
                "☼   ☼" +
                "☼☼☼☼☼");

        hero.left();
        game.tick();

        assertE("☼☼☼☼☼" +
                "☼   ☼" +
                "☼☺  ☼" +
                "☼   ☼" +
                "☼☼☼☼☼");
    }

    @Test
    public void shouldStopWhenWallUp() {
        givenFl("☼☼☼☼☼" +
                "☼ ☼ ☼" +
                "☼ ☺ ☼" +
                "☼   ☼" +
                "☼☼☼☼☼");

        hero.up();
        game.tick();

        assertE("☼☼☼☼☼" +
                "☼ ☼ ☼" +
                "☼ ☺ ☼" +
                "☼   ☼" +
                "☼☼☼☼☼");
    }

    @Test
    public void shouldStopWhenWallDown() {
        givenFl("☼☼☼☼☼" +
                "☼   ☼" +
                "☼ ☺ ☼" +
                "☼ ☼ ☼" +
                "☼☼☼☼☼");

        hero.down();
        game.tick();

        assertE("☼☼☼☼☼" +
                "☼   ☼" +
                "☼ ☺ ☼" +
                "☼ ☼ ☼" +
                "☼☼☼☼☼");
    }

    // я могу оставить бомбу
    @Test
    public void shouldMakeFire() {
        givenFl("☼☼☼☼☼" +
                "☼   ☼" +
                "☼   ☼" +
                "☼ ☺ ☼" +
                "☼☼☼☼☼");

        hero.up();
        game.tick();
        hero.act();
        game.tick();

        assertE("☼☼☼☼☼" +
                "☼ * ☼" +
                "☼ ☺ ☼" +
                "☼   ☼" +
                "☼☼☼☼☼");
    }

    @Test
    public void shouldBulletMove() {
        givenFl("☼☼☼☼☼☼" +
                "☼    ☼" +
                "☼    ☼" +
                "☼    ☼" +
                "☼ ☺  ☼" +
                "☼☼☼☼☼☼");

        hero.up();
        game.tick();
        hero.act();
        game.tick();
        game.tick();

        assertE("☼☼☼☼☼☼" +
                "☼ *  ☼" +
                "☼    ☼" +
                "☼ ☺  ☼" +
                "☼    ☼" +
                "☼☼☼☼☼☼");
    }

    @Test
    public void shouldDestroyBulletOnWall() {
        givenFl("☼☼☼☼☼☼" +
                "☼    ☼" +
                "☼    ☼" +
                "☼    ☼" +
                "☼ ☺  ☼" +
                "☼☼☼☼☼☼");

        hero.up();
        game.tick();
        hero.act();
        game.tick();
        game.tick();
        game.tick();

        assertE("☼☼☼☼☼☼" +
                "☼    ☼" +
                "☼    ☼" +
                "☼ ☺  ☼" +
                "☼    ☼" +
                "☼☼☼☼☼☼");
    }

    @Test
    public void shouldBulletHitOtherHero() {
        givenFl("☼☼☼☼☼☼" +
                "☼ ☻  ☼" +
                "☼    ☼" +
                "☼    ☼" +
                "☼ ☺  ☼" +
                "☼☼☼☼☼☼");

        hero.up();
        game.tick();
        assertE("☼☼☼☼☼☼" +
                "☼ ☻  ☼" +
                "☼    ☼" +
                "☼ ☺  ☼" +
                "☼    ☼" +
                "☼☼☼☼☼☼");


        hero.act();
        game.tick();

        assertE("☼☼☼☼☼☼" +
                "☼ ☻  ☼" +
                "☼ *  ☼" +
                "☼ ☺  ☼" +
                "☼    ☼" +
                "☼☼☼☼☼☼");
        otherHero.setHealth(-(Hero.START_HEALTH-Bullet.START_DAMAGE));
        game.tick();

        assertE("☼☼☼☼☼☼" +
                "☼ X  ☼" +
                "☼    ☼" +
                "☼ ☺  ☼" +
                "☼    ☼" +
                "☼☼☼☼☼☼");
    }

    @Test
    public void shouldOtherHeroRunawayFromBullet() {
        givenFl("☼☼☼☼☼☼" +
                "☼ ☻  ☼" +
                "☼    ☼" +
                "☼    ☼" +
                "☼ ☺  ☼" +
                "☼☼☼☼☼☼");

        hero.up();
        game.tick();
        assertE("☼☼☼☼☼☼" +
                "☼ ☻  ☼" +
                "☼    ☼" +
                "☼ ☺  ☼" +
                "☼    ☼" +
                "☼☼☼☼☼☼");


        hero.act();
        game.tick();

        assertE("☼☼☼☼☼☼" +
                "☼ ☻  ☼" +
                "☼ *  ☼" +
                "☼ ☺  ☼" +
                "☼    ☼" +
                "☼☼☼☼☼☼");
        otherHero.right();
        game.tick();

        assertE("☼☼☼☼☼☼" +
                "☼ *☻ ☼" +
                "☼    ☼" +
                "☼ ☺  ☼" +
                "☼    ☼" +
                "☼☼☼☼☼☼");

        game.tick();

        assertE("☼☼☼☼☼☼" +
                "☼  ☻ ☼" +
                "☼    ☼" +
                "☼ ☺  ☼" +
                "☼    ☼" +
                "☼☼☼☼☼☼");
    }

    @Test
    public void shouldRessurectAfterDeath() {
        givenFl("☼☼☼☼☼☼" +
                "☼ ☻  ☼" +
                "☼    ☼" +
                "☼    ☼" +
                "☼ ☺  ☼" +
                "☼☼☼☼☼☼");

        hero.up();
        game.tick();
        assertE("☼☼☼☼☼☼" +
                "☼ ☻  ☼" +
                "☼    ☼" +
                "☼ ☺  ☼" +
                "☼    ☼" +
                "☼☼☼☼☼☼");


        hero.act();
        game.tick();

        assertE("☼☼☼☼☼☼" +
                "☼ ☻  ☼" +
                "☼ *  ☼" +
                "☼ ☺  ☼" +
                "☼    ☼" +
                "☼☼☼☼☼☼");

        otherHero.setHealth(-(Hero.START_HEALTH-Bullet.START_DAMAGE));
        game.tick();

        assertE("☼☼☼☼☼☼" +
                "☼ X  ☼" +
                "☼    ☼" +
                "☼ ☺  ☼" +
                "☼    ☼" +
                "☼☼☼☼☼☼");

        game.tick();
        otherHero.move(4, 3);
        assertE("☼☼☼☼☼☼" +
                "☼    ☼" +
                "☼   ☻☼" +
                "☼ ☺  ☼" +
                "☼    ☼" +
                "☼☼☼☼☼☼");
    }

    //проверка появления ability
    @Test
    public void shouldShowAbility() {
        givenFl("☼☼☼☼☼" +
                "☼   ☼" +
                "☼ ☺ ☼" +
                "☼   ☼" +
                "☼☼☼☼☼");


        dice(3, 3, Ability.Type.WEAPON.ordinal());
        makeTicks(Quake2D.ABILITY_TIME_EXIST);

        assertE("☼☼☼☼☼" +
                "☼  ~☼" +
                "☼ ☺ ☼" +
                "☼   ☼" +
                "☼☼☼☼☼");
    }

    @Test
    public void shouldHasHeroAbility() {
        givenFl("☼☼☼☼☼" +
                "☼   ☼" +
                "☼ ☺ ☼" +
                "☼   ☼" +
                "☼☼☼☼☼");


        dice(3, 3, Ability.Type.DEFENCE.ordinal());
        makeTicks(Quake2D.ABILITY_TIME_EXIST);
        hero.up();
        game.tick();
        hero.right();
        game.tick();

        assertE("☼☼☼☼☼" +
                "☼  ☺☼" +
                "☼   ☼" +
                "☼   ☼" +
                "☼☼☼☼☼");
        assertNotNull(hero.getAbility());
    }

    private void makeTicks(int count){
        for (int i = count; i != 0; i--) {
            game.tick();
        }
    }

    @Test
    public void shouldIncreaseDamageAfterAbility() {
        givenFl("☼☼☼☼☼☼" +
                "☼ ☻  ☼" +
                "☼    ☼" +
                "☼    ☼" +
                "☼ ☺  ☼" +
                "☼☼☼☼☼☼");

        dice(2, 2, Ability.Type.WEAPON.ordinal());
        makeTicks(Quake2D.ABILITY_TIME_EXIST);
        hero.up();
        game.tick();
        assertE("☼☼☼☼☼☼" +
                "☼ ☻  ☼" +
                "☼    ☼" +
                "☼ ☺  ☼" +
                "☼    ☼" +
                "☼☼☼☼☼☼");


        hero.act();
        game.tick();

        assertE("☼☼☼☼☼☼" +
                "☼ ☻  ☼" +
                "☼ *  ☼" +
                "☼ ☺  ☼" +
                "☼    ☼" +
                "☼☼☼☼☼☼");

//        otherHero.setHealth(Bullet.START_DAMAGE);
        game.tick();

        assertE("☼☼☼☼☼☼" +
                "☼ ☻  ☼" +
                "☼    ☼" +
                "☼ ☺  ☼" +
                "☼    ☼" +
                "☼☼☼☼☼☼");
        assertEquals(otherHero.getHealth(), Hero.START_HEALTH - Bullet.START_DAMAGE*Bullet.WEAPON_MULTIPLICATOR);
//
//        game.tick();
//        otherHero.move(4, 3);
//        otherHero = otherHero;
//
//        assertE("☼☼☼☼☼☼" +
//                "☼    ☼" +
//                "☼   ☻☼" +
//                "☼ ☺  ☼" +
//                "☼    ☼" +
//                "☼☼☼☼☼☼");
    }

    @Test
    public void shouldWorkHealthAbility() {
        givenFl("☼☼☼☼☼" +
                "☼   ☼" +
                "☼ ☺ ☼" +
                "☼   ☼" +
                "☼☼☼☼☼");


        dice(3, 3, Ability.Type.HEALTH.ordinal());
        makeTicks(Quake2D.ABILITY_TIME_EXIST);
        hero.up();
        game.tick();
        hero.setHealth(-(Bullet.START_DAMAGE + Ability.HEALTH_BONUS));
        hero.right();
        game.tick();

        assertE("☼☼☼☼☼" +
                "☼  ☺☼" +
                "☼   ☼" +
                "☼   ☼" +
                "☼☼☼☼☼");
        assertEquals(Hero.START_HEALTH - Bullet.START_DAMAGE + (Ability.HEALTH_BONUS - Ability.HEALTH_BONUS), hero.getHealth());
    }

    @Test
    public void shouldOtherHeroBecomeSuperHero() {
        givenFl("☼☼☼☼☼☼" +
                "☼ ☺  ☼" +
                "☼    ☼" +
                "☼    ☼" +
                "☼ ☻  ☼" +
                "☼☼☼☼☼☼");
        makeTicks(Quake2D.ABILITY_TIME_EXIST);
        game.getAbilities().get(0).move(2, 2);
        otherHero.up();
        game.tick();
        assertE("☼☼☼☼☼☼" +
                "☼ ☺  ☼" +
                "☼    ☼" +
                "☼ Š  ☼" +
                "☼    ☼" +
                "☼☼☼☼☼☼");
    }
    // проверить, что если новому обекту не где появится то программа не зависает - там бесконечный цикл потенциальный есть
    @Test(timeout = 1000)
    public void shouldNoDeadLoopWhenNewObjectCreation() {
//        givenFl("☼☼☼☼☼" +
//                "☼   ☼" +
//                "☼ ☺$☼" +
//                "☼   ☼" +
//                "☼☼☼☼☼");
//
//        dice(2, 2);
//        hero.right();
//        game.tick();
//        verify(listener).event(Events.WIN);
//
//        assertE("☼☼☼☼☼" +
//                "☼   ☼" +
//                "☼ $☺☼" +
//                "☼   ☼" +
//                "☼☼☼☼☼");
    }

    // я не могу ставить две бомбы на одной клетке

}
