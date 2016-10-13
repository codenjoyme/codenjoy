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


import com.codenjoy.dojo.client.Direction.*
import com.codenjoy.dojo.client.WebSocketRunner
import com.codenjoy.dojo.services.Dice
import com.codenjoy.dojo.services.RandomDice

/**
 * Your AI
 */
class YourKotlinSolver() : AbstractSolver() {
    override fun whatToDo(board: Board): String {
        with(board) {
            return when {
                !isMeAlive -> doNothing()
                !isBarrierAt(me.x + 1, me.y) -> go(RIGHT)
                !isBarrierAt(me.x, me.y + 1) -> go(DOWN)
                !isBarrierAt(me.x - 1, me.y) -> go(LEFT)
                else -> doNothing()
            }
        }
    }
}

/**
 * Run this method for connect to the server and start the game
 */
fun main(args: Array<String>) {
    AbstractSolver.start("user@gmail.com", "dojo.lab.epam.com:80", YourKotlinSolver())
}
