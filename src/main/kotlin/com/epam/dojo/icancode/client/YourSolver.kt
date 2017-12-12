package com.epam.dojo.icancode.client

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


import com.codenjoy.dojo.services.Direction.*
import com.codenjoy.dojo.client.WebSocketRunner
import com.codenjoy.dojo.services.Dice
import com.codenjoy.dojo.services.RandomDice

/**
 * Your AI
 */
class YourKotlinSolver() : AbstractSolver() {
    override fun whatToDo(board: Board): Command {
        with(board) {
            if (!board.isMeAlive) return Command.doNothing()

            var goals = board.gold
            if (goals.isEmpty()) {
                goals = board.exits
            }

            // TODO your code here
            return Command.jump()
        }
    }
}

// this is your email
val USER_NAME = "your@email.com"
// you can get this code after registration on the server with your email
// http://server-ip:8080/codenjoy-contest/board/player/your@email.com?code=12345678901234567890
val CODE = "12345678901234567890"

/**
 * Run this method for connect to the server and start the game
 */
fun main(args: Array<String>) {
    AbstractSolver.start(USER_NAME,
            CODE,
            "dojo.lab.epam.com:80",
            YourKotlinSolver())
}
