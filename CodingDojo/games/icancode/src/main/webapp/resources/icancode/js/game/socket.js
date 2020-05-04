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
function initSocket(game, buttons, logger, onSocketMessage, onSocketClose) {

    if (game.debug) {
        game.debugger();
    }

    var createSocket = function(url) {
        var sleepFor = function(sleepDuration){
            var now = new Date().getTime();
            while (new Date().getTime() < now + sleepDuration){
                /* do nothing */
            }
        }

        if (game.demo) {
            var count = 0;
            return {
                runMock : function() {
                    this.onopen();
                },
                send : function(command) {
                    if (++count > 2) {
                        count = 0;
                        buttons.enableAll();
                        return;
                    }
                    var event = {};
                    event.data = 'board={"layers":["                                                                                                     ╔════┐          ║S..E│          └────┘                                                                                                                     ","----------------------------------------------------------------------------------------------------------------------☺-----------------------------------------------------------------------------------------------------------------------------------------"], "levelProgress":{"current":0,"lastPassed":10,"total":14,"multiple":false}}';
                    var that = this;
                    setTimeout(function() {
                        that.onmessage(event);
                    }, 1000);
                }
            }
        } else {
            return new WebSocket(url);
        }
    }

    var connect = function(onSuccess) {
        var hostIp = window.location.hostname;
        var port = window.location.port;
        var server = getWSProtocol() + '://' + hostIp + ':' + port + '/codenjoy-contest/ws';

        logger.print('Connecting to Hero...');
        socket = createSocket(server + '?user=' + game.playerId + "&code=" + game.code);

        socket.onopen = function() {
            logger.print('...connected successfully!');
            logger.printHello();
            if (!!onSuccess) {
                onSuccess();
            }
        }

        socket.onclose = function(event) {
            var reason = ((!!event.reason)?(' reason: ' + event.reason):'');
            logger.print('Signal lost! Code: ' + event.code + reason);
            socket = null;

            onSocketClose();
        }

        socket.onmessage = function(event) {
            var data = event.data;
            onSocketMessage(data);
        }

        socket.onerror = function(error) {
            logger.error(error, false);
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
        command = replace(command, 'WAIT', 'STOP');
        command = replace(command, 'JUMP', 'ACT(1)');
        command = replace(command, 'PULL', 'ACT(2)');
        command = replace(command, 'FIRE', 'ACT(3)');
        command = replace(command, 'RESET', 'ACT(0)');
        command = replace(command, 'WIN', 'ACT(-1)');
        return command;
    }

    var send = function(command) {
        command = encode(command);
        if (socket == null) {
            setTimeout(function() {
                connect(function() {
                    socket.send(command);
                });
            }, 5000);
        } else {
            socket.send(command);
        }
    }

    return {
        send : send,
        connect : connect
    }
}