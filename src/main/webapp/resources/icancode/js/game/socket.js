/*-
 * #%L
 * iCanCode - it's a dojo-like platform from developers to developers.
 * %%
 * Copyright (C) 2016 EPAM
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
function initSocket(game, buttons, console, onSocketMessage, onSocketClose) {
    var createSocket = function(url) {
        if (game.demo) {
            var count = 0;
            return {
                runMock : function() {
                    this.onopen();
                },
                send : function() {
                    if (++count > 3) {
                        count = 0;
                        buttons.enableAll();
                        return;
                    }
                    var event = {};
                    event.data = 'board={"layers":["                                                                                                     ╔════┐          ║E..S│          └────�?                                                                                                                     ","-------------------------------------------------------------------------------------------------------------------------�?�--------------------------------------------------------------------------------------------------------------------------------------"], "levelProgress":{"total":18,"current":3,"lastPassed":2,"multiple":false}}';
                    this.onmessage(event);
                }
            }
        } else {
            return new WebSocket(url);
        }
    }

    var connect = function(onSuccess) {
        var hostIp = window.location.hostname;
        var port = window.location.port;
        var server = 'ws://' + hostIp + ':' + port + '/codenjoy-contest/ws';

        console.print('Connecting to Robot...');
        socket = createSocket(server + '?user=' + game.playerName);

        socket.onopen = function() {
            console.print('...connected successfully!');
            console.printHello();
            if (!!onSuccess) {
                onSuccess();
            }
        }

        socket.onclose = function(event) {
            var reason = ((!!event.reason)?(' reason: ' + event.reason):'');
            console.print('Signal lost! Code: ' + event.code + reason);
            socket = null;

            onSocketClose();
        }

        socket.onmessage = function(event) {
            var data = event.data;
            onSocketMessage(data);
        }

        socket.onerror = function(error) {
            error(error);
            socket = null;
        }

        if (game.demo) {
            socket.runMock();
        }
    }

    var replace = function(string, from, to) {
        return string.split(from).join(to);
    }

    var encode = function(command) {
        command = replace(command, 'WAIT', '');
        command = replace(command, 'JUMP', 'ACT(1)');
        command = replace(command, 'PULL', 'ACT(2)');
        command = replace(command, 'RESET', 'ACT(0)');
        while (command.indexOf('LEVEL') != -1) {
            var level = command.substring(command.indexOf('LEVEL') + 'level'.length);
            command = replace(command, 'LEVEL' + level, 'ACT(0,' + (level - 1) + ')');
        }
        command = replace(command, 'WIN', 'ACT(-1)');
        return command;
    }

    var send = function(command) {
        command = encode(command);
        if (socket == null) {
            connect(function() {
                socket.send(command);
            });
        } else {
            socket.send(command);
        }
    }

    return {
        send : send,
        connect : connect
    }
}