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

require 'websocket-client-simple'


# Check ARGS
usage = %Q(\n\nPlease run 'ruby game.rb GAME_HOST USERNAME CODE'\n\nExample:\nruby game.rb 127.0.0.1:8080 root@localhost.local\n\n)

raise usage unless ARGV[0]
raise usage unless ARGV[1]
raise usage unless ARGV[2]

##################################### ELEMENTS TYPES #########################################################

ELEMENTS = Hash.new

# This is glass content
ELEMENTS[:BLUE] = 'I'
ELEMENTS[:CYAN] = 'J'
ELEMENTS[:ORANGE] = 'L'
ELEMENTS[:YELLOW] = 'O'
ELEMENTS[:GREEN] = 'S'
ELEMENTS[:PURPLE] = 'T'
ELEMENTS[:RED] = 'Z'
ELEMENTS[:NONE] = '.'

# List of figures
FIGURES = [
    ELEMENTS[:BLUE],
    ELEMENTS[:CYAN],
    ELEMENTS[:ORANGE],
    ELEMENTS[:YELLOW],
    ELEMENTS[:GREEN],
    ELEMENTS[:PURPLE],
    ELEMENTS[:RED],
    ELEMENTS[:NONE],
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
    "x=#{@x}; y=#{@y}"
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

# # Bomberman class
# class Bomberman
#
#   # Initialize
#   # @param [Game] game game object
#   def initialize(game)
#     @game = game
#   end
#
#   # Is Bomberman alive?!
#   # @return [Boolean] +true+ if my bomberman alive
#   def dead?
#     board.index(ELEMENTS[:DEAD_BOMBERMAN]) != nil
#   end
#
#   # Can I move in specified direction?
#   #
#   # @param [String] direction string of direction - 'UP', 'DOWN', 'LEFT', 'RIGHT'
#   # @return [Boolean] true/false
#   def can_move?(direction)
#     point = position
#     !@game.is_barrier_at?(point.send(direction.downcase))
#   end
#
#   # Return current position of Bomberman on field
#   # @return [Point] position of bomberman
#   def position
#     pos = @game.board.index(ELEMENTS[:BOMBERMAN])
#     pos = @game.board.index(ELEMENTS[:DEAD_BOMBERMAN]) unless pos
#     pos = @game.board.index(ELEMENTS[:BOMB_BOMBERMAN]) unless pos
#
#     @game.pos_to_coords(pos)
#   end
#
#   # What will be position of bomberman after move in specified direction
#   #
#   # @param [String] direction - 'UP', 'DOWN', 'LEFT', 'RIGHT'
#   # @return [Point] position after move
#   def position_after_move(direction)
#     point = position
#     point.send(direction.downcase)
#   end
# end


# Game class
class Game
  attr_accessor :board
  # attr_reader :bomberman

  # Returns board size
  # @return [Integer] board size
  def board_size
    Math.sqrt(board.length).to_i
  end

  # # Retruns bomberman position
  # # @return [Bomberman] returns bomberman object
  # def bomberman
  #   @bomberman ||= Bomberman.new(self)
  # end

  # # Returns array of other bomberman positions
  # #
  # # @return [Array[Point]] array of other bomberman`s positions
  # def get_other_bombermans
  #   res = []
  #   bombers = []
  #   bombers += indexes(board, ELEMENTS[:OTHER_BOMBERMAN])
  #   bombers += indexes(board, ELEMENTS[:OTHER_DEAD_BOMBERMAN])
  #   bombers += indexes(board, ELEMENTS[:OTHER_BOMB_BOMBERMAN])
  #
  #   # BOMBERS
  #   bombers.each do |pos|
  #     res << pos_to_coords(pos)
  #   end
  #
  #   res
  # end

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
  # @param [Integer] radius radius to check
  def is_near?(point, element, radius = 1)
    res = []

    x = point.x
    y = point.y

    (1..radius).each do |sh|
      res << Point.new(x-sh, y) if is_at?(Point.new(x-sh, y), element)
      res << Point.new(x+sh, y) if is_at?(Point.new(x+sh, y), element)
      res << Point.new(x, y+sh) if is_at?(Point.new(x, y+sh), element)
      res << Point.new(x, y-sh) if is_at?(Point.new(x, y-sh), element)
    end

    res.empty? ? nil : res
  end

  # Count how many objects of specified type around position
  #
  # @param [Point] point position
  # @param [String, Array] element  one or array of +ELEMENTS[...]+
  # @param [Integer] radius radius
  # @return [Integer] number of objects around
  def count_near(point, element, radius = 1)
    res = is_near?(point, element, radius)
    res ? res.size : 0
  end

  # Check if figures (elements of +FIGURES+ array) at position
  #
  # @param [Point] point position
  # @return [Boolean] true if barrier at
  def is_busy_at?(point)
    element = board[coords_to_pos(point)]
    FIGURES.include? element
  end

  # List of busy spaces in the glass
  #
  # @return [Array[Point]] list of barriers on the filed
  def get_figures
    res = []
    pos = 0
    board.chars.each do |ch|
      res << pos_to_coords(pos) if FIGURES.include? ch
      pos += 1
    end

    res
  end

  # Return list of free spaces in the glass
  #
  # @return [Array[Point]] array of walls positions
  def get_free_space
    res = []
    pos = 0
    board.chars.each do |ch|
      res << pos_to_coords(pos) if ch == ELEMENTS[:NONE]
      pos += 1
    end

    res
  end

  # # Return list of positions where blast will be in next few tacts
  # #
  # # @return [Array[Point]] array of positions where blast will be in next few tacts
  # def get_future_blasts
  #   res = []
  #
  #   get_bombs.each do |bomb|
  #     directions = {:up => true, :down => true, :left => true, :right => true}
  #     res << bomb
  #
  #     (1..4).each do |sh|
  #       # x + N
  #       x = Point.new(bomb.x + sh, bomb.y)
  #       if is_barrier_at?(x)
  #         directions[:right] = false
  #       elsif !is_barrier_at?(x) && directions[:right]
  #         res << x
  #       end
  #
  #       # x - N
  #       x = Point.new(bomb.x - sh, bomb.y)
  #       if is_barrier_at?(x)
  #         directions[:left] = false
  #       elsif !is_barrier_at?(x) && directions[:left]
  #         res << x
  #       end
  #
  #       # y + N
  #       x = Point.new(bomb.x, bomb.y + sh)
  #       if is_barrier_at?(x)
  #         directions[:down] = false
  #       elsif !is_barrier_at?(x) && directions[:down]
  #         res << x
  #       end
  #
  #       # y - N
  #       x = Point.new(bomb.x, bomb.y - sh)
  #       if is_barrier_at?(x)
  #         directions[:up] = false
  #       elsif !is_barrier_at?(x) && directions[:up]
  #         res << x
  #       end
  #
  #     end
  #   end
  #
  #   res
  # end

  # # Is danger at position?
  # #
  # # @param [Point] point position
  # # @return [Boolean] true if danger at position
  # def danger_at?(point)
  #   dangers = []
  #
  #   dangers += get_meat_choppers
  #   dangers += get_future_blasts
  #
  #   dangers.include?(point)
  # end

  # How far specified element from position (strait direction)
  # Return +board_size+ if wall in specified direction
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

    (1..board_size).each do |distance|
      el = get_at(
          Point.new(
              (point.x + distance * dirs[direction].first),
              (point.y + distance * dirs[direction].last)
          )
      )

      return board_size if element == ELEMENTS[:WALL]
      return distance if element == el
    end

    board_size
  end

  # Converts position in +board+ string to coords
  #
  # @param [Integer] pos position in string
  # @return [Point] point object
  def pos_to_coords(pos)
    x = (pos % board_size) + 1
    y = (pos / board_size).to_i + 1

    Point.new x, y
  end

  # Converts position in +board+ string to coords
  #
  # @param [Point] point position
  # @return [Integer] position in +board+ string
  def coords_to_pos(point)
    (point.y - 1) * board_size + (point.x - 1)
  end
end


# WebSocket object to connect to Codenjoy server
ws = WebSocket::Client::Simple.connect "ws://#{ARGV[0]}/codenjoy-contest/ws?user=#{ARGV[1]}&code=#{ARGV[2]}"

# Default direction
direction = 'DOWN'
# Game object
game = Game.new

# On message receive
ws.on :message do |msg|
  begin
    # Receive board from Server and update game board
    msg.data =~ /^board=(.*)$/
    board = $1.force_encoding('UTF-8')
    game.board = board

    # # Bomberman object
    # bomber = game.bomberman

    ############################################################################################################
    #
    #                               YOUR ALGORITHM HERE
    #
    #    Set variables:
    #     * +act+ (true/false) - Place bomb or not in current iteration
    #     * +direction+ - Direction to move (UP, DOWN, LEFT, RIGHT)
    #
    ############################################################################################################


    # Place bomb if wall nearby
    # act = game.count_near(bomber.position, ELEMENTS[:DESTROY_WALL]) > 0

    direction = 'DOWN'

    # Change direction if bomberman can't move in specified direction
    # if direction.empty? || !bomber.can_move?(direction)
    #   ['DOWN', 'UP', 'LEFT', 'RIGHT'].each do |dir|
    #     if bomber.can_move?(dir)
    #       direction = dir
    #       break
    #     end
    #   end
    # end

    # Don't move in to danger places
    # if game.danger_at?(bomber.position_after_move(direction))
    #   ['DOWN', 'UP', 'RIGHT', 'LEFT'].each do |dir|
    #     if !game.danger_at?(bomber.position_after_move(dir)) && bomber.can_move?(dir)
    #       direction = dir
    #       break
    #     end
    #   end
    # end

    ############################################################################################################
    #
    #                               END OF YOUR ALGORITHM HERE
    #
    ############################################################################################################



    # Send Direction and Place bomb (ACT) actions to server
    # ws.send "#{act ? 'ACT' : ''} #{direction.to_s.upcase}"
    ws.send "#{direction.to_s.upcase}"
  rescue Exception => e
    puts e.message
    puts e.backtrace
  end
end

ws.on :close do |e|
  p e
  exit 1
end

ws.on :error do |e|
  p e
end

loop do
  sleep 1
end
