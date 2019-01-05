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

str = '{"currentFigureType":"T","futureFigures":["I","O","L","Z"],"layers":["' +
'.......' +
'......I' +
'..LL..I' +
'...LI.I' +
'.SSLI.I' +
'SSOOIOO' +
'..OOIOO' +
'"],"currentFigurePoint":{"x":1,"y":2}}'

board = Board.new
board.process(str)
puts board.to_s

test.assert("T", board.current_figure_type)
test.assert("[1,2]", board.current_figure_point)
test.assert('["I", "O", "L", "Z"]', board.future_figures)

pt1 = Point.new(0, 0)
test.assert(ELEMENTS[:NONE], board.get_at(pt1))
test.assert(false, board.is_at?(pt1, ELEMENTS[:O_YELLOW]))
test.assert(true, board.is_at?(pt1, ELEMENTS[:NONE]))
test.assert(true, board.is_at?(pt1, [ELEMENTS[:L_ORANGE], ELEMENTS[:NONE]]))
test.assert(true, board.is_free?(pt1))

pt2 = Point.new(2, 0)
test.assert(ELEMENTS[:O_YELLOW], board.get_at(pt2))
test.assert(true, board.is_at?(pt2, ELEMENTS[:O_YELLOW]))
test.assert(false, board.is_at?(pt2, ELEMENTS[:NONE]))
test.assert(false, board.is_at?(pt2, [ELEMENTS[:L_ORANGE], ELEMENTS[:NONE]]))
test.assert(false, board.is_free?(pt2))

pt3 = Point.new(2, 2)
test.assert(ELEMENTS[:S_GREEN], board.get_at(pt3))
test.assert(false, board.is_at?(pt3, ELEMENTS[:O_YELLOW]))
test.assert(false, board.is_at?(pt3, ELEMENTS[:NONE]))
test.assert(false, board.is_at?(pt3, [ELEMENTS[:L_ORANGE], ELEMENTS[:NONE]]))
test.assert(false, board.is_free?(pt3))

pt4 = Point.new(3, 4)
test.assert(ELEMENTS[:L_ORANGE], board.get_at(pt4))
test.assert(false, board.is_at?(pt4, ELEMENTS[:O_YELLOW]))
test.assert(false, board.is_at?(pt4, ELEMENTS[:NONE]))
test.assert(true, board.is_at?(pt4, [ELEMENTS[:L_ORANGE], ELEMENTS[:NONE]]))
test.assert(false, board.is_free?(pt4))

test.assert('["[0,1]", "[1,1]", "[1,2]", "[2,2]"]',
            to_string(board.get(ELEMENTS[:S_GREEN])))

test.assert('["[2,4]", "[3,2]", "[3,3]", "[3,4]"]',
            to_string(board.get(ELEMENTS[:L_ORANGE])))

test.assert('["[0,1]", "[1,1]", "[1,2]", "[2,2]", "[2,4]", "[3,2]", "[3,3]", "[3,4]"]',
            to_string(board.get([ELEMENTS[:L_ORANGE], ELEMENTS[:S_GREEN]])))

pt5 = Point.new(3, 4)
test.assert('[".", "L", ".", "L", ".", "I", ".", "."]', board.get_near(pt5).to_s)
test.assert(false, board.is_near?(pt5, ELEMENTS[:T_PURPLE]))
test.assert(true, board.is_near?(pt5, ELEMENTS[:I_BLUE]))
test.assert(1, board.count_near(pt5, ELEMENTS[:I_BLUE]))
test.assert(2, board.count_near(pt5, ELEMENTS[:L_ORANGE]))

pt6 = Point.new(2, 2)
test.assert('["S", "S", ".", "O", ".", "O", "L", "L"]', board.get_near(pt6).to_s)
test.assert(true, board.is_near?(pt6, ELEMENTS[:L_ORANGE]))
test.assert(false, board.is_near?(pt6, ELEMENTS[:I_BLUE]))
test.assert(2, board.count_near(pt6, ELEMENTS[:L_ORANGE]))
test.assert(2, board.count_near(pt6, ELEMENTS[:S_GREEN]))

test.assert('["[0,1]", "[1,1]", "[1,2]", "[2,0]", ' +
                '"[2,1]", "[2,2]", "[2,4]", "[3,0]", ' +
                '"[3,1]", "[3,2]", "[3,3]", "[3,4]", ' +
                '"[4,0]", "[4,1]", "[4,2]", "[4,3]", ' +
                '"[5,0]", "[5,1]", "[6,0]", "[6,1]", ' +
                '"[6,2]", "[6,3]", "[6,4]", "[6,5]"]',
            to_string(board.get_figures))

            test.assert('["[0,0]", "[0,2]", "[0,3]", "[0,4]", ' +
                '"[0,5]", "[0,6]", "[1,0]", "[1,3]", ' +
                '"[1,4]", "[1,5]", "[1,6]", "[2,3]", ' +
                '"[2,5]", "[2,6]", "[3,5]", "[3,6]", ' +
                '"[4,4]", "[4,5]", "[4,6]", "[5,2]", ' +
                '"[5,3]", "[5,4]", "[5,5]", "[5,6]", ' +
                '"[6,6]"]',
            to_string(board.get_free_space))

test.print
