#! /usr/bin/env python3

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
from enum import Enum

class Elements(Enum):
    NONE        = (0, ' ')
    BULLET_PACK = (1, '7')
    HERO        = (2, '☺')
    OTHER_HERO  = (3, '☻')
    DEAD_HERO   = (4, '+')
    STONE       = (5, '0')
    BOMB        = (6, '♣')
    EXPLOSION   = (7, 'x')
    BULLET      = (8, '*')
    GOLD        = (9, '$')
    WALL        = (10,'☼')

    def __init__(self, id: int, char: str):
        self.id = id
        self.char = char

    def __hash__(self):
        return hash(self.char)
        
    def getByChar(char):
        return __Elements__[char]


__Elements__ = dict(map(lambda element : (element.char, element), Elements))

if __name__ == '__main__':
    raise RuntimeError("This module is not designed to be ran from CLI")