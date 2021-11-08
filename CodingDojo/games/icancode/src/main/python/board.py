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


from math import sqrt
from element import Element
from point import Point
import re


class Board:
    COUNT_LAYERS = 3
    INPUT_REGEX = "(.*),\"layers\":\[(.*)\](.*)"

    def __init__(self, input):
        matcher = re.search(Board.INPUT_REGEX, input)
        board_string = matcher.group(2).replace('\n', '').replace(',', '').replace('\"', '')  # one line board

        self._board = []
        _layer_len = int(len(board_string) / Board.COUNT_LAYERS)
        for i in range(Board.COUNT_LAYERS):
            _layer = []
            for j in range(_layer_len):
                _layer.append(board_string[j + (i * _layer_len)])
            self._board.append(_layer)
        self._layer_size = int(sqrt(_layer_len))

    def _find_all(self, element):
        _points = []
        _a_char = element.get_char()
        for i in range(len(self._board)):
            for j in range(len(self._board[i])):
                if self._board[i][j] == _a_char:
                    _points.append(self._strpos2pt(j))
        return _points

    def _strpos2pt(self, strpos):
        return Point(*self._strpos2xy(strpos))

    def _strpos2xy(self, strpos):
        return strpos % self._layer_size, strpos // self._layer_size

    def get_at(self, x, y):
        _strpos = self._xy2strpos(x, y)
        _elements = []
        for i in range(len(self._board)):
            _elements.append(Element(self._board[i][_strpos]))
        return _elements

    def _xy2strpos(self, x, y):
        return self._layer_size * y + x

    def is_at(self, x, y, element_object):
        return element_object in self.get_at(x, y)

    def is_barrier_at(self, x, y):
        points = set()
        points.update(self.get_floors())
        points.update(self.get_starts())
        points.update(self.get_exits())
        points.update(self.get_golds())
        points.update(self.get_holes())
        points.update(self.get_lasers())
        points.add(self.get_hero())
        points.update(self.get_other_heroes())
        return Point(x, y) not in list(points)

    def get_hero(self):
        points = set()
        points.update(self._find_all(Element('ROBO_FALLING')))
        points.update(self._find_all(Element('ROBO_LASER')))
        points.update(self._find_all(Element('ROBO')))
        points.update(self._find_all(Element('ROBO_FLYING')))
        assert len(points) <= 1, "There should be only one robo"
        return list(points)[0]

    def is_me_alive(self):
        points = set()
        points.update(self._find_all(Element('ROBO_FALLING')))
        points.update(self._find_all(Element('ROBO_LASER')))
        return list(points) == 0

    def get_other_heroes(self):
        points = set()
        points.update(self._find_all(Element('ROBO_OTHER_FALLING')))
        points.update(self._find_all(Element('ROBO_OTHER_LASER')))
        points.update(self._find_all(Element('ROBO_OTHER')))
        points.update(self._find_all(Element('ROBO_OTHER_FLYING')))
        return list(points)

    def get_empty(self):
        return self._find_all(Element('EMPTY'))

    def get_zombies(self):
        points = set()
        points.update(self._find_all(Element('FEMALE_ZOMBIE')))
        points.update(self._find_all(Element('MALE_ZOMBIE')))
        points.update(self._find_all(Element('ZOMBIE_DIE')))
        return list(points)

    def get_laser_machines(self):
        points = set()
        points.update(self._find_all(Element('LASER_MACHINE_CHARGING_LEFT')))
        points.update(self._find_all(Element('LASER_MACHINE_CHARGING_RIGHT')))
        points.update(self._find_all(Element('LASER_MACHINE_CHARGING_UP')))
        points.update(self._find_all(Element('LASER_MACHINE_CHARGING_DOWN')))
        points.update(self._find_all(Element('LASER_MACHINE_READY_LEFT')))
        points.update(self._find_all(Element('LASER_MACHINE_READY_RIGHT')))
        points.update(self._find_all(Element('LASER_MACHINE_READY_UP')))
        points.update(self._find_all(Element('LASER_MACHINE_READY_DOWN')))
        return list(points)

    def get_lasers(self):
        points = set()
        points.update(self._find_all(Element('LASER_LEFT')))
        points.update(self._find_all(Element('LASER_RIGHT')))
        points.update(self._find_all(Element('LASER_UP')))
        points.update(self._find_all(Element('LASER_DOWN')))
        return list(points)

    def get_boxes(self):
        return self._find_all(Element('BOX'))

    def get_floors(self):
        return self._find_all(Element('FLOOR'))

    def get_holes(self):
        points = set()
        points.update(self._find_all(Element('HOLE')))
        points.update(self._find_all(Element('ROBO_FALLING')))
        points.update(self._find_all(Element('ROBO_OTHER_FALLING')))
        return list(points)

    def get_exits(self):
        return self._find_all(Element('EXIT'))

    def get_starts(self):
        return self._find_all(Element('START'))

    def get_golds(self):
        return self._find_all(Element('GOLD'))

    def get_walls(self):
        points = set()
        points.update(self._find_all(Element('ANGLE_IN_LEFT')))
        points.update(self._find_all(Element('WALL_FRONT')))
        points.update(self._find_all(Element('ANGLE_IN_RIGHT')))
        points.update(self._find_all(Element('WALL_RIGHT')))
        points.update(self._find_all(Element('ANGLE_BACK_RIGHT')))
        points.update(self._find_all(Element('WALL_BACK')))
        points.update(self._find_all(Element('ANGLE_BACK_LEFT')))
        points.update(self._find_all(Element('WALL_LEFT')))
        points.update(self._find_all(Element('WALL_BACK_ANGLE_LEFT')))
        points.update(self._find_all(Element('WALL_BACK_ANGLE_RIGHT')))
        points.update(self._find_all(Element('ANGLE_OUT_RIGHT')))
        points.update(self._find_all(Element('ANGLE_OUT_LEFT')))
        points.update(self._find_all(Element('SPACE')))
        return list(points)

    def get_perks(self):
        points = set()
        points.update(self._find_all(Element('UNSTOPPABLE_LASER_PERK')))
        points.update(self._find_all(Element('DEATH_RAY_PERK')))
        points.update(self._find_all(Element('UNLIMITED_FIRE_PERK')))
        points.update(self._find_all(Element('FIRE_PERK')))
        points.update(self._find_all(Element('JUMP_PERK')))
        points.update(self._find_all(Element('MOVE_BOXES_PERK')))
        return list(points)

    def is_near(self, x, y, elem):
        _is_near = False
        if not Point(x, y).is_bad(self._layer_size):
            _is_near = (self.is_at(x + 1, y, elem) or
                        self.is_at(x - 1, y, elem) or
                        self.is_at(x, 1 + y, elem) or
                        self.is_at(x, 1 - y, elem))
        return _is_near

    def count_near(self, x, y, elem):
        _near_count = 0
        if not Point(x, y).is_bad(self._layer_size):
            for _x, _y in ((x + 1, y), (x - 1, y), (x, 1 + y), (x, 1 - y)):
                if self.is_at(_x, _y, elem):
                    _near_count += 1
        return _near_count

    def to_string(self):
        return ("Board:\n{brd}\nHero at: {hero}\nOther Heroes "
                "at: {others}\nZombies at: {zmb}\nLasers at:"
                " {lsr}\nHoles at : {hls}\nGolds at: "
                "{gld}\nPerks at: {prk}".format(brd=self._line_by_line(),
                                                  hero=self.get_hero(),
                                                  others=self.get_other_heroes(),
                                                  zmb=self.get_zombies(),
                                                  lsr=self.get_lasers(),
                                                  hls=self.get_holes(),
                                                  gld=self.get_golds(),
                                                  prk=self.get_perks())
                )

    def _line_by_line(self):
        _string_board = '  '
        for i in range(self._layer_size * Board.COUNT_LAYERS):
            _string_board += str(i % 10)
            if (i + 1) % self._layer_size == 0:
                _string_board += '\t'

        _string_board += '\n'

        for i in range(self._layer_size):
            _string_board += str(i % 10) + ' '
            for j in range(Board.COUNT_LAYERS):
                for k in range(self._layer_size):
                    _string_board += self._board[j][k + (i * self._layer_size)]
                _string_board += '\t'
            _string_board += '\n'

        return _string_board


if __name__ == '__main__':
    raise RuntimeError("This module is not designed to be ran from CLI")
