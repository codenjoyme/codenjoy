#!/usr/bin/env python3

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

from time import time
from random import choice
from board import Board
from elements import Elements
from direction import Direction


class Solver:
    """ This class should contain the movement generation algorithm."""

    def __init__(self):
        ''' initialize globals here '''

    def get(self, board: Board):
        """ The method that should be implemented."""
        _command = Direction.STOP.withAct()
        return _command


if __name__ == '__main__':
    raise RuntimeError("This module is not intended to be ran from CLI")
