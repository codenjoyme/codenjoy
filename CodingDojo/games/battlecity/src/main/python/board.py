#! /usr/bin/env python3

###
# #%L
# Codenjoy - it's a dojo-like platform from developers to developers.
# %%
# Copyright (C) 2018 - 2021 Codenjoy
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
from element import Element
from point import Point

WALLS = ('WALL', 'WALL_DESTROYED_DOWN', 'WALL_DESTROYED_UP', 'WALL_DESTROYED_LEFT',
         'WALL_DESTROYED_RIGHT', 'WALL_DESTROYED_DOWN_TWICE', 'WALL_DESTROYED_UP_TWICE',
         'WALL_DESTROYED_LEFT_TWICE', 'WALL_DESTROYED_RIGHT_TWICE', 'WALL_DESTROYED_LEFT_RIGHT',
         'WALL_DESTROYED_UP_DOWN', 'WALL_DESTROYED_UP_LEFT', 'WALL_DESTROYED_RIGHT_UP',
         'WALL_DESTROYED_DOWN_LEFT',  'WALL_DESTROYED_DOWN_RIGHT',)

MY_TANK = ('TANK_UP', 'TANK_DOWN', 'TANK_LEFT', 'TANK_RIGHT',)

AI_TANK = ('AI_TANK_UP', 'AI_TANK_RIGHT', 'AI_TANK_DOWN', 'AI_TANK_LEFT',)

OTHER_TANK = ('OTHER_TANK_UP', 'OTHER_TANK_RIGHT', 'OTHER_TANK_DOWN', 'OTHER_TANK_LEFT',)


class Board:

    def __init__(self, board_string):
        self._string = board_string.replace('\n', '')
        self._len = len(self._string)  # the length of the string
        self._size = int(sqrt(self._len))  # size of the board
        self._board = []

    def _find_all(self, element):
        _points = []
        _a_char = element.char
        for i, c in enumerate(self._string):
            if c == _a_char:
                _points.append(self._strpos2pt(i))
        return _points

    def _strpos2pt(self, strpos):
        return Point(*self._strpos2xy(strpos))

    def _strpos2xy(self, strpos):
        return strpos % self._size, self._size - (strpos // self._size)

    def _xy2strpos(self, x, y):
        return self._size * (self._size - y) + x

    def get_at(self, x, y):
        return Element(self._string[self._xy2strpos(x, y)])

    def is_at(self, x, y, element_object):
        return element_object == self.get_at(x, y)

    def is_barrier_at(self, x, y):
        """ Return true if barrier is at x,y."""
        return Point(x, y) in self.barriers

    def is_tree_at(self, x, y):
        """ Return true if tree is at x,y."""
        return Point(x, y) in self.tree

    def is_other_tank_at(self, x, y):
        """ Return true if other_tank is at x,y."""
        return Point(x, y) in self.other_tank

    @property
    def my_tank(self):
        points = []
        for item in MY_TANK:
            points.extend(self._find_all(Element(item)))
        return points

    @property
    def ai_tank(self):
        points = []
        for item in AI_TANK:
            points.extend(self._find_all(Element(item)))
        return points

    @property
    def other_tank(self):
        points = []
        for item in OTHER_TANK:
            points.extend(self._find_all(Element(item)))
        return points

    @property
    def bullet(self):
        points = []
        points.extend(self._find_all(Element('BULLET')))
        return points

    def get_battle_walls(self):
        """ Retuns the list of walls Element Points."""
        points = []
        points.extend(self._find_all(Element('BATTLE_WALL')))
        return points

    @property
    def barriers(self):
        points = []
        points.extend(self.walls)
        points.extend(self.battle_walls)
        points.extend(self.river)
        points.extend(self.ice)
        points.extend(self.ai_tank)
        points.extend(self.other_tank)
        return points

    @property
    def tanks(self):
        points = []
        points.extend(self.ai_tank)
        points.extend(self.other_tank)
        return points

    @property
    def ice(self):
        points = []
        points.extend(self._find_all(Element('ICE')))
        return points

    @property
    def river(self):
        points = []
        points.extend(self._find_all(Element('RIVER')))
        return points

    @property
    def tree(self):
        points = []
        points.extend(self._find_all(Element('TREE')))
        return points

    @property
    def battle_walls(self):
        points = []
        points.extend(self._find_all(Element('BATTLE_WALL')))
        return points

    @property
    def walls(self):
        points = []
        for item in WALLS:
            points.extend(self._find_all(Element(item)))
        return points

    def is_near(self, x, y, elem):
        """ Return number  of the element near a point  x,y"""
        _is_near = False
        if not Point(x, y).is_bad(self._size):
            _is_near = (self.is_at(x+1, y, elem) or
                        self.is_at(x-1, y, elem) or
                        self.is_at(x, y+1, elem) or
                        self.is_at(x, y-1, elem))
        return _is_near

    def is_near_all(self, x, y, points):
        """ Return number  of points near a point  x,y."""
        _near_count = 0
        if not Point(x, y).is_bad(self._size) and points:
            for _x, _y in ((x+1, y), (x-1, y), (x, y+1), (x, y-1)):
                if Point(_x, _y) in points:
                    _near_count += 1
        return _near_count

    def direction(self, x, y):
        """ returns the direction of the point  x,y """
        courses = ('LEFT', 'RIGHT', 'DOWN', 'UP')
        if not Point(x, y).is_bad(self._size):
            _dir = self.get_at(x, y)
            for item in courses:
                if item in str(_dir.name).rpartition('_'):
                    return item
        else:
            return None

    def to_string(self):
        return ("\nBoard:\n{brd}\nTank at: {tnk}\nOther Tank "
                "at: {other}\nAI Tank at: {aitnk}\nBullet at:"
                "{blt}".format(brd=self._line_by_line(),
                               tnk=self.my_tank,
                               other=self.other_tank,
                               aitnk=self.ai_tank,
                               blt=self.bullet)
                )

    def _line_by_line(self):
        return '\n'.join([self._string[i:i + self._size] for i in range(0, self._len, self._size)])


if __name__ == '__main__':
    raise RuntimeError("This module is not designed to be ran from CLI")
