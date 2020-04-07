package com.codenjoy.dojo.services.settings;

/*-
 * #%L
 * Codenjoy - it's a dojo-like platform from developers to developers.
 * %%
 * Copyright (C) 2018 Codenjoy
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


import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;

public class SettingsTest {

    private Settings settings;

    @Before
    public void setup() {
        settings = new SettingsImpl();
    }

    @Test
    public void shouldGetAllOptionsContainsCreatedParameter() {
        // given
        Parameter<Integer> edit = settings.addEditBox("edit").type(Integer.class);
        Parameter<Integer> select = settings.addSelect("select", Arrays.asList("option1")).type(Integer.class);
        Parameter<Integer> check = settings.addCheckBox("check").type(Integer.class);

        // when
        List<Parameter<?>> options = settings.getParameters();

        // then
        assertEquals(true, options.contains(edit));
        assertEquals(true, options.contains(select));
        assertEquals(true, options.contains(check));
    }

    @Test
    public void shouldUpdatePreviousIfPresent() {
        // given
        Parameter<Integer> edit = settings.addEditBox("edit").type(Integer.class);

        // when
        Parameter<Integer> edit2 = settings.addEditBox("edit").type(Integer.class);

        // then
        assertSame(edit, edit2);

        List<Parameter<?>> options = settings.getParameters();
        assertEquals(1, options.size());
    }

    @Test
    public void shouldGetParameterByNameReturnsSameParameter() {
        // given
        Parameter<Integer> edit = settings.addEditBox("edit").type(Integer.class).def(5);

        // when
        assertSame(edit, settings.getParameter("edit"));

        // then
        edit.update(12);
        assertEquals(12, edit.getValue().intValue());
    }

    @Test
    public void shouldChangedValueWhenChangeIt() {
        // given
        Parameter<Integer> edit = settings.addEditBox("edit").type(Integer.class);
        Parameter<String> select = settings.addSelect("select", Arrays.asList("option1", "option2")).type(String.class);
        Parameter<Boolean> check = settings.addCheckBox("check");

        // when
        edit.update(12);
        assertEquals(12, edit.getValue().intValue());

        // when then
        select.select(1);
        assertEquals("option2", select.getValue());

        // when then
        check.update(true);
        assertEquals(true, check.getValue());
    }

    @Test
    public void shouldHasParameter() {
        // given
        Parameter<Integer> edit = settings.addEditBox("edit").type(Integer.class).def(5);
        Parameter<String> select = settings.addSelect("select", Arrays.asList("option1", "option2", "option3")).type(String.class).def("option3");
        Parameter<Boolean> check = settings.addCheckBox("check").def(true);

        // when then
        assertEquals(true, settings.hasParameter("edit"));
        assertEquals(true, settings.hasParameter("select"));
        assertEquals(true, settings.hasParameter("check"));

        assertEquals(false, settings.hasParameter("not-exists"));
    }

    @Test
    public void shouldCanSetDefaultValue() {
        // given
        Parameter<Integer> edit = settings.addEditBox("edit").type(Integer.class);
        Parameter<String> select = settings.addSelect("select", Arrays.asList("option1", "option2", "option3")).type(String.class);
        Parameter<Boolean> check = settings.addCheckBox("check");

        // when
        edit.def(5);
        select.def("option3");
        check.def(true);

        // then
        assertEquals(5, edit.getValue().intValue());
        assertEquals("option3", select.getValue());
        assertEquals(true, check.getValue());

        // when then
        edit.update(12);
        assertEquals(12, edit.getValue().intValue());

        // when then
        select.select(1);
        assertEquals("option2", select.getValue());

        // when then
        select.update("option1");
        assertEquals("option1", select.getValue());

        // when then
        check.update(false);
        assertEquals(false, check.getValue());

        // when then
        check.select(1);
        assertEquals(true, check.getValue());
    }

    @Test
    public void shouldDefaultValueIsNullIfNotSet() {
        // given
        Parameter<Integer> edit = settings.addEditBox("edit").type(Integer.class);
        Parameter<String> select = settings.addSelect("select", Arrays.asList("option1", "option2", "option3")).type(String.class);
        Parameter<Boolean> check = settings.addCheckBox("check");

        // when not
        // edit.def(5);
        // select.def("option3");
        // check.def(true);

        // then
        assertEquals(null, edit.getValue());
        assertEquals(null, select.getValue());
        assertEquals(null, check.getValue());
    }

    @Test
    public void shouldGetType() {
        // given
        Parameter<Integer> edit = settings.addEditBox("edit").type(Integer.class);
        Parameter<String> select = settings.addSelect("select", Arrays.asList("option1", "option2", "option3")).type(String.class);
        Parameter<Boolean> check = settings.addCheckBox("check");

        // when then
        assertEquals("editbox", edit.getType());
        assertEquals("selectbox", select.getType());
        assertEquals("checkbox", check.getType());
    }

    @Test
    public void shouldGetOptions() {
        // given
        Parameter<Integer> edit = settings.addEditBox("edit").type(Integer.class).def(42);
        Parameter<String> select = settings.addSelect("select", Arrays.asList("option1", "option2", "option3")).type(String.class);
        Parameter<Boolean> check = settings.addCheckBox("check").def(true);

        edit.update(12);
        select.update("option1");
        check.update(false);

        // when then
        assertEquals("[42, 12]", edit.getOptions().toString());
        assertEquals("[option1, option2, option3]", select.getOptions().toString());
        assertEquals("[true, false]", check.getOptions().toString());
    }

    @Test
    public void shouldSetFlagChangedWhenChangeSomething() {
        // given
        Parameter<Integer> edit = settings.addEditBox("edit").type(Integer.class);
        Parameter<String> select = settings.addSelect("select", Arrays.asList("option1", "option2", "option3")).type(String.class);
        Parameter<Boolean> check = settings.addCheckBox("check");

        assertEquals(false, edit.changed());
        assertEquals(false, select.changed());
        assertEquals(false, check.changed());
        assertEquals(false, settings.changed());

        // when then
        edit.update(1);
        assertEquals(true, edit.changed());
        assertEquals(false, select.changed());
        assertEquals(false, check.changed());
        assertEquals(true, settings.changed());

        // when then
        settings.changesReacted();
        assertEquals(false, edit.changed());
        assertEquals(false, select.changed());
        assertEquals(false, check.changed());
        assertEquals(false, settings.changed());

        // when then
        select.update("option1");
        assertEquals(false, edit.changed());
        assertEquals(true, select.changed());
        assertEquals(false, check.changed());
        assertEquals(true, settings.changed());

        // when then
        settings.changesReacted();
        assertEquals(false, edit.changed());
        assertEquals(false, select.changed());
        assertEquals(false, check.changed());
        assertEquals(false, settings.changed());

        // when then
        check.update(true);
        assertEquals(false, edit.changed());
        assertEquals(false, select.changed());
        assertEquals(true, check.changed());
        assertEquals(true, settings.changed());

        // when then
        settings.changesReacted();
        assertEquals(false, edit.changed());
        assertEquals(false, select.changed());
        assertEquals(false, check.changed());
        assertEquals(false, settings.changed());
    }

    @Test
    public void shouldRemoveParameterByName() {
        // given
        Parameter<Integer> edit = settings.addEditBox("edit").type(Integer.class);
        Parameter<String> select = settings.addSelect("select", Arrays.asList("option1", "option2", "option3")).type(String.class);
        Parameter<Boolean> check = settings.addCheckBox("check");

        // when
        settings.removeParameter("edit");

        // then
        assertEquals(false, settings.hasParameter("edit"));
        assertEquals(true, settings.hasParameter("select"));
        assertEquals(true, settings.hasParameter("check"));
    }

    @Test
    public void shouldClearAllParameters() {
        // given
        Parameter<Integer> edit = settings.addEditBox("edit").type(Integer.class);
        Parameter<String> select = settings.addSelect("select", Arrays.asList("option1", "option2", "option3")).type(String.class);
        Parameter<Boolean> check = settings.addCheckBox("check");

        // when
        settings.clear();

        // then
        assertEquals(false, settings.hasParameter("edit"));
        assertEquals(false, settings.hasParameter("select"));
        assertEquals(false, settings.hasParameter("check"));
    }

    @Test
    public void shouldSetFlagChangedOnlyWhenChangeValue() {
        // given
        Parameter<Integer> edit = settings.addEditBox("edit").type(Integer.class);
        Parameter<String> select = settings.addSelect("select", Arrays.asList("option1", "option2", "option3")).type(String.class);
        Parameter<Boolean> check = settings.addCheckBox("check");

        // when
        edit.update(1);
        select.update("option1");
        check.update(true);
        settings.changesReacted();

        // then
        assertEquals(false, edit.changed());
        assertEquals(false, select.changed());
        assertEquals(false, check.changed());
        assertEquals(false, settings.changed());

        // when
        edit.update(1);
        select.update("option1");
        check.update(true);

        // then
        assertEquals(false, edit.changed());
        assertEquals(false, select.changed());
        assertEquals(false, check.changed());
        assertEquals(false, settings.changed());
    }

    @Test
    public void shouldSetFlagChangedOnlyWhenChangeValue_nullCases() {
        // given
        Parameter<Integer> edit = settings.addEditBox("edit").type(Integer.class);
        Parameter<String> select = settings.addSelect("select", Arrays.asList("option1", "option2", "option3")).type(String.class);
        Parameter<Boolean> check = settings.addCheckBox("check");

        // when
        edit.update(null);
        check.update(null);
        settings.changesReacted();

        // then
        assertEquals(false, edit.changed());
        assertEquals(false, select.changed());
        assertEquals(false, check.changed());
        assertEquals(false, settings.changed());

        // when
        edit.update(null);
        check.update(null);

        // then
        assertEquals(false, edit.changed());
        assertEquals(false, select.changed());
        assertEquals(false, check.changed());
        assertEquals(false, settings.changed());
    }

    @Test
    public void shouldSetStringToBooleanEditBox() {
        // given
        Parameter<Boolean> edit = settings.addEditBox("edit")
                .type(Boolean.class).def(true);

        assertEquals(true, edit.getValue());

        // when
        List<Parameter> parameters = (List)settings.getParameters();
        parameters.get(0).update("false");

        // then
        assertEquals(false, edit.getValue());
    }

    @Test
    public void shouldToBooleanCheckBox() {
        // given
        Parameter<Boolean> edit = settings.addCheckBox("check")
                .type(Boolean.class).def(true);

        assertEquals(true, edit.getValue());

        List<Parameter> parameters = (List)settings.getParameters();
        Parameter parameter = parameters.get(0);

        // when then
        parameter.update("false");
        assertEquals(false, edit.getValue());

        // when then
        parameter.update(true);
        assertEquals(true, edit.getValue());

        // when then
        parameter.update(0);
        assertEquals(false, edit.getValue());

        // when then
        parameter.update("1");
        assertEquals(true, edit.getValue());
    }

    @Test
    public void shouldSetStringToIntegerEditBox() {
        // given
        Parameter<Integer> edit = settings.addEditBox("edit")
                .type(Integer.class).def(21);

        assertEquals(21, edit.getValue().intValue());

        List<Parameter> parameters = (List)settings.getParameters();

        // when then
        parameters.get(0).update("42");
        assertEquals(42, edit.getValue().intValue());
    }

    @Test
    public void shouldToIntegerCheckBox() {
        // given
        Parameter<Integer> edit = settings.addCheckBox("check")
                .type(Integer.class).def(1);

        assertEquals(1, edit.getValue().intValue());

        List<Parameter> parameters = (List)settings.getParameters();
        Parameter parameter = parameters.get(0);

        // when then
        parameter.update("0");
        assertEquals(0, edit.getValue().intValue());

        // when then
        parameter.update(1);
        assertEquals(1, edit.getValue().intValue());

        // when then
        parameter.update(false);
        assertEquals(0, edit.getValue().intValue());

        // when then
        parameter.update("true");
        assertEquals(1, edit.getValue().intValue());
    }

    @Test
    public void shouldSetStringToDoubleEditBox() {
        // given
        Parameter<Double> edit = settings.addEditBox("edit")
                .type(Double.class).def(2.1);

        assertEquals(2.1, edit.getValue(), 0);

        List<Parameter> parameters = (List)settings.getParameters();

        // when then
        parameters.get(0).update("4.2");
        assertEquals(4.2, edit.getValue(), 0);
    }

    @Test
    public void shouldSetStringToStringEditBox() {
        // given
        Parameter<String> edit = settings.addEditBox("edit")
                .type(String.class).def("default");

        assertEquals("default", edit.getValue());

        List<Parameter> parameters = (List)settings.getParameters();

        // when then
        parameters.get(0).update("updated");
        assertEquals("updated", edit.getValue());
    }

    @Test
    public void shouldToStringCheckBox() {
        // given
        Parameter<String> edit = settings.addCheckBox("check")
                .type(String.class).def("false");

        assertEquals("false", edit.getValue());

        List<Parameter> parameters = (List)settings.getParameters();
        Parameter parameter = parameters.get(0);

        // when then
        parameter.update("true");
        assertEquals("true", edit.getValue());

        // when then
        parameter.update(0);
        assertEquals("false", edit.getValue());

        // when then
        parameter.update("1");
        assertEquals("true", edit.getValue());

        // when then
        parameter.update(false);
        assertEquals("false", edit.getValue());
    }

    public static class Foo {
        int a;
        int b;

        public Foo(int a, int b) {
            this.a = a;
            this.b = b;
        }

        @Override
        public String toString() {
            return String.format("[%s,%s]", a, b);
        }
    }

    @Test
    public void shouldSetStringToObjectEditBox() {
        // given
        Parameter<Foo> edit = settings.addEditBox("edit")
                .type(Foo.class).def(new Foo(1, 2))
                .parser(string -> new Foo(
                        Integer.valueOf("" + string.charAt(1)),
                        Integer.valueOf("" + string.charAt(3))));

        assertEquals(1, edit.getValue().a);
        assertEquals(2, edit.getValue().b);

        List<Parameter> parameters = (List)settings.getParameters();

        // when then
        parameters.get(0).update("[3,4]");
        assertEquals(3, edit.getValue().a);
        assertEquals(4, edit.getValue().b);
    }

    @Test
    public void shouldSetStringToObjectCheckBox() {
        // given
        Parameter<Foo> edit = settings.addCheckBox("check")
                .type(Foo.class).def(new Foo(2, 2))
                .parser(string -> new Foo(
                        (Boolean.valueOf(string)) ? 1 : 0,
                        (Boolean.valueOf(string)) ? 0 : 1));

        assertEquals(2, edit.getValue().a);
        assertEquals(2, edit.getValue().b);

        List<Parameter> parameters = (List)settings.getParameters();

        // when then
        parameters.get(0).update(true);
        assertEquals(1, edit.getValue().a);
        assertEquals(0, edit.getValue().b);

        // when then
        parameters.get(0).update(false);
        assertEquals(0, edit.getValue().a);
        assertEquals(1, edit.getValue().b);
    }

    @Test
    public void shouldWhatChanged() {
        // given
        Parameter<Integer> edit = settings.addEditBox("edit").type(Integer.class);
        Parameter<String> select = settings.addSelect("select", Arrays.asList("option1", "option2", "option3")).type(String.class);
        Parameter<Boolean> check = settings.addCheckBox("check");

        // when then
        edit.update(1);
        assertEquals("[edit]", settings.whatChanged().toString());

        // when then
        select.update("option1");
        assertEquals("[edit, select]", settings.whatChanged().toString());

        // when then
        settings.changesReacted();
        check.update(true);
        assertEquals("[check]", settings.whatChanged().toString());

        // when then
        settings.changesReacted();
        assertEquals("[]", settings.whatChanged().toString());

        // when then
        // same value
        edit.update(1);
        select.update("option1");
        check.update(true);
        assertEquals("[]", settings.whatChanged().toString());
    }
}
