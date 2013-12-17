package com.codenjoy.dojo.loderunner.model;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import static junit.framework.Assert.assertEquals;

/**
 * User: sanja
 * Date: 17.12.13
 * Time: 4:47
 */
public class LoderunnerTest {

    private Loderunner game;
    private Hero hero;

    @Before
    public void setup() {

    }

    @Test
    @Ignore
    public void shouldFieldAtStart() {
        givenFl("☼☼☼☼☼" +
                "☼   ☼" +
                "☼ ◄ ☼" +
                "☼###☼" +
                "☼☼☼☼☼");

        assertE("☼☼☼☼☼\n" +
                "☼   ☼\n" +
                "☼ ◄ ☼\n" +
                "☼###☼\n" +
                "☼☼☼☼☼\n");
    }

    @Test
    @Ignore
    public void shouldDrillLeft() {
        givenFl("☼☼☼☼☼" +
                "☼   ☼" +
                "☼ ◄ ☼" +
                "☼###☼" +
                "☼☼☼☼☼");

        hero.act();
        hero.left();
        game.tick();

        assertE("☼☼☼☼☼\n" +
                "☼   ☼\n" +
                "☼.Я ☼\n" +
                "☼*##☼\n" +
                "☼☼☼☼☼\n");

        game.tick();

        assertE("☼☼☼☼☼\n" +
                "☼   ☼\n" +
                "☼ Я ☼\n" +
                "☼ ##☼\n" +
                "☼☼☼☼☼\n");
    }

    @Test
    @Ignore
    public void shouldDrillRight() {
        givenFl("☼☼☼☼☼" +
                "☼   ☼" +
                "☼ ◄ ☼" +
                "☼###☼" +
                "☼☼☼☼☼");

        hero.act();
        hero.right();
        game.tick();

        assertE("☼☼☼☼☼\n" +
                "☼   ☼\n" +
                "☼ R.☼\n" +
                "☼##*☼\n" +
                "☼☼☼☼☼\n");

        game.tick();

        assertE("☼☼☼☼☼\n" +
                "☼   ☼\n" +
                "☼ R ☼\n" +
                "☼## ☼\n" +
                "☼☼☼☼☼\n");
    }

    @Test
    public void shouldMoveLeft() {
        givenFl("☼☼☼☼☼" +
                "☼   ☼" +
                "☼ ◄ ☼" +
                "☼###☼" +
                "☼☼☼☼☼");

        hero.left();
        game.tick();

        assertE("☼☼☼☼☼\n" +
                "☼   ☼\n" +
                "☼◄  ☼\n" +
                "☼###☼\n" +
                "☼☼☼☼☼\n");
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

        assertE("☼☼☼☼☼\n" +
                "☼   ☼\n" +
                "☼  ►☼\n" +
                "☼###☼\n" +
                "☼☼☼☼☼\n");
    }

    @Test
    public void shouldStopWhenNoMoreRightCommand() {
        shouldMoveRight();

        game.tick();

        assertE("☼☼☼☼☼\n" +
                "☼   ☼\n" +
                "☼  ►☼\n" +
                "☼###☼\n" +
                "☼☼☼☼☼\n");
    }

    private void givenFl(String board) {
        game = new Loderunner(new LevelImpl(board));
        hero = game.getHero();
    }

    private void assertE(String expected) {
        Printer printer = new Printer(game);
        assertEquals(expected, printer.toString());
    }

}
