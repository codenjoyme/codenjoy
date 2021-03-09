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
            var time = message.time;

            templateData.push({
                id: id,
                text: text,
                room: room,
                player: player,
                time: time
            });
        });
        $('#chat-container script')
            .tmpl(templateData)
            .appendTo('#chat-container');
    }

    function initPost() {
        $('#post-message').click(function() {
            var newMessage = $('#new-message');

            var message = newMessage.val();

            sendData('/rest/chat/' + setup.room + '/messages',
                { text : message },
                function (message) {
                    appendMessages([message]);
                });

            newMessage.val('');
        });
    }

    if (setup.enableChat && setup.authenticated) {
        var chat = $("#chat");

        loadChatMessages();
        initPost();

        chat.show();
    }
}
