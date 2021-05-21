package com.codenjoy.dojo.services.round;

import com.codenjoy.dojo.services.settings.SettingsImpl;
import com.codenjoy.dojo.services.settings.SomeRoundSettings;
import org.junit.Test;

import static org.junit.Assert.*;

public class RoundSettingsTest {

    @Test
    public void testIf_caseNull() {
        assertEquals(false, RoundSettings.is(null));
    }

    @Test
    public void testIf_caseNotRoundInstance() {
        assertEquals(false, RoundSettings.is(new SettingsImpl()));
    }

    @Test
    public void testIf_caseRoundInstance() {
        assertEquals(true, RoundSettings.is(new RoundSettingsImpl()));
    }

    @Test
    public void testIf_caseAllPropertiesCopied() {
        // given when
        SettingsImpl settings = new SettingsImpl(){{
            new SomeRoundSettings().getRoundParams()
                    .forEach(param -> map.put(param.getName(), param));
        }};

        // then
        assertEquals(true, RoundSettings.is(settings));
    }

    @Test
    public void testIf_caseNotAllPropertiesCopied() {
        // given when
        SettingsImpl settings = new SettingsImpl(){{
            new SomeRoundSettings().getRoundParams()
                    .forEach(param -> map.put(param.getName(), param));
        }};

        settings.removeParameter(RoundSettings.Keys.ROUNDS_TIME.key());

        // then
        assertEquals(false, RoundSettings.is(settings));
    }
}