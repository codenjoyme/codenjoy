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

    CONSTRUCTION: '╬',

    CONSTRUCTION_DESTROYED_DOWN: '╩',
    CONSTRUCTION_DESTROYED_UP: '╦',
    CONSTRUCTION_DESTROYED_LEFT: '╠',
    CONSTRUCTION_DESTROYED_RIGHT: '╣',

    CONSTRUCTION_DESTROYED_DOWN_TWICE: '╨',
    CONSTRUCTION_DESTROYED_UP_TWICE: '╥',
    CONSTRUCTION_DESTROYED_LEFT_TWICE: '╞',
    CONSTRUCTION_DESTROYED_RIGHT_TWICE: '╡',

    CONSTRUCTION_DESTROYED_LEFT_RIGHT: '│',
    CONSTRUCTION_DESTROYED_UP_DOWN: '─',

    CONSTRUCTION_DESTROYED_UP_LEFT: '┌',
    CONSTRUCTION_DESTROYED_RIGHT_UP: '┐',
    CONSTRUCTION_DESTROYED_DOWN_LEFT: '└',
    CONSTRUCTION_DESTROYED_DOWN_RIGHT: '┘',

    CONSTRUCTION_DESTROYED: ' ',

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

  def isAt(x, y, element)
    return false if Point.new(x, y).out_of?(size)
    getAt(x, y) == element;
  end

  def findAll(element)
    result = []
    @raw.length.times do |i|
      point = xyl.getXY(i);
      result.push(point) if isAt(point.x, point.y, element)
    end
    result;
  end

  def getMe
    result = [
      ELEMENTS[:TANK_UP],
      ELEMENTS[:TANK_DOWN],
      ELEMENTS[:TANK_LEFT],
      ELEMENTS[:TANK_RIGHT]
    ].map{ |e| findAll(e)}.flatten
    return nil if (result.length == 0)
    result[0];
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

  def isBulletAt(x, y)
    return false if Point.new(x, y).out_of?(size)
    getAt(x, y) == ELEMENTS[:BULLET]
  end

  def isGameOver
    getMe().nil?;
  end

  def board_to_s
    Array.new(size).each_with_index.map{ |e, n| @raw[(n * size)..((n + 1) * size - 1)]}.join("\n")
  end

  def getBarriers
  #     var result = [];
  #     result = result.concat(findAll(Elements.BATTLE_WALL));
  #     result = result.concat(findAll(Elements.CONSTRUCTION));
  #     result = result.concat(findAll(Elements.CONSTRUCTION_DESTROYED_DOWN));
  #     result = result.concat(findAll(Elements.CONSTRUCTION_DESTROYED_UP));
  #     result = result.concat(findAll(Elements.CONSTRUCTION_DESTROYED_LEFT));
  #     result = result.concat(findAll(Elements.CONSTRUCTION_DESTROYED_RIGHT));
  #     result = result.concat(findAll(Elements.CONSTRUCTION_DESTROYED_DOWN_TWICE));
  #     result = result.concat(findAll(Elements.CONSTRUCTION_DESTROYED_UP_TWICE));
  #     result = result.concat(findAll(Elements.CONSTRUCTION_DESTROYED_LEFT_TWICE));
  #     result = result.concat(findAll(Elements.CONSTRUCTION_DESTROYED_RIGHT_TWICE));
  #     result = result.concat(findAll(Elements.CONSTRUCTION_DESTROYED_LEFT_RIGHT));
  #     result = result.concat(findAll(Elements.CONSTRUCTION_DESTROYED_UP_DOWN));
  #     result = result.concat(findAll(Elements.CONSTRUCTION_DESTROYED_UP_LEFT));
  #     result = result.concat(findAll(Elements.CONSTRUCTION_DESTROYED_RIGHT_UP));
  #     result = result.concat(findAll(Elements.CONSTRUCTION_DESTROYED_DOWN_LEFT));
  #     result = result.concat(findAll(Elements.CONSTRUCTION_DESTROYED_DOWN_RIGHT));
  #     return sort(result);
  # };
  end

  def to_s
    [
      "Board:\n#{board_to_s}",
      "My tank at: #{getMe}",
      "Enemies at: #{getEnemies}",
      "Bulets at: #{getBullets}"
    ].join("\n")
  end
end

# var Board = function(board){
#     var contains  = function(a, obj) {
#         var i = a.length;
#         while (i--) {
#             if (a[i].equals(obj)) {
#                 return true;
#             }
#         }
#         return false;
#     };

#     var sort = function(all) {
#         return all.sort(function(pt1, pt2) {
#             return (pt1.getY()*1000 + pt1.getX()) -
#                 (pt2.getY()*1000 + pt2.getX());
#         });
#     }

#     var removeDuplicates = function(all) {
#         var result = [];
#         for (var index in all) {
#             var point = all[index];
#             if (!contains(result, point)) {
#                 result.push(point);
#             }
#         }
#         return sort(result);
#     };

#     var boardSize = function() {
#         return Math.sqrt(board.length);
#     };

#     var size = boardSize();
#     var xyl = new LengthToXY(size);




#     var isAnyOfAt = function(x, y, elements) {
#         if (pt(x, y).isOutOf(size)) {
#             return false;
#         }
#         for (var index in elements) {
#             var element = elements[index];
#             if (isAt(x, y, element)) {
#                 return true;
#             }
#         }
#         return false;
#     };

#     // TODO применить этот подход в других js клиентах
#     var getNear = function(x, y) {
#         var result = [];
#         for (var dx = -1; dx <= 1; dx++) {
#             for (var dy = -1; dy <= 1; dy++) {
#                 if (dx == 0 && dy == 0) continue;
#                 result.push(getAt(x + dx, y + dy));
#             }
#         }
#         return result;
#     };

#     var isNear = function(x, y, element) {
#         return getNear(x, y).includes(element);
#     };

#     var isBarrierAt = function(x, y) {
#         if (pt(x, y).isOutOf(size)) {
#             return true;
#         }

#         return contains(getBarriers(), pt(x, y));
#     };

#     var countNear = function(x, y, element) {
#         return getNear(x, y)
#                     .filter(function(value) { return value === element })
#                     .length;
#     };

#     return {
#         size : boardSize,
#         getMe : getMe,
#         getEnemies : getEnemies,
#         getBullets : getBullets,
#         isGameOver : isGameOver,
#         isAt : isAt,
#         boardAsString : boardAsString,
#         toString : toString,
#         findAll : findAll,
#         isAnyOfAt : isAnyOfAt,
#         getNear : getNear,
#         isNear : isNear,
#         countNear : countNear,
#         isBarrierAt : isBarrierAt,
#         getBarriers : getBarriers,
#         getAt : getAt
#     };
# };
