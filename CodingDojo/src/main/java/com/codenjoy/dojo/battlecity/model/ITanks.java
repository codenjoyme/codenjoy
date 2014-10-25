package com.codenjoy.dojo.battlecity.model;

import com.codenjoy.dojo.services.Joystick;

import java.util.List;

/**
 * User: sanja
 * Date: 09.11.13
 * Time: 16:44
 */
public interface ITanks {
    Joystick getJoystick();

    List<Tank> getTanks();

    void remove(Player player);

    void newGame(Player player);

    int size();
}
