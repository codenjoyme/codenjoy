package com.codenjoy.dojo.loderunner.model;

import org.junit.Before;
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
                "☼◄  ☼\n" +
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
                "☼  ►☼\n" +
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
    // я если упал, то могу перемещаться влево и вправо, если мне не мешают стену
    // если я повернут в какую-то сторону и просто нажимаю сверлить то будет с той стороны дырка
    // если стенка замуровывается вместе со мной, то я умираю и появляюсь в рендомном месте
    // на карте появляется золото, если я его беру то получаю +
    // на карте появляются лестницы, я могу по ним карабкаться вверх и вниз
    // я мошгу в любой момент спрыгнуть с лестницы и я буду падать до тех пор пока не наткнусь на препятствие
    // я могу поднятся по лестнице и зайти на площадку
    // я не могу сверлить бетон
    // появляются на карте трубы, если я с площадки заходу на трубу то я ползу по ней
    // с трубы я могу спрыгунть и тогда я буду падать до препятствия
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
        game = new Loderunner(new LevelImpl(board));
        hero = game.getHero();
    }

    private void assertE(String expected) {
        assertEquals(expected, game.getBoardAsString());
    }

}
