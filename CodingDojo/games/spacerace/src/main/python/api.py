#! /usr/bin/env python3

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

from logger import Logger
from cancel_token.cancel_token import CancellationToken
import websocket
from board import Board
from solver import Solver
from elements import Elements
from safe_callback import safe_callback

class Api:
    def __init__(self, serviceUrl: str, logger, cancelation_token: CancellationToken):
        self.token = cancelation_token
        self.url = serviceUrl.replace("http", "ws").replace(
            "board/player/", "ws?user=").replace("?code=", "&code=")
        self.logger = logger
        self.solver = Solver(logger)
        
    @safe_callback()
    def onOpen(self, ws):
        self.logger.log("Web socket client opened {}".format(self.url))

    @safe_callback()
    def onClose(self, ws, reason, arg):
        self.logger.log("Web socket client closed")

    @safe_callback()
    def onError(self, ws, error):
        try:
            self.logger.log("Error:", error)
            ws.close()
        except:
            pass    

    @safe_callback()
    def onMessage(self, ws, message):
        try:
            if self.token.cancelled:
                ws.close()
                return
            boardString = str(message).replace("board=","")
            board = Board(boardString, Elements.getByChar, Elements.WALL)
            self.logger.log_board(board)
            answer = self.solver.get(board)
            self.logger.log_command(answer)
            ws.send(answer.to_string())
 
        except Exception as error:
            self.logger.log("Error occured:", error)
            ws.send("")

        except:
            self.logger.log("Undefined error")
            ws.send("")

    def connect(self):
        self.logger.log('Opening...')
        ws = websocket.WebSocketApp(self.url,

                                    on_open    = self.onOpen,
                                    on_close   = self.onClose,
                                    on_error   = self.onError,
                                    on_message = self.onMessage
            )
        ws.run_forever()
