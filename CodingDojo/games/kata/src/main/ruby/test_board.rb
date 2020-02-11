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
