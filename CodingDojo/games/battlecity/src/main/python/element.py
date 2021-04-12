#! /usr/bin/env python3


from argparse import ArgumentError


_ELEMENTS = dict(

    WALL='╬',
    WALL_DESTROYED_DOWN='╩',
    WALL_DESTROYED_UP='╦',
    WALL_DESTROYED_LEFT='╠',
    WALL_DESTROYED_RIGHT='╣',
    WALL_DESTROYED_DOWN_TWICE='╨',
    WALL_DESTROYED_UP_TWICE='╥',
    WALL_DESTROYED_LEFT_TWICE='╞',
    WALL_DESTROYED_RIGHT_TWICE='╡',
    WALL_DESTROYED_LEFT_RIGHT='│',
    WALL_DESTROYED_UP_DOWN='─',
    WALL_DESTROYED_UP_LEFT='┌',
    WALL_DESTROYED_RIGHT_UP='┐',
    WALL_DESTROYED_DOWN_LEFT='└',
    WALL_DESTROYED_DOWN_RIGHT='┘',

    AI_TANK_UP='?',
    AI_TANK_RIGHT='»',
    AI_TANK_DOWN='¿',
    AI_TANK_LEFT='«',
    AI_TANK_PRIZE='◘',

    OTHER_TANK_UP='˄',
    OTHER_TANK_RIGHT='˃',
    OTHER_TANK_DOWN='˅',
    OTHER_TANK_LEFT='˂',

    TANK_UP='▲',
    TANK_RIGHT='►',
    TANK_DOWN='▼',
    TANK_LEFT='◄',

    BULLET='•',
    BATTLE_WALL='☼',
    BANG='Ѡ',
    ICE='#',
    TREE='%',
    RIVER='~',

    SPASE=' '
)


class ArgumentError(Exception):
    def __init__(self, message):
            super().__init__(message)


def value_of(char):
    """ Test whether the char is valid Element and return it's name."""
    for value, c in _ELEMENTS.items():
        if char == c:
            return value
    else:
        raise ArgumentError("No such Element: {}".format(char))


class Element:

    def __init__(self, name):
        for key, value in _ELEMENTS.items():
            if name == key or name == value:
                self._name = key
                self._char = value
                break
        else:
            raise ArgumentError("No such Element: {}".format(name))

    @property
    def name(self):
        return self._name

    @property
    def char(self):
        return self._char

    def __eq__(self, other):
        return (self._name == other._name and
                self._char == other._char)


if __name__ == '__main__':
    raise RuntimeError("This module is not intended to be ran from CLI")
