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

setup.setupSprites();

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

// ========================== leaderboard page ==========================

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
        $('#additional-link').attr('href', setup.contextPath + '/resources/user/icancode-servers.zip')
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

// ========================== user page ==========================

var controller;

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
                    initHelpLink();
                    initAdditionalLink();
                    initLoginLogoutLink();
                }
            });
    }
}

if (setup.demo) {
    setup.onBoardPageLoad();
}