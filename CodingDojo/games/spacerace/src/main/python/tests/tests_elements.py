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
from elements import Elements

class ElementsTests(unittest.TestCase):
    def test_getByShar_ShouldMapToTheRightElement_WhenCharDefined(self):
        chars = " 7☺☻+0♣x*$☼"
        elements = list(Elements)
        self.assertEqual(len(chars), len(elements),"Chars and elements counts are not equal")
        sequence = list(zip(chars, elements))
        self.assertEqual(len(sequence), len(elements), "Zipped sequence is not valid")
        for item in sequence:
            self.assertEqual(Elements.getByChar(item[0]), item[1], f"item {item} is not valid")

    def test_getByChar_ShouldRaiseKeyError_WhenCharIsNotDefined(self):
        with self.assertRaises(KeyError):
            Elements.getByChar("Ъ") 
                   

if __name__ == '__main__':
    unittest.main()        