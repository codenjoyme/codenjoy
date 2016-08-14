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
/**
 * Created by Mikhail_Udalyi on 08.08.2016.
 */

var currentLevel = -1;

var boardPageLoad = function() {
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
    $(".content").mCustomScrollbar({
        theme:'dark-2',
        axis: 'yx',
        mouseWheel : { enable : true }
    });

    // ----------------------- init progressbar scrollbar -------------------
    var scrollProgress = function() {
        $(".trainings").mCustomScrollbar("scrollTo", ".level-current");
    }

    // ----------------------- init ace editor -------------------
    var editor = initEditor(libs, 'ide-block', autocomplete);
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
    var progressBar = initProgressbar('progress-bar');
    progressBar.setProgress = function(current, lastPassed) {
        for (var i = 0; i <= lastPassed; ++i) {
            this.done(i);
        }
        this.process(lastPassed + 1);
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

    // ----------------------- init tooltip -------------------
    $('[data-toggle="tooltip"]').tooltip();

    var oldLastPassed = 0;
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

        if (oldLastPassed < lastPassed) {
            oldLastPassed = lastPassed;
            showWinWindow();
        }

        if (currentLevel == level) {
            return;
        }
        currentLevel = level;
        initAutocomplete(level);

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

    // ----------------------- win window --------------
    var showWinWindow = function() {
        $("#modal-level").removeClass("close");
    };

    var hideWinWindow = function() {
        $("#modal-level").addClass("close");
    };

    var setupWinWindow = function() {
        $("#close-level-modal").click(function(){
            hideWinWindow();
        });
        $("#next-level-modal").click(function(){
            hideWinWindow();
        });
        $("#previous-level-modal").click(function(){
            hideWinWindow();
            $(progressBar[0]).click();
        });
        $("body").keydown(function(event){
            if (event.which == 27){
                $("#close-level-modal").click();
            }
        });
    };
    setupWinWindow();

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
        print('Uploading program...');
        try {
            eval(code);
            controller.storeProgram(program);
        } catch (e) {
            error(e.message);
            print('Please try again.');
            enableAll();
            return;
        }
        onSuccess();
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
};