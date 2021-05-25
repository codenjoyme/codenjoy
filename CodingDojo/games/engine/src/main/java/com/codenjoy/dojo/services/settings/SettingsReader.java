package com.codenjoy.dojo.services.settings;

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

import com.codenjoy.dojo.services.incativity.InactivitySettings;
import com.codenjoy.dojo.services.round.RoundSettings;
import com.codenjoy.dojo.services.semifinal.SemifinalSettings;
import org.json.JSONObject;

import java.util.List;
import java.util.Optional;

public interface SettingsReader<T extends SettingsReader> {

    interface Key {
        String key();

        static Optional<String> keyToName(List<Key> values, String value) {
            return values.stream()
                    .filter(element -> element.key().equals(value))
                    .map(Key::toString)
                    .findFirst();
        }

        static Optional<String> nameToKey(List<Key> values, String value) {
            return values.stream()
                    .filter(element -> element.toString().equals(value))
                    .map(Key::key)
                    .findFirst();
        }
    }

    static String keyToName(List<Key> values, String value) {
        return Key.keyToName(values, value)
                .or(() -> RoundSettings.keyToName(RoundSettings.allRoundsKeys(), value))
                .or(() -> SemifinalSettings.keyToName(SemifinalSettings.allSemifinalKeys(), value))
                .or(() -> InactivitySettings.keyToName(InactivitySettings.allInactivityKeys(), value))
                .orElseThrow(() -> new IllegalArgumentException("Parameter not found: " + value));
    }

    static String nameToKey(List<Key> values, String value) {
        return Key.nameToKey(values, value)
                .or(() -> RoundSettings.nameToKey(RoundSettings.allRoundsKeys(), value))
                .or(() -> SemifinalSettings.nameToKey(SemifinalSettings.allSemifinalKeys(), value))
                .or(() -> InactivitySettings.nameToKey(InactivitySettings.allInactivityKeys(), value))
                .orElseThrow(() -> new IllegalArgumentException("Parameter not found: " + value));
    }

    // methods from Settings

    List<Parameter> getParameters();

    boolean hasParameter(String name);

    Parameter<?> getParameter(String name);

    EditBox<?> addEditBox(String name);

    SelectBox<?> addSelect(String name, List<Object> strings);

    CheckBox<Boolean> addCheckBox(String name);

    // getters

    default <T extends Parameter> T parameter(Key key, Class<T> clazz) {
        return (T)getParameter(key.key());
    }

    default String string(Key key) {
        return stringValue(key).getValue();
    }

    default Parameter<String> stringValue(Key key) {
        return getParameter(key.key()).type(String.class);
    }

    default Integer integer(Key key) {
        return integerValue(key).getValue();
    }

    default Parameter<Integer> integerValue(Key key) {
        return getParameter(key.key()).type(Integer.class);
    }

    default Double real(Key key) {
        return realValue(key).getValue();
    }

    default Parameter<Double> realValue(Key key) {
        return getParameter(key.key()).type(Double.class);
    }

    default Boolean bool(Key key) {
        return boolValue(key).getValue();
    }

    default Parameter<Boolean> boolValue(Key key) {
        return getParameter(key.key()).type(Boolean.class);
    }

    // setters

    default CheckBox<Boolean> add(SettingsReader.Key key, boolean value) {
        return addCheckBox(key.key()).type(Boolean.class).def(value);
    }

    default EditBox<String> add(SettingsReader.Key key, String value) {
        return addEditBox(key.key()).type(String.class).def(value);
    }

    default EditBox<Integer> add(SettingsReader.Key key, int value) {
        return addEditBox(key.key()).type(Integer.class).def(value);
    }

    default EditBox<Double> add(SettingsReader.Key key, double value) {
        return addEditBox(key.key()).type(Double.class).def(value);
    }

    default SelectBox<String> options(Key key, List options, String value) {
        return addSelect(key.key(), options).type(String.class).def(value);
    }

    default EditBox<String> multiline(Key key, String value) {
        return addEditBox(key.key()).multiline().type(String.class).def(value);
    }

    default T string(Key key, String data) {
        if (!hasParameter(key.key())) {
            add(key, data);
        }
        getParameter(key.key()).update(data);
        return (T)this;
    }

    default T integer(Key key, int data) {
        if (!hasParameter(key.key())) {
            add(key, data);
        }
        getParameter(key.key()).update(data);
        return (T)this;
    }

    default T real(Key key, double data) {
        if (!hasParameter(key.key())) {
            add(key, data);
        }
        getParameter(key.key()).update(data);
        return (T)this;
    }

    default T bool(Key key, boolean data) {
        if (!hasParameter(key.key())) {
            add(key, data);
        }
        getParameter(key.key()).update(data);
        return (T)this;
    }

    // json

    List<Key> allKeys();

    default T update(JSONObject json) {
        json.keySet().forEach(name -> {
            String key = nameToKey(allKeys(), name);
            getParameter(key).update(json.get(name));
        });
        return (T)this;
    }

    default JSONObject asJson() {
        return new JSONObject(){{
            getParameters().forEach(param ->
                    put(keyToName(allKeys(), param.getName()), param.getValue()));
        }};
    }
}
