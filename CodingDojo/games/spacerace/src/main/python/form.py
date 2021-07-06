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
###

import PySimpleGUI as sg
import os, inspect, io, time
from PIL import Image
from queue import Queue
from threading import Thread
from cancel_token import CancellationToken
from direction import Direction
from elements import Elements
from board import Board

class Form(Thread):
    '''Bots window with graphic representation'''
    def __init__(self, token: CancellationToken):
        Thread.__init__(self)
        self._window_ = None
        self._canvas_ = None
        self._token_ = token
        self._board_ = None
        self._direction_ = None
        self._message_ = Queue(20)
        self._load_resources_("assets")
        self._canvas_size_ = 520
        self._t1 = None
        self._t2 = None

    def show(self):
        ''' make form visible'''
        self.start()

    def run(self):
        '''main loop'''
        header = [[sg.Text("Game: "),sg.Text("Space Race"), sg.VerticalSeparator() ,sg.Text("Decision make time:"),sg.Text(key="__DMT__", size=(30,1))]]
        
        canvas = sg.Image(size=(self._canvas_size_, self._canvas_size_), pad=(10,10), key="__CANVAS__")
        self._canvas_ = canvas
        field =[[canvas]]

        up_key = sg.Image(key="__UP__", pad=(0,0))
        down_key = sg.Image(key="__DOWN__", pad=(0,0))
        right_key = sg.Image(key="__RIGHT__", pad=(0,0))
        left_key = sg.Image(key="__LEFT__", pad=(0,0))
        act_key = sg.Image(key="__ACT__", pad=(0,0))

        self.movements = {
            Direction.LEFT.id : left_key, 
            Direction.RIGHT.id : right_key, 
            Direction.UP.id : up_key, 
            Direction.DOWN.id : down_key, 
            Direction.ACT.id : act_key 
        }

        controlPanel = [sg.Column([[left_key]],vertical_alignment="center", pad=(0,0)), 
            sg.Column([[up_key],[act_key],[down_key]], pad=(0,0)),
            sg.Column([[right_key]],vertical_alignment="center", pad=(0,0))]

        footer =[[sg.Column([controlPanel]) , sg.Column([[sg.Multiline( size=(50, 5), disabled=True, key= '__LOG__', autoscroll= True)]])]]    
        
        layout = [header,[sg.HorizontalSeparator()], field, [sg.HorizontalSeparator()], footer]
        window = sg.Window('Coder games', layout, icon=sg.EMOJI_BASE64_HAPPY_JOY, keep_on_top=True, finalize=True)
        self._window_ = window
        self._reset_direction_btns_()

        while not self._token_.cancelled:             # Event Loop
            event, values = window.read()
            if event == sg.WIN_CLOSED :
                self.close()
                break
            self.__change_board_view__()   
            self.__change_direction_view__()
            self.__change_log_view__()
        window.close()

    def log_direction(self, direction: Direction):
        ''' show direction on control pannel'''
        try:
            self._t2 = time.time()
            self._direction_ = direction
            self._window_.Finalize()
        except:
            pass

    def log_board(self, board: Board):
        ''' show board on board pane'''
        try:
            self._t1 = time.time()
            self._board_ = board
            self._window_.Finalize()
        except:
            pass   

    def log(self, *messages):
        ''' add line to local log '''
        try:
            self._message_.put( ' '.join(map(lambda x: str(x),messages)))
            self._window_.Finalize()
        except:
            pass   

    def wait_for_close(self):
        self.join()
    
    def close(self):
        try:
            self._token_.cancel()
            self._window_.Finalize()
        except:
            pass   
    def __clear_log__(self):
        try:
            self._window_['__LOG__'].update("")
        except:
            pass

    def __change_direction_view__(self):
        if self._direction_ is not None:
            direction = self._direction_
            self._direction_ = None 
            if direction.is_act():
                self.movements[Direction.ACT.id].update(data = self._active_btns_[Direction.ACT.id])
            if direction.id in self.movements.keys():
                self.movements[direction.id].update(data = self._active_btns_[direction.id])

            if(self._t1 is not None and self._t2 is not None):
                dmt = round(self._t2 - self._t1, 4)
                self._window_["__DMT__"].update("{} sec".format(dmt))

    def __change_board_view__(self):
        if self._board_ is not None:
            board = self._board_
            self._board_ = None
            self.__clear_log__()
            mult = 24
            self._reset_direction_btns_()
            
            size = mult * board.size
            with Image.new(mode = "RGBA", size=(size, size)) as canvas:
                for point, element in board.get_all_extend().items():
                    canvas.paste(self._element_btns_[element], box=(point.x * mult, point.y * mult, point.x * mult + mult, point.y * mult + mult))
                bio = io.BytesIO()
                canvas.thumbnail(size=(self._canvas_size_, self._canvas_size_))
                canvas.save(bio, format="PNG")
                self._canvas_.update(data = bio.getvalue())

    def __change_log_view__(self):
        while not  self._message_.empty() :
            try:
                message = self._message_.get()
                log = self._window_['__LOG__']
                log.update("{}\r{}".format(log.get(), message))
            except:
                pass    

    def _load_resources_(self, resources_path):
        size = 24
        currentdir = (os.path.join(os.path.dirname(os.path.abspath(inspect.getfile(inspect.currentframe()))),resources_path))
        dirs = [Direction.LEFT, Direction.RIGHT, Direction.UP, Direction.DOWN, Direction.ACT]
        self._inactive_btns_ = dict(map(lambda dir: (dir.id, self._load_resource_(os.path.join(currentdir,"{}_inactive.png".format(dir.to_string())))) ,dirs))
        self._active_btns_ = dict(map(lambda dir: (dir.id, self._load_resource_(os.path.join(currentdir,"{}_active.png".format(dir.to_string())))) ,dirs))
        self._element_btns_ = dict(map(lambda element: (element, Image.open(os.path.join(currentdir,"{}.png".format(element.name)), mode="r").convert("RGBA").copy()) ,Elements))

    def _load_resource_(seld, resource_path):
        with open(resource_path, "rb") as file:
            return file.read()      

    def _reset_direction_btns_(self):
        for k, v in self.movements.items():
            v.update(data = self._inactive_btns_[k])
