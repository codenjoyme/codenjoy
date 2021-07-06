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


def filterMap(map, filterFunction):
    result = {}
    for k, v in map.items():
        if filterFunction(k,v):
            result[k] = v
    return result

class Solver:
    """ This class should contain the movement generation algorithm."""

    def __init__(self, logger):
        ''' initialize globals here '''
        self.logger = logger
        
    def get(self, board: Board):
        """ The method that should be implemented."""

        # board examples
        self.logger.log("Board size: " + str(board.size))
        allExtend = board.get_all_extend()
        otherHerroes = filterMap(allExtend, lambda k, v: v == Elements.OTHER_HERO)
        otherHeroesString = ", ".join(map(lambda p: "[x: {}, y: {}]".format(p.x, p.x), otherHerroes.keys()))
        self.logger.log("Other heroes on coordinates: ", otherHeroesString)
        bombsAndStones = board.find_all(Elements.BOMB, Elements.STONE)
        self.logger.log("Bombs and stones number: " + str(len(bombsAndStones)))
        me = board.find_all(Elements.HERO)
        if len(me) > 0:
            me=me[0]
            self.logger.log("Me on coordinates: [x: {}, y: {} ]".format(me.x, me.y))
            meIfMoveLeft = Direction.LEFT.change(me) # point left to the hero

        # direction examples
        movements = [Direction.LEFT, Direction.RIGHT, Direction.DOWN, Direction.UP]
        action = choice(movements)
        if choice([True, False]):
            action = action.withAct() 
        return action;                                                        

if __name__ == '__main__':
    raise RuntimeError("This module is not intended to be ran from CLI")
