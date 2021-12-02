package com.codenjoy.dojo.expansion.client

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


import com.codenjoy.dojo.games.expansion.*
import com.codenjoy.dojo.services.Dice
import com.codenjoy.dojo.services.PointImpl.pt
import com.codenjoy.dojo.services.QDirection
import com.codenjoy.dojo.services.RandomDice

/**
 * Author: your name
 *
 * This is your AI algorithm for the game.
 * Implement it at your own discretion.
 * Pay attention to {@see YourSolverTest} - there is
 * a test framework for you.
 */
class YourKotlinSolver(dice: Dice) : AbstractSolver() {

    override fun whatToDo(board: Board): Command {
        with(board) {
            if (!isGameOver) return Command.doNothing()

            // TODO your code here

            return Command
                    .increase(Forces(pt(12, 13), 10))
                    .move(ForcesMoves(pt(14, 15), 5, QDirection.DOWN))
                    .build();
        }
    }

    override fun buildAnswer(board: Board): String {
        return String.format("message('%s')", super.buildAnswer(board))
    }
}

/**
 * Run this method for connect to the server and start the game
 */
fun main() {
    AbstractSolver.connectClient(
            // paste here board page url from browser after registration
            "http://codenjoy.com:80/codenjoy-contest/board/player/your@email.com?code=18899199021366816317",
            // and solver here
            YourKotlinSolver(RandomDice()))
}
