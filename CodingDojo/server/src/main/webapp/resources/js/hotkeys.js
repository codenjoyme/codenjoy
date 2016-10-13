/*-
 * #%L
 * Codenjoy - it's a dojo-like platform from developers to developers.
 * %%
 * Copyright (C) 2016 Codenjoy
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
var adminKey = false;

function initHotkeys(gameName, contextPath) {
    var gameNameParam = ((gameName == '')?'':'gameName=' + gameName);
    $('body').keydown(function(ev) {
        if (ev.ctrlKey && ev.altKey && ev.keyCode == 65) { // Ctrl-Alt-A + ...
            adminKey = true;
        } else if (adminKey && ev.keyCode == 68) { // ... + D
            window.open(contextPath + 'admin31415' + (gameNameParam == ''?'':'?select&') + gameNameParam);
        } else if (adminKey && ev.keyCode == 82) { // ... + R
            window.open(contextPath + 'register' + (gameNameParam == ''?'':'?') + gameNameParam);
        } else if (adminKey && ev.keyCode == 77) { // ... + M
            window.open(contextPath);
        } else if (adminKey && ev.keyCode == 74) { // ... + J
            game.enableJoystick = !game.enableJoystick;
        } else if (adminKey && ev.keyCode == 66) { // ... + B
            window.open(contextPath + 'board/game/' + gameName);
        } else {
            adminKey = false;
        }
    });
}