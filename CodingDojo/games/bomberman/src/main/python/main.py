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


from sys import version_info
from webclient import WebClient
from dds import DirectionSolver
from urllib.parse import urlparse, parse_qs


def get_url_for_ws(url):
    parsed_url = urlparse(url)
    query = parse_qs(parsed_url.query)

    return "{}://{}/codenjoy-contest/ws?user={}&code={}".format('ws' if parsed_url.scheme == 'http' else 'wss',
                                                                parsed_url.netloc,
                                                                parsed_url.path.split('/')[-1],
                                                                query['code'][0])


def main():
    assert version_info[0] == 3, "You should run me with Python 3.x"

    # substitute following link with the one you've copied in your browser after registration
    url = "https://dojorena.io/codenjoy-contest/board/player/8aq9izxu40h7blnrd3l9?code=6712803793075447834&gameName=bomberman"
    direction_solver = DirectionSolver()

    wcl = WebClient(url=get_url_for_ws(url), solver=direction_solver)
    wcl.run_forever()


if __name__ == '__main__':
    main()
