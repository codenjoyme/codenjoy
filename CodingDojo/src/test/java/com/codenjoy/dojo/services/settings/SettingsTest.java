package com.codenjoy.dojo.services.settings;

import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static junit.framework.Assert.assertNull;
import static junit.framework.Assert.assertTrue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;

/**
 * User: sanja
 * Date: 26.09.13
 * Time: 8:45
 */
public class SettingsTest {

    @Test
    public void shouldGetAllOptionsContainsCreatedParameter() {
        Settings settings = new SettingsImpl();

        Parameter edit = settings.addEditBox("edit");
        Parameter select = settings.addSelect("select", Arrays.<Object>asList("option1"));
        Parameter check = settings.addCheckBox("check");

        List<Parameter> options = settings.getParameters();
        assertTrue(options.contains(edit));
        assertTrue(options.contains(select));
        assertTrue(options.contains(check));
    }

    @Test
    public void shouldGetParameterByNameReturnsSameParameter() {
        Settings settings = new SettingsImpl();

        Parameter edit = settings.addEditBox("edit").def(5);

        assertSame(edit, settings.getParameter("edit"));

        edit.update(12);
        assertEquals(12, edit.getValue());

    }

    @Test
     public void shouldChangedValueWhenChangeIt() {
        Settings settings = new SettingsImpl();

        Parameter edit = settings.addEditBox("edit");
        Parameter select = settings.addSelect("select", Arrays.<Object>asList("option1", "option2"));
        Parameter check = settings.addCheckBox("check");

        edit.update(12);
        assertEquals(12, edit.getValue());

        select.update(1);
        assertEquals("option2", select.getValue());

        check.update(true);
        assertEquals(true, check.getValue());
    }

    @Test
    public void shouldCanSetDefaultValue() {
        Settings settings = new SettingsImpl();

        Parameter edit = settings.addEditBox("edit").def(5);
        Parameter select = settings.addSelect("select", Arrays.<Object>asList("option1", "option2", "option3")).def(2);
        Parameter check = settings.addCheckBox("check").def(true);

        assertEquals(5, edit.getValue());
        assertEquals("option3", select.getValue());
        assertEquals(true, check.getValue());

        edit.update(12);
        assertEquals(12, edit.getValue());

        select.update(1);
        assertEquals("option2", select.getValue());

        check.update(false);
        assertEquals(false, check.getValue());
    }

    @Test
    public void shouldDefaultValueIsNullIfNotSet() {
        Settings settings = new SettingsImpl();

        Parameter edit = settings.addEditBox("edit");
        Parameter select = settings.addSelect("select", Arrays.<Object>asList("option1", "option2", "option3"));
        Parameter check = settings.addCheckBox("check");


        assertNull(edit.getValue());
        assertNull("option2", select.getValue());
        assertNull(check.getValue());
    }
}
