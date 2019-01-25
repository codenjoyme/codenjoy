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
  getBoardSize,
  getElementByXY,
  getFirstPositionOf,
  isAt,
  getAt,
  isNear
} from './utils';
import {
  ELEMENT
} from './constants';

describe("utils", () => {
    describe("getBoardSize", ()=> {
        it("should return board size", ()=> {
            const size = getBoardSize('****');
            expect(size).toEqual(2);
        });
    });

    describe("getElementByXY", () => {
        it("should returned specified element for (0,0)", () => {
            // 123|456|789
            const board = '123456789';
            const element = getElementByXY(board, {x: 0, y: 0});
            expect(element).toEqual("1");
        });

        it("should returned specified element for (2,1)", () => {
            // 123|456|789
            const board = '123456789';
            const element = getElementByXY(board, {x: 2, y: 1});
            expect(element).toEqual("6");
        });
    });

    describe("getFirstPositionOf", ()=> {
        it("should return first match", ()=> {
            const board = '123456789';
            const position = getFirstPositionOf(board, ['6', '9']);
            expect(position).toEqual({ x: 2, y: 1});
        });
    });

    describe("isAt", ()=> {
        it("should return false for incorrect coords", ()=> {
            const board = '123456789';
            const res = isAt(board, 6, 3, ELEMENT.NONE);
            expect(res).toEqual(false);
        });

        it("should return 6 for (2,1)", ()=> {
            const board = '12345 789';
            const res = isAt(board, 2, 1, ELEMENT.NONE);
            expect(res).toEqual(true);
        });
    });

    describe("getAt", ()=> {
        it("should return WALL for incorrect coords", ()=> {
            const board = '123456789';
            const res = getAt(board, 6, 1);
            expect(res).toEqual(ELEMENT.WALL);
        });
        it("should return element for specified coords", ()=> {
            const board = '12345 789';
            const res = getAt(board, 2, 1);
            expect(res).toEqual(ELEMENT.NONE);
        });
    });

    describe("isNear", ()=> {
        it("should return WALL for incorrect coords", ()=> {
          const board =
              '******' +
              '* ═► *' +
              '*  ○ *' +
              '*    *' +
              '*    *' +
              '******';
            const res = isNear(board, 3, 1, '○');
            expect(res).toEqual(true);
        });
    })
});
