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
