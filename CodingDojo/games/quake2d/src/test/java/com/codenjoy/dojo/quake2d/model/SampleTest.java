package com.codenjoy.dojo.sample.model;

import com.codenjoy.dojo.services.*;
import com.codenjoy.dojo.utils.TestUtils;
import com.codenjoy.dojo.sample.services.Events;
import org.junit.Assert;
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
public class SampleTest {

    private Sample game;
    private Hero hero;
    private Dice dice;
    private EventListener listener;
    private Player player;
    private Player otherPlayer;
    private Hero otherHero;
    private Ability ability;
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


        game = new Sample(level, dice);
        listener = mock(EventListener.class);
        player = new Player(listener);
        game.newGame(player);
        player.hero = hero;
        hero.init(game);
        player.hero.setHealth(Hero.START_HEALTH - player.hero.getHealth());
        this.hero = game.getHeroes().get(0);


        if (level.getOtherHero().size() != 0){
            Hero otherHero = level.getOtherHero().get(0);
            otherPlayer = new Player(listener);
            game.newGame(otherPlayer);
            otherPlayer.hero = otherHero;
            otherHero.init(game);
            otherPlayer.hero.setHealth(Hero.START_HEALTH - player.hero.getHealth());
            this.otherHero = game.getHeroes().get(1);
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
        otherPlayer.hero.setHealth(-(Hero.START_HEALTH-Bullet.START_DAMAGE));
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

        otherPlayer.hero.setHealth(-(Hero.START_HEALTH-Bullet.START_DAMAGE));
        game.tick();

        assertE("☼☼☼☼☼☼" +
                "☼ X  ☼" +
                "☼    ☼" +
                "☼ ☺  ☼" +
                "☼    ☼" +
                "☼☼☼☼☼☼");

        game.tick();
        otherHero.move(4, 3);
        otherPlayer.hero = otherHero;

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


        makeTicks(Sample.ABILITY_TIME_EXIST);
        ability = game.getAbilities().get(0);
        ability.move(3, 3);

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


        makeTicks(Sample.ABILITY_TIME_EXIST);
        ability = game.getAbilities().get(0);
        ability.move(3, 3);
        player.hero.up();
        game.tick();
        player.hero.right();
        game.tick();

        assertE("☼☼☼☼☼" +
                "☼  ☺☼" +
                "☼   ☼" +
                "☼   ☼" +
                "☼☼☼☼☼");
        Assert.assertNotNull(player.hero.getAbility());
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
        makeTicks(Sample.ABILITY_TIME_EXIST);
        game.getAbilities().get(0).move(2, 2);
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

//        otherPlayer.hero.setHealth(Bullet.START_DAMAGE);
        game.tick();

        assertE("☼☼☼☼☼☼" +
                "☼ ☻  ☼" +
                "☼    ☼" +
                "☼ ☺  ☼" +
                "☼    ☼" +
                "☼☼☼☼☼☼");
        Assert.assertEquals(otherPlayer.hero.getHealth(), Hero.START_HEALTH - Bullet.START_DAMAGE*Bullet.WEAPON_MULTIPLICATOR);
//
//        game.tick();
//        otherHero.move(4, 3);
//        otherPlayer.hero = otherHero;
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


        makeTicks(Sample.ABILITY_TIME_EXIST);
        ability = game.getAbilities().get(0);
        game.getAbilities().add(0, new Ability(new PointImpl(3,3), Ability.Type.HEALTH));

        ability.move(3, 3);
        player.hero.up();
        game.tick();
        player.hero.setHealth(-(Bullet.START_DAMAGE + Ability.HEALTH_BONUS));
        player.hero.right();
        game.tick();

        assertE("☼☼☼☼☼" +
                "☼  ☺☼" +
                "☼   ☼" +
                "☼   ☼" +
                "☼☼☼☼☼");
        Assert.assertEquals(Hero.START_HEALTH - Bullet.START_DAMAGE + (Ability.HEALTH_BONUS - Ability.HEALTH_BONUS), player.hero.getHealth());
    }

    @Test
    public void shouldOtherHeroBecomeSuperHero() {
        givenFl("☼☼☼☼☼☼" +
                "☼ ☺  ☼" +
                "☼    ☼" +
                "☼    ☼" +
                "☼ ☻  ☼" +
                "☼☼☼☼☼☼");
        makeTicks(Sample.ABILITY_TIME_EXIST);
        game.getAbilities().get(0).move(2, 2);
        otherPlayer.hero.up();
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
