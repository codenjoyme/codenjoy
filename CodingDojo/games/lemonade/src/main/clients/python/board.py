#! /usr/bin/env python3

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

import json


class Board:
    """ Class describes the Board for lemonade game."""

    def __init__(self, board_string):
        self._string = board_string
        self._json = json.loads(board_string)

    def messages(self):
        return self._json["messages"]

    def is_bankrupt(self):
        return self._json["isBankrupt"]

    def is_gameover(self):
        return self._json["isGameOver"]

    def day(self):
        return self._json["day"]

    def assets(self):
        return self._json["assets"]

    def lemonade_cost(self):
        return self._json["lemonadeCost"]

    def weather_forecast(self):
        return self._json["weatherForecast"]

    def history(self):
        return self._json["history"]

    def to_string(self):
        return self._json


if __name__ == '__main__':
    raise RuntimeError("This module is not designed to be ran from CLI")
