package com.codenjoy.dojo.snake.client

import com.codenjoy.dojo.client.{Solver, WebSocketRunner}
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
  def main(args: Array[String]): Unit = {
    WebSocketRunner.runClient(// paste here board page url from browser after registration
      "http://codenjoy.com:80/codenjoy-contest/board/player/3edq63tw0bq4w4iem7nb?code=1234567890123456789",
      new YourSolverScala(new RandomDice),
      new BoardScala)
  }
}

class YourSolverScala(var dice: Dice) extends Solver[BoardScala] {
  /**
    * Каждую секунду сервер будет вызывать этот метод, передавая на вход актуальное состояние доски.
    *
    * @param board объект, описывающий состояние доски
    * @return направление, куда следует двигаться змейке
    */
  override def get(board: BoardScala): String = {
    // TODO your code here

    Direction.UP.toString
  }
}