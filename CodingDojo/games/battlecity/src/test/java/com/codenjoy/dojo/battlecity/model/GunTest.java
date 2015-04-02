package com.codenjoy.dojo.battlecity.model;

import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * User: sanja
 * Date: 14.12.13
 * Time: 8:09
 */
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
}
