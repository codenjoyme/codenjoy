package com.codenjoy.dojo.services.settings;

import junit.framework.Assert;
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

        Parameter<Integer> edit = settings.addEditBox("edit").type(Integer.class);
        Parameter<Integer> select = settings.addSelect("select", Arrays.<Object>asList("option1")).type(Integer.class);
        Parameter<Integer> check = settings.addCheckBox("check").type(Integer.class);

        List<Parameter<?>> options = settings.getParameters();
        assertTrue(options.contains(edit));
        assertTrue(options.contains(select));
        assertTrue(options.contains(check));
    }

    @Test
    public void shouldUpdatePreviousIfPresent() {
        Settings settings = new SettingsImpl();

        Parameter<Integer> edit = settings.addEditBox("edit").type(Integer.class);
        Parameter<Integer> edit2 = settings.addEditBox("edit").type(Integer.class);

        Assert.assertSame(edit, edit2);

        List<Parameter<?>> options = settings.getParameters();
        assertEquals(1, options.size());
    }

    @Test
    public void shouldGetParameterByNameReturnsSameParameter() {
        Settings settings = new SettingsImpl();

        Parameter<Integer> edit = settings.addEditBox("edit").type(Integer.class).def(5);

        assertSame(edit, settings.getParameter("edit"));

        edit.update(12);
        assertEquals(12, edit.getValue().intValue());

    }

    @Test
     public void shouldChangedValueWhenChangeIt() {
        Settings settings = new SettingsImpl();

        Parameter<Integer> edit = settings.addEditBox("edit").type(Integer.class);
        Parameter<String> select = settings.addSelect("select", Arrays.<Object>asList("option1", "option2")).type(String.class);
        Parameter<Boolean> check = settings.addCheckBox("check");

        edit.update(12);
        assertEquals(12, edit.getValue().intValue());

        select.select(1);
        assertEquals("option2", select.getValue());

        check.update(true);
        assertEquals(true, check.getValue());
    }

    @Test
    public void shouldCanSetDefaultValue() {
        Settings settings = new SettingsImpl();

        Parameter<Integer> edit = settings.addEditBox("edit").type(Integer.class).def(5);
        Parameter<String> select = settings.addSelect("select", Arrays.<Object>asList("option1", "option2", "option3")).type(String.class).def("option3");
        Parameter<Boolean> check = settings.addCheckBox("check").def(true);

        assertEquals(5, edit.getValue().intValue());
        assertEquals("option3", select.getValue());
        assertEquals(true, check.getValue());

        edit.update(12);
        assertEquals(12, edit.getValue().intValue());

        select.select(1);
        assertEquals("option2", select.getValue());

        select.update("option1");
        assertEquals("option1", select.getValue());

        check.update(false);
        assertEquals(false, check.getValue());

        check.select(1);
        assertEquals(true, check.getValue());
    }

    @Test
    public void shouldDefaultValueIsNullIfNotSet() {
        Settings settings = new SettingsImpl();

        Parameter<Integer> edit = settings.addEditBox("edit").type(Integer.class);
        Parameter<String> select = settings.addSelect("select", Arrays.<Object>asList("option1", "option2", "option3")).type(String.class);
        Parameter<Boolean> check = settings.addCheckBox("check");


        assertNull(edit.getValue());
        assertNull("option2", select.getValue());
        assertNull(check.getValue());
    }
}
