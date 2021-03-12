#!/usr/bin/env python3


from sys import version_info
from webclient import WebClient
from solver import DirectionSolver
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
    url = "http://localhost:8080/codenjoy-contest/board/player/0?code=000000000000"
    direction_solver = DirectionSolver()

    wcl = WebClient(url=get_url_for_ws(url), solver=direction_solver)
    wcl.run_forever()


if __name__ == '__main__':
    main()
