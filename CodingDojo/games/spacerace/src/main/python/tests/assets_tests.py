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
import os
import inspect
from util_import import set_imports
set_imports()

from elements import Elements
from direction import Direction

class AssetsTests(unittest.TestCase):
    def __init__(self, methodName: str) -> None:
        super().__init__(methodName=methodName)
        self.assetsdir = os.path.join(os.path.dirname(os.path.abspath(inspect.getfile(inspect.currentframe()))),"..\\assets")


    def _assert_asset_(self, asset):
        filename = os.path.join(self.assetsdir, asset)
        self.assertTrue(os.path.isfile(filename), "file {} doesnot exists".format(filename))


    def test_directions_assets(self):
        for dir in [Direction.LEFT, Direction.RIGHT, Direction.UP, Direction.DOWN, Direction.ACT]:
            self._assert_asset_("{}_inactive.png".format(dir.to_string()))
            self._assert_asset_("{}_active.png".format(dir.to_string()))

    def test_elements_assets(self):
        for element in Elements:
            self._assert_asset_("{}.png".format(element.name))

if __name__ == "__main__":
    unittest.main()