package com.apofig;

import com.apofig.command.F;

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
            char ch = command.charAt(index++);
            return CommandFactory.valueOf(ch);
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException("Низя");
        }
    }

    private static class CommandFactory {
        public static Command valueOf(char ch) {
            if (ch == 'F') {
                return new F();
            }
            return null; // TODO закончить
        }
    }
}
