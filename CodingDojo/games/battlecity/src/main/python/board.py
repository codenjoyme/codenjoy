#! /usr/bin/env python3


from math import sqrt
from element import Element
from point import Point
from  textwrap import  wrap

_DIRECTIONS= dict(
                LEFT='TANK_LEFT',
                RIGHT='TANK_RIGHT',
                UP='TANK_UP',
                DOWN='TANK_DOWN',
                ACT='TANK_LEFT',
                )



class Board:

    def __init__(self, board_string):
        self._string = board_string.replace('\n', '')
        # self._string = board_string

        # if self._string[39]!='☼':
        #     # a = ''.join(reversed(self._string))
        #     invert =  True
        #     # self._string = ''.join(reversed(self._string))
        #     # self._string = ''.join(''.join(reversed(i)) for i in wrap(self._string[:34*5-1],34,drop_whitespace=False))

        self._len = len(self._string)  # the length of the string
        self._size = int(sqrt(self._len))  # size of the board
        print("Board size is sqrt", self._len, self._size)
        self._board = []

    def _find_all(self, element):
        _points = []
        _a_char = element.char
        for i, c in enumerate(self._string):
            if c == _a_char:
                 _points.append(self._strpos2pt(i))
        return _points

    def _strpos2pt(self, strpos):
        return Point(*self._strpos2xy(strpos))

    def _strpos2xy(self, strpos):
        return strpos % self._size, strpos // self._size

    def _xy2strpos(self, x, y):
        return self._size * y + x

    def get_at(self, x, y):
        return Element(self._string[self._xy2strpos(x, y)])

    def is_at(self, x, y, element_object):
        return element_object==self.get_at(x, y)

    def is_barrier_at(self, x, y):
        """ Return true if barrier is at x,y."""
        return Point(x, y) in self.barriers

    def is_tray_at(self, x, y):
        """ Return true if tray is at x,y."""
        return Point(x, y) in self.tray

    def is_other_tank_at(self, x, y):
        """ Return true if other_tank is at x,y."""
        return Point(x, y) in self.other_tank



    @property
    def my_tank(self):
        points = []
        points.extend(self._find_all(Element('TANK_UP')))
        points.extend(self._find_all(Element('TANK_DOWN')))
        points.extend(self._find_all(Element('TANK_LEFT')))
        points.extend(self._find_all(Element('TANK_RIGHT')))
        return points


    @property
    def ai_tank(self):
        points = []
        points.extend(self._find_all(Element('AI_TANK_UP')))
        points.extend(self._find_all(Element('AI_TANK_RIGHT')))
        points.extend(self._find_all(Element('AI_TANK_DOWN')))
        points.extend(self._find_all(Element('AI_TANK_LEFT')))
        return points

    @property
    def other_tank(self):
        points = []
        points.extend(self._find_all(Element('OTHER_TANK_UP')))
        points.extend(self._find_all(Element('OTHER_TANK_RIGHT')))
        points.extend(self._find_all(Element('OTHER_TANK_DOWN')))
        points.extend(self._find_all(Element('OTHER_TANK_LEFT')))
        return points

    @property
    def bullet(self):
        points = []
        points.extend(self._find_all(Element('BULLET')))
        return points

    def get_battle_walls(self):
        """ Retuns the list of walls Element Points."""
        points = []
        points.extend(self._find_all(Element('BATTLE_WALL')))
        return points

    @property
    def barriers(self):
        points = []
        points.extend(self.walls)
        points.extend(self.battle_walls)
        points.extend(self.river)
        points.extend(self.ice)
        points.extend(self.ai_tank)
        points.extend(self.other_tank)
        points.extend(self.tree)
        # points.extend(self.bullet)
        return points

    @property
    def tanks(self):
        points = []
        points.extend(self.ai_tank)
        points.extend(self.other_tank)
        return points


    @property
    def ice(self):
        points = []
        points.extend(self._find_all(Element('ICE')))
        return points
        # return self._find_all(Element('ICE'))

    @property
    def river(self):
        points = []
        points.extend(self._find_all(Element('RIVER')))
        return points
        # return self._find_all(Element('RIVER'))

    @property
    def tree(self):
        points = []
        points.extend(self._find_all(Element('TREE')))
        return points
        return self._find_all(Element('TREE'))


    @property
    def battle_walls(self):
        points = []
        points.extend(self._find_all(Element('BATTLE_WALL')))
        return points


    @property
    def walls(self):
        points = []
        points.extend(self._find_all(Element('WALL')))
        points.extend(self._find_all(Element('WALL_DESTROYED_DOWN')))
        points.extend(self._find_all(Element('WALL_DESTROYED_UP')))
        points.extend(self._find_all(Element('WALL_DESTROYED_LEFT')))
        points.extend(self._find_all(Element('WALL_DESTROYED_RIGHT')))
        points.extend(self._find_all(Element('WALL_DESTROYED_DOWN_TWICE')))
        points.extend(self._find_all(Element('WALL_DESTROYED_UP_TWICE')))
        points.extend(self._find_all(Element('WALL_DESTROYED_LEFT_TWICE')))
        points.extend(self._find_all(Element('WALL_DESTROYED_RIGHT_TWICE')))
        points.extend(self._find_all(Element('WALL_DESTROYED_LEFT_RIGHT')))
        points.extend(self._find_all(Element('WALL_DESTROYED_UP_DOWN')))
        points.extend(self._find_all(Element('WALL_DESTROYED_UP_LEFT')))
        points.extend(self._find_all(Element('WALL_DESTROYED_RIGHT_UP')))
        points.extend(self._find_all(Element('WALL_DESTROYED_DOWN_LEFT')))
        points.extend(self._find_all(Element('WALL_DESTROYED_UP_LEFT')))
        points.extend(self._find_all(Element('WALL_DESTROYED_DOWN_RIGHT')))
        return points


    def is_near(self, x, y, elem):
        _is_near = False
        if not Point(x, y).is_bad(self._size):
            _is_near = (self.is_at(x + 1, y, elem) or
                        self.is_at(x - 1, y, elem) or
                        self.is_at(x, 1 + y, elem) or
                        self.is_at(x, 1 - y, elem))
        return _is_near

    def is_near_all(self, x, y, points):
        _near_count = 0
        if not Point(x, y).is_bad(self._size) and points:
            for _x, _y in ((x + 1, y), (x - 1, y), (x, 1 + y), (x, 1 - y)):
                if Point(_x, _y) in points:
                    _near_count += 1
        return _near_count

    def direction(self, x, y):
        course = ('LEFT', 'RIGHT', 'DOWN', 'UP')
        if not Point(x, y).is_bad(self._size):
            _dir = self.get_at(x,y)
            for item in course:
                if item in str(_dir.name).rpartition('_'):
                    return item



    def count_near(self, x, y, elem):
        _near_count = 0
        if not Point(x, y).is_bad(self._size):
            for _x, _y in ((x + 1, y), (x - 1, y), (x, 1 + y), (x, 1 - y)):
                if self.is_at(_x, _y, elem):
                    _near_count += 1
        return _near_count


    def to_string(self):
        return ("Board:\n{brd}\nTank at: {tnk}{tank_img}\nOther Tank "
                "at: {others}\nAI Tank at: {aitnk}\nBullet at:"
                "{blt}".format(brd=self._line_by_line(),
                               tnk=self.my_tank,
                               tank_img=self.my_tank,
                               others=self.other_tank,
                               aitnk=self.ai_tank,
                               blt=self.bullet)
                )

    def _line_by_line(self):
        return '\n'.join([self._string[i:i + self._size] for i in range(0, self._len, self._size)])


if __name__ == '__main__':
    raise RuntimeError("This module is not designed to be ran from CLI")
