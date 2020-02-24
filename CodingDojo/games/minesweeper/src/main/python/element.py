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

    NONE  = ' ',
    BORDER  = '☼',
	HERO = '☺',
    HIDDEN = '*',
	MINE_1 = '1',
	MINE_2 = '2',
	MINE_3 = '3',
	MINE_4 = '4',
	MINE_5 = '5',
	MINE_6 = '6',
	MINE_7 = '7',
	MINE_8 = '8',
	FLAG = '‼',
	DEAD_BODY = 'Ѡ',
	HERE_WAS_BOMB = '☻',
	DESTROYED_BOMB = 'x'
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
