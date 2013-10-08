package com.apofig;

import java.util.HashMap;
import java.util.Map;

/**
 * User: sanja
 * Date: 08.10.13
 * Time: 4:41
 */
public class RubicsCube {

    private Map<Face, FaceValue> cube = new HashMap<Face, FaceValue>();

    public RubicsCube() {
        cube.put(Face.BACK, new FaceValue(Color.RED));
        cube.put(Face.DOWN, new FaceValue(Color.YELLOW));
        cube.put(Face.UP, new FaceValue(Color.WHITE));
        cube.put(Face.RIGHT, new FaceValue(Color.GREEN));
        cube.put(Face.LEFT, new FaceValue(Color.BLUE));
        cube.put(Face.FRONT, new FaceValue(Color.ORANGE));
    }

    public String getFace(Face name) {
        return cube.get(name).toString();
    }


    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();
        result.append("    ").append(up().getLine(0)).append("        \n");
        result.append("    ").append(up().getLine(1)).append("        \n");
        result.append("    ").append(up().getLine(2)).append("        \n");
        result.append(left().getLine(0)).append(' ').append(front().getLine(0)).append(' ').append(right().getLine(0)).append(' ').append(back().getLine(0)).append("\n");
        result.append(left().getLine(1)).append(' ').append(front().getLine(1)).append(' ').append(right().getLine(1)).append(' ').append(back().getLine(0)).append("\n");
        result.append(left().getLine(2)).append(' ').append(front().getLine(2)).append(' ').append(right().getLine(2)).append(' ').append(back().getLine(0)).append("\n");
        result.append("    ").append(down().getLine(0)).append("        \n");
        result.append("    ").append(down().getLine(1)).append("        \n");
        result.append("    ").append(down().getLine(2)).append("        \n");
        return result.toString();
    }

    private FaceValue back() {
        return cube.get(Face.BACK);
    }

    private FaceValue down() {
        return cube.get(Face.DOWN);
    }

    private FaceValue up() {
        return cube.get(Face.UP);
    }

    private FaceValue left() {
        return cube.get(Face.LEFT);
    }

    private FaceValue front() {
        return cube.get(Face.FRONT);
    }

    private FaceValue right() {
        return cube.get(Face.RIGHT);
    }

    public void doCommand(String stringCommand) {
        Iterable<Command> commands = new CommandParser(stringCommand);
        for (Command command : commands) {
            command.apply(cube);
        }
    }
}
