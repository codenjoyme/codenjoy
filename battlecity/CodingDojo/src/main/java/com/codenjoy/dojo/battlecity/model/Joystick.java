package com.javatrainee.tanks;

public class Joystick {
    private Command command;
    public Joystick() {
        command = Command.DO_NOTHING;
    }

    public void setCommandByPressedKey(String key) {
        if (key.equals("w")) {
            command = Command.MOVE_UP;
        }
        if (key.equals("s")) {
            command = Command.MOVE_DOWN;
        }
        if (key.equals("d")) {
            command = Command.MOVE_RIGHT;
        }
        if (key.equals("a")) {
            command = Command.MOVE_LEFT;
        }
    }

    public Command getCommand() {
        return command;
    }

}
