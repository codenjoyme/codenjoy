#! /usr/bin/env python3

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


_ELEMENTS = dict(
    # if any encoding problem please use method
    # CHAR_NAME = b'\xe2\x99\xa5'.decode(),

    # a void
    NONE  = ' ',

    # walls
    BRICK  = '#',
    PIT_FILL_1  = '1',
    PIT_FILL_2  = '2',
    PIT_FILL_3  = '3',
    PIT_FILL_4  = '4',
    UNDESTROYABLE_WALL  = '☼',

    DRILL_PIT  = '*',

    // this is enemy
    ENEMY_LADDER  = 'Q',
    ENEMY_LEFT  = '«',
    ENEMY_RIGHT  = '»',
    ENEMY_PIPE_LEFT  = '<',
    ENEMY_PIPE_RIGHT  = '>',
    ENEMY_PIT  = 'X',

    # gold ;)
    GOLD  = '$',

    # This is your loderunner
    HERO_DIE  = 'Ѡ',
    HERO_DRILL_LEFT  = 'Я',
    HERO_DRILL_RIGHT  = 'R',
    HERO_LADDER  = 'Y',
    HERO_LEFT  = '◄',
    HERO_RIGHT  = '►',
    HERO_FALL_LEFT  = ']',
    HERO_FALL_RIGHT  = '[',
    HERO_PIPE_LEFT  = '{',
    HERO_PIPE_RIGHT  = '}',

    # this is other players
    OTHER_HERO_DIE  = 'Z',
    OTHER_HERO_LEFT  = ')',
    OTHER_HERO_RIGHT  = '  = ',
    OTHER_HERO_LADDER  = 'U',
    OTHER_HERO_PIPE_LEFT  = 'Э',
    OTHER_HERO_PIPE_RIGHT  = 'Є',

    # ladder and pipe - you can walk
    LADDER  = 'H',
    PIPE  = '~'

)


def value_of(char):
    """ Test whether the char is valid Element and return it's name."""
    for value, c in _ELEMENTS.items():
        if char == c:
            return value
    else:
        raise ArgumentError("No such Element: {}".format(char))


class Element:
    """ Class describes the Element objects for Bomberman game."""
    def __init__(self, n_or_c):
        """ Construct an Element object from given name or char."""
        for n, c in _ELEMENTS.items():
            if n_or_c == n or n_or_c == c:
                self._name = n
                self._char = c
                break
        else:
            raise ArgumentError("No such Element: {}".format(n_or_c))
            
    def get_char(self):
        """ Return the Element's character."""
        return self._char
    
    def __eq__(self, otherElement):
        return (self._name == otherElement._name and
                self._char == otherElement._char)

if __name__ == '__main__':
    raise RuntimeError("This module is not intended to be ran from CLI")
