#!/usr/bin/env python3

from sys import exc_info
from traceback import print_exception
from websocket import WebSocketApp

def _on_open(webclient):
    print("Opened Connection.\nSending <NULL> command...")
    webclient.send('NULL')

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

def _on_error(webclient, error):
    print(error)

class WebClient(WebSocketApp):

    def __init__(self, solver):
        #assert solver not None
        self._solver = solver
        self._server = None
        self._user = None

    def run(self, server, user):
        super().__init__("{}?user={}".format(server, user))
        self.on_message = _on_message
        self.on_open = _on_open
        self._server = server
        self._user = user
        self.run_forever()


if __name__ == '__main__':
    raise RuntimeError("This module is not designed to be ran from CLI.")
