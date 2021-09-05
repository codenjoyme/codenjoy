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

function initFieldChat(contextPath) {

    var deleteMessage = async (messageId) => new Promise((resolve, reject) =>
        deleteData('/rest/chat/' + setup.room + '/messages/' + messageId,
                deleted => resolve(deleted),
                error => reject(error))
    );

    var postMessage = async (message) => new Promise((resolve, reject) =>
        sendData('/rest/chat/' + setup.room + '/messages/field',
                { text : escapeHtml(message) },
                deleted => resolve(deleted),
                error => reject(error))
    );

    var getMessages = async () => new Promise((resolve, reject) => {
        loadData('/rest/chat/' + setup.room + '/messages/field',
                messages => resolve(messages),
                error => reject(error));
    });

    async function loadChatMessages(onLoad) {
        loading = true;

        var messages = await getMessages();
        appendMessages(messages);

        loading = false;
        if (!!onLoad) {
            onLoad(messages);
        }
    }

    function appendMessages(messages) {
        var templateData = [];
        messages.forEach(function (message) {
            var id = message.id;
            var text = message.text.split('\n').join('<br>');
            var room = message.room;
            var player = message.playerId;
            var author = message.playerName;
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
        var html = $('#field-chat script').tmpl(templateData);

        var scrollHeight = getScrollHeight();
        html.find('span.delete-field-message').each(function( index ) {
            var deleteButton = $(this);
            var messageId = deleteButton.parent().attr('message');
            var message = getMessage(html, messageId);
            if (message.attr('player') != setup.playerId) {
                deleteButton.remove();
                return;
            }
            deleteButton.click(async () => {
                var deleted = await deleteMessage(messageId)
                if (deleted) {
                    message.remove();
                }
            });
        });

        // если нет сообщения рядом с которым догружать - грузим в пустой чат
        html.appendTo('#' + chatContainer.attr('id'));
        // сохраняем скролинг в той же позиции, иначе все сместится из за добавление в начало чата
        scrollTo(getScrollHeight() - scrollHeight);
    }

    function escapeHtml(data) {
        return $('<div />').text(data).html();
    }

    function scrollTo(position) {
        chatContainer.scrollTop(position);
    }

    function scrollToEnd() {
        chatContainer.scrollTop(getScrollHeight());
    }

    function getScrollHeight() {
        return chatContainer[0].scrollHeight;
    }

    function initPost() {
        newMessage.on('keydown', function(event) {
            // Enter - отправляем сообщение
            // Shift + Enter - новая линия в поле
            if (event.which == 13 && !event.shiftKey) {
                event.preventDefault();
                postMessageButton.click();
            }
        });

        postMessageButton.click(async function() {
            var message = newMessage.val();
            if (message == '') {
                return;
            }

            var message = await postMessage(message);
            appendMessages([message]);
            newMessage.val('');
            newMessage.focus();
            scrollToEnd();
        });
    }

    function getMessage(messages, messageId) {
        for (var index in messages) {
            var message = $(messages[index]);
            if (message.attr('message') == messageId) {
                return message;
            }
        }
        return null;
    }

    function getFirstMessageId() {
        return chatContainer.children('div [message]')
            .first().attr('message');
    }

    function getLastMessageId() {
        return chatContainer.children('div [message]')
            .last().attr('message');
    }

    function loadBefore(){
        let beforeId = getFirstMessageId();
        loadChatMessages(null, null, beforeId, false);
    }

    function loadAfter(){
        let afterId = getLastMessageId();
        loadChatMessages(null, afterId, null, false);
    }

    function initScrolling() {
        chatContainer.scroll(function() {
            var el = $(this);
            var scrollTop = el.scrollTop();
            var scrollHeight = el[0].scrollHeight;
            var outerHeight = el.outerHeight();
            var atChatStart = scrollTop == 0;
            var atChatEnd = (scrollHeight - scrollTop - outerHeight) < 1;
            if (atChatStart) {
                loadBefore();
            } else if (atChatEnd) {
                loadAfter();
            }
        });
    }

    function listenNewMessages() {
        $('body').bind('board-updated', function(event, data) {
            var players = Object.keys(data);
            if (players.length == 0) {
                return;
            }

            if (chat.is(':hidden')) {
                return;
            }
            if (!firstLoad) { // ###1
                firstLoad = true;
                scrollToEnd();
            }
            if (!!loading) {
                return;
            }

            // TODO получать новые сообщения из сервера
            // var realLastId = data[players[0]].lastChatMessage;
            // var lastLoadedId = getLastMessageId();
            // if (!lastLoadedId) {
            //     loadChatMessages(null, null, realLastId, true);
            // } else if (realLastId > lastLoadedId) {
            //     loadChatMessages(null, lastLoadedId, realLastId, true);
            // }
        });
    }

    if (!setup.enableChat || !setup.authenticated) {
        return;
    }

    var postMessageButton = $('#post-field-message');
    var newMessage = $('#new-field-message');
    var chatContainer = $('#field-chat-container');
    var chat = $('#field-chat');
    var chatTab = $('#field-chat-tab');

    loadChatMessages(function() {
        scrollToEnd(); // TODO почему-то оно не работает, когда чат неактивен, потому я делаю ###1
    }, null, null, null, 30); // TODO загружать 30 сообщений сразу в чат, тоже костыль, чтобы отобразился вертикальный скролинг, иначе нельзя будет грузить в прошлое

    var firstLoad = false; // ###1
    var loading = false;
    listenNewMessages();
    initPost();
    initScrolling();
    chat.show();
    chatTab.show();
}