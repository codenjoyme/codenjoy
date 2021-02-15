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
import org.junit.Test;
import org.mockito.Mockito;

import static com.codenjoy.dojo.icancode.model.Elements.*;
import static com.codenjoy.dojo.services.Direction.STOP;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class ICanCodeDeathRayPerkTest extends AbstractGameTest {

    @Test
    public void shouldDeathRayPerk() {
        // given
        settings.deathRayRange(10);

        givenFl("╔══════════┐" +
                "║..S.......│" +
                "║..........│" +
                "║..........│" +
                "║..........│" +
                "║..........│" +
                "║..........│" +
                "║..........│" +
                "║..........│" +
                "║..........│" +
                "║..........│" +
                "└──────────┘");
        
        game.move(new DeathRayPerk(DEATH_RAY_PERK), 3, 9);
        game.move(new Box(BOX), 3, 5);

        assertL("╔══════════┐" +
                "║..S.......│" +
                "║..r.......│" +
                "║..........│" +
                "║..........│" +
                "║..........│" +
                "║..........│" +
                "║..........│" +
                "║..........│" +
                "║..........│" +
                "║..........│" +
                "└──────────┘");
        
        assertE("------------" +
                "---☺--------" +
                "------------" +
                "------------" +
                "------------" +
                "------------" +
                "---B--------" +
                "------------" +
                "------------" +
                "------------" +
                "------------" +
                "------------");

        // when
        hero.down();
        game.tick();

        // then
        assertE("------------" +
                "------------" +
                "---☺--------" +
                "------------" +
                "------------" +
                "------------" +
                "---B--------" +
                "------------" +
                "------------" +
                "------------" +
                "------------" +
                "------------");

        has(DeathRayPerk.class);

        // when
        hero.down();
        hero.fire();
        game.tick();

        // then
        assertE("------------" +
                "------------" +
                "---☺--------" +
                "---↓--------" +
                "---↓--------" +
                "---↓--------" +
                "---B--------" +
                "------------" +
                "------------" +
                "------------" +
                "------------" +
                "------------");

        has(DeathRayPerk.class);

        // when
        game.tick();

        // then
        assertE("------------" +
                "------------" +
                "---☺--------" +
                "------------" +
                "------------" +
                "------------" +
                "---B--------" +
                "------------" +
                "------------" +
                "------------" +
                "------------" +
                "------------");

        has(DeathRayPerk.class);
    }

    @Test
    public void deathRayAndUnstoppableLaser() {
        // given
        settings.deathRayRange(10);

        givenFl("╔══════════┐" +
                "║..S.......│" +
                "║..........│" +
                "║..........│" +
                "║..........│" +
                "║..........│" +
                "║..........│" +
                "║..........│" +
                "║..........│" +
                "║..........│" +
                "║..........│" +
                "└──────────┘");
        
        game.move(new DeathRayPerk(DEATH_RAY_PERK), 3, 9);
        game.move(new UnstoppableLaserPerk(UNSTOPPABLE_LASER_PERK), 3, 8);

        game.move(new Box(BOX), 3, 4);
        givenZombie().thenReturn(STOP);
        Zombie zombie = new Zombie(true);
        zombie.setField(Mockito.mock(Field.class));
        game.move(zombie, 3, 2);

        assertL("╔══════════┐" +
                "║..S.......│" +
                "║..r.......│" +
                "║..l.......│" +
                "║..........│" +
                "║..........│" +
                "║..........│" +
                "║..........│" +
                "║..........│" +
                "║..........│" +
                "║..........│" +
                "└──────────┘");

        // when
        hero.down();
        game.tick();

        // then
        assertE("------------" +
                "------------" +
                "---☺--------" +
                "------------" +
                "------------" +
                "------------" +
                "------------" +
                "---B--------" +
                "------------" +
                "---♂--------" +
                "------------" +
                "------------");

        has(DeathRayPerk.class);

        // when
        hero.down();
        game.tick();

        // then
        assertE("------------" +
                "------------" +
                "------------" +
                "---☺--------" +
                "------------" +
                "------------" +
                "------------" +
                "---B--------" +
                "------------" +
                "---♂--------" +
                "------------" +
                "------------");

        has(UnstoppableLaserPerk.class);

        // when
        hero.down();
        hero.fire();
        game.tick();

        // then
        assertE("------------" +
                "------------" +
                "------------" +
                "---☺--------" +
                "---↓--------" +
                "---↓--------" +
                "---↓--------" +
                "---B--------" +
                "---↓--------" +
                "---✝--------" +
                "---↓--------" +
                "------------");

        has(UnstoppableLaserPerk.class);

        has(DeathRayPerk.class);

        // when
        game.tick();

        // then
        assertE("------------" +
                "------------" +
                "------------" +
                "---☺--------" +
                "------------" +
                "------------" +
                "------------" +
                "---B--------" +
                "------------" +
                "------------" +
                "------------" +
                "------------");
    }

    @Test
    public void heroHasDeathRayPerk() {
        // given
        givenFl("╔════┐" +
                "║.S..│" +
                "║....│" +
                "║....│" +
                "║....│" +
                "└────┘");

        game.move(new DeathRayPerk(DEATH_RAY_PERK), 2, 3);

        assertL("╔════┐" +
                "║.S..│" +
                "║.r..│" +
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

        has(DeathRayPerk.class);
    }
}
