package com.codenjoy.dojo.icancode.client

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


import com.codenjoy.dojo.games.icancode.AbstractSolver
import com.codenjoy.dojo.games.icancode.Board
import com.codenjoy.dojo.games.icancode.Command
import com.codenjoy.dojo.services.Dice
import com.codenjoy.dojo.services.Direction
import com.codenjoy.dojo.services.RandomDice

/**
 * Your AI
 */
class YourKotlinSolver(dice: Dice?) : AbstractSolver(dice) {

    override fun whatToDo(board: Board): Command {
        with(board) {
            if (!isMeAlive) return Command.doNothing()

            // TODO your code here
            // var goals = gold
            // if (goals.isEmpty()) {
            //    goals = exits
            // }

            return Command.go(Direction.RIGHT)
        }
    }
}
/**
 * Run this method for connect to the server and start the game
 */
fun main() {
    AbstractSolver.connectClient(
            // paste here board page url from browser after registration
            "http://codenjoy.com:80/codenjoy-contest/board/player/3edq63tw0bq4w4iem7nb?code=1234567890123456789",
            // and solver here
            YourKotlinSolver(RandomDice()))
}
