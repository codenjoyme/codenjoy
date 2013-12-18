package com.codenjoy.dojo.loderunner.model;

import com.codenjoy.dojo.services.Dice;
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
public class LoderunnerTest {

    private Loderunner game;
    private Hero hero;
    private Dice dice;

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

    // есть карта со мной
    @Test
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

    // если небыло команды я никуда не иду
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

    // я останавливаюсь возле границы
    @Test
    public void shouldStopWhenWallRight() {
        shouldMoveRight();

        hero.right();
        game.tick();

        assertE("☼☼☼☼☼\n" +
                "☼   ☼\n" +
                "☼  ►☼\n" +
                "☼###☼\n" +
                "☼☼☼☼☼\n");
    }

    @Test
    public void shouldStopWhenWallLeft() {
        shouldMoveLeft();

        hero.left();
        game.tick();

        assertE("☼☼☼☼☼\n" +
                "☼   ☼\n" +
                "☼◄  ☼\n" +
                "☼###☼\n" +
                "☼☼☼☼☼\n");
    }

    // яма заростает со временем
    @Test
    public void shouldDrillFilled() {
        shouldDrillLeft();

        game.tick();

        assertE("☼☼☼☼☼\n" +
                "☼   ☼\n" +
                "☼ Я ☼\n" +
                "☼ ##☼\n" +
                "☼☼☼☼☼\n");

        game.tick();
        game.tick();
        game.tick();
        game.tick();
        game.tick();
        game.tick();

        assertE("☼☼☼☼☼\n" +
                "☼   ☼\n" +
                "☼ Я ☼\n" +
                "☼4##☼\n" +
                "☼☼☼☼☼\n");

        game.tick();

        assertE("☼☼☼☼☼\n" +
                "☼   ☼\n" +
                "☼ Я ☼\n" +
                "☼3##☼\n" +
                "☼☼☼☼☼\n");

        game.tick();

        assertE("☼☼☼☼☼\n" +
                "☼   ☼\n" +
                "☼ Я ☼\n" +
                "☼2##☼\n" +
                "☼☼☼☼☼\n");

        game.tick();

        assertE("☼☼☼☼☼\n" +
                "☼   ☼\n" +
                "☼ Я ☼\n" +
                "☼1##☼\n" +
                "☼☼☼☼☼\n");

        game.tick();

        assertE("☼☼☼☼☼\n" +
                "☼   ☼\n" +
                "☼ Я ☼\n" +
                "☼###☼\n" +
                "☼☼☼☼☼\n");
    }

    // В просверленную яму я легко могу упасть
    @Test
    public void shouldFallInPitLeft() {
        shouldDrillLeft();

        assertE("☼☼☼☼☼\n" +
                "☼   ☼\n" +
                "☼ Я ☼\n" +
                "☼ ##☼\n" +
                "☼☼☼☼☼\n");

        hero.left();
        game.tick();

        assertE("☼☼☼☼☼\n" +
                "☼   ☼\n" +
                "☼]  ☼\n" +
                "☼ ##☼\n" +
                "☼☼☼☼☼\n");

        game.tick();

        assertE("☼☼☼☼☼\n" +
                "☼   ☼\n" +
                "☼   ☼\n" +
                "☼◄##☼\n" +
                "☼☼☼☼☼\n");
    }

    @Test
    public void shouldFallInPitRight() {
        shouldDrillRight();

        assertE("☼☼☼☼☼\n" +
                "☼   ☼\n" +
                "☼ R ☼\n" +
                "☼## ☼\n" +
                "☼☼☼☼☼\n");

        hero.right();
        game.tick();

        assertE("☼☼☼☼☼\n" +
                "☼   ☼\n" +
                "☼  [☼\n" +
                "☼## ☼\n" +
                "☼☼☼☼☼\n");

        game.tick();

        assertE("☼☼☼☼☼\n" +
                "☼   ☼\n" +
                "☼   ☼\n" +
                "☼##►☼\n" +
                "☼☼☼☼☼\n");
    }

    // я если упал то не могу передвигаться влево и вправо поскольку мне мешают стены
    @Test
    public void shouldCantGoLeftIfWall() {
        shouldFallInPitRight();

        hero.left();
        game.tick();

        assertE("☼☼☼☼☼\n" +
                "☼   ☼\n" +
                "☼   ☼\n" +
                "☼##◄☼\n" +
                "☼☼☼☼☼\n");
    }

    @Test
    public void shouldCantGoRightIfWall() {
        shouldFallInPitLeft();

        hero.right();
        game.tick();

        assertE("☼☼☼☼☼\n" +
                "☼   ☼\n" +
                "☼   ☼\n" +
                "☼►##☼\n" +
                "☼☼☼☼☼\n");
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

        assertE("☼☼☼☼☼\n" +
                "☼   ☼\n" +
                "☼.Я ☼\n" +
                "☼*##☼\n" +
                "☼☼☼☼☼\n");

        hero.right();
        game.tick();

        assertE("☼☼☼☼☼\n" +
                "☼   ☼\n" +
                "☼  ►☼\n" +
                "☼ ##☼\n" +
                "☼☼☼☼☼\n");

        hero.left();
        hero.act();
        game.tick();

        assertE("☼☼☼☼☼\n" +
                "☼   ☼\n" +
                "☼ .Я☼\n" +
                "☼ *#☼\n" +
                "☼☼☼☼☼\n");

        hero.left();
        game.tick();

        assertE("☼☼☼☼☼\n" +
                "☼   ☼\n" +
                "☼ ] ☼\n" +
                "☼  #☼\n" +
                "☼☼☼☼☼\n");

        hero.left();  // при падении я не могу передвигаться
        game.tick();

        assertE("☼☼☼☼☼\n" +
                "☼   ☼\n" +
                "☼   ☼\n" +
                "☼ ◄#☼\n" +
                "☼☼☼☼☼\n");

        hero.left(); // а вот в яме куда угодно, пока есть место
        game.tick();

        assertE("☼☼☼☼☼\n" +
                "☼   ☼\n" +
                "☼   ☼\n" +
                "☼◄ #☼\n" +
                "☼☼☼☼☼\n");

        hero.right(); // пока стенки не заростут
        game.tick();

        assertE("☼☼☼☼☼\n" +
                "☼   ☼\n" +
                "☼   ☼\n" +
                "☼ ►#☼\n" +
                "☼☼☼☼☼\n");

        hero.right();
        game.tick();
        game.tick();

        assertE("☼☼☼☼☼\n" +
                "☼   ☼\n" +
                "☼   ☼\n" +
                "☼4►#☼\n" +
                "☼☼☼☼☼\n");

        hero.left();
        game.tick();

        assertE("☼☼☼☼☼\n" +
                "☼   ☼\n" +
                "☼   ☼\n" +
                "☼◄ #☼\n" +
                "☼☼☼☼☼\n");

        hero.right();
        game.tick();

        assertE("☼☼☼☼☼\n" +
                "☼   ☼\n" +
                "☼   ☼\n" +
                "☼2►#☼\n" +
                "☼☼☼☼☼\n");

        hero.left();
        game.tick();

        assertE("☼☼☼☼☼\n" +
                "☼   ☼\n" +
                "☼   ☼\n" +
                "☼◄3#☼\n" +
                "☼☼☼☼☼\n");

        hero.right();
        game.tick();

        assertE("☼☼☼☼☼\n" +
                "☼   ☼\n" +
                "☼   ☼\n" +
                "☼#►#☼\n" +
                "☼☼☼☼☼\n");

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

        assertE("☼☼☼☼☼\n" +
                "☼   ☼\n" +
                "☼ Я ☼\n" +
                "☼2##☼\n" +
                "☼☼☼☼☼\n");

        hero.left();
        game.tick();

        assertE("☼☼☼☼☼\n" +
                "☼   ☼\n" +
                "☼]  ☼\n" +
                "☼1##☼\n" +
                "☼☼☼☼☼\n");

        game.tick();

        assertE("☼☼☼☼☼\n" +
                "☼   ☼\n" +
                "☼   ☼\n" +
                "☼Ѡ##☼\n" +
                "☼☼☼☼☼\n");

        dice(2, 3);
        game.tick();         // ну а после смерти он появляется в рендомном месте

        assertE("☼☼☼☼☼\n" +
                "☼ ] ☼\n" +
                "☼   ☼\n" +
                "☼###☼\n" +
                "☼☼☼☼☼\n");

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

        assertE("☼☼☼☼☼\n" +
                "☼   ☼\n" +
                "☼ Я ☼\n" +
                "☼3##☼\n" +
                "☼☼☼☼☼\n");

        hero.left();
        game.tick();

        assertE("☼☼☼☼☼\n" +
                "☼   ☼\n" +
                "☼]  ☼\n" +
                "☼2##☼\n" +
                "☼☼☼☼☼\n");

        game.tick();

        assertE("☼☼☼☼☼\n" +
                "☼   ☼\n" +
                "☼   ☼\n" +
                "☼◄##☼\n" +
                "☼☼☼☼☼\n");

        game.tick();

        assertE("☼☼☼☼☼\n" +
                "☼   ☼\n" +
                "☼   ☼\n" +
                "☼Ѡ##☼\n" +
                "☼☼☼☼☼\n");

        dice(2, 3);
        game.tick();         // ну а после смерти он появляется в рендомном месте

        assertE("☼☼☼☼☼\n" +
                "☼ ] ☼\n" +
                "☼   ☼\n" +
                "☼###☼\n" +
                "☼☼☼☼☼\n");

    }

    // я не могу сверлить уже отсверленную дырку, я даже не делаю вид что пытался
    @Test
    public void shouldCantDrillPit() {
        shouldDrillLeft();

        hero.right();
        game.tick();
        hero.left();
        game.tick();

        assertE("☼☼☼☼☼\n" +
                "☼   ☼\n" +
                "☼ ◄ ☼\n" +
                "☼ ##☼\n" +
                "☼☼☼☼☼\n");

        hero.left();
        hero.act();
        game.tick();

        assertE("☼☼☼☼☼\n" +
                "☼   ☼\n" +
                "☼ ◄ ☼\n" +
                "☼ ##☼\n" +
                "☼☼☼☼☼\n");
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
    public void shouldDrillRight_onyActCommand() {
        givenFl("☼☼☼☼☼" +
                "☼   ☼" +
                "☼ ► ☼" +
                "☼###☼" +
                "☼☼☼☼☼");

        hero.act();
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

        assertE("☼☼☼☼☼\n" +
                "☼ $ ☼\n" +
                "☼  ►☼\n" +
                "☼###☼\n" +
                "☼☼☼☼☼\n");

        hero.left();
        game.tick();

        assertE("☼☼☼☼☼\n" +
                "☼ $ ☼\n" +
                "☼ ◄ ☼\n" +
                "☼###☼\n" +
                "☼☼☼☼☼\n");
    }

    // проверить, что если новому обекту не где появится то программа не зависает - там бесконечный цикл потенциальный есть
    @Test(timeout = 1000)
    public void shouldNoDeadLoopWhenNewObjectCreation() {
        givenFl("☼☼☼☼☼" +
                "☼   ☼" +
                "☼ ►$☼" +
                "☼###☼" +
                "☼☼☼☼☼");

        dice(4, 4);
        hero.right();
        game.tick();

        assertE("☼☼☼☼☼\n" +
                "☼   ☼\n" +
                "☼  ►☼\n" +
                "☼###☼\n" +
                "$☼☼☼☼\n");
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

        assertE("☼☼☼☼☼\n" +
                "☼  H☼\n" +
                "☼  H☼\n" +
                "☼  Y☼\n" +
                "☼☼☼☼☼\n");

        hero.left();
        game.tick();

        assertE("☼☼☼☼☼\n" +
                "☼  H☼\n" +
                "☼  H☼\n" +
                "☼ ◄H☼\n" +
                "☼☼☼☼☼\n");
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

        assertE("☼☼☼☼☼\n" +
                "☼  H☼\n" +
                "☼  H☼\n" +
                "☼  Y☼\n" +
                "☼☼☼☼☼\n");

        hero.up();
        game.tick();

        assertE("☼☼☼☼☼\n" +
                "☼  H☼\n" +
                "☼  Y☼\n" +
                "☼  H☼\n" +
                "☼☼☼☼☼\n");

        hero.up();
        game.tick();

        assertE("☼☼☼☼☼\n" +
                "☼  Y☼\n" +
                "☼  H☼\n" +
                "☼  H☼\n" +
                "☼☼☼☼☼\n");
    }

    // я не могу вылезти с лестницей за границы
    @Test
    public void shouldICantGoOnBarrierFromLadder() {
        shouldICanGoOnLadderUp();

        assertE("☼☼☼☼☼\n" +
                "☼  Y☼\n" +
                "☼  H☼\n" +
                "☼  H☼\n" +
                "☼☼☼☼☼\n");

        hero.up();
        game.tick();

        assertE("☼☼☼☼☼\n" +
                "☼  Y☼\n" +
                "☼  H☼\n" +
                "☼  H☼\n" +
                "☼☼☼☼☼\n");

        hero.right();
        game.tick();

        assertE("☼☼☼☼☼\n" +
                "☼  Y☼\n" +
                "☼  H☼\n" +
                "☼  H☼\n" +
                "☼☼☼☼☼\n");
    }

    // я могу спустится вниз, но не дальше границы экрана
    @Test
    public void shouldICanGoOnLadderDown() {
        shouldICanGoOnLadderUp();

        assertE("☼☼☼☼☼\n" +
                "☼  Y☼\n" +
                "☼  H☼\n" +
                "☼  H☼\n" +
                "☼☼☼☼☼\n");

        hero.down();
        game.tick();

        assertE("☼☼☼☼☼\n" +
                "☼  H☼\n" +
                "☼  Y☼\n" +
                "☼  H☼\n" +
                "☼☼☼☼☼\n");

        hero.down();
        game.tick();

        assertE("☼☼☼☼☼\n" +
                "☼  H☼\n" +
                "☼  H☼\n" +
                "☼  Y☼\n" +
                "☼☼☼☼☼\n");

        hero.down();
        game.tick();

        assertE("☼☼☼☼☼\n" +
                "☼  H☼\n" +
                "☼  H☼\n" +
                "☼  Y☼\n" +
                "☼☼☼☼☼\n");
    }

    // я мошгу в любой момент спрыгнуть с лестницы и я буду падать до тех пор пока не наткнусь на препятствие
    @Test
    public void shouldICanFly() {
        shouldICanGoOnLadderUp();

        assertE("☼☼☼☼☼\n" +
                "☼  Y☼\n" +
                "☼  H☼\n" +
                "☼  H☼\n" +
                "☼☼☼☼☼\n");

        hero.left();
        game.tick();

        assertE("☼☼☼☼☼\n" +
                "☼ ]H☼\n" +
                "☼  H☼\n" +
                "☼  H☼\n" +
                "☼☼☼☼☼\n");

        game.tick();

        assertE("☼☼☼☼☼\n" +
                "☼  H☼\n" +
                "☼ ]H☼\n" +
                "☼  H☼\n" +
                "☼☼☼☼☼\n");

        game.tick();

        assertE("☼☼☼☼☼\n" +
                "☼  H☼\n" +
                "☼  H☼\n" +
                "☼ ◄H☼\n" +
                "☼☼☼☼☼\n");
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

        assertE("☼☼☼☼☼\n" +
                "☼  H☼\n" +
                "☼ ►H☼\n" +
                "☼###☼\n" +
                "☼☼☼☼☼\n");
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

        assertE("☼☼☼☼☼\n" +
                "☼   ☼\n" +
                "☼ ►$☼\n" +
                "☼###☼\n" +
                "☼☼☼☼☼\n");
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

        assertE("☼☼☼☼☼\n" +
                "☼H  ☼\n" +
                "☼H# ☼\n" +
                "☼Y  ☼\n" +
                "☼☼☼☼☼\n");

        hero.up();
        game.tick();

        assertE("☼☼☼☼☼\n" +
                "☼H  ☼\n" +
                "☼Y# ☼\n" +
                "☼H  ☼\n" +
                "☼☼☼☼☼\n");

        hero.up();
        game.tick();

        assertE("☼☼☼☼☼\n" +
                "☼Y  ☼\n" +
                "☼H# ☼\n" +
                "☼H  ☼\n" +
                "☼☼☼☼☼\n");

        hero.right();
        game.tick();

        assertE("☼☼☼☼☼\n" +
                "☼H► ☼\n" +
                "☼H# ☼\n" +
                "☼H  ☼\n" +
                "☼☼☼☼☼\n");

        // и упасть
        hero.right();
        game.tick();

        assertE("☼☼☼☼☼\n" +
                "☼H [☼\n" +
                "☼H# ☼\n" +
                "☼H  ☼\n" +
                "☼☼☼☼☼\n");

        game.tick();

        assertE("☼☼☼☼☼\n" +
                "☼H  ☼\n" +
                "☼H#[☼\n" +
                "☼H  ☼\n" +
                "☼☼☼☼☼\n");

        game.tick();

        assertE("☼☼☼☼☼\n" +
                "☼H  ☼\n" +
                "☼H# ☼\n" +
                "☼H ►☼\n" +
                "☼☼☼☼☼\n");

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

        assertE("☼☼☼☼☼\n" +
                "☼   ☼\n" +
                "☼ ► ☼\n" +
                "☼☼☼☼☼\n" +
                "☼☼☼☼☼\n");
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

        assertE("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼  ]  ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼## ##☼\n" +
                "☼☼☼☼☼☼☼\n");

        hero.right();
        game.tick();

        assertE("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼  [  ☼\n" +
                "☼     ☼\n" +
                "☼## ##☼\n" +
                "☼☼☼☼☼☼☼\n");

        hero.left();
        game.tick();

        assertE("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼  ]  ☼\n" +
                "☼## ##☼\n" +
                "☼☼☼☼☼☼☼\n");

        hero.left();
        game.tick();

        assertE("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼##◄##☼\n" +
                "☼☼☼☼☼☼☼\n");
    }

    // появляются на карте трубы, если я с площадки заходу на трубу то я ползу по ней
    // с трубы я могу спрыгунть и тогда я буду падать до препятствия
    // если по дороге я встречаюсь с золотом, то я его захвачу
    // появляются монстры - они за мной гонятся
    // монстр может похитить 1 золото
    // если монстр проваливается в ямку, которую я засверлил, и у него было золото - оно остается на поверхности
    // я могу ходить по монстру, который в ямке
    // монстр сидит в ямке некоторое количество тиков, потом он вылазит
    // если монстр не успел вылезти из ямки и она заросла то монстр умирает
    // когда монстр умирает, то на карте появляется новый
    // если я просверлил дырку и падаю в нее, а под ней ничего нет - то я падаю пока не найду препятствие
    // если в процессе падения я вдург наткнулся на трубу то я повисаю на ней
    // я не могу просверлить дырку непосредственно под монстром
    // появляется другой игрок, игра становится мультипользовательской
    // карта намного больше, чем квардартик вьюшка, и я подходя к границе просто передвигаю вьюшку
    // повляется многопользовательский режим игры в формате "стенка на стенку"
    //


    private void givenFl(String board) {
        game = new Loderunner(new LevelImpl(board), dice);
        hero = game.getHero();
    }

    private void assertE(String expected) {
        assertEquals(expected, game.getBoardAsString());
    }

}
