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

var ROOM_TYPE = 'room';
var FIELD_TYPE = 'field';

function initChat(contextPath, type) {

    var root = $('.id-' + type + '-chat ');
    var urlSuffix = (type == ROOM_TYPE) ? '' : '/field';

    var deleteMessage = async (messageId) => new Promise((resolve, reject) =>
        deleteData('/rest/chat/' + setup.room + '/messages/' + messageId,
                deleted => resolve(deleted),
                error => reject(error))
    );

    var postMessage = async (message) => new Promise((resolve, reject) =>
        sendData('/rest/chat/' + setup.room + '/messages' + urlSuffix,
                { text : escapeHtml(message) },
                deleted => resolve(deleted),
                error => reject(error))
    );

    function buildParams(afterId, beforeId, inclusive, count) {
        var ch = () => (!!result) ? '&' : '?';

        var result = '';
        if (!!afterId) {
            result += ch() + 'afterId=' + afterId;
        }
        if (!!beforeId) {
            result += ch() + 'beforeId=' + beforeId;
        }
        if (!!inclusive) {
            result += ch() + 'inclusive=' + inclusive;
        }
        if (!!count) {
            result += ch() + 'count=' + count;
        }
        return result;
    }

    var getMessages = async (afterId, beforeId, inclusive, count) => new Promise((resolve, reject) => {
        var params = buildParams(afterId, beforeId, inclusive, count);
        loadData('/rest/chat/' + setup.room + '/messages' + urlSuffix + params,
                messages => resolve(messages),
                error => reject(error));
    });

    var firstMessageInChat = null;

    async function loadChatMessages(onLoad, afterId, beforeId, inclusive, count) {
        loading = true;

        // если грузили уже с таким beforeId и сообщений больше не приходило
        // значит это самое первое сообщение в чате, нефиг больше грузить
        if (!!firstMessageInChat && firstMessageInChat == beforeId) {
            loading = false;
            return;
        }

        var messages = await getMessages(afterId, beforeId, inclusive, count);
        var messageId = null;
        var after = true;
        if (!!afterId) {
            messageId = afterId;
            after = true;
        } else if (!!beforeId) {
            messageId = beforeId;
            after = false;
        }

        // когда мы грузим в диапазоне значений, это мы догружаем новые сообщения
        // нам нужно подгрузить в чат (afterId, beforeId] но пришло [afterId, beforeId]
        // потому и удаляем afterId который у нас уже есть в чате
        if (inclusive && !!afterId && !!beforeId) {
            messages.shift();
        }

        if (messages.length == 0) {
            // если ничего не пришло и грузим мы начало чата
            // значит это самое первое сообщение в чате - больше его загружать не будем
            if (!after && !firstMessageInChat) {
                firstMessageInChat = messageId;
            }
            loading = false;
            return;
        }

        appendMessages(messages, messageId, after);

        loading = false;
        if (!!onLoad) {
            onLoad(messages);
        }
    }

    function appendMessages(messages, messageId, isAfterOrBefore) {
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
        var html = root.find('.chat script').tmpl(templateData);

        var scrollHeight = getScrollHeight();
        html.find('span.delete-message').each(function( index ) {
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

        var anchor = 'div[message=' + messageId + ']';
        if (!messageId || !root.find(anchor)[0]) {
            // если нет сообщения рядом с которым догружать - грузим в пустой чат
            html.appendTo(chatContainer);
            // сохраняем скролинг в той же позиции, иначе все сместится из за добавление в начало чата
            scrollTo(getScrollHeight() - scrollHeight);
        } else if (isAfterOrBefore) {
            html.insertAfter(anchor);
            // тут скролинг не смещается, потому что аппенится в конце
        } else {
            html.insertBefore(anchor);
            // сохраняем скролинг в той же позиции, иначе все сместится из за добавление в начало чата
            scrollTo(getScrollHeight() - scrollHeight);
        }
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

            var realLastId = data[players[0]].lastChatMessage;
            var lastLoadedId = getLastMessageId();
            if (!lastLoadedId) {
                loadChatMessages(null, null, realLastId, true);
            } else if (realLastId > lastLoadedId) {
                loadChatMessages(null, lastLoadedId, realLastId, true);
            }
        });
    }

    if (!setup.enableChat || !setup.authenticated) {
        return;
    }

    var postMessageButton = root.find('.id-post-message');
    var newMessage = root.find('.id-new-message');
    var chatContainer = root.find('.id-chat-container');
    var chat = root.find('.chat');
    var chatTab = root.find('#' + type + '-chat-tab');

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