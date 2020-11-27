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
    BOMBERMAN = b'\xe2\x98\xba'.decode(),               # '☺' - this is what he usually looks like
    BOMB_BOMBERMAN = b'\xe2\x98\xbb'.decode(),          # '☻' - this is if he is sitting on own bomb
    DEAD_BOMBERMAN = b'\xd1\xa0'.decode(),              # 'Ѡ' - oops your Bomberman is dead (don`t worry he will appear somewhere in next move)
                                                        #       you`re getting -200 for each death
    # The Enemies
    OTHER_BOMBERMAN = b'\xe2\x99\xa5'.decode(),         # '♥' - this is what other Bombermans looks like
    OTHER_BOMB_BOMBERMAN = b'\xe2\x99\xa0'.decode(),    # '♠' - this is if player just set the bomb
    OTHER_DEAD_BOMBERMAN = b'\xe2\x99\xa3'.decode(),    # '♣' - enemy corpse (it will disappear shortly right on the next move)
                                                        #       if you`ve done it you`ll get +1000
    # The Bombs
    BOMB_TIMER_5 = '5',                                 # '5' - after bomberman set the bomb the timer starts (5 tacts)
    BOMB_TIMER_4 = '4',                                 # '4' - this will blow up after 4 tacts
    BOMB_TIMER_3 = '3',                                 # '3' - this after 3
    BOMB_TIMER_2 = '2',                                 # '2' - two
    BOMB_TIMER_1 = '1',                                 # '1' - one
    BOOM = b'\xd2\x89'.decode(),                        # '҉' - Boom! this is what is bomb does everything that is destroyable got destroyed

    # Walls
    WALL = b'\xe2\x98\xbc'.decode(),                    # '☼' - indestructible wall - it will not fall from bomb
    DESTROY_WALL = '#',                                 # '#' - this wall could be blowed up
    DESTROYED_WALL = 'H',                               # 'H' - this is how broken wall looks like it will dissapear on next move
                                                        #       if it`s you did it - you`ll get +10 points.

    # Meatchoopers
    MEAT_CHOPPER = '&',                                 # '&' - this guys runs over the board randomly and gets in the way all the time
                                                        #       if it will touch bomberman - it will die
    DEAD_MEAT_CHOPPER = 'x',                            # 'x' - you`d better kill this piece of ... meat you`ll get +100 point for it
                                                        #       this is chopper corpse

    # Perks
    BOMB_BLAST_RADIUS_INCREASE = '+',                   # '+' - Bomb blast radius increase. Applicable only to new bombs. The perk is temporary.
    BOMB_COUNT_INCREASE = 'c',                          # 'c' - Increase available bombs count. Number of extra bombs can be set in settings. Temporary.
    BOMB_IMMUNE = 'i',                                  # 'i' - Bomb blast not by timer but by second act. Number of RC triggers is limited and can be set in settings.
    BOMB_REMOTE_CONTROL = 'r',                          # 'r' - Do not die after bomb blast (own bombs and others as well). Temporary.

    # Space
    NONE = ' '                                          # ' ' - this is the only place where you can move your Bomberman
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
