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

function initChat(contextPath, chatControl, type) {

    if (!setup.enableChat || !setup.authenticated) {
        return;
    }

    var root = $('.id-' + type + '-chat ');
    var firstMessageInChat = null;

    // в случае если это field-чат тут будем хранить
    // field id которое придет с первым сообщением к нам
    // или с обновлением тика
    var fieldId = null;

    var setupScroll = function(element) {
        var pos = null;

        function height() {
            return element[0].scrollHeight;
        }

        function out() {
            return element.outerHeight();
        }

        function top() {
            return element.scrollTop();
        }

        function atEnd() {
            // 1 потому что разница в дробные части пикселя
            return (height() - (top() + out())) < 1;
        }

        function atStart() {
            return top() == 0;
        }

        function scroll(position) {
            element.scrollTop(position);
        }

        function scrollStart() {
            scroll(0);
        }

        function scrollEnd() {
            scroll(height());
        }

        function save() {
            pos = height();
        }

        function load() {
            if (pos == null) {
                return;
            }
            scroll(height() - pos);
            pos = null;
        }

        return {
            height : height,
            out : out,
            top : top,
            atEnd : atEnd,
            atStart : atStart,
            scroll : scroll,
            scrollEnd : scrollEnd,
            scrollStart : scrollStart,
            save : save,
            load : load
        }
    }

    var deleteMessage = function(messageId) {
        chatControl.send('delete', {
            id : messageId,
            room : setup.room
        });
    }

    var postMessage = function(message) {
        var command = (type == ROOM_TYPE) ? 'postRoom' : 'postField';
        chatControl.send(command, {
            text : message,
            room : setup.room
        });
    }

    var getMessages = function(afterId, beforeId, inclusive, count) {
        var command = (type == ROOM_TYPE) ? 'getAllRoom' : 'getAllField';
        chatControl.send(command, {
            afterId : afterId,
            beforeId : beforeId,
            inclusive : inclusive,
            count : count,
            room : setup.room
        });
    }

    function loadChatMessages(afterId, beforeId, inclusive) {
        loading = true;

        // если грузили уже с таким beforeId и сообщений больше не приходило
        // значит это самое первое сообщение в чате, нефиг больше грузить
        if (!!firstMessageInChat && firstMessageInChat == beforeId) {
            loading = false;
            return;
        }

        // TODO загружать 30 сообщений сразу в чат,
        //      чтобы отобразился вертикальный скроллинг, иначе
        //      нельзя будет грузить в прошлое
        var count = 30;
        getMessages(afterId, beforeId, inclusive, count);
    }

    function onDeleteMessages(messages) {
        var message = messages[0]; // TODO сделать возможным удаление сразу несколько

        var elements = chatContainer.find(messageWith(message.id));
        if (elements.length == 0) {
            return;
        }
        elements.first().remove();
    }

    function nearestFor(search, all, order) {
        var min = null;
        var found = null;
        all = (order) ? all : all.reverse();
        for (var index in all) {
            var value = all[index];
            if (min == null || Math.abs(search - value) < min) {
                min = Math.abs(search - value);
                found = value;
            }
        }
        return found;
    }

    function onLoadMessages(messages) {
        do {
            // TODO разобраться с этим т.к. к нам никогда не придет пустой список
            if (messages.length == 0) {
                // // если ничего не пришло и грузим мы начало чата
                // // значит это самое первое сообщение в чате - больше его загружать не будем
                // if (!after && !firstMessageInChat) {
                //     firstMessageInChat = messageId;
                // }
                loading = false;
                return;
            }

            var first = messages[0].id;
            var last = messages[messages.length - 1].id;
            var all = allMessagesIds();

            var after = nearestFor(first, all, true);
            var before = nearestFor(last, all, false);

            // удаляем дубликаты, которые у нас уже в чате есть
            var removeAfter = after == first;
            if (removeAfter) {
                messages.shift();
            }

            // удаляем дубликаты, которые у нас уже в чате есть
            var removeBefore = before == last;
            if (removeBefore) {
                messages.pop();
            }
        } while (removeAfter || removeBefore);

        // если после этого сообщений больше не осталось, то заканчиваем тут
        if (messages.length == 0) {
            loading = false;
            return;
        }

        var afterOrBefore = null;
        var messageId = null;
        if (after == null && before == null) {
            // чат пустой, просто добавим как первые сообщения
        } else if (after == null && before != null) {
            // добавляем новые сообщения перед before
            afterOrBefore = false;
            messageId = before;
        } else if (after != null && before == null) {
            // добавляем новые сообщения после after
            afterOrBefore = true;
            messageId = after;
        } else if (after != null && before != null) {
            // тут не понятно куда добавлять,
            // надо смотреть что реально пришло
            if (first > after) {
                // добавляем новые сообщения после after
                afterOrBefore = true;
                messageId = before;
            } else if (last < before) {
                // добавляем новые сообщения перед before
                afterOrBefore = false;
                messageId = before;
            }
        }

        // проверим а не надо ли перегрузить весь чат?
        var topicId = messages[0].topicId;
        if (needToReloadChat(topicId)) {
            loading = false;
            return;
        }

        var needScroll = chatScroll.atEnd();

        appendMessages(messages, messageId, afterOrBefore);

        if (needScroll) {
            chatScroll.scrollEnd();
        }

        loading = false;
    }

    chatControl.addListener(function(request) {
        if (request.type != type) {
            // не наш чат, не обрабатываем
            return;
        }
        var command = request.command;
        if (command == 'add') {
            var messages = request.data;
            onLoadMessages(messages);
        } else if (command == 'delete') {
            var messages = request.data;
            onDeleteMessages(messages);
        } else if (command == 'error') {
            console.log(request);
        }
    });

    var SYSTEM_ID = '-1';

    // с помощью этого малого мы сможем печатать
    // в чат банальные логи
    function appendText(string) {
        var message = {
            id : SYSTEM_ID,
            text : string,
            room : null,
            playerId : 0,
            playerName : 'system',
            time : new Date().getTime()
        };
        appendMessages([message]);
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

        chatScroll.save();
        html.find('span.delete-message').each(function( index ) {
            var deleteButton = $(this);
            var messageId = id(deleteButton.parent());
            var message = getMessage(html, messageId);
            if (message.attr('player') != setup.playerId) {
                deleteButton.remove();
                return;
            }
            deleteButton.click(async () => {
                deleteMessage(messageId);
            });
        });

        var anchor = messageWith(messageId);
        if (!messageId || !root.find(anchor)[0]) {
            // если нет сообщения рядом с которым догружать - грузим в пустой чат
            html.appendTo(chatContainer);
            // сохраняем скроллинг в той же позиции, иначе все
            // сместится из за добавление в начало чата
            chatScroll.load();
        } else if (isAfterOrBefore) {
            html.insertAfter(anchor);
            // тут скролинг не смещается, потому что аппенится в конце
        } else {
            html.insertBefore(anchor);
            // сохраняем скроллинг в той же позиции, иначе все
            // сместится из за добавление в начало чата
            chatScroll.load();
        }
    }

    function escapeHtml(data) {
        return $('<div />').text(data).html();
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

            var message = postMessage(message);
            newMessage.val('');
            newMessage.focus();
        });
    }

    function getMessage(messages, messageId) {
        for (var index in messages) {
            var message = $(messages[index]);
            if (id(message) == messageId) {
                return message;
            }
        }
        return null;
    }

    // с помощью этого селектора мы пропускаем все системные
    // сообщения (логи), которые напечатаны с id=SYSTEM_ID
    function userMessages() {
        return "div[message!='" + SYSTEM_ID + "']";
    }

    // с помощью этого селектора мы получаем сообщение с заданой id
    function messageWith(id) {
        return "div[message='" + id + "']";
    }

    // возвращает id всех сообщений на данный момент загруженных в чат
    function allMessagesIds() {
        return chatContainer.children(userMessages())
            .map(function() {
                return id($(this));
            })
            .get();
    }

    function getFirstMessageId() {
        return id(chatContainer.children(userMessages()).first());
    }

    function getLastMessageId() {
        return id(chatContainer.children(userMessages()).last());
    }

    function id(element) {
        return parseInt(element.attr('message'));
    }

    function loadBefore(){
        let beforeId = getFirstMessageId();
        loadChatMessages(null, beforeId, false);
    }

    function loadAfter(){
        let afterId = getLastMessageId();
        loadChatMessages(afterId, null, false);
    }

    function initScrolling() {
        chatContainer.scroll(function() {
            var scroll = setupScroll($(this));
            if (scroll.atStart()) {
                loadBefore();
            } else if (scroll.atEnd()) {
                loadAfter();
            }
        });
    }

    // метод начальной загрузки пустого чата
    var loadChat = function() {
        loadChatMessages(null, null, null);
    }

    // проверяем, надо ли обновить весь чат
    // актуально, когда field чат использовался,
    // но уже игрок покинул field
    var needToReloadChat = function(id) {
        if (type != FIELD_TYPE || fieldId == id) {
            // не, это не field чат
            // или юзер еще не покидал эту field
            return false;
        }

        // а вот тут мы инвалидируем чат, чистим и грузим новый контент
        fieldId = id;
        chatContainer.empty();
        appendText("Field '" + id + "' created");
        loadChat();

        // сообщаем там на верху, чтобы не продолжали то
        // что делали раньше - неактуально уже
        return true;
    }

    var postMessageButton = root.find('.id-post-message');
    var newMessage = root.find('.id-new-message');
    var chatContainer = root.find('.id-chat-container');
    var chatScroll = setupScroll(chatContainer);
    var chat = root.find('.chat');
    var chatTab = root.find('#' + type + '-chat-tab');

    var loading = false;
    loadChat();
    initPost();
    initScrolling();
    chat.show();
    chatTab.show();
}