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
import com.codenjoy.dojo.icancode.model.items.perks.UnlimitedFirePerk;
import com.codenjoy.dojo.icancode.model.items.perks.UnstoppableLaserPerk;
import com.codenjoy.dojo.icancode.services.Events;
import com.codenjoy.dojo.icancode.services.SettingsWrapper;
import com.codenjoy.dojo.services.settings.SettingsImpl;
import org.junit.Test;

import static com.codenjoy.dojo.services.Direction.STOP;
import static org.mockito.Mockito.verify;

public class UnstoppableLaserPerkTest extends AbstractGameTest {

    @Test
    public void shouldHeroFireThroughBox_withUnstoppableLaser() {
        // given
        givenFl("╔═════┐" +
                "║..S..│" +
                "║..l..│" +
                "║.....│" +
                "║..B..│" +
                "║.....│" +
                "└─────┘");

        // when
        hero.down();
        game.tick();

        // then
        assertL("╔═════┐" +
                "║..S..│" +
                "║.....│" +
                "║.....│" +
                "║.....│" +
                "║.....│" +
                "└─────┘");

        assertE("-------" +
                "-------" +
                "---☺---" +
                "-------" +
                "---B---" +
                "-------" +
                "-------");

        has(UnstoppableLaserPerk.class);

        // when
        hero.down();
        hero.fire();
        game.tick();

        // then
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
    public void shouldHeroFireThroughOtherHero_withUnstoppableLaser() {
        // given
        givenFl("╔═════┐" +
                "║.....│" +
                "║..S..│" +
                "║..X..│" +
                "║..l..│" +
                "║.....│" +
                "└─────┘");

        // when
        hero.down();
        game.tick();

        // then
        assertE("-------" +
                "-------" +
                "---X---" +
                "---☺---" +
                "-------" +
                "-------" +
                "-------");

        assertL("╔═════┐" +
                "║.....│" +
                "║..S..│" +
                "║.....│" +
                "║..l..│" +
                "║.....│" +
                "└─────┘");

        // when
        hero.down();
        game.tick();

        // then
        assertE("-------" +
                "-------" +
                "---X---" +
                "-------" +
                "---☺---" +
                "-------" +
                "-------");

        has(UnstoppableLaserPerk.class);

        // when
        hero.up();
        hero.fire();
        game.tick();

        // then
        assertE("-------" +
                "-------" +
                "---X---" +
                "---↑---" +
                "---☺---" +
                "-------" +
                "-------");

        has(UnstoppableLaserPerk.class);

        // when
        game.tick();

        // then
        assertE("-------" +
                "-------" +
                "---&---" +
                "-------" +
                "---☺---" +
                "-------" +
                "-------");

        has(UnstoppableLaserPerk.class);

        verify(listener).event(Events.KILL_HERO(1, true));

        // when
        game.tick();

        // then
        assertE("-------" +
                "---↑---" +
                "---X---" +
                "-------" +
                "---☺---" +
                "-------" +
                "-------");

        has(UnstoppableLaserPerk.class);
    }

    @Test
    public void shouldHeroFireThroughZombie_withUnstoppableLaser() {
        // given
        givenFl("╔═══════┐" +
                "║.S.....│" +
                "║.l.....│" +
                "║.......│" +
                "║.♂.....│" +
                "║.......│" +
                "║.......│" +
                "║.......│" +
                "└───────┘");

        givenZombie().thenReturn(STOP);

        // when
        hero.down();
        game.tick();

        // then
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

        has(UnstoppableLaserPerk.class);

        // when
        hero.down();
        hero.fire();
        game.tick();

        // then
        assertE("---------" +
                "---------" +
                "--☺------" +
                "--↓------" +
                "--♂------" +
                "---------" +
                "---------" +
                "---------" +
                "---------");

        has(UnstoppableLaserPerk.class);

        // when
        game.tick();

        // then
        assertE("---------" +
                "---------" +
                "--☺------" +
                "---------" +
                "--✝------" +
                "---------" +
                "---------" +
                "---------" +
                "---------");

        has(UnstoppableLaserPerk.class);
        verify(listener).event(Events.KILL_ZOMBIE(1, true));

        // when
        game.tick();

        // then
        assertE("---------" +
                "---------" +
                "--☺------" +
                "---------" +
                "---------" +
                "--↓------" +
                "---------" +
                "---------" +
                "---------");

        has(UnstoppableLaserPerk.class);
    }

    @Test
    public void shouldHeroKillsZombieAndOtherHero_withUnstoppableLaser() {
        // given
        SettingsWrapper.setup(new SettingsImpl())
                .perkActivity(11);

        givenFl("╔═══════┐" +
                "║.......│" +
                "║.S.....│" +
                "║.X.....│" +
                "║.......│" +
                "║.......│" +
                "║.......│" +
                "║.l.....│" +
                "└───────┘");

        // when
        hero.down();
        game.tick();
        hero.down();
        game.tick();
        hero.down();
        game.tick();
        hero.down();
        game.tick();

        // then
        assertE("---------" +
                "---------" +
                "--X------" +
                "---------" +
                "---------" +
                "---------" +
                "--☺------" +
                "---------" +
                "---------");

        assertL("╔═══════┐" +
                "║.......│" +
                "║.S.....│" +
                "║.......│" +
                "║.......│" +
                "║.......│" +
                "║.......│" +
                "║.l.....│" +
                "└───────┘");

        // when
        hero.down();
        game.tick();

        // then
        assertE("---------" +
                "---------" +
                "--X------" +
                "---------" +
                "---------" +
                "---------" +
                "---------" +
                "--☺------" +
                "---------");

        has(UnstoppableLaserPerk.class);

        zombieAt(2, 4);

        assertE("---------" +
                "---------" +
                "--X------" +
                "---------" +
                "--♂------" +
                "---------" +
                "---------" +
                "--☺------" +
                "---------");

        has(UnstoppableLaserPerk.class);

        // when
        hero.up();
        hero.fire();
        game.tick();

        // then
        assertE("---------" +
                "---------" +
                "--X------" +
                "---------" +
                "--♂------" +
                "---------" +
                "--↑------" +
                "--☺------" +
                "---------");

        has(UnstoppableLaserPerk.class);

        // when
        game.tick();

        // then
        assertE("---------" +
                "---------" +
                "--X------" +
                "---------" +
                "--♂------" +
                "--↑------" +
                "---------" +
                "--☺------" +
                "---------");

        has(UnstoppableLaserPerk.class);

        // when
        game.tick();

        // then
        assertE("---------" +
                "---------" +
                "--X------" +
                "---------" +
                "--✝------" +
                "---------" +
                "---------" +
                "--☺------" +
                "---------");

        has(UnstoppableLaserPerk.class);
        verify(listener).event(Events.KILL_ZOMBIE(1, true));

        // whan
        game.tick();

        // then
        assertE("---------" +
                "---------" +
                "--X------" +
                "--↑------" +
                "---------" +
                "---------" +
                "---------" +
                "--☺------" +
                "---------");

        has(UnstoppableLaserPerk.class);

        // when
        game.tick();

        // then
        assertE("---------" +
                "---------" +
                "--&------" +
                "---------" +
                "---------" +
                "---------" +
                "---------" +
                "--☺------" +
                "---------");

        has(UnstoppableLaserPerk.class);
        verify(listener).event(Events.KILL_HERO(1, true));

        // when
        game.tick();

        // then
        assertE("---------" +
                "--↑------" +
                "--X------" +
                "---------" +
                "---------" +
                "---------" +
                "---------" +
                "--☺------" +
                "---------");

        has(UnstoppableLaserPerk.class);
    }

    @Test
    public void shouldHeroHasUnstoppableLaserPerk() {
        // given
        givenFl("╔════┐" +
                "║.S..│" +
                "║.l..│" +
                "║....│" +
                "║....│" +
                "└────┘");

        // when
        hero.down();
        game.tick();

        // then
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

        has(UnstoppableLaserPerk.class);
    }

    @Test
    public void shouldNotPickUnlimitedFirePerk_whenJumpOverIt() {
        // given
        givenFl("╔════┐" +
                "║Sl..│" +
                "║....│" +
                "║....│" +
                "║....│" +
                "└────┘");

        // when
        hero.jump();
        hero.right();
        game.tick();

        // then
        hasNot(UnlimitedFirePerk.class);

        assertL("╔════┐" +
                "║Sl..│" +
                "║....│" +
                "║....│" +
                "║....│" +
                "└────┘");

        assertE("------" +
                "------" +
                "------" +
                "------" +
                "------" +
                "------");

        assertF("------" +
                "--*---" +
                "------" +
                "------" +
                "------" +
                "------");

        // when
        game.tick();

        // then
        hasNot(UnlimitedFirePerk.class);

        assertL("╔════┐" +
                "║Sl..│" +
                "║....│" +
                "║....│" +
                "║....│" +
                "└────┘");

        assertE("------" +
                "---☺--" +
                "------" +
                "------" +
                "------" +
                "------");

        assertF("------" +
                "------" +
                "------" +
                "------" +
                "------" +
                "------");
    }
}
