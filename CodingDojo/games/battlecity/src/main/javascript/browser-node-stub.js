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
var browser = true;
var module = {};

var getSize = function(data) {
    if (!!data.layers) {
        return Math.sqrt(data.layers[0].length);
    }
    return data.split('\n')[0].length;
}

var chunk = function(str, n) {
    var ret = [];
    var i;
    var len;

    for(i = 0, len = str.length; i < len; i += n) {
        ret.push(str.substr(i, n))
    }

    return ret
};

var printBoardOnTextArea = function(data) {
    if (typeof(onBoardData) != 'undefined' && !!onBoardData) {
        onBoardData(data);
    }

    var textarea = document.getElementById('board');
    if (!textarea) return;
    var size = getSize(data);
    textarea.cols = size;
    textarea.rows = size;

    if (!!data.layers) {
        data = chunk(data.layers[0], size).join('\n');
    }
    textarea.value = data;
}

var cache = [];

var printLogOnTextArea = function(data) {
    var textarea = document.getElementById('log-area');
    var addToEnd = document.getElementById('add-to-end');
    if (!textarea) return;
    if (addToEnd.checked) {
        cache.push(data);
        if (cache.length > 30) {
            cache.shift()
        }
    } else {
        cache.unshift(data);
        if (cache.length > 30) {
            cache.pop()
        }
    }

    var all = '';
    for (var i in cache) {
        var data = cache[i];
        all = all + '\n' + data;
    }
    textarea.value = all;
}

var sendSockets = true;
var boardData = false;

var require = function(name) {
    if (name.startsWith('./games/')) {
        return;
    }

    name = Stuff.clean(name);

    if (name == 'stuff') {
        return Stuff;
    } else if (name == 'solver') {
        return Solver;
    } else if (name == 'direction') {
        return Direction;
    } else if (name == 'lxy') {
        return LengthToXY;
    } else if (name == 'point') {
        return Point;
    } else if (name == 'games') {
        return Games;
    } else if (name == 'util') {
        return {
            // thanks to http://stackoverflow.com/a/4673436
            'format': function(format) {
                var args = Array.prototype.slice.call(arguments, 1);
                var number = -1;
                return format.replace(/%s/g, function(match) {
                    number++;
                    return typeof args[number] != 'undefined'
                        ? args[number]
                        : match
                    ;
                });
            }
        }
    } else if (name == 'ws') {
        return function(uri) {
            var socket = new WebSocket(uri);
            var isSend = true;
            return {
                'on' : function(name, callback) {
                    if (name == 'open') {
                        socket.onopen = callback;
                    } else if (name == 'close') {
                        socket.onclose = callback;
                    } else if (name == 'error') {
                        socket.onerror = callback;
                    } if (name == 'message') {
                        socket.onmessage = function(message) {
                            boardData = message.data;
                            callback(boardData);
                        }
                    }
                },
                'send' : function(message) {
                    if (sendSockets) {
                        boardData = null;
                        socket.send(message);
                    }
                },
                'close' : function(message) {
                    socket.close();
                }
            }
        }
    }
}
