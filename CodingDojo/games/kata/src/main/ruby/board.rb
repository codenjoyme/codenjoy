require 'pry'

class Board
  attr_accessor :data

  def process(data, level)
    puts "-------------------------------------------------------------------------------------------"
    @data = JSON.parse(data)
  end
end
