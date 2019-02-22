package com.codenjoy.dojo.battlecity

import com.codenjoy.dojo.client.{Solver, WebSocketRunner}
import com.codenjoy.dojo.services.Direction._
import com.codenjoy.dojo.services.{Dice, RandomDice}

/**
  * User: your name
  * Это твой алгоритм AI для игры. Реализуй его на свое усмотрение.
  */
object YourSolverScala {
  def main(args: Array[String]): Unit = {
    WebSocketRunner.runClient(// paste here board page url from browser after registration
      "http://127.0.0.1:8080/codenjoy-contest/board/player/z2d3lyzvc0lyf68ynpz9?code=8026464261134740400",
      new YourSolverScala(new RandomDice),
      new BoardScala)
  }
}

class YourSolverScala(var dice: Dice) extends Solver[BoardScala] {
  /**
    * Каждую секунду сервер будет вызывать этот метод, передавая на вход актуальное состояние доски.
    *
    * @param board объект, описывающий состояние доски
    * @return следующее действие твоего танка
    */
  override def get(board: BoardScala): String = {
    UP.toString
  }
}