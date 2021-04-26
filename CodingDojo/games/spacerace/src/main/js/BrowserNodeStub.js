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

var module;

var isLogging=true;
var isScrolling=true;

function setLogging(){
    isLogging=!isLogging;
}

function setScrolling(){
    isScrolling = ! isScrolling;
}

function clearLog() {
    var textarea = document.getElementById("log-area");
    textarea.value="";
}

var ElementsForUI = {
    HERO: '☺',
    OTHER_HERO:'☻',
    DEAD_HERO:'+',
    BULLET_PACK: '7',
    STONE: '0',
    BOMB: '♣',
    EXPLOSION:'x',
    BULLET:'*',
    WALL: '☼',

    /// a void
    NONE: ' '                  
};


var classNames = function(){
    var results = [];
    var keys = Object.keys(ElementsForUI);
    for(k in keys){
        var key = keys[k];
        results[ElementsForUI[key]] = key;
    }
    return results;
  }();
  
  function getClassNameByElement(element){
     return classNames[element];
  }

  function printAnswer(answer){
      var span=document.getElementById('lastmove');
      span.innerHTML = answer;
  }
  
  var drawBoard=function(board){
      var table = document.createElement('table');
      table.id = "table-game";
      for (var y = board.size() - 1; y >=0; y--) {
          var row = document.createElement('tr');
          for (var x = 0; x < board.size(); x++) {
              var cell = document.createElement('td');
              cell.className = getClassNameByElement(board.getAt(x,y));
              row.appendChild(cell);
          }
          table.appendChild(row);
      }
      var tableDiv = document.getElementById('board-table');
      var tableItself = document.getElementById('table-game');
  
      tableItself ?
          tableDiv.replaceChild(table, tableItself) :
          tableDiv.appendChild(table);
  }
  
var printBoardOnTextArea = function(data) {
    var textarea = document.getElementById("board");
    if (!textarea) return;
    var size = data.split('\n')[0].length;
    textarea.cols = size;
    textarea.rows = size + 1;
    textarea.value = data;
}

var cache = [];

var printLogOnTextArea = function(data) {
    if(!isLogging) return;
    var textarea = document.getElementById("log-area");
    if (!textarea) return;
    var val = textarea.value;
    if(val) {
        textarea.value = val + '\n' + data;
    } else {
        textarea.value = data;
    }

    if(isScrolling) {
        textarea.scrollTop = textarea.scrollHeight;
    }
}

var require = function(string) {
    if (string == 'util') {
        return {
            // thanks to http://stackoverflow.com/a/4673436
            "format":function(format) {
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
    } else if (string == 'ws') {
        return function(uri) {
            var socket = new WebSocket(uri);
            return {
                "on" : function(name, callback) {
                    if (name == "open") {
                        socket.onopen = callback;
                    } else if (name == "close") {
                        socket.onclose = callback;
                    } else if (name == "error") {
                        socket.onerror = callback;
                    } if (name == "message") {
                        socket.onmessage = function(message) {
                            callback(message.data);
                        }
                    }
                }, 
                "send" : function(message) {
                    socket.send(message);
                }
            }
        }
    }
};
