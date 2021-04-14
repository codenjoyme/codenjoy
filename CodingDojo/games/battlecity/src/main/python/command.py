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
import collections
from dataclasses import dataclass

Move = collections.namedtuple('Move', 'id, x, y')


@dataclass
class Directions(object):
    left = Move(0, -1,  0)
    right = Move(1,  1,  0)
    up = Move(2,  0, 1)
    down = Move(3,  0,  -1)
    act = Move(4,  0,  0)
    stop = Move(5,  0,  0)


class Command:

    def __init__(self, direction):
        """ Initializate direction from string or tuple."""
        self._name = direction
        try:
            self._dir = getattr(Directions, direction.lower())
        except AttributeError:
            raise ValueError("No Such Command: {}".format(direction))

    def __eq__(self, other):
        return self._name == other._name and self._dir == other._dir

    def __ne__(self, other):
        return self._name == other._name and self._dir != other._dir

    def is_null(self):
        return self._name == 'NULL'

    def __str__(self):
        return self._name

    @property
    def x(self):
        return self._dir.x

    @property
    def y(self):
        return self._dir.y

    def new_x(self, x):
        return x + self._dir.x

    def new_y(self, y):
        return y + self._dir.y



if __name__ == '__main__':
    raise RuntimeError("This module is not designed to be ran from CLI")
