package com.codenjoy.dojo.services.mocks;

import com.codenjoy.dojo.services.settings.SettingsImpl;
import com.codenjoy.dojo.services.settings.SettingsReader;

import static com.codenjoy.dojo.services.mocks.SecondGameSettings.Keys.PARAMETER3;
import static com.codenjoy.dojo.services.mocks.SecondGameSettings.Keys.PARAMETER4;

public class SecondGameSettings extends SettingsImpl implements SettingsReader<FirstGameSettings> {

    public enum Keys implements Key {

        PARAMETER3("Parameter 3"),
        PARAMETER4("Parameter 4");

        private String key;

        Keys(String key) {
            this.key = key;
        }

        @Override
        public String key() {
            return key;
        }
    }

    public SecondGameSettings() {
        integer(PARAMETER3, 43);
        bool(PARAMETER3, false);
        boolValue(PARAMETER4).update(true);
    }

    @Override
    public String toString() {
        return "Second-" + super.toString() + "";
    }
}
