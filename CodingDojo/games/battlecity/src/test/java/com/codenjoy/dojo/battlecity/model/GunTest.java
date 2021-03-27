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


import com.codenjoy.dojo.battlecity.TestGameSettings;
import com.codenjoy.dojo.battlecity.services.GameSettings;
import org.junit.Before;
import org.junit.Test;

import static com.codenjoy.dojo.battlecity.services.GameSettings.Keys.TANK_TICKS_PER_SHOOT;
import static org.junit.Assert.*;

public class GunTest {

    private GameSettings settings;

    @Before
    public void setup() {
        settings = new TestGameSettings();
    }
    
    @Test
    public void test5ticks() {
        // given
        settings.integer(TANK_TICKS_PER_SHOOT, 5);
        
        Gun gun = new Gun(settings);

        // when then
        assertEquals(true, gun.tryToFire());
        assertEquals(false, gun.tryToFire());

        // when then
        gun.tick();
        gun.tick();
        gun.tick();
        gun.tick();
        assertEquals(false, gun.tryToFire());

        // when then
        gun.tick();
        assertEquals(true, gun.tryToFire());
        assertEquals(false, gun.tryToFire());
    }

    @Test
    public void test2Ticks() {
        // given
        settings.integer(TANK_TICKS_PER_SHOOT, 2);
        
        Gun gun = new Gun(settings);

        // when then
        assertEquals(true, gun.tryToFire());
        assertEquals(false, gun.tryToFire());

        // when then
        gun.tick();
        assertEquals(false, gun.tryToFire());

        // when then
        gun.tick();
        assertEquals(true, gun.tryToFire());
        assertEquals(false, gun.tryToFire());
    }

    @Test
    public void test1Ticks() {
        // given
        settings.integer(TANK_TICKS_PER_SHOOT, 1);

        Gun gun = new Gun(settings);

        // when then
        assertEquals(true, gun.tryToFire());
        assertEquals(false, gun.tryToFire());

        // when then
        gun.tick();
        assertEquals(true, gun.tryToFire());
        assertEquals(false, gun.tryToFire());
    }

    @Test
    public void test0Ticks() {
        // given
        settings.integer(TANK_TICKS_PER_SHOOT, 0);
        
        Gun gun = new Gun(settings);

        // when then
        assertEquals(true, gun.tryToFire());

        // when then
        gun.tick();
        assertEquals(true, gun.tryToFire());
    }

    @Test
    public void testReset() {
        // given
        settings.integer(TANK_TICKS_PER_SHOOT, 5);

        Gun gun = new Gun(settings);

        // when then
        assertEquals(true, gun.tryToFire());
        assertEquals(false, gun.tryToFire());

        // when then
        gun.tick();
        assertEquals(false, gun.tryToFire());
        assertEquals(false, gun.tryToFire());

        // when then
        gun.tick();
        assertEquals(false, gun.tryToFire());
        assertEquals(false, gun.tryToFire());

        // when then
        gun.reset();
        assertEquals(true, gun.tryToFire());
        assertEquals(false, gun.tryToFire());
    }
}
