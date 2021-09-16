package com.codenjoy.dojo.clifford.game;

/*-
 * #%L
 * Codenjoy - it's a dojo-like platform from developers to developers.
 * %%
 * Copyright (C) 2018 Codenjoy
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/gpl-3.0.html>.
 * #L%
 */


import com.codenjoy.dojo.clifford.model.items.Brick;
import com.codenjoy.dojo.clifford.model.items.Potion;
import com.codenjoy.dojo.services.Point;
import org.junit.Test;
import org.mockito.Mockito;

import static com.codenjoy.dojo.clifford.services.GameSettings.Keys.*;
import static com.codenjoy.dojo.services.PointImpl.pt;
import static org.junit.Assert.assertEquals;

public class GameTest extends AbstractGameTest {

    // есть карта со мной
    @Test
    public void shouldFieldAtStart() {
        givenFl("☼☼☼☼☼" +
                "☼   ☼" +
                "☼ ◄ ☼" +
                "☼###☼" +
                "☼☼☼☼☼");

        assertE("☼☼☼☼☼" +
                "☼   ☼" +
                "☼ ◄ ☼" +
                "☼###☼" +
                "☼☼☼☼☼");
    }

    // я могу простреливать дырки
    @Test
    public void shouldCrackLeft() {
        givenFl("☼☼☼☼☼" +
                "☼   ☼" +
                "☼ ◄ ☼" +
                "☼###☼" +
                "☼☼☼☼☼");

        hero().act();
        hero().left();
        tick();

        assertE("☼☼☼☼☼" +
                "☼   ☼" +
                "☼ Я ☼" +
                "☼*##☼" +
                "☼☼☼☼☼");

        tick();

        assertE("☼☼☼☼☼" +
                "☼   ☼" +
                "☼ Я ☼" +
                "☼ ##☼" +
                "☼☼☼☼☼");
    }

    @Test
    public void shouldCrackCounter() {
        shouldCrackLeft();

        tick();
        tick();
        tick();
        tick();
        tick();
        tick();

        assertE("☼☼☼☼☼" +
                "☼   ☼" +
                "☼ Я ☼" +
                "☼ ##☼" +
                "☼☼☼☼☼");

        tick();

        assertE("☼☼☼☼☼" +
                "☼   ☼" +
                "☼ Я ☼" +
                "☼4##☼" +
                "☼☼☼☼☼");

        tick();

        assertE("☼☼☼☼☼" +
                "☼   ☼" +
                "☼ Я ☼" +
                "☼3##☼" +
                "☼☼☼☼☼");

        tick();

        assertE("☼☼☼☼☼" +
                "☼   ☼" +
                "☼ Я ☼" +
                "☼2##☼" +
                "☼☼☼☼☼");

        tick();

        assertE("☼☼☼☼☼" +
                "☼   ☼" +
                "☼ Я ☼" +
                "☼1##☼" +
                "☼☼☼☼☼");

        tick();

        assertE("☼☼☼☼☼" +
                "☼   ☼" +
                "☼ Я ☼" +
                "☼###☼" +
                "☼☼☼☼☼");
    }

    @Test
    public void shouldCrackRight() {
        givenFl("☼☼☼☼☼" +
                "☼   ☼" +
                "☼ ◄ ☼" +
                "☼###☼" +
                "☼☼☼☼☼");

        hero().act();
        hero().right();
        tick();

        assertE("☼☼☼☼☼" +
                "☼   ☼" +
                "☼ R ☼" +
                "☼##*☼" +
                "☼☼☼☼☼");

        tick();

        assertE("☼☼☼☼☼" +
                "☼   ☼" +
                "☼ R ☼" +
                "☼## ☼" +
                "☼☼☼☼☼");
    }

    // я могу ходить влево и вправо
    @Test
    public void shouldMoveLeft() {
        givenFl("☼☼☼☼☼" +
                "☼   ☼" +
                "☼ ◄ ☼" +
                "☼###☼" +
                "☼☼☼☼☼");

        hero().left();
        tick();

        assertE("☼☼☼☼☼" +
                "☼   ☼" +
                "☼◄  ☼" +
                "☼###☼" +
                "☼☼☼☼☼");
    }

    @Test
    public void shouldMoveRight() {
        givenFl("☼☼☼☼☼" +
                "☼   ☼" +
                "☼ ◄ ☼" +
                "☼###☼" +
                "☼☼☼☼☼");

        hero().right();
        tick();

        assertE("☼☼☼☼☼" +
                "☼   ☼" +
                "☼  ►☼" +
                "☼###☼" +
                "☼☼☼☼☼");
    }

    // если небыло команды я никуда не иду
    @Test
    public void shouldStopWhenNoMoreRightCommand() {
        shouldMoveRight();

        tick();

        assertE("☼☼☼☼☼" +
                "☼   ☼" +
                "☼  ►☼" +
                "☼###☼" +
                "☼☼☼☼☼");
    }

    // я останавливаюсь возле границы
    @Test
    public void shouldStopWhenWallRight() {
        shouldMoveRight();

        hero().right();
        tick();

        assertE("☼☼☼☼☼" +
                "☼   ☼" +
                "☼  ►☼" +
                "☼###☼" +
                "☼☼☼☼☼");
    }

    @Test
    public void shouldStopWhenWallLeft() {
        shouldMoveLeft();

        hero().left();
        tick();

        assertE("☼☼☼☼☼" +
                "☼   ☼" +
                "☼◄  ☼" +
                "☼###☼" +
                "☼☼☼☼☼");
    }

    // яма заростает со временем
    @Test
    public void shouldCrackFilled() {
        shouldCrackLeft();

        tick();

        assertE("☼☼☼☼☼" +
                "☼   ☼" +
                "☼ Я ☼" +
                "☼ ##☼" +
                "☼☼☼☼☼");

        tick();
        tick();
        tick();
        tick();
        tick();
        tick();

        assertE("☼☼☼☼☼" +
                "☼   ☼" +
                "☼ Я ☼" +
                "☼4##☼" +
                "☼☼☼☼☼");

        tick();

        assertE("☼☼☼☼☼" +
                "☼   ☼" +
                "☼ Я ☼" +
                "☼3##☼" +
                "☼☼☼☼☼");

        tick();

        assertE("☼☼☼☼☼" +
                "☼   ☼" +
                "☼ Я ☼" +
                "☼2##☼" +
                "☼☼☼☼☼");

        tick();

        assertE("☼☼☼☼☼" +
                "☼   ☼" +
                "☼ Я ☼" +
                "☼1##☼" +
                "☼☼☼☼☼");

        tick();

        assertE("☼☼☼☼☼" +
                "☼   ☼" +
                "☼ Я ☼" +
                "☼###☼" +
                "☼☼☼☼☼");
    }

    // В простреливаю яму я легко могу упасть
    @Test
    public void shouldFallInPitLeft() {
        shouldCrackLeft();

        assertE("☼☼☼☼☼" +
                "☼   ☼" +
                "☼ Я ☼" +
                "☼ ##☼" +
                "☼☼☼☼☼");

        hero().left();
        tick();

        assertE("☼☼☼☼☼" +
                "☼   ☼" +
                "☼]  ☼" +
                "☼ ##☼" +
                "☼☼☼☼☼");

        tick();

        assertE("☼☼☼☼☼" +
                "☼   ☼" +
                "☼   ☼" +
                "☼◄##☼" +
                "☼☼☼☼☼");
    }

    @Test
    public void shouldFallInPitRight() {
        shouldCrackRight();

        assertE("☼☼☼☼☼" +
                "☼   ☼" +
                "☼ R ☼" +
                "☼## ☼" +
                "☼☼☼☼☼");

        hero().right();
        tick();

        assertE("☼☼☼☼☼" +
                "☼   ☼" +
                "☼  [☼" +
                "☼## ☼" +
                "☼☼☼☼☼");

        tick();

        assertE("☼☼☼☼☼" +
                "☼   ☼" +
                "☼   ☼" +
                "☼##►☼" +
                "☼☼☼☼☼");
    }

    // я если упал то не могу передвигаться влево и вправо поскольку мне мешают стены
    @Test
    public void shouldCantGoLeftIfWall() {
        shouldFallInPitRight();

        hero().left();
        tick();

        assertE("☼☼☼☼☼" +
                "☼   ☼" +
                "☼   ☼" +
                "☼##◄☼" +
                "☼☼☼☼☼");
    }

    @Test
    public void shouldCantGoRightIfWall() {
        shouldFallInPitLeft();

        hero().right();
        tick();

        assertE("☼☼☼☼☼" +
                "☼   ☼" +
                "☼   ☼" +
                "☼►##☼" +
                "☼☼☼☼☼");
    }

    // я если упал, то могу перемещаться влево и вправо, если мне не мешают стены
    @Test
    public void shouldСanGoInPitIfNoWall() {
        givenFl("☼☼☼☼☼" +
                "☼   ☼" +
                "☼ ◄ ☼" +
                "☼###☼" +
                "☼☼☼☼☼");

        hero().act();
        tick();

        assertE("☼☼☼☼☼" +
                "☼   ☼" +
                "☼ Я ☼" +
                "☼*##☼" +
                "☼☼☼☼☼");

        hero().right();
        tick();

        assertE("☼☼☼☼☼" +
                "☼   ☼" +
                "☼  ►☼" +
                "☼ ##☼" +
                "☼☼☼☼☼");

        hero().left();
        hero().act();
        tick();

        assertE("☼☼☼☼☼" +
                "☼   ☼" +
                "☼  Я☼" +
                "☼ *#☼" +
                "☼☼☼☼☼");

        hero().left();
        tick();

        assertE("☼☼☼☼☼" +
                "☼   ☼" +
                "☼ ] ☼" +
                "☼  #☼" +
                "☼☼☼☼☼");

        hero().left();  // при падении я не могу передвигаться
        tick();

        assertE("☼☼☼☼☼" +
                "☼   ☼" +
                "☼   ☼" +
                "☼ ◄#☼" +
                "☼☼☼☼☼");

        hero().left(); // а вот в яме куда угодно, пока есть место
        tick();

        assertE("☼☼☼☼☼" +
                "☼   ☼" +
                "☼   ☼" +
                "☼◄ #☼" +
                "☼☼☼☼☼");

        hero().right(); // пока стенки не заростут
        tick();

        assertE("☼☼☼☼☼" +
                "☼   ☼" +
                "☼   ☼" +
                "☼ ►#☼" +
                "☼☼☼☼☼");

        hero().right();
        tick();
        tick();

        assertE("☼☼☼☼☼" +
                "☼   ☼" +
                "☼   ☼" +
                "☼4►#☼" +
                "☼☼☼☼☼");

        hero().left();
        tick();

        assertE("☼☼☼☼☼" +
                "☼   ☼" +
                "☼   ☼" +
                "☼◄ #☼" +
                "☼☼☼☼☼");

        hero().right();
        tick();

        assertE("☼☼☼☼☼" +
                "☼   ☼" +
                "☼   ☼" +
                "☼2►#☼" +
                "☼☼☼☼☼");

        hero().left();
        tick();

        assertE("☼☼☼☼☼" +
                "☼   ☼" +
                "☼   ☼" +
                "☼◄3#☼" +
                "☼☼☼☼☼");

        hero().right();
        tick();

        assertE("☼☼☼☼☼" +
                "☼   ☼" +
                "☼   ☼" +
                "☼#►#☼" +
                "☼☼☼☼☼");

    }

    // если стенка замуровывается вместе со мной, то я умираю и появляюсь в рендомном месте
    @Test
    public void shouldIDieIPitFillWithMe() {
        shouldCrackLeft();

        tick();
        tick();
        tick();
        tick();
        tick();
        tick();
        tick();
        tick();
        tick();

        assertE("☼☼☼☼☼" +
                "☼   ☼" +
                "☼ Я ☼" +
                "☼2##☼" +
                "☼☼☼☼☼");

        hero().left();
        tick();

        assertE("☼☼☼☼☼" +
                "☼   ☼" +
                "☼]  ☼" +
                "☼1##☼" +
                "☼☼☼☼☼");

        tick();

        assertE("☼☼☼☼☼" +
                "☼   ☼" +
                "☼   ☼" +
                "☼Ѡ##☼" +
                "☼☼☼☼☼");

        events.verifyAllEvents("[KILL_HERO]");

        dice(2, 3);
        tick();         // ну а после смерти он появляется в рендомном месте
        field.newGame(player());

        assertE("☼☼☼☼☼" +
                "☼ [ ☼" +
                "☼   ☼" +
                "☼###☼" +
                "☼☼☼☼☼");
    }

    @Test
    public void shouldIDieIPitFillWithMe2() {
        shouldCrackLeft();

        tick();
        tick();
        tick();
        tick();
        tick();
        tick();
        tick();
        tick();

        assertE("☼☼☼☼☼" +
                "☼   ☼" +
                "☼ Я ☼" +
                "☼3##☼" +
                "☼☼☼☼☼");

        hero().left();
        tick();

        assertE("☼☼☼☼☼" +
                "☼   ☼" +
                "☼]  ☼" +
                "☼2##☼" +
                "☼☼☼☼☼");

        tick();

        assertE("☼☼☼☼☼" +
                "☼   ☼" +
                "☼   ☼" +
                "☼◄##☼" +
                "☼☼☼☼☼");

        tick();

        assertE("☼☼☼☼☼" +
                "☼   ☼" +
                "☼   ☼" +
                "☼Ѡ##☼" +
                "☼☼☼☼☼");

        events.verifyAllEvents("[KILL_HERO]");

        dice(2, 3);
        tick();         // ну а после смерти он появляется в рендомном месте
        field.newGame(player());

        assertE("☼☼☼☼☼" +
                "☼ [ ☼" +
                "☼   ☼" +
                "☼###☼" +
                "☼☼☼☼☼");

    }

    // я не могу простреливать уже прострелиную дырку, я даже не делаю вид что пытался
    @Test
    public void shouldCantCrackPit() {
        shouldCrackLeft();

        hero().right();
        tick();
        hero().left();
        tick();

        assertE("☼☼☼☼☼" +
                "☼   ☼" +
                "☼ ◄ ☼" +
                "☼ ##☼" +
                "☼☼☼☼☼");

        hero().left();
        hero().act();
        tick();

        assertE("☼☼☼☼☼" +
                "☼   ☼" +
                "☼ ◄ ☼" +
                "☼ ##☼" +
                "☼☼☼☼☼");
    }

    // выполнения команд left + act не зависят от порядка - если они сделаны в одном тике, то будет дырка слева без перемещения
    @Test
    public void shouldCrackLeft_otherCommandSequence() {
        givenFl("☼☼☼☼☼" +
                "☼   ☼" +
                "☼ ◄ ☼" +
                "☼###☼" +
                "☼☼☼☼☼");

//        hero().act();
        hero().left();
        hero().act();

        tick();

        assertE("☼☼☼☼☼" +
                "☼   ☼" +
                "☼ Я ☼" +
                "☼*##☼" +
                "☼☼☼☼☼");

        tick();

        assertE("☼☼☼☼☼" +
                "☼   ☼" +
                "☼ Я ☼" +
                "☼ ##☼" +
                "☼☼☼☼☼");
    }

    // если я повернут в какую-то сторону и просто нажимаю прострелить то будет с той стороны дырка
    @Test
    public void shouldCrackLeft_onyActCommand() {
        givenFl("☼☼☼☼☼" +
                "☼   ☼" +
                "☼ ◄ ☼" +
                "☼###☼" +
                "☼☼☼☼☼");

        hero().act();
        tick();

        assertE("☼☼☼☼☼" +
                "☼   ☼" +
                "☼ Я ☼" +
                "☼*##☼" +
                "☼☼☼☼☼");

        tick();

        assertE("☼☼☼☼☼" +
                "☼   ☼" +
                "☼ Я ☼" +
                "☼ ##☼" +
                "☼☼☼☼☼");
    }

    @Test
    public void shouldCrackRight_onyActCommand() {
        givenFl("☼☼☼☼☼" +
                "☼   ☼" +
                "☼ ► ☼" +
                "☼###☼" +
                "☼☼☼☼☼");

        hero().act();
        tick();

        assertE("☼☼☼☼☼" +
                "☼   ☼" +
                "☼ R ☼" +
                "☼##*☼" +
                "☼☼☼☼☼");

        tick();

        assertE("☼☼☼☼☼" +
                "☼   ☼" +
                "☼ R ☼" +
                "☼## ☼" +
                "☼☼☼☼☼");
    }

    // на карте появляется улика, если я его беру то получаю +
    @Test
    public void shouldClueOnMap_iCanGetIt() {
        givenFl("☼☼☼☼☼" +
                "☼   ☼" +
                "☼ ►$☼" +
                "☼###☼" +
                "☼☼☼☼☼");

        dice(2, 3);
        hero().right();
        tick();
        events.verifyAllEvents("[GET_KNIFE_CLUE]");

        assertE("☼☼☼☼☼" +
                "☼ $ ☼" +
                "☼  ►☼" +
                "☼###☼" +
                "☼☼☼☼☼");

        hero().left();
        tick();

        assertE("☼☼☼☼☼" +
                "☼ $ ☼" +
                "☼ ◄ ☼" +
                "☼###☼" +
                "☼☼☼☼☼");
    }

    // проверить, что если новому обекту не где появится то программа не зависает - там бесконечный цикл потенциальный есть
    @Test(timeout = 1000)
    public void shouldNoDeadLoopWhenNewObjectCreation() {
        givenFl("☼☼☼☼☼" +
                "☼   ☼" +
                "☼ ►$☼" +
                "☼###☼" +
                "☼☼☼☼☼");

        dice(3, 3);
        hero().right();
        tick();
        events.verifyAllEvents("[GET_KNIFE_CLUE]");

        assertE("☼☼☼☼☼" +
                "☼  $☼" +
                "☼  ►☼" +
                "☼###☼" +
                "☼☼☼☼☼");
    }

    // на карте появляются лестницы, я могу зайти на нее и выйти обратно
    @Test
    public void shouldICanGoOnLadder() {
        givenFl("☼☼☼☼☼" +
                "☼  H☼" +
                "☼  H☼" +
                "☼ ►H☼" +
                "☼☼☼☼☼");

        hero().right();
        tick();

        assertE("☼☼☼☼☼" +
                "☼  H☼" +
                "☼  H☼" +
                "☼  Y☼" +
                "☼☼☼☼☼");

        hero().left();
        tick();

        assertE("☼☼☼☼☼" +
                "☼  H☼" +
                "☼  H☼" +
                "☼ ◄H☼" +
                "☼☼☼☼☼");
    }

    // я могу карабкаться по лестнице вверх
    @Test
    public void shouldICanGoOnLadderUp() {
        givenFl("☼☼☼☼☼" +
                "☼  H☼" +
                "☼  H☼" +
                "☼ ►H☼" +
                "☼☼☼☼☼");

        hero().right();
        tick();

        assertE("☼☼☼☼☼" +
                "☼  H☼" +
                "☼  H☼" +
                "☼  Y☼" +
                "☼☼☼☼☼");

        hero().up();
        tick();

        assertE("☼☼☼☼☼" +
                "☼  H☼" +
                "☼  Y☼" +
                "☼  H☼" +
                "☼☼☼☼☼");

        hero().up();
        tick();

        assertE("☼☼☼☼☼" +
                "☼  Y☼" +
                "☼  H☼" +
                "☼  H☼" +
                "☼☼☼☼☼");
    }

    // я не могу вылезти с лестницей за границы
    @Test
    public void shouldICantGoOnBarrierFromLadder() {
        shouldICanGoOnLadderUp();

        assertE("☼☼☼☼☼" +
                "☼  Y☼" +
                "☼  H☼" +
                "☼  H☼" +
                "☼☼☼☼☼");

        hero().up();
        tick();

        assertE("☼☼☼☼☼" +
                "☼  Y☼" +
                "☼  H☼" +
                "☼  H☼" +
                "☼☼☼☼☼");

        hero().right();
        tick();

        assertE("☼☼☼☼☼" +
                "☼  Y☼" +
                "☼  H☼" +
                "☼  H☼" +
                "☼☼☼☼☼");
    }

    // я могу спустится вниз, но не дальше границы экрана
    @Test
    public void shouldICanGoOnLadderDown() {
        shouldICanGoOnLadderUp();

        assertE("☼☼☼☼☼" +
                "☼  Y☼" +
                "☼  H☼" +
                "☼  H☼" +
                "☼☼☼☼☼");

        hero().down();
        tick();

        assertE("☼☼☼☼☼" +
                "☼  H☼" +
                "☼  Y☼" +
                "☼  H☼" +
                "☼☼☼☼☼");

        hero().down();
        tick();

        assertE("☼☼☼☼☼" +
                "☼  H☼" +
                "☼  H☼" +
                "☼  Y☼" +
                "☼☼☼☼☼");

        hero().down();
        tick();

        assertE("☼☼☼☼☼" +
                "☼  H☼" +
                "☼  H☼" +
                "☼  Y☼" +
                "☼☼☼☼☼");
    }

    // я мошгу в любой момент спрыгнуть с лестницы и я буду падать до тех пор пока не наткнусь на препятствие
    @Test
    public void shouldICanFly() {
        shouldICanGoOnLadderUp();

        assertE("☼☼☼☼☼" +
                "☼  Y☼" +
                "☼  H☼" +
                "☼  H☼" +
                "☼☼☼☼☼");

        hero().left();
        tick();

        assertE("☼☼☼☼☼" +
                "☼ ]H☼" +
                "☼  H☼" +
                "☼  H☼" +
                "☼☼☼☼☼");

        tick();

        assertE("☼☼☼☼☼" +
                "☼  H☼" +
                "☼ ]H☼" +
                "☼  H☼" +
                "☼☼☼☼☼");

        tick();

        assertE("☼☼☼☼☼" +
                "☼  H☼" +
                "☼  H☼" +
                "☼ ◄H☼" +
                "☼☼☼☼☼");
    }

    // под стеной я не могу прострелить
    @Test
    public void shouldICantCrackUnderLadder() {
        givenFl("☼☼☼☼☼" +
                "☼  H☼" +
                "☼ ►H☼" +
                "☼###☼" +
                "☼☼☼☼☼");

        hero().act();
        tick();

        assertE("☼☼☼☼☼" +
                "☼  H☼" +
                "☼ ►H☼" +
                "☼###☼" +
                "☼☼☼☼☼");
    }

    // под уликой я не могу прострелить
    @Test
    public void shouldICantCrackUnderClue() {
        givenFl("☼☼☼☼☼" +
                "☼   ☼" +
                "☼ ►$☼" +
                "☼###☼" +
                "☼☼☼☼☼");

        hero().act();
        tick();

        assertE("☼☼☼☼☼" +
                "☼   ☼" +
                "☼ ►$☼" +
                "☼###☼" +
                "☼☼☼☼☼");
    }

    // я могу поднятся по лестнице и зайти на площадку
    @Test
    public void shouldICanGoFromLadderToArea() {
        givenFl("☼☼☼☼☼" +
                "☼H  ☼" +
                "☼H# ☼" +
                "☼H◄ ☼" +
                "☼☼☼☼☼");

        hero().left();
        tick();

        assertE("☼☼☼☼☼" +
                "☼H  ☼" +
                "☼H# ☼" +
                "☼Y  ☼" +
                "☼☼☼☼☼");

        hero().up();
        tick();

        assertE("☼☼☼☼☼" +
                "☼H  ☼" +
                "☼Y# ☼" +
                "☼H  ☼" +
                "☼☼☼☼☼");

        hero().up();
        tick();

        assertE("☼☼☼☼☼" +
                "☼Y  ☼" +
                "☼H# ☼" +
                "☼H  ☼" +
                "☼☼☼☼☼");

        hero().right();
        tick();

        assertE("☼☼☼☼☼" +
                "☼H► ☼" +
                "☼H# ☼" +
                "☼H  ☼" +
                "☼☼☼☼☼");

        // и упасть
        hero().right();
        tick();

        assertE("☼☼☼☼☼" +
                "☼H [☼" +
                "☼H# ☼" +
                "☼H  ☼" +
                "☼☼☼☼☼");

        tick();

        assertE("☼☼☼☼☼" +
                "☼H  ☼" +
                "☼H#[☼" +
                "☼H  ☼" +
                "☼☼☼☼☼");

        tick();

        assertE("☼☼☼☼☼" +
                "☼H  ☼" +
                "☼H# ☼" +
                "☼H ►☼" +
                "☼☼☼☼☼");

    }

    // я не могу прострелить бетон
    @Test
    public void shouldICantCrackWall() {
        givenFl("☼☼☼☼☼" +
                "☼   ☼" +
                "☼ ► ☼" +
                "☼☼☼☼☼" +
                "☼☼☼☼☼");

        hero().act();
        tick();

        assertE("☼☼☼☼☼" +
                "☼   ☼" +
                "☼ ► ☼" +
                "☼☼☼☼☼" +
                "☼☼☼☼☼");
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

        hero().left();
        tick();

        assertE("☼☼☼☼☼☼☼" +
                "☼     ☼" +
                "☼  ]  ☼" +
                "☼     ☼" +
                "☼     ☼" +
                "☼## ##☼" +
                "☼☼☼☼☼☼☼");

        hero().right();
        tick();

        assertE("☼☼☼☼☼☼☼" +
                "☼     ☼" +
                "☼     ☼" +
                "☼  [  ☼" +
                "☼     ☼" +
                "☼## ##☼" +
                "☼☼☼☼☼☼☼");

        hero().left();
        tick();

        assertE("☼☼☼☼☼☼☼" +
                "☼     ☼" +
                "☼     ☼" +
                "☼     ☼" +
                "☼  ]  ☼" +
                "☼## ##☼" +
                "☼☼☼☼☼☼☼");

        hero().left();
        tick();

        assertE("☼☼☼☼☼☼☼" +
                "☼     ☼" +
                "☼     ☼" +
                "☼     ☼" +
                "☼     ☼" +
                "☼##◄##☼" +
                "☼☼☼☼☼☼☼");
    }

    // появляются на карте трубы, если я с площадки захожу на трубу то я ползу по ней
    @Test
    public void shouldIPipe() {
        givenFl("☼☼☼☼☼☼☼" +
                "☼     ☼" +
                "☼►~~~ ☼" +
                "☼#    ☼" +
                "☼     ☼" +
                "☼     ☼" +
                "☼☼☼☼☼☼☼");

        hero().right();
        tick();

        assertE("☼☼☼☼☼☼☼" +
                "☼     ☼" +
                "☼ }~~ ☼" +
                "☼#    ☼" +
                "☼     ☼" +
                "☼     ☼" +
                "☼☼☼☼☼☼☼");

        hero().right();
        tick();

        assertE("☼☼☼☼☼☼☼" +
                "☼     ☼" +
                "☼ ~}~ ☼" +
                "☼#    ☼" +
                "☼     ☼" +
                "☼     ☼" +
                "☼☼☼☼☼☼☼");

        hero().right();
        tick();

        assertE("☼☼☼☼☼☼☼" +
                "☼     ☼" +
                "☼ ~~} ☼" +
                "☼#    ☼" +
                "☼     ☼" +
                "☼     ☼" +
                "☼☼☼☼☼☼☼");
    }

    // с трубы я могу спрыгунть и тогда я буду падать до препятствия
    @Test
    public void shouldIFallFromPipe() {
        shouldIPipe();

        assertE("☼☼☼☼☼☼☼" +
                "☼     ☼" +
                "☼ ~~} ☼" +
                "☼#    ☼" +
                "☼     ☼" +
                "☼     ☼" +
                "☼☼☼☼☼☼☼");

        hero().right();
        tick();

        assertE("☼☼☼☼☼☼☼" +
                "☼     ☼" +
                "☼ ~~~[☼" +
                "☼#    ☼" +
                "☼     ☼" +
                "☼     ☼" +
                "☼☼☼☼☼☼☼");

        tick();

        assertE("☼☼☼☼☼☼☼" +
                "☼     ☼" +
                "☼ ~~~ ☼" +
                "☼#   [☼" +
                "☼     ☼" +
                "☼     ☼" +
                "☼☼☼☼☼☼☼");

        tick();

        assertE("☼☼☼☼☼☼☼" +
                "☼     ☼" +
                "☼ ~~~ ☼" +
                "☼#    ☼" +
                "☼    [☼" +
                "☼     ☼" +
                "☼☼☼☼☼☼☼");

        tick();

        assertE("☼☼☼☼☼☼☼" +
                "☼     ☼" +
                "☼ ~~~ ☼" +
                "☼#    ☼" +
                "☼     ☼" +
                "☼    ►☼" +
                "☼☼☼☼☼☼☼");
    }

    // если по дороге я встречаюсь с уликой, то я его захвачу
    @Test
    public void shouldIGetClueWhenFallenFromPipe() {
        givenFl("☼☼☼☼☼☼☼" +
                "☼     ☼" +
                "☼ $~~◄☼" +
                "☼ $  #☼" +
                "☼ $   ☼" +
                "☼ $   ☼" +
                "☼☼☼☼☼☼☼");

        hero().left();
        tick();

        assertE("☼☼☼☼☼☼☼" +
                "☼     ☼" +
                "☼ $~{ ☼" +
                "☼ $  #☼" +
                "☼ $   ☼" +
                "☼ $   ☼" +
                "☼☼☼☼☼☼☼");

        hero().left();
        tick();

        assertE("☼☼☼☼☼☼☼" +
                "☼     ☼" +
                "☼ ${~ ☼" +
                "☼ $  #☼" +
                "☼ $   ☼" +
                "☼ $   ☼" +
                "☼☼☼☼☼☼☼");

        dice(1, 5);
        hero().left();
        tick();

        assertE("☼☼☼☼☼☼☼" +
                "☼$    ☼" +
                "☼ ]~~ ☼" +
                "☼ $  #☼" +
                "☼ $   ☼" +
                "☼ $   ☼" +
                "☼☼☼☼☼☼☼");

        events.verifyAllEvents("[GET_KNIFE_CLUE]");

        dice(2, 5);
        tick();

        assertE("☼☼☼☼☼☼☼" +
                "☼$$   ☼" +
                "☼  ~~ ☼" +
                "☼ ]  #☼" +
                "☼ $   ☼" +
                "☼ $   ☼" +
                "☼☼☼☼☼☼☼");

        events.verifyAllEvents("[GET_KNIFE_CLUE]");

        dice(3, 5);
        tick();

        assertE("☼☼☼☼☼☼☼" +
                "☼$$$  ☼" +
                "☼  ~~ ☼" +
                "☼    #☼" +
                "☼ ]   ☼" +
                "☼ $   ☼" +
                "☼☼☼☼☼☼☼");

        events.verifyAllEvents("[GET_KNIFE_CLUE]");

        dice(4, 5);
        tick();

        assertE("☼☼☼☼☼☼☼" +
                "☼$$$$ ☼" +
                "☼  ~~ ☼" +
                "☼    #☼" +
                "☼     ☼" +
                "☼ ◄   ☼" +
                "☼☼☼☼☼☼☼");

        events.verifyAllEvents("[GET_KNIFE_CLUE]");
    }

    // если я прострелил дырку и падаю в нее, а под ней ничего нет - то я падаю пока не найду препятствие
    @Test
    public void shouldIFallWhenUnderPitIsFree() {
        givenFl("☼☼☼☼☼☼☼" +
                "☼  ◄  ☼" +
                "☼#####☼" +
                "☼     ☼" +
                "☼     ☼" +
                "☼     ☼" +
                "☼☼☼☼☼☼☼");

        hero().act();
        tick();

        assertE("☼☼☼☼☼☼☼" +
                "☼  Я  ☼" +
                "☼#*###☼" +
                "☼     ☼" +
                "☼     ☼" +
                "☼     ☼" +
                "☼☼☼☼☼☼☼");

        hero().left();
        tick();

        assertE("☼☼☼☼☼☼☼" +
                "☼ ]   ☼" +
                "☼# ###☼" +
                "☼     ☼" +
                "☼     ☼" +
                "☼     ☼" +
                "☼☼☼☼☼☼☼");

        tick();

        assertE("☼☼☼☼☼☼☼" +
                "☼     ☼" +
                "☼#]###☼" +
                "☼     ☼" +
                "☼     ☼" +
                "☼     ☼" +
                "☼☼☼☼☼☼☼");

        tick();

        assertE("☼☼☼☼☼☼☼" +
                "☼     ☼" +
                "☼# ###☼" +
                "☼ ]   ☼" +
                "☼     ☼" +
                "☼     ☼" +
                "☼☼☼☼☼☼☼");

        tick();

        assertE("☼☼☼☼☼☼☼" +
                "☼     ☼" +
                "☼# ###☼" +
                "☼     ☼" +
                "☼ ]   ☼" +
                "☼     ☼" +
                "☼☼☼☼☼☼☼");

        tick();

        assertE("☼☼☼☼☼☼☼" +
                "☼     ☼" +
                "☼# ###☼" +
                "☼     ☼" +
                "☼     ☼" +
                "☼ ◄   ☼" +
                "☼☼☼☼☼☼☼");
    }

    // если в процессе падения я вдург наткнулся на трубу то я повисаю на ней
    @Test
    public void shouldIPipeWhenFall() {
        givenFl("☼☼☼☼☼☼☼" +
                "☼  ◄  ☼" +
                "☼# ###☼" +
                "☼     ☼" +
                "☼ ~~~ ☼" +
                "☼     ☼" +
                "☼☼☼☼☼☼☼");

        hero().left();
        tick();

        assertE("☼☼☼☼☼☼☼" +
                "☼ ]   ☼" +
                "☼# ###☼" +
                "☼     ☼" +
                "☼ ~~~ ☼" +
                "☼     ☼" +
                "☼☼☼☼☼☼☼");

        tick();

        assertE("☼☼☼☼☼☼☼" +
                "☼     ☼" +
                "☼#]###☼" +
                "☼     ☼" +
                "☼ ~~~ ☼" +
                "☼     ☼" +
                "☼☼☼☼☼☼☼");

        tick();

        assertE("☼☼☼☼☼☼☼" +
                "☼     ☼" +
                "☼# ###☼" +
                "☼ ]   ☼" +
                "☼ ~~~ ☼" +
                "☼     ☼" +
                "☼☼☼☼☼☼☼");

        tick();

        assertE("☼☼☼☼☼☼☼" +
                "☼     ☼" +
                "☼# ###☼" +
                "☼     ☼" +
                "☼ {~~ ☼" +
                "☼     ☼" +
                "☼☼☼☼☼☼☼");

        tick();

        assertE("☼☼☼☼☼☼☼" +
                "☼     ☼" +
                "☼# ###☼" +
                "☼     ☼" +
                "☼ {~~ ☼" +
                "☼     ☼" +
                "☼☼☼☼☼☼☼");
    }

    // TODO я могу прострелить дырку под лестницей, а потом спуститься туда

    // я не могу прострелить дырку под другим камнем
    @Test
    public void shouldICantCrackUnderBrick() {
        givenFl("☼☼☼☼☼" +
                "☼   ☼" +
                "☼ ►#☼" +
                "☼###☼" +
                "☼☼☼☼☼");

        hero().act();
        tick();

        assertE("☼☼☼☼☼" +
                "☼   ☼" +
                "☼ ►#☼" +
                "☼###☼" +
                "☼☼☼☼☼");
    }

    // я могу спрыгнуть с трубы
    @Test
    public void shouldCanJumpFromPipe() {
        givenFl("☼☼☼☼☼☼☼" +
                "☼  ◄  ☼" +
                "☼     ☼" +
                "☼ ~~~ ☼" +
                "☼     ☼" +
                "☼     ☼" +
                "☼☼☼☼☼☼☼");

        tick();

        assertE("☼☼☼☼☼☼☼" +
                "☼     ☼" +
                "☼  ]  ☼" +
                "☼ ~~~ ☼" +
                "☼     ☼" +
                "☼     ☼" +
                "☼☼☼☼☼☼☼");

        tick();

        assertE("☼☼☼☼☼☼☼" +
                "☼     ☼" +
                "☼     ☼" +
                "☼ ~{~ ☼" +
                "☼     ☼" +
                "☼     ☼" +
                "☼☼☼☼☼☼☼");

        hero().down();
        tick();

        assertE("☼☼☼☼☼☼☼" +
                "☼     ☼" +
                "☼     ☼" +
                "☼ ~~~ ☼" +
                "☼  ]  ☼" +
                "☼     ☼" +
                "☼☼☼☼☼☼☼");

        tick();

        assertE("☼☼☼☼☼☼☼" +
                "☼     ☼" +
                "☼     ☼" +
                "☼ ~~~ ☼" +
                "☼     ☼" +
                "☼  ◄  ☼" +
                "☼☼☼☼☼☼☼");
    }

    // бага: мне нельзя спускаться с лестницы в бетон, так же как и подниматься
    // плюс я должен иметь возможность спустится по лестнице
    @Test
    public void shouldCantWalkThroughWallDown() {
        givenFl("☼☼☼☼☼" +
                "☼ ◄ ☼" +
                "☼ H ☼" +
                "☼☼☼☼☼" +
                "☼☼☼☼☼");

        hero().down();
        tick();

        assertE("☼☼☼☼☼" +
                "☼   ☼" +
                "☼ Y ☼" +
                "☼☼☼☼☼" +
                "☼☼☼☼☼");

        hero().down();
        tick();

        assertE("☼☼☼☼☼" +
                "☼   ☼" +
                "☼ Y ☼" +
                "☼☼☼☼☼" +
                "☼☼☼☼☼");
    }

    @Test
    public void shouldCantWalkThroughWallUp() {
        givenFl("☼☼☼☼☼" +
                "☼☼☼☼☼" +
                "☼ H ☼" +
                "☼ H◄☼" +
                "☼☼☼☼☼");

        hero().left();
        tick();

        assertE("☼☼☼☼☼" +
                "☼☼☼☼☼" +
                "☼ H ☼" +
                "☼ Y ☼" +
                "☼☼☼☼☼");

        hero().up();
        tick();

        assertE("☼☼☼☼☼" +
                "☼☼☼☼☼" +
                "☼ Y ☼" +
                "☼ H ☼" +
                "☼☼☼☼☼");

        hero().up();
        tick();

        assertE("☼☼☼☼☼" +
                "☼☼☼☼☼" +
                "☼ Y ☼" +
                "☼ H ☼" +
                "☼☼☼☼☼");
    }

    // бага: мне нельзя проходить с лестницы через бетон направо или налево
    @Test
    public void shouldCantWalkThroughWallLeftRight() {
        givenFl("☼☼☼☼☼" +
                "☼☼☼☼☼" +
                "☼☼H☼☼" +
                "☼ H◄☼" +
                "☼☼☼☼☼");

        hero().left();
        tick();

        assertE("☼☼☼☼☼" +
                "☼☼☼☼☼" +
                "☼☼H☼☼" +
                "☼ Y ☼" +
                "☼☼☼☼☼");

        hero().up();
        tick();

        assertE("☼☼☼☼☼" +
                "☼☼☼☼☼" +
                "☼☼Y☼☼" +
                "☼ H ☼" +
                "☼☼☼☼☼");

        hero().left();
        tick();

        assertE("☼☼☼☼☼" +
                "☼☼☼☼☼" +
                "☼☼Y☼☼" +
                "☼ H ☼" +
                "☼☼☼☼☼");

        hero().right();
        tick();

        assertE("☼☼☼☼☼" +
                "☼☼☼☼☼" +
                "☼☼Y☼☼" +
                "☼ H ☼" +
                "☼☼☼☼☼");
    }

    // бага: мне нельзя проходить через бетон
    @Test
    public void shouldCantWalkThroughWallLeftRight2() {
        givenFl("☼☼☼☼☼" +
                "☼   ☼" +
                "☼   ☼" +
                "☼☼◄☼☼" +
                "☼☼☼☼☼");

        hero().left();
        tick();

        assertE("☼☼☼☼☼" +
                "☼   ☼" +
                "☼   ☼" +
                "☼☼◄☼☼" +
                "☼☼☼☼☼");

        hero().right();
        tick();

        assertE("☼☼☼☼☼" +
                "☼   ☼" +
                "☼   ☼" +
                "☼☼►☼☼" +
                "☼☼☼☼☼");
    }

    // бага: мне нельзя спрыгивать с трубы что сразу над бетоном, протелая сквозь него
    @Test
    public void shouldCantJumpThroughWall() {
        givenFl("☼☼☼☼☼☼" +
                "☼  ◄ ☼" +
                "☼  ~ ☼" +
                "☼☼☼☼☼☼" +
                "☼    ☼" +
                "☼☼☼☼☼☼");

        tick();

        assertE("☼☼☼☼☼☼" +
                "☼    ☼" +
                "☼  { ☼" +
                "☼☼☼☼☼☼" +
                "☼    ☼" +
                "☼☼☼☼☼☼");

        hero().down();
        tick();

        assertE("☼☼☼☼☼☼" +
                "☼    ☼" +
                "☼  { ☼" +
                "☼☼☼☼☼☼" +
                "☼    ☼" +
                "☼☼☼☼☼☼");
    }

    @Test
    public void shouldBoardIsFree() {
        givenFl("☼☼☼☼☼☼" +
                "☼~◄$H☼" +
                "☼####☼" +
                "☼☼☼☼☼☼" +
                "☼☼☼☼☼☼" +
                "☼☼☼☼☼☼");

        for (int x = 0; x < field.size(); x++) {
            for (int y = 0; y < field.size(); y++) {
                Point pt = pt(x, y);
                assertEquals("At:" + pt, false, field.isFree(pt));
            }

        }
    }

    // Есть хитрый хак, когда спрыгиваешь с трубы, то можно при падении простреливать под собой
    // но она многоптточная
    @Test
    public void shouldCrackDown() {
        givenFl("☼☼☼☼☼☼" +
                "☼  ◄ ☼" +
                "☼  ~ ☼" +
                "☼    ☼" +
                "☼####☼" +
                "☼☼☼☼☼☼");

        tick();

        assertE("☼☼☼☼☼☼" +
                "☼    ☼" +
                "☼  { ☼" +
                "☼    ☼" +
                "☼####☼" +
                "☼☼☼☼☼☼");

        hero().down();
        hero().act();
        tick();

        assertE("☼☼☼☼☼☼" +
                "☼    ☼" +
                "☼  { ☼" +
                "☼    ☼" +
                "☼####☼" +
                "☼☼☼☼☼☼");

        hero().down();
        tick();

        assertE("☼☼☼☼☼☼" +
                "☼    ☼" +
                "☼  ~ ☼" +
                "☼  ◄ ☼" +
                "☼####☼" +
                "☼☼☼☼☼☼");

        hero().act();
        tick();

        assertE("☼☼☼☼☼☼" +
                "☼    ☼" +
                "☼  ~ ☼" +
                "☼  Я ☼" +
                "☼#*##☼" +
                "☼☼☼☼☼☼");
    }

    // если я сам себя закопаю, то получу ли я за это очки? не должен!
    @Test
    public void shouldNoScoreWhenKamikadze() {
        givenFl("☼☼☼☼" +
                "☼ ◄☼" +
                "☼##☼" +
                "☼☼☼☼");

        hero().act();
        tick();
        hero().left();
        tick();
        tick();

        assertE("☼☼☼☼" +
                "☼  ☼" +
                "☼◄#☼" +
                "☼☼☼☼");

        for (int c = 3; c < Brick.CRACK_TIMER; c++) {
            tick();
        }

        assertE("☼☼☼☼" +
                "☼  ☼" +
                "☼Ѡ#☼" +
                "☼☼☼☼");

        tick();

        assertE("☼☼☼☼" +
                "☼  ☼" +
                "☼Ѡ#☼" +
                "☼☼☼☼");

        events.verifyAllEvents("[KILL_HERO]");
    }

    // я могу прострелить стенки под стенками, если те разрушены
    @Test
    public void shouldCrackUnderCrackedBrick() {
        givenFl("☼☼☼☼☼☼" +
                "☼    ☼" +
                "☼ ◄  ☼" +
                "☼####☼" +
                "☼####☼" +
                "☼☼☼☼☼☼");

        hero().act();
        tick();

        hero().right();
        tick();

        hero().left();
        hero().act();
        tick();

        hero().right();
        tick();

        hero().left();
        hero().act();
        tick();

        assertE("☼☼☼☼☼☼" +
                "☼    ☼" +
                "☼   Я☼" +
                "☼  *#☼" +
                "☼####☼" +
                "☼☼☼☼☼☼");

        hero().left();
        tick();
        tick();

        hero().left();
        tick();

        assertE("☼☼☼☼☼☼" +
                "☼    ☼" +
                "☼    ☼" +
                "☼ ◄ #☼" +
                "☼####☼" +
                "☼☼☼☼☼☼");

        hero().right();
        hero().act();
        tick();

        hero().left();
        hero().act();
        tick();

        tick();

        assertE("☼☼☼☼☼☼" +
                "☼    ☼" +
                "☼    ☼" +
                "☼2Я #☼" +
                "☼ # #☼" +
                "☼☼☼☼☼☼");

        hero().left();
        tick();

        assertE("☼☼☼☼☼☼" +
                "☼    ☼" +
                "☼    ☼" +
                "☼]3 #☼" +
                "☼ # #☼" +
                "☼☼☼☼☼☼");

        hero().left();
        tick();

        assertE("☼☼☼☼☼☼" +
                "☼    ☼" +
                "☼    ☼" +
                "☼#24#☼" +
                "☼◄# #☼" +
                "☼☼☼☼☼☼");
    }

    // если я спрыгива с последней секции лестницы, то как-то неудачно этто делаю. Бага!
    @Test
    public void shouldJumpFromLadderDown() {
        givenFl("☼☼☼☼☼☼" +
                "☼ ◄  ☼" +
                "☼ H##☼" +
                "☼#H  ☼" +
                "☼    ☼" +
                "☼☼☼☼☼☼");

        hero().down();
        tick();

        hero().down();
        tick();

        assertE("☼☼☼☼☼☼" +
                "☼    ☼" +
                "☼ H##☼" +
                "☼#Y  ☼" +
                "☼    ☼" +
                "☼☼☼☼☼☼");

        tick();

        assertE("☼☼☼☼☼☼" +
                "☼    ☼" +
                "☼ H##☼" +
                "☼#Y  ☼" +
                "☼    ☼" +
                "☼☼☼☼☼☼");

        hero().right();
        tick();

        assertE("☼☼☼☼☼☼" +
                "☼    ☼" +
                "☼ H##☼" +
                "☼#H[ ☼" +
                "☼    ☼" +
                "☼☼☼☼☼☼");

        tick();

        assertE("☼☼☼☼☼☼" +
                "☼    ☼" +
                "☼ H##☼" +
                "☼#H  ☼" +
                "☼  ► ☼" +
                "☼☼☼☼☼☼");
    }

    // чертик двигается так же как и обычный игрок - мжет ходить влево и вправо
    @Test
    public void shouldRobberMoveLeft() {
        givenFl("☼☼☼☼►" +
                "☼   ☼" +
                "☼ « ☼" +
                "☼###☼" +
                "☼☼☼☼☼");

        robber().left();
        tick();

        assertE("☼☼☼☼►" +
                "☼   ☼" +
                "☼«  ☼" +
                "☼###☼" +
                "☼☼☼☼☼");
    }

    @Test
    public void shouldRobberMoveRight() {
        givenFl("☼☼☼☼►" +
                "☼   ☼" +
                "☼ « ☼" +
                "☼###☼" +
                "☼☼☼☼☼");

        robber().right();
        tick();

        assertE("☼☼☼☼►" +
                "☼   ☼" +
                "☼  »☼" +
                "☼###☼" +
                "☼☼☼☼☼");
    }

    // если небыло команды чертик никуда не идет
    @Test
    public void shouldRobberStopWhenNoMoreRightCommand() {
        givenFl("☼☼☼☼►" +
                "☼   ☼" +
                "☼  «☼" +
                "☼###☼" +
                "☼☼☼☼☼");

        robber().left();
        tick();

        assertE("☼☼☼☼►" +
                "☼   ☼" +
                "☼ « ☼" +
                "☼###☼" +
                "☼☼☼☼☼");

        tick();

        assertE("☼☼☼☼►" +
                "☼   ☼" +
                "☼ « ☼" +
                "☼###☼" +
                "☼☼☼☼☼");
    }

    // Чертик останавливается возле границы
    @Test
    public void shouldRobberStopWhenWallRight() {
        givenFl("☼☼☼☼►" +
                "☼   ☼" +
                "☼  »☼" +
                "☼###☼" +
                "☼☼☼☼☼");

        robber().right();
        tick();

        assertE("☼☼☼☼►" +
                "☼   ☼" +
                "☼  »☼" +
                "☼###☼" +
                "☼☼☼☼☼");
    }

    @Test
    public void shouldRobberStopWhenWallLeft() {
        givenFl("☼☼☼☼►" +
                "☼   ☼" +
                "☼«  ☼" +
                "☼###☼" +
                "☼☼☼☼☼");

        robber().left();
        tick();

        assertE("☼☼☼☼►" +
                "☼   ☼" +
                "☼«  ☼" +
                "☼###☼" +
                "☼☼☼☼☼");
    }

    // В простреленную яму чертик легко может упасть
    @Test
    public void shouldRobberFallInPitLeft() {
        givenFl("☼☼☼☼☼" +
                "☼   ☼" +
                "☼« ◄☼" +
                "☼###☼" +
                "☼☼☼☼☼");

        hero().act();
        tick();

        robber().right();
        tick();

        assertE("☼☼☼☼☼" +
                "☼   ☼" +
                "☼ »Я☼" +
                "☼# #☼" +
                "☼☼☼☼☼");

        tick();

        assertE("☼☼☼☼☼" +
                "☼   ☼" +
                "☼  Я☼" +
                "☼#⍈#☼" +
                "☼☼☼☼☼");
    }

    @Test
    public void shouldRobberFallInPitRight() {
        givenFl("☼☼☼☼☼" +
                "☼   ☼" +
                "☼◄ «☼" +
                "☼###☼" +
                "☼☼☼☼☼");

        hero().right();
        hero().act();
        tick();

        assertE("☼☼☼☼☼" +
                "☼   ☼" +
                "☼R «☼" +
                "☼#*#☼" +
                "☼☼☼☼☼");

        robber().left();
        tick();

        assertE("☼☼☼☼☼" +
                "☼   ☼" +
                "☼R« ☼" +
                "☼# #☼" +
                "☼☼☼☼☼");

        tick();

        assertE("☼☼☼☼☼" +
                "☼   ☼" +
                "☼R  ☼" +
                "☼#⍇#☼" +
                "☼☼☼☼☼");
    }

    // при падении чертик не может передвигаться влево и вправо - ему мешают стены
    @Test
    public void shouldRobberCantGoLeftIfWall() {
        shouldRobberFallInPitRight();

        robber().left();
        tick();

        assertE("☼☼☼☼☼" +
                "☼   ☼" +
                "☼R  ☼" +
                "☼#⍇#☼" +
                "☼☼☼☼☼");
    }

    @Test
    public void shouldRobberCantGoRightIfWall() {
        shouldRobberFallInPitLeft();

        robber().right();
        tick();

        assertE("☼☼☼☼☼" +
                "☼   ☼" +
                "☼  Я☼" +
                "☼#⍈#☼" +
                "☼☼☼☼☼");
    }

    // монстр сидит в ямке некоторое количество тиков, потом он вылазит
    @Test
    public void shouldMonsterGetUpFromPit() {
        shouldRobberFallInPitLeft();

        tick();
        tick();
        tick();
        tick();
        tick();
        tick();
        tick();
        tick();
        tick();
        tick();

        assertE("☼☼☼☼☼" +
                "☼   ☼" +
                "☼  Я☼" +
                "☼#⍈#☼" +
                "☼☼☼☼☼");

        tick();

        assertE("☼☼☼☼☼" +
                "☼   ☼" +
                "☼ »Я☼" +
                "☼###☼" +
                "☼☼☼☼☼");

        robber().left(); // после этого он может двигаться
        tick();

        assertE("☼☼☼☼☼" +
                "☼   ☼" +
                "☼« Я☼" +
                "☼###☼" +
                "☼☼☼☼☼");
    }

    // если чертик попадает на героя - тот погибает
    @Test
    public void shouldHeroDieWhenMeetWithRobber() {
        givenFl("☼☼☼☼☼" +
                "☼   ☼" +
                "☼◄ «☼" +
                "☼###☼" +
                "☼☼☼☼☼");

        hero().right();
        robber().left();
        tick();

        assertE("☼☼☼☼☼" +
                "☼   ☼" +
                "☼ Ѡ ☼" +
                "☼###☼" +
                "☼☼☼☼☼");

        events.verifyAllEvents("[KILL_HERO]");

        dice(1, 3);
        tick();         // ну а после смерти он появляется в рендомном месте причем чертик остается на своем месте
        field.newGame(player());

        assertE("☼☼☼☼☼" +
                "☼[  ☼" +
                "☼ « ☼" +
                "☼###☼" +
                "☼☼☼☼☼");
    }

    // другой кейс, когда оба двигаются на встречу друг к другу
    @Test
    public void shouldHeroDieWhenMeetWithRobber2() {
        givenFl("☼☼☼☼☼" +
                "☼   ☼" +
                "☼◄« ☼" +
                "☼###☼" +
                "☼☼☼☼☼");

        hero().right();
        robber().left();
        tick();

        assertE("☼☼☼☼☼" +
                "☼   ☼" +
                "☼«Ѡ ☼" +
                "☼###☼" +
                "☼☼☼☼☼");

        events.verifyAllEvents("[KILL_HERO]");

        dice(0,  // охотимся за первым игроком
            3, 3);
        tick();         // ну а после смерти он появляется в рендомном месте причем чертик остается на своем месте
        field.newGame(player());

        assertE("☼☼☼☼☼" +
                "☼  [☼" +
                "☼«  ☼" +
                "☼###☼" +
                "☼☼☼☼☼");
    }

    // другой кейс, когда игрок идет на чертика
    @Test
    public void shouldHeroDieWhenMeetWithRobber_whenHeroWalk() {
        givenFl("☼☼☼☼☼" +
                "☼   ☼" +
                "☼◄« ☼" +
                "☼###☼" +
                "☼☼☼☼☼");

        hero().right();
        tick();

        assertE("☼☼☼☼☼" +
                "☼   ☼" +
                "☼ Ѡ ☼" +
                "☼###☼" +
                "☼☼☼☼☼");

        events.verifyAllEvents("[KILL_HERO]");

        dice(3, 3);
        tick();         // ну а после смерти он появляется в рендомном месте причем чертик остается на своем месте
        field.newGame(player());

        assertE("☼☼☼☼☼" +
                "☼  [☼" +
                "☼ « ☼" +
                "☼###☼" +
                "☼☼☼☼☼");
    }

    // другой кейс, когда чертик идет на игрока
    @Test
    public void shouldHeroDieWhenMeetWithRobber_whenRobberWalk() {
        givenFl("☼☼☼☼☼" +
                "☼   ☼" +
                "☼◄« ☼" +
                "☼###☼" +
                "☼☼☼☼☼");

        robber().left();
        tick();

        assertE("☼☼☼☼☼" +
                "☼   ☼" +
                "☼Ѡ  ☼" +
                "☼###☼" +
                "☼☼☼☼☼");

        events.verifyAllEvents("[KILL_HERO]");

        dice(0,  // охотимся за первым игроком
            3, 3);
        tick();         // ну а после смерти он появляется в рендомном месте причем чертик остается на своем месте
        field.newGame(player());

        assertE("☼☼☼☼☼" +
                "☼  [☼" +
                "☼«  ☼" +
                "☼###☼" +
                "☼☼☼☼☼");
    }

    // Чертик может зайти на лестницу и выйти обратно
    @Test
    public void shouldRobberCanGoOnLadder() {
        givenFl("☼☼☼☼►" +
                "☼  H☼" +
                "☼  H☼" +
                "☼ «H☼" +
                "☼☼☼☼☼");

        robber().right();
        tick();

        assertE("☼☼☼☼►" +
                "☼  H☼" +
                "☼  H☼" +
                "☼  Q☼" +
                "☼☼☼☼☼");

        robber().left();
        tick();

        assertE("☼☼☼☼►" +
                "☼  H☼" +
                "☼  H☼" +
                "☼ «H☼" +
                "☼☼☼☼☼");
    }

    // Чертик может карабкаться по лестнице вверх
    @Test
    public void shouldRobberCanGoOnLadderUp() {
        givenFl("☼☼☼☼►" +
                "☼  H☼" +
                "☼  H☼" +
                "☼ «H☼" +
                "☼☼☼☼☼");

        robber().right();
        tick();

        assertE("☼☼☼☼►" +
                "☼  H☼" +
                "☼  H☼" +
                "☼  Q☼" +
                "☼☼☼☼☼");

        robber().up();
        tick();

        assertE("☼☼☼☼►" +
                "☼  H☼" +
                "☼  Q☼" +
                "☼  H☼" +
                "☼☼☼☼☼");

        robber().up();
        tick();

        assertE("☼☼☼☼►" +
                "☼  Q☼" +
                "☼  H☼" +
                "☼  H☼" +
                "☼☼☼☼☼");
    }

    // Чертик не может вылезти с лестницей за границы
    @Test
    public void shouldRobberCantGoOnBarrierFromLadder() {
        shouldRobberCanGoOnLadderUp();

        assertE("☼☼☼☼►" +
                "☼  Q☼" +
                "☼  H☼" +
                "☼  H☼" +
                "☼☼☼☼☼");

        robber().up();
        tick();

        assertE("☼☼☼☼►" +
                "☼  Q☼" +
                "☼  H☼" +
                "☼  H☼" +
                "☼☼☼☼☼");

        robber().right();
        tick();

        assertE("☼☼☼☼►" +
                "☼  Q☼" +
                "☼  H☼" +
                "☼  H☼" +
                "☼☼☼☼☼");
    }

    // Чертик может спустится вниз, но не дальше границы экрана
    @Test
    public void shouldRobberCanGoOnLadderDown() {
        shouldRobberCanGoOnLadderUp();

        assertE("☼☼☼☼►" +
                "☼  Q☼" +
                "☼  H☼" +
                "☼  H☼" +
                "☼☼☼☼☼");

        robber().down();
        tick();

        assertE("☼☼☼☼►" +
                "☼  H☼" +
                "☼  Q☼" +
                "☼  H☼" +
                "☼☼☼☼☼");

        robber().down();
        tick();

        assertE("☼☼☼☼►" +
                "☼  H☼" +
                "☼  H☼" +
                "☼  Q☼" +
                "☼☼☼☼☼");

        robber().down();
        tick();

        assertE("☼☼☼☼►" +
                "☼  H☼" +
                "☼  H☼" +
                "☼  Q☼" +
                "☼☼☼☼☼");
    }

    // Чертик может в любой момент спрыгнуть с лестницы и будет падать до тех пор пока не наткнется на препятствие
    @Test
    public void shouldRobberCanFly() {
        shouldRobberCanGoOnLadderUp();

        assertE("☼☼☼☼►" +
                "☼  Q☼" +
                "☼  H☼" +
                "☼  H☼" +
                "☼☼☼☼☼");

        robber().left();
        tick();

        assertE("☼☼☼☼►" +
                "☼ «H☼" +
                "☼  H☼" +
                "☼  H☼" +
                "☼☼☼☼☼");

        tick();

        assertE("☼☼☼☼►" +
                "☼  H☼" +
                "☼ «H☼" +
                "☼  H☼" +
                "☼☼☼☼☼");

        tick();

        assertE("☼☼☼☼►" +
                "☼  H☼" +
                "☼  H☼" +
                "☼ «H☼" +
                "☼☼☼☼☼");
    }

    // Чертик может поднятся по лестнице и зайти на площадку
    @Test
    public void shouldRobberCanGoFromLadderToArea() {
        givenFl("☼☼☼☼►" +
                "☼H  ☼" +
                "☼H# ☼" +
                "☼H« ☼" +
                "☼☼☼☼☼");

        robber().left();
        tick();

        assertE("☼☼☼☼►" +
                "☼H  ☼" +
                "☼H# ☼" +
                "☼Q  ☼" +
                "☼☼☼☼☼");

        robber().up();
        tick();

        assertE("☼☼☼☼►" +
                "☼H  ☼" +
                "☼Q# ☼" +
                "☼H  ☼" +
                "☼☼☼☼☼");

        robber().up();
        tick();

        assertE("☼☼☼☼►" +
                "☼Q  ☼" +
                "☼H# ☼" +
                "☼H  ☼" +
                "☼☼☼☼☼");

        robber().right();
        tick();

        assertE("☼☼☼☼►" +
                "☼H» ☼" +
                "☼H# ☼" +
                "☼H  ☼" +
                "☼☼☼☼☼");

        // и упасть
        robber().right();
        tick();

        assertE("☼☼☼☼►" +
                "☼H »☼" +
                "☼H# ☼" +
                "☼H  ☼" +
                "☼☼☼☼☼");

        tick();

        assertE("☼☼☼☼►" +
                "☼H  ☼" +
                "☼H#»☼" +
                "☼H  ☼" +
                "☼☼☼☼☼");

        tick();

        assertE("☼☼☼☼►" +
                "☼H  ☼" +
                "☼H# ☼" +
                "☼H »☼" +
                "☼☼☼☼☼");
    }

    // пока чертик падает он не может двигаться влево и справо, даже если там есть площадки
    @Test
    public void shouldRobberCantMoveWhenFall() {
        givenFl("☼☼☼☼☼☼►" +
                "☼  »  ☼" +
                "☼     ☼" +
                "☼     ☼" +
                "☼     ☼" +
                "☼## ##☼" +
                "☼☼☼☼☼☼☼");

        robber().left();
        tick();

        assertE("☼☼☼☼☼☼►" +
                "☼     ☼" +
                "☼  »  ☼" +
                "☼     ☼" +
                "☼     ☼" +
                "☼## ##☼" +
                "☼☼☼☼☼☼☼");

        robber().right();
        tick();

        assertE("☼☼☼☼☼☼►" +
                "☼     ☼" +
                "☼     ☼" +
                "☼  »  ☼" +
                "☼     ☼" +
                "☼## ##☼" +
                "☼☼☼☼☼☼☼");

        robber().left();
        tick();

        assertE("☼☼☼☼☼☼►" +
                "☼     ☼" +
                "☼     ☼" +
                "☼     ☼" +
                "☼  »  ☼" +
                "☼## ##☼" +
                "☼☼☼☼☼☼☼");

        robber().left();
        tick();

        assertE("☼☼☼☼☼☼►" +
                "☼     ☼" +
                "☼     ☼" +
                "☼     ☼" +
                "☼     ☼" +
                "☼##»##☼" +
                "☼☼☼☼☼☼☼");
    }

    // если чертик с площадки заходит на трубу то он ползет по ней
    @Test
    public void shouldRobberPipe() {
        givenFl("☼☼☼☼☼☼►" +
                "☼     ☼" +
                "☼»~~~ ☼" +
                "☼#    ☼" +
                "☼     ☼" +
                "☼     ☼" +
                "☼☼☼☼☼☼☼");

        robber().right();
        tick();

        assertE("☼☼☼☼☼☼►" +
                "☼     ☼" +
                "☼ >~~ ☼" +
                "☼#    ☼" +
                "☼     ☼" +
                "☼     ☼" +
                "☼☼☼☼☼☼☼");

        robber().right();
        tick();

        assertE("☼☼☼☼☼☼►" +
                "☼     ☼" +
                "☼ ~>~ ☼" +
                "☼#    ☼" +
                "☼     ☼" +
                "☼     ☼" +
                "☼☼☼☼☼☼☼");

        robber().right();
        tick();

        assertE("☼☼☼☼☼☼►" +
                "☼     ☼" +
                "☼ ~~> ☼" +
                "☼#    ☼" +
                "☼     ☼" +
                "☼     ☼" +
                "☼☼☼☼☼☼☼");
    }

    // с трубы чертик может спрыгунть и тогда он будет падать до препятствия
    @Test
    public void shouldRobberFallFromPipe() {
        shouldRobberPipe();

        assertE("☼☼☼☼☼☼►" +
                "☼     ☼" +
                "☼ ~~> ☼" +
                "☼#    ☼" +
                "☼     ☼" +
                "☼     ☼" +
                "☼☼☼☼☼☼☼");

        robber().right();
        tick();

        assertE("☼☼☼☼☼☼►" +
                "☼     ☼" +
                "☼ ~~~»☼" +
                "☼#    ☼" +
                "☼     ☼" +
                "☼     ☼" +
                "☼☼☼☼☼☼☼");

        tick();

        assertE("☼☼☼☼☼☼►" +
                "☼     ☼" +
                "☼ ~~~ ☼" +
                "☼#   »☼" +
                "☼     ☼" +
                "☼     ☼" +
                "☼☼☼☼☼☼☼");

        tick();

        assertE("☼☼☼☼☼☼►" +
                "☼     ☼" +
                "☼ ~~~ ☼" +
                "☼#    ☼" +
                "☼    »☼" +
                "☼     ☼" +
                "☼☼☼☼☼☼☼");

        tick();

        assertE("☼☼☼☼☼☼►" +
                "☼     ☼" +
                "☼ ~~~ ☼" +
                "☼#    ☼" +
                "☼     ☼" +
                "☼    »☼" +
                "☼☼☼☼☼☼☼");
    }

    // чертик может похитить 1 улику в падении
    @Test
    public void shouldRobberGetClueWhenFallenFromPipe() {
        givenFl("☼☼☼☼☼☼☼☼" +
                "☼      ☼" +
                "☼  $~~«☼" +
                "☼  $  #☼" +
                "☼  $   ☼" +
                "☼  $  ◄☼" +
                "☼######☼" +
                "☼☼☼☼☼☼☼☼");

        robber().left();
        tick();

        assertE("☼☼☼☼☼☼☼☼" +
                "☼      ☼" +
                "☼  $~< ☼" +
                "☼  $  #☼" +
                "☼  $   ☼" +
                "☼  $  ◄☼" +
                "☼######☼" +
                "☼☼☼☼☼☼☼☼");

        robber().left();
        tick();

        assertE("☼☼☼☼☼☼☼☼" +
                "☼      ☼" +
                "☼  $<~ ☼" +
                "☼  $  #☼" +
                "☼  $   ☼" +
                "☼  $  ◄☼" +
                "☼######☼" +
                "☼☼☼☼☼☼☼☼");

        robber().left();
        tick();

        assertE("☼☼☼☼☼☼☼☼" +
                "☼      ☼" +
                "☼  «~~ ☼" +
                "☼  $  #☼" +
                "☼  $   ☼" +
                "☼  $  ◄☼" +
                "☼######☼" +
                "☼☼☼☼☼☼☼☼");

        tick();      // чертик не берет больше 1 улики

        assertE("☼☼☼☼☼☼☼☼" +
                "☼      ☼" +
                "☼   ~~ ☼" +
                "☼  «  #☼" +
                "☼  $   ☼" +
                "☼  $  ◄☼" +
                "☼######☼" +
                "☼☼☼☼☼☼☼☼");

        tick();

        assertE("☼☼☼☼☼☼☼☼" +
                "☼      ☼" +
                "☼   ~~ ☼" +
                "☼  $  #☼" +
                "☼  «   ☼" +
                "☼  $  ◄☼" +
                "☼######☼" +
                "☼☼☼☼☼☼☼☼");

        tick();

        assertE("☼☼☼☼☼☼☼☼" +
                "☼      ☼" +
                "☼   ~~ ☼" +
                "☼  $  #☼" +
                "☼  $   ☼" +
                "☼  «  ◄☼" +
                "☼######☼" +
                "☼☼☼☼☼☼☼☼");
    }

    @Test
    public void shouldRobberLeaveClueWhenFallInPit() {
        shouldRobberGetClueWhenFallenFromPipe();

        robber().right();
        hero().act();
        tick();

        assertE("☼☼☼☼☼☼☼☼" +
                "☼      ☼" +
                "☼   ~~ ☼" +
                "☼  $  #☼" +
                "☼  $   ☼" +
                "☼  $» Я☼" +
                "☼####*#☼" +
                "☼☼☼☼☼☼☼☼");

        robber().right();     // если чертик с уликой падает в ямку - он оставляет улику на поверхности
        tick();

        assertE("☼☼☼☼☼☼☼☼" +
                "☼      ☼" +
                "☼   ~~ ☼" +
                "☼  $  #☼" +
                "☼  $   ☼" +
                "☼  $ »Я☼" +
                "☼#### #☼" +
                "☼☼☼☼☼☼☼☼");

        robber().right();
        tick();

        assertE("☼☼☼☼☼☼☼☼" +
                "☼      ☼" +
                "☼   ~~ ☼" +
                "☼  $  #☼" +
                "☼  $   ☼" +
                "☼  $ $Я☼" +
                "☼####⍈#☼" +
                "☼☼☼☼☼☼☼☼");
    }

    @Test
    public void shouldIWalkOnRobberInPitAndGetClue() {
        shouldRobberLeaveClueWhenFallInPit();

        hero().left();     //я могу пройти по нему сверху и забрать улику
        dice(1, 6);
        tick();

        assertE("☼☼☼☼☼☼☼☼" +
                "☼$     ☼" +
                "☼   ~~ ☼" +
                "☼  $  #☼" +
                "☼  $   ☼" +
                "☼  $ ◄ ☼" +
                "☼####⍈#☼" +
                "☼☼☼☼☼☼☼☼");

        events.verifyAllEvents("[GET_KNIFE_CLUE]");

        hero().left();
        tick();

        assertE("☼☼☼☼☼☼☼☼" +
                "☼$     ☼" +
                "☼   ~~ ☼" +
                "☼  $  #☼" +
                "☼  $   ☼" +
                "☼  $◄  ☼" +
                "☼####⍈#☼" +
                "☼☼☼☼☼☼☼☼");

        hero().left();
        dice(2, 6);
        tick();

        events.verifyAllEvents("[GET_KNIFE_CLUE]");

        assertE("☼☼☼☼☼☼☼☼" +
                "☼$$    ☼" +
                "☼   ~~ ☼" +
                "☼  $  #☼" +
                "☼  $   ☼" +
                "☼  ◄   ☼" +
                "☼####⍈#☼" +
                "☼☼☼☼☼☼☼☼");
    }

    @Test
    public void shouldNoMoreClueARobber() {
        shouldIWalkOnRobberInPitAndGetClue();

        for (int c = 4; c < Brick.CRACK_TIMER; c++) { // враг вылазит
            tick();
        }

        robber().left();
        hero().right();
        hero().act();
        tick();

        assertE("☼☼☼☼☼☼☼☼" +
                "☼$$    ☼" +
                "☼   ~~ ☼" +
                "☼  $  #☼" +
                "☼  $   ☼" +
                "☼  R « ☼" +
                "☼###*##☼" +
                "☼☼☼☼☼☼☼☼");

        robber().left();
        tick();

        assertE("☼☼☼☼☼☼☼☼" +
                "☼$$    ☼" +
                "☼   ~~ ☼" +
                "☼  $  #☼" +
                "☼  $   ☼" +
                "☼  R«  ☼" +
                "☼### ##☼" +
                "☼☼☼☼☼☼☼☼");

        tick();  // уликы у него больше нет

        assertE("☼☼☼☼☼☼☼☼" +
                "☼$$    ☼" +
                "☼   ~~ ☼" +
                "☼  $  #☼" +
                "☼  $   ☼" +
                "☼  R   ☼" +
                "☼###⍇##☼" +
                "☼☼☼☼☼☼☼☼");
    }

    // я могу ходить по монстру, который в ямке
    @Test
    public void shouldIWalkOnRobber() {
        shouldRobberLeaveClueWhenFallInPit();

        assertE("☼☼☼☼☼☼☼☼" +
                "☼      ☼" +
                "☼   ~~ ☼" +
                "☼  $  #☼" +
                "☼  $   ☼" +
                "☼  $ $Я☼" +
                "☼####⍈#☼" +
                "☼☼☼☼☼☼☼☼");

        dice(1, 6);
        hero().left();
        tick();

        events.verifyAllEvents("[GET_KNIFE_CLUE]");

        assertE("☼☼☼☼☼☼☼☼" +
                "☼$     ☼" +
                "☼   ~~ ☼" +
                "☼  $  #☼" +
                "☼  $   ☼" +
                "☼  $ ◄ ☼" +
                "☼####⍈#☼" +
                "☼☼☼☼☼☼☼☼");

        hero().left();
        tick();

        assertE("☼☼☼☼☼☼☼☼" +
                "☼$     ☼" +
                "☼   ~~ ☼" +
                "☼  $  #☼" +
                "☼  $   ☼" +
                "☼  $◄  ☼" +
                "☼####⍈#☼" +
                "☼☼☼☼☼☼☼☼");

        dice(2, 6);
        hero().left();
        tick();

        assertE("☼☼☼☼☼☼☼☼" +
                "☼$$    ☼" +
                "☼   ~~ ☼" +
                "☼  $  #☼" +
                "☼  $   ☼" +
                "☼  ◄   ☼" +
                "☼####⍈#☼" +
                "☼☼☼☼☼☼☼☼");

        tick();

        events.verifyAllEvents("[GET_KNIFE_CLUE]");

        tick();
        tick();
        tick();
        tick();
        tick();
        tick();
        tick();

        assertE("☼☼☼☼☼☼☼☼" +
                "☼$$    ☼" +
                "☼   ~~ ☼" +
                "☼  $  #☼" +
                "☼  $   ☼" +
                "☼  ◄ » ☼" +
                "☼######☼" +
                "☼☼☼☼☼☼☼☼");
    }

    // я не могу прострелить дырку непосредственно под монстром
    // TODO сделать так, чтобы мог
    @Test
    public void shouldICantCrackUnderRobber() {
        givenFl("☼☼☼☼☼" +
                "☼   ☼" +
                "☼ ►»☼" +
                "☼###☼" +
                "☼☼☼☼☼");

        hero().act();
        tick();

        assertE("☼☼☼☼☼" +
                "☼   ☼" +
                "☼ ►»☼" +
                "☼###☼" +
                "☼☼☼☼☼");
    }

    // если я прострелил дырку монстр падает в нее, а под ней ничего нет - монстр не проваливается сквозь
    @Test
    public void shouldRobberStayOnPitWhenUnderPitIsFree() {
        givenFl("☼☼☼☼☼☼☼" +
                "☼» ◄  ☼" +
                "☼#####☼" +
                "☼     ☼" +
                "☼     ☼" +
                "☼     ☼" +
                "☼☼☼☼☼☼☼");

        hero().act();
        tick();

        assertE("☼☼☼☼☼☼☼" +
                "☼» Я  ☼" +
                "☼#*###☼" +
                "☼     ☼" +
                "☼     ☼" +
                "☼     ☼" +
                "☼☼☼☼☼☼☼");

        robber().right();
        tick();

        assertE("☼☼☼☼☼☼☼" +
                "☼ »Я  ☼" +
                "☼# ###☼" +
                "☼     ☼" +
                "☼     ☼" +
                "☼     ☼" +
                "☼☼☼☼☼☼☼");

        tick();

        assertE("☼☼☼☼☼☼☼" +
                "☼  Я  ☼" +
                "☼#⍈###☼" +
                "☼     ☼" +
                "☼     ☼" +
                "☼     ☼" +
                "☼☼☼☼☼☼☼");

        tick();
        tick();
        tick();
        tick();
        tick();
        tick();
        tick();
        tick();
        tick();
        tick();

        assertE("☼☼☼☼☼☼☼" +
                "☼  Я  ☼" +
                "☼#⍈###☼" +
                "☼     ☼" +
                "☼     ☼" +
                "☼     ☼" +
                "☼☼☼☼☼☼☼");

        tick();

        assertE("☼☼☼☼☼☼☼" +
                "☼ »Я  ☼" +
                "☼#####☼" +
                "☼     ☼" +
                "☼     ☼" +
                "☼     ☼" +
                "☼☼☼☼☼☼☼");
    }

    // если в процессе падения чертик вдург наткнулся на трубу то он повисаю на ней
    @Test
    public void shouldRobberPipeWhenFall() {
        givenFl("☼☼☼☼☼☼►" +
                "☼  »  ☼" +
                "☼# ###☼" +
                "☼     ☼" +
                "☼ ~~~ ☼" +
                "☼     ☼" +
                "☼☼☼☼☼☼☼");

        robber().left();
        tick();

        assertE("☼☼☼☼☼☼►" +
                "☼ «   ☼" +
                "☼# ###☼" +
                "☼     ☼" +
                "☼ ~~~ ☼" +
                "☼     ☼" +
                "☼☼☼☼☼☼☼");

        tick();

        assertE("☼☼☼☼☼☼►" +
                "☼     ☼" +
                "☼#«###☼" +
                "☼     ☼" +
                "☼ ~~~ ☼" +
                "☼     ☼" +
                "☼☼☼☼☼☼☼");

        tick();

        assertE("☼☼☼☼☼☼►" +
                "☼     ☼" +
                "☼# ###☼" +
                "☼ «   ☼" +
                "☼ ~~~ ☼" +
                "☼     ☼" +
                "☼☼☼☼☼☼☼");

        tick();

        assertE("☼☼☼☼☼☼►" +
                "☼     ☼" +
                "☼# ###☼" +
                "☼     ☼" +
                "☼ <~~ ☼" +
                "☼     ☼" +
                "☼☼☼☼☼☼☼");

        tick();

        assertE("☼☼☼☼☼☼►" +
                "☼     ☼" +
                "☼# ###☼" +
                "☼     ☼" +
                "☼ <~~ ☼" +
                "☼     ☼" +
                "☼☼☼☼☼☼☼");
    }

    // чертик может спрыгнуть с трубы
    @Test
    public void shouldRobberCanJumpFromPipe() {
        givenFl("☼☼☼☼☼☼►" +
                "☼  «  ☼" +
                "☼     ☼" +
                "☼ ~~~ ☼" +
                "☼     ☼" +
                "☼     ☼" +
                "☼☼☼☼☼☼☼");

        tick();

        assertE("☼☼☼☼☼☼►" +
                "☼     ☼" +
                "☼  «  ☼" +
                "☼ ~~~ ☼" +
                "☼     ☼" +
                "☼     ☼" +
                "☼☼☼☼☼☼☼");

        tick();

        assertE("☼☼☼☼☼☼►" +
                "☼     ☼" +
                "☼     ☼" +
                "☼ ~<~ ☼" +
                "☼     ☼" +
                "☼     ☼" +
                "☼☼☼☼☼☼☼");

        robber().down();
        tick();

        assertE("☼☼☼☼☼☼►" +
                "☼     ☼" +
                "☼     ☼" +
                "☼ ~~~ ☼" +
                "☼  «  ☼" +
                "☼     ☼" +
                "☼☼☼☼☼☼☼");

        tick();

        assertE("☼☼☼☼☼☼►" +
                "☼     ☼" +
                "☼     ☼" +
                "☼ ~~~ ☼" +
                "☼     ☼" +
                "☼  «  ☼" +
                "☼☼☼☼☼☼☼");
    }

    // чертику нельзя спускаться с лестницы в бетон, так же как и подниматься
    // плюс чертик должен иметь возможность спустится по лестнице
    @Test
    public void shouldRobberCantWalkThroughWallDown() {
        givenFl("☼☼☼☼►" +
                "☼ « ☼" +
                "☼ H ☼" +
                "☼☼☼☼☼" +
                "☼☼☼☼☼");

        robber().down();
        tick();

        assertE("☼☼☼☼►" +
                "☼   ☼" +
                "☼ Q ☼" +
                "☼☼☼☼☼" +
                "☼☼☼☼☼");

        robber().down();
        tick();

        assertE("☼☼☼☼►" +
                "☼   ☼" +
                "☼ Q ☼" +
                "☼☼☼☼☼" +
                "☼☼☼☼☼");
    }

    @Test
    public void shouldRobberCantWalkThroughWallUp() {
        settings.integer(ROBBERS_COUNT, 1);
        givenFl("☼☼☼☼►" +
                "☼☼☼☼☼" +
                "☼ H ☼" +
                "☼ H«☼" +
                "☼☼☼☼☼");

        robber().left();
        tick();

        assertE("☼☼☼☼►" +
                "☼☼☼☼☼" +
                "☼ H ☼" +
                "☼ Q ☼" +
                "☼☼☼☼☼");

        robber().up();
        tick();

        assertE("☼☼☼☼►" +
                "☼☼☼☼☼" +
                "☼ Q ☼" +
                "☼ H ☼" +
                "☼☼☼☼☼");

        robber().up();
        tick();

        assertE("☼☼☼☼►" +
                "☼☼☼☼☼" +
                "☼ Q ☼" +
                "☼ H ☼" +
                "☼☼☼☼☼");
    }

    // Чертику нельзя проходить с лестницы через бетон направо или налево
    @Test
    public void shouldRobberCantWalkThroughWallLeftRight() {
        givenFl("☼☼☼☼►" +
                "☼☼☼☼☼" +
                "☼☼H☼☼" +
                "☼ H«☼" +
                "☼☼☼☼☼");

        robber().left();
        tick();

        assertE("☼☼☼☼►" +
                "☼☼☼☼☼" +
                "☼☼H☼☼" +
                "☼ Q ☼" +
                "☼☼☼☼☼");

        robber().up();
        tick();

        assertE("☼☼☼☼►" +
                "☼☼☼☼☼" +
                "☼☼Q☼☼" +
                "☼ H ☼" +
                "☼☼☼☼☼");

        robber().left();
        tick();

        assertE("☼☼☼☼►" +
                "☼☼☼☼☼" +
                "☼☼Q☼☼" +
                "☼ H ☼" +
                "☼☼☼☼☼");

        robber().right();
        tick();

        assertE("☼☼☼☼►" +
                "☼☼☼☼☼" +
                "☼☼Q☼☼" +
                "☼ H ☼" +
                "☼☼☼☼☼");
    }

    // чертику нельзя проходить через бетон
    @Test
    public void shouldRobberCantWalkThroughWallLeftRight2() {
        givenFl("☼☼☼☼►" +
                "☼   ☼" +
                "☼   ☼" +
                "☼☼«☼☼" +
                "☼☼☼☼☼");

        robber().left();
        tick();

        assertE("☼☼☼☼►" +
                "☼   ☼" +
                "☼   ☼" +
                "☼☼«☼☼" +
                "☼☼☼☼☼");

        robber().right();
        tick();

        assertE("☼☼☼☼►" +
                "☼   ☼" +
                "☼   ☼" +
                "☼☼»☼☼" +
                "☼☼☼☼☼");
    }

    // Чертику нельзя спрыгивать с трубы что сразу над бетоном, протелая сквозь него
    @Test
    public void shouldRobberCantJumpThroughWall() {
        givenFl("☼☼☼☼☼►" +
                "☼  » ☼" +
                "☼  ~ ☼" +
                "☼☼☼☼☼☼" +
                "☼    ☼" +
                "☼☼☼☼☼☼");

        tick();

        assertE("☼☼☼☼☼►" +
                "☼    ☼" +
                "☼  > ☼" +
                "☼☼☼☼☼☼" +
                "☼    ☼" +
                "☼☼☼☼☼☼");

        robber().down();
        tick();

        assertE("☼☼☼☼☼►" +
                "☼    ☼" +
                "☼  > ☼" +
                "☼☼☼☼☼☼" +
                "☼    ☼" +
                "☼☼☼☼☼☼");
    }

    // если чертик спрыгивает с последней секции лестницы
    @Test
    public void shouldRobberJumpFromLadderDown() {
        givenFl("☼☼☼☼☼►" +
                "☼ »  ☼" +
                "☼ H##☼" +
                "☼#H  ☼" +
                "☼    ☼" +
                "☼☼☼☼☼☼");

        robber().down();
        tick();

        robber().down();
        tick();

        assertE("☼☼☼☼☼►" +
                "☼    ☼" +
                "☼ H##☼" +
                "☼#Q  ☼" +
                "☼    ☼" +
                "☼☼☼☼☼☼");

        tick();

        assertE("☼☼☼☼☼►" +
                "☼    ☼" +
                "☼ H##☼" +
                "☼#Q  ☼" +
                "☼    ☼" +
                "☼☼☼☼☼☼");

        robber().right();
        tick();

        assertE("☼☼☼☼☼►" +
                "☼    ☼" +
                "☼ H##☼" +
                "☼#H» ☼" +
                "☼    ☼" +
                "☼☼☼☼☼☼");

        tick();

        assertE("☼☼☼☼☼►" +
                "☼    ☼" +
                "☼ H##☼" +
                "☼#H  ☼" +
                "☼  » ☼" +
                "☼☼☼☼☼☼");
    }

    // Чертик не может прыгять вверх :)
    @Test
    public void shouldRobberCantJump() {
        givenFl("☼☼☼☼☼►" +
                "☼    ☼" +
                "☼    ☼" +
                "☼    ☼" +
                "☼ »  ☼" +
                "☼☼☼☼☼☼");

        robber().up();
        tick();

        assertE("☼☼☼☼☼►" +
                "☼    ☼" +
                "☼    ☼" +
                "☼    ☼" +
                "☼ »  ☼" +
                "☼☼☼☼☼☼");
    }

    // я могу прыгнуть на голову монстру и мне ничего не будет
    @Test
    public void shouldICanJumpAtRobberHead() {
        givenFl("☼☼☼☼☼" +
                "☼ ◄ ☼" +
                "☼   ☼" +
                "☼ » ☼" +
                "☼☼☼☼☼");

        tick();

        assertE("☼☼☼☼☼" +
                "☼   ☼" +
                "☼ ◄ ☼" +
                "☼ » ☼" +
                "☼☼☼☼☼");

        tick();

        assertE("☼☼☼☼☼" +
                "☼   ☼" +
                "☼ ◄ ☼" +
                "☼ » ☼" +
                "☼☼☼☼☼");

        robber().right();     // если он отойдет - я упаду дальше
        tick();

        assertE("☼☼☼☼☼" +
                "☼   ☼" +
                "☼ ] ☼" +
                "☼  »☼" +
                "☼☼☼☼☼");

        tick();

        assertE("☼☼☼☼☼" +
                "☼   ☼" +
                "☼   ☼" +
                "☼ ◄»☼" +
                "☼☼☼☼☼");
    }

    // если чертик после того, как упал в ямку оставил улику, то когда он выберется - он свою улику заберет
    // А когда снова упадет, то оставит
    @Test
    public void shouldGetClueWheExitFromPit() {
        shouldRobberLeaveClueWhenFallInPit();

        assertE("☼☼☼☼☼☼☼☼" +
                "☼      ☼" +
                "☼   ~~ ☼" +
                "☼  $  #☼" +
                "☼  $   ☼" +
                "☼  $ $Я☼" +
                "☼####⍈#☼" +
                "☼☼☼☼☼☼☼☼");

        for (int c = 3; c < Brick.CRACK_TIMER; c++) { // враг вылазит
            tick();
        }
        tick();

        assertE("☼☼☼☼☼☼☼☼" +
                "☼      ☼" +
                "☼   ~~ ☼" +
                "☼  $  #☼" +
                "☼  $   ☼" +
                "☼  $ »Я☼" +
                "☼######☼" +
                "☼☼☼☼☼☼☼☼");

        robber().left();
        tick();

        hero().act();
        tick();

        assertE("☼☼☼☼☼☼☼☼" +
                "☼      ☼" +
                "☼   ~~ ☼" +
                "☼  $  #☼" +
                "☼  $   ☼" +
                "☼  $« Я☼" +
                "☼####*#☼" +
                "☼☼☼☼☼☼☼☼");

        robber().right();
        tick();
        tick();

        assertE("☼☼☼☼☼☼☼☼" +
                "☼      ☼" +
                "☼   ~~ ☼" +
                "☼  $  #☼" +
                "☼  $   ☼" +
                "☼  $ $Я☼" +
                "☼####⍈#☼" +
                "☼☼☼☼☼☼☼☼");
    }

    // Если чертик упал на другого чертика который был на трубе, то они складываются в один :)
    @Test
    public void shouldRobberStayOnOtherAtThePipe() {
        givenFl("☼☼☼☼☼☼☼☼" +
                "☼  «   ☼" +
                "☼      ☼" +
                "☼  «   ☼" +
                "☼  ~   ☼" +
                "☼     ◄☼" +
                "☼######☼" +
                "☼☼☼☼☼☼☼☼");

        tick();

        assertE("☼☼☼☼☼☼☼☼" +
                "☼      ☼" +
                "☼  «   ☼" +
                "☼      ☼" +
                "☼  <   ☼" +
                "☼     ◄☼" +
                "☼######☼" +
                "☼☼☼☼☼☼☼☼");

        tick();
        tick();
        tick();

        assertE("☼☼☼☼☼☼☼☼" +
                "☼      ☼" +
                "☼      ☼" +
                "☼  «   ☼" +
                "☼  <   ☼" +
                "☼     ◄☼" +
                "☼######☼" +
                "☼☼☼☼☼☼☼☼");

        robber().down();
        tick();

        assertE("☼☼☼☼☼☼☼☼" +
                "☼      ☼" +
                "☼      ☼" +
                "☼      ☼" +
                "☼  <   ☼" +
                "☼     ◄☼" +
                "☼######☼" +
                "☼☼☼☼☼☼☼☼");

        robber().left();
        tick();
        tick();

        assertE("☼☼☼☼☼☼☼☼" +
                "☼      ☼" +
                "☼      ☼" +
                "☼      ☼" +
                "☼  <   ☼" +
                "☼ «   ◄☼" +
                "☼######☼" +
                "☼☼☼☼☼☼☼☼");
    }

    @Test
    public void shouldRobberDontStopOnPipe() {
        givenFl("☼☼☼☼☼☼☼☼" +
                "☼      ☼" +
                "☼      ☼" +
                "☼  ««  ☼" +
                "☼  ~~  ☼" +
                "☼     ◄☼" +
                "☼######☼" +
                "☼☼☼☼☼☼☼☼");

        tick();

        assertE("☼☼☼☼☼☼☼☼" +
                "☼      ☼" +
                "☼      ☼" +
                "☼      ☼" +
                "☼  <<  ☼" +
                "☼     ◄☼" +
                "☼######☼" +
                "☼☼☼☼☼☼☼☼");

        robber().right();
        tick();

        assertE("☼☼☼☼☼☼☼☼" +
                "☼      ☼" +
                "☼      ☼" +
                "☼      ☼" +
                "☼  ~<  ☼" +
                "☼     ◄☼" +
                "☼######☼" +
                "☼☼☼☼☼☼☼☼");

        robber().right();
        tick();
        tick();

        assertE("☼☼☼☼☼☼☼☼" +
                "☼      ☼" +
                "☼      ☼" +
                "☼      ☼" +
                "☼  ~<  ☼" +
                "☼    »◄☼" +
                "☼######☼" +
                "☼☼☼☼☼☼☼☼");
    }

    // Чертик должен сам проваливаться на героя, а не впрыгивать в него
    @Test
    public void shouldRobberStayOnHeroAtThePipe() {
        givenFl("☼☼☼☼☼☼☼☼" +
                "☼  «   ☼" +
                "☼      ☼" +
                "☼  ◄   ☼" +
                "☼  ~   ☼" +
                "☼      ☼" +
                "☼######☼" +
                "☼☼☼☼☼☼☼☼");

        tick();

        assertE("☼☼☼☼☼☼☼☼" +
                "☼      ☼" +
                "☼  «   ☼" +
                "☼      ☼" +
                "☼  {   ☼" +
                "☼      ☼" +
                "☼######☼" +
                "☼☼☼☼☼☼☼☼");

        tick();
        tick();

        events.verifyAllEvents("[KILL_HERO]");

        assertE("☼☼☼☼☼☼☼☼" +
                "☼      ☼" +
                "☼      ☼" +
                "☼      ☼" +
                "☼  Ѡ   ☼" +
                "☼      ☼" +
                "☼######☼" +
                "☼☼☼☼☼☼☼☼");

        robber().down();
        dice(2, 3);
        field.newGame(player());
        dice(0);  // охотимся за первым игроком
        tick();

        assertE("☼☼☼☼☼☼☼☼" +
                "☼      ☼" +
                "☼      ☼" +
                "☼      ☼" +
                "☼  ~   ☼" +
                "☼ ►«   ☼" +
                "☼######☼" +
                "☼☼☼☼☼☼☼☼");
    }

    // Чертик проваливается в яму за героем и там его находит
    @Test
    public void shouldRobberFindHeroAtPit() {
        givenFl("☼☼☼☼☼☼☼☼" +
                "☼      ☼" +
                "☼      ☼" +
                "☼      ☼" +
                "☼      ☼" +
                "☼  « ◄ ☼" +
                "☼######☼" +
                "☼☼☼☼☼☼☼☼");

        hero().act();
        tick();

        assertE("☼☼☼☼☼☼☼☼" +
                "☼      ☼" +
                "☼      ☼" +
                "☼      ☼" +
                "☼      ☼" +
                "☼  « Я ☼" +
                "☼###*##☼" +
                "☼☼☼☼☼☼☼☼");

        hero().left();
        tick();
        tick();

        assertE("☼☼☼☼☼☼☼☼" +
                "☼      ☼" +
                "☼      ☼" +
                "☼      ☼" +
                "☼      ☼" +
                "☼  «   ☼" +
                "☼###◄##☼" +
                "☼☼☼☼☼☼☼☼");

        robber().right();
        tick();
        tick();

        events.verifyAllEvents("[KILL_HERO]");

        assertE("☼☼☼☼☼☼☼☼" +
                "☼      ☼" +
                "☼      ☼" +
                "☼      ☼" +
                "☼      ☼" +
                "☼      ☼" +
                "☼###Ѡ##☼" +
                "☼☼☼☼☼☼☼☼");

        dice(2, 3);
        field.newGame(player());
        dice(0);  // охотимся за первым игроком
        tick();

        assertE("☼☼☼☼☼☼☼☼" +
                "☼      ☼" +
                "☼      ☼" +
                "☼      ☼" +
                "☼      ☼" +
                "☼ ►    ☼" +
                "☼###⍈##☼" +
                "☼☼☼☼☼☼☼☼");

        for (int c = 5; c < Brick.CRACK_TIMER; c++) { // враг вылазит
            tick();
        }

        assertE("☼☼☼☼☼☼☼☼" +
                "☼      ☼" +
                "☼      ☼" +
                "☼      ☼" +
                "☼      ☼" +
                "☼ ► »  ☼" +
                "☼######☼" +
                "☼☼☼☼☼☼☼☼");

    }

    @Test
    public void iLooseScoresWhenDoHarakiri() {
        givenFl("☼☼☼☼☼" +
                "☼   ☼" +
                "☼ ◄ ☼" +
                "☼###☼" +
                "☼☼☼☼☼");

        hero().act(0);
        tick();

        assertE("☼☼☼☼☼" +
                "☼   ☼" +
                "☼ Ѡ ☼" +
                "☼###☼" +
                "☼☼☼☼☼");

        events.verifyAllEvents("[KILL_HERO, SUICIDE]");
    }

    @Test
    public void iCanJumpThroughBackways() {
        settings.integer(BACKWAYS_COUNT, 2);

        dice(1, 2,
            3, 3);
        givenFl("☼☼☼☼☼" +
                "☼  ⊛☼" +
                "☼⊛◄ ☼" +
                "☼###☼" +
                "☼☼☼☼☼");

        hero().left();
        tick();

        assertE("☼☼☼☼☼" +
                "☼  ]☼" +
                "☼⊛  ☼" +
                "☼###☼" +
                "☼☼☼☼☼");

        tick();

        assertE("☼☼☼☼☼" +
                "☼  ⊛☼" +
                "☼⊛ ◄☼" +
                "☼###☼" +
                "☼☼☼☼☼");

        tick();

        assertE("☼☼☼☼☼" +
                "☼  ⊛☼" +
                "☼⊛ ◄☼" +
                "☼###☼" +
                "☼☼☼☼☼");
    }

    @Test
    public void backwaysAreRecreatedEveryFewTicks() {
        settings.integer(BACKWAYS_COUNT, 2)
                .integer(BACKWAY_TICKS, 5);

        givenFl("☼☼☼☼☼" +
                "☼  ⊛☼" +
                "☼⊛◄ ☼" +
                "☼###☼" +
                "☼☼☼☼☼");

        assertEquals(5, field.getBackwaysTimer());

        hero().left();
        tick();

        assertEquals(4, field.getBackwaysTimer());

        assertE("☼☼☼☼☼" +
                "☼  ]☼" +
                "☼⊛  ☼" +
                "☼###☼" +
                "☼☼☼☼☼");

        tick();

        assertEquals(3, field.getBackwaysTimer());

        assertE("☼☼☼☼☼" +
                "☼  ⊛☼" +
                "☼⊛ ◄☼" +
                "☼###☼" +
                "☼☼☼☼☼");

        tick();

        assertEquals(2, field.getBackwaysTimer());

        tick();

        assertEquals(1, field.getBackwaysTimer());

        tick();

        assertEquals(0, field.getBackwaysTimer());

        assertE("☼☼☼☼☼" +
                "☼  ⊛☼" +
                "☼⊛ ◄☼" +
                "☼###☼" +
                "☼☼☼☼☼");

        dice(1, 3,  // new backways
            2, 3);
        tick();

        assertEquals(5, field.getBackwaysTimer());

        assertE("☼☼☼☼☼" +
                "☼⊛⊛ ☼" +
                "☼  ◄☼" +
                "☼###☼" +
                "☼☼☼☼☼");
    }

    @Test
    public void shouldResetHeroAndRobber_whenClearBoard() {
        // given
        givenFl("☼☼☼☼☼☼☼☼" +
                "☼      ☼" +
                "☼      ☼" +
                "☼      ☼" +
                "☼      ☼" +
                "☼  « ◄ ☼" +
                "☼######☼" +
                "☼☼☼☼☼☼☼☼");

        tick();

        assertE("☼☼☼☼☼☼☼☼" +
                "☼      ☼" +
                "☼      ☼" +
                "☼      ☼" +
                "☼      ☼" +
                "☼  « ◄ ☼" +
                "☼######☼" +
                "☼☼☼☼☼☼☼☼");

        robber().left();
        tick();

        assertE("☼☼☼☼☼☼☼☼" +
                "☼      ☼" +
                "☼      ☼" +
                "☼      ☼" +
                "☼      ☼" +
                "☼ «  ◄ ☼" +
                "☼######☼" +
                "☼☼☼☼☼☼☼☼");

        // when
        dice(1, 2); // new hero coordinates
        field.clearScore();

        // then
        assertE("☼☼☼☼☼☼☼☼" +
                "☼      ☼" +
                "☼      ☼" +
                "☼      ☼" +
                "☼      ☼" +
                "☼► «   ☼" +
                "☼######☼" +
                "☼☼☼☼☼☼☼☼");
    }

    @Test
    public void shouldResetHeroScores_whenClearBoard() {
        // given
        Brick.CRACK_TIMER = 4;

        givenFl("☼☼☼☼☼☼☼☼" +
                "☼      ☼" +
                "☼      ☼" +
                "☼      ☼" +
                "☼      ☼" +
                "☼  ◄ ◄ ☼" +
                "☼######☼" +
                "☼☼☼☼☼☼☼☼");

        tick();

        assertE("☼☼☼☼☼☼☼☼" +
                "☼      ☼" +
                "☼      ☼" +
                "☼      ☼" +
                "☼      ☼" +
                "☼  ◄ ) ☼" +
                "☼######☼" +
                "☼☼☼☼☼☼☼☼");

        hero(0).act();
        hero(0).right();
        hero(1).left();
        tick();

        assertE("☼☼☼☼☼☼☼☼" +
                "☼      ☼" +
                "☼      ☼" +
                "☼      ☼" +
                "☼      ☼" +
                "☼  R⊐  ☼" +
                "☼###*##☼" +
                "☼☼☼☼☼☼☼☼");

        tick();

        assertE("☼☼☼☼☼☼☼☼" +
                "☼      ☼" +
                "☼      ☼" +
                "☼      ☼" +
                "☼      ☼" +
                "☼  R   ☼" +
                "☼###)##☼" +
                "☼☼☼☼☼☼☼☼");

        tick();

        events.verifyNoEvents();

        assertEquals(0, hero(0).scores());
        assertEquals(0, hero(1).scores());
        assertEquals(true, hero(0).isAlive());
        assertEquals(true, hero(1).isAlive());

        assertE("☼☼☼☼☼☼☼☼" +
                "☼      ☼" +
                "☼      ☼" +
                "☼      ☼" +
                "☼      ☼" +
                "☼  R   ☼" +
                "☼###)##☼" +
                "☼☼☼☼☼☼☼☼");

        tick();

        events.verifyAllEvents("" +
                "listener(0) => [KILL_ROBBER]\n" +
                "listener(1) => [KILL_HERO]\n");

        assertEquals(1, hero(0).scores());
        assertEquals(0, hero(1).scores());
        assertEquals(true, hero(0).isAlive());
        assertEquals(false, hero(1).isAlive());

        assertE("☼☼☼☼☼☼☼☼" +
                "☼      ☼" +
                "☼      ☼" +
                "☼      ☼" +
                "☼      ☼" +
                "☼  R   ☼" +
                "☼###Z##☼" +
                "☼☼☼☼☼☼☼☼");

        // when
        dice(1, 2,
            6, 2);
        field.clearScore();

        // then
        assertEquals(0, hero(0).scores());
        assertEquals(0, hero(1).scores());
        assertEquals(true, hero(0).isAlive());
        assertEquals(true, hero(1).isAlive());

        assertE("☼☼☼☼☼☼☼☼" +
                "☼      ☼" +
                "☼      ☼" +
                "☼      ☼" +
                "☼      ☼" +
                "☼►    (☼" +
                "☼######☼" +
                "☼☼☼☼☼☼☼☼");
    }

    @Test
    public void shouldCollectAllClue() {
        // given
        givenFl("☼☼☼☼☼☼☼☼" +
                "☼      ☼" +
                "☼      ☼" +
                "☼      ☼" +
                "☼      ☼" +
                "☼►$$&@@☼" +
                "☼######☼" +
                "☼☼☼☼☼☼☼☼");

        tick();

        assertE("☼☼☼☼☼☼☼☼" +
                "☼      ☼" +
                "☼      ☼" +
                "☼      ☼" +
                "☼      ☼" +
                "☼►$$&@@☼" +
                "☼######☼" +
                "☼☼☼☼☼☼☼☼");

        hero().right();
        tick();

        events.verifyAllEvents("[GET_KNIFE_CLUE]");
        events.verifyNoEvents();
        listeners.forEach(Mockito::reset);

        assertE("☼☼☼☼☼☼☼☼" +
                "☼      ☼" +
                "☼      ☼" +
                "☼      ☼" +
                "☼      ☼" +
                "☼ ►$&@@☼" +
                "☼######☼" +
                "☼☼☼☼☼☼☼☼");

        hero().right();
        tick();

        events.verifyAllEvents("[GET_KNIFE_CLUE]");
        events.verifyNoEvents();
        listeners.forEach(Mockito::reset);

        assertE("☼☼☼☼☼☼☼☼" +
                "☼      ☼" +
                "☼      ☼" +
                "☼      ☼" +
                "☼      ☼" +
                "☼  ►&@@☼" +
                "☼######☼" +
                "☼☼☼☼☼☼☼☼");

        hero().right();
        tick();

        events.verifyAllEvents("[GET_GLOVE_CLUE]");
        events.verifyNoEvents();
        listeners.forEach(Mockito::reset);

        assertE("☼☼☼☼☼☼☼☼" +
                "☼      ☼" +
                "☼      ☼" +
                "☼      ☼" +
                "☼      ☼" +
                "☼   ►@@☼" +
                "☼######☼" +
                "☼☼☼☼☼☼☼☼");

        hero().right();
        tick();

        events.verifyAllEvents("[GET_RING_CLUE]");
        events.verifyNoEvents();
        listeners.forEach(Mockito::reset);

        assertE("☼☼☼☼☼☼☼☼" +
                "☼      ☼" +
                "☼      ☼" +
                "☼      ☼" +
                "☼      ☼" +
                "☼    ►@☼" +
                "☼######☼" +
                "☼☼☼☼☼☼☼☼");

        hero().right();
        tick();

        events.verifyAllEvents("[GET_RING_CLUE]");
        events.verifyNoEvents();
        listeners.forEach(Mockito::reset);

        assertE("☼☼☼☼☼☼☼☼" +
                "☼      ☼" +
                "☼      ☼" +
                "☼      ☼" +
                "☼      ☼" +
                "☼     ►☼" +
                "☼######☼" +
                "☼☼☼☼☼☼☼☼");
    }

    @Test
    public void shouldResetClue_whenClearBoard() {
        // given
        shouldCollectAllClue();

        // when
        dice(1, 2);
        field.clearScore();

        assertE("☼☼☼☼☼☼☼☼" +
                "☼      ☼" +
                "☼      ☼" +
                "☼      ☼" +
                "☼      ☼" +
                "☼►$$&@@☼" +
                "☼######☼" +
                "☼☼☼☼☼☼☼☼");

        // when
        // добавим еще улик
        settings.integer(CLUE_COUNT_KNIFE, settings.integer(CLUE_COUNT_KNIFE) + 2)
                .integer(CLUE_COUNT_RING,    settings.integer(CLUE_COUNT_RING) + 3)
                .integer(CLUE_COUNT_GLOVE,  settings.integer(CLUE_COUNT_GLOVE) + 1);
        dice(
            2, 3, // knife
            3, 3, // knife
            4, 3, // glove
            5, 3, // ring
            6, 3, // ring
            6, 4, // ring
            1, 6  // герой
        );
        field.clearScore();

        assertE("☼☼☼☼☼☼☼☼" +
                "☼[     ☼" +
                "☼      ☼" +
                "☼     @☼" +
                "☼ $$&@@☼" +
                "☼ $$&@@☼" +
                "☼######☼" +
                "☼☼☼☼☼☼☼☼");

        // when
        // удалим улики
        settings.integer(CLUE_COUNT_KNIFE, 1)
                .integer(CLUE_COUNT_RING, 1)
                .integer(CLUE_COUNT_GLOVE, 1);

        dice(2, 6);  // герой
        field.clearScore();

        assertE("☼☼☼☼☼☼☼☼" +
                "☼ [    ☼" +
                "☼      ☼" +
                "☼      ☼" +
                "☼      ☼" +
                "☼ $ &@ ☼" +
                "☼######☼" +
                "☼☼☼☼☼☼☼☼");
    }

    @Test
    public void shouldEndlesslyWalkThroughTheBackways_untilExit() {
        // given
        settings.integer(BACKWAYS_COUNT, 3);

        givenFl("☼☼☼☼☼☼☼☼" +
                "☼   ⊛  ☼" +
                "☼      ☼" +
                "☼   ⊛  ☼" +
                "☼      ☼" +
                "☼►⊛    ☼" +
                "☼######☼" +
                "☼☼☼☼☼☼☼☼");

        tick();

        assertE("☼☼☼☼☼☼☼☼" +
                "☼   ⊛  ☼" +
                "☼      ☼" +
                "☼   ⊛  ☼" +
                "☼      ☼" +
                "☼►⊛    ☼" +
                "☼######☼" +
                "☼☼☼☼☼☼☼☼");

        hero().right();
        tick();

        assertE("☼☼☼☼☼☼☼☼" +
                "☼   [  ☼" +
                "☼      ☼" +
                "☼   ⊛  ☼" +
                "☼      ☼" +
                "☼ ⊛    ☼" +
                "☼######☼" +
                "☼☼☼☼☼☼☼☼");

        tick();

        assertE("☼☼☼☼☼☼☼☼" +
                "☼   ⊛  ☼" +
                "☼   [  ☼" +
                "☼   ⊛  ☼" +
                "☼      ☼" +
                "☼ ⊛    ☼" +
                "☼######☼" +
                "☼☼☼☼☼☼☼☼");

        tick();

        assertE("☼☼☼☼☼☼☼☼" +
                "☼   ⊛  ☼" +
                "☼      ☼" +
                "☼   ⊛  ☼" +
                "☼      ☼" +
                "☼ ►    ☼" +
                "☼######☼" +
                "☼☼☼☼☼☼☼☼");

        // что интересно, если ты не выйдешь из черный хода то в следующий тик отправишься дальше
        tick();

        assertE("☼☼☼☼☼☼☼☼" +
                "☼   [  ☼" +
                "☼      ☼" +
                "☼   ⊛  ☼" +
                "☼      ☼" +
                "☼ ⊛    ☼" +
                "☼######☼" +
                "☼☼☼☼☼☼☼☼");

        tick();

        assertE("☼☼☼☼☼☼☼☼" +
                "☼   ⊛  ☼" +
                "☼   [  ☼" +
                "☼   ⊛  ☼" +
                "☼      ☼" +
                "☼ ⊛    ☼" +
                "☼######☼" +
                "☼☼☼☼☼☼☼☼");

        tick();

        assertE("☼☼☼☼☼☼☼☼" +
                "☼   ⊛  ☼" +
                "☼      ☼" +
                "☼   ⊛  ☼" +
                "☼      ☼" +
                "☼ ►    ☼" +
                "☼######☼" +
                "☼☼☼☼☼☼☼☼");

        hero().right();
        tick();

        assertE("☼☼☼☼☼☼☼☼" +
                "☼   ⊛  ☼" +
                "☼      ☼" +
                "☼   ⊛  ☼" +
                "☼      ☼" +
                "☼ ⊛►   ☼" +
                "☼######☼" +
                "☼☼☼☼☼☼☼☼");
    }

    @Test
    public void shouldResetBackways_whenClearBoard() {
        shouldEndlesslyWalkThroughTheBackways_untilExit();

        assertE("☼☼☼☼☼☼☼☼" +
                "☼   ⊛  ☼" +
                "☼      ☼" +
                "☼   ⊛  ☼" +
                "☼      ☼" +
                "☼ ⊛►   ☼" +
                "☼######☼" +
                "☼☼☼☼☼☼☼☼");

        // when
        // добавим еще один черный ход
        settings.integer(BACKWAYS_COUNT, settings.integer(BACKWAYS_COUNT) + 1);
        dice(2, 4, // new backway
            1, 2); // hero
        field.clearScore();

        assertE("☼☼☼☼☼☼☼☼" +
                "☼   ⊛  ☼" +
                "☼      ☼" +
                "☼ ⊛ ⊛  ☼" +
                "☼      ☼" +
                "☼►⊛    ☼" +
                "☼######☼" +
                "☼☼☼☼☼☼☼☼");

        // when
        // оставим два черный хода
        settings.integer(BACKWAYS_COUNT, 2);
        dice(1, 2); // hero
        field.clearScore();

        assertE("☼☼☼☼☼☼☼☼" +
                "☼   ⊛  ☼" +
                "☼      ☼" +
                "☼   ⊛  ☼" +
                "☼      ☼" +
                "☼►     ☼" +
                "☼######☼" +
                "☼☼☼☼☼☼☼☼");
    }

    @Test
    public void shouldResetPotions_whenClearBoard() {
        // given
        settings.integer(MASK_POTIONS_COUNT, 3);

        givenFl("☼☼☼☼☼☼☼☼" +
                "☼      ☼" +
                "☼      ☼" +
                "☼      ☼" +
                "☼      ☼" +
                "☼►S S S☼" +
                "☼######☼" +
                "☼☼☼☼☼☼☼☼");

        tick();

        assertE("☼☼☼☼☼☼☼☼" +
                "☼      ☼" +
                "☼      ☼" +
                "☼      ☼" +
                "☼      ☼" +
                "☼►S S S☼" +
                "☼######☼" +
                "☼☼☼☼☼☼☼☼");

        assertEquals(false, hero().under(Potion.PotionType.MASK_POTION));

        hero().right();
        tick();

        hero().right();
        tick();

        hero().right();
        tick();

        hero().right();
        tick();

        hero().right();
        tick();

        assertE("☼☼☼☼☼☼☼☼" +
                "☼      ☼" +
                "☼      ☼" +
                "☼      ☼" +
                "☼      ☼" +
                "☼     ⊳☼" +
                "☼######☼" +
                "☼☼☼☼☼☼☼☼");

        assertEquals(true, hero().under(Potion.PotionType.MASK_POTION));

        // when
        // почистим все
        dice(1, 2);  // hero
        field.clearScore();

        assertEquals(false, hero().under(Potion.PotionType.MASK_POTION));

        assertE("☼☼☼☼☼☼☼☼" +
                "☼      ☼" +
                "☼      ☼" +
                "☼      ☼" +
                "☼      ☼" +
                "☼►S S S☼" +
                "☼######☼" +
                "☼☼☼☼☼☼☼☼");

        // when
        // добавим еще
        settings.integer(MASK_POTIONS_COUNT, settings.integer(MASK_POTIONS_COUNT) + 2);
        dice(
                3, 3, // new potion
                5, 3,
                1, 6  // hero
        );
        field.clearScore();

        assertE("☼☼☼☼☼☼☼☼" +
                "☼[     ☼" +
                "☼      ☼" +
                "☼      ☼" +
                "☼  S S ☼" +
                "☼ S S S☼" +
                "☼######☼" +
                "☼☼☼☼☼☼☼☼");

        // when
        // оставим только 1
        settings.integer(MASK_POTIONS_COUNT, 1);
        dice(1, 2);  // hero
        field.clearScore();

        assertE("☼☼☼☼☼☼☼☼" +
                "☼      ☼" +
                "☼      ☼" +
                "☼      ☼" +
                "☼      ☼" +
                "☼►S    ☼" +
                "☼######☼" +
                "☼☼☼☼☼☼☼☼");
    }

    @Test
    public void shouldRemoveOldWalls_whenClearBoard() {
        // given
        givenFl("☼☼☼☼☼☼☼☼" +
                "☼      ☼" +
                "☼      ☼" +
                "☼      ☼" +
                "☼      ☼" +
                "☼     ►☼" +
                "☼######☼" +
                "☼☼☼☼☼☼☼☼");

        tick();

        assertE("☼☼☼☼☼☼☼☼" +
                "☼      ☼" +
                "☼      ☼" +
                "☼      ☼" +
                "☼      ☼" +
                "☼     ►☼" +
                "☼######☼" +
                "☼☼☼☼☼☼☼☼");

        assertEquals(7 * 4, field.borders().size());

        // when
        dice(1, 2); // new hero
        field.clearScore();

        // then
        assertEquals(7 * 4, field.borders().size());

        assertE("☼☼☼☼☼☼☼☼" +
                "☼      ☼" +
                "☼      ☼" +
                "☼      ☼" +
                "☼      ☼" +
                "☼►     ☼" +
                "☼######☼" +
                "☼☼☼☼☼☼☼☼");

        // when
        dice(2, 2); // new hero
        field.clearScore();

        // then
        assertEquals(7 * 4, field.borders().size());

        assertE("☼☼☼☼☼☼☼☼" +
                "☼      ☼" +
                "☼      ☼" +
                "☼      ☼" +
                "☼      ☼" +
                "☼ ►    ☼" +  // TODO героя приходится смещать, потому что при очистке его прошлое место занято им самим
                "☼######☼" +
                "☼☼☼☼☼☼☼☼");
    }

    @Test
    public void shouldResetBackwaysTimeout_whenClearBoard() {
        backwaysAreRecreatedEveryFewTicks();

        assertE("☼☼☼☼☼" +
                "☼⊛⊛ ☼" +
                "☼  ◄☼" +
                "☼###☼" +
                "☼☼☼☼☼");

        assertEquals(5, field.getBackwaysTimer());

        tick();
        tick();
        tick();

        assertEquals(2, field.getBackwaysTimer());

        // when
        dice(3, 3, // new backways
            3, 2,
            1, 2); // new hero
        field.clearScore();

        // then
        assertEquals(5, field.getBackwaysTimer());
    }

    // прострелить находясь на трубе нельзя, в оригинале только находясь на краю трубы

    // карта намного больше, чем квардартик вьюшка, и я подходя к границе просто передвигаю вьюшку
    // повляется многопользовательский режим игры в формате "стенка на стенку"

}
