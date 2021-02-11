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

  def out_of?(board_size)
    x >= board_size || y >= board_size || x < 0 || y < 0;
  end
end

class LengthToXY
  def initialize(board_size)
    @board_size = board_size
  end

  def inversionY(y)
    @board_size - 1 - y;
  end

  def inversionX(x)
    x;
  end

  def getXY(length)
    return nil if (length == -1)
    x = inversionX(length % @board_size);
    y = inversionY((length / @board_size).floor);
    return Point.new(x, y);
  end

  def getLength(x, y)
      xx = inversionX(x);
      yy = inversionY(y);
      yy * @board_size + xx;
  end
end

class Board

  ELEMENTS = {
    NONE: ' ',
    BATTLE_WALL: '☼',
    BANG: 'Ѡ',

    WALL: '╬',

    WALL_DESTROYED_DOWN: '╩',
    WALL_DESTROYED_UP: '╦',
    WALL_DESTROYED_LEFT: '╠',
    WALL_DESTROYED_RIGHT: '╣',

    WALL_DESTROYED_DOWN_TWICE: '╨',
    WALL_DESTROYED_UP_TWICE: '╥',
    WALL_DESTROYED_LEFT_TWICE: '╞',
    WALL_DESTROYED_RIGHT_TWICE: '╡',

    WALL_DESTROYED_LEFT_RIGHT: '│',
    WALL_DESTROYED_UP_DOWN: '─',

    WALL_DESTROYED_UP_LEFT: '┌',
    WALL_DESTROYED_RIGHT_UP: '┐',
    WALL_DESTROYED_DOWN_LEFT: '└',
    WALL_DESTROYED_DOWN_RIGHT: '┘',

    WALL_DESTROYED: ' ',

    BULLET: '•',

    TANK_UP: '▲',
    TANK_RIGHT: '►',
    TANK_DOWN: '▼',
    TANK_LEFT: '◄',

    OTHER_TANK_UP: '˄',
    OTHER_TANK_RIGHT: '˃',
    OTHER_TANK_DOWN: '˅',
    OTHER_TANK_LEFT: '˂',

    AI_TANK_UP: '?',
    AI_TANK_RIGHT: '»',
    AI_TANK_DOWN: '¿',
    AI_TANK_LEFT: '«'
  }

  ENEMIES = [
    ELEMENTS[:AI_TANK_UP],
    ELEMENTS[:AI_TANK_DOWN],
    ELEMENTS[:AI_TANK_LEFT],
    ELEMENTS[:AI_TANK_RIGHT],
    ELEMENTS[:OTHER_TANK_UP],
    ELEMENTS[:OTHER_TANK_DOWN],
    ELEMENTS[:OTHER_TANK_LEFT],
    ELEMENTS[:OTHER_TANK_RIGHT]
  ]

  TANK = [
    ELEMENTS[:TANK_UP],
    ELEMENTS[:TANK_DOWN],
    ELEMENTS[:TANK_LEFT],
    ELEMENTS[:TANK_RIGHT]
  ]

  BARRIERS = [
    ELEMENTS[:BATTLE_WALL],
    ELEMENTS[:WALL],
    ELEMENTS[:WALL_DESTROYED_DOWN],
    ELEMENTS[:WALL_DESTROYED_UP],
    ELEMENTS[:WALL_DESTROYED_LEFT],
    ELEMENTS[:WALL_DESTROYED_RIGHT],
    ELEMENTS[:WALL_DESTROYED_DOWN_TWICE],
    ELEMENTS[:WALL_DESTROYED_UP_TWICE],
    ELEMENTS[:WALL_DESTROYED_LEFT_TWICE],
    ELEMENTS[:WALL_DESTROYED_RIGHT_TWICE],
    ELEMENTS[:WALL_DESTROYED_LEFT_RIGHT],
    ELEMENTS[:WALL_DESTROYED_UP_DOWN],
    ELEMENTS[:WALL_DESTROYED_UP_LEFT],
    ELEMENTS[:WALL_DESTROYED_RIGHT_UP],
    ELEMENTS[:WALL_DESTROYED_DOWN_LEFT],
    ELEMENTS[:WALL_DESTROYED_DOWN_RIGHT]
  ]

  def process(data)
    @raw = data
  end

  def size
    @size ||= Math.sqrt(@raw.length);
  end

  def xyl
    @xyl ||= LengthToXY.new(size);
  end

  def getAt(x, y)
    return false if Point.new(x, y).out_of?(size)
    @raw[xyl.getLength(x, y)];
  end

  def at?(x, y, element)
    return false if Point.new(x, y).out_of?(size)
    getAt(x, y) == element;
  end

  def findAll(element)
    result = []
    @raw.length.times do |i|
      point = xyl.getXY(i);
      result.push(point) if at?(point.x, point.y, element)
    end
    result;
  end

  def get_me
    me = find_by_list(TANK)
    return nil if me.nil?
    find_by_list(TANK).flatten
  end

  def find_by_list(list)
    result = list.map{ |e| findAll(e) }.flatten.map{ |e| [e.x, e.y] }
    return nil if (result.length == 0)
    result
  end

  def getEnemies
    find_by_list(ENEMIES)
  end

  def getBullets
    find_by_list([ELEMENTS[:BULLET]])
  end

  def get_near(x, y)
    return false if Point.new(x, y).out_of?(size)
    result = []
    (-1..1).each do |dx|
      (-1..1).each do |dy|
          next if (dx == 0 && dy == 0)
          result.push(getAt(x + dx, y + dy))
      end
    end
    result;
  end

  def barrier_at?(x, y)
    return false if Point.new(x, y).out_of?(size)
    get_barriers.include?([x.to_f, y.to_f]);
  end

  def count_near(x, y, element)
    get_near(x, y).select{ |e| e == element}.size
  end

  def near?(x, y, element)
    n = get_near(x, y)
    return false if !n
    n.include?(element);
  end

  def bullet_at?(x, y)
    return false if Point.new(x, y).out_of?(size)
    getAt(x, y) == ELEMENTS[:BULLET]
  end

  def any_of_at?(x, y, elements = [])
    return false if Point.new(x, y).out_of?(size)
    elements.each do |e|
      return true if at?(x, y, e)
    end
    false;
  end

  def game_over?
    get_me.nil?;
  end

  def board_to_s
    Array.new(size).each_with_index.map{ |e, n| @raw[(n * size)..((n + 1) * size - 1)]}.join("\n")
  end

  def get_barriers
    find_by_list(BARRIERS)
  end

  def to_s
    [
      "Board:\n#{board_to_s}",
      "My tank at: #{getMe}",
      "Enemies at: #{getEnemies}",
      "Bullets at: #{getBullets}"
    ].join("\n")
  end
end
