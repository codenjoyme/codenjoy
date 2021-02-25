package com.codenjoy.dojo.services.settings;

public interface SettingsReader<T extends SettingsReader> {

    interface Key {
        String key();
    }
    
    Parameter<?> getParameter(String name);
    
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

    default T string(Key key, String data) {
        getParameter(key.key()).update(data);
        return (T)this;
    }

    default T integer(Key key, Integer data) {
        getParameter(key.key()).update(data);
        return (T)this;
    }

    default T real(Key key, Double data) {
        getParameter(key.key()).update(data);
        return (T)this;
    }

    default T bool(Key key, Boolean data) {
        getParameter(key.key()).update(data);
        return (T)this;
    }
}
