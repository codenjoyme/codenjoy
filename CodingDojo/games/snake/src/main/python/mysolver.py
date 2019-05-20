#!/usr/bin/env python3

###
# #%L
# Codenjoy - it's a dojo-like platform from developers to developers.
# %%
# Copyright (C) 2019 Codenjoy
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

from random import choice
from board import Board
from element import Element
from direction import Direction


class DirectionSolver:
    """ This class should contain the movement generation algorithm."""

    def __init__(self):
        self._direction = None
        self._board = None
        self._last = None
        self._count = 0

    def get(self, board_string):
        """ The function that should be implemented."""
        self._board = Board(board_string)
        _command = self.find_direction()
        print("Sending Command {}".format(_command))
        return _command

    def find_direction(self):
        """ This is an example of direction solver subroutine."""
        _direction = Direction('NULL').to_string()
        if self._board.is_my_snake_dead():
            print("Snake is dead. Sending 'NULL' command...")
            return _direction
        # here's how we find the current Point of our snake head
        _bm = self._board.get_head()
        _bm_x, _bm_y = _bm.get_x(), _bm.get_y()
        # print("Snake head at {}".format(_bm))
        print(self._board.to_string())

        # TODO: Implement your logic here
        _dir = Direction(choice(('LEFT', 'RIGHT', 'DOWN', 'UP')))

        _direction = _dir.to_string()
        self._last = _bm.get_x(), _bm.get_y()
        return _direction


if __name__ == '__main__':
    raise RuntimeError("This module is not intended to be ran from CLI")
