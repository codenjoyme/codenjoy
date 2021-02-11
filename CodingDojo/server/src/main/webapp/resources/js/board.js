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
    game.gameName = getSettings('gameName');
    game.playerId = getSettings('playerId');
    game.readableName = getSettings('readableName');
    game.code = getSettings('code');
    game.allPlayersScreen = getSettings('allPlayersScreen');
    game.contextPath = getSettings('contextPath');

    initBoardPage(game, initBoardComponents);
    initHotkeys();
}

function initBoardPage(game, onLoad) {
    loadData('/rest/player/' + game.playerId + '/' + game.code + '/wantsToPlay/' + game.gameName, function(gameData) {
        game.contextPath = gameData.context;
        game.multiplayerType = gameData.gameType.multiplayerType;
        game.boardSize = gameData.gameType.boardSize;
        game.registered = gameData.registered;

        game.isGraphicOrTextGame = gameData.sprites.length > 0;
        game.spriteElements = gameData.sprites;
        game.alphabet = gameData.alphabet;

        // TODO надо как-то иначе решить переключение спрайтов, а то если не грузить icancode c ее кастомной html странички, то спрайты не прорисуются
        if (!game.sprites && game.gameName == 'icancode') {
            game.sprites = 'robot';
        }

        var players = gameData.players;
        if (game.allPlayersScreen) {
            game.players = players;
        } else {
            for (var index in players) {
                if (players[index].id == game.playerId) {
                    game.players = [players[index]];
                }
            }
        }

        if (!!onLoad) {
            onLoad(game);
        }
    });
}

function initBoardComponents(game) {
    if (game.loadBoardData) {
        initBoards(game.players, game.allPlayersScreen,
            game.gameName, game.playerId, game.contextPath);
    }

    if (game.drawCanvases) {
        if (typeof initCanvasesGame == 'function') {
            initCanvasesGame(game.contextPath, game.players, game.allPlayersScreen,
                game.multiplayerType, game.boardSize,
                game.gameName, game.enablePlayerInfo,
                game.enablePlayerInfoLevel,
                game.sprites, game.alphabet, game.spriteElements,
                game.drawBoard);
        } else if (game.isGraphicOrTextGame) {
            initCanvases(game.contextPath, game.players, game.allPlayersScreen,
                game.multiplayerType, game.boardSize,
                game.gameName, game.enablePlayerInfo,
                game.enablePlayerInfoLevel,
                game.sprites, game.alphabet, game.spriteElements,
                game.drawBoard);
        } else {
            initCanvasesText(game.contextPath, game.players, game.allPlayersScreen,
                game.multiplayerType, game.boardSize,
                game.gameName, game.enablePlayerInfo,
                game.enablePlayerInfoLevel, game.drawBoard);
        }
    }

    if (game.enableDonate) {
        initDonate(game.contextPath);
    }

    if (typeof initJoystick == 'function') {
        if (!!game.playerId) {
            initJoystick(game.playerId, game.registered,
                game.code, game.contextPath);
        }
    }

    if (game.enableLeadersTable) {
        initLeadersTable(game.contextPath, game.playerId, game.code);
    }

    if (!game.enableForkMe) {
        $("#fork-me").hide();
    }

    if (!game.enableInfo) {
        $("#how-to-play").hide();
    }

    if (game.enableAdvertisement) {
        initAdvertisement(game.contextPath);
    }

    if (game.showBody) {
        $(document.body).show();
    }

    if (game.allPlayersScreen) {
        if (!!game.onBoardAllPageLoad) {
            game.onBoardAllPageLoad();
        }
    } else {
        if (!!game.onBoardPageLoad) {
            game.onBoardPageLoad();
        }
    }

    if (typeof setupMouseWheelZoom == 'function') {
        setupMouseWheelZoom();
    }
}
