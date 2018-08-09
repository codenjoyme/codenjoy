package com.codenjoy.dojo.rubicscube.model;

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


import com.codenjoy.dojo.rubicscube.services.Events;
import com.codenjoy.dojo.services.joystick.ActJoystick;
import com.codenjoy.dojo.services.multiplayer.PlayerHero;

public class Hero extends PlayerHero<Field> implements ActJoystick {

    private Player player;
    private String command;

    public boolean isAlive() {
        return true;
    }

    @Override
    public void tick() {
        // do nothing
    }

    @Override
    public void act(int... p) { // TODO test me
        if (field.isGameOver()) return;

        if (p.length == 1 && p[0] == 0) {
            field.gameOver();
            player.event(Events.FAIL);
            return;
        }

        if (p.length != 2) {
            return;
        }

        int faceNumber = p[0];
        if (field.check(faceNumber, 1, 6)) return;

        int rotateCount = p[1];
        if (field.check(rotateCount, -1, 2)) return;

        command = "";
        switch (faceNumber) {
            case 1 : command = "L"; break;
            case 2 : command = "F"; break;
            case 3 : command = "R"; break;
            case 4 : command = "B"; break;
            case 5 : command = "U"; break;
            case 6 : command = "D"; break;
        }

        switch (rotateCount) {
            case -1 : command += "'"; break;
            case 0 : command = "";
            case 1 : break;
            case 2 : command += "2"; break;
        }
    }

    public void init(Player player) {
        this.player = player;
    }

    public String pullCommand() {
        String command = this.command;
        this.command = null;
        return command;
    }
}
