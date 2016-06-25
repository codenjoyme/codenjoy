package com.epam.dojo.icancode.model;

import com.codenjoy.dojo.services.Dice;
import com.codenjoy.dojo.services.EventListener;
import com.codenjoy.dojo.services.Joystick;
import com.codenjoy.dojo.utils.TestUtils;
import com.epam.dojo.icancode.model.items.Hero;
import com.epam.dojo.icancode.services.Events;
import com.epam.dojo.icancode.services.Levels;
import com.epam.dojo.icancode.services.Printer;
import org.junit.Before;
import org.junit.Test;
import org.mockito.stubbing.OngoingStubbing;

import java.util.*;

import static junit.framework.Assert.*;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Mockito.*;

/**
 * User: sanja
 * Date: 17.12.13
 * Time: 4:47
 */
public class SingleTest {

    private Dice dice;
    private EventListener listener;
    private Single single;

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
        Deque<String> strings = new LinkedList<>(Arrays.asList(boards));
        String multiple = strings.removeLast();
        List<ILevel> levelsSingle = createLevels(strings.toArray(new String[]{}));
        List<ILevel> levelMultiple = createLevels(new String[] { multiple });

        ICanCode gameSingle = new ICanCode(levelsSingle, dice);
        ICanCode gameMultiple = new ICanCode(levelMultiple, dice);
        listener = mock(EventListener.class);

        single = new Single(gameSingle, gameMultiple, listener, null);
        single.newGame();
    }

    private Joystick hero() {
        return single.getJoystick();
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
                single.getPrinter().getBoardAsString(1, single.getPlayer())[0]);
    }

    private void assertE(String expected) {
        assertEquals(TestUtils.injectN(expected),
                single.getPrinter().getBoardAsString(2, single.getPlayer())[1]);
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
                "└──┘",
                "╔══┐" +
                "║S.│" +
                "║.E│" +
                "└──┘");

        // when
        hero().right();
        single.tick();

        verify(listener).event(Events.WIN(0));
        reset(listener);

        assertL("╔══┐" +
                "║SE│" +
                "║..│" +
                "└──┘");

        assertE("----" +
                "--☺-" +
                "----" +
                "----");

        // when
        single.tick();

        // then
        assertL("╔══┐" +
                "║S.│" +
                "║E.│" +
                "└──┘");

        assertE("----" +
                "-☺--" +
                "----" +
                "----");

        // when
        hero().down();
        single.tick();

        // then
        verify(listener).event(Events.WIN(0));
        reset(listener);

        assertL("╔══┐" +
                "║S.│" +
                "║E.│" +
                "└──┘");

        assertE("----" +
                "----" +
                "-☺--" +
                "----");

        // when
        single.tick();

        // then
        assertL("╔══┐" +
                "║S.│" +
                "║.E│" +
                "└──┘");

        assertE("----" +
                "-☺--" +
                "----" +
                "----");

        // when
        hero().down();
        single.tick();

        // then
        assertL("╔══┐" +
                "║S.│" +
                "║.E│" +
                "└──┘");

        assertE("----" +
                "----" +
                "-☺--" +
                "----");

        // when
        hero().right();
        single.tick();

        // then
        verify(listener).event(Events.WIN(0));
        reset(listener);

        assertL("╔══┐" +
                "║S.│" +
                "║.E│" +
                "└──┘");

        assertE("----" +
                "----" +
                "--☺-" +
                "----");

        // when
        single.tick();

        // then
        verifyNoMoreInteractions(listener);

        assertL("╔══┐" +
                "║S.│" +
                "║.E│" +
                "└──┘");

        assertE("----" +
                "-☺--" +
                "----" +
                "----");
    }

//    @Test
//    public void shouldSeveralPlayersCollectionAtLastLevel() {
//        // given
//        givenFl("╔══┐" +
//                "║SE│" +
//                "║..│" +
//                "└──┘",
//                "╔══┐" +
//                "║S.│" +
//                "║.E│" +
//                "└──┘");
//
//        // when
//        hero().right();
//        single.tick();
//
//        verify(listener).event(Events.WIN(0));
//        reset(listener);
//
//        assertL("╔══┐" +
//                "║SE│" +
//                "║..│" +
//                "└──┘");
//
//        assertE("----" +
//                "--☺-" +
//                "----" +
//                "----");
//
//        // when
//        single.tick();
//
//        // then
//        assertL("╔══┐" +
//                "║S.│" +
//                "║E.│" +
//                "└──┘");
//
//        assertE("----" +
//                "-☺--" +
//                "----" +
//                "----");
//
//        // when
//        hero().down();
//        single.tick();
//
//        // then
//        verify(listener).event(Events.WIN(0));
//        reset(listener);
//
//        assertL("╔══┐" +
//                "║S.│" +
//                "║E.│" +
//                "└──┘");
//
//        assertE("----" +
//                "----" +
//                "-☺--" +
//                "----");
//
//        // when
//        single.tick();
//
//        // then
//        assertL("╔══┐" +
//                "║S.│" +
//                "║.E│" +
//                "└──┘");
//
//        assertE("----" +
//                "-☺--" +
//                "----" +
//                "----");
//
//        // when
//        hero().down();
//        single.tick();
//
//        // then
//        assertL("╔══┐" +
//                "║S.│" +
//                "║.E│" +
//                "└──┘");
//
//        assertE("----" +
//                "----" +
//                "-☺--" +
//                "----");
//
//        // when
//        hero().right();
//        single.tick();
//
//        // then
//        verify(listener).event(Events.WIN(0));
//        reset(listener);
//
//        assertL("╔══┐" +
//                "║S.│" +
//                "║.E│" +
//                "└──┘");
//
//        assertE("----" +
//                "----" +
//                "--☺-" +
//                "----");
//
//        // when
//        single.tick();
//
//        // then
//        verifyNoMoreInteractions(listener);
//
//        assertL("╔══┐" +
//                "║S.│" +
//                "║.E│" +
//                "└──┘");
//
//        assertE("----" +
//                "-☺--" +
//                "----" +
//                "----");
//    }

}
