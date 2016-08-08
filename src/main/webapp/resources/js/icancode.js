/*-
 * #%L
 * iCanCode - it's a dojo-like platform from developers to developers.
 * %%
 * Copyright (C) 2016 EPAM
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
    initAutocomplete();
};

var initAutocomplete = function() {
    var data;
    icancodeMaps = {};

    for(var iLevel = 0; iLevel <= currentLevel; ++iLevel) {
        if (!getLevelInfo || !getLevelInfo(iLevel).hasOwnProperty('autocomplete')) {
            continue;
        }

        data = getLevelInfo(iLevel).autocomplete;

        for(var index in data) {
            if (!data.hasOwnProperty(index)) {
                continue;
            }

            if (icancodeMaps.hasOwnProperty(index)) {
                icancodeMaps[index] = icancodeMaps[index].concat(data[index].values);
            } else {
                icancodeMaps[index] = data[index].values;
            }

            for(var isynonym = 0; isynonym < data[index].synonyms.length; ++isynonym) {
                icancodeMaps[data[index].synonyms[isynonym]] = icancodeMaps[index];
            }
        }
    }
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

            'js/game/point.js',
            'js/game/direction.js',
            'js/game/elements.js',
            'js/game/board.js',

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
                mouseWheel : { enable : true }
            });

            // ----------------------- init progressbar scrollbar -------------------
            $(".trainings").mCustomScrollbar({
              scrollButtons:{ enable: true },
                theme:"dark-2",
                axis: "x"
            });

            var scrollProgress = function() {
                $(".trainings").mCustomScrollbar("scrollTo", ".level-current");
            }

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
              
                scrollProgress();
            });
            if (game.demo) {
                var data = '{"' + game.playerName + '":{"board":"{\\"levelProgress\\":{\\"total\\":18,\\"current\\":3,\\"lastPassed\\":2,\\"multiple\\":false},\\"layers\\":[\\"OOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOCDDDDEOOOOOOOOOOJaBB9FOOOOOOOOOOIHHHHGOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOO\\",\\"AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAPAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA\\"]}","gameName":"icancode","score":150,"maxLength":0,"length":0,"level":1,"boardSize":16,"info":"","scores":"{\\"fasdfddd@gmail.com\\":150,\\"SDAsd@sas.as\\":2250}","coordinates":"{\\"fasdfddd@gmail.com\\":{\\"y\\":8,\\"x\\":9},\\"SDAsd@sas.as\\":{\\"y\\":8,\\"x\\":9}}"}}';
                $('body').trigger('board-updated', JSON.parse(data));
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
                        previousDirection : function() {
                            if (goThere == null) {
                                return null;
                            }

                            return goThere;
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

// ----------------------- get level info -------------------
var getLevelInfo = function(level) {
    if (!level) {
        level = currentLevel + 1;
    }

    var result = levelInfo[level];
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

levelInfo[1] = {
        'help':'Robot asks for new orders every second. He should know where to go.<br>' +
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
               'If something is wrong - check Robot message in the Console (the rightmost field).<br>' +
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
               '}',
        'autocomplete': {
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
        }
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
               '}',
        'autocomplete': {
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
        }
    };

levelInfo[3] = {
         'help':'This Maze is very similar to the previous. But it’s not so easy.<br>' +
                'Try to solve it by adding new IF.<br>' +
                'You can use new methods for refactoring:<br>' +
                '<pre>scanner.at("RIGHT");\n' +
                'robot.go("LEFT");</pre>' +
                'If you want to know where we came from - use this expression:<br>' +
                '<pre>robot.cameFrom() == "LEFT"</pre>' +
                'If you want to know where we came to on our previous step, use:<br>' +
                '<pre>robot.previousDirection() == "RIGHT"</pre>' +
                'You can use these commands with previous to tell robot to go on one direction, like:<br>' +
                '<pre>robot.go(robot.comeTo());</pre>' +
                'Be careful! The program should work for all previous levels too.',
         'defaultCode':levelInfo[2].winCode,
         'winCode':'function program(robot) {\n' +
                '    var scanner = robot.getScanner();\n' +
                '    if (robot.cameFrom() != null) {\n' +
                '        robot.go(robot.previousDirection());\n' +
                '    } else {\n' +
                '        if (scanner.atRight() != "WALL") {\n' +
                '            robot.goRight();\n' +
                '        } else if (scanner.atDown() != "WALL") {\n' +
                '            robot.goDown();\n' +
                '        } else {\n' +
                '            robot.goLeft();\n' +
                '        }\n' +
                '    }\n' +
                '}',
        'autocomplete':{
            'robot.':{
                'synonyms':[],
                'values':['cameFrom()', 'previousDirection()']
            },
            'scanner.':{
                'synonyms':['robot.getScanner().'],
                'values':[]
            },
            ' == ':{
                'synonyms':[' != '],
                'values':['\'RIGHT\'', '\'DOWN\'', '\'LEFT\'', '\'UP\'', 'null']
            }
        }
    };

levelInfo[4] = {
        'help':'Try to solve it by adding new IF. Now it should be easy!<br>' +
               'After solving try to refactor this code.<br>' +
               'You can use new methods for refactoring:<br>' +
               '<pre>scanner.at("RIGHT");\n' +
               'robot.go("LEFT");</pre>' +
               'You can use new FOR construction:<br>' +
               '<pre>var directions = ["RIGHT", "DOWN", "LEFT", "UP"];\n' +
               'for (var index in directions) {\n' +
               '    var direction = directions[index];\n' +
               '    // do something\n' +
               '}</pre>' +
               'Be careful! The program should work for all previous levels too.',
        'defaultCode':levelInfo[3].winCode,
        'winCode':'function program(robot) {\n' +
               '    var scanner = robot.getScanner();\n' +
               '    if (robot.cameFrom() != null) {\n' +
               '        robot.go(robot.previousDirection());\n' +
               '    } else {\n' +
               '        if (scanner.atRight() != "WALL") {\n' +
               '            robot.goRight();\n' +
               '        } else if (scanner.atDown() != "WALL") {\n' +
               '            robot.goDown();\n' +
               '        } else if (scanner.atLeft() != "WALL") {\n' +
               '            robot.goLeft();\n' +
               '        } else {\n' +
               '            robot.goUp();\n' +
               '        }\n' +
               '    }\n' +
               '}',
        'refactoringCode':'function program(robot) {\n' +
               '    var scanner = robot.getScanner();\n' +
               '    if (robot.cameFrom() != null) {\n' +
               '        robot.go(robot.previousDirection());\n' +
               '        return;\n' +
               '    }\n' +
               '    \n' +
               '    var directions = ["RIGHT", "DOWN", "LEFT", "UP"];\n' +
               '    for (var index in directions) {\n' +
               '        var direction = directions[index];\n' +
               '        if (scanner.at(direction) != "WALL") {\n' +
               '            robot.go(direction);\n' +
               '        }\n' +
               '    }\n' +
               '}'
    };

levelInfo[5] = {
        'help':'Oops! Looks like we didn’t predict this situation.<br>' +
               'Think how to adapt the code to these new conditions.<br>' +
               'Use refactoring to make your code more abstract.<br>' +
               'Уou can complicate IF conditions by using operators AND/OR/NOT:<br>' +
               '<pre>if (variable1 && !variable2 || variable3) {\n' +
               '    // this code wull run IF\n' +
               '    //          variable1 IS true AND variable2 IS true\n' +
               '    //       OR variable3 IS true (ignoring variable1, variable2)\n' +
               '}</pre>' +
               'These operators allow you to use any combination.<br>' +
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
               'If you want to save value during program working - use Robot\'s memory.<br>' +
               'You can use this method to show data in console:<br>' +
               '<pre>var someVariable = "someData";\n' +
               'robot.log(someVariable);</pre>' +
               'Remember! Your program should work for all previous levels too.',
        'defaultCode':levelInfo[4].refactoringCode,
        'winCode':'function program(robot) {\n' +
               '    var scanner = robot.getScanner();\n' +
               '    if (robot.cameFrom() != null &&\n' +
               '        scanner.at(robot.previousDirection()) != "WALL")\n' +
               '    {\n' +
               '        robot.go(robot.previousDirection());\n' +
               '    } else {\n' +
               '        robot.go(freeDirection(scanner, robot));\n' +
               '    }\n' +
               '}\n' +
               '\n' +
               'function freeDirection(scanner, robot) {\n' +
               '    var directions = ["RIGHT", "DOWN", "LEFT", "UP"];\n' +
               '    for (var index in directions) {\n' +
               '        var direction = directions[index];\n' +
               '        if (direction == robot.cameFrom()) {\n' +
               '            continue;\n' +
               '        }\n' +
               '        if (scanner.at(direction) != "WALL") {\n' +
               '            return direction;\n' +
               '        }\n' +
               '    }\n' +
               '    return null;\n' +
               '}'
    };

levelInfo[6] = {
        'help':'You should check all cases.<br>' +
        'Remember! Your program should work for all previous levels too.',
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
        'help':'This is final LevelA Maze. Good luck!<br>' +
         'Remember! Your program should work for all previous levels too.',
        'defaultCode':levelInfo[8].defaultCode,
        'winCode':levelInfo[8].winCode
    };

levelInfo[10] = { // LEVELB
        'help':'You can use new methods for the scanner:<br>' +
               '<pre>var destinationPoints = scanner.getGold();\n' +
               'var nextPoint = scanner.getShortestWay(destinationPoints[0]);\n' +
               'var exitPoint = scanner.getExit();\n' +
               'var robotPoint = scanner.getMe();</pre>' +
               'Try to collect all the golden bags in the Maze.' +
               'Remember! Your program should work for all previous levels too.',
        'defaultCode':'function program(robot) {\n' +
               '    var scanner = robot.getScanner();\n' +
               '    var dest = scanner.getGold();\n' +
               '    var next = scanner.getShortestWay(dest[0]);\n' +
               '    var exit = scanner.getExit();\n' +
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
               '}',
        'autocomplete':{
            'robot.':{
                'synonyms':[],
                'values':[]
            },
            'scanner.':{
                'synonyms':['robot.getScanner().'],
                'values':['getGold()', 'getExit()', 'getExit()', 'getShortestWay()', 'getMe()']
            },
            ' == ':{
                'synonyms':[' != '],
                'values':[]
            },
        }
    };

levelInfo[11] = { // LEVELC
        'help':'In this case, we have Holes. Robot will fall down, if you won’t avoid it.<br>' +
               'You can use this method to detect Holes:<br>' +
               '<pre>var scanner = robot.getScanner();\n' +
               'if (scanner.at("LEFT") == "HOLE") {\n' +
               '    // some statement here\n' +
               '}</pre>' +
               'And these new methods for jumping through it:<br>' +
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
               '}',
        'autocomplete':{
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
        }
};