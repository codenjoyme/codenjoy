#! /usr/bin/env python3


# from argparse import ArgumentError

# _ELEMENTS = dict(
#     # empty space where player can go
#     NONE=" ",
#
#
#     # walls
#
#     WALL =b'\xe2\x95\xac'.decode(),                 #('╬', 3),
#
#     WALL_DESTROYED_DOWN=b'\xe2\x95\xa9'.decode(),   #('╩', 2),
#     WALL_DESTROYED_UP=b'\xe2\x95\xa6'.decode(),     #('╦', 2),
#     WALL_DESTROYED_LEFT=b'\xe2\x95\xa0'.decode(),   #('╠', 2),
#     WALL_DESTROYED_RIGHT=b'\xe2\x95\xa3'.decode(),  #('╣', 2),
#
#
#     WALL_DESTROYED_DOWN_TWICE=b'\xe2\x95\xa8'.decode(),   #('╨', 1),
#     WALL_DESTROYED_UP_TWICE=b'\xe2\x95\xa5'.decode(),   #('╥', 1),
#     WALL_DESTROYED_LEFT_TWICE=b'\xe2\x95\x9e'.decode(),   #('╞', 1),
#     WALL_DESTROYED_RIGHT_TWICE=b'\xe2\x95\xa1'.decode(),   #('╡', 1),
#
#     WALL_DESTROYED_LEFT_RIGHT=b'\xe2\x94\x82'.decode(),   #('│', 1),
#     WALL_DESTROYED_UP_DOWN=b'\xe2\x94\x80'.decode(),   #('─', 1),
#
#     WALL_DESTROYED_UP_LEFT=b'\xe2\x94\x8c'.decode(),   #('┌', 1),
#     WALL_DESTROYED_RIGHT_UP=b'\xe2\x94\x90'.decode(),   #('┐', 1),
#     WALL_DESTROYED_DOWN_LEFT=b'\xe2\x94\x94'.decode(),   #('└', 1),
#     WALL_DESTROYED_DOWN_RIGHT=b'\xe2\x94\x98'.decode(),   #('┘', 1),
#
#     # WALL_DESTROYED=' ',   #(' ', 0),
#
#     #  BULLET
#     # BULLET=b"\xe2\x80\xa2" ,#('•'),
#     BULLET='•',
#
#     # OTHER_TANK
#     OTHER_TANK_UP=b"\xcb\x84".decode() ,      #('˄'),
#     OTHER_TANK_RIGHT=b"\xcb\x82".decode() ,   #('˃'),
#     OTHER_TANK_DOWN=b"\xcb\x85".decode() ,    #('˅'),
#     OTHER_TANK_LEFT=b"\xcb\x82".decode() ,    #('˂'),
#
#     # TANK
#     TANK_UP=b"\xe2\x96\xb2".decode()  ,    #('▲'),
#     TANK_RIGHT=b"\xe2\x96\xba".decode()  , #('►'),
#     TANK_DOWN=b"\xe2\x96\xbc".decode()  ,  #('▼'),
#     TANK_LEFT=b"\xe2\x97\x84".decode()  ,  #('◄'),
#
#     # AI_TANK
#     AI_TANK_UP="?" ,#('?'),
#     AI_TANK_RIGHT=b"\xc2\xbb".decode() ,#('»'),
#     AI_TANK_DOWN=b"\xc2\xbf".decode() ,#('¿'),
#     AI_TANK_LEFT=b"\xc2\xab".decode() ,#('«'),
#
#     # other
#     PRIZE="!",           #('!'),
#     PRIZE_IMMORTALITY="1" ,#('1'),
#     PRIZE_BREAKING_WALLS="2" ,#('2'),
#     PRIZE_WALKING_ON_WATER="3" ,#('3'),
#     PRIZE_VISIBILITY="4" ,#('4'),
#     PRIZE_NO_SLIDING="5" ,#('5');
#     AI_TANK_PRIZE=b"\xe2\x97\x98" ,#('◘'),
#
#     # system elements,
#     BATTLE_WALL=b'\xe2\x98\xbc'.decode(),  # encoded '☼' char
#     BANG='Ѡ',
#
#     ICE='#',
#     TREE='%',
#     RIVER=b'\x7e',  #  ~
#
#
# )

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
    WALL_DESTROYED_UP_RIGHT='┐',
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
