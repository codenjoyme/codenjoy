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
import time
from api import Api
from logger import Logger
from composite_logger import CompositeLogger
from cancel_token import CancellationToken
from configuration import Configuration

def main():
    conf = Configuration()
    cancelation_token = CancellationToken()
    conf.is_ui_enabled
    logger =  CompositeLogger(cancelation_token) if conf.is_ui_enabled else Logger()
    api = Api(conf.connectionString, logger, cancelation_token)
    while not cancelation_token.cancelled:
        api.connect()
        time.sleep(conf.connectionTimeout / 1000)

if __name__ == '__main__':
    main()
