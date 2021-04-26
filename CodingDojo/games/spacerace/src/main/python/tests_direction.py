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
import random
from direction import Direction
from point import Point

class DirectionTests(unittest.TestCase):

    def test_toString_ShouldReturnRightValue_WhenWithActModifierIsNotApplied(self):
        testData = {
            Direction.ACT:      "ACT",
            Direction.SUICUDE:  "ACT(0)",
            Direction.UP:       "UP",
            Direction.DOWN:     "DOWN",
            Direction.LEFT:     "LEFT",
            Direction.RIGHT:    "RIGHT",
            Direction.STOP:     "STOP"
        }

        for k, v in testData.items():
            self.assertEqual(k.to_string(), v, f"{k} is invalid")

    def test_toString_ShouldReturnRightValue_WhenWithActModifierIsApplied(self):
        testData = {
            Direction.ACT:      "ACT",
            Direction.SUICUDE:  "ACT(0)",
            Direction.UP:       "UP, ACT",
            Direction.DOWN:     "DOWN, ACT",
            Direction.LEFT:     "LEFT, ACT",
            Direction.RIGHT:    "RIGHT, ACT",
            Direction.STOP:     "STOP, ACT"
        }

        for k,v in testData.items():
            self.assertEqual(k.withAct().to_string(), v, f"{k} is invalid")

    def test_get_dx_ShouldReturnValidValues(self):
        testData = {
            Direction.ACT:      0,
            Direction.SUICUDE:  0,
            Direction.UP:       0,
            Direction.DOWN:     0,
            Direction.LEFT:     -1,
            Direction.RIGHT:    1,
            Direction.STOP:     0
        }
        for k, v in testData.items():
            self.assertEqual(k.get_dx(), v)
            self.assertEqual(k.withAct().get_dx(), v)
        
    def test_get_dy_ShouldReturnValidValues(self):
        testData = {
            Direction.ACT:      0,
            Direction.SUICUDE:  0,
            Direction.UP:       -1,
            Direction.DOWN:     1,
            Direction.LEFT:     0,
            Direction.RIGHT:    0,
            Direction.STOP:     0
        }
        for k, v in testData.items():
            self.assertEqual(k.get_dy(), v)
            self.assertEqual(k.withAct().get_dy(), v)

    def test_change_x_ShouldChangeValueRight(self):
        testData = {
            Direction.ACT:      0,
            Direction.SUICUDE:  0,
            Direction.UP:       0,
            Direction.DOWN:     0,
            Direction.LEFT:     -1,
            Direction.RIGHT:    1,
            Direction.STOP:     0
        }
        for k, v in testData.items():
            givenValue = random.randint(-2e6, 2e6)
            expectedValue = givenValue + v
            self.assertEqual(k.change_x(givenValue), expectedValue)
            self.assertEqual(k.withAct().change_x(givenValue), expectedValue)

    def test_change_y_ShouldChangeValueRight(self):
        testData = {
            Direction.ACT:      0,
            Direction.SUICUDE:  0,
            Direction.UP:       -1,
            Direction.DOWN:     1,
            Direction.LEFT:     0,
            Direction.RIGHT:    0,
            Direction.STOP:     0
        }
        for k, v in testData.items():
            givenValue = random.randint(-2e6, 2e6)
            expectedValue = givenValue + v
            self.assertEqual(k.change_y(givenValue), expectedValue)
            self.assertEqual(k.withAct().change_y(givenValue), expectedValue)
    
    def test_change_ShouldChangePoint(self):
        testData = {
            Direction.ACT:      (0, 0),
            Direction.SUICUDE:  (0, 0),
            Direction.UP:       (0,-1),
            Direction.DOWN:     (0, 1),
            Direction.LEFT:     (-1,0),
            Direction.RIGHT:    (1, 0),
            Direction.STOP:     (0, 0)
        }
        for k, v in testData.items():
            dx, dy = v
            givenX = random.randint(-2e6, 2e6)
            givenY = random.randint(-2e6, 2e6)
            givenPoint = Point(givenX, givenY)
            expectedPoint = Point(givenX + dx, givenY + dy)
            self.assertEqual(k.change(givenPoint), expectedPoint)
            self.assertEqual(k.withAct().change(givenPoint), expectedPoint)


    def test_inverted_ShouldReturnValidValues(self):
        testData = {
            Direction.LEFT   : Direction.RIGHT,
            Direction.RIGHT  : Direction.LEFT,
            Direction.UP     : Direction.DOWN,
            Direction.DOWN   : Direction.UP,
            Direction.ACT    : Direction.ACT,
            Direction.SUICUDE: Direction.SUICUDE,
            Direction.STOP   : Direction.STOP
        }
        for k, v in testData.items():
            self.assertEqual(k.inverted(), v)
            self.assertEqual(k.withAct().inverted(), v.withAct())
    
    def test_withAct_ShouldReturnExpectedValue(self):
        '''   withAct tested by all tests above  '''

if __name__ == '__main__':
    unittest.main()                


