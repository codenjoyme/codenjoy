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

    var firstMessageInChat = null;

    function loadChatMessages(onLoad, afterId, beforeId, inclusive, count) {
        var params = '';

        // если грузили уже с таким beforeId и сообщений больше не приходило
        // значит это самое первое сообщение в чате, нефиг больше грузить
        if (!!firstMessageInChat && firstMessageInChat == beforeId) {
            return;
        }

        var ch = function() {
            return (!!params) ? '&' : '?';
        }

        if (!!afterId) {
            params += ch() + "afterId=" + afterId;
        }
        if (!!beforeId) {
            params += ch() + "beforeId=" + beforeId;
        }
        if (!!inclusive) {
            params += ch() + "inclusive=" + inclusive;
        }
        if (!!count) {
            params += ch() + "count=" + count;
        }
        loadData('/rest/chat/' + setup.room + '/messages' + params, function (messages) {
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
                return;
            }

            appendMessages(messages, messageId, after);

            if (!!onLoad) {
                onLoad(messages);
            }
        });
    }

    function appendMessages(messages, messageId, isAfterOrBefore) {
        var templateData = [];
        messages.forEach(function (message) {
            var id = message.id;
            var text = message.text.split('\n').join('<br>');
            var room = message.room;
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
        var html = $('#chat script').tmpl(templateData);

        var scrollHeight = getScrollHeight();

        var anchor = 'div[message=' + messageId + ']';
        if (!messageId || !$(anchor)[0]) {
            // если нет сообщения рядом с которым догружать - грузим в пустой чат
            html.appendTo('#chat-container');
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

        postMessageButton.click(function() {
            var message = newMessage.val();
            if (message == '') {
                return;
            }

            sendData('/rest/chat/' + setup.room + '/messages',
                { text : escapeHtml(message) },
                function (message) {
                    appendMessages([message]);
                    newMessage.val('');
                    newMessage.focus();
                    scrollToEnd();
                });
        });
    }


    function getFirstMessageId() {
        return chatContainer.children("div [message]")
            .first().attr("message");
    }

    function getLastMessageId() {
        return chatContainer.children("div [message]")
            .last().attr("message");
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
        $('body').bind("board-updated", function(event, data) {
            if (setup.playerId == '' || !data[setup.playerId]) {
                return;
            }

            var realLastId = data[setup.playerId].lastChatMessage;
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

    var postMessageButton = $('#post-message');
    var newMessage = $('#new-message');
    var chatContainer = $("#chat-container");
    var chat = $("#chat");
    var chatTab = $("#chat-tab");

    loadChatMessages(function() {
        scrollToEnd(); // TODO это почему-то не работает, разобраться
    }, null, null, null, 30);

    listenNewMessages();
    initPost();
    initScrolling();
    chat.show();
    chatTab.show();
}