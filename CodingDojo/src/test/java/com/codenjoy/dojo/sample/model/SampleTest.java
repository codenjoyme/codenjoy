package com.codenjoy.dojo.sample.model;

import com.codenjoy.dojo.loderunner.model.LoderunnerTest;
import com.codenjoy.dojo.sample.services.SampleEvents;
import com.codenjoy.dojo.services.Dice;
import com.codenjoy.dojo.services.EventListener;
import com.codenjoy.dojo.services.Printer;
import org.junit.Before;
import org.junit.Test;
import org.mockito.stubbing.OngoingStubbing;

import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertTrue;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Mockito.*;

/**
 * User: sanja
 * Date: 17.12.13
 * Time: 4:47
 */
public class SampleTest {

    private Sample game;
    private Hero hero;
    private Dice dice;
    private EventListener listener;
    private Player player;

    @Before
    public void setup() {
        dice = mock(Dice.class);
    }

    private void dice(int...ints) {
        OngoingStubbing<Integer> when = when(dice.next(anyInt()));
        for (int i : ints) {
            when = when.thenReturn(i);
        }
    }

    private void givenFl(String board) {
        LevelImpl level = new LevelImpl(board);
        Hero hero = level.getHero().get(0);

        game = new Sample(level, dice);
        listener = mock(EventListener.class);
        player = new Player(listener);
        game.newGame(player);
        player.hero = hero;
        hero.init(game);
        this.hero = game.getHeroes().get(0);
    }

    private void assertE(String expected) {
        LoderunnerTest.assertE(Printer.getSimpleFor(game.reader(), player, Elements.NONE), expected);
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
    public void shouldMakeBomb() {
        givenFl("☼☼☼☼☼" +
                "☼   ☼" +
                "☼ ☺ ☼" +
                "☼   ☼" +
                "☼☼☼☼☼");

        hero.act();
        hero.down();
        game.tick();

        assertE("☼☼☼☼☼" +
                "☼   ☼" +
                "☼ x ☼" +
                "☼ ☺ ☼" +
                "☼☼☼☼☼");
    }

    // на бомбе я взрываюсь
    @Test
    public void shouldDieOnBomb() {
        shouldMakeBomb();

        assertTrue(hero.isAlive());

        hero.up();
        game.tick();
        verify(listener).event(SampleEvents.LOOSE);

        assertE("☼☼☼☼☼" +
                "☼   ☼" +
                "☼ X ☼" +
                "☼   ☼" +
                "☼☼☼☼☼");

        assertFalse(hero.isAlive());
    }

    // я могу оставить бомб сколько хочу
    @Test
    public void shouldMakeBombTwice() {
        shouldMakeBomb();

        hero.act();
        hero.right();
        game.tick();

        assertE("☼☼☼☼☼" +
                "☼   ☼" +
                "☼ x ☼" +
                "☼ x☺☼" +
                "☼☼☼☼☼");
    }

    // я могу собирать золото и получать очки
    // новое золото появится в рендомном месте
    @Test
    public void shouldGetGold() {
        givenFl("☼☼☼☼☼" +
                "☼   ☼" +
                "☼ ☺$☼" +
                "☼   ☼" +
                "☼☼☼☼☼");

        dice(1, 3);
        hero.right();
        game.tick();
        verify(listener).event(SampleEvents.WIN);

        assertE("☼☼☼☼☼" +
                "☼$  ☼" +
                "☼  ☺☼" +
                "☼   ☼" +
                "☼☼☼☼☼");
    }

    // выполнения команд left + act не зависят от порядка - если они сделаны в одном тике, то будет дырка слева без перемещения
    @Test
    public void shouldMakeBomb2() {
        givenFl("☼☼☼☼☼" +
                "☼   ☼" +
                "☼ ☺ ☼" +
                "☼   ☼" +
                "☼☼☼☼☼");

        hero.down();
        hero.act();
//        hero.down();
        game.tick();

        assertE("☼☼☼☼☼" +
                "☼   ☼" +
                "☼ x ☼" +
                "☼ ☺ ☼" +
                "☼☼☼☼☼");
    }

    // проверить, что если новому обекту не где появится то программа не зависает - там бесконечный цикл потенциальный есть
    @Test(timeout = 1000)
    public void shouldNoDeadLoopWhenNewObjectCreation() {
        givenFl("☼☼☼☼☼" +
                "☼   ☼" +
                "☼ ☺$☼" +
                "☼   ☼" +
                "☼☼☼☼☼");

        dice(2, 2);
        hero.right();
        game.tick();
        verify(listener).event(SampleEvents.WIN);

        assertE("☼☼☼☼☼" +
                "☼   ☼" +
                "☼ $☺☼" +
                "☼   ☼" +
                "☼☼☼☼☼");
    }

    // я не могу ставить две бомбы на одной клетке
    @Test
    public void shouldMakeOnlyOneBomb() {
        givenFl("☼☼☼☼☼" +
                "☼   ☼" +
                "☼ ☺ ☼" +
                "☼   ☼" +
                "☼☼☼☼☼");

        hero.act();
        game.tick();

        hero.act();
        hero.down();
        game.tick();

        assertE("☼☼☼☼☼" +
                "☼   ☼" +
                "☼ x ☼" +
                "☼ ☺ ☼" +
                "☼☼☼☼☼");

        dice(1, 2);
        hero.up();
        game.tick();

        assertE("☼☼☼☼☼" +
                "☼   ☼" +
                "☼ X ☼" +
                "☼   ☼" +
                "☼☼☼☼☼");

        game.newGame(player);
        game.tick();

        assertE("☼☼☼☼☼" +
                "☼   ☼" +
                "☼☺  ☼" +
                "☼   ☼" +
                "☼☼☼☼☼");
    }
}
