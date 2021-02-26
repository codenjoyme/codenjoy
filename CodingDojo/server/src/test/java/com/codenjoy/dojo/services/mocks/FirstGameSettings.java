package com.codenjoy.dojo.services.mocks;

import com.codenjoy.dojo.services.settings.SettingsImpl;
import com.codenjoy.dojo.services.settings.SettingsReader;

public class FirstGameSettings extends SettingsImpl implements SettingsReader<FirstGameSettings> {

    public FirstGameSettings() {
        integer(() -> "Parameter 1", 12);
        integerValue(() -> "Parameter 1").update(15);
        bool(() -> "Parameter 2", true);
    }

    @Override
    public String toString() {
        return "First-" + super.toString() + "";
    }
}
