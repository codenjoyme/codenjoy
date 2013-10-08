package com.apofig;

import com.apofig.command.*;

import java.util.Iterator;

/**
 * User: sanja
 * Date: 08.10.13
 * Time: 5:19
 */
public class CommandParser implements Iterable<Command> {

    private String command;

    public CommandParser(String command) {
        this.command = command;
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
            }
            return null; // TODO закончить
        }
    }
}
