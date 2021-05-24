package com.codenjoy.dojo.services.semifinal;

/*-
 * #%L
 * Codenjoy - it's a dojo-like platform from developers to developers.
 * %%
 * Copyright (C) 2018 - 2021 Codenjoy
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/gpl-3.0.html>.
 * #L%
 */

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
        assertEquals("Some[[Semifinal] Enabled=false, \n" +
                        "[Semifinal] Timeout=900, \n" +
                        "[Semifinal] Percentage=true, \n" +
                        "[Semifinal] Limit=50, \n" +
                        "[Semifinal] Reset board=true, \n" +
                        "[Semifinal] Shuffle board=true, \n" +
                        "Parameter 1=15, \n" +
                        "Parameter 2=true, \n" +
                        "Parameter 3=0.5, \n" +
                        "Parameter 4=string]",
                SemifinalSettings.get(settings).toString().replace(", ", ", \n"));
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
