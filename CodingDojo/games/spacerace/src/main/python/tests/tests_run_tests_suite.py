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
from tests_board import BoardTests
from tests_configuration import ConfigurationTests
from tests_direction import DirectionTests
from tests_elements import ElementsTests
from tests_point import PointTests
from assets_tests import AssetsTests

testsSuite = unittest.TestSuite()
testsSuite.addTests(unittest.makeSuite(BoardTests))
testsSuite.addTests(unittest.makeSuite(ConfigurationTests))
testsSuite.addTests(unittest.makeSuite(DirectionTests))
testsSuite.addTests(unittest.makeSuite(ElementsTests))
testsSuite.addTests(unittest.makeSuite(PointTests))
testsSuite.addTests(unittest.makeSuite(AssetsTests))

runner = unittest.TextTestRunner(verbosity=2)
runner.run(testsSuite)