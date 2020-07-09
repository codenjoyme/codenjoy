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
var currentCommand = null;

function initJoystick(playerId, registered, code, contextPath) {
    if (!registered) {
        return;
    }

    var container = "#div_" + playerId;
    var actParams = $("#act_params");

    function ok() {
    }

    function sendCommand(command) {
        if (!game.enableJoystick) return;

        $.ajax({ url:contextPath + "/joystick",
                data:'command=' + command + '&player=' + playerId + "&code=" + code,
                dataType:"json",
                cache:false,
                complete:ok,
                timeout:30000
            });
    }

    function parseCommand(event) {
        var keyCode = event.keyCode;
        if (keyCode == 0) {
            keyCode = event.charCode;
        }
        switch (keyCode) {
            case 38 : return "up";
            case 37 : return "left";
            case 39 : return "right";
            case 40 : return "down";
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

    function registerCommand(command) {
        $("#" + command).click(function() {
            // TODO сделать через хоткеи
            // var params = (actParams.val() == '')?"":("(" + actParams.val() + ")");
            var params = '';
            var result = command + params;
            if (currentCommand != null) { // TODO тут кажется ошибка
                if (currentCommand != "act") {
                    return;
                }
                currentCommand += "," + result;
            }
            currentCommand = result;
            sendCommand(currentCommand);
        });
    }

    function registerKeys() {
        $("body").keydown(function(event) {
            if (!game.enableJoystick) {
                return;
            }
            var command = parseCommand(event);
            if (!command) {
                return;
            }

            if (!!currentCommand) {
                if (command == 'act' && currentCommand != 'act' ||
                    command != 'act' && currentCommand == 'act')
                {
                    command = currentCommand + "," + command;
                }
            }
            currentCommand = command;

            event.preventDefault();
        });
        $("body").keyup(function(event) {
            if (!game.enableJoystick) {
                return;
            }

            var command = parseCommand(event);
            if (!command) {
                return;
            }
            if (!!currentCommand) {
                sendCommand(currentCommand);
            } else {
                sendCommand(command);
            }
            currentCommand = null;

            event.preventDefault();
        });
    }

    registerCommand("up");
    registerCommand("down");
    registerCommand("left");
    registerCommand("right");
    registerCommand("act");
    registerKeys();
}
