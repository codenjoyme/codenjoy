package com.epam.dojo.icancode.model;

import com.codenjoy.dojo.services.Dice;
import com.codenjoy.dojo.services.EventListener;
import com.codenjoy.dojo.utils.TestUtils;
import com.epam.dojo.icancode.model.items.Hero;
import com.epam.dojo.icancode.services.Events;
import com.epam.dojo.icancode.services.Printer;
import org.junit.Before;
import org.junit.Test;
import org.mockito.stubbing.OngoingStubbing;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import static junit.framework.Assert.*;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Mockito.*;

/**
 * User: sanja
 * Date: 17.12.13
 * Time: 4:47
 */
public class ICanCodeTest {

    public static final int FIRE_TICKS = 6;
    private ICanCode game;
    private Printer printer;

    private Hero hero;
    private Dice dice;
    private EventListener listener;
    private Player player;

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

    private void givenFl(String... boards) {
        List<ILevel> levels = createLevels(boards);

        game = new ICanCode(levels, dice);
        listener = mock(EventListener.class);
        player = new Player(listener);
        game.newGame(player);
        this.hero = game.getHeroes().get(0);

        printer = new Printer(game, 16);
    }

    private List<ILevel> createLevels(String[] boards) {
        List<ILevel> levels = new LinkedList<ILevel>();
        for (String board : boards) {
            ILevel level = new LevelImpl(board);
            levels.add(level);
        }
        return levels;
    }

    private void assertL(String expected) {
        assertEquals(TestUtils.injectN(expected),
                printer.getBoardAsString(1, hero.getPosition())[0]);
    }

    private void assertE(String expected) {
        assertEquals(TestUtils.injectN(expected),
                printer.getBoardAsString(2, hero.getPosition())[1]);
    }

    @Test
    public void shouldFieldAtStart() {
        // given
        givenFl("╔═════════┐" +
                "║.........│" +
                "║.S.┌─╗...│" +
                "║...│ ║...│" +
                "║.┌─┘ └─╗.│" +
                "║.│     ║.│" +
                "║.╚═┐ ╔═╝.│" +
                "║...│ ║...│" +
                "║...╚═╝...│" +
                "║........E│" +
                "└─────────┘");

        // then
        assertL(
                "╔═════════┐" +
                        "║.........│" +
                        "║.S.┌─╗...│" +
                        "║...│ ║...│" +
                        "║.┌─┘ └─╗.│" +
                        "║.│     ║.│" +
                        "║.╚═┐ ╔═╝.│" +
                        "║...│ ║...│" +
                        "║...╚═╝...│" +
                        "║........E│" +
                        "└─────────┘");

        assertE("-----------" +
                "-----------" +
                "--☺--------" +
                "-----------" +
                "-----------" +
                "-----------" +
                "-----------" +
                "-----------" +
                "-----------" +
                "-----------" +
                "-----------");
    }

    @Test
    public void shouldWalk() {
        // given
        givenFl("╔═══┐" +
                "║...│" +
                "║.S.│" +
                "║...│" +
                "└───┘");

        // when
        hero.right();
        game.tick();

        // then
        assertE("-----" +
                "-----" +
                "---☺-" +
                "-----" +
                "-----");

        // when
        hero.left();
        game.tick();

        // then
        assertE("-----" +
                "-----" +
                "--☺--" +
                "-----" +
                "-----");

        // when
        hero.down();
        game.tick();

        // then
        assertE("-----" +
                "-----" +
                "-----" +
                "--☺--" +
                "-----");

        // when
        hero.up();
        game.tick();

        // then
        assertE("-----" +
                "-----" +
                "--☺--" +
                "-----" +
                "-----");

        assertL(
                "╔═══┐" +
                        "║...│" +
                        "║.S.│" +
                        "║...│" +
                        "└───┘");
    }

    @Test
    public void shouldStopWhenNoCommands() {
        // given
        givenFl("╔═══┐" +
                "║...│" +
                "║.S.│" +
                "║...│" +
                "└───┘");

        // when
        game.tick();

        // then
        assertE("-----" +
                "-----" +
                "--☺--" +
                "-----" +
                "-----");

        // when
        game.tick();

        // then
        assertE("-----" +
                "-----" +
                "--☺--" +
                "-----" +
                "-----");

        assertL("╔═══┐" +
                "║...│" +
                "║.S.│" +
                "║...│" +
                "└───┘");
    }

    @Test
    public void shouldStopWhenWallForDirections() {
        // given
        givenFl("╔═┐" +
                "║S│" +
                "└─┘");

        // when
        hero.left();
        game.tick();

        // then
        assertE("---" +
                "-☺-" +
                "---");

        // when
        hero.right();
        game.tick();

        // then
        assertE("---" +
                "-☺-" +
                "---");

        // when
        hero.up();
        game.tick();

        // then
        assertE("---" +
                "-☺-" +
                "---");

        // when
        hero.down();
        game.tick();

        // then
        assertE("---" +
                "-☺-" +
                "---");

        assertL("╔═┐" +
                "║S│" +
                "└─┘");
    }

    @Test
    public void shouldStopWhenWalls() {
        // given
        givenFl(".╔." +
                "║S═" +
                ".┐.");

        // when
        hero.left();
        game.tick();

        // then
        assertE("---" +
                "-☺-" +
                "---");

        // when
        hero.right();
        game.tick();

        // then
        assertE("---" +
                "-☺-" +
                "---");

        // when
        hero.up();
        game.tick();

        // then
        assertE("---" +
                "-☺-" +
                "---");

        // when
        hero.down();
        game.tick();

        // then
        assertE("---" +
                "-☺-" +
                "---");
    }

    @Test
    public void shouldStopWhenWalls2() {
        // given
        givenFl(".│." +
                "┘S└" +
                ".─.");

        // when
        hero.left();
        game.tick();

        // then
        assertE("---" +
                "-☺-" +
                "---");

        // when
        hero.right();
        game.tick();

        // then
        assertE("---" +
                "-☺-" +
                "---");

        // when
        hero.up();
        game.tick();

        // then
        assertE("---" +
                "-☺-" +
                "---");

        // when
        hero.down();
        game.tick();

        // then
        assertE("---" +
                "-☺-" +
                "---");
    }

    @Test
    public void shouldStopWhenWalls3() {
        // given
        givenFl(".┌." +
                "╝S╗" +
                ".╚.");

        // when
        hero.left();
        game.tick();

        // then
        assertE("---" +
                "-☺-" +
                "---");

        // when
        hero.right();
        game.tick();

        // then
        assertE("---" +
                "-☺-" +
                "---");

        // when
        hero.up();
        game.tick();

        // then
        assertE("---" +
                "-☺-" +
                "---");

        // when
        hero.down();
        game.tick();

        // then
        assertE("---" +
                "-☺-" +
                "---");
    }

    @Test
    public void shouldWin() {
        // given
        givenFl("     " +
                "╔═══┐" +
                "║.SE│" +
                "└───┘" +
                "     ");

        // when
        hero.right();
        game.tick();

        // then
        verify(listener).event(Events.WIN(0));

        assertE("-----" +
                "-----" +
                "---☺-" +
                "-----" +
                "-----");

        assertL("     " +
                "╔═══┐" +
                "║.SE│" +
                "└───┘" +
                "     ");
    }

    @Test
    public void shouldNewGameAfterWin() {
        // given
        givenFl("     " +
                "╔═══┐" +
                "║.SE│" +
                "└───┘" +
                "     ");

        // when
        hero.right();
        game.tick();

        // then
        assertTrue(hero.isWin());

        // when
        game.tick();

        // then
        assertE("-----" +
                "-----" +
                "--☺--" +
                "-----" +
                "-----");

        assertL("     " +
                "╔═══┐" +
                "║.SE│" +
                "└───┘" +
                "     ");

        assertFalse(hero.isWin());
    }

    @Test
    public void shouldStartInNewPlaceWhenAct() {
        // given
        givenFl("╔═══┐" +
                "║...│" +
                "║S..│" +
                "║...│" +
                "└───┘");

        hero.right();
        game.tick();

        hero.right();
        game.tick();

        assertE("-----" +
                "-----" +
                "---☺-" +
                "-----" +
                "-----");

        // when
        hero.act(0);

        assertE("-----" +
                "-----" +
                "---☺-" +
                "-----" +
                "-----");

        game.tick();

        // then
        assertE("-----" +
                "-----" +
                "-☺---" +
                "-----" +
                "-----");
    }

    @Test
    public void demo1_generalWay() {
        // given
        givenFl("     " +
                "╔═══┐" +
                "║S.E│" +
                "└───┘" +
                "     ");

        // when
        hero.right();
        game.tick();

        hero.right();
        game.tick();

        // then
        verify(listener).event(Events.WIN(0));

        assertL("     " +
                "╔═══┐" +
                "║S.E│" +
                "└───┘" +
                "     ");

        assertE("-----" +
                "-----" +
                "---☺-" +
                "-----" +
                "-----");
    }

    @Test
    public void shouldNextLevelWhenFinishCurrent() {
        // given
        givenFl("╔══┐" +
                        "║SE│" +
                        "║..│" +
                        "└──┘",
                "╔══┐" +
                        "║S.│" +
                        "║E.│" +
                        "└──┘");

        // when
        hero.right();
        game.tick();

        verify(listener).event(Events.WIN(0));

        assertL("╔══┐" +
                "║SE│" +
                "║..│" +
                "└──┘");

        assertE("----" +
                "--☺-" +
                "----" +
                "----");

        // when
        game.tick();

        // then
        assertL("╔══┐" +
                "║S.│" +
                "║E.│" +
                "└──┘");

        assertE("----" +
                "-☺--" +
                "----" +
                "----");
    }

    @Test
    public void shouldAfterNextLevelHeroCanMove() {
        // given
        shouldNextLevelWhenFinishCurrent();

        assertL("╔══┐" +
                "║S.│" +
                "║E.│" +
                "└──┘");

        assertE("----" +
                "-☺--" +
                "----" +
                "----");

        // when
        hero.right();
        game.tick();

        // then
        assertL("╔══┐" +
                "║S.│" +
                "║E.│" +
                "└──┘");

        assertE("----" +
                "--☺-" +
                "----" +
                "----");
    }

    @Test
    public void shouldLooseWhenFallInHole() {
        // given
        givenFl("╔══┐" +
                "║SO│" +
                "║.E│" +
                "└──┘");

        // when
        hero.right();
        game.tick();

        // then
        verify(listener).event(Events.LOOSE());

        assertL("╔══┐" +
                "║SO│" +
                "║.E│" +
                "└──┘");

        assertE("----" +
                "--o-" +
                "----" +
                "----");
    }

    @Test
    public void shouldNewGameAfterFallInHole() {
        // given
        shouldLooseWhenFallInHole();

        assertL("╔══┐" +
                "║SO│" +
                "║.E│" +
                "└──┘");

        assertE("----" +
                "--o-" +
                "----" +
                "----");

        // when
        game.tick();

        // then
        assertL("╔══┐" +
                "║SO│" +
                "║.E│" +
                "└──┘");

        assertE("----" +
                "-☺--" +
                "----" +
                "----");
    }

    @Test
    public void shouldCanGoAfterFallInHole() {
        // given
        shouldNewGameAfterFallInHole();

        assertL("╔══┐" +
                "║SO│" +
                "║.E│" +
                "└──┘");

        assertE("----" +
                "-☺--" +
                "----" +
                "----");

        // when
        hero.down();
        game.tick();

        // then
        assertL("╔══┐" +
                "║SO│" +
                "║.E│" +
                "└──┘");

        assertE("----" +
                "----" +
                "-☺--" +
                "----");

    }

    @Test
    public void shouldDoubleScoreWhenGetGold() {
        // given
        givenFl("     " +
                "╔═══┐" +
                "║S$E│" +
                "└───┘" +
                "     ");

        // when
        hero.right();
        game.tick();

        hero.right();
        game.tick();

        // then
        verify(listener).event(Events.WIN(1));

        assertL("     " +
                "╔═══┐" +
                "║S.E│" +
                "└───┘" +
                "     ");

        assertE("-----" +
                "-----" +
                "---☺-" +
                "-----" +
                "-----");
    }

    @Test
    public void shouldDoubleScoreWhenGetTwoGold() {
        // given
        givenFl("      " +
                "╔════┐" +
                "║S$$E│" +
                "└────┘" +
                "      " +
                "      ");

        // when
        hero.right();
        game.tick();

        hero.right();
        game.tick();

        hero.right();
        game.tick();

        // then
        verify(listener).event(Events.WIN(2));

        assertL("      " +
                "╔════┐" +
                "║S..E│" +
                "└────┘" +
                "      " +
                "      ");

        assertE("------" +
                "------" +
                "----☺-" +
                "------" +
                "------" +
                "------");
    }

    @Test
    public void shouldNoScoreWhenGetGold() {
        // given
        givenFl("     " +
                "╔═══┐" +
                "║S$E│" +
                "└───┘" +
                "     ");

        // when
        hero.right();
        game.tick();

        // then
        verifyNoMoreInteractions(listener);
    }

    @Test
    public void shouldHideGoldWhenGet() {
        // given
        givenFl("      " +
                "╔════┐" +
                "║S$$E│" +
                "└────┘" +
                "      " +
                "      ");

        // when
        hero.right();
        game.tick();

        // then
        assertL("      " +
                "╔════┐" +
                "║S.$E│" +
                "└────┘" +
                "      " +
                "      ");

        assertE("------" +
                "------" +
                "--☺---" +
                "------" +
                "------" +
                "------");

        // when
        hero.right();
        game.tick();

        // then
        assertL("      " +
                "╔════┐" +
                "║S..E│" +
                "└────┘" +
                "      " +
                "      ");

        assertE("------" +
                "------" +
                "---☺--" +
                "------" +
                "------" +
                "------");
    }

    @Test
    public void shouldReNewGoldWhenReset() {
        // given
        givenFl("      " +
                "╔════┐" +
                "║S$$E│" +
                "└────┘" +
                "      " +
                "      ");

        hero.right();
        game.tick();

        hero.right();
        game.tick();

        assertL("      " +
                "╔════┐" +
                "║S..E│" +
                "└────┘" +
                "      " +
                "      ");

        assertE("------" +
                "------" +
                "---☺--" +
                "------" +
                "------" +
                "------");

        // when
        hero.reset();
        game.tick();

        // then
        assertL("      " +
                "╔════┐" +
                "║S$$E│" +
                "└────┘" +
                "      " +
                "      ");

        assertE("------" +
                "------" +
                "-☺----" +
                "------" +
                "------" +
                "------");
    }

    @Test
    public void shouldJump() {
        // given
        givenFl("     " +
                "╔═══┐" +
                "║S..│" +
                "└───┘" +
                "     ");

        // when
        hero.jump();

        // then
        assertL("     " +
                "╔═══┐" +
                "║S..│" +
                "└───┘" +
                "     ");

        assertE("-----" +
                "-----" +
                "-☺---" +
                "-----" +
                "-----");

        // when
        game.tick();

        // then
        assertL("     " +
                "╔═══┐" +
                "║S..│" +
                "└───┘" +
                "     ");

        assertE("-----" +
                "-----" +
                "-*---" +
                "-----" +
                "-----");

        // when
        game.tick();

        // then
        assertL("     " +
                "╔═══┐" +
                "║S..│" +
                "└───┘" +
                "     ");

        assertE("-----" +
                "-----" +
                "-☺---" +
                "-----" +
                "-----");
    }

    @Test
    public void shouldJumpRight() {
        // given
        givenFl("     " +
                "╔═══┐" +
                "║S..│" +
                "└───┘" +
                "     ");

        // when
        hero.jump();
        hero.right();

        // then
        assertL("     " +
                "╔═══┐" +
                "║S..│" +
                "└───┘" +
                "     ");

        assertE("-----" +
                "-----" +
                "-☺---" +
                "-----" +
                "-----");

        // when
        game.tick();

        // then
        assertL("     " +
                "╔═══┐" +
                "║S..│" +
                "└───┘" +
                "     ");

        assertE("-----" +
                "-----" +
                "--*--" +
                "-----" +
                "-----");

        // when
        game.tick();

        // then
        assertL("     " +
                "╔═══┐" +
                "║S..│" +
                "└───┘" +
                "     ");

        assertE("-----" +
                "-----" +
                "---☺-" +
                "-----" +
                "-----");
    }

    @Test
    public void shouldCantChangeDirectionWhenJumpForward() {
        // given
        givenFl("     " +
                "╔═══┐" +
                "║S..│" +
                "└───┘" +
                "     ");

        // when
        hero.jump();
        hero.right();

        // then
        assertL("     " +
                "╔═══┐" +
                "║S..│" +
                "└───┘" +
                "     ");

        assertE("-----" +
                "-----" +
                "-☺---" +
                "-----" +
                "-----");

        // when
        game.tick();

        // then
        assertL("     " +
                "╔═══┐" +
                "║S..│" +
                "└───┘" +
                "     ");

        assertE("-----" +
                "-----" +
                "--*--" +
                "-----" +
                "-----");

        // when
        hero.left(); // will be ignored
        hero.right();
        hero.up();
        hero.down();

        game.tick();

        // then
        assertL("     " +
                "╔═══┐" +
                "║S..│" +
                "└───┘" +
                "     ");

        assertE("-----" +
                "-----" +
                "---☺-" +
                "-----" +
                "-----");
    }

    @Test
    public void shouldLeaveGoldWhenJump() {
        // given
        givenFl("     " +
                "╔═══┐" +
                "║S$.│" +
                "└───┘" +
                "     ");

        // when
        hero.jump();
        hero.right();

        game.tick();

        // then
        assertL("     " +
                "╔═══┐" +
                "║S$.│" +
                "└───┘" +
                "     ");

        assertE("-----" +
                "-----" +
                "--*--" +
                "-----" +
                "-----");

        // when
        game.tick();

        // then
        assertL("     " +
                "╔═══┐" +
                "║S$.│" +
                "└───┘" +
                "     ");

        assertE("-----" +
                "-----" +
                "---☺-" +
                "-----" +
                "-----");
    }

    @Test
    public void shouldJumpOverHole() {
        // given
        givenFl("     " +
                "╔═══┐" +
                "║SO.│" +
                "└───┘" +
                "     ");

        // when
        hero.jump();
        hero.right();

        game.tick();

        // then
        assertL("     " +
                "╔═══┐" +
                "║SO.│" +
                "└───┘" +
                "     ");

        assertE("-----" +
                "-----" +
                "--*--" +
                "-----" +
                "-----");

        // when
        game.tick();

        // then
        assertL("     " +
                "╔═══┐" +
                "║SO.│" +
                "└───┘" +
                "     ");

        assertE("-----" +
                "-----" +
                "---☺-" +
                "-----" +
                "-----");
    }

    @Test
    public void shouldJumpOverExit() {
        // given
        givenFl("     " +
                "╔═══┐" +
                "║.SE│" +
                "└───┘" +
                "     ");

        // when
        hero.jump();
        hero.right();

        game.tick();

        // then
        assertL("     " +
                "╔═══┐" +
                "║.SE│" +
                "└───┘" +
                "     ");

        assertE("-----" +
                "-----" +
                "---*-" +
                "-----" +
                "-----");

        verify(listener).event(Events.WIN(0));

        // when
        game.tick();

        // then
        assertL("     " +
                "╔═══┐" +
                "║.SE│" +
                "└───┘" +
                "     ");

        assertE("-----" +
                "-----" +
                "--☺--" +
                "-----" +
                "-----");

        verifyNoMoreInteractions(listener);
    }

    @Test
    public void shouldLaserFireOn6Ticks() {
        // given
        givenFl("╔═════┐" +
                "║˃...˅│" +
                "║.S...│" +
                "║.....│" +
                "║.....│" +
                "║˄...˂│" +
                "└─────┘");

        // when
        game.tick();

        // then
        assertL("╔═════┐" +
                "║˃...˅│" +
                "║.S...│" +
                "║.....│" +
                "║.....│" +
                "║˄...˂│" +
                "└─────┘");

        assertE("-------" +
                "-------" +
                "--☺----" +
                "-------" +
                "-------" +
                "-------" +
                "-------");

        // when
        ticks(FIRE_TICKS - 1);

        // then
        assertL("╔═════┐" +
                "║►...▼│" +
                "║.S...│" +
                "║.....│" +
                "║.....│" +
                "║▲...◄│" +
                "└─────┘");

        assertE("-------" +
                "-------" +
                "--☺----" +
                "-------" +
                "-------" +
                "-------" +
                "-------");

        // when
        game.tick();

        // then
        assertL("╔═════┐" +
                "║˃...˅│" +
                "║.S...│" +
                "║.....│" +
                "║.....│" +
                "║˄...˂│" +
                "└─────┘");

        assertE("-------" +
                "--→----" +
                "--☺--↓-" +
                "-------" +
                "-↑-----" +
                "----←--" +
                "-------");

        // when
        game.tick();

        // then
        assertL("╔═════┐" +
                "║˃...˅│" +
                "║.S...│" +
                "║.....│" +
                "║.....│" +
                "║˄...˂│" +
                "└─────┘");

        assertE("-------" +
                "---→---" +
                "--☺----" +
                "-↑---↓-" +
                "-------" +
                "---←---" +
                "-------");

        // when
        game.tick();

        // then
        assertL("╔═════┐" +
                "║˃...˅│" +
                "║.S...│" +
                "║.....│" +
                "║.....│" +
                "║˄...˂│" +
                "└─────┘");

        assertE("-------" +
                "----→--" +
                "-↑☺----" +
                "-------" +
                "-----↓-" +
                "--←----" +
                "-------");

        // when
        game.tick();

        // then
        assertL("╔═════┐" +
                "║˃...˅│" +
                "║.S...│" +
                "║.....│" +
                "║.....│" +
                "║˄...˂│" +
                "└─────┘");

        assertE("-------" +
                "-------" +
                "--☺----" +
                "-------" +
                "-------" +
                "-------" +
                "-------");
    }

    @Test
    public void shouldLaserDoNotTouchGoldPitStartEnd() {
        // given
        givenFl("╔═════┐" +
                "║˃.O.˅│" +
                "║.....│" +
                "║S...E│" +
                "║.....│" +
                "║˄.$.˂│" +
                "└─────┘");

        // when
        hero.right();
        ticks(FIRE_TICKS);
        game.tick();

        // then
        assertL("╔═════┐" +
                "║˃.O.˅│" +
                "║.....│" +
                "║S...E│" +
                "║.....│" +
                "║˄.$.˂│" +
                "└─────┘");

        assertE("-------" +
                "--→----" +
                "-----↓-" +
                "--☺----" +
                "-↑-----" +
                "----←--" +
                "-------");

        // when
        game.tick();

        // then
        assertL("╔═════┐" +
                "║˃.O.˅│" +
                "║.....│" +
                "║S...E│" +
                "║.....│" +
                "║˄.$.˂│" +
                "└─────┘");

        assertE("-------" +
                "---→---" +
                "-------" +
                "-↑☺--↓-" +
                "-------" +
                "---←---" +
                "-------");

        // when
        game.tick();

        // then
        assertL("╔═════┐" +
                "║˃.O.˅│" +
                "║.....│" +
                "║S...E│" +
                "║.....│" +
                "║˄.$.˂│" +
                "└─────┘");

        assertE("-------" +
                "----→--" +
                "-↑-----" +
                "--☺----" +
                "-----↓-" +
                "--←----" +
                "-------");
    }

    @Test
    public void shouldLaserKillRobo() {
        // given
        givenFl("╔═════┐" +
                "║˃....│" +
                "║..S..│" +
                "└─────┘" +
                "       " +
                "       " +
                "       ");

        hero.up();
        ticks(FIRE_TICKS);
        game.tick();

        assertL("╔═════┐" +
                "║˃....│" +
                "║..S..│" +
                "└─────┘" +
                "       " +
                "       " +
                "       ");

        assertE("-------" +
                "--→☺---" +
                "-------" +
                "-------" +
                "-------" +
                "-------" +
                "-------");

        // when
        game.tick();

        // then
        verify(listener).event(Events.LOOSE());

        assertL("╔═════┐" +
                "║˃....│" +
                "║..S..│" +
                "└─────┘" +
                "       " +
                "       " +
                "       ");

        assertE("-------" +
                "---☻---" +
                "-------" +
                "-------" +
                "-------" +
                "-------" +
                "-------");

        // when
        game.tick();

        // then
        assertL("╔═════┐" +
                "║˃....│" +
                "║..S..│" +
                "└─────┘" +
                "       " +
                "       " +
                "       ");

        assertE("-------" +
                "-------" +
                "---☺---" +
                "-------" +
                "-------" +
                "-------" +
                "-------");

        // when
        game.tick();

        // then
        verifyNoMoreInteractions(listener);
    }

    @Test
    public void shouldAliveWhenJumpOverLaser() {
        // given
        givenFl("╔═════┐" +
                "║˃....│" +
                "║..S..│" +
                "└─────┘" +
                "       " +
                "       " +
                "       ");

        hero.up();
        ticks(FIRE_TICKS);
        game.tick();

        assertL("╔═════┐" +
                "║˃....│" +
                "║..S..│" +
                "└─────┘" +
                "       " +
                "       " +
                "       ");

        assertE("-------" +
                "--→☺---" +
                "-------" +
                "-------" +
                "-------" +
                "-------" +
                "-------");

        // when
        hero.jump();
        game.tick();

        // then
        verifyNoMoreInteractions(listener);

        assertL("╔═════┐" +
                "║˃....│" +
                "║..S..│" +
                "└─────┘" +
                "       " +
                "       " +
                "       ");

        assertE("-------" +
                "---*---" +
                "-------" +
                "-------" +
                "-------" +
                "-------" +
                "-------");

        // when
        game.tick();

        // then
        assertL("╔═════┐" +
                "║˃....│" +
                "║..S..│" +
                "└─────┘" +
                "       " +
                "       " +
                "       ");

        assertE("-------" +
                "---☺→--" +
                "-------" +
                "-------" +
                "-------" +
                "-------" +
                "-------");

        // when
        game.tick();

        // then
        verifyNoMoreInteractions(listener);

        // then
        assertL("╔═════┐" +
                "║˃....│" +
                "║..S..│" +
                "└─────┘" +
                "       " +
                "       " +
                "       ");

        assertE("-------" +
                "---☺-→-" +
                "-------" +
                "-------" +
                "-------" +
                "-------" +
                "-------");
    }

    @Test
    public void shouldDieWhenFallOnLaser() {
        // given
        givenFl("╔═════┐" +
                "║˃....│" +
                "║..S..│" +
                "└─────┘" +
                "       " +
                "       " +
                "       ");

        hero.up();
        ticks(FIRE_TICKS);
        hero.jump();
        game.tick();

        assertL("╔═════┐" +
                "║˃....│" +
                "║..S..│" +
                "└─────┘" +
                "       " +
                "       " +
                "       ");

        assertE("-------" +
                "--→*---" +
                "-------" +
                "-------" +
                "-------" +
                "-------" +
                "-------");

        // when
        game.tick();

        // then
        verify(listener).event(Events.LOOSE());

        assertL("╔═════┐" +
                "║˃....│" +
                "║..S..│" +
                "└─────┘" +
                "       " +
                "       " +
                "       ");

        assertE("-------" +
                "---☻---" +
                "-------" +
                "-------" +
                "-------" +
                "-------" +
                "-------");

        // when
        game.tick();

        // then
        verifyNoMoreInteractions(listener);

        assertL("╔═════┐" +
                "║˃....│" +
                "║..S..│" +
                "└─────┘" +
                "       " +
                "       " +
                "       ");

        assertE("-------" +
                "-------" +
                "---☺---" +
                "-------" +
                "-------" +
                "-------" +
                "-------");

        // when
        game.tick();

        // then
        verifyNoMoreInteractions(listener);
    }

    @Test
    public void shouldDieWhenJumpWhenLaserComeInCell() {
        // given
        givenFl("╔═════┐" +
                "║˃....│" +
                "║..S..│" +
                "└─────┘" +
                "       " +
                "       " +
                "       ");

        hero.up();
        ticks(FIRE_TICKS);
        game.tick();

        assertL("╔═════┐" +
                "║˃....│" +
                "║..S..│" +
                "└─────┘" +
                "       " +
                "       " +
                "       ");

        assertE("-------" +
                "--→☺---" +
                "-------" +
                "-------" +
                "-------" +
                "-------" +
                "-------");

        // when
        game.tick();
        hero.jump();

        // then
        verify(listener).event(Events.LOOSE());

        assertL("╔═════┐" +
                "║˃....│" +
                "║..S..│" +
                "└─────┘" +
                "       " +
                "       " +
                "       ");

        assertE("-------" +
                "---☻---" +
                "-------" +
                "-------" +
                "-------" +
                "-------" +
                "-------");

        // when
        game.tick();

        // then
        verifyNoMoreInteractions(listener);

        assertL("╔═════┐" +
                "║˃....│" +
                "║..S..│" +
                "└─────┘" +
                "       " +
                "       " +
                "       ");

        assertE("-------" +
                "-------" +
                "---☺---" +
                "-------" +
                "-------" +
                "-------" +
                "-------");

        // when
        game.tick();

        // then
        verifyNoMoreInteractions(listener);
    }

    @Test
    public void shouldJumpAtHoleWhenNearWall() {
        // given
        givenFl("╔══┐" +
                "║SO│" +
                "║..│" +
                "└──┘");

        // when
        hero.right();
        hero.jump();
        game.tick();

        // then
        assertL("╔══┐" +
                "║SO│" +
                "║..│" +
                "└──┘");

        assertE("----" +
                "--*-" +
                "----" +
                "----");

        // when
        game.tick();

        // then
        assertL("╔══┐" +
                "║SO│" +
                "║..│" +
                "└──┘");

        assertE("----" +
                "--o-" +
                "----" +
                "----");

    }

    @Test
    public void shouldJumpAtGoldWhenNearWall() {
        // given
        givenFl("╔══┐" +
                "║S$│" +
                "║..│" +
                "└──┘");

        // when
        hero.right();
        hero.jump();
        game.tick();

        // then
        assertL("╔══┐" +
                "║S$│" +
                "║..│" +
                "└──┘");

        assertE("----" +
                "--*-" +
                "----" +
                "----");

        // when
        game.tick();

        // then
        assertL("╔══┐" +
                "║S.│" +
                "║..│" +
                "└──┘");

        assertE("----" +
                "--☺-" +
                "----" +
                "----");
    }

    @Test
    public void shouldJumpAtLaserWhenNearWall() {
        // given
        givenFl("╔══┐" +
                "║S.│" +
                "║.˄│" +
                "└──┘");

        ticks(FIRE_TICKS - 1);

        // when
        hero.right();
        hero.jump();
        game.tick();

        // then
        assertL("╔══┐" +
                "║S.│" +
                "║.▲│" +
                "└──┘");

        assertE("----" +
                "--*-" +
                "----" +
                "----");

        // when
        game.tick();

        // then
        verify(listener).event(Events.LOOSE());

        assertL("╔══┐" +
                "║S.│" +
                "║.˄│" +
                "└──┘");

        assertE("----" +
                "--☻-" +
                "----" +
                "----");

    }

    private void ticks(int count) {
        for (int i = 0; i < count; i++) {
            game.tick();
        }
    }

    @Test
    public void shouldAliveWhenJumpLeftOverLaser_case1() {
        // given
        givenFl("╔═════┐" +
                "║˃...S│" +
                "║.....│" +
                "└─────┘" +
                "       " +
                "       " +
                "       ");

        ticks(FIRE_TICKS);
        game.tick();

        assertL("╔═════┐" +
                "║˃...S│" +
                "║.....│" +
                "└─────┘" +
                "       " +
                "       " +
                "       ");

        assertE("-------" +
                "--→--☺-" +
                "-------" +
                "-------" +
                "-------" +
                "-------" +
                "-------");

        // when
        hero.jump();
        hero.left();
        game.tick();

        // then
        verifyNoMoreInteractions(listener);

        assertL("╔═════┐" +
                "║˃...S│" +
                "║.....│" +
                "└─────┘" +
                "       " +
                "       " +
                "       ");

        assertE("-------" +
                "---→*--" +
                "-------" +
                "-------" +
                "-------" +
                "-------" +
                "-------");

        // when
        game.tick();

        // then
        verify(listener).event(Events.LOOSE());

        assertL("╔═════┐" +
                "║˃...S│" +
                "║.....│" +
                "└─────┘" +
                "       " +
                "       " +
                "       ");

        assertE("-------" +
                "---☻---" +
                "-------" +
                "-------" +
                "-------" +
                "-------" +
                "-------");
    }

    @Test
    public void shouldAliveWhenJumpLeftOverLaser_case2() {
        // given
        givenFl("╔═════┐" +
                "║˃..S.│" +
                "║.....│" +
                "└─────┘" +
                "       " +
                "       " +
                "       ");

        ticks(FIRE_TICKS);
        game.tick();

        assertL("╔═════┐" +
                "║˃..S.│" +
                "║.....│" +
                "└─────┘" +
                "       " +
                "       " +
                "       ");

        assertE("-------" +
                "--→-☺--" +
                "-------" +
                "-------" +
                "-------" +
                "-------" +
                "-------");

        // when
        hero.jump();
        hero.left();
        game.tick();

        // then
        verifyNoMoreInteractions(listener);

        assertL("╔═════┐" +
                "║˃..S.│" +
                "║.....│" +
                "└─────┘" +
                "       " +
                "       " +
                "       ");

        assertE("-------" +
                "---*---" +
                "-------" +
                "-------" +
                "-------" +
                "-------" +
                "-------");

        // when
        game.tick();

        // then
        verifyNoMoreInteractions(listener);

        assertL("╔═════┐" +
                "║˃..S.│" +
                "║.....│" +
                "└─────┘" +
                "       " +
                "       " +
                "       ");

        assertE("-------" +
                "--☺-→--" +
                "-------" +
                "-------" +
                "-------" +
                "-------" +
                "-------");
    }

    @Test
    public void shouldAliveWhenJumpLeftOverLaser_case3() {
        // given
        givenFl("╔═════┐" +
                "║.....│" +
                "║˃....│" +
                "║..S..│" +
                "└─────┘" +
                "       " +
                "       ");

        ticks(FIRE_TICKS);
        game.tick();

        assertL("╔═════┐" +
                "║.....│" +
                "║˃....│" +
                "║..S..│" +
                "└─────┘" +
                "       " +
                "       ");

        assertE("-------" +
                "-------" +
                "--→----" +
                "---☺---" +
                "-------" +
                "-------" +
                "-------");

        // when
        hero.jump();
        hero.up();
        game.tick();

        // then
        verifyNoMoreInteractions(listener);

        assertL("╔═════┐" +
                "║.....│" +
                "║˃....│" +
                "║..S..│" +
                "└─────┘" +
                "       " +
                "       ");

        assertE("-------" +
                "-------" +
                "---*---" +
                "-------" +
                "-------" +
                "-------" +
                "-------");

        // when
        game.tick();

        // then
        verifyNoMoreInteractions(listener);

        assertL("╔═════┐" +
                "║.....│" +
                "║˃....│" +
                "║..S..│" +
                "└─────┘" +
                "       " +
                "       ");

        assertE("-------" +
                "---☺---" +
                "----→--" +
                "-------" +
                "-------" +
                "-------" +
                "-------");
    }

    @Test
    public void shouldIgnoreJumpWhenFlying() {
        // given
        givenFl("     " +
                "╔═══┐" +
                "║S..│" +
                "└───┘" +
                "     ");

        hero.jump();
        game.tick();

        assertL("     " +
                "╔═══┐" +
                "║S..│" +
                "└───┘" +
                "     ");

        assertE("-----" +
                "-----" +
                "-*---" +
                "-----" +
                "-----");

        // when
        hero.jump();
        game.tick();

        // then
        assertL("     " +
                "╔═══┐" +
                "║S..│" +
                "└───┘" +
                "     ");

        assertE("-----" +
                "-----" +
                "-☺---" +
                "-----" +
                "-----");

        // when
        game.tick();

        // then
        assertL("     " +
                "╔═══┐" +
                "║S..│" +
                "└───┘" +
                "     ");

        assertE("-----" +
                "-----" +
                "-☺---" +
                "-----" +
                "-----");

        // when
        hero.jump();
        game.tick();

        assertL("     " +
                "╔═══┐" +
                "║S..│" +
                "└───┘" +
                "     ");

        assertE("-----" +
                "-----" +
                "-*---" +
                "-----" +
                "-----");
    }

    @Test
    public void shouldScrollingView() {
        //given
        givenFl("╔══════════════════┐" +
                "║S.................│" +
                "║..................│" +
                "║....┌──╗..........│" +
                "║....│  ║..........│" +
                "║..┌─┘  └─╗........│" +
                "║..│      ║........│" +
                "║..│      ║........│" +
                "║..╚═┐  ╔═╝........│" +
                "║....│  ║..........│" +
                "║....╚══╝..........│" +
                "║..................│" +
                "║..................│" +
                "║..................│" +
                "║..................│" +
                "║..................│" +
                "║..................│" +
                "║..................│" +
                "║.................E│" +
                "└──────────────────┘");

        //then
        assertL("╔═══════════════" +
                "║S.............." +
                "║..............." +
                "║....┌──╗......." +
                "║....│  ║......." +
                "║..┌─┘  └─╗....." +
                "║..│      ║....." +
                "║..│      ║....." +
                "║..╚═┐  ╔═╝....." +
                "║....│  ║......." +
                "║....╚══╝......." +
                "║..............." +
                "║..............." +
                "║..............." +
                "║..............." +
                "║...............");

        //when
        for (int i = 0; i < 12; ++i) {
            hero.right();
            game.tick();
        }

        //then
        assertL("╔═══════════════" +
                "║S.............." +
                "║..............." +
                "║....┌──╗......." +
                "║....│  ║......." +
                "║..┌─┘  └─╗....." +
                "║..│      ║....." +
                "║..│      ║....." +
                "║..╚═┐  ╔═╝....." +
                "║....│  ║......." +
                "║....╚══╝......." +
                "║..............." +
                "║..............." +
                "║..............." +
                "║..............." +
                "║...............");

        //when
        hero.right();
        game.tick();

        //then
        assertL("════════════════" +
                "S..............." +
                "................" +
                "....┌──╗........" +
                "....│  ║........" +
                "..┌─┘  └─╗......" +
                "..│      ║......" +
                "..│      ║......" +
                "..╚═┐  ╔═╝......" +
                "....│  ║........" +
                "....╚══╝........" +
                "................" +
                "................" +
                "................" +
                "................" +
                "................");


        //when
        for (int i = 0; i < 12; ++i) {
            hero.right();
            game.tick();
        }

        //then
        assertL("═══════════════┐" +
                "...............│" +
                "...............│" +
                ".┌──╗..........│" +
                ".│  ║..........│" +
                "─┘  └─╗........│" +
                "      ║........│" +
                "      ║........│" +
                "═┐  ╔═╝........│" +
                ".│  ║..........│" +
                ".╚══╝..........│" +
                "...............│" +
                "...............│" +
                "...............│" +
                "...............│" +
                "...............│");

        //when
        hero.left();
        game.tick();

        for (int i = 0; i < 20; ++i) {
            hero.down();
            game.tick();
        }

        assertL(".│  ║..........│" +
                "─┘  └─╗........│" +
                "      ║........│" +
                "      ║........│" +
                "═┐  ╔═╝........│" +
                ".│  ║..........│" +
                ".╚══╝..........│" +
                "...............│" +
                "...............│" +
                "...............│" +
                "...............│" +
                "...............│" +
                "...............│" +
                "...............│" +
                "..............E│" +
                "───────────────┘");

        //when
        for (int i = 0; i < 20; ++i) {
            hero.left();
            game.tick();
        }

        assertL("║....│  ║......." +
                "║..┌─┘  └─╗....." +
                "║..│      ║....." +
                "║..│      ║....." +
                "║..╚═┐  ╔═╝....." +
                "║....│  ║......." +
                "║....╚══╝......." +
                "║..............." +
                "║..............." +
                "║..............." +
                "║..............." +
                "║..............." +
                "║..............." +
                "║..............." +
                "║..............." +
                "└───────────────");
    }
}
