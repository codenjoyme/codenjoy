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
from player import Player
from codenjoy_connection import CodenjoyConnection


try:
    name = '3edq63tw0bq4w4iem7nb'     # player id (which you can get from board page url after registration)
    code = '1234567890123456789'      # player code (-- " --")
    port = '80'                       # game port
    host = 'codenjoy.com'             # game host
    game_url = 'codenjoy-contest/ws?' # game url

    url = "ws://{0}:{1}/{2}user={3}&code={4}".format(host, port, game_url, name, code)
    player = Player()
    ws = CodenjoyConnection(url, player)
    ws.connect()
    ws.run_forever()
except KeyboardInterrupt:
    ws.close()
