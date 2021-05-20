#!/usr/bin/env python3

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


from board import Board
from command import Command
from random import choice

_COURSES = ('LEFT', 'RIGHT', 'DOWN', 'UP')

""" This class should contain the movement generation algorithm."""


class DirectionSolver:
    """ This class should contain the movement generation algorythm."""

    def __init__(self):
        self._direction = None
        self._board = None
        self._last = None
        self._count = 0

    def get(self, board_string):
        """ The function that should be implemented."""
        self._board = Board(board_string)
        _command = str(self.find_command())
        print(self._board.to_string())
        print("Sending Command {}".format(_command))

        return _command

    def find_command(self):

        course = list(_COURSES)
        _direction = choice(course)
        if not self._board.my_tank:
            __dir = choice(course)
        else:
            tank = self._board.my_tank[0]
            tank_x, tank_y = tank.x, tank.y
            my_direction = self._board.direction(tank_x, tank_y)
            _direction = self.next_step(tank_x, tank_y, my_direction)

        return _direction

    def next_step(self, x, y, _direction):
        """   find new coordinates for the direction
        """
        _direction = Command(_direction)
        _x, _y = _direction.new_x(x), _direction.new_y(y)

        """analyzing a new course and choice of old or new course
        """
        return _direction


if __name__ == '__main__':
    raise RuntimeError("This module is not intended to be ran from CLI")
