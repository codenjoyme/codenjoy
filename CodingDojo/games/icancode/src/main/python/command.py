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


_COMMANDS = dict(
    DIE="ACT(0)",

    LEFT="LEFT",
    RIGHT="RIGHT",
    UP="UP",
    DOWN="DOWN",

    JUMP="ACT(1)",
    JUMP_LEFT="ACT(1),LEFT",
    JUMP_RIGHT="ACT(1),RIGHT",
    JUMP_UP="ACT(1),UP",
    JUMP_DOWN="ACT(1),DOWN",

    PULL_LEFT="ACT(2),LEFT",
    PULL_RIGHT="ACT(2),RIGHT",
    PULL_UP="ACT(2),UP",
    PULL_DOWN="ACT(2),DOWN",

    FIRE_LEFT="ACT(3),LEFT",
    FIRE_RIGHT="ACT(3),RIGHT",
    FIRE_UP="ACT(3),UP",
    FIRE_DOWN="ACT(3),DOWN",

    NULL=""
)


class Command:

    def __init__(self, command):
        for key, value in _COMMANDS.items():
            if command == key or command == value:
                self._name = key
                self._command = value
                break
        else:
            raise ValueError("No Such Command: {}".format(command))

    def get_name(self):
        return self._name

    def get_command(self):
        return self._command

    def inverted(self):
        _inv_dir = None
        if self._name == 'LEFT':
            _inv_dir = Command
            "RIGHT"
        elif self._name == 'RIGHT':
            _inv_dir = Command
            "LEFT"
        elif self._name == 'UP':
            _inv_dir = Command
            "DOWN"
        elif self._name == 'DOWN':
            _inv_dir = Command
            "UP"
        else:
            _inv_dir = Command
            "NULL"
        return _inv_dir

    def to_string(self):
        return self._command

    def __eq__(self, other):
        return self._name == other._name and self._command == other._command


if __name__ == '__main__':
    raise RuntimeError("This module is not designed to be ran from CLI")
