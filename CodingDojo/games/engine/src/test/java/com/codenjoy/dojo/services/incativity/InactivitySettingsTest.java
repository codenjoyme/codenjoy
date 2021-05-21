package com.codenjoy.dojo.services.incativity;

import com.codenjoy.dojo.services.settings.SettingsImpl;
import com.codenjoy.dojo.services.settings.SomeInactivitySettings;
import com.codenjoy.dojo.services.settings.SomeRoundSettings;
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
        SettingsImpl settings = givenAllPropertiesCopied();

        // then
        assertEquals(true, InactivitySettings.is(settings));
    }

    @Test
    public void testIf_caseNotAllPropertiesCopied() {
        // given when
        SettingsImpl settings = givenAllPropertiesCopied();

        settings.removeParameter(InactivitySettings.Keys.INACTIVITY_TIMEOUT.key());

        // then
        assertEquals(false, InactivitySettings.is(settings));
    }

    @Test
    public void testGet_caseAllPropertiesCopied() {
        // given when
        SettingsImpl settings = givenAllPropertiesCopied();

        // then
        assertEquals("SettingsImpl(map={[Inactivity] Kick inactive players=[[Inactivity] Kick inactive players:Boolean = def[false] val[false]], \n" +
                "[Inactivity] Inactivity timeout ticks=[[Inactivity] Inactivity timeout ticks:Integer = multiline[false] def[300] val[300]]})",
                InactivitySettings.get(settings).toString().replace(", [Inactivity]", ", \n[Inactivity]"));
    }

    private SettingsImpl givenAllPropertiesCopied() {
        return new SettingsImpl(){{
            new SomeInactivitySettings().getInactivityParams()
                    .forEach(param -> map.put(param.getName(), param));
        }};
    }

    @Test
    public void testGet_caseInactivityInstance() {
        // given when
        SettingsImpl settings = new SomeInactivitySettings();

        // then
        assertEquals("Some[[Inactivity] Kick inactive players=false, \n" +
                        "[Inactivity] Inactivity timeout ticks=300, \n" +
                        "Parameter 1=15, \n" +
                        "Parameter 2=true, \n" +
                        "Parameter 3=0.5, \n" +
                        "Parameter 4=string]",
                InactivitySettings.get(settings).toString().replace(", ", ", \n"));
    }

    @Test
    public void testGet_caseNotInactivityInstance() {
        // given when
        SettingsImpl settings = new SomeRoundSettings();

        // then
        assertEquals("SettingsImpl(map={})",
                InactivitySettings.get(settings).toString());
    }

    @Test
    public void testGet_caseNull() {
        // given when
        SettingsImpl settings = null;

        // then
        assertEquals("SettingsImpl(map={})",
                InactivitySettings.get(settings).toString());
    }
}