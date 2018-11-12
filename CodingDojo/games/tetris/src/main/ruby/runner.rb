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

load 'solver.rb'

require 'websocket-client-simple'

# Check ARGS
usage = %Q(\n\nPlease run 'ruby runner.rb GAME_HOST USERNAME CODE'\n\nExample:\nruby runner.rb 127.0.0.1:8080 your@email.com 20010765231070354251\n\n)

raise usage unless ARGV[0]
raise usage unless ARGV[1]
raise usage unless ARGV[2]

# WebSocket object to connect to Codenjoy server
ws = WebSocket::Client::Simple.connect "ws://#{ARGV[0]}/codenjoy-contest/ws?user=#{ARGV[1]}&code=#{ARGV[2]}"

# Board object
board = Board.new

# your solver
solver = YourSolver.new

# On message receive
ws.on :message do |msg|
  begin
    # Receive board from Server and update game board
    msg.data =~ /^board=(.*)$/
    json = $1.force_encoding('UTF-8')

    board.process(json)
    answer = solver.process(board)

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

loop do
  sleep 1
end
