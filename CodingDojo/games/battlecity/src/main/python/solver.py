#!/usr/bin/env python3


from board import Board
from command import Command
# from element import Element
from random import choice
import collections

history=collections.deque([],maxlen=20)
_COURSES = ('LEFT','RIGHT','DOWN','UP')

""" This class should contain the movement generation algorithm."""



class DirectionSolver:
    """ This class should contain the movement generation algorythm."""

    def __init__(self):
        self._direction = None
        self._board = None
        self._last = None
        self._count = 0

    def get(self, board_string):
        """ The function that should be implemented."""
        self._board = Board(board_string)
        _command = str(self.find_command())
        print("Sending Command {}".format(_command))
        # return ''.join(_command)
        return ''.join(_command)

    def find_command(self):

        course = list(_COURSES)
        _direction = choice(course)
        print(self._board.to_string())
        all_tanks = self._board.tanks
        if not self._board.my_tank:
            __dir = choice((course))
        else:
            tank = self._board.my_tank[0]
            tank_x, tank_y = tank.x, tank.y
            my_direction = self._board.direction(tank_x, tank_y)
            _direction = self.next_step(tank_x, tank_y,my_direction)


        print("Sending Command: {}\n".format( _direction))

        return _direction

    def next_step(self,x,y,_direction):
        course = list(_COURSES)
        ''''find new coordinates for the direction'''

        _direction = Command(_direction)
        _x, _y = _direction.new_x(x), _direction.new_y(y)

        """analyzing a new course and choice of old or new course"""

        return _direction







if __name__ == '__main__':
    raise RuntimeError("This module is not intended to be ran from CLI")














