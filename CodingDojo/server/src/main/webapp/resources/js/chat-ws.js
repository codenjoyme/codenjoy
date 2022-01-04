/*-
 * #%L
 * Codenjoy - it's a dojo-like platform from developers to developers.
 * %%
 * Copyright (C) 2012 - 2022 Codenjoy
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
function initChatWebSocket(room, playerId, code, contextPath, onConnect) {
    var constructUrl = function() {
        var link = document.createElement('a');
        link.setAttribute('href', window.location.href);
        return getWSProtocol() + "://" + link.hostname + ':' + link.port + contextPath +
            "/chat-ws?user=" + playerId + "&code=" + code;
    }

    var control = {
        send : function(command, data) {
            socket.send("{'command':'" + command + "', " +
                "'data':" + JSON.stringify(data) + "}");
        },

        addListener : function(listener) {
            listeners.push(listener);
        }
    };

    var socket = null;
    var reconnectOnError = function() {
        setTimeout(function(){
            connectToServer();
        }, 5000);
    }
    var connectToServer = function() {
        try {
            socket = new WebSocket(constructUrl());
        } catch (err) {
            console.log(err);
        }
        socket.onopen = function() {
            if (!!onConnect) {
                onConnect(control);
            }
        };
        socket.onclose = function() {
            reconnectOnError();
        };
        socket.onerror = function() {
            // reconnectOnError();
        };
        socket.onmessage = function(message) {
            var data = JSON.parse(message.data);

            $('body').trigger('chat-updated', data);

            notifyListeners(data);
        };
    }

    var listeners = [];

    var notifyListeners = function(data) {
        if (socket == null) {
            return;
        }

        for (var index in listeners) {
            var listener = listeners[index];
            if (!!listener) {
                listener(data);
            }
        }
    }

    connectToServer();
}
