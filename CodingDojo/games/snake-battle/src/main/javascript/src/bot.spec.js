import {
    getNextSnakeMove,
} from './bot';
import {
  COMMANDS
} from './constants';

describe("bot", () => {
    describe("getNextSnakeMove", ()=> {
        const mockLogger = ()=> {};

        it("should define method", ()=> {
            expect(getNextSnakeMove).toBeDefined();
        });
        it("should avoid horisontal wall", ()=> {
            const board =
            '*****' +
            '*   *' +
            '*   *' +
            '* ═►*' +
            '*****';
            const move = getNextSnakeMove(board, mockLogger);
            expect(move).toEqual(COMMANDS.UP);
        });
        it("should avoid wall", ()=> {
            const board =
            '*****' +
            '* ═►*' +
            '*   *' +
            '*   *' +
            '*****';
            const move = getNextSnakeMove(board, mockLogger);
            expect(move).toEqual(COMMANDS.DOWN);
        });

        it("should try to catch apples", ()=> {
            const board =
            '******' +
            '* ═► *' +
            '*  ○ *' +
            '*    *' +
            '*    *' +
            '******';
            const move = getNextSnakeMove(board, mockLogger);
            expect(move).toEqual(COMMANDS.DOWN);
        });
    });
});
