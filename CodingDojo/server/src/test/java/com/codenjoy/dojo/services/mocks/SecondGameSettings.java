package com.codenjoy.dojo.services.mocks;

import com.codenjoy.dojo.services.settings.SettingsImpl;
import com.codenjoy.dojo.services.settings.SettingsReader;

public class SecondGameSettings extends SettingsImpl implements SettingsReader<FirstGameSettings> {

    public SecondGameSettings() {
        integer(() -> "Parameter 3", 43);
        bool(() -> "Parameter 4", false);
        boolValue(() -> "Parameter 4").update(true);
    }

    @Override
    public String toString() {
        return "Second-" + super.toString() + "";
    }
}
