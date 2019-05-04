#!/usr/bin/env python3

###
# #%L
# Codenjoy - it's a dojo-like platform from developers to developers.
# %%
# Copyright (C) 2019 Codenjoy
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

from sys import version_info
from webclient import WebClient
from mysolver import DirectionSolver
import re


def main():
    assert version_info[0] == 3, "You should run me with Python 3.x"

    # you can get this URL after registration on the server with your email
    url = "http://localhost:8080/codenjoy-contest/board/player/a1b2c3d4e5f6g7i8j9k0?code=1234567890123456789"

    repattern = re.compile(
        "^https?://([-a-zA-Z0-9@:%._\+~#=\.]+)/codenjoy-contest/board/player/([a-z0-9]+)\?code=([0-9]+)")
    rematch = repattern.match(url)
    serverandport = rematch.group(1)
    username = rematch.group(2)
    usercode = rematch.group(3)

    dds = DirectionSolver()
    wcl = WebClient(dds)

    wcl.run(serverandport, username, usercode)


if __name__ == '__main__':
    main()
