package com.codenjoy.dojo.startandjump.model;

import com.codenjoy.dojo.startandjump.services.HeroStatus;
import com.codenjoy.dojo.services.PrinterFactory;
import com.codenjoy.dojo.startandjump.model.StartAndJump;
import com.codenjoy.dojo.utils.TestUtils;
import com.codenjoy.dojo.services.Dice;
import com.codenjoy.dojo.services.EventListener;
import com.codenjoy.dojo.services.PrinterFactoryImpl;
import junit.framework.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.stubbing.OngoingStubbing;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertTrue;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Mockito.*;

/**
 * User: sanja
 * Date: 17.12.13
 * Time: 4:47
 */
public class StartAndJumpTest {

    private StartAndJump game;
    private Hero hero;
    private Dice dice;
    private EventListener listener;
    private Player player;
    private PrinterFactory printer = new PrinterFactoryImpl();

    @Before
    public void setup() {
        dice = mock(Dice.class);
    }

    private void dice(int... ints) {
        OngoingStubbing<Integer> when = when(dice.next(anyInt()));
        for (int i : ints) {
            when = when.thenReturn(i);
        }
    }

    private void givenFl(String board) {
        LevelImpl level = new LevelImpl(board);
        Hero hero = level.getHero().get(0);

        game = new StartAndJump(dice, level);
        listener = mock(EventListener.class);
        player = new Player(listener);
        game.newGame(player);
        player.hero = hero;
        this.hero = game.getHeroes().get(0);
    }

    private void assertE(String expected) {
        assertEquals(TestUtils.injectN(expected),
                printer.getPrinter(game.reader(), player).print());
    }

    // есть карта со мной
    @Test
    public void shouldFieldAtStart() {
        givenFl("#######" +
                "       " +
                "       " +
                "☺      " +
                "       " +
                "       " +
                "#######");

        assertE("#######" +
                "       " +
                "       " +
                "☺      " +
                "       " +
                "       " +
                "#######");
    }

    @Test
    public void shouldFieldWithPlatformAtStart() {
        givenFl("#######" +
                "       " +
                "       " +
                "☺      " +
                "=======" +
                "       " +
                "#######");

        assertE("#######" +
                "       " +
                "       " +
                "☺      " +
                "=======" +
                "       " +
                "#######");
    }

    @Test
    public void shouldMoveLeftPlatform() {
        givenFl("#######" +
                "       " +
                "       " +
                "☺      " +
                " =     " +
                "       " +
                "#######");

        game.tick();


        assertE("#######" +
                "       " +
                "       " +
                "☺      " +
                "=      " +
                "       " +
                "#######");
    }

    @Test
    public void shouldDestroyPlatformOutOfBorders() {
        givenFl("#######" +
                "       " +
                "       " +
                "☺      " +
                "==     " +
                "       " +
                "#######");

        game.tick();


        assertE("#######" +
                "       " +
                "       " +
                "☺      " +
                "=      " +
                "       " +
                "#######");

        Assert.assertEquals(1, game.getPlatforms().size());
    }

    @Test
    public void shouldDestroyAllPlatformsOutOfBorders() {
        givenFl("=      " +
                "=      " +
                "=      " +
                "☺      " +
                "==     " +
                "=      " +
                "#######");

        game.tick();


        assertE("       " +
                "       " +
                "       " +
                "☺      " +
                "=      " +
                "       " +
                "#######");

        Assert.assertEquals(1, game.getPlatforms().size());
    }

    @Test
    public void shouldHeroDiesWhenCollision() {
        givenFl("       " +
                "       " +
                "       " +
                "☺=     " +
                "==     " +
                "       " +
                "#######");

        game.tick();

        assertE("       " +
                "       " +
                "       " +
                "☻      " +
                "=      " +
                "       " +
                "#######");

        assertFalse(hero.isAlive());

        game.newGame(player);

        assertE("       " +
                "       " +
                "       " +
                "☺=     " +
                "==     " +
                "       " +
                "#######");
    }

    @Test
    public void shouldDestroyAllPlatforms() {
        givenFl("       " +
                "       " +
                "       " +
                "☺      " +
                "=======" +
                "       " +
                "#######");

        game.tick();
        game.tick();
        game.tick();
        game.tick();
        game.tick();
        game.tick();
        game.tick();

        assertE("       " +
                "       " +
                "       " +
                "☺      " +
                "       " +
                "       " +
                "#######");
    }

    @Test
    public void shouldCreateOnePlatform() {
        givenFl("       " +
                "       " +
                "       " +
                "☺      " +
                "=====  " +
                "       " +
                "#######");

        dice(2 - 1, 2);

        game.tick();

        assertE("       " +
                "       " +
                "       " +
                "☺      " +
                "====  =" +
                "       " +
                "#######");
    }

    @Test
    public void shouldHeroFall() {
        givenFl("       " +
                "       " +
                "       " +
                "☺      " +
                "=      " +
                "       " +
                "#######");

        game.tick();

        assertE("       " +
                "       " +
                "       " +
                "☺      " +
                "       " +
                "       " +
                "#######");

        game.tick();

        assertE("       " +
                "       " +
                "       " +
                "       " +
                "☺      " +
                "       " +
                "#######");
    }

    @Test
    public void shouldHeroStopsOnPlatformWhenFalling() {
        givenFl("       " +
                "       " +
                "       " +
                "☺      " +
                "=      " +
                "  =    " +
                "#######");

        game.tick();

        assertE("       " +
                "       " +
                "       " +
                "☺      " +
                "       " +
                " =     " +
                "#######");

        game.tick();

        assertE("       " +
                "       " +
                "       " +
                "       " +
                "☺      " +
                "=      " +
                "#######");
        game.tick();

        assertE("       " +
                "       " +
                "       " +
                "       " +
                "☺      " +
                "       " +
                "#######");

        game.tick();

        assertE("       " +
                "       " +
                "       " +
                "       " +
                "       " +
                "☺      " +
                "#######");
    }

    @Test
    public void shouldHeroDiesOnBottomWall() {
        givenFl("       " +
                "       " +
                "       " +
                "       " +
                "       " +
                "☺      " +
                "#######");

        game.tick();
        game.tick();

        assertE("       " +
                "       " +
                "       " +
                "       " +
                "       " +
                "       " +
                "☻######");

        assertFalse(hero.isAlive());

        game.newGame(player);

        assertE("       " +
                "       " +
                "       " +
                "☺      " +
                "       " +
                "       " +
                "#######");
    }

    @Test
    public void shouldHeroDiesFullLifeCycle() {
        givenFl("       " +
                "       " +
                "       " +
                "☺      " +
                "=      " +
                "  =    " +
                "#######");

        game.tick();

        assertE("       " +
                "       " +
                "       " +
                "☺      " +
                "       " +
                " =     " +
                "#######");

        game.tick();

        assertE("       " +
                "       " +
                "       " +
                "       " +
                "☺      " +
                "=      " +
                "#######");

        game.tick();

        assertE("       " +
                "       " +
                "       " +
                "       " +
                "☺      " +
                "       " +
                "#######");

        game.tick();

        assertE("       " +
                "       " +
                "       " +
                "       " +
                "       " +
                "☺      " +
                "#######");

        game.tick();

        assertE("       " +
                "       " +
                "       " +
                "       " +
                "       " +
                "       " +
                "☻######");

        assertFalse(hero.isAlive());

        game.newGame(player);

        assertE("       " +
                "       " +
                "       " +
                "☺      " +
                "=      " +
                "  =    " +
                "#######");
    }

    @Test
    public void shouldJump() {
        givenFl("      =" +
                "       " +
                "       " +
                "☺      " +
                "=      " +
                "       " +
                "#######");

        hero.up();
        game.tick();

        assertE("     = " +
                "       " +
                "☺      " +
                "       " +
                "       " +
                "       " +
                "#######");

        game.tick();

        assertE("    =  " +
                "       " +
                "       " +
                "☺      " +
                "       " +
                "       " +
                "#######");
    }

    @Test
    public void shouldJumpOnPlatform() {
        givenFl("       " +
                "       " +
                "       " +
                "☺      " +
                "= ==   " +
                "       " +
                "#######");

        hero.up();
        game.tick();

        assertE("       " +
                "       " +
                "☺      " +
                "       " +
                " ==    " +
                "       " +
                "#######");

        game.tick();

        assertE("       " +
                "       " +
                "       " +
                "☺      " +
                "==     " +
                "       " +
                "#######");

        game.tick();

        assertE("       " +
                "       " +
                "       " +
                "☺      " +
                "=      " +
                "       " +
                "#######");
    }

    @Test
    public void shouldJumpAboveOnPlatform() {
        givenFl("       " +
                "       " +
                "       " +
                "☺ ==   " +
                "=      " +
                "       " +
                "#######");

        hero.up();
        game.tick();

        assertE("       " +
                "       " +
                "☺      " +
                " ==    " +
                "       " +
                "       " +
                "#######");
        game.tick();

        assertE("       " +
                "       " +
                "☺      " +
                "==     " +
                "       " +
                "       " +
                "#######");

        game.tick();

        assertE("       " +
                "       " +
                "☺      " +
                "=      " +
                "       " +
                "       " +
                "#######");
    }


    @Test
    public void shouldHeroNotDieWhenFallsOnPlatform() {
        givenFl("☺      " +
                "       " +
                "       " +
                "       " +
                "       " +
                "     ==" +
                "#######");

        dice(1 - 1, 10);

        game.tick();

        assertE("☺      " +
                "       " +
                "       " +
                "       " +
                "       " +
                "    ===" +
                "#######");

        game.tick();

        assertE("       " +
                "☺      " +
                "       " +
                "       " +
                "       " +
                "   ====" +
                "#######");

        game.tick();

        assertE("       " +
                "       " +
                "☺      " +
                "       " +
                "       " +
                "  =====" +
                "#######");

        game.tick();

        assertE("       " +
                "       " +
                "       " +
                "☺      " +
                "       " +
                " ======" +
                "#######");

        game.tick();

        assertE("       " +
                "       " +
                "       " +
                "       " +
                "☺      " +
                "=======" +
                "#######");
    }

    @Test
    public void shouldOnlyDoubleJump() {
        givenFl("       " +
                "       " +
                "       " +
                "☺      " +
                "=      " +
                "       " +
                "#######");
//first jump
        hero.up();
        game.tick();

        assertE("       " +
                "       " +
                "☺      " +
                "       " +
                "       " +
                "       " +
                "#######");
//second jump
        hero.up();
        game.tick();

        assertE("       " +
                "☺      " +
                "       " +
                "       " +
                "       " +
                "       " +
                "#######");
//cannot jump
        hero.up();
        game.tick();


        assertE("       " +
                "       " +
                "☺      " +
                "       " +
                "       " +
                "       " +
                "#######");

        hero.up();
        game.tick();

        assertE("       " +
                "       " +
                "       " +
                "☺      " +
                "       " +
                "       " +
                "#######");

        hero.up();
        game.tick();

        assertE("       " +
                "       " +
                "       " +
                "       " +
                "☺      " +
                "       " +
                "#######");
    }

    @Test
    public void shouldDoubleJumpAfterIdle() {
        givenFl("       " +
                "       " +
                "       " +
                "☺      " +
                "=======" +
                "       " +
                "#######");
//first jump
        dice(2 - 1, 20);
        hero.up();
        game.tick();

        assertE("       " +
                "       " +
                "☺      " +
                "       " +
                "=======" +
                "       " +
                "#######");
//second jump
        hero.up();
        game.tick();

        assertE("       " +
                "☺      " +
                "       " +
                "       " +
                "=======" +
                "       " +
                "#######");
//cannot jump
        hero.up();
        game.tick();


        assertE("       " +
                "       " +
                "☺      " +
                "       " +
                "=======" +
                "       " +
                "#######");

        hero.up();
        game.tick();

        assertE("       " +
                "       " +
                "       " +
                "☺      " +
                "=======" +
                "       " +
                "#######");

        hero.up();
        game.tick();

        assertE("       " +
                "       " +
                "☺      " +
                "       " +
                "=======" +
                "       " +
                "#######");
    }

    @Test
    public void shouldJumpOnlyOnceWhenFalling() {
        givenFl("       " +
                "       " +
                "       " +
                "☺      " +
                "=      " +
                "       " +
                "#######");

        game.tick();

        assertE("       " +
                "       " +
                "       " +
                "☺      " +
                "       " +
                "       " +
                "#######");
//first jump - should be like double
        hero.up();
        game.tick();

        assertE("       " +
                "       " +
                "☺      " +
                "       " +
                "       " +
                "       " +
                "#######");

//cannot jump twice when falling
        hero.up();
        game.tick();

        assertE("       " +
                "       " +
                "       " +
                "☺      " +
                "       " +
                "       " +
                "#######");
    }

    @Test
    public void shouldFallingJustAfterJump() {
        givenFl("       " +
                "       " +
                "       " +
                "☺      " +
                "====== " +
                "       " +
                "#######");

        hero.up();
        game.tick();

        assertE("       " +
                "       " +
                "☺      " +
                "       " +
                "=====  " +
                "       " +
                "#######");

        Assert.assertTrue(hero.getStatus() == HeroStatus.FALLING);
    }

    @Test
    public void shouldMakeTenTicks() {
        givenFl("       " +
                "       " +
                "       " +
                "☺      " +
                "=======" +
                "       " +
                "#######");
        dice(2 - 1, 10);
        for (int i = 0; i < 10; i++) {
            game.tick();
        }

        assertE("       " +
                "       " +
                "       " +
                "☺      " +
                "=======" +
                "       " +
                "#######");

        Assert.assertEquals(10, game.getTickCounter());
    }

    @Test
    public void shouldMakePlatformNoHigherThanY4WhenDice5() {
        givenFl("       " +
                "       " +
                "       " +
                "☺      " +
                "=======" +
                "       " +
                "#######");

        dice(5 - 1, 1);
        game.tick();

        assertE("       " +
                "       " +
                "      =" +
                "☺      " +
                "====== " +
                "       " +
                "#######");
    }

    @Test
    public void shouldNotMakeSecondPlatformHigherThanY4WhenDice5() {
        givenFl("#######" +
                "       " +
                "       " +
                "☺      " +
                "=======" +
                "       " +
                "#######");

        dice(5 - 1, 2);
        game.tick();
        game.tick();
        dice(6 - 1, 1);
        game.tick();

        assertE("#######" +
                "       " +
                "    == " +
                "☺      " +
                "====   " +
                "       " +
                "#######");
    }

    @Test
    public void shouldMake3Platforms() {
        givenFl("#######" +
                "       " +
                "       " +
                "☺      " +
                "=======" +
                "       " +
                "#######");

        dice(4 - 1, 1);
        game.tick();
        game.tick();
        dice(1 - 1, 1);
        game.tick();
        game.tick();
        dice(4 - 1, 1);
        game.tick();

        assertE("#######" +
                "       " +
                "  =    " +
                "☺     =" +
                "==     " +
                "    =  " +
                "#######");
    }

    @Test
    public void shouldFullGameWithPlatformsAndJumps() {
        givenFl("####################" +
                " =                  " +
                " =                  " +
                " =                  " +
                " =                  " +
                " =                  " +
                " =                  " +
                " =                  " +
                " =                  " +
                " =                  " +
                " =                  " +
                " =                  " +
                " =                  " +
                " =                  " +
                " =                  " +
                " =                  " +
                "☺             =     " +
                " =                  " +
                " =  ====   ==       " +
                "####################");

        dice(1 - 1, 10);
        game.tick();
        game.tick();
        game.tick();
        hero.up();
        game.tick();

        assertE("####################" +
                "                    " +
                "                    " +
                "                    " +
                "                    " +
                "                    " +
                "                    " +
                "                    " +
                "                    " +
                "                    " +
                "                    " +
                "                    " +
                "                    " +
                "                    " +
                "                    " +
                "                    " +
                "☺         =         " +
                "                    " +
                "====   ==       ====" +
                "####################");

        game.tick();
        game.tick();
        game.tick();
        hero.up();
        game.tick();
        hero.up();
        game.tick();

        assertE("####################" +
                "                    " +
                "                    " +
                "                    " +
                "                    " +
                "                    " +
                "                    " +
                "                    " +
                "                    " +
                "                    " +
                "                    " +
                "                    " +
                "                    " +
                "                    " +
                "                    " +
                "☺                   " +
                "     =              " +
                "                    " +
                "  ==       =========" +
                "####################");

        game.tick();
        game.tick();
        game.tick();
        hero.up();
        game.tick();
        hero.up();
        game.tick();

        assertE("####################" +
                "                    " +
                "                    " +
                "                    " +
                "                    " +
                "                    " +
                "                    " +
                "                    " +
                "                    " +
                "                    " +
                "                    " +
                "                    " +
                "                    " +
                "                    " +
                "                    " +
                "☺                   " +
                "=                ===" +
                "                    " +
                "      ==========    " +
                "####################");
    }

}
















