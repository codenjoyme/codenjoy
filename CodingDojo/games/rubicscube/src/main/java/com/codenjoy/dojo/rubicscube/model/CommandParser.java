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


import com.codenjoy.dojo.rubicscube.model.command.*;

import java.util.Iterator;

public class CommandParser implements Iterable<Command> {

    private String command;

    public CommandParser(String command) {
        this.command = command.replaceAll("[() ]", "");
    }

    @Override
    public Iterator<Command> iterator() {
        return new CommandIterator();
    }

    private class CommandIterator implements Iterator<Command> {

        private int index = 0;

        @Override
        public boolean hasNext() {
            return index < command.length();
        }

        @Override
        public Command next() {
            String c = "" + command.charAt(index++);
            if (hasNext() && (command.charAt(index) == '2' || command.charAt(index) == '\'')) {
                c += command.charAt(index++);
            }
            return CommandFactory.valueOf(c);
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException("Низя");
        }
    }

    private static class CommandFactory {
        public static Command valueOf(String command) {
            if (command.equals("F")) {
                return new F();
            } else if (command.equals("F2")) {
                return new F2();
            } else if (command.equals("F'")) {
                return new F_();
            } else if (command.equals("R")) {
                return new R();
            } else if (command.equals("R2")) {
                return new R2();
            } else if (command.equals("R'")) {
                return new R_();
            } else if (command.equals("U")) {
                return new U();
            } else if (command.equals("U2")) {
                return new U2();
            } else if (command.equals("U'")) {
                return new U_();
            } else if (command.equals("L")) {
                return new L();
            } else if (command.equals("L2")) {
                return new L2();
            } else if (command.equals("L'")) {
                return new L_();
            } else if (command.equals("D")) {
                return new D();
            } else if (command.equals("D2")) {
                return new D2();
            } else if (command.equals("D'")) {
                return new D_();
            } else if (command.equals("B")) {
                return new B();
            } else if (command.equals("B2")) {
                return new B2();
            } else if (command.equals("B'")) {
                return new B_();
            }
            return null; // TODO закончить
        }
    }
}
