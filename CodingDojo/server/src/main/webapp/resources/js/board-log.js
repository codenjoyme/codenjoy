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
    game.enableForkMe = false;
    game.enableInfo = false;
    game.enableHotkeys = true;
    game.enableAdvertisement = false;
    game.showBody = true;
    game.sprites = null;
    game.heroInfo = null;

    game.gameName = getSettings('gameName');
    game.playerName = getSettings('playerName');
    game.readableName = getSettings('readableName');
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

var logTicks = [];
var currentTick = 0;
var firstTick = null;
var lastTick = null;

function initLogs(gameName, boardSize, alphabet, playerName) {

    function loadTick(time) {
        currentTick = time;
        var tick = logTicks[time];

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
    }

    function loadNewLogs(time, onLoad) {
        if (firstTick == time || lastTick == time) {
            if (!!onLoad) {
                onLoad(time);
            }
            return;
        }

        loadLogs(playerName, time, function(ticks) {
            var max = 0;
            for (var index in ticks) {
                var tick = ticks[index];
                logTicks[tick.time] = tick;
                if (max < tick.time) {
                    max = tick.time;
                }
            }
            if (time == 0) {
                time = max;
            } else {
                if (!firstTick && !findSmaller(time)) {
                    firstTick = time;
                }
                if (!lastTick && !findLarger(time)) {
                    lastTick = time;
                }
            }
            if (!!onLoad) {
                onLoad(time);
            }
        });
    }

    function findNext(from, filter) {
        var minDelta = Number.MAX_SAFE_INTEGER;
        var result = -1;
        for (var time in logTicks) {

            if (!filter(time)) {
                continue;
            }

            var delta = Math.abs(from - time);
            if (delta < minDelta) {
                minDelta = delta;
                result = time;
            }
        }

        if (result == -1) {
            return null;
        }

        return result;
    }

    function findSmaller(time) {
        return findNext(time, function(it) { return it < time; })
    }

    function findLarger(time) {
        return findNext(time, function(it) { return it > time; })
    }

    function goPast() {
        var time = findSmaller(currentTick);
        if (!!time) {
            loadTick(time);
        } else {
            loadNewLogs(currentTick, function(time) {
                loadTick(time);
            });
        }
    }

    function goFuture() {
        var time = findLarger(currentTick);
        if (!!time) {
            loadTick(time);
        } else {
            loadNewLogs(currentTick, function(time) {
                loadTick(time);
            });
        }
    }

    $('body').keydown(function(ev) {
        if (ev.keyCode == 37) { // left
            goPast();
        } else if (ev.keyCode == 39) { // right
            goFuture();
        }
    });

    loadNewLogs(0, function(time) {
        loadTick(time);
    });
}