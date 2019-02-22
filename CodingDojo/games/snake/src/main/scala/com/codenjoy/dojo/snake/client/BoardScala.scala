package com.codenjoy.dojo.snake.client

import com.codenjoy.dojo.client.AbstractBoard
import com.codenjoy.dojo.services.Direction._
import com.codenjoy.dojo.services.{Direction, Point}
import com.codenjoy.dojo.snake.model.Elements
import com.codenjoy.dojo.snake.model.Elements._

import scala.collection.JavaConverters._

/**
  * Класс, обрабатывающий строковое представление доски.
  * Содержит ряд унаследованных методов {@see AbstractBoard},
  * но ты можешь добавить сюда любые свои методы на их основе.
  */
class BoardScala extends AbstractBoard[Elements] {

  // твои новые методы


  def getApples: List[Point] = getPoints(GOOD_APPLE)

  def getStones: List[Point] = getPoints(BAD_APPLE)

  def getWalls: List[Point] = getPoints(BREAK)

  def getHead: Option[Point] = getPoints(HEAD_UP, HEAD_DOWN, HEAD_LEFT, HEAD_RIGHT).headOption

  def getSnakeDirection: Direction = {
    val head = getHead.orNull
    if (head == null) return null
    if (isAt(head.getX, head.getY, HEAD_LEFT)) LEFT
    else if (isAt(head.getX, head.getY, HEAD_RIGHT)) RIGHT
    else if (isAt(head.getX, head.getY, HEAD_UP)) UP
    else DOWN
  }

  def getSnake: List[Point] = {
    val head = getHead.orNull
    if (head == null) return List.empty
    getPoints(
      TAIL_END_DOWN,
      TAIL_END_LEFT,
      TAIL_END_UP,
      TAIL_END_RIGHT,
      TAIL_HORIZONTAL,
      TAIL_VERTICAL,
      TAIL_LEFT_DOWN,
      TAIL_LEFT_UP,
      TAIL_RIGHT_DOWN,
      TAIL_RIGHT_UP
    ) :+ head
  }

  def getBarriers: List[Point] = getSnake ++ getStones ++ getWalls

  def isGameOver: Boolean = getHead == null

  def getPoints(elements: Elements*): List[Point] = super.get(elements: _*).asScala.toList

  override def valueOf(ch: Char): Elements = Elements.valueOf(ch)

  override protected def inversionY(y: Int): Int = size - 1 - y

  override def toString: String = {
    String.format("Board:%n%s%n" +
      "Apple at: %s%n" +
      "Stones at: %s%n" +
      "Head at: %s%n" +
      "Snake at: %s%n" +
      "Current direction: %s",
      boardAsString, getApples, getStones,
      getHead, getSnake, getSnakeDirection)
  }

}
