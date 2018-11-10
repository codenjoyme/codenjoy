package com.codenjoy.dojo.tetris.client

/*-
 * #%L
 * Codenjoy - it's a dojo-like platform from developers to developers.
 * %%
 * Copyright (C) 2018 Codenjoy
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

import com.codenjoy.dojo.services.Point
import com.codenjoy.dojo.tetris.model.Elements
import org.json.JSONObject
import org.junit.Test
import com.codenjoy.dojo.services.PointImpl.pt
import org.junit.Assert.assertEquals

object BoardScalaTest {
  def getBoard(glass: String, figureType: String, point: Point, futureFigures: Array[String]): Board = {
    val result: JSONObject = BoardTest.getJson(glass, figureType, point, futureFigures)
    new BoardScala().forString(result.toString).asInstanceOf[Board]
  }
}

class BoardScalaTest {
  @Test def test() {
    val board: Board = BoardTest.getBoard(
      "......." +
      "......I" +
      "..LL..I" +
      "...LI.I" +
      ".SSLI.I" +
      "SSOOIOO" +
      "..OOIOO",
      "T",
      pt(1, 2),
      Array[String]("I", "O", "L", "Z"))

    assertEquals(Elements.NONE, board.getGlass.getAt(0, 0))
    assertEquals(true, board.isFree(0, 0))

    assertEquals(Elements.YELLOW, board.getGlass.getAt(2, 0))
    assertEquals(false, board.isFree(2, 0))

    assertEquals(Elements.GREEN, board.getGlass.getAt(2, 2))
    assertEquals(false, board.isFree(2, 2))

    assertEquals(Elements.ORANGE, board.getGlass.getAt(3, 4))
    assertEquals(false, board.isFree(3, 0))

    assertEquals("[[0,1], [1,1], [1,2], [2,2]]", board.getGlass.get(Elements.GREEN).toString)
    assertEquals("[., L, ., L, L, ., I, ., .]", board.getGlass.getNear(pt(3, 4)).toString)

    assertEquals("[1,2]", board.getCurrentFigurePoint.toString)
    assertEquals("T", board.getCurrentFigureType.toString)
    assertEquals("[I, O, L, Z]", board.getFutureFigures.toString)
  }
}
