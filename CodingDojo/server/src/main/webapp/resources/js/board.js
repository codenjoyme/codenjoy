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
        var url = contextPath + "screen?";

        var playersPresent = !!Object.keys(players)[0];
        if (!playersPresent) {
            allPlayersScreen = true;
        }

        var users = ((!allPlayersScreen && playersPresent) ? ("&" + players[Object.keys(players)[0]]) : "");
        return url + "allPlayersScreen=" + allPlayersScreen + users;
    }

    var updatePlayersInfo = function() {
        currentCommand = null; // for joystick.js
        $.ajax({ url:constructUrl(),
                error:function(data) {
                    $('body').css('background-color', 'bisque');
                },
                success:function (data) {
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

                    $('body').trigger("board-updated", data);
                },
                dataType:"json",
                cache:false,
                complete:updatePlayersInfo,
                timeout:30000
            });
    }

    updatePlayersInfo();
}