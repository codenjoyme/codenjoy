package com.codenjoy.dojo.expansion.client;

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


import com.codenjoy.dojo.expansion.model.Forces;
import com.codenjoy.dojo.expansion.model.ForcesMoves;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by indigo on 2016-10-13.
 */
public class Command {

    public static final String MOVEMENTS_KEY = "movements";
    public static final String INCREASE_KEY = "increase";

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
     * Select forces to move
     */
    public static CommandBuilder increase(Forces... forces) {
        return new CommandBuilder().increase(forces);
    }

    /**
     * Select forces to increase
     */
    public static CommandBuilder move(ForcesMoves... forces) {
        return new CommandBuilder().move(forces);
    }

    public static class CommandBuilder {
        private List<ForcesMoves> movements;
        private List<Forces> increase;

        CommandBuilder() {
            movements = new LinkedList<>();
            increase = new LinkedList<>();
        }

        public CommandBuilder increase(Forces... forces) {
            increase.addAll(Arrays.asList(forces));
            return this;
        }

        public CommandBuilder move(ForcesMoves... forces) {
            movements.addAll(Arrays.asList(forces));
            return this;
        }

        public Command build() {
            return new Command(new JSONObject() {{
                put(INCREASE_KEY, new JSONArray(increase));
                put(MOVEMENTS_KEY, new JSONArray(movements));
            }}.toString());
        }
    }

}
