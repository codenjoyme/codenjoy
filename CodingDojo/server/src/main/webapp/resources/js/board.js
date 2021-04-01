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

pages.board = function() {
    setup.authenticated = getSettings('authenticated');
    setup.game = getSettings('game');
    setup.room = getSettings('room');
    setup.playerId = getSettings('playerId');
    setup.readableName = getSettings('readableName');
    setup.code = getSettings('code');
    setup.allPlayersScreen = getSettings('allPlayersScreen');
    setup.contextPath = getSettings('contextPath');

    initBoardPage(setup, initBoardComponents);
    initHotkeys();
}

function initBoardPage(setup, onLoad) {
    loadData('/rest/player/' + setup.playerId + '/' + setup.code + '/wantsToPlay/' + setup.game + '/' + setup.room, function(gameData) {
        setup.contextPath = gameData.context;
        setup.multiplayerType = gameData.gameType.multiplayerType;
        setup.boardSize = gameData.gameType.boardSize;
        setup.registered = gameData.registered;

        setup.isGraphicOrTextGame = gameData.sprites.length > 0;
        setup.spriteElements = gameData.sprites;
        setup.alphabet = gameData.alphabet;
        setup.setupSprites();

        var players = gameData.players;
        if (setup.allPlayersScreen) {
            setup.players = players;
        } else {
            for (var index in players) {
                if (players[index].id == setup.playerId) {
                    setup.players = [players[index]];
                }
            }
        }

        if (!!onLoad) {
            onLoad(setup);
        }
    });
}

function initBoardComponents(setup) {
    if (setup.loadBoardData) {
        initBoards(setup.players, setup.allPlayersScreen,
            setup.game, setup.playerId, setup.contextPath);
    }

    if (setup.drawCanvases) {
        if (typeof initCanvasesGame == 'function') {
            initCanvasesGame(setup.contextPath, setup.players, setup.allPlayersScreen,
                setup.multiplayerType, setup.boardSize,
                setup.game, setup.enablePlayerInfo,
                setup.enablePlayerInfoLevel,
                setup.sprites, setup.alphabet, setup.spriteElements,
                setup.drawBoard);
        } else if (setup.isGraphicOrTextGame) {
            initCanvases(setup.contextPath, setup.players, setup.allPlayersScreen,
                setup.multiplayerType, setup.boardSize,
                setup.game, setup.enablePlayerInfo,
                setup.enablePlayerInfoLevel,
                setup.sprites, setup.alphabet, setup.spriteElements,
                setup.drawBoard);
        } else {
            initCanvasesText(setup.contextPath, setup.players, setup.allPlayersScreen,
                setup.multiplayerType, setup.boardSize,
                setup.game, setup.enablePlayerInfo,
                setup.enablePlayerInfoLevel, setup.drawBoard);
        }
    }

    if (setup.enableChat) {
        initChat(setup.contextPath);
    }

    if (setup.enableDonate) {
        initDonate(setup.contextPath);
    }

    if (typeof initJoystick == 'function') {
        if (!!setup.playerId) {
            initJoystick(setup.playerId, setup.registered,
                setup.code, setup.contextPath);
        }
    }

    if (setup.enableLeadersTable) {
        initLeadersTable(setup.contextPath, setup.playerId, setup.code);
    }

    if (!setup.enableForkMe) {
        $("#fork-me").hide();
    }

    if (!setup.enableInfo) {
        $("#how-to-play").hide();
    }

    if (setup.enableAdvertisement) {
        initAdvertisement(setup.contextPath);
    }

    if (setup.showBody) {
        $(document.body).show();
    }

    if (setup.allPlayersScreen) {
        if (!!setup.onBoardAllPageLoad) {
            setup.onBoardAllPageLoad();
        }
    } else {
        if (!!setup.onBoardPageLoad) {
            setup.onBoardPageLoad();
        }
    }

    if (typeof setupMouseWheelZoom == 'function') {
        setupMouseWheelZoom();
    }
}
