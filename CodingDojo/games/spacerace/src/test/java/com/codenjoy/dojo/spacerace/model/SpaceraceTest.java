package com.codenjoy.dojo.spacerace.model;

import com.codenjoy.dojo.services.Dice;
import com.codenjoy.dojo.services.EventListener;
import com.codenjoy.dojo.services.PrinterFactory;
import com.codenjoy.dojo.services.PrinterFactoryImpl;
import com.codenjoy.dojo.spacerace.services.Events;
import com.codenjoy.dojo.utils.TestUtils;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.mockito.stubbing.OngoingStubbing;

import static junit.framework.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * User: sanja
 * Date: 17.12.13
 * Time: 4:47
 */
public class SpaceraceTest {

    public final static BulletCharger UNLIMITED_CHARGER = new BulletCharger(1000, 10);
    private Spacerace game;
    private BulletCharger charger = UNLIMITED_CHARGER;
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
        Hero hero = level.getHero(charger).get(0);

        game = new Spacerace(level, dice,
                charger.getTicksToRecharge(),
                charger.getBulletsCount());
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
                "☼7  ☼" +
                "☼ ☺ ☼" +
                "☼   ☼" +
                "☼   ☼");
        //When
        game.addStone(3);
        game.tick();

        //Then
        assertE("☼  0☼" +
                "☼70 ☼" +
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
                "☼7  ☼" +
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
        dice(1, 1, 0, -1,  1); // камень в первой колонке, мины нет, камень во второй колонке
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
        hero.recharge();
        hero.act();
        game.tick();
        game.tick();

        //Then
        assertE("☼   ☼" +
                "☼7* ☼" +
                "☼   ☼" +
                "☼ ☺ ☼" +
                "☼   ☼");
    }

    // перезарядка патронов
    // выделить в сеттингс сколько и за соклько тиков

    @Ignore// пока таймера нет
    @Test
    public void shouldHeroRechargeBulletsWhenItEmpty() {
        //Given
        int ticksToRecharge = 5;
        int bulletsCount = 3;
        charger = new BulletCharger(ticksToRecharge, bulletsCount);
        dice(-1, -1);

        givenFl("☼   ☼" +
                "☼   ☼" +
                "☼   ☼" +
                "☼   ☼" +
                "☼ ☺ ☼");

        //When
        hero.recharge();
        hero.act();
        game.tick();

        //Then
        assertE("☼   ☼" +
                "☼   ☼" +
                "☼   ☼" +
                "☼ * ☼" +
                "☼ ☺ ☼");

        //When
        hero.act();
        game.tick();

        //Then
        assertE("☼   ☼" +
                "☼   ☼" +
                "☼ * ☼" +
                "☼ * ☼" +
                "☼ ☺ ☼");

        //When
        hero.act();
        game.tick();

        //Then
        assertE("☼   ☼" +
                "☼ * ☼" +
                "☼ * ☼" +
                "☼ * ☼" +
                "☼ ☺ ☼");

        //When
        hero.act();
        game.tick();

        //Then
        assertE("☼ * ☼" +
                "☼ * ☼" +
                "☼ * ☼" +
                "☼   ☼" +
                "☼ ☺ ☼");

        //When
        hero.act();
        game.tick();

        //Then
        assertE("☼ * ☼" +
                "☼ * ☼" +
                "☼   ☼" +
                "☼   ☼" +
                "☼ ☺ ☼");

        //When
        hero.act();
        game.tick();

        //Then
        assertE("☼ * ☼" +
                "☼   ☼" +
                "☼   ☼" +
                "☼ * ☼" +
                "☼ ☺ ☼");

        //When
        hero.act();
        game.tick();

        //Then
        assertE("☼   ☼" +
                "☼   ☼" +
                "☼ * ☼" +
                "☼ * ☼" +
                "☼ ☺ ☼");

        //When
        hero.act();
        game.tick();

        //Then
        assertE("☼   ☼" +
                "☼ * ☼" +
                "☼ * ☼" +
                "☼ * ☼" +
                "☼ ☺ ☼");

        //When
        hero.act();
        game.tick();

        //Then
        assertE("☼ * ☼" +
                "☼ * ☼" +
                "☼ * ☼" +
                "☼   ☼" +
                "☼ ☺ ☼");
    }

    // я могу удалить своего игрока из поля, он появится в новом месте и перезарядится
    // реинкарнация героя очки онимает (самоубийство)
    // реинкарнация героя обновляет обойму
    @Ignore // сейчас нет таймера
    @Test
    public void shouldRechargeWhenDie_Timer_ON() {
        //Given
        int ticksToRecharge = 50;
        int bulletsCount = 3;
        charger = new BulletCharger(ticksToRecharge, bulletsCount);
        dice(-1, -1);

        givenFl("☼   ☼" +
                "☼   ☼" +
                "☼   ☼" +
                "☼   ☼" +
                "☼ ☺ ☼");

        //When
        hero.act();
        game.tick();

        //Then
        assertE("☼   ☼" +
                "☼   ☼" +
                "☼   ☼" +
                "☼ * ☼" +
                "☼ ☺ ☼");

        //When
        hero.act();
        game.tick();

        //Then
        assertE("☼   ☼" +
                "☼   ☼" +
                "☼ * ☼" +
                "☼ * ☼" +
                "☼ ☺ ☼");

        //When
        hero.act();
        game.tick();

        //Then
        assertE("☼   ☼" +
                "☼ * ☼" +
                "☼ * ☼" +
                "☼ * ☼" +
                "☼ ☺ ☼");

        //When
        hero.act();
        game.tick();

        //Then
        assertE("☼ * ☼" +
                "☼ * ☼" +
                "☼ * ☼" +
                "☼   ☼" +
                "☼ ☺ ☼");

        //When
        hero.act(0);
        game.tick();

        verify(listener).event(Events.LOOSE);

        assertFalse(hero.isAlive());

        //Then
        assertE("☼ * ☼" +
                "☼ * ☼" +
                "☼   ☼" +
                "☼   ☼" +
                "☼ + ☼");

        dice(1, 0);
        game.newGame(player);
        hero = player.getHero();
        game.tick();

        //Then
        assertE("☼ * ☼" +
                "☼   ☼" +
                "☼   ☼" +
                "☼   ☼" +
                "☼☺  ☼");

        //When
        hero.act();
        game.tick();

        //Then
        assertE("☼   ☼" +
                "☼   ☼" +
                "☼   ☼" +
                "☼*  ☼" +
                "☼☺  ☼");
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
        dice(1, 1, 1, -1);
        hero.recharge();
        hero.act();
        game.tick();

        //Then
        assertE("☼ 7 ☼" +
                "☼   ☼" +
                "☼   ☼" +
                "☼ * ☼" +
                "☼ ☺ ☼");

        //When
        game.tick();

        //Then
        assertE("☼ 7 ☼" +
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
        dice(1, 1, 1, -1);
        hero.recharge();
        hero.act();
        game.tick();

        //Then
        assertE("☼ 7 ☼" +
                "☼   ☼" +
                "☼ * ☼" +
                "☼ ☺ ☼" +
                "☼   ☼");

        //When
        game.tick();

        //Then
        assertE("☼ 7 ☼" +
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
        assertE("☼ 7 ☼" +
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
        dice(1, 1, -1, 1); // камень не появляем, мину появляем, тоже на 3-м тике
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
        dice(1, 1, -1, 2);
        game.tick();
        game.tick();
        game.tick();

        //then
        assertE("☼ 7♣☼" +
                "☼   ☼" +
                "☼   ☼" +
                "☼   ☼" +
                "☼☺  ☼");
    }
// не понятно, почему перед первым тиком уже появился магазин
    @Test
    public void shouldNewBombAndNewStoneAtNewPlace() {
        //given
        givenFl("☼   ☼" +
                "☼   ☼" +
                "☼   ☼" +
                "☼ ☺ ☼" +
                "☼   ☼");

        //when
        dice(1, 1, 0, 2);
        game.tick();
        game.tick();
        game.tick();
        game.tick();

        //then
        assertE("☼ 7 ☼" +
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
        dice(1, 1, -1, 2, 2, 2);
        game.tick();
        game.tick();
        game.tick();

        //Then
        assertE("☼ 7♣☼" +
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
        assertE("☼ 7 ☼" +
                "☼  ♣☼" +
                "☼   ☼" +
                "☼   ☼" +
                "☼☺ ♣☼");

        //When
        game.tick();

        //Then
        assertE("☼ 7 ☼" +
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


        dice(1, 1, -1, 1);
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


        dice(1, 1, -1, 1);
        hero.recharge();
        hero.act();
        game.tick();
        game.tick();




        assertE("☼ 7 ☼" +
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

        assertE("☼ 7 ☼" +
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

        dice(1, 1, 1, 3);
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
        hero.recharge();
        dice(1, 1, 0, 2);

        game.tick();
        game.tick();
        hero.act();
        game.tick();
        game.tick();

        // then
        assertE("☼ 7  ☼" +
                "☼0 ♣ ☼" +
                "☼    ☼" +
                "☼  * ☼" +
                "☼    ☼" +
                "☼  ☺ ☼");

        game.tick();

        // then
        assertE("☼ 7  ☼" +
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

        dice(1, 1, 0, 2);
        hero.recharge();
        game.tick();
        game.tick();

        game.tick();
        game.tick();

        // then
        assertE("☼ 7  ☼" +
                "☼0 ♣ ☼" +
                "☼    ☼" +
                "☼    ☼" +
                "☼  ☺ ☼" +
                "☼    ☼");

        game.tick();
        game.tick();


        // then
        assertE("☼ 70 ☼" +
                "☼    ☼" +
                "☼ xxx☼" +
                "☼0xxx☼" +
                "☼ xxx☼" +
                "☼    ☼");

        game.tick();


        // then
        assertE("☼ 7  ☼" +
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
        dice(1, 1, 0, 2);
        game.tick();
        game.tick();
        game.tick();
        game.tick();
        // then
        assertE("☼ 7  ☼" +
                "☼0 ♣ ☼" +
                "☼    ☼" +
                "☼    ☼" +
                "☼  ☺ ☼" +
                "☼    ☼");
        game.tick();
        game.tick();
        // then
        assertE("☼ 70 ☼" +
                "☼    ☼" +
                "☼ xxx☼" +
                "☼0xxx☼" +
                "☼ xxx☼" +
                "☼    ☼");
        game.tick();
        // then
        assertE("☼ 7  ☼" +
                "☼  0 ☼" +
                "☼    ☼" +
                "☼    ☼" +
                "☼0 + ☼" +
                "☼    ☼");
        game.tick();
        // then
        assertE("☼ 7  ☼" +
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

        dice(1, 1, 0, 3);

        game.tick();
        game.tick();

        game.tick();
        game.tick();

        // then
        assertE("☼ 7  ☼" +
                "☼0  ♣☼" +
                "☼    ☼" +
                "☼    ☼" +
                "☼  ☺ ☼" +
                "☼    ☼");

        game.tick();
        game.tick();

        // then
        assertE("☼ 7 0☼" +
                "☼    ☼" +
                "☼  xxx" +
                "☼0 xxx" +
                "☼  xxx" +
                "☼    ☼");
        game.tick();

        // then
        assertE("☼ 7  ☼" +
                "☼   0☼" +
                "☼    ☼" +
                "☼    ☼" +
                "☼0 + ☼" +
                "☼    ☼");
        game.tick();

        // then
        assertE("☼ 7  ☼" +
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
        dice(1, 1, 2, -1);
        game.tick();
        game.tick();
        game.tick();
        game.tick();
        // then
        assertE("☼ 7  ☼" +
                "☼  0 ☼" +
                "☼    ☼" +
                "☼    ☼" +
                "☼  ☺ ☼" +
                "☼    ☼");

        game.tick();
        game.tick();
        // then
        assertE("☼ 7  ☼" +
                "☼    ☼" +
                "☼    ☼" +
                "☼  0 ☼" +
                "☼  ☺ ☼" +
                "☼    ☼");
        game.tick();
        // then
        assertE("☼ 7  ☼" +
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
        player.event(Events.DESTROY_BOMB);

        assertEquals(1, player.getScore());
        player.event(Events.DESTROY_STONE);
        player.event(Events.DESTROY_STONE);

        assertEquals(3, player.getScore());
        player.event(Events.DESTROY_ENEMY);

        assertEquals(4, player.getScore());
        player.event(Events.LOOSE);

        assertEquals(0, player.getScore());
        assertEquals(4, player.getMaxScore());
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
        dice(-1, -1, 1, 1);
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
        dice(1, 1, -1, -1, 0, 0, 3);
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
                "☼7   ☼" +
                "☼    ☼" +
                "☼    ☼" +
                "☼    ☼" +
                "☼    ☼");
        hero.left();
        game.tick();
        assertE("☼☺   ☼" +
                "☼7   ☼" +
                "☼    ☼" +
                "☼    ☼" +
                "☼    ☼" +
                "☼    ☼");
        game.tick();

        // then
        assertE("☼☺   ☼" +
                "☼7   ☼" +
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
        dice(0, 0, -1, -1, -1, -1, 1, 1, -1);
        game.tick();
        // then
        assertE("☼    ☼" +
                "☼7☺  ☼" +
                "☼    ☼" +
                "☼    ☼" +
                "☼    ☼" +
                "☼    ☼");
        hero.left();
        game.tick();
        // then
        assertE("☼    ☼" +
                "☼☺   ☼" +
                "☼    ☼" +
                "☼    ☼" +
                "☼    ☼" +
                "☼    ☼");
        hero.right();
        game.tick();
        // then
        assertE("☼ 7  ☼" +
                "☼ ☺  ☼" +
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
        dice(0, 0, -1);
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

    @Test
    public void shouldNoBulletsAfteremptyBullets() {
       //Given
        givenFl("☼   ☼" +
                "☼ ☺ ☼" +
                "☼   ☼" +
                "☼   ☼" +
                "☼   ☼");
        //when
        dice(1, 1, -1, -1, -1, -1, 0, 0, -1);
        game.tick();
        //Given
        assertE("☼ 7 ☼" +
                "☼ ☺ ☼" +
                "☼   ☼" +
                "☼   ☼" +
                "☼   ☼");
        hero.up();
        game.tick();
        hero.down();
        game.tick();
        //Given
        assertE("☼   ☼" +
                "☼7☺ ☼" +
                "☼   ☼" +
                "☼   ☼" +
                "☼   ☼");
        //When
        hero.act();
        game.tick();
        hero.act();
        game.tick();
        hero.act();
        game.tick();
        hero.act();
        game.tick();
        hero.act();
        game.tick();
        hero.act();
        game.tick();
        hero.act();
        game.tick();
        hero.act();
        game.tick();
        hero.act();
        game.tick();
        hero.act();
        game.tick();
        //Given
        assertE("☼ * ☼" +
                "☼7☺ ☼" +
                "☼   ☼" +
                "☼   ☼" +
                "☼   ☼");
        hero.act();
        game.tick();
        hero.act();
        game.tick();
        //Given
        assertE("☼   ☼" +
                "☼7☺ ☼" +
                "☼   ☼" +
                "☼   ☼" +
                "☼   ☼");

    }

    @Ignore
    @Test
    public void shouldRechargeWhenDie_Timer_OFF() {
        //Given
//        int ticksToRecharge = 1000;
//        int bulletsCount = 1;
//        charger = new BulletCharger(ticksToRecharge, bulletsCount);


        givenFl("☼   ☼" +
                "☼   ☼" +
                "☼☺  ☼" +
                "☼   ☼" +
                "☼   ☼");

        //When
//        dice(0, 2, 0);
//        dice(1, -1);
//        hero.recharge();
        game.tick();
        game.tick();
        game.tick();

        assertE("☼0  ☼" +
                "☼7  ☼" +
                "☼☺  ☼" +
                "☼   ☼" +
                "☼   ☼");
//
////        //When
////        hero.act();
        game.tick();
        game.tick();
////
////
        //Then
        assertE("☼   ☼" +
                "☼7  ☼" +
                "☼+  ☼" +
                "☼   ☼" +
                "☼   ☼");

        //When
//        hero.act();
        game.tick();
//        game.tick();
//
        //Then
        assertE("☼   ☼" +
                "☼   ☼" +
                "☼   ☼" +
                "☼   ☼" +
                "☼   ☼");
//
//        //When
//        hero.act();
//        game.tick();
//
//        //Then
//        assertE("☼ * ☼" +
//                "☼ * ☼" +
//                "☼ * ☼" +
//                "☼   ☼" +
//                "☼ ☺ ☼");
//
//        //When
//        hero.act(0);
//        game.tick();
//
//        verify(listener).event(Events.LOOSE);
//
//        assertFalse(hero.isAlive());
//
//        //Then
//        assertE("☼ * ☼" +
//                "☼ * ☼" +
//                "☼   ☼" +
//                "☼   ☼" +
//                "☼ + ☼");
//
//        dice(1, 0);
//        game.newGame(player);
//        hero = player.getHero();
//        game.tick();
//
//        //Then
//        assertE("☼ * ☼" +
//                "☼   ☼" +
//                "☼   ☼" +
//                "☼   ☼" +
//                "☼☺  ☼");
//
//        //When
//        hero.act();
//        game.tick();
//
//        //Then
//        assertE("☼   ☼" +
//                "☼   ☼" +
//                "☼   ☼" +
//                "☼*  ☼" +
//                "☼☺  ☼");
    }


    @Test
    @Ignore
    public void shouldBombDestroyHeroAndResurrectionHeroWithRecharge() {
        // given
        givenFl("☼    ☼" +
                "☼    ☼" +
                "☼    ☼" +
                "☼    ☼" +
                "☼  ☺ ☼" +
                "☼    ☼");
        dice(1, 1, 0, 2);
        game.tick();
        game.tick();
        game.tick();
        game.tick();
        // then
        assertE("☼ 7  ☼" +
                "☼0 ♣ ☼" +
                "☼    ☼" +
                "☼    ☼" +
                "☼  ☺ ☼" +
                "☼    ☼");
        game.tick();
        game.tick();
        // then
        assertE("☼ 70 ☼" +
                "☼    ☼" +
                "☼ xxx☼" +
                "☼0xxx☼" +
                "☼ xxx☼" +
                "☼    ☼");
        game.tick();
        // then
        assertE("☼ 7  ☼" +
                "☼  0 ☼" +
                "☼    ☼" +
                "☼    ☼" +
                "☼0 + ☼" +
                "☼    ☼");
        game.tick();
        // then
        assertE("☼ 7  ☼" +
                "☼    ☼" +
                "☼  0 ☼" +
                "☼    ☼" +
                "☼  ☺ ☼" +
                "☼0   ☼");

        hero.act();
        game.tick();
        // then
        assertE("☼ 70 ☼" +
                "☼    ☼" +
                "☼    ☼" +
                "☼  0 ☼" +
                "☼  ☺ ☼" +
                "☼    ☼");
        game.tick();
        // then
        assertE("☼ 7  ☼" +
                "☼  0 ☼" +
                "☼    ☼" +
                "☼    ☼" +
                "☼  + ☼" +
                "☼    ☼");

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


}
