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
from point import Point

class PointTests(unittest.TestCase):
    
    def test_dataAccess(self):
        x = random.randint(-2e6, 2e6)
        y = random.randint(-2e6, 2e6)
        point = Point(x, y)
        self.assertEqual(point.x, x)
        self.assertEqual(point.y, y)

    def test_equivalence(self):
        x = random.randint(-2e6, 2e6)
        y = random.randint(-2e6, 2e6)
        pointA = Point(x, y)
        pointB = Point(x, y)
        pointC = Point(x + 1, y)
        pointD = Point(x, y + 1)
        self.assertEqual(pointA, pointB)
        self.assertNotEqual(pointA, pointC)
        self.assertNotEqual(pointA, pointD)
        self.assertNotEqual(pointD, pointC)

if __name__=="__main__":
    unittest.main()