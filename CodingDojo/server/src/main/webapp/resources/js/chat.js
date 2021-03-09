/*-
 * #%L
 * Codenjoy - it's a dojo-like platform from developers to developers.
 * %%
 * Copyright (C) 2018 - 2021 Codenjoy
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
function initChat(contextPath) {

    function loadChatMessages(onLoad) {
        loadData('/rest/chat/' + setup.room + '/messages', function (messages) {
            appendMessages(messages);

            if (!!onLoad) {
                onLoad(messages);
            }
        });
    }

    function appendMessages(messages) {
        var templateData = [];
        messages.forEach(function (message) {
            var id = message.id;
            var text = message.text;
            var room = message.roomId;
            var player = message.playerId;
            var author = setup.playerName(player);
            var dateTime = getTickDateTime(message.time, true);
            var time = getTickTime(message.time, true);

            templateData.push({
                id: id,
                text: text,
                room: room,
                author: author,
                player: player,
                time: time,
                dateTime: dateTime
            });
        });
        $('#chat-container script')
            .tmpl(templateData)
            .appendTo('#chat-container');
    }

    function initPost() {
        newMessage.on('keypress', function(event) {
            if (event.which == 13) {
                if (event.ctrlKey) {
                    event.ctrlKey = false;
                    return;
                }
                event.preventDefault();
                postMessageButton.click();
            }
        });

        postMessageButton.click(function() {
            var message = newMessage.val();
            if (message == '') {
                return;
            }

            sendData('/rest/chat/' + setup.room + '/messages',
                { text : message },
                function (message) {
                    appendMessages([message]);
                    newMessage.val('');
                    newMessage.focus();
                    messages.scrollTop(messages[0].scrollHeight);
                });
        });
    }

    if (!setup.enableChat || !setup.authenticated) {
        return;
    }

    var postMessageButton = $('#post-message');
    var newMessage = $('#new-message');
    var messages = $("#chat-container");
    var chat = $("#chat");
    var chatTab = $("#chat-tab");

    loadChatMessages();
    initPost();

    chat.show();
    chatTab.show();
}