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


import websocket
from logger import Logger
from board import Board
from solver import Solver
from elements import Elements
from configuration import Configuration
from callbackhelper import CallBack


class Api:

    def __init__(self, serviceUrl: str):
        self.url = serviceUrl.replace("http", "ws").replace(
            "board/player/", "ws?user=").replace("?code=", "&code=")
        self.solver = Solver()

    def onOpen(self):
        Logger.log("Web socket client opened {}".format(self.url))

    def onClose(self):
        Logger.log("Web socket client closed")

    def onError(self, ws, error):
        Logger.log("Error:", error)
        ws.close()

    def onMessage(self, ws, message):
        try:
            boardString = str(message).replace("board=","")
            board = Board(boardString, Elements.getByChar, Elements.WALL)
            Logger.logBoard(board)
            answer = self.solver.get(board)
            Logger.logCommand(answer)
            ws.send(answer.to_string())
 
        except Exception as error:
            Logger.log("Error occured:", error)    

        except:
            Logger.log("Undefined error")

    def connect(self):
        Logger.log('Opening...')
        ws = websocket.WebSocketApp(self.url,

                                    on_open    = CallBack(self, Api.onOpen),
                                    on_close   = CallBack(self, Api.onClose),
                                    on_error   = CallBack(self, Api.onError),
                                    on_message = CallBack(self, Api.onMessage)
            )
        ws.run_forever()
