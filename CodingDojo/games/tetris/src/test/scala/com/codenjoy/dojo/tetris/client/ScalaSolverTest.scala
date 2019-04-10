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

import com.codenjoy.dojo.services.{Dice, Direction, Point}
import com.codenjoy.dojo.services.PointImpl.pt
import org.junit.Assert.assertEquals
import org.junit.{Before, Test}
import org.mockito.ArgumentMatchers.anyInt
import org.mockito.Mockito.{mock, when}

class SolverScalaTest {
  private var dice: Dice = null
  private var ai: YourSolverScala = null

  @Before def setup() {
    dice = mock(classOf[Dice])
    ai = new YourSolverScala(dice)
  }

  @Test def should() {
    asertAI(
      "......." +
      "......I" +
      "..LL..I" +
      "...LI.I" +
      ".SSLI.I" +
      "SSOOIOO" +
      "..OOIOO",
      "T",
      pt(1, 2),
      Array[String]("I", "O", "L", "Z"),
      Direction.DOWN)
  }

  private def asertAI(glass: String, figureType: String, point: Point, futureFigures: Array[String], expected: Direction) {
    val board = BoardScalaTest.getBoard(glass, figureType, point, futureFigures)
    val actual: String = ai.get(board)
    assertEquals(expected.toString, actual)
  }

  private def dice(direction: Direction) {
    when(dice.next(anyInt)).thenReturn(direction.value)
  }
}
