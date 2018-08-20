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

function loadPlayers(onLoad) {
    loadData('/rest/game/' + game.gameName + '/players', function(players) {
        if (game.allPlayersScreen) {
            game.players = players;
        } else {
            for (var index in players) {
                if (players[index].name == game.playerName) {
                    game.players = [players[index]];
                }
            }
        }

        onLoad(game.players);
    });
}

function initBoardPage(game) {
    loadContext(function(ctx) {
        loadData('/rest/game/' + game.gameName + '/type', function(playerGameInfo) {
            game.multiplayerType = playerGameInfo.multiplayerType;
            game.boardSize = playerGameInfo.boardSize;

            loadData('/rest/player/' + game.playerName + '/check/' + game.code, function(registered) {
                game.registered = registered;

                loadData('/rest/sprites/' + game.gameName + '/exists', function(isGraphicOrTextGame) {
                    game.isGraphicOrTextGame = isGraphicOrTextGame;

                    loadPlayers(function(players) {
                        initBoardComponents(game);
                    });
                });
            });
        });
    });
}

function initBoardComponents(game) {
    initBoards(game.players, game.allPlayersScreen,
            game.gameName, game.playerName, game.contextPath);

    if (initCanvasesGame) {
        initCanvasesGame(game.contextPath, game.players, game.allPlayersScreen,
                    game.multiplayerType, game.boardSize,
                    game.gameName, game.enablePlayerInfo,
                    game.enablePlayerInfoLevel,
                    game.sprites, game.drawBoard);
    } else if (game.isGraphicOrTextGame) {
        initCanvases(game.contextPath, game.players, game.allPlayersScreen,
                    game.multiplayerType, game.boardSize,
                    game.gameName, game.enablePlayerInfo,
                    game.enablePlayerInfoLevel,
                    game.sprites, game.drawBoard);
    } else {
        initCanvasesText(game.contextPath, game.players, game.allPlayersScreen,
                        game.multiplayerType, game.boardSize,
                        game.gameName, game.enablePlayerInfo,
                        game.enablePlayerInfoLevel, game.drawBoard);
    }

    if (game.enableDonate) {
        initDonate(game.contextPath);
    }

    initJoystick(game.playerName, game.registered,
            game.code, game.contextPath);

    if (game.enableLeadersTable) {
        initLeadersTable(game.contextPath, game.playerName, game.code);
    }

    if (!game.enableInfo) {
        $("#fork-me").hide();
        $("#how-to-play").hide();
    }

    if (game.enableHotkeys) {
        // do nothing because hotkeys init itself
    }

    if (game.enableAdvertisement) {
        initAdvertisement();
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
}

$(document).ready(function() {
    game.gameName = getSettings('gameName');
    game.playerName = getSettings('playerName');
    game.code = getSettings('code');
    game.allPlayersScreen = getSettings('allPlayersScreen');
    game.contextPath = getSettings('contextPath');

    initBoardPage(game);
});
