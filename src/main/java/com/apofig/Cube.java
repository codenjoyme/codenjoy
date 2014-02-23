package com.apofig;

import java.util.HashMap;
import java.util.Map;

/**
 * User: sanja
 * Date: 08.10.13
 * Time: 4:41
 */
public class Cube {

    private Map<Face, FaceValue> cube = new HashMap<Face, FaceValue>();

    public Cube() {
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
        result.append(left().getLine(1)).append(' ').append(front().getLine(1)).append(' ').append(right().getLine(1)).append(' ').append(back().getLine(1)).append("\n");
        result.append(left().getLine(2)).append(' ').append(front().getLine(2)).append(' ').append(right().getLine(2)).append(' ').append(back().getLine(2)).append("\n");
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


    public Edges getEdges() {
        Edges result = new Edges();

        result.add(Face.FRONT, Face.UP, front().get(Neighbor.UP), up().get(Neighbor.DOWN));
        result.add(Face.FRONT, Face.DOWN, front().get(Neighbor.DOWN), down().get(Neighbor.UP));
        result.add(Face.FRONT, Face.LEFT, front().get(Neighbor.LEFT), left().get(Neighbor.RIGHT));
        result.add(Face.FRONT, Face.RIGHT, front().get(Neighbor.RIGHT), right().get(Neighbor.LEFT));

        result.add(Face.UP, Face.BACK, up().get(Neighbor.UP), back().get(Neighbor.UP));
        result.add(Face.UP, Face.FRONT, up().get(Neighbor.DOWN), front().get(Neighbor.UP));
        result.add(Face.UP, Face.LEFT, up().get(Neighbor.LEFT), left().get(Neighbor.UP));
        result.add(Face.UP, Face.RIGHT, up().get(Neighbor.RIGHT), right().get(Neighbor.UP));

        result.add(Face.DOWN, Face.BACK, down().get(Neighbor.DOWN), back().get(Neighbor.DOWN));
        result.add(Face.DOWN, Face.FRONT, down().get(Neighbor.UP), front().get(Neighbor.DOWN));
        result.add(Face.DOWN, Face.LEFT, down().get(Neighbor.LEFT), left().get(Neighbor.DOWN));
        result.add(Face.DOWN, Face.RIGHT, down().get(Neighbor.RIGHT), right().get(Neighbor.DOWN));

        result.add(Face.LEFT, Face.UP, left().get(Neighbor.UP), up().get(Neighbor.LEFT));
        result.add(Face.LEFT, Face.DOWN, left().get(Neighbor.DOWN), down().get(Neighbor.LEFT));
        result.add(Face.LEFT, Face.FRONT, left().get(Neighbor.RIGHT), front().get(Neighbor.LEFT));
        result.add(Face.LEFT, Face.BACK, left().get(Neighbor.LEFT), back().get(Neighbor.RIGHT));

        result.add(Face.RIGHT, Face.UP, right().get(Neighbor.UP), up().get(Neighbor.RIGHT));
        result.add(Face.RIGHT, Face.DOWN, right().get(Neighbor.DOWN), down().get(Neighbor.RIGHT));
        result.add(Face.RIGHT, Face.FRONT, right().get(Neighbor.LEFT), front().get(Neighbor.RIGHT));
        result.add(Face.RIGHT, Face.BACK, right().get(Neighbor.RIGHT), back().get(Neighbor.LEFT));

        result.add(Face.BACK, Face.UP, back().get(Neighbor.UP), up().get(Neighbor.UP));
        result.add(Face.BACK, Face.DOWN, back().get(Neighbor.DOWN), down().get(Neighbor.DOWN));
        result.add(Face.BACK, Face.LEFT, back().get(Neighbor.RIGHT), left().get(Neighbor.LEFT));
        result.add(Face.BACK, Face.RIGHT, back().get(Neighbor.LEFT), right().get(Neighbor.RIGHT));

        return result;
    }
}
