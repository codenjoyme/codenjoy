package com.codenjoy.dojo.snake.client

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


import com.codenjoy.dojo.client.Solver
import com.codenjoy.dojo.client.WebSocketRunner
import com.codenjoy.dojo.services.Dice
import com.codenjoy.dojo.services.Direction
import com.codenjoy.dojo.services.RandomDice

/**
 * User: your name
 */
class YourKotlinSolver(private val dice: Dice) : Solver<Board> {

    var board: Board? = null

    override fun get(board: Board): String {
        this.board = board

        return Direction.UP.toString()
    }

    companion object {
        @JvmStatic fun main(args: Array<String>) {
            WebSocketRunner.runClient(
                    // paste here board page url from browser after registration
                    "https://dojo.lab.epam.com/codenjoy-contest/board/player/your@email.com?code=12345678901234567890",
                    YourSolver(RandomDice()),
                    Board())
        }
    }

}
