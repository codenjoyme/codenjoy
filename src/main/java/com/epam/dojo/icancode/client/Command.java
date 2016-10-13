package com.epam.dojo.icancode.client;

import com.codenjoy.dojo.client.Direction;

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
    public static Command jumpTo(Direction direction) {
        return new Command("ACT(1)" + "," + direction.toString());
    }

    /**
     * Says to Hero pull box on this direction
     */
    public static Command pullTo(Direction direction) {
        return new Command("ACT(2)" + "," + direction.toString());
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

}
