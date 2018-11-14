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
require 'json'

##################################### ELEMENTS TYPES #########################################################

ELEMENTS = Hash.new

# This is glass content
ELEMENTS[:I_BLUE] = 'I'
ELEMENTS[:J_CYAN] = 'J'
ELEMENTS[:L_ORANGE] = 'L'
ELEMENTS[:O_YELLOW] = 'O'
ELEMENTS[:S_GREEN] = 'S'
ELEMENTS[:T_PURPLE] = 'T'
ELEMENTS[:Z_RED] = 'Z'
ELEMENTS[:NONE] = '.'

# List of figures
FIGURES = [
    ELEMENTS[:I_BLUE],
    ELEMENTS[:J_CYAN],
    ELEMENTS[:L_ORANGE],
    ELEMENTS[:O_YELLOW],
    ELEMENTS[:S_GREEN],
    ELEMENTS[:T_PURPLE],
    ELEMENTS[:Z_RED]
]

##################################### END OF ELEMENTS TYPES #########################################################

# Return list of indexes of char +char+ in string +s+ ("STR".index returns only first char/string appear)
#
# @param [String] s string to search in
# @param [String] char substring to search
# @return [Array] list of indexes
def indexes(s, char)
  (0 ... s.length).find_all { |i| s[i,1] == char }
end

def compare(pt1, pt2)
  if (pt1.x <=> pt2.x) != 0
    pt1.x <=> pt2.x
  else
    pt1.y <=> pt2.y
  end
end

def sort(array)
  array.sort { |pt1, pt2| compare(pt1, pt2) }
end

# Point class
class Point
  attr_accessor :x
  attr_accessor :y

  # Coords (1,1) - upper left side of field
  #
  # @param [Integer] x X coord
  # @param [Integer] y Y coord
  def initialize(x, y)
    @x = x
    @y = y
  end

  # Override of compare method for Point
  def == (other_object)
    other_object.x == @x && other_object.y == @y
  end

  # For better +.inspect+ output
  def to_s
    "[#{@x},#{@y}]"
  end

  # Position of point above current
  def up
    Point.new(@x, @y + 1)
  end

  # Position of point below current
  def down
    Point.new(@x, @y - 1)
  end

  # Position of point on the left side
  def left
    Point.new(@x - 1, @y)
  end

  # Position of point on the right side
  def right
    Point.new(@x + 1, @y)
  end
end

# Game class
class Board
  attr_accessor :board
  attr_accessor :size
  attr_accessor :current_figure_type
  attr_accessor :future_figures
  attr_accessor :current_figure_point

  def process(str)
    puts "-------------------------------------------------------------------------------------------"
    json = JSON.parse(str)
    @board = json["layers"][0]
    @size = Math.sqrt(@board.length).round
    @current_figure_type = json["currentFigureType"]
    @future_figures = json["futureFigures"]
    @current_figure_point = Point.new(json["currentFigurePoint"]["x"], json["currentFigurePoint"]["y"])
  end

  def to_s
    return ("currentFigure: \"" + @current_figure_type + "\" at: " + @current_figure_point.to_s + "\n" +
       "futureFigures: " + @future_figures.to_s + "\n" +
       "board:" + "\n" +
       @board.scan(/.{#{@size}}|.+/).join("\n"))
  end

  # Returns board size
  # @return [Integer] board size
  def size
    Math.sqrt(board.length).to_i
  end

  # Get object at position
  #
  # @param [Point] point position
  # @return [String] char with object, compare with +ELEMENTS[...]+
  def get_at(point)
    board[coords_to_pos(point)]
  end

  # Is element type/s is at specified X,Y?
  #
  # @param [Point] point position
  # @param [String, Array] element one or array of +ELEMENTS[...]+
  # @return [Boolean] if +element+ at position
  def is_at?(point, element)
    if element.is_a?(Array)
      element.include?(get_at(point))
    elsif element.is_a?(String)
      get_at(point) == element
    else
      raise ArgumentError.new("Invalid argument type #{element.class}")
    end
  end

  # Check if element is near position
  #
  # @param [Point] point position
  # @param [String, Array] element one or array of +ELEMENTS[...]+
  def get_near(point)
    res = []

    for dx in -1..1
      for dy in -1..1
        if dx == 0 && dy == 0
          next
        end
        res << get_at(Point.new(point.x + dx, point.y + dy))
      end
    end

    res.empty? ? nil : res
  end

  # Count how many objects of specified type around position
  #
  # @param [Point] point position
  # @param [String, Array] element  one or array of +ELEMENTS[...]+
  # @return [Integer] number of objects around
  def count_near(point, element)
    elements = get_near(point)
    elements.count { |it| it == element }
  end

  # Count how many objects of specified type around position
  #
  # @param [Point] point position
  # @param [String, Array] element  one or array of +ELEMENTS[...]+
  # @return [Integer] number of objects around
  def is_near?(point, element)
    elements = get_near(point)
    elements.find { |it| it == element } != nil
  end

  # Check if figures (elements of +FIGURES+ array) at position
  #
  # @param [Point] point position
  # @return [Boolean] true if barrier at
  def is_free?(point)
    element = board[coords_to_pos(point)]
    !FIGURES.include? element
  end

  # List of given elements
  #
  # @param [String, Array] element  one or array of +ELEMENTS[...]+
  # @return [Array[Point]] list of barriers on the filed
  def get(element)
    res = []
    pos = 0
    board.chars.each do |ch|
      if element.is_a?(Array)
        res << pos_to_coords(pos) if element.include? ch
      elsif element.is_a?(String)
        res << pos_to_coords(pos) if element == ch
      else
        raise ArgumentError.new("Invalid argument type #{element.class}")
      end
      pos += 1
    end

    sort(res)
  end

  # List of busy spaces in the glass
  #
  # @return [Array[Point]] list of barriers on the filed
  def get_figures
    get(FIGURES)
  end

  # Return list of free spaces in the glass
  #
  # @return [Array[Point]] array of walls positions
  def get_free_space
    get(ELEMENTS[:NONE])
  end

  # How far specified element from position (strait direction)
  # Return +size+ if wall in specified direction
  #
  # @param [Point] point position
  # @param [String] direction direction 'UP', 'DOWN', 'LEFT', 'RIGHT'
  # @param [String] element on of +ELEMENTS[...]+
  # @return [Integer] distance
  def next_element_in_direction(point, direction, element)
    dirs = {
        'UP'    => [0, -1],
        'DOWN'  => [0, +1],
        'LEFT'  => [-1, 0],
        'RIGHT' => [+1, 0],
    }

    (1..size).each do |distance|
      el = get_at(
          Point.new(
              (point.x + distance * dirs[direction].first),
              (point.y + distance * dirs[direction].last)
          )
      )

      return size if element == ELEMENTS[:WALL]
      return distance if element == el
    end

    size
  end

  # Converts position in +board+ string to coords
  #
  # @param [Integer] pos position in string
  # @return [Point] point object
  def pos_to_coords(pos)
    x = (pos % size)
    y = size - 1 - (pos / size).to_i

    Point.new x, y
  end

  # Converts position in +board+ string to coords
  #
  # @param [Point] point position
  # @return [Integer] position in +board+ string
  def coords_to_pos(point)
    (size - 1 - point.y) * size + point.x
  end
end
