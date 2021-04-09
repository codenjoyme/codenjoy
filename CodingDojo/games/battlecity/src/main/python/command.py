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

_COMMAND = dict(
                LEFT=(0, -1,  0),
                RIGHT=(1,  1,  0),
                UP=(2,  0, -1),
                DOWN=(3,  0,  1),
                ACT=(4,  0,  0),
                STOP=(5,  0,  0),
                NULL=(-1,  0,  0)
                )


class Command:

    def __init__(self, direction):
        """ Initializate direction from string or tuple."""
        for name, value in _COMMAND.items():
            if direction == name or direction == value:
                self._name = name
                self._dir = _COMMAND[name]
                break
        else:
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
        return self._dir[1]

    @property
    def y(self):
        return self._dir[2]


    # @x.setter
    # def x(self, x):
    #     self.x = x + self._dir[1]
    #
    # @y.setter
    # def y(self, y):
    #     self.y = y - self._dir[2]

    def new_x(self, x):
        return x + self._dir[1]

    def new_y(self, y):
        return y + self._dir[2]



if __name__ == '__main__':
    raise RuntimeError("This module is not designed to be ran from CLI")
