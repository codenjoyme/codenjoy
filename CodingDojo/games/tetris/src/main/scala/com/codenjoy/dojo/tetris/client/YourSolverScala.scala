package com.codenjoy.dojo.tetris.client

/*-
 * #%L
 * Codenjoy - it's a dojo-like platform from developers to developers.
 * %%
 * Copyright (C) 2016 Codenjoy
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

import com.codenjoy.dojo.client.{AbstractJsonSolver, WebSocketRunner}
import com.codenjoy.dojo.services.{Dice, Direction, RandomDice}

/**
 * Author: your name
 *
 * This is your AI algorithm for the game.
 * Implement it at your own discretion.
 * Pay attention to {@see YourSolverTest} - there is
 * a test framework for you.
 */
object YourSolverScala {

  def main(args: Array[String]) {
    WebSocketRunner.runClient(
      // paste here board page url from browser after registration
      "http://codenjoy.com:80/codenjoy-contest/board/player/3edq63tw0bq4w4iem7nb?code=1234567890123456789",
      new YourSolverScala(new RandomDice), new BoardScala)
  }
}

class YourSolverScala(var dice: Dice) extends AbstractJsonSolver[BoardScala] {

  override def getAnswer(board: BoardScala): String = {

    // TODO your code here

    Direction.UP.toString
  }
}
