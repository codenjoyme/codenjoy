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


class MySolver:
    """ This class should contain the movement generation algorythm."""

    def __init__(self):
        self._direction = None
        self._board = None

    def get(self, board_string):
        """ The function that should be implemented."""
        self._board = Board(board_string)
        _command = self.find_answer()
        print("Sending command {}".format(_command))
        return _command

    def find_answer(self):
        """ This is an example of the solver subroutine."""
        if self._board.is_bankrupt():
            print("Bankrupt. Sending reset command...")
            return "message('go reset')"
        if self._board.is_gameover():
            print("Game Over. Sending reset command...")
            return "message('go reset')"

        # print(self._board.day())
        # print(self._board.is_bankrupt())
        # print(self._board.assets())
        # print(self._board.lemonade_cost())
        # print(self._board.weather_forecast())
        # print(self._board.history())
        # print(self._board.messages())
        print(self._board.to_string())

        # TODO: Implement your logic here
        _glassesToMake = 2
        _signsToMake = 0
        _lemonadePriceCents = 15

        _answer = "message('go {},{},{}')".format(_glassesToMake, _signsToMake, _lemonadePriceCents)
        return _answer


if __name__ == '__main__':
    raise RuntimeError("This module is not intended to be ran from CLI")
