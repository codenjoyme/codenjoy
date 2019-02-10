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

pages = pages || {};

pages.boardLog = function() {
    game.enableDonate = false;
    game.enableJoystick = false;
    game.enableAlways = false;
    game.enablePlayerInfo = false;
    game.enablePlayerInfoLevel = false;
    game.enableLeadersTable = false;
    game.enableInfo = false;
    game.enableHotkeys = false;
    game.enableAdvertisement = false;
    game.showBody = true;
    game.sprites = null;
    game.heroInfo = null;

    game.gameName = getSettings('gameName');
    game.playerName = getSettings('playerName');
    game.contextPath = getSettings('contextPath');
    game.code = null;
    game.allPlayersScreen = false;

    initBoardPage(game, initBoardLogComponents);
    initHotkeys();
}

function initBoardLogComponents(game) {
    if (!game.isGraphicOrTextGame) {
        return;
    }

    initCanvases(game.contextPath, game.players, game.allPlayersScreen,
        game.multiplayerType, game.boardSize,
        game.gameName, game.enablePlayerInfo,
        game.enablePlayerInfoLevel,
        game.sprites, game.alphabet, game.spriteElements,
        game.drawBoard);

    initLogs(game.gameName, game.boardSize, game.alphabet, game.playerName);

    if (game.showBody) {
        $(document.body).show();
    }
}

function loadLogs(playerName, time, onLoad) {
    loadData('/rest/player/' + playerName + '/log/' + time, function(gameData) {
        onLoad(gameData);
    });
}

function initLogs(gameName, boardSize, alphabet, playerName) {
    var time = 0;

    loadLogs(playerName, time, function(ticks) {
        var tick = ticks[0];

        var data = {};
        var info = data[playerName] = {};
        info.score = tick.score;
        info.gameName = tick.gameType;
        info.scores = {};
        info.scores[playerName] = tick.score;
        info.boardSize = boardSize;
        info.board = tick.board;
        info.info = "";
        info.heroesData = {};
        info.heroesData.readableNames = {};
        info.heroesData.readableNames[playerName] = playerName;
        info.heroesData.coordinates = {};
        var coordinates = info.heroesData.coordinates[playerName] = {};
        coordinates.coordinate = {x:-1, y:-1};
        coordinates.level = 0;
        coordinates.multiplayer = false;
        info.heroesData.group = [];
        info.heroesData.group[0] = playerName;

        $('body').trigger('board-updated', data);
    });



}