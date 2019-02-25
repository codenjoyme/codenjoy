package com.codenjoy.dojo.snakebattle.client

import com.codenjoy.dojo.client.AbstractBoard
import com.codenjoy.dojo.services.Point
import com.codenjoy.dojo.snakebattle.model.Elements
import com.codenjoy.dojo.snakebattle.model.Elements._

import scala.collection.JavaConverters._

/**
  * Класс, обрабатывающий строковое представление доски.
  * Содержит ряд унаследованных методов {@see AbstractBoard},
  * но ты можешь добавить сюда любые свои методы на их основе.
  */
class BoardScala extends AbstractBoard[Elements] {

  // твои новые методы

  def isBarrierAt(x: Int, y: Int): Boolean = {
    val barrierElements = List(WALL, START_FLOOR,
      ENEMY_HEAD_SLEEP, ENEMY_TAIL_INACTIVE, TAIL_INACTIVE)
    barrierElements.exists(isAt(x, y, _))
  }

  def getHead: Option[Point] = getPoints(HEAD_DOWN, HEAD_LEFT, HEAD_RIGHT, HEAD_UP, HEAD_SLEEP, HEAD_EVIL, HEAD_FLY).headOption

  def isGameOver: Boolean = getHead.isEmpty

  def getPoints(elements: Elements*): List[Point] = super.get(elements: _*).asScala.toList

  override def valueOf(ch: Char): Elements = Elements.valueOf(ch)

  override protected def inversionY(y: Int): Int = size - 1 - y

}
