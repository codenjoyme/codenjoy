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
# encoding: utf-8

load 'board.rb'

class YourSolver

  # Find solution for this board
  # @param [Board] board
  # @return string
  # UP:   'up',                // you can move
  # DOWN: 'down',
  # LEFT: 'left',
  # RIGHT:'right',
  # ACT:  'act',               // fire
  # STOP: ''                   // stay
  def get_answer(board)

    #######################################################################
    #
    #                     YOUR ALGORITHM HERE
    #
    #######################################################################

    return 'act'
  end

end
