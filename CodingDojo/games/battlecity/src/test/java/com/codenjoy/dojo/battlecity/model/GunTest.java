package com.codenjoy.dojo.battlecity.model;

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


import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class GunTest {

    @Test
     public void test5ticks() {
        Gun gun = new Gun(5);

        assertTrue(gun.tryToFire());
        assertFalse(gun.tryToFire());

        gun.tick();
        gun.tick();
        gun.tick();
        gun.tick();
        assertFalse(gun.tryToFire());
        gun.tick();

        assertTrue(gun.tryToFire());
        assertFalse(gun.tryToFire());
    }

    @Test
    public void test2Ticks() {
        Gun gun = new Gun(2);

        assertTrue(gun.tryToFire());
        assertFalse(gun.tryToFire());

        gun.tick();
        assertFalse(gun.tryToFire());
        gun.tick();

        assertTrue(gun.tryToFire());
        assertFalse(gun.tryToFire());
    }

    @Test
    public void test1Ticks() {
        Gun gun = new Gun(1);

        assertTrue(gun.tryToFire());
        assertFalse(gun.tryToFire());

        gun.tick();

        assertTrue(gun.tryToFire());
        assertFalse(gun.tryToFire());
    }

    @Test
    public void test0Ticks() {
        Gun gun = new Gun(0);

        assertTrue(gun.tryToFire());

        gun.tick();

        assertTrue(gun.tryToFire());
    }

    @Test
    public void testReset() {
        // given
        Gun gun = new Gun(5);

        assertTrue(gun.tryToFire());
        assertFalse(gun.tryToFire());

        gun.tick();

        assertFalse(gun.tryToFire());
        assertFalse(gun.tryToFire());

        gun.tick();

        assertFalse(gun.tryToFire());
        assertFalse(gun.tryToFire());

        // when
        gun.reset();

        // then
        assertTrue(gun.tryToFire());
        assertFalse(gun.tryToFire());
    }
}
