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

from logger import Logger
from solver import Solver
from board import Board
from elements import Elements

board_strings = [
    '''☼               0            ☼
☼                            ☼
☼ ♣                          ☼
☼    0                       ☼
☼                 $          ☼
☼                            ☼
☼            ♣   0           ☼
☼             7              ☼
☼                            ☼
☼              0     $       ☼
☼                 ♣          ☼
☼                            ☼
☼                       0    ☼
☼                            ☼
☼        $     ♣             ☼
☼               0            ☼
☼                            ☼
☼                            ☼
☼          ♣        0        ☼
☼                    $       ☼
☼                            ☼
☼   0                        ☼
☼        ♣                   ☼
☼                            ☼
☼   $    0                   ☼
☼                            ☼
☼                            ☼
☼             ☻             0☼
☼                            ☼
☼               ☺  ☻    $    ☼''',
    '''☼    $                       ☼
☼               0            ☼
☼                            ☼
☼ ♣                          ☼
☼    0                       ☼
☼                 $          ☼
☼                            ☼
☼            ♣7  0           ☼
☼                            ☼
☼                            ☼
☼              0     $       ☼
☼                 ♣          ☼
☼                            ☼
☼                       0    ☼
☼                            ☼
☼        $     ♣             ☼
☼               0            ☼
☼                            ☼
☼                            ☼
☼          ♣        0        ☼
☼                    $       ☼
☼                            ☼
☼   0                        ☼
☼        ♣                   ☼
☼                            ☼
☼   $    0                   ☼
☼                            ☼
☼            ☻               ☼
☼                           0☼
☼              ☺   ☻         ☼''',
    '''☼                   ♣        ☼
☼    $                       ☼
☼               0            ☼
☼                            ☼
☼ ♣                          ☼
☼    0                       ☼
☼                 $          ☼
☼             7              ☼
☼            ♣   0           ☼
☼                            ☼
☼                            ☼
☼              0     $       ☼
☼                 ♣          ☼
☼                            ☼
☼                       0    ☼
☼                            ☼
☼        $     ♣             ☼
☼               0            ☼
☼                            ☼
☼                            ☼
☼          ♣        0        ☼
☼                    $       ☼
☼                            ☼
☼   0                        ☼
☼        ♣                   ☼
☼                            ☼
☼   $    0                   ☼
☼           ☻                ☼
☼                            ☼
☼               ☺  ☻        0☼''',
    '''☼                            ☼
☼                   ♣        ☼
☼    $                       ☼
☼               0            ☼
☼                            ☼
☼ ♣                          ☼
☼    0                       ☼
☼             7   $          ☼
☼                            ☼
☼            ♣   0           ☼
☼                            ☼
☼                            ☼
☼              0     $       ☼
☼                 ♣          ☼
☼                            ☼
☼                       0    ☼
☼                            ☼
☼        $     ♣             ☼
☼               0            ☼
☼                            ☼
☼                            ☼
☼          ♣        0        ☼
☼                    $       ☼
☼                            ☼
☼   0                        ☼
☼        ♣                   ☼
☼                            ☼
☼   $    0 ☻                 ☼
☼                            ☼
☼              ☺   ☻         ☼''']

logger = Logger()
solver = Solver(logger)

for board_string in board_strings:
    board = Board(board_string, Elements.getByChar, Elements.WALL)
    try:
        result = solver.get(board)
        logger.log_command(result)
    except Exception as error:
        logger.log("Error occured:", error)