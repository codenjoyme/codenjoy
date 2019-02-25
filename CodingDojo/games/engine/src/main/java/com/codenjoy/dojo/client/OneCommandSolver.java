package com.codenjoy.dojo.client;

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


import org.apache.commons.lang3.StringUtils;

import java.util.Scanner;

/**
 * Created by indigo on 2017-03-30.
 */
public class OneCommandSolver<T extends ClientBoard> implements Solver<T> {

    private String command;

    public OneCommandSolver(String command) {
        this.command = command;
    }
    
    private boolean processed = false;
    
    @Override
    public String get(T board) {
        if (processed) {
            System.exit(0);
            return StringUtils.EMPTY;
        } else {
            processed = true;
        }
        System.out.printf("Are you sure you want to run the command '%s' (y/n)?\n", command);
        String answer = new Scanner(System.in).next();
        if (answer.equals("y")) {
            System.out.printf("Sending '%s' to the server\n", command);
            return command;
        } else {
            System.exit(0);
            return StringUtils.EMPTY;
        }
    }
}
