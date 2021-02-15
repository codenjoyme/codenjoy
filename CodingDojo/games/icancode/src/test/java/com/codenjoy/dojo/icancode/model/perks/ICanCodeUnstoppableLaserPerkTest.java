package com.codenjoy.dojo.icancode.model.perks;

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

import com.codenjoy.dojo.icancode.model.AbstractGameTest;
import com.codenjoy.dojo.icancode.model.Field;
import com.codenjoy.dojo.icancode.model.items.Box;
import com.codenjoy.dojo.icancode.model.items.Zombie;
import com.codenjoy.dojo.icancode.services.Events;
import org.junit.Test;
import org.mockito.Mockito;

import static com.codenjoy.dojo.icancode.model.Elements.BOX;
import static com.codenjoy.dojo.icancode.model.Elements.UNSTOPPABLE_LASER_PERK;
import static com.codenjoy.dojo.services.Direction.STOP;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.verify;

public class ICanCodeUnstoppableLaserPerkTest extends AbstractGameTest {

    @Test
    public void heroFireThroughBoxWithUnstoppableLaser() {
        givenFl("╔═════┐" +
                "║..S..│" +
                "║.....│" +
                "║.....│" +
                "║.....│" +
                "║.....│" +
                "└─────┘");

        game.move(new UnstoppableLaserPerk(UNSTOPPABLE_LASER_PERK), 3, 4);
        game.move(new Box(BOX), 3, 2);

        assertL("╔═════┐" +
                "║..S..│" +
                "║..l..│" +
                "║.....│" +
                "║.....│" +
                "║.....│" +
                "└─────┘");

        hero.down();
        game.tick();

        assertE("-------" +
                "-------" +
                "---☺---" +
                "-------" +
                "---B---" +
                "-------" +
                "-------");

        assertEquals(true, hero.has(UnstoppableLaserPerk.class));

        hero.down();
        hero.fire();
        game.tick();

        assertE("-------" +
                "-------" +
                "---☺---" +
                "---↓---" +
                "---B---" +
                "-------" +
                "-------");

        game.tick();


        assertE("-------" +
                "-------" +
                "---☺---" +
                "-------" +
                "---B---" +
                "-------" +
                "-------");

        game.tick();

        assertE("-------" +
                "-------" +
                "---☺---" +
                "-------" +
                "---B---" +
                "---↓---" +
                "-------");
    }

    @Test
    public void heroFireThroughOtherHeroWithUnstoppableLaser() {
        // given
        givenFl("╔═════┐" +
                "║.....│" +
                "║..S..│" +
                "║..X..│" +
                "║.....│" +
                "║.....│" +
                "└─────┘");

        hero.down();
        game.tick();

        assertE("-------" +
                "-------" +
                "---X---" +
                "---☺---" +
                "-------" +
                "-------" +
                "-------");

        game.move(new UnstoppableLaserPerk(UNSTOPPABLE_LASER_PERK), 3, 2);

        assertL("╔═════┐" +
                "║.....│" +
                "║..S..│" +
                "║.....│" +
                "║..l..│" +
                "║.....│" +
                "└─────┘");

        hero.down();
        game.tick();

        assertE("-------" +
                "-------" +
                "---X---" +
                "-------" +
                "---☺---" +
                "-------" +
                "-------");

        assertEquals(true, hero.has(UnstoppableLaserPerk.class));

        hero.up();
        hero.fire();
        game.tick();

        assertE("-------" +
                "-------" +
                "---X---" +
                "---↑---" +
                "---☺---" +
                "-------" +
                "-------");

        assertEquals(true, hero.has(UnstoppableLaserPerk.class));

        game.tick();

        assertE("-------" +
                "-------" +
                "---&---" +
                "-------" +
                "---☺---" +
                "-------" +
                "-------");

        assertEquals(true, hero.has(UnstoppableLaserPerk.class));
        verify(listener).event(Events.KILL_HERO(1, true));

        game.tick();

        assertE("-------" +
                "---↑---" +
                "---X---" +
                "-------" +
                "---☺---" +
                "-------" +
                "-------");

        assertEquals(true, hero.has(UnstoppableLaserPerk.class));
    }

    @Test
    public void heroFireThroughZombieWithUnstoppableLaser() {
        givenFl("╔═══════┐" +
                "║.S.....│" +
                "║.......│" +
                "║.......│" +
                "║.......│" +
                "║.......│" +
                "║.......│" +
                "║.......│" +
                "└───────┘");

        game.move(new UnstoppableLaserPerk(UNSTOPPABLE_LASER_PERK), 2, 6);
        givenZombie().thenReturn(STOP);
        Zombie zombie = new Zombie(true);
        zombie.setField(Mockito.mock(Field.class));
        game.move(zombie, 2, 4);

        assertL("╔═══════┐" +
                "║.S.....│" +
                "║.l.....│" +
                "║.......│" +
                "║.......│" +
                "║.......│" +
                "║.......│" +
                "║.......│" +
                "└───────┘");

        assertE("---------" +
                "--☺------" +
                "---------" +
                "---------" +
                "--♂------" +
                "---------" +
                "---------" +
                "---------" +
                "---------");

        hero.down();
        game.tick();

        assertL("╔═══════┐" +
                "║.S.....│" +
                "║.......│" +
                "║.......│" +
                "║.......│" +
                "║.......│" +
                "║.......│" +
                "║.......│" +
                "└───────┘");
        assertE("---------" +
                "---------" +
                "--☺------" +
                "---------" +
                "--♂------" +
                "---------" +
                "---------" +
                "---------" +
                "---------");

        assertEquals(true, hero.has(UnstoppableLaserPerk.class));

        hero.down();
        hero.fire();
        game.tick();

        assertE("---------" +
                "---------" +
                "--☺------" +
                "--↓------" +
                "--♂------" +
                "---------" +
                "---------" +
                "---------" +
                "---------");

        assertEquals(true, hero.has(UnstoppableLaserPerk.class));

        game.tick();

        assertE("---------" +
                "---------" +
                "--☺------" +
                "---------" +
                "--✝------" +
                "---------" +
                "---------" +
                "---------" +
                "---------");

        assertEquals(true, hero.has(UnstoppableLaserPerk.class));
        verify(listener).event(Events.KILL_ZOMBIE(1, true));

        game.tick();

        assertE("---------" +
                "---------" +
                "--☺------" +
                "---------" +
                "---------" +
                "--↓------" +
                "---------" +
                "---------" +
                "---------");

        assertEquals(true, hero.has(UnstoppableLaserPerk.class));
    }

    @Test
    public void heroKillsZombieAndOtherHeroWithUnstoppableLaser() {
        givenFl("╔═══════┐" +
                "║.......│" +
                "║.S.....│" +
                "║.X.....│" +
                "║.......│" +
                "║.......│" +
                "║.......│" +
                "║.......│" +
                "└───────┘");

        hero.down();
        game.tick();
        hero.down();
        game.tick();
        hero.down();
        game.tick();
        hero.down();
        game.tick();

        assertE("---------" +
                "---------" +
                "--X------" +
                "---------" +
                "---------" +
                "---------" +
                "--☺------" +
                "---------" +
                "---------");

        game.move(new UnstoppableLaserPerk(UNSTOPPABLE_LASER_PERK), 2, 1);

        assertL("╔═══════┐" +
                "║.......│" +
                "║.S.....│" +
                "║.......│" +
                "║.......│" +
                "║.......│" +
                "║.......│" +
                "║.l.....│" +
                "└───────┘");

        hero.down();
        game.tick();

        assertE("---------" +
                "---------" +
                "--X------" +
                "---------" +
                "---------" +
                "---------" +
                "---------" +
                "--☺------" +
                "---------");

        assertEquals(true, hero.has(UnstoppableLaserPerk.class));

        givenZombie().thenReturn(STOP);
        Zombie zombie = new Zombie(true);
        zombie.setField(Mockito.mock(Field.class));
        game.move(zombie, 2, 4);

        assertE("---------" +
                "---------" +
                "--X------" +
                "---------" +
                "--♂------" +
                "---------" +
                "---------" +
                "--☺------" +
                "---------");

        assertEquals(true, hero.has(UnstoppableLaserPerk.class));


        hero.up();
        hero.fire();
        game.tick();

        assertE("---------" +
                "---------" +
                "--X------" +
                "---------" +
                "--♂------" +
                "---------" +
                "--↑------" +
                "--☺------" +
                "---------");

        assertEquals(true, hero.has(UnstoppableLaserPerk.class));

        game.tick();

        assertE("---------" +
                "---------" +
                "--X------" +
                "---------" +
                "--♂------" +
                "--↑------" +
                "---------" +
                "--☺------" +
                "---------");

        assertEquals(true, hero.has(UnstoppableLaserPerk.class));

        game.tick();

        assertE("---------" +
                "---------" +
                "--X------" +
                "---------" +
                "--✝------" +
                "---------" +
                "---------" +
                "--☺------" +
                "---------");

        assertEquals(true, hero.has(UnstoppableLaserPerk.class));
        verify(listener).event(Events.KILL_ZOMBIE(1, true));

        game.tick();

        assertE("---------" +
                "---------" +
                "--X------" +
                "--↑------" +
                "---------" +
                "---------" +
                "---------" +
                "--☺------" +
                "---------");

        assertEquals(true, hero.has(UnstoppableLaserPerk.class));

        game.tick();

        assertE("---------" +
                "---------" +
                "--&------" +
                "---------" +
                "---------" +
                "---------" +
                "---------" +
                "--☺------" +
                "---------");

        assertEquals(true, hero.has(UnstoppableLaserPerk.class));
        verify(listener).event(Events.KILL_HERO(1, true));

        game.tick();

        assertE("---------" +
                "--↑------" +
                "--X------" +
                "---------" +
                "---------" +
                "---------" +
                "---------" +
                "--☺------" +
                "---------");

        assertEquals(true, hero.has(UnstoppableLaserPerk.class));
    }

    @Test
    public void heroHasUnstoppableLaserPerk() {
        givenFl("╔════┐" +
                "║.S..│" +
                "║....│" +
                "║....│" +
                "║....│" +
                "└────┘");

        game.move(new UnstoppableLaserPerk(UNSTOPPABLE_LASER_PERK), 2, 3);

        assertL("╔════┐" +
                "║.S..│" +
                "║.l..│" +
                "║....│" +
                "║....│" +
                "└────┘");

        hero.down();
        game.tick();

        assertE("------" +
                "------" +
                "--☺---" +
                "------" +
                "------" +
                "------");
        assertL("╔════┐" +
                "║.S..│" +
                "║....│" +
                "║....│" +
                "║....│" +
                "└────┘");

        assertEquals(true, hero.has(UnstoppableLaserPerk.class));
    }
}
