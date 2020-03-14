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
require_relative 'solver.rb'

require 'websocket-client-simple'

# Check ARGS
usage = %Q(\n\nPlease run 'ruby runner.rb BOARD_URL'\n\nExample:\nruby runner.rb http://dojorena.com/codenjoy-contest/board/player/rmtsf91ee1xitijsdtxz\?code\=7743500744161891908\n\n)

# WebSocket object to connect to Codenjoy server
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
