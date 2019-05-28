package com.codenjoy.dojo.snake.client

import com.codenjoy.dojo.client.{Solver, WebSocketRunner}
import com.codenjoy.dojo.services.{Dice, Direction, RandomDice}

/**
  * User: your name
  * Это твой алгоритм AI для игры. Реализуй его на свое усмотрение.
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
    return Direction.UP.toString
  }
}