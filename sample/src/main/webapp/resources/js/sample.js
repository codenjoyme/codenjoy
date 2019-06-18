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
// ты можешь включать или выключать те или иные компоненты
// например этот скрипт настроен на то, что будет только борда и
// встроится она в layout указанный в ресурсах
// если заремарить это все, тогда UI будет базовый

var doNothing = true;

/*
game.enableDonate = false;
game.enableJoystick = true;
game.enableAlways = true;
game.enablePlayerInfo = false;
game.enableLeadersTable = false;
game.enableHotkeys = true;
game.enableAdvertisement = false;
game.showBody = false;

game.onBoardPageLoad = function() {
    initLayout(game.gameName, 'board.html', game.contextPath,
        function() {
            $("#glasses").before($("#main_board"));
            $("#main_board").remove();

            $("#leaderboard").before($("#main_leaderboard"));
            $("#main_leaderboard").remove();
        },
        ['js/lib1/script1.js',
            'js/lib2/script1.js',
            'js/lib2/script2.js',
        ],
        function() {
            // setup UI

            $(document.body).show();
        });
}

game.onBoardAllPageLoad = function() {
    initLayout(game.gameName, 'leaderboard.html', game.contextPath,
        null,
        [],
        function() {
            // setup UI

            $(document.body).show();
        });
}

*/
