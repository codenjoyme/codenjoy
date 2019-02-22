package com.codenjoy.dojo.bomberman.client

import com.codenjoy.dojo.bomberman.model.Elements
import com.codenjoy.dojo.bomberman.model.Elements._
import com.codenjoy.dojo.client.AbstractBoard
import com.codenjoy.dojo.services.{Direction, Point}

import scala.collection.JavaConverters._

/**
  * Класс, обрабатывающий строковое представление доски.
  * Содержит ряд унаследованных методов {@see AbstractBoard},
  * но ты можешь добавить сюда любые свои методы на их основе.
  */
class BoardScala extends AbstractBoard[Elements] {

  // твои новые методы

  def isGameOver: Boolean = getPoints(DEAD_BOMBERMAN).nonEmpty

  def getBomberman: Option[Point] = getPoints(BOMBERMAN, BOMB_BOMBERMAN).headOption

  def getOtherBombermans: List[Point] = getPoints(OTHER_BOMBERMAN, OTHER_BOMB_BOMBERMAN)

  def getMeatChoppers: List[Point] = getPoints(MEAT_CHOPPER)

  def getWalls: List[Point] = getPoints(WALL)

  def getDestroyableWalls: List[Point] = getPoints(DESTROYABLE_WALL)

  def getBombs: List[Point] = getPoints(BOMB_TIMER_1) ++ getPoints(BOMB_TIMER_2) ++
    getPoints(BOMB_TIMER_3) ++ getPoints(BOMB_TIMER_4) ++ getPoints(BOMB_TIMER_5) ++
    getPoints(BOMB_BOMBERMAN) ++ getPoints(OTHER_BOMB_BOMBERMAN)

  def getBlasts: List[Point] = getPoints(BOOM)

  def getFutureBlasts: List[Point] = {
    val result = List[Point]()
    getBombs.foreach(p => {
      // TODO remove code duplicating (for example getNear method in Point class)
      result :+ p :+
        p.copy().change(Direction.UP) :+
        p.copy().change(Direction.RIGHT) :+
        p.copy().change(Direction.DOWN) :+
        p.copy().change(Direction.LEFT)
    })

    result.filterNot(blast => blast.isOutOf(size) || getWalls.contains(blast)).distinct
  }

  def getPoints(elements: Elements*): List[Point] = super.get(elements: _*).asScala.toList

  override def valueOf(ch: Char): Elements = Elements.valueOf(ch)

  override protected def inversionY(y: Int): Int = size - 1 - y

  override protected def withoutCorners(): Boolean = true

  override def toString: String = {
    String.format("%s\n" +
      "Bomberman at: %s\n" +
      "Other bombermans at: %s\n" +
      "Meat choppers at: %s\n" +
      "Destroy walls at: %s\n" +
      "Bombs at: %s\n" +
      "Blasts: %s\n" +
      "Expected blasts at: %s",
      boardAsString, getBomberman, getOtherBombermans, getMeatChoppers,
      getDestroyableWalls, getBombs, getBlasts, getFutureBlasts)
  }

}
