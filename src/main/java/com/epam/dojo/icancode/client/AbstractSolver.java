package com.epam.dojo.icancode.client;

/*-
 * #%L
 * iCanCode - it's a dojo-like platform from developers to developers.
 * %%
 * Copyright (C) 2016 EPAM
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


import com.codenjoy.dojo.services.Direction;
import com.codenjoy.dojo.client.Solver;
import com.codenjoy.dojo.client.WebSocketRunner;

/**
 * Created by indigo on 2016-10-12.
 */
public abstract class AbstractSolver implements Solver<Board>  {

    @Override
    public String get(Board board) {
        if (board.getMe() == null) {
            System.out.println("Please call Dojo Sensei for debug!!!"); // TODO debug this issue
            return Command.die().toString();
        }
        return whatToDo(board).toString();
    }

    public abstract Command whatToDo(Board board);

    /**
     * @param host Server url
     * @param userName Your email entered at http://dojo.lab.epam.com/codenjoy-contest/resources/icancode/registration.html
     */
    public static void start(String userName, String host, Solver solver) {
        try {
            WebSocketRunner.run("ws://" + host + "/codenjoy-contest/ws", userName,
                    solver,
                    new Board());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
