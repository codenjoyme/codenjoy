package com.codenjoy.dojo.battlecity.model;

import com.codenjoy.dojo.services.Joystick;

import java.util.List;

public interface ITanks {
    Joystick getJoystick();

    List<Tank> getTanks();

    void remove(Player player);

    void newGame(Player player);

    int size();
}
