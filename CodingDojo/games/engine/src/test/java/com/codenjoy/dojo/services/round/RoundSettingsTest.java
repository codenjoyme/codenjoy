package com.codenjoy.dojo.services.round;

import com.codenjoy.dojo.services.settings.SettingsImpl;
import com.codenjoy.dojo.services.settings.SomeRoundSettings;
import com.codenjoy.dojo.services.settings.SomeSemifinalSettings;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

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

    @Test
    public void testGet_caseAllPropertiesCopied() {
        // given when
        SettingsImpl settings = givenAllPropertiesCopied();

        // then
        assertEquals("SettingsImpl(map={[Rounds] Enabled=[[Rounds] Enabled:Boolean = def[true] val[true]], \n" +
                        "[Rounds] Players per room=[[Rounds] Players per room:Integer = multiline[false] def[5] val[5]], \n" +
                        "[Rounds] Time per Round=[[Rounds] Time per Round:Integer = multiline[false] def[200] val[200]], \n" +
                        "[Rounds] Time for Winner=[[Rounds] Time for Winner:Integer = multiline[false] def[1] val[1]], \n" +
                        "[Rounds] Time before start Round=[[Rounds] Time before start Round:Integer = multiline[false] def[5] val[5]], \n" +
                        "[Rounds] Rounds per Match=[[Rounds] Rounds per Match:Integer = multiline[false] def[1] val[1]], \n" +
                        "[Rounds] Min ticks for win=[[Rounds] Min ticks for win:Integer = multiline[false] def[1] val[1]]})",
                RoundSettings.get(settings).toString().replace(", [Rounds]", ", \n[Rounds]"));
    }

    private SettingsImpl givenAllPropertiesCopied() {
        return new SettingsImpl(){{
            new SomeRoundSettings().getRoundParams()
                    .forEach(param -> map.put(param.getName(), param));
        }};
    }

    @Test
    public void testGet_caseRoundInstance() {
        // given when
        SettingsImpl settings = new SomeRoundSettings();

        // then
        assertEquals("Some[[Rounds] Enabled=true, \n" +
                        "[Rounds] Players per room=5, \n" +
                        "[Rounds] Time per Round=200, \n" +
                        "[Rounds] Time for Winner=1, \n" +
                        "[Rounds] Time before start Round=5, \n" +
                        "[Rounds] Rounds per Match=1, \n" +
                        "[Rounds] Min ticks for win=1, \n" +
                        "Parameter 1=15, \n" +
                        "Parameter 2=true, \n" +
                        "Parameter 3=0.5, \n" +
                        "Parameter 4=string]",
                RoundSettings.get(settings).toString().replace(", ", ", \n"));
    }

    @Test
    public void testGet_caseNotRoundInstance() {
        // given when
        SettingsImpl settings = new SomeSemifinalSettings();

        // then
        assertEquals("SettingsImpl(map={})",
                RoundSettings.get(settings).toString());
    }

    @Test
    public void testGet_caseNull() {
        // given when
        SettingsImpl settings = null;

        // then
        assertEquals("SettingsImpl(map={})",
                RoundSettings.get(settings).toString());
    }
}