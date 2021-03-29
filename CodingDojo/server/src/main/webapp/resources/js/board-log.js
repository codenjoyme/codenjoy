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
    setup.enableDonate = false;
    setup.enableJoystick = false;
    setup.enableAlways = false;
    setup.enablePlayerInfo = true;
    setup.enablePlayerInfoLevel = false;
    setup.enableLeadersTable = false;
    setup.enableForkMe = false;
    setup.enableInfo = false;
    setup.enableHotkeys = true;
    setup.enableAdvertisement = false;
    setup.showBody = true;
    setup.sprites = null;
    setup.heroInfo = null;

    setup.game = getSettings('game');
    setup.room = getSettings('room');
    setup.playerId = getSettings('playerId');
    setup.readableName = getSettings('readableName');
    setup.contextPath = getSettings('contextPath');
    setup.code = null;
    setup.allPlayersScreen = false;

    initBoardPage(setup, initBoardLogComponents);
    initHotkeys();
}

function initBoardLogComponents(setup) {
    if (!setup.isGraphicOrTextGame) {
        return;
    }

    initCanvases(setup.contextPath, setup.players, setup.allPlayersScreen,
        setup.multiplayerType, setup.boardSize,
        setup.game, setup.enablePlayerInfo,
        setup.enablePlayerInfoLevel,
        setup.sprites, setup.alphabet, setup.spriteElements,
        setup.drawBoard,
        function() {
            initLogs(setup.game, setup.boardSize, setup.alphabet, setup.playerId);

            if (setup.showBody) {
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

function initLogs(game, boardSize, alphabet, playerId) {

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
        info.readableName = setup.readableName;
        info.score = tick.score;
        info.tickTime = time;
        info.command = tick.command;
        info.message = tick.message;
        info.game = tick.gameType;
        info.scores = {};
        info.scores[playerId] = tick.score;
        info.boardSize = boardSize;
        info.board = tick.board;
        info.info = "";
        info.readableNames = {};
        info.readableNames[playerId] = playerId;
        info.coordinates = {};
        var coordinates = info.coordinates[playerId] = {};
        coordinates.coordinate = {x:-1, y:-1};
        coordinates.level = 0;
        coordinates.multiplayer = false;
        info.group = [];
        info.group[0] = playerId;

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