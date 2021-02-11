package com.codenjoy.dojo.icancode.client;

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


import com.codenjoy.dojo.services.Direction;

/**
 * Created by indigo on 2016-10-13.
 */
public class Command {

    private String command;

    public Command(String command) {
        this.command = command;
    }

    @Override
    public String toString() {
        return command;
    }

    /**
     * Says to Hero do nothing
     */
    public static Command doNothing() {
        return new Command("");
    }

    /**
     * Reset current level
     */
    public static Command die() {
        return new Command("ACT(0)");
    }

    /**
     * Says to Hero jump to direction
     */
    public static Command jump(Direction direction) {
        return new Command("ACT(1)" + "," + direction.toString());
    }

    /**
     * Says to Hero pull box on this direction
     */
    public static Command pull(Direction direction) {
        return new Command("ACT(2)" + "," + direction.toString());
    }

    /**
     * Says to Hero fire on this direction
     */
    public static Command fire(Direction direction) {
        return new Command("ACT(3)" + "," + direction.toString());
    }

    /**
     * Says to Hero jump in place
     */
    public static Command jump() {
        return new Command("ACT(1)");
    }

    /**
     * Says to Hero go to direction
     */
    public static Command go(Direction direction) {
        return new Command(direction.toString());
    }

    /**
     * Says to Hero goes to start point
     */
    public static Command reset() {
        return new Command("ACT(0)");
    }

}
