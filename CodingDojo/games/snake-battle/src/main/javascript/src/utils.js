/*-
 * #%L
 * Codenjoy - it's a dojo-like platform from developers to developers.
 * %%
 * Copyright (C) 2018 - 2019 Codenjoy
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
import {
  ELEMENT
} from './constants';

// Here is utils that might help for bot development
export function getBoardAsString(board) {
    const size = getBoardSize(board);

    return getBoardAsArray(board).join("\n");
}

export function getBoardAsArray(board) {
  const size = getBoardSize(board);
  var result = [];
  for (var i = 0; i < size; i++) {
      result.push(board.substring(i * size, (i + 1) * size));
  }
  return result;
}

export function getBoardSize(board) {
    return Math.sqrt(board.length);
}

export function isGameOver(board) {
    return board.indexOf(ELEMENT.HEAD_DEAD) !== -1;
}

export function isAt(board, x, y, element) {
    if (isOutOf(board, x, y)) {
        return false;
    }
    return getAt(board, x, y) === element;
}

export function getAt(board, x, y) {
    if (isOutOf(board, x, y)) {
        return ELEMENT.WALL;
    }
    return getElementByXY(board, { x, y });
}

export function isNear(board, x, y, element) {
    if (isOutOf(board, x, y)) {
        return ELEMENT.WALL;
    }

    return isAt(board, x + 1, y, element) ||
			  isAt(board, x - 1, y, element) ||
			  isAt(board, x, y + 1, element) ||
			  isAt(board, x, y - 1, element);
}

export function isOutOf(board, x, y) {
    const boardSize = getBoardSize(board);
    return x >= boardSize || y >= boardSize || x < 0 || y < 0;
}

export function getHeadPosition(board) {
    return getFirstPositionOf(board, [
        ELEMENT.HEAD_DOWN,
        ELEMENT.HEAD_LEFT,
        ELEMENT.HEAD_RIGHT,
        ELEMENT.HEAD_UP,
        ELEMENT.HEAD_DEAD,
        ELEMENT.HEAD_EVIL,
        ELEMENT.HEAD_FLY,
        ELEMENT.HEAD_SLEEP,
    ]);
}

export function getFirstPositionOf(board, elements) {
    for (var i = 0; i < elements.length; i++) {
        var element = elements[i];
        var position = board.indexOf(element);
        if (position !== -1) {
            return getXYByPosition(board, position);
        }
    }
    return null;
}

export function getXYByPosition(board, position) {
    if (position === -1) {
        return null;
    }

    const size = getBoardSize(board);
    return {
        x:  position % size,
        y: (position - (position % size)) / size
    };
}

export function getElementByXY(board, position) {
    const size = getBoardSize(board);
    return board[size * position.y + position.x];
}
