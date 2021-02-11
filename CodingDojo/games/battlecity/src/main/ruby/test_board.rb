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

require_relative 'board.rb'

class XUnit

  attr_accessor :failures
  attr_accessor :index

  def initialize
    @failures = Array.new
    @index = 1
  end

  def assert(expected, actual)
    @index += 1
    if expected.to_s != actual.to_s
      @failures.push "[" + @index.to_s + "] '" + expected.to_s + "' != '" + actual.to_s + "'"
    end
  end

  def print()
    puts "------------------------------------"
    if @failures.any?
      @failures.each { |fail| puts "FAIL " + fail }
    else
      puts "SUCCESS!!!"
    end
    puts "------------------------------------"
  end

end

def to_string(array)
  array.map {|it| it.to_s }
end

test = XUnit.new

board = Board.new

formated_data = File.open("test_boards/board1.txt", "r").read
data = formated_data.split("\n").join('')

board = Board.new
board.process(data)

test.assert(formated_data, board.board_to_s + "\n")
test.assert([9.0, 5.0], board.get_me)
test.assert([[28.0, 20.0], [12.0, 1.0]], board.getBullets)
test.assert(true, board.bullet_at?(28, 20))
test.assert(false, board.game_over?)
test.assert(true, board.near?(4, 4, Board::ELEMENTS[:WALL]))
test.assert(false, board.near?(100, 100, Board::ELEMENTS[:WALL]))
test.assert(547, board.get_barriers.size)
test.assert(true, board.any_of_at?(9, 5, Board::TANK))
test.assert(true, board.barrier_at?(28, 13))
test.assert(5, board.count_near(4, 4, "╬"))
test.assert(["╨", "└", "╬", "╬", "╬", "╬", "╬", "╩"], board.get_near(4,4))
test.assert([
  [12.0, 4.0], [2.0, 32.0], [28.0, 32.0], [29.0, 32.0], [1.0, 18.0],
  [31.0, 31.0], [10.0, 19.0], [28.0, 17.0], [2.0, 4.0]], board.getEnemies)

# test out of board behavior
%w(getAt bullet_at? any_of_at?).each do |method|
  test.assert(false, board.send(method, 100, 100))
end

test.print
