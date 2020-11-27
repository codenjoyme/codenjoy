package com.codenjoy.dojo.icancode.model;

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


import com.codenjoy.dojo.icancode.services.Events;
import com.codenjoy.dojo.services.Direction;
import com.codenjoy.dojo.services.Point;
import com.codenjoy.dojo.icancode.model.items.Zombie;
import com.codenjoy.dojo.icancode.model.items.ZombieBrain;
import com.codenjoy.dojo.icancode.model.items.ZombiePot;
import org.junit.Test;
import org.mockito.stubbing.OngoingStubbing;

import static com.codenjoy.dojo.services.Direction.LEFT;
import static com.codenjoy.dojo.services.Direction.UP;
import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class ICanCodeZombieTest extends AbstractGameTest {

    @Test
    public void shouldZombieFemaleStart() {
        // given
        ZombiePot.TICKS_PER_NEW_ZOMBIE = 3;

        givenFl("╔════┐" +
                "║S...│" +
                "║....│" +
                "║....│" +
                "║...Z│" +
                "└────┘");

        // when
        dice(1); // female
        game.tick();
        game.tick();

        assertE("------" +
                "-☺----" +
                "------" +
                "------" +
                "------" +
                "------");

        game.tick(); // 3rd tick

        // then
        assertE("------" +
                "-☺----" +
                "------" +
                "------" +
                "----♀-" +
                "------");
    }

    @Test
    public void shouldZombieCanWalk() {
        // given
        ZombiePot.TICKS_PER_NEW_ZOMBIE = 10;
        Zombie.WALK_EACH_TICKS = 2;
        givenZombie().thenReturn(UP);

        givenFl("╔════┐" +
                "║S...│" +
                "║....│" +
                "║....│" +
                "║...Z│" +
                "└────┘");

        // when
        dice(1); // female
        game.tick();
        game.tick();
        game.tick();
        game.tick();
        game.tick();
        game.tick();
        game.tick();
        game.tick();
        game.tick();
        game.tick();

        assertE("------" +
                "-☺----" +
                "------" +
                "------" +
                "----♀-" +
                "------");

        // when
        game.tick();
        game.tick();

        // then
        assertE("------" +
                "-☺----" +
                "------" +
                "----♀-" +
                "------" +
                "------");

        // when
        game.tick();
        game.tick();

        // then
        assertE("------" +
                "-☺----" +
                "----♀-" +
                "------" +
                "------" +
                "------");
    }

    @Test
    public void shouldZombiePotGeneratesAnotherOne() {
        // given
        ZombiePot.TICKS_PER_NEW_ZOMBIE = 4;
        Zombie.WALK_EACH_TICKS = 2;
        givenZombie().thenReturn(UP);

        givenFl("╔════┐" +
                "║S...│" +
                "║....│" +
                "║....│" +
                "║...Z│" +
                "└────┘");

        // when
        dice(1); // female
        game.tick();
        game.tick();
        game.tick();
        game.tick();

        assertE("------" +
                "-☺----" +
                "------" +
                "------" +
                "----♀-" +
                "------");

        // when
        game.tick();
        game.tick();

        // then
        assertE("------" +
                "-☺----" +
                "------" +
                "----♀-" +
                "------" +
                "------");

        // when
        game.tick();
        generateMale();
        game.tick();

        // then
        assertE("------" +
                "-☺----" +
                "----♀-" +
                "------" +
                "----♂-" +
                "------");

        game.tick();
        game.tick();

        // then
        assertE("------" +
                "-☺--♀-" +
                "------" +
                "----♂-" +
                "------" +
                "------");
    }

    @Test
    public void shouldZombieKillHero() {
        // given
        ZombiePot.TICKS_PER_NEW_ZOMBIE = 6;
        Zombie.WALK_EACH_TICKS = 2;
        givenZombie().thenReturn(UP, LEFT);

        givenFl("╔════┐" +
                "║.S..│" +
                "║....│" +
                "║..Z.│" +
                "║....│" +
                "└────┘");

        hero.down();
        game.tick();
        game.tick();
        game.tick();
        game.tick();
        game.tick();
        generateFemale();
        game.tick();

        assertE("------" +
                "------" +
                "--☺---" +
                "---♀--" +
                "------" +
                "------");

        // when
        game.tick();
        game.tick();

        // then
        assertE("------" +
                "------" +
                "--☺♀--" +
                "------" +
                "------" +
                "------");

        // when
        game.tick();
        game.tick();

        // then
        assertE("------" +
                "------" +
                "--☻---" +
                "------" +
                "------" +
                "------");

        verify(listener).event(Events.LOOSE());
        assertEquals(false, hero.isAlive());
        assertEquals(false, hero.isWin());

        // when
        selectStartSpot().thenReturn(1); // and female zombie
        game.newGame(player);
        game.tick();
        game.tick();
        game.tick();
        game.tick();
        game.tick();

        // then
        assertE("------" +
                "--☺---" +
                "------" +
                "------" +
                "------" +
                "------");

        game.tick();

        // then
        assertE("------" +
                "--☺---" +
                "------" +
                "---♀--" +
                "------" +
                "------");
    }

    OngoingStubbing<Integer> selectStartSpot() {
        return dice(0);
    }

    OngoingStubbing<Integer> generateFemale() {
        return dice(1);
    }

    OngoingStubbing<Integer> generateMale() {
        return dice(0);
    }

    OngoingStubbing<Direction> givenZombie() {
        Zombie.BRAIN = mock(ZombieBrain.class);
        return when(Zombie.BRAIN.whereToGo(any(Point.class), any(Field.class)));
    }

    @Test
    public void shouldHeroKillZombie() {
        // given
        ZombiePot.TICKS_PER_NEW_ZOMBIE = 6;
        Zombie.WALK_EACH_TICKS = 2;
        givenZombie().thenReturn(UP);

        givenFl("╔════┐" +
                "║.S..│" +
                "║....│" +
                "║....│" +
                "║.Z..│" +
                "└────┘");

        game.tick();
        game.tick();
        game.tick();
        game.tick();
        game.tick();
        generateFemale();
        game.tick();
        hero.down();
        hero.fire();

        assertE("------" +
                "--☺---" +
                "------" +
                "------" +
                "--♀---" +
                "------");

        // when
        game.tick();

        // then
        assertE("------" +
                "--☺---" +
                "--↓---" +
                "------" +
                "--♀---" +
                "------");

        // when
        game.tick();

        // then
        verify(listener).event(Events.KILL_ZOMBIE(1, false));
        assertE("------" +
                "--☺---" +
                "------" +
                "--✝---" +
                "------" +
                "------");

        // when
        game.tick();

        // then
        assertE("------" +
                "--☺---" +
                "------" +
                "------" +
                "------" +
                "------");

        // when
        game.tick();
        game.tick();
        generateMale();
        game.tick();

        // then
        assertE("------" +
                "--☺---" +
                "------" +
                "------" +
                "--♂---" +
                "------");

        // when
        game.tick();
        game.tick();

        // then
        assertE("------" +
                "--☺---" +
                "------" +
                "--♂---" +
                "------" +
                "------");
    }


    @Test
    public void shouldPlayerJumpOverZombie() {
        // given
        ZombiePot.TICKS_PER_NEW_ZOMBIE = 6;
        Zombie.WALK_EACH_TICKS = 2;
        givenZombie().thenReturn(UP);

        givenFl("╔════┐" +
                "║.S..│" +
                "║....│" +
                "║....│" +
                "║.Z..│" +
                "└────┘");

        game.tick();
        game.tick();
        game.tick();
        game.tick();
        game.tick();
        generateFemale();
        game.tick();

        assertE("------" +
                "--☺---" +
                "------" +
                "------" +
                "--♀---" +
                "------");

        // when
        hero.down();
        game.tick();
        game.tick();

        // then
        assertE("------" +
                "------" +
                "--☺---" +
                "--♀---" +
                "------" +
                "------");

        // when
        hero.jump();
        hero.down();
        game.tick();

        // then
        assertE("------" +
                "------" +
                "------" +
                "--♀---" +
                "------" +
                "------");

        assertF("------" +
                "------" +
                "------" +
                "--*---" +
                "------" +
                "------");

        // when
        game.tick();

        // then
        assertE("------" +
                "------" +
                "--♀---" +
                "------" +
                "--☺---" +
                "------");
    }
}
