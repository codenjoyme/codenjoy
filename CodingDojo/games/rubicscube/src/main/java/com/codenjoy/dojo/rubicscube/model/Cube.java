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


import java.util.HashMap;
import java.util.Map;

public class Cube {

    private Map<Face, FaceValue> cube = new HashMap<Face, FaceValue>();

    public Cube() {
        init();
    }

    public void init() {
        cube.put(Face.BACK, new FaceValue(Elements.RED));
        cube.put(Face.DOWN, new FaceValue(Elements.YELLOW));
        cube.put(Face.UP, new FaceValue(Elements.WHITE));
        cube.put(Face.RIGHT, new FaceValue(Elements.GREEN));
        cube.put(Face.LEFT, new FaceValue(Elements.BLUE));
        cube.put(Face.FRONT, new FaceValue(Elements.ORANGE));
    }

    public String getFace(Face name) {
        return cube.get(name).toString();
    }

    public FaceValue face(Face name) {
        return cube.get(name);
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

    public boolean isSolved() { // TODO test me
        boolean result = true;

        result &= back().isSolved();
        result &= down().isSolved();
        result &= left().isSolved();
        result &= right().isSolved();
        result &= front().isSolved();
        result &= back().isSolved();

        return result;
    }
}
