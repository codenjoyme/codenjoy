function runProgram(program, robot) {
    program(robot);
}

// ========================== game setup ==========================

if (typeof game == 'undefined') {
    game = {};
    game.demo = true;
    game.code = 123;
    game.playerName = 'user@gmail.com';
    initLayout = function(game, html, context, transformations, scripts, onLoad) {
        onLoad();
    }
}

game.enableDonate = false;
game.enableJoystick = false;
game.enableAlways = true;
game.enablePlayerInfo = false;
game.enableLeadersTable = false;
game.enableChat = false;
game.enableInfo = false;
game.enableHotkeys = true;
game.enableAdvertisement = false;
game.showBody = false;

// ========================== autocomplete ==========================
var icancodeMaps = {};
var icancodeWordCompleter = {
    getCompletions: function(editor, session, pos, prefix, callback) {
        var line = editor.session.getLine(pos.row);
        var isFind = false;

        for(var index in icancodeMaps) {
            var startFindIndex = pos.column - index.length;

            if (startFindIndex >= 0 && line.substring(startFindIndex, pos.column) == index) {
                isFind = true;
                break;
            }
        }

        if (!isFind) {
            return;
        }

        callback(null, icancodeMaps[index].map(function(word) {
            return {
                caption: word,
                value: word,
                meta: "game",
                score: 1000
            };
        }));

    }
}

var changeLevel = function(value) {
    currentLevel = value;

    // ------------------- get autocomplete -------------------
    initAutocomplete(currentLevel + 1);
};

var initAutocomplete = function(level) {
    var data;
    icancodeMaps = {};

    for(var iLevel = 0; iLevel <= level; ++iLevel) {
        data = autocompleteValues[iLevel];

        if (!autocompleteValues.hasOwnProperty(iLevel)) {
            continue;
        }

        for(var index in data) {
            if (icancodeMaps.hasOwnProperty(index)) {
                icancodeMaps[index] = icancodeMaps[index].concat(data[index].values);
            } else {
                icancodeMaps[index] = data[index].values;
            }

            for(var isynonym = 0; isynonym <= data[index].synonyms.length; ++isynonym) {
                icancodeMaps[data[index].synonyms[isynonym]] = icancodeMaps[index];
            }
        }
    }
};

// ========================== elements ==========================

var el = function(char, type) {
    return {
        char : char,
        type : type
    }
}

var Element = {
    EMPTY : el('-', 'NONE'),
    FLOOR : el('.', 'NONE'),

    ANGLE_IN_LEFT : el('╔', 'WALL'),
    WALL_FRONT : el('═', 'WALL'),
    ANGLE_IN_RIGHT : el('┐', 'WALL'),
    WALL_RIGHT : el('│', 'WALL'),
    ANGLE_BACK_RIGHT : el('┘', 'WALL'),
    WALL_BACK : el('─', 'WALL'),
    ANGLE_BACK_LEFT : el('└', 'WALL'),
    WALL_LEFT : el('║', 'WALL'),
    WALL_BACK_ANGLE_LEFT : el('┌', 'WALL'),
    WALL_BACK_ANGLE_RIGHT : el('╗', 'WALL'),
    ANGLE_OUT_RIGHT : el('╝', 'WALL'),
    ANGLE_OUT_LEFT : el('╚', 'WALL'),
    SPACE : el(' ', 'WALL'),

    ROBOT : el('☺', 'MY_ROBOT'),
    ROBOT_FALLING : el('o', 'HOLE'),
    ROBOT_FLYING : el('*', 'MY_ROBOT'),
    ROBOT_LASER : el('☻', 'MY_ROBOT'),

    ROBOT_OTHER : el('X', 'OTHER_ROBOT'),
    ROBOT_OTHER_FALLING : el('x', 'HOLE'),
    ROBOT_OTHER_FLYING : el('^', 'OTHER_ROBOT'),
    ROBOT_OTHER_LASER : el('&', 'OTHER_ROBOT'),

    LASER_MACHINE_CHARGING_LEFT : el('˂', 'LASER_MACHINE'),
    LASER_MACHINE_CHARGING_RIGHT : el('˃', 'LASER_MACHINE'),
    LASER_MACHINE_CHARGING_UP : el('˄', 'LASER_MACHINE'),
    LASER_MACHINE_CHARGING_DOWN : el('˅', 'LASER_MACHINE'),

    LASER_MACHINE_READY_LEFT : el('◄', 'LASER_MACHINE_READY'),
    LASER_MACHINE_READY_RIGHT : el('►', 'LASER_MACHINE_READY'),
    LASER_MACHINE_READY_UP : el('▲', 'LASER_MACHINE_READY'),
    LASER_MACHINE_READY_DOWN : el('▼', 'LASER_MACHINE_READY'),

    LASER_LEFT : el('←', 'LASER_LEFT'),
    LASER_RIGHT : el('→', 'LASER_RIGHT'),
    LASER_UP : el('↑', 'LASER_UP'),
    LASER_DOWN : el('↓', 'LASER_DOWN'),

    START : el('S', 'START'),
    EXIT : el('E', 'EXIT'),
    GOLD : el('$', 'GOLD'),
    HOLE : el('O', 'HOLE'),
    BOX : el('B', 'BOX'),

    getElements : function(char) {
        var result = [];
        for (name in this) {
            if (typeof this[name] === 'function') {
                continue;
            }
            result.push(this[name]);
        }
        return result;
    },

    getElement : function(char) {
        var elements = this.getElements();
        for (name in elements) {
            if (elements[name].char == char) {
                return elements[name];
            }
        }
        return null;
    },

    getElementsTypes : function() {
        var result = [];
        var elements = this.getElements();
        for (name in elements) {
            var type = elements[name].type;
            if (result.indexOf(type) == -1) {
                result.push(type);
            }
        }
        return result;
    },

    getElementsOfType : function(type) {
        var result = [];
        var elements = this.getElements();
        for (name in elements) {
            if (elements[name].type == type) {
                result.push(elements[name]);
            }
        }
        return result;
    }

};

// ========================== direction ==========================

var D = function(index, dx, dy, name){

    var changeX = function(x) {
        return x + dx;
    };

    var changeY = function(y) {
        return y + dy;
    };

    var inverted = function() {
        switch (this) {
            case Direction.UP : return Direction.DOWN;
            case Direction.DOWN : return Direction.UP;
            case Direction.LEFT : return Direction.RIGHT;
            case Direction.RIGHT : return Direction.LEFT;
            default : return Direction.STOP;
        }
    };

    var getName = function() {
        return name.toUpperCase();
    };

    var getIndex = function() {
        return index;
    }

    return {
        changeX : changeX,
        changeY : changeY,
        inverted : inverted,
        name : getName,
        getIndex : getIndex
    };
};

var Direction = {
    UP : D(2, 0, -1, 'UP'),
    DOWN : D(3, 0, 1, 'DOWN'),
    LEFT : D(0, -1, 0, 'LEFT'),
    RIGHT : D(1, 1, 0, 'RIGHT'),
    ACT : D(4, 0, 0, 'ACT'),
    STOP : D(5, 0, 0, ''),

    get : function(direction) {
        if (typeof direction == 'string') {
            direction = direction.toUpperCase();
            for (var name in Direction) {
                if (direction == Direction[name].name()) {
                    return Direction[name];
                }
            }
            return null;
        } else {
            return direction;
        }
    }
};

Direction.values = function() {
   return [Direction.UP, Direction.DOWN, Direction.LEFT, Direction.RIGHT, Direction.ACT, Direction.STOP];
};

Direction.valueOf = function(index) {
    var directions = Direction.values();
    for (var i in directions) {
        var direction = directions[i];
        if (direction.getIndex() == index) {
             return direction;
        }
    }
    return Direction.STOP;
};

// ========================== point ==========================

var Point = function (x, y) {
    return {
        equals : function (o) {
            return o.getX() == x && o.getY() == y;
        },

        toString : function() {
            return '[' + x + ',' + y + ']';
        },

        isBad : function(boardSize) {
            return x >= boardSize || y >= boardSize || x < 0 || y < 0;
        },

        getX : function() {
            return x;
        },

        getY : function() {
            return y;
        }
    }
};

var pt = function(x, y) {
    return new Point(x, y);
};

// ========================== board ==========================

var LengthToXY = function(boardSize) {
    return {
        getXY : function(length) {
            if (length == -1) {
                return null;
            }
            return new Point(length % boardSize, Math.trunc(length / boardSize));
        },

        getLength : function(x, y) {
            return y*boardSize + x;
        }
    };
};

var LAYER1 = 0;
var LAYER2 = 1;

var Board = function(boardString){
    var board = eval(boardString);
    var layers = board.layers;

    var contains  = function(a, obj) {
        var i = a.length;
        while (i--) {
           if (a[i].equals(obj)) {
               return true;
           }
        }
        return false;
    };

    var removeDuplicates = function(all) {
        var result = [];
        for (var index in all) {
            var point = all[index];
            if (!contains(result, point)) {
                result.push(point);
            }
        }
        return result;
    };

    var boardSize = function() {
        return Math.sqrt(layers[LAYER1].length);
    };

    var size = boardSize();
    var xyl = new LengthToXY(size);

    var isAt = function(x, y, layer, element) {
       if (pt(x, y).isBad(size)) {
           return false;
       }
       return getAt(x, y, layer).char == element.char;
    };

    var getAt = function(x, y, layer) {
        return Element.getElement(layers[layer].charAt(xyl.getLength(x, y)));
    };

    var findAll = function(element, layer) {
        var result = [];
        for (var i = 0; i < size*size; i++) {
            var point = xyl.getXY(i);
            if (isAt(point.getX(), point.getY(), layer, element)) {
                result.push(point);
            }
        }
        return result;
    };

    var isAnyOfAt = function(x, y, layer, elements) {
        for (var index in elements) {
            var element = elements[index];
            if (isAt(x, y, layer, element)) {
                return true;
            }
        }
        return false;
    };

    var isNear = function(x, y, layer, element) {
        if (pt(x, y).isBad(size)) {
            return false;
        }
        return isAt(x + 1, y, layer, element) || isAt(x - 1, y, layer, element)
             || isAt(x, y + 1, layer, element) || isAt(x, y - 1, layer, element);
    };

    var isBarrierAt = function(x, y) {
        return contains(getBarriers(), pt(x, y));
    };

    var countNear = function(x, y, layer, element) {
        if (pt(x, y).isBad(size)) {
            return 0;
        }
        var count = 0;
        if (isAt(x - 1, y    , layer, element)) count ++;
        if (isAt(x + 1, y    , layer, element)) count ++;
        if (isAt(x    , y - 1, layer, element)) count ++;
        if (isAt(x    , y + 1, layer, element)) count ++;
        return count;
    };

    var getHero = function() {
        var result = [];
        result = result.concat(findAll(Element.ROBOT, LAYER2));
        result = result.concat(findAll(Element.ROBOT_FALLING, LAYER2));
        result = result.concat(findAll(Element.ROBOT_FLYING, LAYER2));
        result = result.concat(findAll(Element.ROBOT_LASER, LAYER2));
        return result[0];
    };

    var getOtherHeroes = function() {
        var result = [];
        result = result.concat(findAll(Element.ROBOT_OTHER, LAYER2));
        return result;
    };

    var getLaserMachines = function() {
        var result = [];
        result = result.concat(findAll(Element.LASER_MACHINE_CHARGING_LEFT, LAYER1));
        result = result.concat(findAll(Element.LASER_MACHINE_CHARGING_RIGHT, LAYER1));
        result = result.concat(findAll(Element.LASER_MACHINE_CHARGING_UP, LAYER1));
        result = result.concat(findAll(Element.LASER_MACHINE_CHARGING_DOWN, LAYER1));
        result = result.concat(findAll(Element.LASER_MACHINE_READY_LEFT, LAYER1));
        result = result.concat(findAll(Element.LASER_MACHINE_READY_RIGHT, LAYER1));
        result = result.concat(findAll(Element.LASER_MACHINE_READY_UP, LAYER1));
        result = result.concat(findAll(Element.LASER_MACHINE_READY_DOWN, LAYER1));
        return result;
    };

    var getLasers = function() {
        var result = [];
        result = result.concat(findAll(Element.LASER_LEFT, LAYER2));
        result = result.concat(findAll(Element.LASER_RIGHT, LAYER2));
        result = result.concat(findAll(Element.LASER_UP, LAYER2));
        result = result.concat(findAll(Element.LASER_DOWN, LAYER2));
        return result;
    };

    var getWalls = function() {
        var result = [];
        result = result.concat(findAll(Element.ANGLE_IN_LEFT, LAYER1));
        result = result.concat(findAll(Element.WALL_FRONT, LAYER1));
        result = result.concat(findAll(Element.ANGLE_IN_RIGHT, LAYER1));
        result = result.concat(findAll(Element.WALL_RIGHT, LAYER1));
        result = result.concat(findAll(Element.ANGLE_BACK_RIGHT, LAYER1));
        result = result.concat(findAll(Element.WALL_BACK, LAYER1));
        result = result.concat(findAll(Element.ANGLE_BACK_LEFT, LAYER1));
        result = result.concat(findAll(Element.WALL_LEFT, LAYER1));
        result = result.concat(findAll(Element.WALL_BACK_ANGLE_LEFT, LAYER1));
        result = result.concat(findAll(Element.WALL_BACK_ANGLE_RIGHT, LAYER1));
        result = result.concat(findAll(Element.ANGLE_OUT_RIGHT, LAYER1));
        result = result.concat(findAll(Element.ANGLE_OUT_LEFT, LAYER1));
        result = result.concat(findAll(Element.SPACE, LAYER1));
        return result;
    };

    var getBoxes = function() {
        var result = [];
        result = result.concat(findAll(Element.BOX, LAYER1));
        return result;
    };

    var getGold = function() {
        var result = [];
        result = result.concat(findAll(Element.GOLD, LAYER1));
        return result;
    };

    var getStart = function() {
        var result = [];
        result = result.concat(findAll(Element.START, LAYER1));
        return result;
    };

    var getExit = function() {
        var result = [];
        result = result.concat(findAll(Element.EXIT, LAYER1));
        return result;
    };

    var getGold = function() {
        var result = [];
        result = result.concat(findAll(Element.GOLD, LAYER1));
        return result;
    };

    var getHoles = function() {
        var result = [];
        result = result.concat(findAll(Element.HOLE, LAYER1));
        return result;
    };

    var isMyRobotAlive = function() {
        return layers[LAYER2].indexOf(Element.ROBOT_LASER.char) == -1 &&
                layers[LAYER2].indexOf(Element.ROBOT_FALLING.char) == -1;
    };

    var barriers = null; // TODO optimize this method
    var getBarriers = function() {
        if (!!barriers) {
            return barriers;
        }
        var all = getWalls();
        all = all.concat(getLaserMachines());
        all = all.concat(getBoxes());
        barriers = removeDuplicates(all);
        return barriers;
    };

    var getShortestWay = function(from, to) {
        var mask = Array(size);
        for (var x = 0; x < size; x++) {
            mask[x] = new Array(size);
            for (var y = 0; y < size; y++) {
                mask[x][y] = (isBarrierAt(x, y)) ? -1 : 0;
            }
        }

        var current = 1;
        mask[from.getX()][from.getY()] = current;

        var isOutOf = function(x, y) {
            return (x < 0 || y < 0 || x >= size || y >= size);
        }

        var comeRound = function(x, y, onElement) {
            var dd = [[-1, 0], [1, 0], [0, -1], [0, 1]];
            for (var i in dd) {
                var dx = dd[i][0];
                var dy = dd[i][1];

                var xx = x + dx;
                var yy = y + dy;
                if (isOutOf(xx, yy)) continue;

                var stop = !onElement(xx, yy);

                if (stop) return;
            }
        }

        var done = false;
        while (!done) {
            for (var x = 0; x < size; x++) {
                for (var y = 0; y < size; y++) {
                    if (mask[x][y] != current) continue;

                    comeRound(x, y, function(xx, yy) {
                        if (mask[xx][yy] == 0) {
                            mask[xx][yy] = current + 1;
                            if (xx == to.getX() && yy == to.getY()) {
                                done = true;
                            }
                        }
                        return true;
                    });
                }
            }
            current++;
        }
        var point = to;
        done = false;
        current = mask[point.getX()][point.getY()];
        var path = [];
        path.push(point);
        while (!done) {
            comeRound(point.getX(), point.getY(), function(xx, yy) {
                if (mask[xx][yy] == current - 1) {
                    point = pt(xx, yy);
                    current--;

                    path.push(point);

                    if (current == 1) {
                        done = true;
                    }
                    return false;
                }
                return true;
           });
        }

        return path.reverse();
    }

    var boardAsString = function(layer) {
        var result = "";
        for (var i = 0; i <= size - 1; i++) {
            result += layers[layer].substring(i * size, (i + 1) * size);
            result += "\n";
        }
        return result;
    };

    // thanks http://jsfiddle.net/queryj/g109jvxd/
    String.format = function() {
        // The string containing the format items (e.g. "{0}")
        // will and always has to be the first argument.
        var theString = arguments[0];

        // start with the second argument (i = 1)
        for (var i = 1; i < arguments.length; i++) {
            // "gm" = RegEx options for Global search (more than one instance)
            // and for Multiline search
            var regEx = new RegExp("\\{" + (i - 1) + "\\}", "gm");
            theString = theString.replace(regEx, arguments[i]);
        }
    }

    var toString = function() {
        return String.format(
            "Board layer 1:\n{0}\n" +
            "Board layer 2:\n{1}\n" +
            "Robot at: {2}\n" +
            "Other robots at: {3}\n" +
            "LaserMachine at: {4}" +
            "Laser at: {5}" +
                boardAsString(LAYER1),
                boardAsString(LAYER2),
                getHero(),
                printArray(getOtherHeroes()),
                printArray(getLaserMachines()),
                printArray(getLasers())
            );
    };

    return {
        size : function() {
           return size;
        },
        getHero : getHero,
        getOtherHeroes : getOtherHeroes,
        getLaserMachines : getLaserMachines,
        getLasers : getLasers,
        getWalls : getWalls,
        getBoxes : getBoxes,
        getGold : getGold,
        getStart : getStart,
        getExit : getExit,
        getHoles : getHoles,
        isMyRobotAlive : isMyRobotAlive,
        isAt : isAt,
        getAt : getAt,
        toString : toString,
        layer1 : function() {
            return boardAsString(LAYER1)
        },
        layer2 : function() {
            return boardAsString(LAYER2)
        },
        getBarriers : getBarriers,
        findAll : findAll,
        isAnyOfAt : isAnyOfAt,
        isNear : isNear,
        isBarrierAt : isBarrierAt,
        countNear : countNear,
        getShortestWay : getShortestWay
    };
};

var random = function(n){
    return Math.floor(Math.random()*n);
};

// ========================== leaderboard page ==========================

game.onBoardAllPageLoad = function() {
    initLayout(game.gameName, 'leaderboard.html', game.contextPath,
        null,
		[],
        function() {
            initLeadersTable(game.contextPath, game.playerName, game.code,
                    function() {
                    },
                    function(count, you, link, name, score, maxLength, level) {
                        var star = '';
                        if (count == 1) {
                            star = 'first';
                        } else if (count < 3) {
                            star = 'second';
                        }
                        return '<tr>' +
                                '<td><span class="' + star + ' star">' + count + '<span></td>' +
                                '<td>' + you + '<a href="' + link + '">' + name + '</a></td>' +
                                '<td class="center">' + score + '</td>' +
                            '</tr>';
                    });
            $('#table-logs').removeClass('table');
            $('#table-logs').removeClass('table-striped');
            $(document.body).show();
        });
}

// ========================== user page ==========================

var controller;
var currentLevel = -1;

game.onBoardPageLoad = function() {
    initLayout(game.gameName, 'board.html', game.contextPath,
        null,
		[
            'js/mousewheel/jquery.mousewheel.min.js',
		    'js/scroll/jquery.mCustomScrollbar.js',

            'js/ace/src/ace.js',
            'js/ace/src/ext-language_tools.js',

		    'bootstrap/js/bootstrap.js',
        ],
        function() {
            var starting = true;

            var libs = game.contextPath + 'resources/' + game.gameName + '/js';
            if (game.demo) {
                libs = 'js';
            }

            // ----------------------- disable backspace -------------------
            $(document).on('keydown', function(e) {
                if (e.which === 8 && !$(e.target).is('input, textarea')) {
                    e.preventDefault();
                }
            });

            // ----------------------- init scrollbar -------------------
            $('.content').mCustomScrollbar({
                theme:'dark-2',
                axis: 'yx',
                mouseWheel : { enable : false }
            });

            // ----------------------- init ace editor -------------------
            if (game.demo) {
                ace.config.set('basePath', 'js/ace/src/');
            } else {
                ace.config.set('basePath', game.contextPath + 'resources/' + game.gameName + '/js/ace/src/');
            }

            ace.config.set('basePath', libs + '/ace/src/');
            var tools = ace.require("ace/ext/language_tools");
            tools.addCompleter(icancodeWordCompleter);
            var editor = ace.edit('ide-block');
            editor.setTheme('ace/theme/monokai');
            editor.session.setMode('ace/mode/javascript');
            editor.setOptions({
                fontSize: '14pt',
                enableBasicAutocompletion: true,
                enableSnippets: true,
                enableLiveAutocompletion: true
            });
            editor.on('focus', function() {
                game.enableJoystick = false;
            });
            editor.on('blur', function() {
                game.enableJoystick = true;
            });
            var typeCounter = 0;
            var clean = null;
            editor.on('change', function() {
                if (!game.code) {
                    return;
                }

                if (!starting && editor.getValue() == '') {
                    clean = 0;
                }

                if (typeCounter++ % 10 == 0) {
                    saveSettings();
                }
            });
            $('body').bind("tick", function() {
                if (!game.code) {
                    return;
                }

                if (clean != null) {
                    clean++;
                    if (clean == 2) {
                        clean = null;

                        if (editor.getValue() == '') {
                            editor.setValue(getDefaultEditorValue(), 1);
                        } else if (editor.getValue() == 'win') {
                            editor.setValue(getWinEditorValue(), 1);
                        } else if (editor.getValue() == 'ref') {
                            editor.setValue(getRefactoringEditorValue(), 1);
                        }
                    }
                }
            });

            // ----------------------- init progressbar -------------------
            var progressBar = $('#progress-bar li.training');
            progressBar.clean = function(level) {
                $(progressBar[level]).removeClass("level-done");
                $(progressBar[level]).removeClass("level-current");
                $(progressBar[level]).removeClass("level-not-active");
            }
            progressBar.notActive = function(level) {
                progressBar.clean(level);
                $(progressBar[level]).addClass("level-not-active");
            }
            progressBar.active = function(level) {
                progressBar.clean(level);
                $(progressBar[level]).addClass("level-current");
            }
            progressBar.done = function(level) {
                progressBar.clean(level);
                $(progressBar[level]).addClass("level-done");
            }
            progressBar.setProgress = function(current, lastPassed) {
                for (var i = 0; i <= lastPassed; ++i) {
                    this.done(i);
                }
                this.active(lastPassed + 1);
                this.active(current);
            }
            progressBar.click(function(event) {
                if (!game.code) {
                    return;
                }

                var element = $(event.target);

                if (element.hasClass('level-not-active')) {
                    return;
                }

                var level = element.attr('level');
                if (currentLevel == level - 1) {
                    return;
                }

                send(encode('LEVEL' + level));
            });
            progressBar.each(function(index) {
                progressBar.notActive(index);
            });

            // ----------------------- init tooltip -------------------
            $('[data-toggle="tooltip"]').tooltip();

            // ----------------------- init progressbar slider -------------------
            var initProgressbarSlider = function() {
                var width = 0;
                var currentWidth = 0;
                $(".training").each(function() {
                    width += $(this).outerWidth();
                });
                $(".training.level-done").each(function() {
                    currentWidth += $(this).outerWidth();
                });
                currentWidth += $(".training.level-current").outerWidth();
                // console.log(width, currentWidth);
                if (currentWidth > width) {
                    $(".trainings").animate({left: "50%"}, 1000, function(){
                    });
                }

                $(".trainings-button.left").click(function(){
                    $(".trainings").animate({right: "-=200"}, 1000, function(){
                    });
                });

                $(".trainings-button.right").click(function(){
                    if ($(".trainings").attr("style")) {
                        // console.log(parseFloat($(".trainings").attr("style").substring(7)));
                        if (parseFloat($(".trainings").attr("style").substring(7)) >= 0) {
                            return;
                        }
                    }
                    $(".trainings").animate({right: "+=200"}, 1000, function(){
                    });
                });
            }
            initProgressbarSlider();

            // ----------------------- update progressbar -------------------
            $('body').bind("board-updated", function(events, data) {
                if (game.playerName == '' || !data[game.playerName]) {
                    return;
                }

                $('body').trigger("tick");

                var board = JSON.parse(data[game.playerName].board);

                var level = board.levelProgress.current;
                var multiple = board.levelProgress.multiple;
                var lastPassed = board.levelProgress.lastPassed;
                level = multiple ? (progressBar.length - 1) : level;

                if (currentLevel == level) {
                    return;
                }
                changeLevel(level);

                progressBar.setProgress(currentLevel, lastPassed);
            });
            if (game.demo) {
                var data = '{"' + game.playerName + '":{"board":"{\\"levelProgress\\":{\\"total\\":18,\\"current\\":3,\\"lastPassed\\":2,\\"multiple\\":false},\\"layers\\":[\\"OOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOCDDDDEOOOOOOOOOOJaBB9FOOOOOOOOOOIHHHHGOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOO\\",\\"AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAPAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA\\"]}","gameName":"icancode","score":150,"maxLength":0,"length":0,"level":1,"boardSize":16,"info":"","scores":"{\\"fasdfddd@gmail.com\\":150,\\"SDAsd@sas.as\\":2250}","coordinates":"{\\"fasdfddd@gmail.com\\":{\\"y\\":8,\\"x\\":9},\\"SDAsd@sas.as\\":{\\"y\\":8,\\"x\\":9}}"}}';
                $('body').trigger('board-updated', JSON.parse(data));
            }

            // ----------------------- get level info -------------------
            var getLevelInfo = function() {
                var result = levelInfo[currentLevel + 1];
                if (!result) {
                    result = {
                        'help':'<pre>// under construction</pre>',
                        'defaultCode':'function program(robot) {\n'  +
                               '    // TODO write your code here\n' +
                               '}',
                        'winCode':'function program(robot) {\n'  +
                               '    robot.nextLevel();\n' +
                               '}',
                        'refactoringCode':'function program(robot) {\n'  +
                               '    robot.nextLevel();\n' +
                               '}'
                    };
                }
                return result;
            }

            var getDefaultEditorValue = function() {
                return getLevelInfo().defaultCode;
            }

            var getWinEditorValue = function() {
                return getLevelInfo().winCode;
            }

            var getRefactoringEditorValue = function() {
                var code = getLevelInfo().refactoringCode;
                if (!!code) {
                    return code;
                } else {
                    return getWinEditorValue();
                }
            }

            var resetButton = $('#ide-reset');
            var commitButton = $('#ide-commit');
            var helpButton = $("#ide-help");

            // ----------------------- init console -------------------
            var console = $('#ide-console');
            console.empty();

            var print = function(message) {
                console.append('> ' + message + '<br>')
                console.animate({scrollTop: console.prop('scrollHeight')});
            }

            var printCongrats = function() {
                print('Congrats ' + game.playerName + '! You have passed the puzzle!!!');
            }

            var printHello = function() {
                print('Hello ' + game.playerName + '! I am Robot! Please write your code and press Commit.');
            }

            var error = function(message) {
                print('Error: ' + message);
            }

            // ----------------------- init slider -------------------
            var setupSlider = function() {
                $("#console-panel").click(function(){
                    if ($("#console").hasClass("open")) {
                        $("#console").removeClass("open").addClass("close");
                        $("#block").removeClass("console-open").addClass("console-close");
                        $("#console-panel-icon").removeClass("fa-angle-right").addClass("fa-angle-left");
                    } else {
                        $("#console").removeClass("close").addClass("open");
                        $("#block").removeClass("console-close").addClass("console-open");
                        $("#console-panel-icon").removeClass("fa-angle-left").addClass("fa-angle-right");
                    }
                });

                $("#editor-panel").click(function(){
                    if (!$("#main").hasClass("editor-fullscreen")) {
                        $("#main").addClass("editor-fullscreen");
                        $("#editor-panel-icon").removeClass("fa-angle-left").addClass("fa-angle-right");
                    } else {
                        $("#main").removeClass("editor-fullscreen");
                        $("#editor-panel-icon").removeClass("fa-angle-right").addClass("fa-angle-left");
                    }
                });
            }
            setupSlider();

            // ----------------------- init help -------------------
            var setupHelp = function() {
                helpButton.click(function() {
                    $('#ide-help-window').html(getLevelInfo().help);
                    $("#modal").removeClass("close");
                });
                $("#close").click(function(){
                    $("#modal").addClass("close");
                });
                $("body").keydown(function(event){
                    if (event.which == 27){
                        $("#close").click();
                    }
                });
            }
            setupHelp();

            var replace = function(string, from, to) {
                return string.split(from).join(to);
            }

            var sleep = function(onSuccess) {
                setTimeout(function(){
                    onSuccess();
                }, 1000);
            }

            // ----------------------- init websocket -------------------
            var send = function(command) {
                if (socket == null) {
                    connect(function() {
                        socket.send(command);
                    });
                } else {
                    socket.send(command);
                }
            }

            var compileCommands = function(onSuccess) {
                var code = editor.getValue();
                print('Compiling program...');
                try {
                    eval(code);
                } catch (e) {
                    error(e.message);
                    print('Please try again.');
                    enableAll();
                    return;
                }

                sleep(function() {
                    print('Uploading program...');

                    sleep(function() {
                        print('Running program...');

                        try {
                            controller.storeProgram(program);
                        } catch (e) {
                            error(e.message);
                            print('Please try again.');
                            enableAll();
                            return;
                        }

                        sleep(function() {
                            onSuccess();
                        });
                    });
                });
            }

            var encode = function(command) {
                command = replace(command, 'WAIT', '');
                command = replace(command, 'JUMP', 'ACT(1)');
                command = replace(command, 'RESET', 'ACT(0)');
                while (command.indexOf('LEVEL') != -1) {
                    var level = command.substring(command.indexOf('LEVEL') + 'level'.length);
                    command = replace(command, 'LEVEL' + level, 'ACT(0,' + (level - 1) + ')');
                }
                command = replace(command, 'WIN', 'ACT(-1)');
                return command;
            }

            var scannerMethod = function() {
                var controlling = false;
                var commands = [];
                var command = null;
                board = null;
                var functionToRun = null;

                var finish = function() {
                    controlling = false;
                    commands = [];
                    command = null;
                    board = null;
                    functionToRun = null;
                    enableAll();
                }

                var currentCommand = function() {
                    if (commands.length != 0) {
                        return commands[0];
                    } else {
                        return null;
                    }
                }

                var hasCommand = function(cmd) {
                    return (commands.indexOf(cmd) != -1);
                }

                var robot = null;
                var memory = null;
                var goThere = null;
                var resetRobot = function() {
                    memory = [];
                    goThere = null;
                    robot = {
                        nextLevel: function() {
                            send(encode('WIN'));
                            commands.push('WAIT');
                        },
                        log : function(message) {
                            print("Robot says: " + message);
                        },
                        invert : function(direction) {
                            if (direction == "LEFT") return "RIGHT";
                            if (direction == "RIGHT") return "LEFT";
                            if (direction == "DOWN") return "UP";
                            if (direction == "UP") return "DOWN";
                        },
                        cameFrom : function() {
                            if (goThere == null) {
                                return null;
                            }

                            return this.invert(goThere);
                        },
                        go : function(direction) {
                            goThere = direction;
                            commands = [];
                            commands.push(direction);
                        },
                        goLeft : function() {
                            this.go(Direction.LEFT.name());
                        },
                        goRight : function() {
                            this.go(Direction.RIGHT.name());
                        },
                        goUp : function() {
                            this.go(Direction.UP.name());
                        },
                        goDown : function() {
                            this.go(Direction.DOWN.name());
                        },
                        jump : function(direction) {
                            goThere = direction;
                            commands = [];
                            if (!direction) {
                                commands.push('JUMP');
                            } else {
                                commands.push('JUMP,' + direction);
                                commands.push('WAIT');
                            }
                        },
                        jumpLeft : function() {
                            this.jump(Direction.LEFT.name());
                        },
                        jumpRight : function() {
                            this.jump(Direction.RIGHT.name());
                        },
                        jumpUp : function() {
                            this.jump(Direction.UP.name());
                        },
                        jumpDown : function() {
                            this.jump(Direction.DOWN.name());
                        },
                        getMemory : function() {
                            return {
                                has : function(key) {
                                    return memory[key] != undefined;
                                },
                                save : function(key, value) {
                                    memory[key] = value;
                                },
                                remove : function(key) {
                                    var old = memory[key];
                                    delete memory[key];
                                    return old;
                                },
                                load : function(key) {
                                    return memory[key];
                                },
                                clean : function() {
                                    memory = [];
                                }
                            };
                        },
                        getScanner : function() {
                            var b = new Board(board);
                            var hero = b.getHero();

                            var forAll = function(elementType, doThat) {
                                var elements = Element.getElementsOfType(elementType);
                                for (var index in elements) {
                                    var element = elements[index];
                                    if (!!doThat) {
                                        doThat(element);
                                    }
                                }
                            }

                            var atNearRobot = function(dx, dy) {
                                var element1 = b.getAt(hero.getX() + dx, hero.getY() + dy, LAYER1);
                                var element2 = b.getAt(hero.getX() + dx, hero.getY() + dy, LAYER2);

                                var result = [];
                                result.push(element1.type);
                                if (element2.type != 'NONE') {
                                    result.push(element2.type);
                                }
                                return result;
                            }

                            var getMe = function() {
                                return hero;
                            }

                            var isAt = function(x, y, elementType) {
                                var found = false;
                                forAll(elementType, function(element) {
                                    if (b.isAt(x, y, LAYER1, element) ||
                                          b.isAt(x, y, LAYER2, element))
                                    {
                                        found = true;
                                    }
                                });
                                return found;
                            }

                            var getAt = function(x, y) {
                                var result = [];
                                var atLayer1 = b.getAt(x, y, LAYER1).type;
                                var atLayer2 = b.getAt(x, y, LAYER2).type;
                                if (atLayer1 != 'NONE') {
                                    result.push(atLayer1);
                                }
                                if (atLayer2 != 'NONE') {
                                    result.push(atLayer2);
                                }
                                if (result.length == 0) {
                                    result.push('NONE');
                                }
                                return result;
                            }

                            var findAll = function(elementType) {
                                var result = [];
                                forAll(elementType, function(element) {
                                    var found = b.findAll(element, LAYER1);
                                    for (var index in found) {
                                        result.push(found[index]);
                                    }
                                    found = b.findAll(element, LAYER2);
                                    for (var index in found) {
                                        result.push(found[index]);
                                    }
                                });
                                return result;
                            }

                            var isAnyOfAt = function(x, y, elementTypes) {
                                var elements = [];
                                for (var index in elementTypes) {
                                    var elementType = elementTypes[index];
                                    forAll(elementType, function(element) {
                                        elements.push(element);
                                    });
                                }

                                if (b.isAnyOfAt(x, y, LAYER1, elements) ||
                                    b.isAnyOfAt(x, y, LAYER2, elements))
                                {
                                    return true;
                                }
                                return false;
                            }

                            var isNear = function(x, y, elementTypes) {
                                if (!Array.isArray(elementTypes)) {
                                    elementTypes = [elementTypes];
                                }
                                var found = false;
                                for(var index in elementTypes) {
                                    forAll(elementTypes[index], function(element) {
                                        if (b.isNear(x, y, LAYER1, element) ||
                                            b.isNear(x, y, LAYER2, element))
                                        {
                                            found = true;
                                        }
                                    });
                                }
                                return found;
                            }

                            var isBarrierAt = function(x, y) {
                                return b.isBarrierAt(x, y);
                            }

                            var countNear = function(x, y, elementType) {
                                var count = 0;
                                forAll(elementType, function(element) {
                                    count += b.countNear(x, y, LAYER1, element);
                                    count += b.countNear(x, y, LAYER2, element);
                                });

                                return count;
                            }

                            var getOtherRobots = function() {
                                return b.getOtherHeroes();
                            }

                            var getLaserMachines = function() {
                                return b.getLaserMachines();
                            }

                            var getLasers = function() {
                                return b.getLasers();
                            }

                            var getWalls = function() {
                                return b.getWalls();
                            }

                            var getBoxes = function() {
                                return b.getBoxes();
                            }

                            var getGold = function() {
                                return b.getGold();
                            }

                            var getStart = function() {
                                return b.getStart();
                            }

                            var getExit = function() {
                                return b.getExit();
                            }

                            var getHoles = function() {
                                return b.getHoles();
                            }

                            var isMyRobotAlive = function() {
                                return b.isMyRobotAlive();
                            }

                            var getBarriers = function() {
                                return b.getBarriers();
                            }

                            var getElements = function() {
                                return Element.getElementsTypes();
                            }

                            var at = function(direction) {
                                var d = Direction.get(direction);
                                return atNearRobot(d.changeX(0), d.changeY(0));
                            }

                            var atLeft = function() {
                                return at(Direction.LEFT);
                            }

                            var atRight = function() {
                                return at(Direction.RIGHT);
                            }

                            var atUp = function() {
                                return at(Direction.UP);
                            }

                            var atDown = function() {
                                return at(Direction.DOWN);
                            }

                            var getShortestWay = function(to) {
                                return b.getShortestWay(getMe(), to)[1];
                            }

                            return {
                                at : at,
                                atLeft : atLeft,
                                atRight : atRight,
                                atUp : atUp,
                                atDown : atDown,
                                atNearRobot : atNearRobot,
                                getMe : getMe,
                                isAt : isAt,
                                getAt : getAt,
                                findAll : findAll,
                                isAnyOfAt : isAnyOfAt,
                                isNear : isNear,
                                isBarrierAt : isBarrierAt,
                                countNear : countNear,
                                getOtherRobots : getOtherRobots,
                                getLaserMachines : getLaserMachines,
                                getLasers : getLasers,
                                getWalls : getWalls,
                                getBoxes : getBoxes,
                                getGold : getGold,
                                getStart : getStart,
                                getExit : getExit,
                                getHoles : getHoles,
                                isMyRobotAlive : isMyRobotAlive,
                                getBarriers : getBarriers,
                                getElements : getElements,
                                getShortestWay : getShortestWay
                            }
                        }
                    };
                }
                resetRobot();

                var processCommands = function(newBoard) {
                    board = newBoard;

                    if (board) {
                        var b = new Board(board);
                        var hero = b.getHero();
                        var exit = b.getExit();
                    }

                    var finished = !!b && hero.toString() == exit.toString();
                    var stopped = currentCommand() == 'STOP';
                    if (!controlling || stopped || finished) {
                        finish();
                        if (finished) {
                            console.empty();
                            printCongrats();
                        } else if (stopped) {
                            console.empty();
                            printHello();
                        }
                        return;
                    }
                    if (hasCommand('STOP')) {
                        commands = ['RESET', 'STOP'];
                    } else if (currentCommand() == 'RESET') {
                        // do nothing
                    } else if (!board) {
                        commands = ['WAIT'];
                    } else {
                        if (!!functionToRun) {
                            try {
                                runProgram(functionToRun, robot);
                            } catch (e) {
                                error(e.message);
                                print('Please try again.');
                                enableAll();
                                return;
                            }
                        } else {
                            error('function program(robot) not implemented!');
                            print('Info: if you clean your code you will get info about commands')
                        }
                    }
                    if (commands.length == 0) {
                        finish();
                        return;
                    }
                    if (controlling) {
                        if (commands.length > 0) {
                            command = commands.shift();
                            send(encode(command));
                        }
                    }
                }

                return {
                    isControlling : function() {
                        return controlling;
                    },
                    startControlling : function() {
                        controlling = true;
                    },
                    stopControlling : function() {
                        controlling = false;
                    },
                    getRobot : function() {
                        return robot;
                    },
                    processCommands : processCommands,
                    cleanCommands : function() {
                        resetRobot();
                        commands = [];
                    },
                    resetCommand : function() {
                        commands = ['RESET'];
                    },
                    stopCommand : function() {
                        commands.push('STOP');
                    },
                    popLastCommand : function() {
                        var result = command;
                        command == null;
                        return command;
                    },
                    storeProgram : function(fn) {
                        functionToRun = fn;
                    }
                };
            }
            controller = scannerMethod();

            var createSocket = function(url) {
                if (game.demo) {
                    var count = 0;
                    return {
                        runMock : function() {
                            this.onopen();
                        },
                        send : function() {
                            if (++count > 3) {
                                count = 0;
                                enableAll();
                                return;
                            }
                            var event = {};
                            event.data = 'board={"layers":["                                                                                                     ╔════┐          ║E..S│          └────┘                                                                                                                     ","-------------------------------------------------------------------------------------------------------------------------☺--------------------------------------------------------------------------------------------------------------------------------------"], "levelProgress":{"total":18,"current":3,"lastPassed":2,"multiple":false}}';
                            this.onmessage(event);
                        }
                    }
                } else {
                    return new WebSocket(url);
                }
            }

            var socket = null;
            var connect = function(onSuccess) {
                var hostIp = window.location.hostname;
                var port = window.location.port;
                var server = 'ws://' + hostIp + ':' + port + '/codenjoy-contest/ws';

                print('Connecting to Robot...');
                socket = createSocket(server + '?user=' + game.playerName);

                socket.onopen = function() {
                    print('...connected successfully!');
              		printHello();
                    if (!!onSuccess) {
                        onSuccess();
                    }
                }

                socket.onclose = function(event) {
                    var controlling = controller.isControlling();
                    controller.stopControlling();

                    var reason = ((!!event.reason)?(' reason: ' + event.reason):'');
                    print('Signal lost! Code: ' + event.code + reason);

                    socket = null;
                    sleep(function() {
                        connect(function() {
                            if (controlling) {
                                controller.startControlling();
                                controller.processCommands();
                            }
                        });
                    });
                }

                socket.onmessage = function(event) {
                    var data = event.data;
                    var command = controller.popLastCommand();
                    if (!!command && command != 'WAIT') {
                        print('Robot do ' + command);
                    }
                    controller.processCommands(data);
                }

                socket.onerror = function(error) {
                    error(error);
                    socket = null;
                }

                if (game.demo) {
                    socket.runMock();
                }
            }

            // ----------------------- init buttons -------------------
            var enable = function(button, enable) {
                button.prop('disabled', !enable);
            }

            var enableAll = function() {
                enable(resetButton, true);
                enable(commitButton, true);
            }

            var disableAll = function() {
                enable(resetButton, false);
                enable(commitButton, false);
            }

            resetButton.click(function() {
                disableAll();

                controller.resetCommand();
                controller.stopCommand();
                if (!controller.isControlling()) {
                    controller.startControlling();
                    controller.processCommands();
                }
            });

            commitButton.click(function() {
                disableAll();

                controller.cleanCommands();
                compileCommands(function() {
                    controller.resetCommand();
                    controller.startControlling();
                    controller.processCommands();

                    enable(resetButton, true);
                });
            });

            // ----------------------- save ide code -------------------
            var saveSettings = function() {
                var text = editor.getValue();
                if (!!text && text != '') {
                    localStorage.setItem('editor.code', editor.getValue());
                    var position =  editor.selection.getCursor();
                    localStorage.setItem('editor.cursor.position.column', position.column);
                    localStorage.setItem('editor.cursor.position.row', position.row);
                    editor.selection.getCursor()
                }
            }
            var loadSettings = function() {
                try {
                    var text = localStorage.getItem('editor.code');
                    if (!!text && text != '') {
                        editor.setValue(text);
                        var column = localStorage.getItem('editor.cursor.position.column');
                        var row = localStorage.getItem('editor.cursor.position.row');
                        editor.focus();
                        editor.selection.moveTo(row, column);
                    } else {
                        editor.setValue(getDefaultEditorValue());
                    }
                } catch (e) {
                    // do nothing
                }
            }
            $(window).on('unload', saveSettings);


            // ----------------------- starting UI -------------------
            disableAll();
            $(document.body).show();

            if (!!game.code) {
                loadSettings();

                connect(function() {
                    enableAll();
                });
            } else {
                enable(helpButton, false);

                var link = $('#register-link').attr('href');
                print('<a href="' + link + '">Please register</a>');

                editor.setValue(
                        'function program(robot) {\n' +
                        '    // PLEASE REGISTER\n' +
                        '}');
            }
            starting = false;
        });
}

if (game.demo) {
    game.onBoardPageLoad();
}

// ========================== level info ==========================

var levelInfo = [];
levelInfo[1] = {
        'help':'Robot asks for new orders every second. <br>' +
               'He should know where to go.<br>' +
               'Help him - write program and save him from the Maze. <br>' +
               'The code looks like this:<br>' +
               '<pre>function program(robot) {\n' +
               '    // TODO Uncomment one line that will help\n' +
               '    // robot.goDown();\n' +
               '    // robot.goUp();\n' +
               '    // robot.goLeft();\n' +
               '    // robot.goRight();\n' +
               '}</pre>' +
               'Send program to Robot by clicking the Commit button.<br>' +
               'If something is wrong - check Robot message in the Console ().<br>' +
               'You can always stop the program by clicking the Reset button.',
        'defaultCode':'function program(robot) {\n' +
               '    // TODO Uncomment one line that will help\n' +
               '    // robot.goDown();\n' +
               '    // robot.goUp();\n' +
               '    // robot.goLeft();\n' +
               '    // robot.goRight();\n' +
               '}',
        'winCode':'function program(robot) {\n' +
               '    robot.goRight();\n' +
               '}'
    };

levelInfo[2] = {
        'help':'Looks like the Maze was changed. Our old program will not help.<br>' +
               'We need to change it! The robot must learn how to use the scanner.<br>' +
               'To use scanner is necessary to execute the following code:<br>' +
               '<pre>function program(robot) {\n' +
               '    var scanner = robot.getScanner();\n' +
               '    if (scanner.atRight() != "WALL") {\n' +
               '        robot.goRight();\n' +
               '    } else {\n' +
               '        // TODO write your code here\n' +
               '    }\n' +
               '}</pre>' +
               'In this code, you can see the new IF-ELSE construction:<br>' +
               '<pre>if (expression) {\n' +
               '    // statement\n' +
               '} else {\n' +
               '    // statement\n' +
               '}</pre>',
        'defaultCode':'function program(robot) {\n' +
               '    var scanner = robot.getScanner();\n' +
               '    if (scanner.atRight() != "WALL") {\n' +
               '        robot.goRight();\n' +
               '    } else {\n' +
               '        // TODO write your code here\n' +
               '    }\n' +
               '}',
        'winCode':'function program(robot) {\n' +
               '    var scanner = robot.getScanner();\n' +
               '    if (scanner.atRight() != "WALL") {\n' +
               '        robot.goRight();\n' +
               '    } else {\n' +
               '        robot.goDown();\n' +
               '    }\n' +
               '}'
    };

levelInfo[3] = {
         'help':'This Maze is very similar to the previous. But no so easy.<br>' +
                'Try to solve it by adding new IF.<br>' +
                'You can also use Robot memory for saving data while Robot moving:<br>' +
                '<pre>var memory = robot.getMemory();\n' +
                'memory.save("key", "value"); // save the value as "key"\n' +
                'if (memory.has("key")) {\n' +
                '    // if value for "key" is not set\n' +
                '} else {\n' +
                '    // otherwise\n' +
                '}\n' +
                'var data = memory.load("key"); // load "key" data\n' +
                'memory.remove("key"); // remove "key" data\n' +
                'memory.clear(); // clear all data</pre>' +
                'You can use new methods for refactoring:<br>' +
                '<pre>scanner.at("RIGHT");\n' +
                'robot.go("LEFT");</pre>' +
                'Be careful! The program should work for all previous levels too.',
         'defaultCode':'function program(robot) {\n' +
                '    var scanner = robot.getScanner();\n' +
                '    var memory = robot.getMemory();\n' +
                '    if (scanner.atRight() != "WALL") {\n' +
                '        robot.goRight();\n' +
                '    } else {\n' +
                '        robot.goDown();\n' +
                '    }\n' +
                '}',
         'winCode':'function program(robot) {\n' +
                '    var scanner = robot.getScanner();\n' +
                '    var memory = robot.getMemory();    \n' +
                '\n' +
                '    if (!memory.has("data")) {\n' +
                '        if (scanner.atRight() != "WALL") {\n' +
                '            memory.save("data", "RIGHT");\n' +
                '        } else if (scanner.atDown() != "WALL") {\n' +
                '    	    memory.save("data", "DOWN");\n' +
                '    	} else {\n' +
                '    	    memory.save("data", "LEFT");\n' +
                '    	}\n' +
                '    }\n' +
                '    \n' +
                '    robot.go(memory.load("data"));\n' +
                '}'
    };

levelInfo[4] = {
        'help':'Try to solve it by adding new IF. Now it should be easy!<br>' +
               'After solving try to refactor this code.<br>' +
               'You can use new FOR construction:<br>' +
               '<pre>var directions = ["RIGHT", "DOWN", "LEFT", "UP"];\n' +
               'for (var index in directions) {\n' +
               '    var direction = directions[index];\n' +
               '    // do something\n' +
               '}</pre>' +
               'Be careful! The program should work for all previous levels too.',
        'defaultCode':levelInfo[3].defaultCode,
        'winCode':'function program(robot) {\n' +
               '    var scanner = robot.getScanner();\n' +
               '    var memory = robot.getMemory();    \n' +
               '\n' +
               '    if (!memory.has("data")) {\n' +
               '        if (scanner.atRight() != "WALL") {\n' +
               '            memory.save("data", "RIGHT");\n' +
               '        } else if (scanner.atDown() != "WALL") {\n' +
               '    	    memory.save("data", "DOWN");\n' +
               '    	} else if (scanner.atLeft() != "WALL") {\n' +
               '    	    memory.save("data", "LEFT");\n' +
               '    	} else {\n' +
               '    	    memory.save("data", "UP");\n' +
               '    	}\n' +
               '    }\n' +
               '    \n' +
               '    robot.go(memory.load("data"));\n' +
               '}',
        'refactoringCode':'function program(robot) {\n' +
               '    var scanner = robot.getScanner();\n' +
               '    var memory = robot.getMemory();    \n' +
               '\n' +
               '    if (!memory.has("data")) {\n' +
               '        var directions = ["RIGHT", "DOWN", "LEFT", "UP"];\n' +
               '        for (var index in directions) {\n' +
               '            var direction = directions[index];\n' +
               '            \n' +
               '            if (scanner.at(direction) != "WALL") {\n' +
               '                memory.save("data", direction);\n' +
               '            } \n' +
               '        }\n' +
               '    }\n' +
               '    \n' +
               '    robot.go(memory.load("data"));\n' +
               '}'
    };

levelInfo[5] = {
        'help':'Oops! This case, we seem to have not predicted.<br>' +
               'Think how to adapt the code to these new conditions.<br>' +
               'Use refactoring to make your code more abstract.<br>' +
               'Уou can extract functions, create new local variables:<br>' +
               '<pre>function program(robot) {\n' +
               '    var scanner = robot.getScanner();\n' +
               '    var localVariable = newFunction(scanner);\n' +
               '}\n' +
               'function newFunction(scanner) {\n' +
               '    return "some data";\n' +
               '}</pre>' +
               'New function used for encapsulate algorithm.<br>' +
               'Local variable saves value only during current step.<br>' +
               'If you want to save value during program working - use Robot memory.<br>' +
               'If you want to know where we came from - use this expression:<br>' +
               '<pre>robot.cameFrom() == "LEFT"</pre>' +
               'Remember! Your program should work for all previous levels too.',
        'defaultCode':levelInfo[4].refactoringCode,
        'winCode':'function program(robot) {\n' +
               '    var scanner = robot.getScanner();\n' +
               '    var memory = robot.getMemory();    \n' +
               '\n' +
               '    if (!memory.has("data") || \n' +
               '        scanner.at(memory.load("data")) == "WALL") \n' +
               '    {\n' +
               '        var direction = freeDirection(scanner, robot);\n' +
               '        memory.save("data", direction);\n' +
               '    } \n' +
               '    \n' +
               '    robot.go(memory.load("data"));\n' +
               '}\n' +
               '\n' +
               'function freeDirection(scanner, robot) {\n' +
               '    var directions = ["RIGHT", "DOWN", "LEFT", "UP"];\n' +
               '    for (var index in directions) {\n' +
               '        var direction = directions[index];\n' +
               '        if (direction == robot.cameFrom()) {\n' +
               '            continue;\n' +
               '        }\n' +
               '        \n' +
               '        if (scanner.at(direction) != "WALL") {\n' +
               '            return direction;\n' +
               '        } \n' +
               '    }\n' +
               '    return null;\n' +
               '}'
    };

levelInfo[6] = {
        'help':'We should check all cases.',
        'defaultCode':levelInfo[5].defaultCode,
        'winCode':levelInfo[5].winCode
    };

levelInfo[7] = {
        'help':levelInfo[6].help,
        'defaultCode':levelInfo[6].defaultCode,
        'winCode':levelInfo[6].winCode
    };

levelInfo[8] = {
        'help':levelInfo[7].help,
        'defaultCode':levelInfo[7].defaultCode,
        'winCode':levelInfo[7].winCode
    }

levelInfo[9] = {
        'help':'This is final LevelA Maze. Solve it!',
        'defaultCode':levelInfo[8].defaultCode,
        'winCode':levelInfo[8].winCode
    };

levelInfo[10] = { // LEVELB
        'help':'You can use new methods in the scanner:<br>' +
               '<pre>var destinationPoints = scanner.getGold();\n' +
               'var nextPoint = scanner.getShortestWay(destinationPoints[0]);\n' +
               'var finishPoint = scanner.getFinish();\n' +
               'var robotPoint = scanner.getMe();</pre>' +
               'Remember! Your program should work for all previous levels too.',
        'defaultCode':'function program(robot) {\n' +
               '    var scanner = robot.getScanner();\n' +
               '    var dest = scanner.getGold();\n' +
               '    var next = scanner.getShortestWay(dest[0]);\n' +
               '    var finish = scanner.getFinish();\n' +
               '    var robot = scanner.getMe();\n' +
               '    // TODO write your code here\n' +
               '}',
        'winCode':'function program(robot) {\n' +
               '    var scanner = robot.getScanner();\n' +
               '    var dest = scanner.getGold();\n' +
               '    if (dest.length === 0) {\n' +
               '        dest = scanner.getExit();\n' +
               '    }\n' +
               '    var to = scanner.getShortestWay(dest[0]);\n' +
               '    var from = scanner.getMe();\n' +
               '    \n' +
               '    var dx = to.getX() - from.getX();\n' +
               '    var dy = to.getY() - from.getY();\n' +
               '    if (dx > 0) {\n' +
               '        robot.goRight();\n' +
               '    } else if (dx < 0) {\n' +
               '        robot.goLeft();\n' +
               '    } else if (dy > 0) {\n' +
               '        robot.goDown();\n' +
               '    } else if (dy < 0) {\n' +
               '        robot.goUp();\n' +
               '    }\n' +
               '}'
    };

levelInfo[11] = { // LEVELC
        'help':'In this case case, we have an Hole.<br>' +
               'You can use this method for detecting:<br>' +
               '<pre>var scanner = robot.getScanner();\n' +
               'if (scanner.at("LEFT") == "HOLE") {\n' +
               '    // some statement here\n' +
               '}</pre>' +
               'And these new methods for jumping:<br>' +
               '<pre>robot.jumpLeft();\n' +
               'robot.jumpRight();\n' +
               'robot.jumpUp();\n' +
               'robot.jumpDown();\n' +
               'robot.jump("LEFT");</pre>' +
               'Also you can add new method to robot by:' +
               '<pre>robot.doSmthNew = function(parameter) {\n' +
               '    // some statement here\n' +
               '}</pre>' +
               'Remember! Your program should work for all previous levels too.',
        'defaultCode':levelInfo[10].defaultCode,
        'winCode':'function program(robot) {\n' +
               '    var scanner = robot.getScanner();\n' +
               '    var dest = scanner.getGold();\n' +
               '    if (dest.length === 0) {\n' +
               '        dest = scanner.getExit();\n' +
               '    }\n' +
               '    var to = scanner.getShortestWay(dest[0]);\n' +
               '    var from = scanner.getMe();\n' +
               '\n' +
               '    robot.goOverHole = function(direction) {\n' +
               '        if (scanner.at(direction) != "HOLE") {\n' +
               '            robot.go(direction);\n' +
               '        } else {\n' +
               '            robot.jump(direction);\n' +
               '        }\n' +
               '    };\n' +
               '    \n' +
               '    var dx = to.getX() - from.getX(); \n' +
               '    var dy = to.getY() - from.getY(); \n' +
               '    if (dx > 0) {\n' +
               '        robot.goOverHole("RIGHT");\n' +
               '    } else if (dx < 0) {\n' +
               '        robot.goOverHole("LEFT");\n' +
               '    } else if (dy > 0) {\n' +
               '        robot.goOverHole("DOWN");\n' +
               '    } else if (dy < 0) {\n' +
               '        robot.goOverHole("UP");\n' +
               '    }\n' +
               '}'
};

var autocompleteValues = {};
autocompleteValues[1] =
{// LEVEL1
    'robot.':{
        'synonyms':[],
        'values':['goDown()', 'goUp()', 'goLeft()', 'goRight()']
    },
    'scanner.':{
        'synonyms':['robot.getScanner().'],
        'values':[]
    },
    ' == ':{
        'synonyms':[' != '],
        'values':[]
    }
};

autocompleteValues[2] =
{// LEVEL2
    'robot.':{
        'synonyms':[],
        'values':['getScanner()']
    },
    'scanner.':{
        'synonyms':['robot.getScanner().'],
        'values':['atRight()', 'atLeft()', 'atUp()', 'atDown()']
    },
    ' == ':{
        'synonyms':[' != '],
        'values':['\'WALL\'']
    }
};

autocompleteValues[3] =
{// LEVEL3
    'robot.':{
        'synonyms':[],
        'values':['getMemory()']
    },
    'scanner.':{
        'synonyms':['robot.getScanner().'],
        'values':[]
    },
    ' == ':{
        'synonyms':[' != '],
        'values':['\'RIGHT\'', '\'DOWN\'', '\'LEFT\'', '\'UP\'']
    },
    'memory.':{
        'synonyms':['robot.getMemory().'],
        'values':['save()', 'is()', 'load()', 'remove()', 'clear()']
    }
};

autocompleteValues[5] =
{// LEVEL5
    'robot.':{
        'synonyms':[],
        'values':['cameFrom()']
    },
    'scanner.':{
        'synonyms':['robot.getScanner().'],
        'values':[]
    },
    ' == ':{
        'synonyms':[' != '],
        'values':[]
    },
    'memory.':{
        'synonyms':['robot.getMemory().'],
        'values':[]
    }
};

autocompleteValues[10] =
{// LEVEL B
    'robot.':{
        'synonyms':[],
        'values':[]
    },
    'scanner.':{
        'synonyms':['robot.getScanner().'],
        'values':['getGold()', 'getExit()', 'getFinish()', 'getShortestWay()', 'getMe()']
    },
    ' == ':{
        'synonyms':[' != '],
        'values':[]
    },
    'memory.':{
        'synonyms':['robot.getMemory().'],
        'values':[]
    }
};

autocompleteValues[11] =
{// LEVEL C
    'robot.':{
        'synonyms':[],
        'values':['goOverHole()', 'jumpLeft()', 'jumpRight()', 'jumpUp()', 'jumpDown()']
    },
    'scanner.':{
        'synonyms':['robot.getScanner().'],
        'values':[]
    },
    ' == ':{
        'synonyms':[' != '],
        'values':['\'HOLE\'']
    },
    'memory.':{
        'synonyms':['robot.getMemory().'],
        'values':[]
    }
};