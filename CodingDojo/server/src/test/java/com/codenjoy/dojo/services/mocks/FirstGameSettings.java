package com.codenjoy.dojo.services.mocks;

import com.codenjoy.dojo.services.settings.SettingsImpl;
import com.codenjoy.dojo.services.settings.SettingsReader;

import java.util.List;

import static com.codenjoy.dojo.services.mocks.FirstGameSettings.Keys.PARAMETER1;
import static com.codenjoy.dojo.services.mocks.FirstGameSettings.Keys.PARAMETER2;
import static java.util.stream.Collectors.toList;

public class FirstGameSettings extends SettingsImpl implements SettingsReader<FirstGameSettings> {

    public enum Keys implements Key {

        PARAMETER1("Parameter 1"),
        PARAMETER2("Parameter 2");

        private String key;

        Keys(String key) {
            this.key = key;
        }

        @Override
        public String key() {
            return key;
        }
    }

    public FirstGameSettings() {
        integer(PARAMETER1, 12);
        integerValue(PARAMETER1).update(15);
        bool(PARAMETER2, true);
    }

    @Override
    public String toString() {
        return "First" + super.toStringShort();
    }
}
