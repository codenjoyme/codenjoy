package com.codenjoy.dojo.bomberman.client

import com.codenjoy.dojo.client.{Solver, WebSocketRunner}
import com.codenjoy.dojo.services.{Dice, Direction, RandomDice}

/**
  * User: your name
  * Это твой алгоритм AI для игры. Реализуй его на свое усмотрение.
  */
object YourSolverScala {
  def main(args: Array[String]): Unit = {
    WebSocketRunner.runClient(// paste here board page url from browser after registration
      "http://codenjoy.com:80/codenjoy-contest/board/player/your@email.com?code=12345678901234567890",
      new YourSolverScala(new RandomDice),
      new BoardScala)
  }
}

class YourSolverScala(var dice: Dice) extends Solver[BoardScala] {
  /**
    * Каждую секунду сервер будет вызывать этот метод, передавая на вход актуальное состояние доски.
    *
    * @param board объект, описывающий состояние доски
    * @return следующее действие твоего персонажа
    */
  override def get(board: BoardScala): String = {
    if (board.isGameOver) return ""
    Direction.ACT.toString
  }
}