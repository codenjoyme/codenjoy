package com.codenjoy.dojo.icancode.model.gun;

import com.codenjoy.dojo.icancode.services.SettingsWrapper;
import com.codenjoy.dojo.services.settings.SettingsImpl;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;


public class GunWithOverHeatUniqTest {

    public static final String SYMBOL_FIRE = "f";
    public static final String SYMBOL_PAUSE = "-";
    private Gun gun;
    private SettingsWrapper settings;

    @Before
    public void setup() {
        settings = SettingsWrapper.setup(new SettingsImpl())
                .gunRecharge(1)
                .gunRestTime(10)
                .gunShotQueue(10);
        gun = new GunWithOverHeat();
    }

    @Test
    public void shouldMakeCorrectShotsWithLittleRestBeetwenShotsLongerThanRecharge_Case1() {
        String result = new String();

        //should shot 3 times with recharge
        for (int i = 0; i < 5; i++) result += getResultOfAttemptShot();
        //then
        assertEquals("f-f-f", result);

        //don't shoot for 4 ticks. The gun must be a little cool down
        for (int i = 0; i < 5; i++) {
            gun.tick();
            result+=SYMBOL_PAUSE;
        }
        //then
        assertEquals("f-f-f-----", result);

        //should make fire queue with 9 shots with recharge and rest after it. And then full 10 shots queue
        for (int i = 0; i < 50; i++) result += getResultOfAttemptShot();
        //then
        assertEquals("f-f-f-----f-f-f-f-f-f-f-f-f----------f-f-f-f-f-f-f-f-f-f----", result);
    }

    @Test
    public void shouldMakeCorrectShotsWithLittleRestBeetwenShotsLongerThanRecharge_Case2() {
        String result = new String();

        //should shot 7 times with recharge
        for (int i = 0; i < 13; i++) result += getResultOfAttemptShot();
        //then
        assertEquals("f-f-f-f-f-f-f", result);

        //don't shoot for 5 ticks. The gun must be a little cool down
        for (int i = 0; i < 5; i++) {
            gun.tick();
            result+=SYMBOL_PAUSE;
        }
        //then
        assertEquals("f-f-f-f-f-f-f-----", result);

        //should make fire queue with 5 shots with recharge and rest after it. And then full 10 shots queue after rest
        for (int i = 0; i < 51; i++) result += getResultOfAttemptShot();
        //then
        assertEquals("f-f-f-f-f-f-f-----f-f-f-f-f----------f-f-f-f-f-f-f-f-f-f----------f-f", result);
    }

    private String getResultOfAttemptShot() {
        String s;
        if (gun.isCanShoot()) {
            gun.shoot();
            s = SYMBOL_FIRE;
        } else {
            s = SYMBOL_PAUSE;
        }
        gun.tick();
        return s;
    }
}