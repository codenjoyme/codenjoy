#!/usr/bin/env python3

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

from sys import exc_info
from traceback import print_exception
from websocket import WebSocketApp


def _on_open(webclient):
    print("WebSocket opened.")
    # webclient.send('NULL')


def _on_close(webclient):
    print("WebSocket closed.")


def _on_error(webclient, error):
    print("WebSocket error: {}".format(error))


def _on_message(webclient, message):
    """
    Gets board string from message and passes it to solver.
    Solver should provide a get function that takes a board string 
    and returns a Movement command to send.
    """
    try:
        board = message.lstrip("board=")
        webclient.send(webclient._solver.get(board))
    except Exception as e:
        print("Exception occurred")
        print(e)
        print_exception(*exc_info())


class WebClient(WebSocketApp):

    def __init__(self, solver):
        # assert solver not None
        self._solver = solver
        self._server = None
        self._user = None

    def run(self, serverandport, user, code):
        super().__init__(
            "ws://{}/codenjoy-contest/ws?user={}&code={}".format(serverandport, user, code))
        self.on_open = _on_open
        self.on_close = _on_close
        self.on_error = _on_error
        self.on_message = _on_message
        self._server = serverandport
        self._user = user
        self.run_forever()


if __name__ == '__main__':
    raise RuntimeError("This module is not designed to be ran from CLI.")
