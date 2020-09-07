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
/**
 * Created by Mikhail_Udalyi on 09.08.2016.
 */

var boardAllPageLoad = function(showProgress) {

    if (game.debug) {
        game.debugger();
    }

    // ----------------------- init progressbar -------------------
    $("#progress-bar-container").toggle(showProgress);
    if (showProgress) {
        var onChangeLevel = function(level, multiple, lastPassed, levelIncreased, win) {
            // do nothing
        }
        var levelProgress = initLevelProgress(game, onChangeLevel);
    }
    // ----------------------- init leaders table -------------------

    initLeadersTable(game.contextPath, game.playerId, game.code,
        function(count, you, link, name, score, maxLength, level) {
            var star = '';
            if (count == 1) {
                star = 'first';
            } else if (count <= 3) {
                star = 'second';
            }
            return '<tr>' +
                '<td><span class="' + star + ' star">' + count + '<span></td>' +
                '<td>' + you + '<a href="' + link + '">' + name + '</a></td>' +
                '<td class="center">' + score + '</td>' +
                '</tr>';
        });

    // ----------------- init visibility hotkeys ----------------------

    var leader = $('.board');
    var glasses = $('.glasses');
    leaderPosition = 1;
    glassesPosition = 2;
    leaderKey = false;
    var changeAlignment = function(element, position) {
        switch (position) {
            case 0:
                element.css({
                    left: 0,
                    right: 0,
                    'margin-left': 'auto',
                    'margin-right': 'auto'
                });
            break;
            case 1:
                element.css({
                    left: 0,
                    right: 0,
                    'margin-left': 'auto',
                    'margin-right': ''
                });
            break;
            case 2:
                element.css({
                    left: 0,
                    right: 0,
                    'margin-left': '',
                    'margin-right': 'auto'
                });
            break;
        }
    };
    var showHide = function(element) {
        if (element.is(':visible')) {
            element.hide();
        } else {
            element.show();
        }
    }
    $('body').keydown(function(ev) {
        if (ev.ctrlKey && ev.altKey && ev.keyCode == 83) { // Ctrl-Alt-S
            leaderKey = !leaderKey;
        } else if (leaderKey && ev.keyCode == 49) { // ... + 1 // show hide leaderboard
            showHide(leader);
        } else if (leaderKey && ev.keyCode == 50) { // ... + 2 // show hide glasses
            showHide(glasses);
        } else if (leaderKey && ev.keyCode == 51) { // ... + 3
            showHide($('.header-container'));
        } else if (leaderKey && ev.keyCode == 52) { // ... + 4 // leaderboard position
            if (++leaderPosition > 2) {
                leaderPosition = 0;
            }
            changeAlignment(leader, leaderPosition);
        } else if (leaderKey && ev.keyCode == 53) { // ... + 5
            if (++glassesPosition > 2) {
                glassesPosition = 0;
            }
            changeAlignment(glasses, glassesPosition);
        } else {
            leaderKey = false;
        }
    });

    // ------------------------ starting ---- ----------------------

    $('#table-logs').removeClass('table');
    $('#table-logs').removeClass('table-striped');
    $(document.body).show();
}