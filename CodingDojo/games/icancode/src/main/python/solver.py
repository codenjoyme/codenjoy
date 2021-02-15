#!/usr/bin/env python3


from board import Board
from command import Command

""" This class should contain the movement generation algorithm."""
class DirectionSolver:

    def __init__(self):
        self._board = None

    def get(self, board_string):
        self._board = Board(board_string)
        return self.next_command()

    """ Implement your logic here """
    def next_command(self):
        _command = Command('RIGHT').to_string()
        print(self._board.to_string())
        print("Sending Command: {}\n".format(_command))
        return _command


if __name__ == '__main__':
    raise RuntimeError("This module is not intended to be ran from CLI")
