#! /usr/bin/env python3

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
