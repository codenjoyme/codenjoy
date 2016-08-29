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
var currentCommand = null;

function initJoystick(playerName, registered, code, contextPath, enableAlways) {
    if (!registered) {
        return;
    }

    var container = "#div_" + playerName.replace(/[@.]/gi, "_");
    var joystick = $(container + " #joystick");
    var actParams = $("#act_params");

    function visible() {
        return joystick.is(":visible");
    }

    $(container + " #player_name").click(function(){
        if (visible()) {
            joystick.hide();
        } else {
            joystick.show();
        }
    });

    function ok() {
    }

    function sendCommand(command) {
        if (!game.enableJoystick || !(enableAlways || visible())) return;
        $.ajax({ url:contextPath + "joystick",
                data:'command=' + command + '&playerName=' + playerName + "&code=" + code,
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
        }
    }

    function registerCommand(command) {
        $("#" + command).click(function() {
            var params = (actParams.val() == '')?"":("(" + actParams.val() + ")");
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
        $("body").keydown(function(event) { // TODO из за этого чат не работает +  event.preventDefault();
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
        });
        $("body").keyup(function(event) {
            var command = parseCommand(event);
            if (!!currentCommand) {
                sendCommand(currentCommand);
            } else {
                sendCommand(command);
            }
            currentCommand = null;
        });
    }

    registerCommand("up");
    registerCommand("down");
    registerCommand("left");
    registerCommand("right");
    registerCommand("act");
    registerKeys();


}