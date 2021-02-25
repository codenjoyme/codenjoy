package com.codenjoy.dojo.services.settings;

import com.codenjoy.dojo.services.round.RoundSettings;

import java.util.Arrays;
import java.util.List;

public interface SettingsReader<T extends SettingsReader> {

    interface Key {

        String key();

        static String keyToName(Key[] values, String value) {
            return Arrays.stream(values)
                    .filter(element -> element.key().equals(value))
                    .map(Key::toString)
                    .findFirst()
                    .orElseGet(() -> keyToName(RoundSettings.Keys.values(), value));
        }

        static String nameToKey(Key[] values, String value) {
            return Arrays.stream(values)
                    .filter(element -> element.toString().equals(value))
                    .map(Key::key)
                    .findFirst()
                    .orElseGet(() -> nameToKey(RoundSettings.Keys.values(), value));
        }
    }


    // methods from Settings

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

    default T string(Key key, String data) {
        if (!hasParameter(key.key())) {
            add(key, data);
        }
        getParameter(key.key()).update(data);
        return (T)this;
    }

    default T integer(Key key, Integer data) {
        if (!hasParameter(key.key())) {
            add(key, data);
        }
        getParameter(key.key()).update(data);
        return (T)this;
    }

    default T real(Key key, Double data) {
        if (!hasParameter(key.key())) {
            add(key, data);
        }
        getParameter(key.key()).update(data);
        return (T)this;
    }

    default T bool(Key key, Boolean data) {
        if (!hasParameter(key.key())) {
            add(key, data);
        }
        getParameter(key.key()).update(data);
        return (T)this;
    }
}
