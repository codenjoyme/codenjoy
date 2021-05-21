package com.codenjoy.dojo.services.semifinal;

import com.codenjoy.dojo.services.settings.SettingsImpl;
import com.codenjoy.dojo.services.settings.SomeRoundSettings;
import com.codenjoy.dojo.services.settings.SomeSemifinalSettings;
import org.junit.Test;

import static org.junit.Assert.*;

public class SemifinalSettingsTest {

    @Test
    public void testIf_caseNull() {
        assertEquals(false, SemifinalSettings.is(null));
    }

    @Test
    public void testIf_caseNotSemifinalInstance() {
        assertEquals(false, SemifinalSettings.is(new SettingsImpl()));
    }

    @Test
    public void testIf_caseSemifinalInstance() {
        assertEquals(true, SemifinalSettings.is(new SemifinalSettingsImpl()));
    }

    @Test
    public void testIf_caseAllPropertiesCopied() {
        // given when
        SettingsImpl settings = new SettingsImpl(){{
            new SomeSemifinalSettings().getSemifinalParams()
                    .forEach(param -> map.put(param.getName(), param));
        }};

        // then
        assertEquals(true, SemifinalSettings.is(settings));
    }

    @Test
    public void testIf_caseNotAllPropertiesCopied() {
        // given when
        SettingsImpl settings = new SettingsImpl(){{
            new SomeSemifinalSettings().getSemifinalParams()
                    .forEach(param -> map.put(param.getName(), param));
        }};

        settings.removeParameter(SemifinalSettings.Keys.SEMIFINAL_LIMIT.key());

        // then
        assertEquals(false, SemifinalSettings.is(settings));
    }
}