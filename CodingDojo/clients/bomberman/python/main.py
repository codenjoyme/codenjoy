#!/usr/bin/env python3

from sys import version_info
from webclient import WebClient
from dds import DirectionSolver

def main():

    assert version_info[0] == 3, "You should run me with Python 3.x"

    dds = DirectionSolver()
    wcl = WebClient(dds)

    wcl.run("ws://tetrisj.jvmhost.net:12270/codenjoy-contest/ws", 'au')

if __name__ == '__main__':
    main()
