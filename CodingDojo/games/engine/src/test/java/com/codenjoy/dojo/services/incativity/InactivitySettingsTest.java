package com.codenjoy.dojo.services.incativity;

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
import com.codenjoy.dojo.services.settings.SomeInactivitySettings;
import com.codenjoy.dojo.services.settings.SomeRoundSettings;
import org.junit.Test;

import static com.codenjoy.dojo.utils.TestUtils.split;
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
                split(InactivitySettings.get(settings), ", \n[Inactivity]"));
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
                split(InactivitySettings.get(settings), ", \n"));
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
