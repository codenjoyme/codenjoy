const GameClient = require("./src/GameClient");
const Solver = require("./src/Solver");

const serverUrl = "http://localhost:8080/codenjoy-contest/board/player/0?code=000000000000"
const gameClient = new GameClient(serverUrl, new Solver());

gameClient.connect();
