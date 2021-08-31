/*-
 * #%L
 * Codenjoy - it's a dojo-like platform from developers to developers.
 * %%
 * Copyright (C) 2018 Codenjoy
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/gpl-3.0.html>.
 * #L%
 */

const WSocket = require('ws');

var browser = (browser !== undefined);

const log = function(string) {
  if (browser) {
    printLogOnTextArea(string);
  } else {
    console.log(string)
  }
};

const processBoard = function(boardString) {
  const board = new Board(boardString);
  if (browser) {
    printBoardOnTextArea(board.boardAsString());
  }

  let logMessage = board + "\n\n";
  const answer = new DirectionSolver(board).get().toString();
  logMessage += "Answer: " + answer + "\n";
  logMessage += "-----------------------------------\n";
  log(logMessage);

  return answer;
};

const parseBoard = function(message) {
  const pattern = new RegExp(/^board=(.*)$/);
  const parameters = message.match(pattern);
  return parameters[1];
}
// you can get this code after registration on the server with your email
let url = "https://codenjoy.com/codenjoy-contest/board/player/dojorena108?code=5753375427823728155";

url = url.replace("http", "ws");
url = url.replace("board/player/", "ws?user=");
url = url.replace("?code=", "&code=");

function connect() {
  const socket = new WSocket(url);
  log('Opening...');

  socket.on('open', function() {
    log('Web socket client opened ' + url);
  });

  socket.on('close', function() {
    log('Web socket client closed');

    if (!browser) {
      setTimeout(function() {
        connect();
      }, 5000);
    }
  });

  socket.on('message', function(message) {
    const board = parseBoard(message);
    const answer = processBoard(board);
    socket.send(answer);
  });

  return socket;
}

if (!browser) {
  connect();
}

/*
*
*
* Elements
*
*
* */
const Element = {
  BLUE: "I",
  CYAN: "J",
  ORANGE: "L",
  YELLOW: "O",
  GREEN: "S",
  PURPLE: "T",
  RED: "Z",
  NONE: "."
};

Element.values = function() {
  return [Element.BLUE, Element.CYAN, Element.ORANGE, Element.YELLOW, Element.GREEN, Element.PURPLE, Element.RED]
}

/*
*
* Point
*
* */
const Point = function({ x, y } = {}) {
  if (typeof x !== "number" || typeof y !== "number") throw new Error('X and Y are required and should be a number')

  const equals = function(o) {
    return o.getX() === x && o.getY() === y;
  };

  const getX = function() {
    return x
  }

  const getY = function() {
    return y
  }

  const isOutOf = function(size) {
    return (x < 0 || x > size - 1) || (y < 0 || y > size - 1)
  }

  const toString = function() {
    return `[${ x }, ${ y }]`
  };

  const top = function () {
    return new Point({ x, y: y + 1 })
  };

  const bottom = function() {
    return new Point({ x, y: y - 1 })
  };

  const left = function () {
    return new Point({ x: x - 1, y })
  };

  const right = function() {
    return new Point({ x: x + 1, y })
  };

  return {
    equals,
    getX,
    getY,
    toString,
    top,
    bottom,
    right,
    left,
    isOutOf
  }
}

/*
*
*
* Direction
*
* */
const D = function(index, dx, dy, name){

  const changeX = function(x) {
    return x + dx;
  };

  const changeY = function(y) {
    return y - dy;
  };

  const toString = function() {
    return name;
  };

  return {
    changeX : changeX,

    changeY : changeY,

    toString : toString,

    getIndex : function() {
      return index;
    }
  };
};

const Direction = {
  LEFT : D(0, -1, 0, 'left'),
  RIGHT : D(1, 1, 0, 'right'),
  DOWN : D(2, 0, -1, 'down'),
  ACT : D(3, 0, 0, 'act'),        // Rotation of the figure clockwise by 90 deg
  ACT2: D(4, 0, 0, 'act(2)'),     // Rotation of the figure clockwise by 180 deg
  ACT3: D(5, 0, 0, 'act(3)'),     // Rotation of the figure counterclockwise by 90 deg
  RESET: D(6, 0, 0, 'act(0,0)'),  // Reset the glass (as in case of overflow of the glass, points will be removed)
};

Direction.values = function() {
  return [Direction.DOWN, Direction.LEFT, Direction.RIGHT, Direction.ACT, Direction.ACT2, Direction.ACT3, Direction.RESET];
};

Direction.valueOf = function(index) {
  const directions = Direction.values();
  for (let i in directions) {
    const direction = directions[i];
    if (direction.getIndex() === index) {
      return direction;
    }
  }
  return Direction.STOP;
};

/*
*
* Board
*
* */
const Board = function(boardString) {
  const boardObj = JSON.parse(boardString);
  const board = boardObj && boardObj.layers[0];

  // Size of the glass
  const size = Math.sqrt(board.length);
  // Coordinates of lower left corner of the current figure
  const currentFigurePoint = new Point(boardObj.currentFigurePoint)
  //Type of the current figure. One of Elements
  const currentFigureType = boardObj.currentFigureType
  // Types of figures what will be after current figure
  const futureFigures = boardObj.futureFigures

  const xyl = LengthToXY(size)

  const toString = function() {
    return `currentFigure: ${ currentFigureType } at ${ currentFigurePoint.toString() }
    futureFigures: ${ futureFigures }
    board: ${ boardAsString() }
    `
  }

  const boardAsString = function() {
    let result = "";
    for (let i = 0; i < size; i++) {
      result += board.substring(i * size, (i + 1) * size);
      result += "\n";
    }
    return result;
  };

  // Is element at specified coordinates
  // args [Point] point of position
  // args [String] element of Elements which will be compare
  // return [Boolean] if element at position
  const isAt = function(point, element) {
    if (point.isOutOf(size)) return false;

    return getAt(point) === element;
  };

  // Get element at specified coordinates
  // args [Point] point of position
  // return [String] element of Elements
  const getAt = function(point) {
    if (point.isOutOf(size)) return;

    return board.charAt(xyl.getLength(point.getX(), point.getY()));
  };

  // Find all specified elements
  // args [String] element of Elements
  // return [Array[Point]] list of elements on the glass
  const findAll = function(element) {
    let result = [];
    for (let i = 0; i < size ** 2; i++) {
      let point = xyl.getXY(i);
      if (isAt(point, element)) {
        result.push(point);
      }
    }
    return result;
  };

  // Is any element at specified coordinates except Element.NONE
  // args [Point] point of position
  // return [Boolean] if any element at position except Element.NONE
  const isAnyOfAt = function(point) {
    if (point.isOutOf(size)) return false;

    for (let element of Element.values()) {
      if (element === Element.NONE) continue;
      if (isAt(point, element)) {
        return true;
      }
    }
    return false;
  };

  // Is any specified element around position except Element.NONE
  // args [Point] point of position
  // args [String] element of Elements which will be compare
  // return [Boolean] if any specified element around position
  const isNear = function(point, element) {
    return  isAt(point.right(), element) ||
            isAt(point.left(), element) ||
            isAt(point.top(), element) ||
            isAt(point.bottom(), element);
  };

  // Get any elements around position Except Element.None
  // args [Point] point of position
  // return [Array[String]] elements around position except Element.NONE
  const getNear = function(point) {
    let res = []
    res.push(getAt(point.top()))
    res.push(getAt(point.right()))
    res.push(getAt(point.bottom()))
    res.push(getAt(point.left()))

    return res.filter(function(el) {
      return !!el && el !== Element.NONE
    })
  }

  // Count of elements around position
  // args [Point] point of position
  // args [Element] element of Elements which will be compare
  // return [Number] count of elements around position
  const countNear = function(point, element) {
    let count = 0;
    if (isAt(point.top(),      element)) count++;
    if (isAt(point.right(),   element)) count++;
    if (isAt(point.bottom(),    element)) count++;
    if (isAt(point.left(),    element)) count++;
    return count;
  };

  // Is free space at specified position
  // args [Point] point of position
  // return [Boolean] is free space at position
  const isFreeAt = function (point) {
    return isAt(point, Element.NONE);
  }

  // Find all free spaces
  // return [Array[Point]] list of all free spaces on the glass
  const findAllFreeSpace = function () {
    return findAll(Element.NONE)
  }

  // How far specified element from position (strait direction)
  // return distance from point to the end of glass if no any element in specified direction
  // args [Point] point of position
  // args [String] one of ['top', 'bottom', 'left', 'right']
  // args [String] element of Elements which will be compare
  // return [Number] distance to the element in specified direction or distance from point to the end of glass
  const getDistanceToNextElementByDirection = function (point, direction, element) {
    if (!['top', 'bottom', 'left', 'right'].includes(direction)) throw new Error('Direction can be one of [top, bottom, left, right]')
    let distance = 1;

    const getDistance = (point) => {
      const newPoint = point[direction]();

      if (newPoint.isOutOf(size)) return --distance;

      if (isAt(newPoint, element)) {
        return distance;
      } else {
        distance++;
        return getDistance(newPoint)
      }
    };

    return getDistance(point);
  }

  return {
    size,
    currentFigurePoint,
    currentFigureType,
    futureFigures,
    toString,
    isAt,
    getAt,
    boardAsString,
    findAll,
    isAnyOfAt,
    isNear,
    getNear,
    countNear,
    LengthToXY,
    isFreeAt,
    findAllFreeSpace,
    getDistanceToNextElementByDirection,
  }
}

const LengthToXY = function(boardSize) {
  function inversionY(y) {
    return boardSize - 1 - y;
  }

  function inversionX(x) {
    return x;
  }

  return {
    getXY : function(length) {
      if (length === -1) {
        return null;
      }
      const x = inversionX(length % boardSize);
      const y = inversionY(Math.trunc(length / boardSize));
      return new Point({ x, y });
    },

    getLength : function(x, y) {
      const xx = inversionX(x);
      const yy = inversionY(y);
      return yy*boardSize + xx;
    }
  };
};

/*
* Exports
* */
if (!browser) {
  module.exports = { Point, Board, Elements: Element, Direction }
}

/*
*
* Solver
*
* */

const DirectionSolver = function(board){
  return {
    /**
     * @return next hero action
     */
    get : function() {
      // TODO your code here
      return [Direction.LEFT, Direction.ACT3];
    }
  };
};
