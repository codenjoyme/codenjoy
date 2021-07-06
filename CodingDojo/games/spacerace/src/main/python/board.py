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

from math import sqrt
from point import Point

class Board:

    """ Class describes the Board field for SpaceRace game."""
    def __init__(self, board_string: str, createElementFunction, *barierElements):
        self._string = board_string.replace('\n', '')
        self._len = len(self._string)  # the length of the string
        self._size = int(sqrt(self._len))  # size of the board 
        self._createElement = createElementFunction
        self._barierElements = barierElements
        self._extended = None

    @property
    def size(self):
        ''' Returns board size '''
        return self._size

    def get_all_extend(self):
        ''' Returns dictionary where key - point, value - element'''
        if self._extended is None:
            result = []
            for i, c in enumerate(self._string):
                point = self._strpos2pt(i)
                element = self._createElement(c)
                result.append((point, element))
            self._extended = dict(result)
        return self._extended    

    def find_all(self, *elements):
        """ Returns the list of points for the given element type."""
        _points = []
        _a_chars = set(map(lambda element: element.char, elements))
        for i, c in enumerate(self._string):
            if  c in _a_chars:
                _points.append(self._strpos2pt(i))
        return _points

    def get_at(self, x, y):
        """ Returns an Element object at coordinates x,y."""
        if not self._is_valid_point(Point(x, y)):
            return self._barierElements[0]
        return self._createElement(self._string[self._xy2strpos(x, y)])

    def is_at(self, x, y, *elements):
        """ Returns True if Element is at x,y coordinates."""
        return self.get_at(x, y) in elements

    def get_barriers(self):
        """ Returns the list of barriers Points."""
        return self.find_all(*self._barierElements)

    def is_barrier_at(self, x, y):
        """ Returns true if barrier is at x,y."""
        return Point(x, y) in self.get_barriers()
   
    def to_string(self):
        ''' Returns str representation of board '''
        return ("Board:\n{brd}\n".format(brd=self._line_by_line()))

    def _is_valid_point(self, point):
        return point.x >=0 and point.x < self._size and point.y >= 0 and point.y < self._size 
    
    def _line_by_line(self):
        return '\n'.join([self._string[i:i + self._size]
                          for i in range(0, self._len, self._size)])

    def _strpos2pt(self, strpos):
        return Point(*self._strpos2xy(strpos))

    def _strpos2xy(self, strpos):
        return (strpos % self._size, strpos // self._size)

    def _xy2strpos(self, x, y):
        return self._size * y + x
   
if __name__ == '__main__':
    raise RuntimeError("This module is not designed to be ran from CLI")
