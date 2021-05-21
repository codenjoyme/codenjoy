package com.codenjoy.dojo.services.semifinal;

import com.codenjoy.dojo.services.settings.SettingsImpl;
import com.codenjoy.dojo.services.settings.SomeRoundSettings;
import com.codenjoy.dojo.services.settings.SomeSemifinalSettings;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

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
        SettingsImpl settings = givenAllPropertiesCopied();

        // then
        assertEquals(true, SemifinalSettings.is(settings));
    }

    @Test
    public void testIf_caseNotAllPropertiesCopied() {
        // given when
        SettingsImpl settings = givenAllPropertiesCopied();

        settings.removeParameter(SemifinalSettings.Keys.SEMIFINAL_LIMIT.key());

        // then
        assertEquals(false, SemifinalSettings.is(settings));
    }

    @Test
    public void testGet_caseAllPropertiesCopied() {
        // given when
        SettingsImpl settings = givenAllPropertiesCopied();

        // then
        assertEquals("SettingsImpl(map={[Semifinal] Enabled=[[Semifinal] Enabled:Boolean = def[false] val[false]], \n" +
                        "[Semifinal] Timeout=[[Semifinal] Timeout:Integer = multiline[false] def[900] val[900]], \n" +
                        "[Semifinal] Percentage=[[Semifinal] Percentage:Boolean = def[true] val[true]], \n" +
                        "[Semifinal] Limit=[[Semifinal] Limit:Integer = multiline[false] def[50] val[50]], \n" +
                        "[Semifinal] Reset board=[[Semifinal] Reset board:Boolean = def[true] val[true]], \n" +
                        "[Semifinal] Shuffle board=[[Semifinal] Shuffle board:Boolean = def[true] val[true]]})",
                SemifinalSettings.get(settings).toString().replace(", [Semifinal]", ", \n[Semifinal]"));
    }

    private SettingsImpl givenAllPropertiesCopied() {
        return new SettingsImpl(){{
            new SomeSemifinalSettings().getSemifinalParams()
                    .forEach(param -> map.put(param.getName(), param));
        }};
    }

    @Test
    public void testGet_caseSemifinalInstance() {
        // given when
        SettingsImpl settings = new SomeSemifinalSettings();

        // then
        assertEquals("Some[[Semifinal] Enabled=false, " +
                        "[Semifinal] Timeout=900, " +
                        "[Semifinal] Percentage=true, " +
                        "[Semifinal] Limit=50, " +
                        "[Semifinal] Reset board=true, " +
                        "[Semifinal] Shuffle board=true, " +
                        "Parameter 1=15, " +
                        "Parameter 2=true, " +
                        "Parameter 3=0.5, " +
                        "Parameter 4=string]",
                SemifinalSettings.get(settings).toString());
    }

    @Test
    public void testGet_caseNotSemifinalInstance() {
        // given when
        SettingsImpl settings = new SomeRoundSettings();

        // then
        assertEquals("SettingsImpl(map={})",
                SemifinalSettings.get(settings).toString());
    }

    @Test
    public void testGet_caseNull() {
        // given when
        SettingsImpl settings = null;

        // then
        assertEquals("SettingsImpl(map={})",
                SemifinalSettings.get(settings).toString());
    }
}