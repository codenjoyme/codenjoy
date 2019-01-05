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
 
gameName = 'Contest';
game.onlyLeaderBoard = true;
game.sprites = null;
game.enableDonate = false;
game.enableJoystick = false;
game.enablePlayerInfo = false;
game.enablePlayerInfoLevel = false;
game.enableLeadersTable = false;
game.enableChat = false;
game.enableInfo = false;
game.enableHotkeys = true;
game.enableAdvertisement = false;
game.showBody = false;
game.debug = false;

var initHelpLink = function() {
    var pageName = gameName.split(' ').join('-').toLowerCase();
    $('#help-link').attr('href', '/codenjoy-contest/resources/snakebattle/landing-' + pageName + '.html')
}
var initAdditionalLink = function() {
    if (game.onlyLeaderBoard) {
        $('#additional-link').attr('href', '/codenjoy-contest/resources/user/snakebattle-servers.zip')
        $('#additional-link').text('Get client')
    }
}

game.onBoardAllPageLoad = function() {
    initLayout(game.gameName, 'leaderboard.html', game.contextPath,
        null,
        ['js/game/loader/boardAllPageLoad.js'],
        function() {
            boardAllPageLoad();
            initHelpLink();
            initAdditionalLink();
        });
}

if (game.onlyLeaderBoard) {
    game.onBoardPageLoad = game.onBoardAllPageLoad;
} else {
    alert('Not implemented');
}