package com.codenjoy.dojo.battlecity.client

import com.codenjoy.dojo.battlecity.model.Elements
import com.codenjoy.dojo.battlecity.model.Elements._
import com.codenjoy.dojo.client.AbstractBoard
import com.codenjoy.dojo.services.Direction._
import com.codenjoy.dojo.services.{Direction, Point}

import scala.collection.JavaConverters._

/**
  * Класс, обрабатывающий строковое представление доски.
  * Содержит ряд унаследованных методов {@see AbstractBoard},
  * но ты можешь добавить сюда любые свои методы на их основе.
  */
class BoardScala extends AbstractBoard[Elements] {

  // твои новые методы

  def isBarrierAt(x: Int, y: Int): Boolean = {
    val barrierElements = List(BATTLE_WALL, CONSTRUCTION,
      CONSTRUCTION_DESTROYED_DOWN, CONSTRUCTION_DESTROYED_UP,
      CONSTRUCTION_DESTROYED_LEFT, CONSTRUCTION_DESTROYED_RIGHT,
      CONSTRUCTION_DESTROYED_DOWN_TWICE, CONSTRUCTION_DESTROYED_UP_TWICE,
      CONSTRUCTION_DESTROYED_LEFT_TWICE, CONSTRUCTION_DESTROYED_RIGHT_TWICE,
      CONSTRUCTION_DESTROYED_LEFT_RIGHT, CONSTRUCTION_DESTROYED_UP_DOWN,
      CONSTRUCTION_DESTROYED_UP_LEFT, CONSTRUCTION_DESTROYED_RIGHT_UP,
      CONSTRUCTION_DESTROYED_DOWN_LEFT, CONSTRUCTION_DESTROYED_DOWN_RIGHT)
    barrierElements.exists(isAt(x, y, _))
  }

  def getMe: Option[Point] = getPoints(TANK_UP, TANK_DOWN, TANK_LEFT, TANK_RIGHT).headOption

  def isGameOver: Boolean = getMe.isEmpty

  def getMyDirection: Direction = {
    val me = getMe.orNull
    if (me == null) return null
    if (isAt(me, TANK_UP)) return UP
    if (isAt(me, TANK_RIGHT)) return RIGHT
    if (isAt(me, TANK_DOWN)) return DOWN
    LEFT
  }

  def isBulletAt(x: Int, y: Int): Boolean = getAt(x, y).equals(BULLET)

  def getEnemies: List[Point] = getPoints(
    OTHER_TANK_UP, OTHER_TANK_RIGHT, OTHER_TANK_DOWN, OTHER_TANK_LEFT,
    AI_TANK_UP, AI_TANK_RIGHT, AI_TANK_DOWN, AI_TANK_LEFT
  )

  def getPoints(elements: Elements*): List[Point] = super.get(elements: _*).asScala.toList

  override def valueOf(ch: Char): Elements = valueOf(ch)

  override protected def inversionY(y: Int): Int = size - 1 - y

}
