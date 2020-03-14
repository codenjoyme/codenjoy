###
# #%L
# Codenjoy - it's a dojo-like platform from developers to developers.
# %%
# Copyright (C) 2018 - 2020 Codenjoy
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

test = XUnit.new

#There are no tests , because Board class has no real-life methods
