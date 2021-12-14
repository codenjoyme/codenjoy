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

// ========================== for debugging ==========================

function compileProgram(code) {
    try {
        eval(code);
        return program;
    } catch (e) {
        throw e;
    }
}

function runProgram(program, robot) {
    try {
        program(robot);
    } catch (e) {
        throw e;
    }
}

// ========================== leaderboard page ==========================

var initLeaderboardLink = function() {
    var room = getSettings('room')
    $('#leaderboard-link').attr('href', setup.contextPath + '/board/room/' + room);
}

var initHelpLink = function() {
    if (setup.gameMode == MODE_EKIDS) {
        $('#help-link').hide();
        return; // TODO написать нормально мануал и убрать это
    }
    var pageName = setup.gameMode.split(' ').join('-').toLowerCase();
    $('#help-link').attr('href', setup.contextPath + '/resources/icancode/landing-' + pageName + '.html')
}

var initAdditionalLink = function() {
    if (setup.onlyLeaderBoard) {
        $('#additional-link').attr('href', setup.contextPath + '/resources/icancode/user/clients.zip')
        $('#additional-link').text('Get client')
    }
}

var initLoginLogoutLink = function() {
    if (!!setup.code) {
        var link = setup.contextPath + '/process_logout';
        $('#login-logout-link').attr('href', link);
        $('#login-logout-link').html('Logout');
    } else {
        var link = setup.contextPath + '/login?game=icancode';
        $('#login-logout-link').attr('href', link);
        $('#login-logout-link').html('Login');
    }
}

setup.onBoardAllPageLoad = function(showProgress) {
    initLayout(setup.game, 'leaderboard.html', setup.contextPath,
        null,
        [],
        function() {
            boardAllPageLoad(!!showProgress);
            initHelpLink();
            initAdditionalLink();
            initLoginLogoutLink();
        });
}

setup.drawBoard = function(drawer) {
    drawer.clear();
    drawer.drawBack();
    drawer.drawLayers();

    var fonts = {};
    fonts.userName = {};
    fonts.userName.dx = -15;
    fonts.userName.dy = -45;
    fonts.userName.font = "20px 'Verdana, sans-serif'";
    fonts.userName.fillStyle = "#0FF";
    fonts.userName.textAlign = "left";
    fonts.userName.shadowColor = "#000";
    fonts.userName.shadowOffsetX = 0;
    fonts.userName.shadowOffsetY = 0;
    fonts.userName.shadowBlur = 5;
    drawer.drawPlayerNames(fonts.userName);

    drawer.drawFog();
}

// ========================== game setup ==========================

if (typeof setup == 'undefined') {
    setup = {};
    setup.demo = true;
    setup.code = 123;
    setup.playerId = 'userId';
    setup.readableName = 'Stiven Pupkin';
    initLayout = function(setup, html, context, transformations, scripts, onLoad) {
        onLoad();
    }
}

var controller;

const PARAM_GAME_MODE = 'gameMode';

const SPRITES_EKIDS = 'ekids';
const SPRITES_ROBOT = 'robot';

const MODE_JS = 'javascript';
const MODE_EKIDS = 'ekids';
const MODE_BEFUNGE = 'befunge';
const MODE_CONTEST = 'contest';

setup.setupGame = function() {
    setup.enableDonate = false;
    setup.enableJoystick = false;
    setup.enablePlayerInfo = false;
    setup.enablePlayerInfoLevel = false;
    setup.enableLeadersTable = false;
    setup.enableChat = false;
    setup.enableInfo = false;
    setup.enableHotkeys = true;
    setup.enableForkMe = false;
    setup.enableAdvertisement = false;
    setup.showBody = false;
    setup.debug = false;

    // ==================== gameMode / sprites / controls ========================

    // так как спрайты icancode вылазят за сетку элемента,
    // то надо рисовать всегда все спрайты
    setup.isDrawOnlyChanges = false;

    var toLowerCase = function(param) {
        return (!!param) ? param.toLowerCase() : param;
    }

    setup.gameMode = toLowerCase(getSettings(PARAM_GAME_MODE, '#query'));
    setup.onlyControls = getSettings('controlsOnly', '#query');

    if (setup.onlyControls) {
        setup.drawCanvases = false;
        setup.enableHeader = false;
        setup.enableFooter = false;
        if (!setup.gameMode) { // TODO удалить if после изменения линков на dojorena
            setup.gameMode = MODE_JS;
        }
    } else {
        setup.enableHeader = true;
        setup.enableFooter = true;
    }

    if (!setup.gameMode) {
        // check KEYS constants in register.js
        setup.gameMode = toLowerCase(localStorage.getItem(PARAM_GAME_MODE));

        // TODO почему-то сторится в сторадж строчка "undefined"
        if (setup.gameMode == 'undefined') {
            localStorage.removeItem(PARAM_GAME_MODE);
            setup.gameMode = null;
        }
    }

    // TODO это тут надо потому что join на main page и
    //      форма регистрации иногда отпускает без указания мода
    if (!setup.gameMode) {
        setup.gameMode = MODE_JS;
    }

    if (setup.gameMode == MODE_JS) {
        setup.enableBefunge = false;
        setup.sprites = SPRITES_ROBOT;
    } else if (setup.gameMode == MODE_EKIDS) {
        setup.enableBefunge = true;
        setup.sprites = SPRITES_EKIDS;
    } else if (setup.gameMode == MODE_BEFUNGE) {
        setup.enableBefunge = true;
        setup.sprites = SPRITES_ROBOT;
    } else if (setup.gameMode == MODE_CONTEST) {
        setup.enableBefunge = false;
        setup.sprites = SPRITES_ROBOT;
        setup.onlyLeaderBoard = true;
    } else {
        throw new Error("Unknown iCanCode mode: " + setup.gameMode);
    }
    setup.isDrawByOrder = true;

    // ========================== user page ==========================

    if (setup.onlyLeaderBoard) {
        setup.onBoardPageLoad = function() {
            var showProgress = true;
            setup.onBoardAllPageLoad(showProgress);
        }
    } else {
        setup.onBoardPageLoad = function() {
            initLayout(setup.game, 'board.html', setup.contextPath,
                null,
                [],
                function() {
                    if (this.hasOwnProperty('boardPageLoad')) {
                        boardPageLoad();
                        initLeaderboardLink();
                        initHelpLink();
                        initAdditionalLink();
                        initLoginLogoutLink();
                    }
                });
        }
    }
}

if (setup.demo) {
    setup.onBoardPageLoad();
}