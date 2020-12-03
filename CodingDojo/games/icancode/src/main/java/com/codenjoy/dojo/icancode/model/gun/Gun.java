package com.codenjoy.dojo.icancode.model.gun;

import com.codenjoy.dojo.icancode.services.SettingsWrapper;
import com.codenjoy.dojo.services.Tickable;

public interface Gun extends Tickable {
    void reset();

    boolean isCanShoot();

    void shoot();

    void unlimitedShoot();

    default int getChargePoints() {
        return SettingsWrapper.data.gunRecharge();
    }
}