package com.codenjoy.dojo.bomberman.services;

import com.codenjoy.dojo.services.settings.Parameter;
import org.json.JSONObject;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;

import static java.util.stream.Collectors.toList;

public class SettingsUtils {
    public static void save(JSONObject json, Object object) {
        fields(object, Parameter.class)
                .forEach(name -> save(object, name, json));
    }

    public static void load(JSONObject json, Object object) {
        fields(object, Parameter.class)
                .forEach(name -> load(object, name, json));
    }

    private static List<String> fields(Object object, Class type) {
        return Arrays.stream(object.getClass().getDeclaredFields())
                .filter(field -> field.getType().equals(type))
                .map(field -> field.getName())
                .collect(toList());
    }

    private static void save(Object object, String name, JSONObject json) {
        if (!json.has(name)) {
            return;
        }

        get(object, name).update(json.get(name));
    }

    private static void load(Object object, String name, JSONObject json) {
        json.put(name, get(object, name).getValue());
    }

    private static Parameter<Object> get(Object object, String name) {
        try {
            Field field = object.getClass().getDeclaredField(name);

            boolean accessible = field.isAccessible();
            field.setAccessible(true);

            Parameter<Object> result = (Parameter<Object>) field.get(object);

            field.setAccessible(accessible);
            return result;
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }
}
