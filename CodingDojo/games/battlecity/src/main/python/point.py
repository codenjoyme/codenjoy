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

class Point:
    def __init__(self, x=0, y=0):
        self._x = int(x)
        self._y = int(y)

    def __key(self):
        return self._x, self._y

    def __str__(self):
        return "[{},{}]".format(self._x, self._y)

    def __repr__(self):
        return "[{},{}]".format(self._x, self._y)

    def __eq__(self, other_point):
        return self.__key() == other_point.__key()

    # def __hash__(self):
    #     return hash(self.__key())

    @property
    def x(self):
        return self._x

    @property
    def y(self):
        return self._y

    def is_bad(self, board_size):
        return (self._x > board_size or self._x < 0 or
                self._y > board_size or self._y < 0)


if __name__ == '__main__':
    raise RuntimeError("This module is not expected to be ran from CLI")
