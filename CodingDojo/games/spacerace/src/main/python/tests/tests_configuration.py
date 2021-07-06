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
from util_import import set_imports
set_imports()
from configuration import Configuration

class ConfigurationTests(unittest.TestCase):

    def test_connectionString_ShouldBeStr(self):
        self.assertIsInstance(Configuration().connectionString, str)
    
    def test_connectionTimeout_ShouldBeInt(self):
        self.assertIsInstance(Configuration().connectionTimeout, int)

if __name__ == "__main__":
    unittest.main()