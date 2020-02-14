require_relative 'solver.rb'

require 'websocket-client-simple'

# Check ARGS
usage = %Q(\n\nPlease run 'ruby runner.rb BOARD_URL'\n\nExample:\nruby runner.rb http://dojorena.com/codenjoy-contest/board/player/rmtsf91ee1xitijsdtxz\?code\=7743500744161891908\n\n)

WebSocket object to connect to Codenjoy server
ws_url = ARGV[0].dup
ws_url["board/player/"] = "ws?user="
ws_url["?code="] = "&code="

if ws_url.include?("https")
  ws_url["https"] = "wss"
else
  ws_url["http"] = "ws"
end

ws = WebSocket::Client::Simple.connect ws_url

# Board object
board = Board.new

# your solver
solver = YourSolver.new

ws.on :message do |msg|
  begin
    # Receive board from Server and update game board
    msg.data =~ /^board=(.*)$/
    json = $1.force_encoding('UTF-8')
    current_level = json["level"]
    puts "-------------------------------------------------------------------------------------------"
    p msg.data

    board.process(json, current_level)

    answer = solver.get_answer(board)

    puts "answer: " + answer

    # Send command to server
    ws.send answer
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

loop do sleep 1 end
