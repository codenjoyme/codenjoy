package com.codenjoy.dojo.a2048.model;

import com.codenjoy.dojo.services.settings.Settings;

import java.util.List;

public interface Level {

    int size();

    List<Number> getNumbers();

    int getNewAdd();

    Settings getSettings();

    Mode getMode();
}
