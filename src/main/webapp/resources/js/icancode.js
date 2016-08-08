/*-
 * #%L
 * iCanCode - it's a dojo-like platform from developers to developers.
 * %%
 * Copyright (C) 2016 EPAM
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
function runProgram(program, robot) {
    program(robot);
}

// ========================== game setup ==========================

if (typeof game == 'undefined') {
    game = {};
    game.demo = true;
    game.code = 123;
    game.playerName = 'user@gmail.com';
    initLayout = function(game, html, context, transformations, scripts, onLoad) {
        onLoad();
    }
}

game.enableDonate = false;
game.enableJoystick = false;
game.enableAlways = true;
game.enablePlayerInfo = false;
game.enableLeadersTable = false;
game.enableChat = false;
game.enableInfo = false;
game.enableHotkeys = true;
game.enableAdvertisement = false;
game.showBody = false;

// ========================== autocomplete ==========================
var icancodeMaps = {};
var icancodeWordCompleter = {
    getCompletions: function(editor, session, pos, prefix, callback) {
        var line = editor.session.getLine(pos.row);
        var isFind = false;

        for(var index in icancodeMaps) {
            var startFindIndex = pos.column - index.length;

            if (startFindIndex >= 0 && line.substring(startFindIndex, pos.column) == index) {
                isFind = true;
                break;
            }
        }

        if (!isFind) {
            return;
        }

        callback(null, icancodeMaps[index].map(function(word) {
            return {
                caption: word,
                value: word,
                meta: "game",
                score: 1000
            };
        }));

    }
}

var changeLevel = function(value) {
    currentLevel = value;

    // ------------------- get autocomplete -------------------
    initAutocomplete();
};

var initAutocomplete = function() {
    var data;
    icancodeMaps = {};

    for(var iLevel = 0; iLevel <= currentLevel; ++iLevel) {
        if (!getLevelInfo || !getLevelInfo(iLevel).hasOwnProperty('autocomplete')) {
            continue;
        }

        data = getLevelInfo(iLevel).autocomplete;

        for(var index in data) {
            if (!data.hasOwnProperty(index)) {
                continue;
            }

            if (icancodeMaps.hasOwnProperty(index)) {
                icancodeMaps[index] = icancodeMaps[index].concat(data[index].values);
            } else {
                icancodeMaps[index] = data[index].values;
            }

            for(var isynonym = 0; isynonym < data[index].synonyms.length; ++isynonym) {
                icancodeMaps[data[index].synonyms[isynonym]] = icancodeMaps[index];
            }
        }
    }
};

// ========================== leaderboard page ==========================

game.onBoardAllPageLoad = function() {
    initLayout(game.gameName, 'leaderboard.html', game.contextPath,
        null,
        [],
        function() {
            initLeadersTable(game.contextPath, game.playerName, game.code,
                    function() {
                    },
                    function(count, you, link, name, score, maxLength, level) {
                        var star = '';
                        if (count == 1) {
                            star = 'first';
                        } else if (count < 3) {
                            star = 'second';
                        }
                        return '<tr>' +
                                '<td><span class="' + star + ' star">' + count + '<span></td>' +
                                '<td>' + you + '<a href="' + link + '">' + name + '</a></td>' +
                                '<td class="center">' + score + '</td>' +
                            '</tr>';
                    });
            $('#table-logs').removeClass('table');
            $('#table-logs').removeClass('table-striped');
            $(document.body).show();
        });
}

// ========================== user page ==========================

var controller;
var currentLevel = -1;

game.onBoardPageLoad = function() {
    initLayout(game.gameName, 'board.html', game.contextPath,
        null,
        [
            'js/mousewheel/jquery.mousewheel.min.js',
            'js/scroll/jquery.mCustomScrollbar.js',

            'js/game/point.js',
            'js/game/direction.js',
            'js/game/elements.js',
            'js/game/board.js',
            'js/game/level-info.js',

            'js/game/loader/boardPageLoad.js',

            'js/ace/src/ace.js',
            'js/ace/src/ext-language_tools.js',

            'bootstrap/js/bootstrap.js',
        ], function() {
            boardPageLoad();
        });
}

if (game.demo) {
    game.onBoardPageLoad();
}