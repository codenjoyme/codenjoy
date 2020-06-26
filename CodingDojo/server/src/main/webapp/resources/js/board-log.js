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
    game.enablePlayerInfo = true;
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
    game.playerId = getSettings('playerId');
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
        game.drawBoard,
        function() {
            initLogs(game.gameName, game.boardSize, game.alphabet, game.playerId);

            if (game.showBody) {
                $(document.body).show();
            }
        });
}

var loading = false;

function loadLogs(playerId, time, onLoad) {
    if (loading) {
        return;
    }

    loading = true;
    loadData('/rest/player/' + playerId + '/log/' + time, function(gameData) {
        onLoad(gameData);
        loading = false;
    });
}

var logTicks = [];
var currentTick = 0;
var firstTick = null;
var lastTick = null;

function initLogs(gameName, boardSize, alphabet, playerId) {

    function getTickFromUrl(){
        let url = new URL(window.location.href);
        if (url.searchParams.has('tick')) {
            return url.searchParams.get('tick');
        } else {
            return 0;
        }
    }

    function updateUrl(key, value) {
        let url = new URL(window.location.href);
        url.searchParams.set(key, value);
        window.history.pushState('data', 'Title', url.href);
    }

    function loadTick(time) {
        currentTick = time;
        var tick = logTicks[time];

        updateUrl("tick", time);

        var data = {};
        var info = data[playerId] = {};
        info.readableName = game.readableName;
        info.score = tick.score;
        info.tickTime = time;
        info.command = tick.command;
        info.message = tick.message;
        info.gameName = tick.gameType;
        info.scores = {};
        info.scores[playerId] = tick.score;
        info.boardSize = boardSize;
        info.board = tick.board;
        info.info = "";
        info.heroesData = {};
        info.heroesData.readableNames = {};
        info.heroesData.readableNames[playerId] = playerId;
        info.heroesData.coordinates = {};
        var coordinates = info.heroesData.coordinates[playerId] = {};
        coordinates.coordinate = {x:-1, y:-1};
        coordinates.level = 0;
        coordinates.multiplayer = false;
        info.heroesData.group = [];
        info.heroesData.group[0] = playerId;

        $('body').trigger('board-updated', data);
    }

    function loadNewLogs(time, onLoad) {
        if (firstTick == time /*|| lastTick == time*/) {
            if (!!onLoad) {
                onLoad(time);
            }
            return;
        }

        loadLogs(playerId, time, function(ticks) {
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
                // if (!lastTick && !findLarger(time)) {
                //     lastTick = time;
                // }
            }
            if (time == 0) {
                return;
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
                var prev = findSmaller(time);
                if (!!prev) {
                    loadTick(prev);
                }
            });
        }
    }

    function goFuture() {
        var time = findLarger(currentTick);
        if (!!time) {
            loadTick(time);
        } else {
            loadNewLogs(currentTick, function(time) {
                var next = findLarger(time);
                if (!!next) {
                    loadTick(next);
                }
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

    loadNewLogs(getTickFromUrl(), function(time) {
        loadTick(time);
    });
}