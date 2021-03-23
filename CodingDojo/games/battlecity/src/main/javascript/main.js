/*-
 * #%L
 * Codenjoy - it's a dojo-like platform from developers to developers.
 * %%
 * Copyright (C) 2018 - 2020 Codenjoy
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
 var checkVisibility = function(checkbox, component) {
    $(checkbox).change(function() {
        if ($(this).is(":checked")) {
            $(component).css('visibility', 'visible');
        } else {
            $(component).css('visibility', 'hidden');
        }
    });
}

var ws = null;

var apply = function(isConnect) {
    if (isConnect) {
         ws = connect();

         ws.on('open', function() {
            $('#client-connect').prop('checked', true);
         });

         ws.on('close', function() {
            $('#client-connect').prop('checked', false);
         });
    } else {
         if (!!ws) {
            ws.close();
            ws = null;
         }
    }
}

var applyCheck = function(isConnect) {
    $('#client-connect').prop('checked', isConnect);
    apply(isConnect);
}

var _connect = function() {
    applyCheck(true);
}

var _disconnect = function() {
    applyCheck(false);
}

var parseCommand = function(event) {
    var keyCode = event.keyCode;
    if (keyCode == 0) {
        keyCode = event.charCode;
    }
    var actPre = (event.shiftKey | event.ctrlKey) ? "act," : "";
    var actPost = event.altKey ? ",act" : "";
    switch (keyCode) {
        case 38 : return actPre + "up" + actPost;
        case 37 : return actPre + "left" + actPost;
        case 39 : return actPre + "right" + actPost;
        case 40 : return actPre + "down" + actPost;
        case 8  : return "stop";
        case 13 : return "stop";
        case 32 : return "act";
        default : {
            if (keyCode >= 48 && keyCode <= 57) {
                return "act(" + (keyCode - 48) + ")"
            } else {
                return null;
            }
        };
    }
}

var onKeyDown = function(event) {
    if (!isServerConnected() || !isJoystickEnabled()) {
        return;
    }
    var command = parseCommand(event);
    if (!command) {
        return;
    }

    if (!!boardData) {
        sendSockets = true;
        ws.send(command);
        sendSockets = false;
    }

    event.preventDefault();
};

var isJoystickEnabled = function() {
    return $('#joystick').prop('checked');
}

var isServerConnected = function() {
    return $('#client-connect').prop('checked');
}

var joystickEnableDisable = function() {
    if (isJoystickEnabled()) {
        sendSockets = false;
    } else {
        sendSockets = true;
        if (isServerConnected()) {
            ws.send(processBoard(parseBoard(boardData)));
        }
    }
}

$(document).ready(function() {
    checkVisibility('#show-graphic', '#board-canvas');
    checkVisibility('#show-text', '#board');

    $('#client-connect').change(function() {
        apply(this.checked);
    });
    _connect();

    $('#joystick').prop('checked', false);
    $('#joystick').change(joystickEnableDisable);

    $("body").keydown(onKeyDown);
});

