#!/usr/bin/env python3


from sys import exc_info
from traceback import print_exception
from websocket import WebSocketApp


def _on_open(webclient):
    print("Opened Connection.\nSending <NULL> command...")
    webclient.send('')


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


def _on_close(webclient):
    print("WebSocket closed.")


class WebClient(WebSocketApp):

    def __init__(self, url, header=[],
                 on_open=None, on_message=None, on_error=None,
                 on_close=None, keep_running=True, get_mask_key=None, solver=None):
        self._solver = solver
        self.retries = 0
        super().__init__(url, [], _on_open, _on_message, _on_error, _on_close)


if __name__ == '__main__':
    raise RuntimeError("This module is not designed to be ran from CLI.")
