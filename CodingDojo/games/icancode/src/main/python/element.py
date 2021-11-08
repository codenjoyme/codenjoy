#! /usr/bin/env python3

###
# #%L
# Codenjoy - it's a dojo-like platform from developers to developers.
# %%
# Copyright (C) 2018 - 2021 Codenjoy
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
    # empty space where player can go
    EMPTY=("LAYER2", "-"),
    FLOOR=("LAYER1", "."),

    # walls
    ANGLE_IN_LEFT=("LAYER1", b"\xe2\x95\x94".decode()),  # encoded "╔" char
    WALL_FRONT=("LAYER1", b"\xe2\x95\x90".decode()),  # encoded "═" char
    ANGLE_IN_RIGHT=("LAYER1", b"\xe2\x94\x90".decode()),  # encoded "┐" char
    WALL_RIGHT=("LAYER1", b"\xe2\x94\x82".decode()),  # encoded "│" char
    ANGLE_BACK_RIGHT=("LAYER1", b"\xe2\x94\x98".decode()),  # encoded "┘" char
    WALL_BACK=("LAYER1", b"\xe2\x94\x80".decode()),  # encoded "─" char
    ANGLE_BACK_LEFT=("LAYER1", b"\xe2\x94\x94".decode()),  # encoded "└" char
    WALL_LEFT=("LAYER1", b"\xe2\x95\x91".decode()),  # encoded "║" char
    WALL_BACK_ANGLE_LEFT=("LAYER1", b"\xe2\x94\x8c".decode()),  # encoded "┌" char
    WALL_BACK_ANGLE_RIGHT=("LAYER1", b"\xe2\x95\x97".decode()),  # encoded "╗" char
    ANGLE_OUT_RIGHT=("LAYER1", b"\xe2\x95\x9d".decode()),  # encoded "╝" char
    ANGLE_OUT_LEFT=("LAYER1", b"\xe2\x95\x9a".decode()),  # encoded "╚" char
    SPACE=("LAYER1", " "),

    # laser machine
    LASER_MACHINE_CHARGING_LEFT=("LAYER1", b"\xcb\x82".decode()),  # encoded "˂" char
    LASER_MACHINE_CHARGING_RIGHT=("LAYER1", b"\xcb\x83".decode()),  # encoded "˃" char
    LASER_MACHINE_CHARGING_UP=("LAYER1", b"\xcb\x84".decode()),  # encoded "˄" char
    LASER_MACHINE_CHARGING_DOWN=("LAYER1", b"\xcb\x85".decode()),  # encoded "˅" char

    # lase machine ready
    LASER_MACHINE_READY_LEFT=("LAYER1", b"\xe2\x97\x84".decode()),  # encoded "◄" char
    LASER_MACHINE_READY_RIGHT=("LAYER1", b"\xe2\x96\xba".decode()),  # encoded "►" char
    LASER_MACHINE_READY_UP=("LAYER1", b"\xe2\x96\xb2".decode()),  # encoded "▲" char
    LASER_MACHINE_READY_DOWN=("LAYER1", b"\xe2\x96\xbc".decode()),  # encoded "▼" char

    # other stuff
    START=("LAYER1", "S"),
    EXIT=("LAYER1", "E"),
    HOLE=("LAYER1", "O"),
    BOX=("LAYER2", "B"),
    ZOMBIE_START=("LAYER1", "Z"),
    GOLD=("LAYER1", "$"),

    # your robot
    ROBO=("LAYER2", b"\xe2\x98\xba".decode()),  # encoded "☺" char
    ROBO_FALLING=("LAYER2", "o"),
    ROBO_FLYING=("LAYER3", "*"),
    ROBO_LASER=("LAYER2", b"\xe2\x98\xbb".decode()),  # encoded "☻" char

    # other robot
    ROBO_OTHER=("LAYER2", "X"),
    ROBO_OTHER_FALLING=("LAYER2", "x"),
    ROBO_OTHER_FLYING=("LAYER3", "^"),
    ROBO_OTHER_LASER=("LAYER2", "&"),

    # laser
    LASER_LEFT=("LAYER2", b"\xe2\x86\x90".decode()),  # encoded "←" char
    LASER_RIGHT=("LAYER2", b"\xe2\x86\x92".decode()),  # encoded "→" char
    LASER_UP=("LAYER2", b"\xe2\x86\x91".decode()),  # encoded "↑" char
    LASER_DOWN=("LAYER2", b"\xe2\x86\x93".decode()),  # encoded "↓" char

    # zombie
    FEMALE_ZOMBIE=("LAYER2", b"\xe2\x99\x80".decode()),  # encoded "♀" char
    MALE_ZOMBIE=("LAYER2", b"\xe2\x99\x82".decode()),  # encoded "♂" char
    ZOMBIE_DIE=("LAYER2", b"\xe2\x9c\x9d".decode()),  # encoded "✝" char

    # perks
    UNSTOPPABLE_LASER_PERK=("LAYER1", "l"),
    DEATH_RAY_PERK=("LAYER1", "r"),
    UNLIMITED_FIRE_PERK=("LAYER1", "f"),
    FIRE_PERK=("LAYER1", "a"),
    JUMP_PERK=("LAYER1", "j"),
    MOVE_BOXES_PERK=("LAYER1", "m"),

    # system elements, don"t touch it
    FOG=("LAYER1", "F"),
    BACKGROUND=("LAYER2", "G")
)


def layer(name):
    _elements = set()
    for key, value in _ELEMENTS.items():
        if value[0] == name:
            _elements.update(Element(key))
    return list(_elements)


def layer1():
    return layer("LAYER1")


def layer2():
    return layer("LAYER2")


def layer3():
    return layer("LAYER3")


class Element:

    def __init__(self, name):
        for key, value in _ELEMENTS.items():
            if name == key or name == value[1]:
                self._name = key
                self._layer = value[0]
                self._char = value[1]
                break
        else:
            raise ArgumentError("No such Element: {}".format(name))

    def get_name(self):
        return self._name

    def get_layer(self):
        return self._layer

    def get_char(self):
        return self._char

    def __eq__(self, other):
        return (self._name == other._name and
                self._layer == other._layer and
                self._char == other._char)


if __name__ == '__main__':
    raise RuntimeError("This module is not intended to be ran from CLI")
