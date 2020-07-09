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
var getWSProtocol = function() {
    if (window.location.protocol == "https:") {
        return "wss";
    } else {
        return "ws";
    }
}

function initBoards(players, allPlayersScreen, gameName, playerId, contextPath){
    var constructUrl = function() {
        var link = document.createElement('a');
        link.setAttribute('href', window.location.href);
        var user = (playerId == null) ? "anonymous" : playerId;
        return getWSProtocol() + "://" + link.hostname + ':' + link.port + contextPath + "/screen-ws?user=" + user;
    }

    var socket = null;
    var reconnectOnError = function() {
        $('body').css('background-color', 'bisque');

        setTimeout(function(){
            connectToServer();
        }, 5000);
    }
    var connectToServer = function() {
        currentCommand = null; // for joystick.js

        try {
            socket = new WebSocket(constructUrl());
        } catch (err) {
            console.log(err);
        }
        socket.onopen = function() {
            updatePlayersInfo();
        };
        socket.onclose = function() {
            reconnectOnError();
        };
        socket.onerror = function() {
//            reconnectOnError();
        };
        socket.onmessage = function(message) {
            var data = JSON.parse(message.data);

            $('body').css('background-color', 'white');

            $('body').trigger('board-updated', data);

            updatePlayersInfo();
        };
    }

    var updatePlayersInfo = function() {
        if (socket == null) {
            return;
        }

        var ids = [];
        for (var index in players) {
            var id = players[index].id;
            ids.push(id);
        }

        var request = {
            'name':'getScreen',
            'allPlayersScreen' : allPlayersScreen,
            'players' : ids,
            'gameName' : gameName
        }

        socket.send(JSON.stringify(request));
    }

    connectToServer();
}
