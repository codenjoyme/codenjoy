###
# #%L
# Codenjoy - it's a dojo-like platform from developers to developers.
# %%
# Copyright (C) 2018 - 2021 Codenjoy
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
from logger import Logger
from form import Form
from cancel_token import CancellationToken

class CompositeLogger:
    def __init__(self, token: CancellationToken) -> None:
        self.logger = Logger()
        self.form = Form(token)
        self.form.show()

    def log(self, *args):
        self.logger.log(*args)
        self.form.log(*args)

    def log_board(self,board):
        self.logger.log_board(board)
        self.form.log_board(board)

    def log_command(self, command):
        self.logger.log_command(command)
        self.form.log_direction(command)
