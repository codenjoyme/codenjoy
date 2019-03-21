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
    name = 'anatoliliotych' # name which was used during registration
    port = '8080' # game port
    host = 'localhost' # game host
    game_url = 'codenjoy-contest/ws?' # game url

    url = "ws://{0}:{1}/{2}user={3}".format(host, port, game_url, name)
    player = Player()
    ws = CodenjoyConnection(url, player)
    ws.connect()
    ws.run_forever()
except KeyboardInterrupt:
    ws.close()
