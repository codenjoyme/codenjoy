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
from element import Element
from direction import Direction

class Board:
    """ Class describes the Board field for Snake game."""
    def __init__(self, board_string):
        self._string = board_string.replace('\n', '')
        self._len = len(self._string)  # the length of the string
        self._size = int(sqrt(self._len))  # size of the board
        #print("Board size is sqrt", self._len, self._size)

    def _find_all(self, element):
        """ Returns the list of points for the given element type."""
        _points = []
        _a_char = element.get_char()
        for i, c in enumerate(self._string):
            if c == _a_char:
                 _points.append(self._strpos2pt(i))
        return _points

    def get_at(self, x, y):
        """ Return an Element object at coordinates x,y."""
        return Element(self._string[self._xy2strpos(x, y)])

    def is_at(self, x, y, element_object):
        """ Return True if Element is at x,y coordinates."""
        return element_object == self.get_at(x, y)

    def is_near(self, x, y, elem):
        _is_near = False
        if not Point(x, y).is_bad(self._size):
            _is_near = (self.is_at(x + 1, y, elem) or
                        self.is_at(x - 1, y, elem) or
                        self.is_at(x, 1 + y, elem) or
                        self.is_at(x, 1 - y, elem))
        return _is_near

    def count_near(self, x, y, elem):
        """ Counts the number of occurencies of elem nearby """
        _near_count = 0
        if not Point(x, y).is_bad(self._size):
            for _x, _y in ((x + 1, y), (x - 1, y), (x, 1 + y), (x, 1 - y)):
                if self.is_at(_x, _y, elem):
                    _near_count += 1
        return _near_count

    def get_snake_direction(self):
        """ Return the snake head direction."""
        head = self.get_head()
        if not head:
            return None

        if self.is_at(head.get_x(), head.get_y(), Element('HEAD_LEFT')):
            return Direction('LEFT')
        elif self.is_at(head.get_x(), head.get_y(), Element('HEAD_RIGHT')):
            return Direction('RIGHT')
        elif self.is_at(head.get_x(), head.get_y(), Element('HEAD_UP')):
            return Direction('UP')
        else:
            return Direction('DOWN')

    def get_head(self):
        """ Return the point where your snake head is."""
        points = set()
        points.update(self._find_all(Element('HEAD_UP')))
        points.update(self._find_all(Element('HEAD_DOWN')))
        points.update(self._find_all(Element('HEAD_LEFT')))
        points.update(self._find_all(Element('HEAD_RIGHT')))
        if not points:
            return None
        else:
            return list(points)[0]

    def get_snake(self):
        """ Return all points where your snake body (with head) is."""
        head = self.get_head()
        if not head:
            return None
        else:
            result = set();
            result.update(self._find_all(Element('TAIL_END_DOWN')))
            result.update(self._find_all(Element('TAIL_END_DOWN')))
            result.update(self._find_all(Element('TAIL_END_LEFT')))
            result.update(self._find_all(Element('TAIL_END_UP')))
            result.update(self._find_all(Element('TAIL_END_RIGHT')))
            result.update(self._find_all(Element('TAIL_HORIZONTAL')))
            result.update(self._find_all(Element('TAIL_VERTICAL')))
            result.update(self._find_all(Element('TAIL_LEFT_DOWN')))
            result.update(self._find_all(Element('TAIL_LEFT_UP')))
            result.update(self._find_all(Element('TAIL_RIGHT_DOWN')))
            result.update(self._find_all(Element('TAIL_RIGHT_UP')))
            return list(result)

    def get_apples(self):
        """ Return the list of points for apples."""
        return self._find_all(Element('GOOD_APPLE'))

    def get_stones(self):
        """ Return the list of points for stones."""
        return self._find_all(Element('BAD_APPLE'))

    def get_barriers(self):
        """ Return the list of points for barriers."""
        points = set()
        points.update(self.get_stones())
        points.update(self.get_walls())
        return list(points)

    def is_barrier_at(self, x, y):
        """ Return true if barrier is at x,y."""
        return Point(x, y) in self.get_barriers()

    def is_snake_alive(self):
        """ Returns True if your snake still alive."""
        if self.get_head():
            return True
        else:
            return False

    def get_walls(self):
        """ Retuns the list of walls."""
        return self._find_all(Element('BREAK'))

    def to_string(self):
        return ("Board:\n{brd}\n"
                "Apple at: {apl}\n"
                "Stones at: {stn}\n"
                'Head at: {hed}\n'
                "Snake at: {snk}\n"
                "Current direction: {dir}".format(brd=self._line_by_line(),
                                          apl=self.get_apples(),
                                          stn=self.get_stones(),
                                          hed=self.get_head(),
                                          snk=self.get_snake(),
                                          dir=self.get_snake_direction())
        )

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
