package com.codenjoy.dojo.icancode.model.gun;

import com.codenjoy.dojo.icancode.services.SettingsWrapper;
import com.codenjoy.dojo.services.settings.SettingsImpl;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static org.junit.Assert.assertEquals;


@RunWith(Parameterized.class)
public class GunWithOverHeatParamTest {

    public static final String SYMBOL_FIRE = "f";
    public static final String SYMBOL_PAUSE = "-";
    private Gun gun;
    private SettingsWrapper settings;
    private Params params;

    public GunWithOverHeatParamTest(Params params) {
        this.params = params;
        settings = SettingsWrapper.setup(new SettingsImpl());
        gun = new GunWithOverHeat();

    }

    @Parameters
    public static Collection<Params> data() {
        List<Params> data = new ArrayList<>();
        //should shoot without recharge, pause and rest
        data.add(new Params(
                50,
                "ffffffffffffffffffffffffffffffffffffffffffffffffff",
                0,
                0,
                0));

        //should make delay for recharge
        data.add(new Params(
                50,
                "f-f-f-f-f-f-f-f-f-f-f-f-f-f-f-f-f-f-f-f-f-f-f-f-f-",
                1,
                0,
                0));

        //should shoot without recharge and take a rest after long queue
        data.add(new Params(
                50,
                "ffffffffff-----ffffffffff-----ffffffffff-----fffff",
                0,
                5,
                10));

        //should shoot with recharge and take a rest after long queue
        data.add(new Params(
                60,
                "f---f---f---f---f----------f---f---f---f---f----------f---f-",
                3,
                10,
                5));

        //should shoot with recharge and ignore rest after long queue
        data.add(new Params(
                50,
                "f-----f-----f-----f-----f-----f-----f-----f-----f-",
                5,
                3,
                5));

        return data;
    }

    @Test
    public void shouldMakeCorrectShotsInDifferentCases() {
        settings.gunRecharge(params.gunRecharge);
        settings.gunRestTime(params.gunRestTime);
        settings.gunShotQueue(params.gunShotQueue);

        String result = new String();
//        gun.reset();
        for (int i = 0; i < params.shots; i++) {
            if (gun.isCanShoot()) {
                gun.shoot();
                result += SYMBOL_FIRE;
            } else {
                result += SYMBOL_PAUSE;
            }
            gun.tick();
        }
        assertEquals(params.expected, result);
    }

    static class Params {
        int shots;
        String expected;
        int gunRecharge;
        int gunRestTime;
        int gunShotQueue;

        public Params(int shots, String expected, int gunRecharge, int gunRestTime, int gunShotQueue) {
            this.shots = shots;
            this.expected = expected;
            this.gunRecharge = gunRecharge;
            this.gunRestTime = gunRestTime;
            this.gunShotQueue = gunShotQueue;
        }
    }
}