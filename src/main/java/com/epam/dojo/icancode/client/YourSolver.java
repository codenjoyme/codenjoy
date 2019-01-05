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


import com.codenjoy.dojo.services.Dice;
import com.codenjoy.dojo.services.Point;
import com.codenjoy.dojo.services.RandomDice;

import java.util.List;

import static com.epam.dojo.icancode.client.Command.doNothing;
import static com.epam.dojo.icancode.client.Command.jump;

/**
 * Your AI
 */
public class YourSolver extends AbstractSolver {

    /**
     * @param dice DIP (SOLID) for Random dependency in your Solver realization
     */
    public YourSolver(Dice dice) {
        super(dice);
    }

    /**
     * @param board use it for find elements on board
     * @return what hero should do in this tick (for this board)
     */
    @Override
    public Command whatToDo(Board board) {
        if (!board.isMeAlive()) return doNothing();

        List<Point> goals = board.getGold();
        if (goals.isEmpty()) {
            goals = board.getExits();
        }

        // TODO your code here
        return jump();
    }

    /**
     * Run this method for connect to Server
     */
    public static void main(String[] args) {
        connectClient(
                // paste here board page url from browser after registration
                "http://192.168.1.102:80/codenjoy-contest/board/player/your@email.com?code=18899199021366816317",
                // and solver here
                new YourSolver(new RandomDice()));
    }
}