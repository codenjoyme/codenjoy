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

var adminKey = false;

function initHotkeys() {
    var game = getSettings('game') || setup.game;
    var room = getSettings('room') || setup.room;
    var contextPath = getSettings('contextPath') || setup.contextPath;

    var gameParam = function(ch) {
        return !game ? '' : (ch + 'game=' + game);
    }
    var roomParam = function(ch) {
        return !room ? '' : (ch + 'room=' + room);
    }
    $('body').keydown(function(ev) {
        if (!setup.enableHotkeys) {
            return;
        }

        if (ev.ctrlKey && ev.altKey && ev.keyCode == 65) { // Ctrl-Alt-A + ...
            adminKey = true;
        } else if (adminKey && ev.keyCode == 68) { // ... + D (aDmin)
            window.open(contextPath + '/admin' + roomParam('?'));
        } else if (adminKey && ev.keyCode == 82) { // ... + R (Register)
            window.open(contextPath + '/register' + gameParam('?'));
        } else if (adminKey && ev.keyCode == 77) { // ... + M (Main)
            window.open(contextPath);
        } else if (adminKey && ev.keyCode == 74) { // ... + J (Joystick)
            setup.enableJoystick = !setup.enableJoystick;
        } else if (adminKey && ev.keyCode == 76) { // ... + l (Log)
            window.open(contextPath + '/board/log/player/'
                + setup.playerId + '?code='
                + setup.code + gameParam('&') + roomParam('&'));
        } else if (adminKey && ev.keyCode == 66) { // ... + B (Board)
            window.open(contextPath + '/board/room/' + room);
        } else if (adminKey && ev.keyCode == 72) { // ... + H (Help)
            window.open(contextPath + '/help');
        } else if (adminKey && ev.keyCode == 85) { // ... + U (rUles)
            window.open(contextPath + '/help?game=' + game);
        } else {
            adminKey = false;
        }
    });
}