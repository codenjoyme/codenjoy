###
# #%L
# Codenjoy - it's a dojo-like platform from developers to developers.
# %%
# Copyright (C) 2018 - 2019 Codenjoy
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

require 'codenjoy_connection'
require File.expand_path('../player', __FILE__)

host_ip = 'codenjoy_server' # ip of host with running tetris-server
port = '8080' # this port is used for communication between your client and tetris-server
user = 'anatoliliotych' # your username, use the same for registration on tetris-server

opts = {:username => user, :host=> host_ip, :port => port, :game_url => 'tetris-contest/ws?'}

player = Player.new
CodenjoyConnection.play(player,opts)
