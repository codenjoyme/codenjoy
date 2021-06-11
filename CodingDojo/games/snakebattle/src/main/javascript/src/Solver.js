const PlayerCommand = require("./Enums/PlayerCommand");

class Solver {
    /**
     * @param {Board} board
     * @returns {string}
     */
    Decide(board) {
        // Todo Make your magic here!

        return this.Random();
    }

    /**
     * 
     * @return {string}
     * @constructor
     */
    Random() {
        let keys = Object.keys(PlayerCommand);
        return PlayerCommand[keys[keys.length * Math.random() << 0]];
    }
}

module.exports = Solver;