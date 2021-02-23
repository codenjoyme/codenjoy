package com.codenjoy.dojo.services.settings;

public interface SettingsReader<T extends SettingsReader> {

    interface Key {
        String key();
    }
    
    Parameter<?> getParameter(String name);
    
    // getters

    default String string(Key key) {
        return getParameter(key.key()).type(String.class).getValue();
    }

    default Parameter<String> stringValue(Key key) {
        return getParameter(key.key()).type(String.class);
    }

    default Integer integer(Key key) {
        return getParameter(key.key()).type(Integer.class).getValue();
    }

    default Parameter<Integer> integerValue(Key key) {
        return getParameter(key.key()).type(Integer.class);
    }

    default Boolean bool(Key key) {
        return getParameter(key.key()).type(Boolean.class).getValue();
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

    default T bool(Key key, Boolean data) {
        getParameter(key.key()).update(data);
        return (T)this;
    }
}
