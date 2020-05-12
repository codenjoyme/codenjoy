package com.codenjoy.dojo.bomberman.services;

import com.codenjoy.dojo.bomberman.model.Elements;
import com.codenjoy.dojo.bomberman.model.GameSettings;
import com.codenjoy.dojo.bomberman.model.perks.PerkSettings;
import com.codenjoy.dojo.bomberman.model.perks.PerksSettingsWrapper;
import com.codenjoy.dojo.services.Dice;
import com.codenjoy.dojo.services.settings.Settings;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class OptionGameSettingsTest {
    @Mock
    private Dice dice;
    @Mock
    Settings settings;

    @Test
    @Ignore("TODO: mock GameSettings properly")
    public void shouldBombermanContainPerksSettings_whenCreated() {
        PerkSettings perkSettings = PerksSettingsWrapper.getPerkSettings(Elements.BOMB_IMMUNE);

        assertPerkSettings(Elements.BOMB_BLAST_RADIUS_INCREASE, perkSettings,2, 10);
        assertPerkSettings(Elements.BOMB_COUNT_INCREASE, perkSettings,3, 10);
        assertPerkSettings(Elements.BOMB_IMMUNE, perkSettings,0, 10);
//        assertPerkSettings(Elements.BOMB_REMOTE_CONTROL, perkSettings,0, 10);
    }

    private boolean assertPerkSettings(Elements perk, PerkSettings perkSettings, int value, int timeout) {
        String message = "";
        String defaultValueErrorPattern = "%s expected default value = %d, but found it = %d \n";
        String defaultTimeoutErrorPattern = "%s expected default timeout = %d, but found it = %d \n";

        if(perkSettings.getValue() != value) {
            message += String.format(defaultValueErrorPattern, perk.name(), value, perkSettings.getValue());
        }

        if(perkSettings.getTimeout() != timeout) {
            message += String.format(defaultTimeoutErrorPattern, perk.name(), timeout, perkSettings.getTimeout());
        }

        if(message.isEmpty()) {
            return true;
        } else {
            throw  new AssertionError(message);
        }
    }
}