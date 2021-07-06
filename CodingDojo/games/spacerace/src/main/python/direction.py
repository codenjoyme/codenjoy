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
from enum import Enum
from point import Point

class Direction(Enum):
    LEFT    = (0, -1,  0, False)
    RIGHT   = (1,  1,  0, False)
    UP      = (2,  0, -1, False)
    DOWN    = (3,  0,  1, False)
    ACT     = (4,  0,  0, True)
    SUICUDE = (5,  0,  0, True)
    STOP    = (6,  0,  0, False)

    def __init__(self, id: int, dx: int, dy: int, isAct: bool):
        self.id = id
        self.dx = dx
        self.dy = dy
        self.isAct = isAct 
    
    def is_act(self):
        return self.isAct
 
    def to_string(self):
        if self == Direction.SUICUDE: return "ACT(0)"
        if self == Direction.ACT: return self.name
        return self.name +str(", ACT" if self.isAct else "") 

    def get_dx(self):
        return self.dx
    
    def get_dy(self):
        return self.dy

    def change_x(self, x: int):
        return x + self.dx

    def change_y(self, y: int):
        return y + self.dy
 
    def change(self, point: Point):
        return Point(point.x + self.dx, point.y + self.dy) 

    def inverted(self):
        return __REVERSE_DIRECTIONS__[self]

    def withAct(self):
        return Command(self, True)

__REVERSE_DIRECTIONS__ = {
   Direction.LEFT   : Direction.RIGHT,
   Direction.RIGHT  : Direction.LEFT,
   Direction.UP     : Direction.DOWN,
   Direction.DOWN   : Direction.UP,
   Direction.ACT    : Direction.ACT,
   Direction.SUICUDE: Direction.SUICUDE,
   Direction.STOP   : Direction.STOP
}

class Command:
    def __init__(self, direction: Direction, isAct: bool):
        self.direction = direction
        self.isAct = isAct
    
    @property
    def id(self):
        return self.direction.id

    def is_act(self):
        return self.isAct

    def __eq__(self, other):
        if isinstance(other, Command):
            return self.direction == other.direction and self.isAct == other.isAct
   
    def to_string(self):
        if self.direction in [Direction.SUICUDE, Direction.ACT]: return self.direction.to_string()
        return self.direction.to_string() + str((", " + Direction.ACT.to_string()) if self.isAct else "") 

    def get_dx(self):
        return self.direction.dx
    
    def get_dy(self):
        return self.direction.dy

    def change_x(self, x: int):
        return self.direction.change_x(x)

    def change_y(self, y: int):
        return self.direction.change_y(y)
        
    def change(self, point: Point):
        return self.direction.change(point)

    def inverted(self):
        dir = self.direction.inverted()
        return Command(dir, self.isAct)

    def withAct(self):
        if self.isAct: return self
        return Command(self.direction, True)

if __name__ == '__main__':
    raise RuntimeError("This module is not designed to be ran from CLI")


