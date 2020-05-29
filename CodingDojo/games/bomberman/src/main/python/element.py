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

from argparse import ArgumentError

_ELEMENTS = dict(
    # The Bomberman
    BOMBERMAN = b'\xe2\x98\xba'.decode(),  # encoded '☺' char
    BOMB_BOMBERMAN = b'\xe2\x98\xbb'.decode(),  # encoded '☻' char
    DEAD_BOMBERMAN = b'\xd1\xa0'.decode(),  # encoded 'Ѡ' char
    # The Enemies
    OTHER_BOMBERMAN = b'\xe2\x99\xa5'.decode(),  # encoded '♥' char
    OTHER_BOMB_BOMBERMAN = b'\xe2\x99\xa0'.decode(),  # encoded '♠' char
    OTHER_DEAD_BOMBERMAN = b'\xe2\x99\xa3'.decode(),  # encoded '♣' char
    # The Bombs
    BOMB_TIMER_5 = '5',
    BOMB_TIMER_4 = '4',
    BOMB_TIMER_3 = '3',
    BOMB_TIMER_2 = '2',
    BOMB_TIMER_1 = '1',
    BOOM = b'\xd2\x89'.decode(),  # encoded '҉character
    # Walls
    WALL = b'\xe2\x98\xbc'.decode(), # encoded '☼' char
    DESTROY_WALL = '#',
    DESTROYED_WALL = 'H',
    # Meatchoopers
    MEAT_CHOPPER = '&',
    DEAD_MEAT_CHOPPER = 'x',
    #Perks
    BOMB_BLAST_RADIUS_INCREASE = '+',
    BOMB_COUNT_INCREASE = 'c',
    BOMB_IMMUNE = 'i',
    BOMB_REMOTE_CONTROL = 'r',
    # Space
    NONE = ' '
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
