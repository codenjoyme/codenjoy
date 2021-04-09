#!/usr/bin/env python3


_COMMANDS = dict(
    DIE="ACT",

    LEFT="LEFT",
    RIGHT="RIGHT",
    UP="UP",
    DOWN="DOWN",


    FIRE_LEFT="ACT,LEFT",
    FIRE_RIGHT="ACT,RIGHT",
    FIRE_UP="ACT,UP",
    FIRE_DOWN="ACT,DOWN",


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
