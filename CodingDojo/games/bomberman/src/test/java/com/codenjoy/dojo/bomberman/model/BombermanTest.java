package com.codenjoy.dojo.bomberman.model;

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


import com.codenjoy.dojo.bomberman.services.Events;
import com.codenjoy.dojo.services.*;
import com.codenjoy.dojo.services.settings.SimpleParameter;
import org.junit.Assert;
import org.junit.Test;

import java.util.Iterator;
import java.util.List;

import static com.codenjoy.dojo.services.PointImpl.pt;
import static com.codenjoy.dojo.services.settings.SimpleParameter.v;
import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;

public class BombermanTest extends AbstractBombermanTest {

    @Test
    public void shouldBoard_whenStartGame() {
        when(settings.getBoardSize()).thenReturn(v(10));

        Bomberman board = new Bomberman(settings);

        assertEquals(10, board.size());
    }

    @Test
    public void shouldBoard_whenStartGame2() {
        assertEquals(SIZE, field.size());
    }

    @Test
    public void shouldHeroOnBoardAtInitPos_whenGameStart() {
        asrtBrd("     \n" +
                "     \n" +
                "     \n" +
                "     \n" +
                "☺    \n");
    }

    @Test
    public void shouldHeroOnBoardOneRightStep_whenCallRightCommand() {
        hero.right();
        field.tick();

        asrtBrd("     \n" +
                "     \n" +
                "     \n" +
                "     \n" +
                " ☺   \n");
    }

    @Test
    public void shouldHeroOnBoardTwoRightSteps_whenCallRightCommandTwice() {
        hero.right();
        field.tick();

        hero.right();
        field.tick();

        asrtBrd("     \n" +
                "     \n" +
                "     \n" +
                "     \n" +
                "  ☺  \n");
    }

    @Test
    public void shouldHeroOnBoardOneUpStep_whenCallDownCommand() {
        hero.up();
        field.tick();

        asrtBrd("     \n" +
                "     \n" +
                "     \n" +
                "☺    \n" +
                "     \n");
    }

    @Test
    public void shouldHeroWalkUp() {
        hero.up();
        field.tick();

        hero.up();
        field.tick();

        hero.down();
        field.tick();

        asrtBrd("     \n" +
                "     \n" +
                "     \n" +
                "☺    \n" +
                "     \n");
    }

    @Test
    public void shouldHeroStop_whenGoToWallDown() {
        hero.down();
        field.tick();

        asrtBrd("     \n" +
                "     \n" +
                "     \n" +
                "     \n" +
                "☺    \n");
    }

    @Test
    public void shouldHeroWalkLeft() {
        hero.right();
        field.tick();

        hero.right();
        field.tick();

        hero.left();
        field.tick();

        asrtBrd("     \n" +
                "     \n" +
                "     \n" +
                "     \n" +
                " ☺   \n");
    }

    @Test
    public void shouldHeroStop_whenGoToWallLeft() {
        hero.left();
        field.tick();

        asrtBrd("     \n" +
                "     \n" +
                "     \n" +
                "     \n" +
                "☺    \n");
    }

    @Test
    public void shouldHeroStop_whenGoToWallRight() {
        gotoMaxRight();

        asrtBrd("     \n" +
                "     \n" +
                "     \n" +
                "     \n" +
                "    ☺\n");
    }

    @Test
    public void shouldHeroStop_whenGoToWallUp() {
        gotoMaxUp();

        asrtBrd("☺    \n" +
                "     \n" +
                "     \n" +
                "     \n" +
                "     \n");
    }

    @Test
    public void shouldHeroMovedOncePerTact() {
        hero.down();
        hero.up();
        hero.left();
        hero.right();
        field.tick();

        asrtBrd("     \n" +
                "     \n" +
                "     \n" +
                "     \n" +
                " ☺   \n");

        hero.right();
        hero.left();
        hero.down();
        hero.up();
        field.tick();

        asrtBrd("     \n" +
                "     \n" +
                "     \n" +
                " ☺   \n" +
                "     \n");
    }

    @Test
    public void shouldBombDropped_whenHeroDropBomb() {
        hero.act();
        field.tick();

        asrtBrd("     \n" +
                "     \n" +
                "     \n" +
                "     \n" +
                "☻    \n");
    }

    @Test
    public void shouldBombDropped_whenHeroDropBombAtAnotherPlace() {
        hero.up();
        field.tick();

        hero.right();
        field.tick();

        hero.act();
        field.tick();

        asrtBrd("     \n" +
                "     \n" +
                "     \n" +
                " ☻   \n" +
                "     \n");
    }

    @Test
    public void shouldBombsDropped_whenHeroDropThreeBomb() {
        canDropBombs(3);

        hero.up();
        field.tick();

        hero.act();
        field.tick();

        hero.right();
        field.tick();

        hero.act();
        field.tick();

        asrtBrd("     \n" +
                "     \n" +
                "     \n" +
                "2☻   \n" +
                "     \n");
    }

    // проверить, что бомбермен не может бомб дропать больше, чем у него в level прописано
    @Test
    public void shouldOnlyTwoBombs_whenLevelApproveIt() {
        canDropBombs(2);

        asrtBrd("     \n" +
                "     \n" +
                "     \n" +
                "     \n" +
                "☺    \n");

        hero.up();
        hero.act();
        field.tick();

        asrtBrd("     \n" +
                "     \n" +
                "     \n" +
                "☻    \n" +
                "     \n");

        hero.up();
        hero.act();
        field.tick();

        asrtBrd("     \n" +
                "     \n" +
                "☻    \n" +
                "3    \n" +
                "     \n");

        hero.up();
        hero.act();
        field.tick();

        asrtBrd("     \n" +
                "☺    \n" +
                "3    \n" +
                "2    \n" +
                "     \n");
    }

    // бомберен не может дропать два бомбы на одно место
    @Test
    public void shouldOnlyOneBombPerPlace() {
        canDropBombs(2);

        hero.act();
        field.tick();

        hero.act();
        field.tick();

        asrtBrd("     \n" +
                "     \n" +
                "     \n" +
                "     \n" +
                "☻    \n");

        assertEquals(1, field.bombs().size());

        hero.right();
        field.tick();

        asrtBrd("     \n" +
                "     \n" +
                "     \n" +
                "     \n" +
                "2☺   \n");

        hero.right();
        field.tick();

        asrtBrd("     \n" +
                "     \n" +
                "     \n" +
                "     \n" +
                "1 ☺  \n");

        field.tick();

        asrtBrd("     \n" +
                "     \n" +
                "     \n" +
                "     \n" +
                "҉ ☺  \n");

        field.tick();   // бомб больше нет, иначе тут был бы взрыв второй

        asrtBrd("     \n" +
                "     \n" +
                "     \n" +
                "     \n" +
                "  ☺  \n");
    }

    @Test
    public void shouldBoom_whenDroppedBombHas5Ticks() {
        hero.act();
        field.tick();

        hero.right();
        field.tick();

        hero.right();
        field.tick();

        field.tick();

        asrtBrd("     \n" +
                "     \n" +
                "     \n" +
                "     \n" +
                "1 ☺  \n");

        field.tick();

        asrtBrd("     \n" +
                "     \n" +
                "     \n" +
                "҉    \n" +
                "҉҉☺  \n");
    }

    // проверить, что я могу поставить еще одну бомбу, когда другая рванула
    @Test
    public void shouldCanDropNewBomb_whenOtherBoom() {
        shouldBoom_whenDroppedBombHas5Ticks();

        asrtBrd("     \n" +
                "     \n" +
                "     \n" +
                "҉    \n" +
                "҉҉☺  \n");

        hero.act();
        field.tick();

        asrtBrd("     \n" +
                "     \n" +
                "     \n" +
                "     \n" +
                "  ☻  \n");
    }

    // если бомбермен стоит на бомбе то он умирает после ее взрыва
    @Test
    public void shouldKillHero_whenBombExploded() {
        hero.act();
        hero.right();
        field.tick();

        field.tick();
        field.tick();

        field.tick();

        assertHeroAlive();
        asrtBrd("     \n" +
                "     \n" +
                "     \n" +
                "     \n" +
                "1☺   \n");

        field.tick();

        asrtBrd("     \n" +
                "     \n" +
                "     \n" +
                "҉    \n" +
                "҉Ѡ   \n");

        verifyAllEvents("[DIED]");
        assertHeroDie();

        field.tick();

        asrtBrd("     \n" +
                "     \n" +
                "     \n" +
                "     \n" +
                " Ѡ   \n");

        verifyAllEvents("[]");
        assertHeroDie();
    }

    // после смерти ходить больше нельзя
    @Test
    public void shouldException_whenTryToMoveIfDead_goLeft() {
        killBomber();

        hero.left();
        field.tick();

        asrtBrd("     \n" +
                "     \n" +
                "     \n" +
                " Ѡ   \n" +
                "     \n");
    }

    private void killBomber() {
        hero.up();
        field.tick();

        hero.right();
        hero.act();
        field.tick();

        field.tick();
        field.tick();
        field.tick();
        field.tick();

        asrtBrd("     \n" +
                "     \n" +
                " ҉   \n" +
                "҉Ѡ҉  \n" +
                " ҉   \n");
    }

    @Test
    public void shouldException_whenTryToMoveIfDead_goUp() {
        killBomber();

        hero.up();
        field.tick();

        asrtBrd("     \n" +
                "     \n" +
                "     \n" +
                " Ѡ   \n" +
                "     \n");
    }

    @Test
    public void shouldException_whenTryToMoveIfDead_goDown() {
        killBomber();

        hero.down();
        field.tick();

        asrtBrd("     \n" +
                "     \n" +
                "     \n" +
                " Ѡ   \n" +
                "     \n");
    }

    @Test
    public void shouldException_whenTryToMoveIfDead_goRight() {
        killBomber();

        hero.right();
        field.tick();

        asrtBrd("     \n" +
                "     \n" +
                "     \n" +
                " Ѡ   \n" +
                "     \n");
    }

    @Test
    public void shouldException_whenTryToMoveIfDead_dropBomb() {
        killBomber();

        hero.act();
        field.tick();

        asrtBrd("     \n" +
                "     \n" +
                "     \n" +
                " Ѡ   \n" +
                "     \n");
    }

    // если бомбермен стоит под действием ударной волны, он умирает
    @Test
    public void shouldKillHero_whenBombExploded_blastWaveAffect_fromLeft() {
        hero.act();
        field.tick();

        hero.right();
        field.tick();

        field.tick();

        field.tick();

        asrtBrd("     \n" +
                "     \n" +
                "     \n" +
                "     \n" +
                "1☺   \n");
        assertHeroAlive();

        field.tick();

        asrtBrd("     \n" +
                "     \n" +
                "     \n" +
                "҉    \n" +
                "҉Ѡ   \n");

        verifyAllEvents("[DIED]");
        assertHeroDie();

        field.tick();

        asrtBrd("     \n" +
                "     \n" +
                "     \n" +
                "     \n" +
                " Ѡ   \n");

        verifyAllEvents("[]");
        assertHeroDie();
    }

    @Test
    public void shouldKillHero_whenBombExploded_blastWaveAffect_fromRight() {
        hero.right();
        field.tick();

        hero.act();
        field.tick();

        hero.left();
        field.tick();

        field.tick();
        field.tick();

        asrtBrd("     \n" +
                "     \n" +
                "     \n" +
                "     \n" +
                "☺1   \n");
        assertHeroAlive();

        field.tick();

        asrtBrd("     \n" +
                "     \n" +
                "     \n" +
                " ҉   \n" +
                "Ѡ҉҉  \n");

        verifyAllEvents("[DIED]");
        assertHeroDie();

        field.tick();

        asrtBrd("     \n" +
                "     \n" +
                "     \n" +
                "     \n" +
                "Ѡ    \n");

        verifyAllEvents("[]");
        assertHeroDie();
    }

    @Test
    public void shouldKillHero_whenBombExploded_blastWaveAffect_fromUp() {
        hero.up();
        hero.act();
        field.tick();

        hero.down();
        field.tick();

        field.tick();
        field.tick();

        asrtBrd("     \n" +
                "     \n" +
                "     \n" +
                "1    \n" +
                "☺    \n");
        assertHeroAlive();

        field.tick();

        asrtBrd("     \n" +
                "     \n" +
                "҉    \n" +
                "҉҉   \n" +
                "Ѡ    \n");

        verifyAllEvents("[DIED]");
        assertHeroDie();

        field.tick();

        asrtBrd("     \n" +
                "     \n" +
                "     \n" +
                "     \n" +
                "Ѡ    \n");

        verifyAllEvents("[]");
        assertHeroDie();
    }

    @Test
    public void shouldKillHero_whenBombExploded_blastWaveAffect_fromDown() {
        hero.down();
        field.tick();

        hero.act();
        field.tick();

        hero.up();
        field.tick();

        field.tick();
        field.tick();

        asrtBrd("     \n" +
                "     \n" +
                "     \n" +
                "☺    \n" +
                "1    \n");
        assertHeroAlive();

        field.tick();

        asrtBrd("     \n" +
                "     \n" +
                "     \n" +
                "Ѡ    \n" +
                "҉҉   \n");

        verifyAllEvents("[DIED]");
        assertHeroDie();

        field.tick();

        asrtBrd("     \n" +
                "     \n" +
                "     \n" +
                "Ѡ    \n" +
                "     \n");

        verifyAllEvents("[]");
        assertHeroDie();
    }

    @Test
    public void shouldNoKillHero_whenBombExploded_blastWaveAffect_fromDownRight() {
        hero.down();
        field.tick();

        hero.right();
        field.tick();

        hero.act();
        field.tick();

        hero.up();
        field.tick();

        hero.left();
        field.tick();

        field.tick();

        assertHeroAlive();
        asrtBrd("     \n" +
                "     \n" +
                "     \n" +
                "☺    \n" +
                " 1   \n");

        field.tick();

        assertHeroAlive();
        asrtBrd("     \n" +
                "     \n" +
                "     \n" +
                "☺҉   \n" +
                "҉҉҉  \n");
    }

    @Test
    public void shouldSameHero_whenNetFromBoard() {
        assertSame(hero, game.getJoystick());
    }

    @Test
    public void shouldBlastAfter_whenBombExposed() {
        hero.act();
        field.tick();

        hero.right();
        field.tick();

        hero.right();
        field.tick();

        field.tick();
        field.tick();

        asrtBrd("     \n" +
                "     \n" +
                "     \n" +
                "҉    \n" +
                "҉҉☺  \n");
    }

    @Test
    public void shouldBlastAfter_whenBombExposed_inOtherCorner() {
        gotoMaxUp();
        gotoMaxRight();

        hero.act();
        field.tick();

        hero.left();
        field.tick();

        hero.left();
        field.tick();

        field.tick();
        field.tick();

        asrtBrd("  ☺҉҉\n" +
                "    ҉\n" +
                "     \n" +
                "     \n" +
                "     \n");
    }

    @Test
    public void shouldBlastAfter_whenBombExposed_HeroDie() {
        gotoBoardCenter();

        hero.act();
        field.tick();

        hero.down();
        field.tick();

        field.tick();
        field.tick();
        field.tick();

        asrtBrd("     \n" +
                "  ҉  \n" +
                " ҉҉҉ \n" +
                "  Ѡ  \n" +
                "     \n");

        verifyAllEvents("[DIED]");
        assertHeroDie();

        field.tick();
        asrtBrd("     \n" +
                "     \n" +
                "     \n" +
                "  Ѡ  \n" +
                "     \n");

        verifyAllEvents("[]");
        assertHeroDie();
    }

    // появляются стенки, которые конфигурятся извне
    @Test
    public void shouldHeroNotAtWall() {
        asrtBrd("     \n" +
                "     \n" +
                "     \n" +
                "     \n" +
                "☺    \n");

        givenBoardWithWalls();

        asrtBrd("☼☼☼☼☼\n" +
                "☼   ☼\n" +
                "☼ ☼ ☼\n" +
                "☼☺  ☼\n" +
                "☼☼☼☼☼\n");

    }

    // бомбермен не может пойти вперед на стенку
    @Test
    public void shouldHeroStop_whenUpWall() {
        givenBoardWithWalls();

        hero.down();
        field.tick();

        asrtBrd("☼☼☼☼☼\n" +
                "☼   ☼\n" +
                "☼ ☼ ☼\n" +
                "☼☺  ☼\n" +
                "☼☼☼☼☼\n");
    }

    @Test
    public void shouldHeroStop_whenLeftWall() {
        givenBoardWithWalls();

        hero.left();
        field.tick();

        asrtBrd("☼☼☼☼☼\n" +
                "☼   ☼\n" +
                "☼ ☼ ☼\n" +
                "☼☺  ☼\n" +
                "☼☼☼☼☼\n");
    }

    @Test
    public void shouldHeroStop_whenRightWall() {
        givenBoardWithWalls();

        gotoMaxRight();

        asrtBrd("☼☼☼☼☼\n" +
                "☼   ☼\n" +
                "☼ ☼ ☼\n" +
                "☼  ☺☼\n" +
                "☼☼☼☼☼\n");
    }

    @Test
    public void shouldHeroStop_whenDownWall() {
        givenBoardWithWalls();

        gotoMaxUp();

        asrtBrd("☼☼☼☼☼\n" +
                "☼☺  ☼\n" +
                "☼ ☼ ☼\n" +
                "☼   ☼\n" +
                "☼☼☼☼☼\n");
    }

    private void gotoMaxRight() {
        for (int x = 0; x <= SIZE + 1; x++) {
            hero.right();
            field.tick();
        }
    }

    // бомбермен не может вернуться на место бомбы, она его не пускает как стена
    @Test
    public void shouldHeroStop_whenGotoBomb() {
        hero.act();
        field.tick();

        hero.right();
        field.tick();

        hero.left();
        field.tick();

        asrtBrd("     \n" +
                "     \n" +
                "     \n" +
                "     \n" +
                "2☺   \n");
    }

    // проверить, что бомбермен может одноверменно перемещаться по полю и дропать бомбы за один такт, только как именно?
    @Test
    public void shouldHeroWalkAndDropBombsTogetherInOneTact_bombFirstly() {
        hero.act();
        hero.right();
        field.tick();

        asrtBrd("     \n" +
                "     \n" +
                "     \n" +
                "     \n" +
                "4☺   \n");
    }

    @Test
    public void shouldHeroWalkAndDropBombsTogetherInOneTact_moveFirstly() {
        hero.right();
        hero.act();
        field.tick();

        asrtBrd("     \n" +
                "     \n" +
                "     \n" +
                "     \n" +
                " ☻   \n");

        hero.right();
        field.tick();

        asrtBrd("     \n" +
                "     \n" +
                "     \n" +
                "     \n" +
                " 3☺  \n");
    }

    @Test
    public void shouldHeroWalkAndDropBombsTogetherInOneTact_bombThanMove() {
        hero.act();
        field.tick();

        hero.right();
        field.tick();

        asrtBrd("     \n" +
                "     \n" +
                "     \n" +
                "     \n" +
                "3☺   \n");
    }

    @Test
    public void shouldHeroWalkAndDropBombsTogetherInOneTact_moveThanBomb() {
        hero.right();
        field.tick();

        hero.act();
        field.tick();

        asrtBrd("     \n" +
                "     \n" +
                "     \n" +
                "     \n" +
                " ☻   \n");

        hero.right();
        field.tick();

        asrtBrd("     \n" +
                "     \n" +
                "     \n" +
                "     \n" +
                " 3☺  \n");
    }

    @Test
    public void shouldWallProtectsHero() {
        givenBoardWithOriginalWalls();

        hero.act();
        goOut();

        asrtBrd("☼☼☼☼☼\n" +
                "☼  ☺☼\n" +
                "☼ ☼ ☼\n" +
                "☼1  ☼\n" +
                "☼☼☼☼☼\n");

        field.tick();

        asrtBrd("☼☼☼☼☼\n" +
                "☼  ☺☼\n" +
                "☼҉☼ ☼\n" +
                "☼҉҉ ☼\n" +
                "☼☼☼☼☼\n");

        assertHeroAlive();
    }

    @Test
    public void shouldWallProtectsHero2() {
        assertBombPower(5,
                "☼☼☼☼☼☼☼☼☼\n" +
                "☼       ☼\n" +
                "☼҉☼ ☼ ☼ ☼\n" +
                "☼҉      ☼\n" +
                "☼҉☼ ☼ ☼ ☼\n" +
                "☼҉ ☺    ☼\n" +
                "☼҉☼ ☼ ☼ ☼\n" +
                "☼҉҉҉҉҉҉ ☼\n" +
                "☼☼☼☼☼☼☼☼☼\n");

        assertHeroAlive();
    }

    // проверить, что разрыв бомбы длинной указанной в level
    @Test
    public void shouldChangeBombPower_to2() {
        assertBombPower(2,
                "☼☼☼☼☼☼☼☼☼\n" +
                "☼       ☼\n" +
                "☼ ☼ ☼ ☼ ☼\n" +
                "☼       ☼\n" +
                "☼ ☼ ☼ ☼ ☼\n" +
                "☼҉ ☺    ☼\n" +
                "☼҉☼ ☼ ☼ ☼\n" +
                "☼҉҉҉    ☼\n" +
                "☼☼☼☼☼☼☼☼☼\n");
    }

    @Test
    public void shouldChangeBombPower_to3() {
        assertBombPower(3,
                "☼☼☼☼☼☼☼☼☼\n" +
                "☼       ☼\n" +
                "☼ ☼ ☼ ☼ ☼\n" +
                "☼       ☼\n" +
                "☼҉☼ ☼ ☼ ☼\n" +
                "☼҉ ☺    ☼\n" +
                "☼҉☼ ☼ ☼ ☼\n" +
                "☼҉҉҉҉   ☼\n" +
                "☼☼☼☼☼☼☼☼☼\n");
    }

    @Test
    public void shouldChangeBombPower_to6() {
        assertBombPower(6,
                "☼☼☼☼☼☼☼☼☼\n" +
                "☼҉      ☼\n" +
                "☼҉☼ ☼ ☼ ☼\n" +
                "☼҉      ☼\n" +
                "☼҉☼ ☼ ☼ ☼\n" +
                "☼҉ ☺    ☼\n" +
                "☼҉☼ ☼ ☼ ☼\n" +
                "☼҉҉҉҉҉҉҉☼\n" +
                "☼☼☼☼☼☼☼☼☼\n");
    }

    // я немогу модифицировать список бомб на доске, меняя getBombs
    // но список бомб, что у меня на руках обязательно синхронизирован с теми, что на поле
    @Test
    public void shouldNoChangeOriginalBombsWhenUseBoardApiButTimersSynchronized() {
        canDropBombs(2);
        hero.act();
        hero.right();
        field.tick();
        hero.act();
        hero.right();
        field.tick();

        List<Bomb> bombs1 = field.bombs();
        List<Bomb> bombs2 = field.bombs();
        List<Bomb> bombs3 = field.bombs();
        assertSame(bombs1, bombs2);
        assertSame(bombs2, bombs3);
        assertSame(bombs3, bombs1);

        Bomb bomb11 = bombs1.get(0);
        Bomb bomb12 = bombs2.get(0);
        Bomb bomb13 = bombs3.get(0);
        assertSame(bomb11, bomb12);
        assertSame(bomb12, bomb13);
        assertSame(bomb13, bomb11);

        Bomb bomb21 = bombs1.get(1);
        Bomb bomb22 = bombs2.get(1);
        Bomb bomb23 = bombs3.get(1);
        assertSame(bomb21, bomb22);
        assertSame(bomb22, bomb23);
        assertSame(bomb23, bomb21);

        field.tick();
        field.tick();

        assertFalse(bomb11.isExploded());
        assertFalse(bomb12.isExploded());
        assertFalse(bomb13.isExploded());

        field.tick();

        assertTrue(bomb11.isExploded());
        assertTrue(bomb12.isExploded());
        assertTrue(bomb13.isExploded());

        assertFalse(bomb21.isExploded());
        assertFalse(bomb22.isExploded());
        assertFalse(bomb23.isExploded());

        field.tick();

        assertTrue(bomb21.isExploded());
        assertTrue(bomb22.isExploded());
        assertTrue(bomb23.isExploded());
    }

    @Test
    public void shouldReturnShouldNotSynchronizedBombsList_whenUseBoardApi() {
        hero.act();
        hero.right();
        field.tick();

        List<Bomb> bombs1 = field.bombs();
        assertEquals(1, bombs1.size());

        field.tick();
        field.tick();
        field.tick();
        field.tick();

        List<Bomb> bombs2 = field.bombs();
        assertEquals(0, bombs2.size());
        assertEquals(0, bombs1.size());
        assertSame(bombs1, bombs2);
    }

    @Test
    public void shouldChangeBlast_whenUseBoardApi() {  // TODO а нода вообще такое? стреляет по перформансу перекладывать объекты и усложняет код
        hero.act();
        hero.right();
        field.tick();
        hero.right();
        field.tick();
        field.tick();
        field.tick();
        field.tick();

        List<Blast> blasts1 = field.blasts();
        List<Blast> blasts2 = field.blasts();
        List<Blast> blasts3 = field.blasts();
        assertSame(blasts1, blasts2);
        assertSame(blasts2, blasts3);
        assertSame(blasts3, blasts1);

        Point blast11 = blasts1.get(0);
        Point blast12 = blasts2.get(0);
        Point blast13 = blasts3.get(0);
        assertSame(blast11, blast12);
        assertSame(blast12, blast13);
        assertSame(blast13, blast11);

        Point blast21 = blasts1.get(1);
        Point blast22 = blasts2.get(1);
        Point blast23 = blasts3.get(1);
        assertSame(blast21, blast22);
        assertSame(blast22, blast23);
        assertSame(blast23, blast21);
    }

    @Test
    public void shouldChangeWall_whenUseBoardApi() {
        givenBoardWithWalls();

        Walls walls1 = field.walls();
        Walls walls2 = field.walls();
        Walls walls3 = field.walls();
        assertSame(walls1, walls2);
        assertSame(walls2, walls3);
        assertSame(walls3, walls1);

        Iterator<Wall> iterator1 = walls1.iterator();
        Iterator<Wall> iterator2 = walls2.iterator();
        Iterator<Wall> iterator3 = walls3.iterator();

        Point wall11 = iterator1.next();
        Point wall12 = iterator2.next();
        Point wall13 = iterator3.next();
        assertSame(wall11, wall12);
        assertSame(wall12, wall13);
        assertSame(wall13, wall11);

        Point wall21 = iterator1.next();
        Point wall22 = iterator2.next();
        Point wall23 = iterator3.next();
        assertSame(wall21, wall22);
        assertSame(wall22, wall23);
        assertSame(wall23, wall21);
    }

    // в настройках уровня так же есть и разрущающиеся стены
    @Test
    public void shouldRandomSetDestroyWalls_whenStart() {
        givenBoardWithDestroyWalls();

        asrtBrd("#####\n" +
                "#   #\n" +
                "# # #\n" +
                "#☺  #\n" +
                "#####\n");
    }

    // они взрываются от ударной волны
    @Test
    public void shouldDestroyWallsDestroyed_whenBombExploded() {
        givenBoardWithDestroyWalls();

        hero.act();
        goOut();

        asrtBrd("#####\n" +
                "#  ☺#\n" +
                "# # #\n" +
                "#1  #\n" +
                "#####\n");

        field.tick();

        asrtBrd("#####\n" +
                "#  ☺#\n" +
                "#҉# #\n" +
                "H҉҉ #\n" +
                "#H###\n");
    }

    // появляются чертики, их несоклько за игру
    // каждый такт чертики куда-то рендомно муваются
    // если бомбермен и чертик попали в одну клетку - бомбермен умирает
    @Test
    public void shouldRandomMoveMonster() {
        givenBoardWithMeatChopper(11);

        asrtBrd("☼☼☼☼☼☼☼☼☼☼☼\n" +
                "☼        &☼\n" +
                "☼ ☼ ☼ ☼ ☼ ☼\n" +
                "☼         ☼\n" +
                "☼ ☼ ☼ ☼ ☼ ☼\n" +
                "☼         ☼\n" +
                "☼ ☼ ☼ ☼ ☼ ☼\n" +
                "☼         ☼\n" +
                "☼ ☼ ☼ ☼ ☼ ☼\n" +
                "☼☺        ☼\n" +
                "☼☼☼☼☼☼☼☼☼☼☼\n");

        dice(chopperDice, 1, Direction.DOWN.value());
        field.tick();

        asrtBrd("☼☼☼☼☼☼☼☼☼☼☼\n" +
                "☼         ☼\n" +
                "☼ ☼ ☼ ☼ ☼&☼\n" +
                "☼         ☼\n" +
                "☼ ☼ ☼ ☼ ☼ ☼\n" +
                "☼         ☼\n" +
                "☼ ☼ ☼ ☼ ☼ ☼\n" +
                "☼         ☼\n" +
                "☼ ☼ ☼ ☼ ☼ ☼\n" +
                "☼☺        ☼\n" +
                "☼☼☼☼☼☼☼☼☼☼☼\n");

        dice(chopperDice, 1);
        field.tick();
        field.tick();
        field.tick();
        field.tick();
        field.tick();

        asrtBrd("☼☼☼☼☼☼☼☼☼☼☼\n" +
                "☼         ☼\n" +
                "☼ ☼ ☼ ☼ ☼ ☼\n" +
                "☼         ☼\n" +
                "☼ ☼ ☼ ☼ ☼ ☼\n" +
                "☼         ☼\n" +
                "☼ ☼ ☼ ☼ ☼ ☼\n" +
                "☼        &☼\n" +
                "☼ ☼ ☼ ☼ ☼ ☼\n" +
                "☼☺        ☼\n" +
                "☼☼☼☼☼☼☼☼☼☼☼\n");

        dice(chopperDice, 0, Direction.LEFT.value());
        field.tick();

        asrtBrd("☼☼☼☼☼☼☼☼☼☼☼\n" +
                "☼         ☼\n" +
                "☼ ☼ ☼ ☼ ☼ ☼\n" +
                "☼         ☼\n" +
                "☼ ☼ ☼ ☼ ☼ ☼\n" +
                "☼         ☼\n" +
                "☼ ☼ ☼ ☼ ☼ ☼\n" +
                "☼       & ☼\n" +
                "☼ ☼ ☼ ☼ ☼ ☼\n" +
                "☼☺        ☼\n" +
                "☼☼☼☼☼☼☼☼☼☼☼\n");

        field.tick();
        field.tick();
        field.tick();
        field.tick();
        field.tick();
        field.tick();
        field.tick();

        asrtBrd("☼☼☼☼☼☼☼☼☼☼☼\n" +
                "☼         ☼\n" +
                "☼ ☼ ☼ ☼ ☼ ☼\n" +
                "☼         ☼\n" +
                "☼ ☼ ☼ ☼ ☼ ☼\n" +
                "☼         ☼\n" +
                "☼ ☼ ☼ ☼ ☼ ☼\n" +
                "☼&        ☼\n" +
                "☼ ☼ ☼ ☼ ☼ ☼\n" +
                "☼☺        ☼\n" +
                "☼☼☼☼☼☼☼☼☼☼☼\n");

        dice(chopperDice, 1, Direction.RIGHT.value());
        field.tick();

        asrtBrd("☼☼☼☼☼☼☼☼☼☼☼\n" +
                "☼         ☼\n" +
                "☼ ☼ ☼ ☼ ☼ ☼\n" +
                "☼         ☼\n" +
                "☼ ☼ ☼ ☼ ☼ ☼\n" +
                "☼         ☼\n" +
                "☼ ☼ ☼ ☼ ☼ ☼\n" +
                "☼ &       ☼\n" +
                "☼ ☼ ☼ ☼ ☼ ☼\n" +
                "☼☺        ☼\n" +
                "☼☼☼☼☼☼☼☼☼☼☼\n");

        dice(chopperDice, 0, Direction.LEFT.value());
        field.tick();
        field.tick();

        dice(chopperDice, Direction.LEFT.value());
        field.tick();

        asrtBrd("☼☼☼☼☼☼☼☼☼☼☼\n" +
                "☼         ☼\n" +
                "☼ ☼ ☼ ☼ ☼ ☼\n" +
                "☼         ☼\n" +
                "☼ ☼ ☼ ☼ ☼ ☼\n" +
                "☼         ☼\n" +
                "☼ ☼ ☼ ☼ ☼ ☼\n" +
                "☼&        ☼\n" +
                "☼ ☼ ☼ ☼ ☼ ☼\n" +
                "☼☺        ☼\n" +
                "☼☼☼☼☼☼☼☼☼☼☼\n");

        dice(chopperDice, Direction.DOWN.value());
        field.tick();

        asrtBrd("☼☼☼☼☼☼☼☼☼☼☼\n" +
                "☼         ☼\n" +
                "☼ ☼ ☼ ☼ ☼ ☼\n" +
                "☼         ☼\n" +
                "☼ ☼ ☼ ☼ ☼ ☼\n" +
                "☼         ☼\n" +
                "☼ ☼ ☼ ☼ ☼ ☼\n" +
                "☼         ☼\n" +
                "☼&☼ ☼ ☼ ☼ ☼\n" +
                "☼☺        ☼\n" +
                "☼☼☼☼☼☼☼☼☼☼☼\n");

        field.tick();

        asrtBrd("☼☼☼☼☼☼☼☼☼☼☼\n" +
                "☼         ☼\n" +
                "☼ ☼ ☼ ☼ ☼ ☼\n" +
                "☼         ☼\n" +
                "☼ ☼ ☼ ☼ ☼ ☼\n" +
                "☼         ☼\n" +
                "☼ ☼ ☼ ☼ ☼ ☼\n" +
                "☼         ☼\n" +
                "☼ ☼ ☼ ☼ ☼ ☼\n" +
                "☼Ѡ        ☼\n" +
                "☼☼☼☼☼☼☼☼☼☼☼\n");

        Assert.assertTrue(game.isGameOver());
        verify(listener).event(Events.DIED);
    }


    // чертик умирает, если попадает под взывающуюся бомбу
    @Test
    public void shouldDieMonster_whenBombExploded() {
        SIZE = 11;
        givenBoardWithMeatChopper(SIZE);

        asrtBrd("☼☼☼☼☼☼☼☼☼☼☼\n" +
                "☼        &☼\n" +
                "☼ ☼ ☼ ☼ ☼ ☼\n" +
                "☼         ☼\n" +
                "☼ ☼ ☼ ☼ ☼ ☼\n" +
                "☼         ☼\n" +
                "☼ ☼ ☼ ☼ ☼ ☼\n" +
                "☼         ☼\n" +
                "☼ ☼ ☼ ☼ ☼ ☼\n" +
                "☼☺        ☼\n" +
                "☼☼☼☼☼☼☼☼☼☼☼\n");

        dice(chopperDice, 1, Direction.DOWN.value());
        field.tick();
        field.tick();
        field.tick();
        field.tick();
        field.tick();
        field.tick();
        field.tick();
        field.tick();

        dice(chopperDice, 1, Direction.LEFT.value());
        field.tick();
        field.tick();

        hero.act();
        hero.up();
        field.tick();

        hero.up();
        field.tick();

        field.tick();
        field.tick();

        asrtBrd("☼☼☼☼☼☼☼☼☼☼☼\n" +
                "☼         ☼\n" +
                "☼ ☼ ☼ ☼ ☼ ☼\n" +
                "☼         ☼\n" +
                "☼ ☼ ☼ ☼ ☼ ☼\n" +
                "☼         ☼\n" +
                "☼ ☼ ☼ ☼ ☼ ☼\n" +
                "☼☺        ☼\n" +
                "☼ ☼ ☼ ☼ ☼ ☼\n" +
                "☼1 &      ☼\n" +
                "☼☼☼☼☼☼☼☼☼☼☼\n");

        field.tick();

        asrtBrd("☼☼☼☼☼☼☼☼☼☼☼\n" +
                "☼         ☼\n" +
                "☼ ☼ ☼ ☼ ☼ ☼\n" +
                "☼         ☼\n" +
                "☼ ☼ ☼ ☼ ☼ ☼\n" +
                "☼         ☼\n" +
                "☼ ☼ ☼ ☼ ☼ ☼\n" +
                "☼☺        ☼\n" +
                "☼҉☼ ☼ ☼ ☼ ☼\n" +
                "☼҉x       ☼\n" +
                "☼☼☼☼☼☼☼☼☼☼☼\n");

        dice(chopperDice, SIZE - 2, SIZE - 2, Direction.DOWN.value());
        field.tick();

        asrtBrd("☼☼☼☼☼☼☼☼☼☼☼\n" +
                "☼         ☼\n" +
                "☼ ☼ ☼ ☼ ☼&☼\n" +
                "☼         ☼\n" +
                "☼ ☼ ☼ ☼ ☼ ☼\n" +
                "☼         ☼\n" +
                "☼ ☼ ☼ ☼ ☼ ☼\n" +
                "☼☺        ☼\n" +
                "☼ ☼ ☼ ☼ ☼ ☼\n" +
                "☼         ☼\n" +
                "☼☼☼☼☼☼☼☼☼☼☼\n");
    }

    @Test
    public void shouldNoEventsWhenHeroNotMove() {
        field.tick();
        field.tick();
        field.tick();
        field.tick();

        verifyNoMoreInteractions(listener);
    }

    @Test
    public void shouldFireEventWhenKillWall() {
        destroyWallAt(0, 0);

        dice(heroDice, 1, 0);
        givenBoard(SIZE);

        asrtBrd("     \n" +
                "     \n" +
                "     \n" +
                "     \n" +
                "#☺   \n");

        hero.act();
        hero.right();
        field.tick();
        hero.right();
        field.tick();
        hero.right();
        field.tick();
        field.tick();
        field.tick();

        asrtBrd("     \n" +
                "     \n" +
                "     \n" +
                " ҉   \n" +
                "H҉҉ ☺\n");

        verify(listener).event(Events.KILL_DESTROY_WALL);
    }

    @Test
    public void shouldFireEventWhenKillMeatChopper() {
        meatChopperAt(0, 0);

        dice(heroDice,
                1, 0);
        givenBoard(SIZE);

        hero.act();
        hero.right();
        field.tick();
        hero.right();
        field.tick();
        hero.right();
        field.tick();
        field.tick();
        field.tick();

        asrtBrd("     \n" +
                "     \n" +
                "     \n" +
                " ҉   \n" +
                "x҉҉ ☺\n");

        verify(listener).event(Events.KILL_MEAT_CHOPPER);
    }

    @Test
    public void shouldCalculateMeatChoppersAndWallKills() {
        meatChopperAt(0, 0);
        destroyWallAt(0, 1);
        meatChopperAt(0, 2);
        destroyWallAt(0, 3);

        dice(heroDice, 1, 0);
        givenBoard(SIZE);

        canDropBombs(4);
        bombsPower(1);

        asrtBrd("     \n" +
                "#    \n" +
                "&    \n" +
                "#    \n" +
                "&☺   \n");

        hero.act();
        hero.up();
        field.tick();

        hero.act();
        hero.up();
        field.tick();

        hero.act();
        hero.up();
        field.tick();

        hero.act();
        hero.up();
        field.tick();

        asrtBrd(" ☺   \n" +
                "#4   \n" +
                "&3   \n" +
                "#2   \n" +
                "&1   \n");

        hero.right();
        field.tick();

        asrtBrd("  ☺  \n" +
                "#3   \n" +
                "&2   \n" +
                "#1   \n" +
                "x҉҉  \n");

        verifyAllEvents("[KILL_MEAT_CHOPPER]");

        field.tick();

        asrtBrd("  ☺  \n" +
                "#2   \n" +
                "&1   \n" +
                "H҉҉  \n" +
                " ҉   \n");

        verifyAllEvents("[KILL_DESTROY_WALL]");

        field.tick();

        asrtBrd("  ☺  \n" +
                "#1   \n" +
                "x҉҉  \n" +
                " ҉   \n" +
                "     \n");

        verifyAllEvents("[KILL_MEAT_CHOPPER]");

        field.tick();

        asrtBrd(" ҉☺  \n" +
                "H҉҉  \n" +
                " ҉   \n" +
                "     \n" +
                "     \n");

        verifyAllEvents("[KILL_DESTROY_WALL]");

        hero.left();
        field.tick();

        hero.down();
        hero.act();
        field.tick();

        asrtBrd("     \n" +
                " ☻   \n" +
                "     \n" +
                "     \n" +
                "     \n");

        field.tick();
        field.tick();
        field.tick();
        field.tick();

        asrtBrd(" ҉   \n" +
                "҉Ѡ҉  \n" +
                " ҉   \n" +
                "     \n" +
                "     \n");

        verifyAllEvents("[DIED]");
        assertHeroDie();

        initHero();
        field.tick();
        dice(heroDice, 1, 0);
        game.newGame();

        asrtBrd("     \n" +
                "     \n" +
                "     \n" +
                "     \n" +
                " ☺   \n");

        hero.act();
        hero.right();
        field.tick();

        hero.right();
        field.tick();

        field.tick();
        field.tick();
        field.tick();

        asrtBrd("     \n" +
                "     \n" +
                "     \n" +
                " ҉   \n" +
                "҉҉҉☺ \n");
    }

    @Test
    public void shouldCalculateMeatChoppersAndWallKills_caseBigBadaboom() {
        when(settings.isBigBadaboom()).thenReturn(new SimpleParameter<>(true));

        meatChopperAt(0, 0);
        destroyWallAt(0, 1);
        meatChopperAt(0, 2);
        destroyWallAt(0, 3);

        dice(heroDice, 1, 0);
        givenBoard(SIZE);

        canDropBombs(4);
        bombsPower(1);

        asrtBrd("     \n" +
                "#    \n" +
                "&    \n" +
                "#    \n" +
                "&☺   \n");

        hero.act();
        hero.up();
        field.tick();

        hero.act();
        hero.up();
        field.tick();

        hero.act();
        hero.up();
        field.tick();

        hero.act();
        hero.up();
        field.tick();

        asrtBrd(" ☺   \n" +
                "#4   \n" +
                "&3   \n" +
                "#2   \n" +
                "&1   \n");

        verifyAllEvents("[]");

        hero.right();
        field.tick();

        asrtBrd(" ҉☺  \n" +
                "H҉҉  \n" +
                "x҉҉  \n" +
                "H҉҉  \n" +
                "x҉҉  \n");

        field.tick();

        asrtBrd("  ☺  \n" +
                "     \n" +
                "     \n" +
                "     \n" +
                "     \n");

        verifyAllEvents("[KILL_MEAT_CHOPPER, KILL_DESTROY_WALL, KILL_MEAT_CHOPPER, KILL_DESTROY_WALL]");
    }

    // если я двинулся за пределы стены и тут же поставил бомбу, то бомба упадет на моем текущем месте
    @Test
    public void shouldMoveOnBoardAndDropBombTogether() {
        givenBoardWithOriginalWalls();
        hero.up();
        field.tick();

        hero.up();
        field.tick();

        hero.left();
        hero.act();
        field.tick();

        asrtBrd("☼☼☼☼☼\n" +
                "☼☻  ☼\n" +
                "☼ ☼ ☼\n" +
                "☼   ☼\n" +
                "☼☼☼☼☼\n");
    }

    // чертик  может ходить по бомбам
    @Test
    public void shouldMonsterCanMoveOnBomb() {
        givenBoardWithMeatChopper(SIZE);

        asrtBrd("☼☼☼☼☼\n" +
                "☼  &☼\n" +
                "☼ ☼ ☼\n" +
                "☼☺  ☼\n" +
                "☼☼☼☼☼\n");

        hero.up();
        field.tick();

        hero.up();
        field.tick();

        hero.right();
        hero.act();
        field.tick();

        hero.left();
        field.tick();

        hero.down();
        field.tick();

        asrtBrd("☼☼☼☼☼\n" +
                "☼ 2&☼\n" +
                "☼☺☼ ☼\n" +
                "☼   ☼\n" +
                "☼☼☼☼☼\n");

        dice(chopperDice, Direction.LEFT.value());
        field.tick();

        asrtBrd("☼☼☼☼☼\n" +
                "☼ & ☼\n" +
                "☼☺☼ ☼\n" +
                "☼   ☼\n" +
                "☼☼☼☼☼\n");
    }

    @Test
    public void shouldStopBlastWhenHeroOrDestroyWalls() {
        bombsPower(5);

        destroyWallAt(3, 0);

        givenBoard(7);
        when(heroDice.next(anyInt())).thenReturn(101); // don't drop perk by accident

        hero.act();
        hero.up();
        field.tick();

        hero.up();
        field.tick();

        field.tick();
        field.tick();
        field.tick();

        asrtBrd("       \n" +
                "       \n" +
                "       \n" +
                "       \n" +
                "Ѡ      \n" +
                "҉      \n" +
                "҉҉҉H   \n");
    }

    @Test
    public void shouldStopBlastWhenMeatChopper() {
        bombsPower(5);

        meatChopperAt(4, 0);

        dice(heroDice, 0, 0);
        givenBoard(7);

        hero.act();
        hero.up();
        field.tick();

        hero.up();
        field.tick();

        hero.up();
        field.tick();

        hero.right();
        field.tick();
        field.tick();

        asrtBrd("       \n" +
                "҉      \n" +
                "҉      \n" +
                "҉☺     \n" +
                "҉      \n" +
                "҉      \n" +
                "҉҉҉҉x  \n");
    }

    @Test
    public void shouldMeatChopperAppearAfterKill() {
        bombsPower(3);

        dice(chopperDice, 3, 0, Direction.DOWN.value());
        MeatChoppers walls = new MeatChoppers(new WallsImpl(), v(1), chopperDice);
        walls.init(field);
        withWalls(walls);

        givenBoard(SIZE);

        hero.act();
        hero.up();
        field.tick();

        hero.right();
        field.tick();

        field.tick();
        field.tick();
        field.tick();

        asrtBrd("     \n" +
                "҉    \n" +
                "҉    \n" +
                "҉☺   \n" +
                "҉҉҉x \n");

        dice(chopperDice, 2, 2, Direction.DOWN.value());
        field.tick();

        asrtBrd("     \n" +
                "     \n" +
                "     \n" +
                " ☺&  \n" +
                "     \n");
    }

    @Test
    public void shouldMeatChopperNotAppearWhenDestroyWall() {
        bombsPower(3);

        dice(chopperDice, 4, 4, Direction.RIGHT.value());
        destroyWallAt(3, 0);
        MeatChoppers walls = new MeatChoppers(this.walls, v(1), chopperDice);
        walls.init(field);
        withWalls(walls);

        givenBoard(SIZE);

        hero.act();
        hero.up();
        field.tick();

        hero.right();
        field.tick();

        field.tick();
        field.tick();
        field.tick();

        asrtBrd("    &\n" +
                "҉    \n" +
                "҉    \n" +
                "҉☺   \n" +
                "҉҉҉H \n");

        dice(chopperDice, Direction.DOWN.value());
        field.tick();

        asrtBrd("     \n" +
                "    &\n" +
                "     \n" +
                " ☺   \n" +
                "     \n");
    }

    // Чертик не может появится на бомбере!
    @Test
    public void shouldMeatChopperNotAppearOnHero() {
        shouldMonsterCanMoveOnBomb();

        hero.down();
        field.tick();

        asrtBrd("☼☼☼☼☼\n" +
                "☼x҉҉☼\n" +
                "☼ ☼ ☼\n" +
                "☼☺  ☼\n" +
                "☼☼☼☼☼\n");

        dice(chopperDice,
                0, 0, // на неразрушаемой стене нельзя
                hero.getX(), hero.getY(), // попытка поселиться на бобмере
                3, 3, // попытка - клетка свободна
                Direction.DOWN.value()); // а это куда он сразу же отправится

        // when пришла пора регенериться чоперу
        field.tick();

        asrtBrd("☼☼☼☼☼\n" +
                "☼   ☼\n" +
                "☼ ☼&☼\n" +
                "☼☺  ☼\n" +
                "☼☼☼☼☼\n");
    }

    @Test
    public void shouldWallNotAppearOnHero() {
        int size = 5;
        dice(wallDice, 2, 1);
        dice(heroDice, 1, 1);  // hero в левом нижнем углу

        EatSpaceWalls walls = new EatSpaceWalls(new OriginalWalls(v(size)), v(1), wallDice);
        withWalls(walls);

        givenBoard(size);

        walls.init(field);
        walls.regenerate();

        asrtBrd("☼☼☼☼☼\n" +
                "☼   ☼\n" +
                "☼ ☼ ☼\n" +
                "☼☺# ☼\n" +
                "☼☼☼☼☼\n");

        hero.act();
        field.tick();

        hero.up();
        field.tick();

        hero.up();
        field.tick();

        hero.right();
        field.tick();

        field.tick();

        asrtBrd("☼☼☼☼☼\n" +
                "☼ ☺ ☼\n" +
                "☼҉☼ ☼\n" +
                "☼҉H ☼\n" +
                "☼☼☼☼☼\n");
        // when
        field.tick();
        dice(wallDice,
                0, 0,                     // на неразрушаемоей стене нельзя
                hero.getX(), hero.getY(), // на месте бомбера не должен появиться
                1, 1);                    // а вот тут свободно

        // then
        asrtBrd("☼☼☼☼☼\n" +
                "☼ ☺ ☼\n" +
                "☼ ☼ ☼\n" +
                "☼#  ☼\n" +
                "☼☼☼☼☼\n");
    }


}
