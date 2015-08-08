package com.codenjoy.dojo.spacerace.model;

import com.codenjoy.dojo.services.Dice;
import com.codenjoy.dojo.services.EventListener;
import com.codenjoy.dojo.services.PrinterFactory;
import com.codenjoy.dojo.services.PrinterFactoryImpl;
import com.codenjoy.dojo.spacerace.services.Events;
import com.codenjoy.dojo.utils.TestUtils;
import org.junit.Before;
import org.junit.Test;
import org.mockito.stubbing.OngoingStubbing;

import static junit.framework.Assert.assertEquals;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * User: sanja
 * Date: 17.12.13
 * Time: 4:47
 */
public class SpaceraceTest {

    private Spacerace game;
    private Hero hero;
    private Dice dice;
    private EventListener listener;
    private Player player;
    private PrinterFactory printer = new PrinterFactoryImpl();

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

        game = new Spacerace(level, dice);
        listener = mock(EventListener.class);
        player = new Player(listener);
        game.newGame(player);
        player.hero = hero;
        hero.init(game);
        this.hero = game.getHeroes().get(0);
    }

    private void assertE(String expected) {
        assertEquals(TestUtils.injectN(expected),
                printer.getPrinter(game.reader(), player).print());
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
    public void shouldFieldIcanMove() {
        //given
        givenFl("☼   ☼" +
                "☼   ☼" +
                "☼ ☺ ☼" +
                "☼   ☼" +
                "☼   ☼");
        //when
        dice(-1); // выключаем генерацию каменей и мин
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
                "☼ ☺ ☼" +
                "☼   ☼" +
                "☼   ☼");

        //When
        game.addStone(2);
        game.tick();

        //Then
        assertE("☼ 0 ☼" +
                "☼   ☼" +
                "☼ ☺ ☼" +
                "☼   ☼" +
                "☼   ☼");
        //When
        game.addStone(3);
        game.tick();

        //Then
        assertE("☼  0☼" +
                "☼ 0 ☼" +
                "☼ ☺ ☼" +
                "☼   ☼" +
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
        game.addStone(1);
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
        dice(0, -1,  1); // камень в первой колонке, мины нет, камень во второй колонке
        game.tick();
        game.tick();
        game.tick();
        game.tick();
        game.tick();
        game.tick();

        //Then
        assertE("☼ 0 ☼" +
                "☼   ☼" +
                "☼   ☼" +
                "☼0  ☼" +
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
        dice(-1);
        hero.act();
        game.tick();
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
    public void shouldStoneDestroyedByBullet() {
        //Given
        givenFl("☼   ☼" +
                "☼   ☼" +
                "☼   ☼" +
                "☼   ☼" +
                "☼ ☺ ☼");
        //When
        dice(1, -1);
        hero.act();
        game.tick();

        //Then
        assertE("☼   ☼" +
                "☼   ☼" +
                "☼   ☼" +
                "☼ * ☼" +
                "☼ ☺ ☼");

        //When
        game.tick();

        //Then
        assertE("☼   ☼" +
                "☼   ☼" +
                "☼ * ☼" +
                "☼   ☼" +
                "☼ ☺ ☼");

        //When
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
    public void shouldStoneDestroyedByBullet2() {
        //Given
        givenFl("☼   ☼" +
                "☼   ☼" +
                "☼   ☼" +
                "☼ ☺ ☼" +
                "☼   ☼");

        //When
        dice(1, -1);
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
        dice(-1, 1); // камень не появляем, мину появляем, тоже на 3-м тике
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
                "☼☺  ☼");

        //when
        dice(-1, 2);
        game.tick();
        game.tick();
        game.tick();

        //then
        assertE("☼  ♣☼" +
                "☼   ☼" +
                "☼   ☼" +
                "☼   ☼" +
                "☼☺  ☼");
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
        dice(0, 2);
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
        dice(-1, 2, 2, 2);
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
                "☼  ♣☼" +
                "☼   ☼" +
                "☼   ☼" +
                "☼☺ ♣☼");

        //When
        game.tick();

        //Then
        assertE("☼   ☼" +
                "☼   ☼" +
                "☼  ♣☼" +
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


        dice(-1, 1);
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


        dice(-1, 1);
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

        dice(1, 3);
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

        dice(0, 2);

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

        dice(0, 2);

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
        assertE("☼  0 ☼" +
                "☼    ☼" +
                "☼ xxx☼" +
                "☼0xxx☼" +
                "☼ xxx☼" +
                "☼    ☼");

        game.tick();


        // then
        assertE("☼    ☼" +
                "☼  0 ☼" +
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
        dice(0, 2);
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
        assertE("☼  0 ☼" +
                "☼    ☼" +
                "☼ xxx☼" +
                "☼0xxx☼" +
                "☼ xxx☼" +
                "☼    ☼");
        game.tick();
        // then
        assertE("☼    ☼" +
                "☼  0 ☼" +
                "☼    ☼" +
                "☼    ☼" +
                "☼0 + ☼" +
                "☼    ☼");
        game.tick();
        // then
        assertE("☼    ☼" +
                "☼    ☼" +
                "☼  0 ☼" +
                "☼    ☼" +
                "☼  ☺ ☼" +
                "☼0   ☼");
    }




    @Test
    public void shouldBombDestroyMeNearRightAndResurrectHero() {
        // given
        givenFl("☼    ☼" +
                "☼    ☼" +
                "☼    ☼" +
                "☼    ☼" +
                "☼  ☺ ☼" +
                "☼    ☼");

        dice(0, 3);

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
        assertE("☼   0☼" +
                "☼    ☼" +
                "☼  xxx" +
                "☼0 xxx" +
                "☼  xxx" +
                "☼    ☼");
        game.tick();

        // then
        assertE("☼    ☼" +
                "☼   0☼" +
                "☼    ☼" +
                "☼    ☼" +
                "☼0 + ☼" +
                "☼    ☼");
        game.tick();

        // then
        assertE("☼    ☼" +
                "☼    ☼" +
                "☼   0☼" +
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
        dice(2, -1);
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
    public void shouldCountScores() {
        // given
        givenFl("☼   ☼" +
                "☼   ☼" +
                "☼ ☺ ☼" +
                "☼   ☼" +
                "☼   ☼");
        assertEquals(0, player.getScore());
        player.event(Events.WIN);
        assertEquals(1, player.getScore());
        player.event(Events.WIN);
        player.event(Events.WIN);
        player.event(Events.WIN);
        assertEquals(4, player.getScore());
        player.event(Events.LOOSE);
        assertEquals(0, player.getScore());
        assertEquals(4, player.getMaxScore());
    }

    // логику подсчета очков выделить в сеттингс
         // разрушение бомбы
         // разрушение камня
         // разрушение других игроков
    // перезарядка патронов
        // выделить в сеттингс сколько и за соклько тиков
    // привязать пули к игроку, и каждый получат очки за свое
    // реикарнация героя внизу поля
    // реинкарнация героя очки онимает
    // реинкарнация героя обновляет обойму

    // итераторы
    // инструкция
    // золото/здоровье
    // лазер
    // выстрелы не каждый тик (?)
    // плюшка, которая позволяет стрелать каждый тик, пока она действует (время или количество снарядов)
    // узнать причину загадочного ексепшна при вылете игры и исправить
    // написать нормального бота


}
