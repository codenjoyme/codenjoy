#!/usr/bin/env python3

_DIRECTIONS = dict(
    LEFT = (0, -1,  0),
    RIGHT = (1,  1,  0),
    UP = (2,  0, -1),
    DOWN = (3,  0,  1),
    ACT = (4,  0,  0),
    STOP = (5,  0,  0),
    NULL = (-1,  0,  0)
)

class Direction:

    def __init__(self, direction):
        """ Initializate direction from string or tuple."""
        for name, value in _DIRECTIONS.items():
            if direction == name or direction == value:
                self._name = name
                self._dir = _DIRECTIONS[name]
                break
        else:
            raise ValueError("No Such Direction: {}".format(direction))
        
    def __eq__(self, other):
        return self._name == other._name and self._dir == other._dir

    def __ne__(self, other):
        return self._name == other._name and self._dir != other._dir

    def is_null(self):
        return self._name == 'NULL'

    def to_string(self):
        return self._name

    def get_x(self):
        return self._dir[1]
    
    def get_y(self):
        return self._dir[2]

    def change_x(self, x):
        return x + self._dir[1]

    def change_y(self, y):
        return y + self._dir[2]
        
    def inverted(self):
        _inv_dir = None
        if self._name == 'LEFT':
            _inv_dir = Direction('RIGHT')
        elif self._name == 'RIGHT':
            _inv_dir = Direction('LEFT')
        elif self._name == 'UP':
            _inv_dir = Direction('DOWN')
        elif self._name == 'DOWN':
            _inv_dir = Direction('UP')
        else:
            _inv_dir = Direction('STOP')
        return _inv_dir


if __name__ == '__main__':
    raise RuntimeError("This module is not designed to be ran from CLI")
