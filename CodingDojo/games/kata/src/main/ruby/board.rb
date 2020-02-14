require 'pry'

class Board
  attr_accessor :data

  def process(data, level)
    @data = JSON.parse(data)
  end
end
