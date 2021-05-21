package com.codenjoy.dojo.services.incativity;

import com.codenjoy.dojo.services.settings.SettingsImpl;
import com.codenjoy.dojo.services.settings.SomeInactivitySettings;
import org.junit.Test;

import static org.junit.Assert.*;

public class InactivitySettingsTest {
    
    @Test
    public void testIf_caseNull() {
        assertEquals(false, InactivitySettings.is(null));
    }

    @Test
    public void testIf_caseNotInactivityInstance() {
        assertEquals(false, InactivitySettings.is(new SettingsImpl()));
    }

    @Test
    public void testIf_caseInactivityInstance() {
        assertEquals(true, InactivitySettings.is(new InactivitySettingsImpl()));
    }

    @Test
    public void testIf_caseAllPropertiesCopied() {
        // given when
        SettingsImpl settings = new SettingsImpl(){{
            new SomeInactivitySettings().getInactivityParams()
                    .forEach(param -> map.put(param.getName(), param));
        }};

        // then
        assertEquals(true, InactivitySettings.is(settings));
    }

    @Test
    public void testIf_caseNotAllPropertiesCopied() {
        // given when
        SettingsImpl settings = new SettingsImpl(){{
            new SomeInactivitySettings().getInactivityParams()
                    .forEach(param -> map.put(param.getName(), param));
        }};

        settings.removeParameter(InactivitySettings.Keys.INACTIVITY_TIMEOUT.key());

        // then
        assertEquals(false, InactivitySettings.is(settings));
    }
}