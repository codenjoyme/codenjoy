#! /usr/bin/env python3
###
# #%L
# Codenjoy - it's a dojo-like platform from developers to developers.
# %%
# Copyright (C) 2018 Codenjoy
# %%
# This program is free software: you can redistribute it and/or modify
# it under the terms of the GNU General Public License as
# published by the Free Software Foundation, either version 3 of the
# License, or (at your option) any later version.
# 
# This program is distributed in the hope that it will be useful,
# but WITHOUT ANY WARRANTY; without even the implied warranty of
# MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
# GNU General Public License for more details.
# 
# You should have received a copy of the GNU General Public
# License along with this program.  If not, see
# <http://www.gnu.org/licenses/gpl-3.0.html>.
# #L%
###

import unittest

from tests_mock_element import ElementMock
from point import Point
from board import Board
from elements import Elements
from neighbour_type import NeighbourType

class BoardTests(unittest.TestCase):
 
    def test_get_all_extend_ShouldReturnValidDictionary(self):
        boardString = "1234"
        expected = dict([
            (Point(0,0), ElementMock("1")),
            (Point(1,0), ElementMock("2")),
            (Point(0,1), ElementMock("3")),
            (Point(1,1), ElementMock("4"))
        ])
        board = Board(boardString, lambda c: ElementMock(c))
        actual = board.get_all_extend()
        self.assertDictEqual(actual, expected)
   
    def test_get_size_ShouldReturnCorrectValue(self):
        board = Board("123456789", None) 
        self.assertEqual(board.size, 3)
        board = Board("1234", None)
        self.assertEqual(board.size, 2)
        board = Board("1234123\n412341234", None)
        self.assertEqual(board.size, 4)

    def test_find_all_ShouldReturnRightCoordinates(self):
        board = Board("123456789", None) 
        #single element
        point1 = board.find_all(ElementMock("1")) 
        self.assertEqual(point1, [Point(0, 0)])
        #single element in other position
        point2 = board.find_all(ElementMock("2"))
        self.assertEqual(point2, [Point(1, 0)])
        #multiple elements at once
        point34 = board.find_all(ElementMock("3"),ElementMock("4"))
        self.assertEqual(point34, [Point(2, 0),Point(0,1)])
        #multiple elements in arrayList
        point578 = board.find_all( *[ElementMock("5"), ElementMock("7"), ElementMock("8")])
        self.assertEqual(point578, [Point(1, 1), Point(0,2), Point(1, 2)])

    def test_getAt_ShouldReturnRightValue(self):
        boardString ="    5    "
        noneElement = ElementMock(" ", "None", 0)
        fiveElement = ElementMock("5", "Five", 5)
        board = Board(boardString, lambda c: ElementMock("5", "Five", 5) if c == "5" else  ElementMock(" ", "None", 0))
        self.assertEqual(board.get_at(0, 0), noneElement)
        self.assertEqual(board.get_at(1, 0), noneElement)
        self.assertEqual(board.get_at(0, 1), noneElement)
        self.assertEqual(board.get_at(1, 1), fiveElement)

    def test_getAt_ShouldReturnBarierElement_WhenOutOfField(self):
        boardString ="    5    "
        barierElement = ElementMock("*", "Barier", 0)
        board = Board(boardString, None, barierElement)
        self.assertEqual(board.get_at(-1, 1),  barierElement)
        self.assertEqual(board.get_at(3, 0),  barierElement)
        self.assertEqual(board.get_at(0, 3),  barierElement)
        self.assertEqual(board.get_at(3, 3),  barierElement)

    def test_is_at_ShouldReturnRightValue(self):
        boardString = "    5  1 "
        noneElement = ElementMock(" ", "None", 0)
        fiveElement = ElementMock("5", "Five", 5)
        oneElement = ElementMock("1", "three", 1)
        board = Board(boardString, lambda c: ElementMock("5", "Five", 5) if c == "5" else  ElementMock(" ", "None", 0))
        self.assertTrue(board.is_at(0, 0, noneElement))
        self.assertFalse(board.is_at(1, 0, fiveElement))
        self.assertTrue(board.is_at(1, 1, fiveElement))
        self.assertFalse(board.is_at(1, 1, noneElement))
        self.assertTrue(board.is_at(1, 1, fiveElement, oneElement))
        self.assertFalse(board.is_at(1, 1, oneElement))

    def test_get_barriers_ShouldReturnBariers(self):
        boardString = "111   353"
        oneElement = ElementMock("1")
        threeElement = ElementMock("3")
        board = Board(boardString, None, *[oneElement, threeElement])
        self.assertEqual(board.get_barriers(), [Point(0, 0), Point(1, 0), Point(2, 0), Point(0, 2), Point(2, 2)])

    def test_is_barier_ShouldReturnCorrectValue(self):
        boardString = "111   353"
        oneElement = ElementMock("1")
        threeElement = ElementMock("3")
        board = Board(boardString, None, oneElement, threeElement)
        self.assertTrue(board.is_barrier_at(0, 0))
        self.assertTrue(board.is_barrier_at(2, 2))
        self.assertFalse(board.is_barrier_at(1, 1))
   
    def test_is_at_ShouldReturnCorrectValue(self):
        boardString = "*7*7 7*7*"
        board = Board(boardString, Elements.getByChar, Elements.WALL)
        self.assertTrue(board.is_near(1, 1, [NeighbourType.DIAMOND], Elements.getByChar("7")))
        self.assertTrue(board.is_near(1, 1, [NeighbourType.CROSS], Elements.getByChar("*")))
        self.assertTrue(board.is_near(1, 1, [NeighbourType.DIAMOND], Elements.getByChar("*"), Elements.getByChar("7")))
        self.assertTrue(board.is_near(1, 1, [NeighbourType.CROSS], Elements.getByChar("*"), Elements.getByChar("7")))
        self.assertFalse(board.is_near(1, 1, [NeighbourType.DIAMOND], Elements.getByChar("*")))
        self.assertFalse(board.is_near(1, 1, [NeighbourType.CROSS], Elements.getByChar("7")))
        self.assertTrue(board.is_near(1, 1, [NeighbourType.DIAMOND, NeighbourType.CROSS], Elements.getByChar("*")))
        self.assertFalse(board.is_near(1, 1, [NeighbourType.DIAMOND, NeighbourType.CROSS]))

    def test_count_near_ShouldReturnCorrectValue(self):
        boardString = "123456789"
        board = Board(boardString, lambda c: ElementMock(c))
        self.assertEqual(board.count_near(1, 1, [NeighbourType.DIAMOND], ElementMock("2")), 1)
        self.assertEqual(board.count_near(1, 1, [NeighbourType.CROSS], ElementMock("2")), 0)
        self.assertEqual(board.count_near(1, 1, [NeighbourType.DIAMOND, NeighbourType.CROSS], ElementMock("2")), 1)
        self.assertEqual(board.count_near(1, 1, [NeighbourType.CROSS], ElementMock("1"), ElementMock("2"),ElementMock("3")), 2)

if __name__ == "__main__":
    unittest.main()