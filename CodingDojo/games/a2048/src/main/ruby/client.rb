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

host_ip = 'codenjoy.com'       # ip of host with running server
port = '80'                    # this port is used for communication between your client and server
user = '3edq63tw0bq4w4iem7nb'  # player id (which you can get from board page url after registration)
code = '1234567890123456789'   # your code  (-- " --")

opts = {:username => user, :host=> host_ip, :port => port, :game_url => 'codenjoy-contest/ws?'}

player = Player.new
CodenjoyConnection.play(player,opts)
