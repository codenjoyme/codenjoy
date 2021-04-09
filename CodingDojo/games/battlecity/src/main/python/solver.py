#!/usr/bin/env python3


from board import Board
from command import Command
# from element import Element
from random import choice
import collections

history=collections.deque([],maxlen=20)
_COURSES = ['LEFT','RIGHT','DOWN','UP']

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
        _command = "ACT," + str(self.find_command())
        # _command = str(self.find_command())
        print("Sending Command {}".format(_command))
        return ''.join(_command)

    def find_command(self):

        course = list(_COURSES)
        _direction = choice(course)
        print(self._board.to_string())
        all_tanks = self._board.tanks

        # tank in trees
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
        _direction = Command(_direction)
        _x, _y = _direction.new_x(x), _direction.new_y(y)
        lockup = self._board.is_near_all(_x, _y, self._board.barriers)
        while self._board.is_barrier_at(_x, _y) or lockup >= 3:
            if len(course)<3:
                course = list(_COURSES)
            course.remove(str(_direction))
            _direction = Command(choice(course))
            _x, _y = _direction.new_x(x), _direction.new_y(y)
            lockup = self._board.is_near_all(_x, _y, self._board.barriers)
        return _direction







if __name__ == '__main__':
    raise RuntimeError("This module is not intended to be ran from CLI")














