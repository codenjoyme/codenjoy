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
function initBoards(players, allPlayersScreen, gameName, contextPath){

    var constructUrl = function() {
        var link = document.createElement('a');
        link.setAttribute('href', window.location.href);
        return "ws://" + link.hostname + ':' + link.port + contextPath + "/screen-ws?user=apofig@gmail.com";
    }

    var socket = null;
    var connectToServer = function() {
        currentCommand = null; // for joystick.js

        socket = new WebSocket(constructUrl());
        socket.onopen = function() {
            updatePlayersInfo();
        };
        socket.onclose = function() {
            // do nothing
        };
        socket.onerror = function() {
            $('body').css('background-color', 'bisque');
            // TODO после этого сразу же отправляется второй запрос, и если серчер отключен то мы имеем купу ошибок js в консоли. Надо сделать так, чтобы при ошибке повторный запро отправлялся через секунду
        };
        socket.onmessage = function(message) {
            var data = JSON.parse(message.data);

            $('body').css('background-color', 'white');

            // TODO:1 Вот тут надо вообще другим запросом чат брать из другого скрина, чтобы тут им и не пахло
            if (chatLog == null) { // uses for chat.js
                chatLog = data['#CHAT'].messages;
            }
            delete data['#CHAT'];

            if (!!gameName) {  // TODO вот потому что dojo transport не делает подобной фильтрации - ее приходится делать тут.
                var filtered = {};
                for (var key in data) {
                    if (data[key].gameName == gameName) {
                        filtered[key] = data[key];
                    }
                }

                data = filtered;
            }

            $('body').trigger('board-updated', data);

            updatePlayersInfo();
        };
    }

    var updatePlayersInfo = function() {
        if (socket == null) {
            return;
        }

        var playerNames = [];
        for (var index in players) {
            var playerName = players[index].name;
            playerNames.push(playerName);
        }

        var request = {
            'name':'getScreen',
            'allPlayersScreen' : players.length == 0,
            'players' : playerNames
        }

        socket.send(JSON.stringify(request));
    }

    connectToServer();
}